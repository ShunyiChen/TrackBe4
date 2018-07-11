package com.maxtree.automotive.dashboard.view.front;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.Status;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.component.TimeAgo;
import com.maxtree.automotive.dashboard.data.Area;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.domain.SendDetails;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.vaadin.data.Binder;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.schlichtherle.license.LicenseContent;

/**
 * 
 * @author chens
 *
 */
@SuppressWarnings("serial")
public final class FrontView extends Panel implements View, FrontendViewIF {
    
	private static final Logger log = LoggerFactory.getLogger(FrontView.class);
	
    public FrontView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        root = new VerticalLayout();
        root.setWidth("100%");
        root.setHeight("100%");
        root.setSpacing(false);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);
        root.addComponent(buildHeader());
        root.addComponent(buildSparklines());
        main.addStyleName("dynamicallyVLayout");
        main.setWidth("100%");
        main.setHeight("100%");
        main.addComponents(blankLabel);
        main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
        root.addComponents(main);
        root.setExpandRatio(main, 7.0f);
        loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
            }
        });
        
        
        
        // 开启轮询事件提醒
        startPolling();
        
        ui.access(new Runnable() {
            @Override
            public void run() {
            	
            	
            	Callback2 verifyEvent = new Callback2() {
					@Override
					public void onSuccessful(Object... objects) {
						// fails
						if (objects[0].equals("")) {
							
							LicenseHasExpiredWindow.open("您的许可证已经过期，请联系管理员及时更新许可证文件。", new Callback() {

								@Override
								public void onSuccessful() {
									DashboardEventBus.post(new DashboardEvent.UserLoggedOutEvent());
								}
				        	});
							
						} else {
							
							LicenseContent content = (LicenseContent) objects[0];
					        long endTimes = content.getNotAfter().getTime();
					        long times = new Date().getTime();
					        long minusTimes = endTimes - times;
							
					        int remainingDays = (int) (minusTimes / (1000 * 60 * 60 * 24));
					        if (remainingDays <= 15) {
					        	LicenseHasExpiredWindow.open("您的授权证书有效期剩余 "+remainingDays+" 天，请联系管理员及时更新许可文件。", null);
					        }
						}
					}
				};
				new Test().verify(verifyEvent);
            	
            	
            }
        });
    }
    
    /**
     * 
     */
	private void startPolling() {
		SystemConfiguration sc = Yaml.readSystemConfiguration();
		ui.setPollInterval(sc.getPollinginterval() * 1000);
		ui.addPollListener(new UIEvents.PollListener() {
			@Override
			public void poll(UIEvents.PollEvent event) {
				getUnreadCount();
			}
		});
	}

    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);
        return sparks;
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("扫描录入");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        buildPrintButton();
        buildNotificationsButton();
        buildAddButton();
        buildCommitButton();
        HorizontalLayout tools = new HorizontalLayout(btnPrint, btnAdd, btnCommit, notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private void openNotificationsPopup(final ClickEvent event) {
    	VerticalLayout mainVLayout = new VerticalLayout();
    	mainVLayout.setSpacing(false);
        
        Label title = new Label("事件提醒");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        mainVLayout.addComponent(title);

        Panel scrollPane = new Panel();
    	scrollPane.addStyleName("reminder-scrollpane");
    	scrollPane.setHeight("220px");
    	scrollPane.setWidth("100%");
        VerticalLayout listLayout = new VerticalLayout();
        
        List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(loggedInUser, DashboardViewType.DASHBOARD.getViewName());
        for (Map<String, Object> m : allMessages) {
        	
        	int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
        	VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.setMargin(false);
            notificationLayout.setSpacing(false);
            notificationLayout.addStyleName("notification-item");
            String readStr = m.get("markedasread").toString().equals("0")?"(未读)":"(已读)";
            Label titleLabel = new Label(m.get("subject")+readStr);
            titleLabel.addStyleName("notification-title");
            
            Date dateCreated = (Date) m.get("datecreated");
            long duration = new Date().getTime() - dateCreated.getTime();
            Label timeLabel = new Label();
            timeLabel.setValue(new TimeAgo().toDuration(duration));
            timeLabel.addStyleName("notification-time");
            String json = m.get("messagebody").toString();
            Map<String, String> map = new MessageBodyParser().json2Map(json);
            String type = map.get("type").toString();
            String messageContent = map.get("message");
            Label contentLabel = new Label(messageContent);
            contentLabel.addStyleName("notification-content");

//            // 自动删除已过时的消息
//            if ("transaction".equals(type)) {
//            	int transId = Integer.parseInt(map.get("transactionUniqueId").toString());
//            	Transaction trans = ui.transactionService.findById(transId);
//	            long time1 = trans.getDateModified().getTime();
//				long time2 = dateCreated.getTime();
//				if (time1 > time2) {
//					ui.messagingService.deleteMessageRecipient(messageUniqueId, currentUser.getUserUniqueId());
//					continue;
//				}
//            }
            
            notificationLayout.addComponents(titleLabel, timeLabel, contentLabel);
            listLayout.addComponent(notificationLayout);
            notificationLayout.addStyleName("switchbutton");
            notificationLayout.addLayoutClickListener(e -> {
            	
            	notificationsWindow.close();
            	if ("text".equals(type)) {
            		
            		 showAll(allMessages, messageUniqueId);
            		 
    			} else if ("transaction".equals(type)) {
//    				String senderUserName = m.get("username").toString();
//    				String senderPicture = "../VAADIN/themes/dashboard/"+ m.get("picture").toString();
//    				String subject = m.get("subject").toString();
    				int transactionUniqueId = 0;
//    				String status = null;
    				if (type.equals("transaction")) {
    					transactionUniqueId = Integer.parseInt(map.get("transactionUniqueId").toString());
//    					status = map.get("status").toString();
    				}
//    				String read = m.get("read").toString().equals("1")?"已读":"未读";
//    				MessageWrapper wrapper = new MessageWrapper(messageUniqueId, senderPicture+" "+senderUserName, senderPicture, subject, messageContent, transactionUniqueId, read, dateCreated, type, status);
    				// 标记已读
    				ui.messagingService.markAsRead(messageUniqueId, loggedInUser.getUserUniqueId());
    				
    				CacheManager.getInstance().refreshSendDetailsCache();
    				
    				getUnreadCount();
    				
    				openTransaction(transactionUniqueId, dateCreated);
    				
    			}
            });
        }
        
        scrollPane.setContent(listLayout);
        mainVLayout.addComponent(scrollPane);
        mainVLayout.setExpandRatio(scrollPane, 0.9f);
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        footer.setSpacing(false);
        Button showAll = new Button("查看全部事件");
        showAll.addClickListener(e->{

        	notificationsWindow.close();
        	showAll(allMessages, 0);
        	
        });
        
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        mainVLayout.addComponent(footer);
        mainVLayout.setExpandRatio(footer, 0.1f);
        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
            notificationsWindow.setContent(mainVLayout);
        } else {
        	notificationsWindow.setContent(mainVLayout);
        }
        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    	getUnreadCount();
    }

    public static final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(VaadinIcons.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            DashboardEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(NotificationsCountUpdatedEvent event) {
        	log.info("===============DashboardView Polling");
        	DashboardMenu.getInstance().updateNotificationsCount(event.getCount());
        	setUnreadCount(event.getCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "事件提醒";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " 未执行)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }
    
    /**
     * 
     * @param allMessages
     * @param selectedMessageUniqueId
     */
    private void showAll(List<Map<String, Object>> allMessages, int selectedMessageUniqueId) {
    	Callback2 event = new Callback2() {
			@Override
			public void onSuccessful(Object... objects) {
				getUnreadCount();
			}
    	};
    	MessageInboxWindow.open(allMessages, event, selectedMessageUniqueId);
    }
    
    /**
     * 
     */
    private void resetComponents() {
    	main.removeAllComponents();
    	main.setHeightUndefined();
    	
//    	VerticalLayout leftChild = new VerticalLayout();
//    	leftChild.setSizeFull();
//    	leftChild.setSpacing(true);
//    	leftChild.setMargin(false);
//    	leftChild.addComponents(topGrid, bottomGrid);
//    	Panel captureStage = new Panel("拍照");
//    	captureStage.setSizeFull();
//    	captureStage.setHeight("612px");
//    	captureStage.addStyleName("picture-pane");
    	spliterSouth.setSizeFull();
    	spliterSouth.addComponents(fileGrid, capturePane);
    	
    	spliterNorth.setSizeFull();
    	spliterNorth.addComponents(basicInfoPane, businessTypePane);
    	spliterNorth.setExpandRatio(basicInfoPane, 3);
    	spliterNorth.setExpandRatio(businessTypePane, 1);
    	main.addComponents(spliterNorth, spliterSouth);
    	
    	
    	main.setExpandRatio(spliterNorth, 1);
    	main.setExpandRatio(spliterSouth, 9);
    	
    }
    
    /**
     * 
     */
    private void buildPrintButton() {
    	btnPrint.setEnabled(true);
    	btnPrint.setId(EDIT_ID);
    	btnPrint.setIcon(VaadinIcons.PRINT);
    	btnPrint.addStyleName("icon-edit");
    	btnPrint.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnPrint.setDescription("查询待补充的记录");
    	btnPrint.addClickListener(e -> {
    		Callback2 callback = new Callback2() {

				@Override
				public void onSuccessful(Object... objects) {
					
					openTransaction(Integer.parseInt(objects[0].toString()), new Date());
				}
    		};
    		SearchAndPrintWindow.open("", callback);
        });
    }
    
    /**
     * 添加按钮
     * 
     * @return
     */
    private void buildAddButton() {
    	btnAdd.setEnabled(true);
    	btnAdd.setId(EDIT_ID);
    	btnAdd.setIcon(VaadinIcons.FILE_ADD);
    	btnAdd.addStyleName("icon-edit");
    	btnAdd.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnAdd.setDescription("创建或补充流水号");
    	btnAdd.addClickListener(e -> {
    		createTransaction();
        });
    }
    
    /**
     * 提交按钮 
     * 
     * @return
     */
    private void buildCommitButton() {
    	btnCommit.setId(EDIT_ID);
        btnCommit.setIcon(VaadinIcons.CLOUD_UPLOAD);
        btnCommit.addStyleName("icon-edit");
        btnCommit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnCommit.setDescription("保存并提交给质检");
        btnCommit.addClickListener(e -> {
        	if (editableTrans == null) {
        		Notifications.warning("没有可提交的业务。");
        	} else {
        		commitTransaction();
        	}
        });
    }
    
    /**
     * 提醒按钮
     * 
     * @return
     */
    private void buildNotificationsButton() {
        notificationsButton = new NotificationsButton();
    	notificationsButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                openNotificationsPopup(event);
            }
        });
    }
    
    /**
     * 
     */
    private void createTransaction() {
    	if (editableTrans == null) {
    		editableTrans = new Transaction();
    		try {
				Yaml.deleteUploadParameters(loggedInUser.getUserUniqueId());
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    		int companyUniqueId = loggedInUser.getCompanyUniqueId();
        	int communityUniqueId = loggedInUser.getCommunityUniqueId();
        	if (companyUniqueId == 0) {
        		Notifications.warning("当前用户没有加入任何机构，请联系管理员进行分配。");
        		return;
        	}
        	if (communityUniqueId == 0) {
        		Notifications.warning("当前用户没有加入任何社区，请联系管理员进行分配。");
        		return;
        	}
        	editableCompany = ui.companyService.findById(companyUniqueId);
        	List<Site> allSites = ui.siteService.getSites(communityUniqueId);
        	for (int i = 0; i < allSites.size(); i++) {
        		if (allSites.get(i).getRunningStatus() == 1) {
        			editableSite = allSites.get(i);
        			break;
        		}
        	}
        	if (editableSite == null) {
        		Notifications.warning("当前用户所在的社区不存在文件站点，或站点已关闭。请联系管理员进行设置。");
        		return;
        	}
    		//创建UUID
        	uuid = UUID.randomUUID().toString();
    		//如果站点文件夹装满则提醒用户
        	batch = ui.siteService.updateFolders(editableSite);
        	if (batch == 0) {
        		Notifications.warning("当前站点-"+editableSite.getSiteName()+"已满。请联系管理员进行重新分配。");
        		editableTrans = null;
        		editableSite = null;
        		return;
        	}
        	
        	Area area = Yaml.readArea();
        	editableTrans = new Transaction();
        	editableTrans.setBarcode("");
        	editableTrans.setPlateType("");
        	editableTrans.setPlateNumber(area.getLicenseplate());
        	editableTrans.setVin("");
        	basicInfoPane.setFieldValues(editableTrans);
        	
        	// validating the transaction information
        	basicInfoPane.validatingFieldValues(binder);
        	binder.setBean(editableTrans);
        	
        	resetComponents();
    	}
    	else {
    		Notifications.warning("请提交当前业务再新建。");
    	}
    }
    
    /**
     * 
     * @param transactionUniqueId
     * @param messageDateCreated
     */
    private void openTransaction(int transactionUniqueId, Date messageDateCreated) {
//    	// 1.取得业务
//    	editableTrans = ui.transactionService.findById(transactionUniqueId);
//		if (editableTrans != null) {
//			long time1 = editableTrans.getDateModified().getTime();
//			long time2 = messageDateCreated.getTime();
//			if (time1 > time2) {
//				Notifications.warning("该业务已过时。");
//				return;
//			}
//			// 2.重置文本框
//			this.resetComponents();
//			
//			// 初始化更改信息
//			User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//			editableCompany = ui.companyService.findById(loginUser.getCompanyUniqueId());
//			editableType.setSelected(editableTrans.getBusinessUniqueId());
//			editableType.setSelectorEnabled(false);
//			informationGrid.setFieldValues(editableTrans);
//			editableSite = ui.siteService.findById(editableTrans.getSiteUniqueId());
//			uuid = editableTrans.getUuid();
//			
//			// 获取主要材料
//			Business business = editableType.getSelector().getValue();
//			List<Document> documentList = ui.documentService.findPrimary(uuid, business.getBusinessUniqueId());
//			List<Document> filledDocumentList = new ArrayList<Document>();
//			for (DataItem item : business.getItems()) {
//				Document doc = existCheck(item.getItemName(), documentList);
//				if (doc == null) {
//					doc = new Document();
//					doc.setAlias(item.getItemName());
//					doc.setFileName("");
//					doc.setFileFullPath("");
//					doc.setCategory(0); 
//        			// 文件类别0：主要图片,1：次要图片
//					doc.setBusinessUniqueId(business.getBusinessUniqueId());
//					doc.setUuid(editableTrans.getUuid());
//				}
//				filledDocumentList.add(doc);
//			}
//			// 加载主要材料
//    		primaryGrid.addUploadCells(editableSite, filledDocumentList.toArray(new Document[filledDocumentList.size()]));
//
//    		// 初始化次要材料
//    		secondaryGrid.setBusinessUniqueId(business.getBusinessUniqueId());
//    		secondaryGrid.setEnabledForUploadComponent(true);//如果业务类型已选，则可以启用选择上传组件
//    		secondaryGrid.setSite(editableSite);
//    		secondaryGrid.setUuid(uuid);
//        	List<Document> doc2 = ui.documentService.findSecondary(uuid, business.getBusinessUniqueId());
//        	// 加载次要材料
//        	secondaryGrid.addUploadCells(editableSite, doc2.toArray(new Document[doc2.size()]));
//        	
//        	
//        	if (editableTrans.getStatus().equals(Status.S4.name)) {
//  	    		
//  	    		// 打印文件标签和车辆标签
//  	    		PrintingConfirmationWindow.open("打印确认", editableTrans.getTransactionUniqueId()); 
//  	    		
//  	    	} else if (editableTrans.getStatus().equals(Status.ReturnedToThePrint.name) 
//  	    			|| editableTrans.getStatus().equals(Status.S3.name)) {
//  	    		// 打印审核结果单
//  	    		PrintingResultsWindow.open("打印确认", editableTrans.getTransactionUniqueId()); 
//  	    	}
//		}
    }
    
    /**
     * 
     */
    public void commitTransaction() {
//		Business business = editableType.getSelector().getValue();
//		if (!basicInfo.checkEmptyValues() || business == null) {
//			Notifications.warning("请将信息输入完整。");
//			return;
//		}
//		if (!primaryGrid.checkUploads()) {
//			Notifications.warning("请将主要材料上传完整。");
//			return;
//		}
    	
    	basicInfoPane.assignValues(editableTrans);
//    	editableTrans.setSiteUniqueId(editableSite.getSiteUniqueId());
    	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
    	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
    	editableTrans.setProvince(editableCompany.getProvince());
    	editableTrans.setCity(editableCompany.getCity());
    	editableTrans.setPrefecture(editableCompany.getPrefecture());
    	editableTrans.setDistrict(editableCompany.getDistrict());
    	editableTrans.setSite(editableSite);
    	editableTrans.setDateModified(new Date());
//    	editableTrans.setBusinessUniqueId(business.getBusinessUniqueId());
    	editableTrans.setTypist(loggedInUser.getUserUniqueId());
    	// Insert new transaction
    	if (editableTrans.getTransactionUniqueId() == 0) {
    		
    		editableTrans.setStatus(Status.S1.name);
    		
    		int transactionUniqueId = ui.transactionService.create(editableTrans);
        	editableTrans.setTransactionUniqueId(transactionUniqueId);
    	}
    	else {
    		ui.transactionService.update(editableTrans);
    	}
    	
    	// 添加到质检队列
		Queue newQueue = new Queue();
		newQueue.setTransactionUniqueId(editableTrans.getTransactionUniqueId());
		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
		newQueue.setSentByUser(loggedInUser.getUserUniqueId());	// 发送者
		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
		ui.queueService.create(newQueue, serial);
    	
		// 清空舞台
		cleanStage();
		
		// 提示信息
		Notification success = new Notification("信息提交成功！");
		success.setDelayMsec(2000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
		
		// 清空编辑变量
		editableTrans = null;
		editableCompany = null;
		editableSite = null;
    }
    
	@Override
	public void cleanStage() {
		main.removeAllComponents();
		main.setHeight("100%");
		main.addComponents(blankLabel);
		main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void getUnreadCount() {
		List<SendDetails> sendDetailsList = CacheManager.getInstance().getSendDetailsCache().asMap().get(loggedInUser.getUserUniqueId());
		int unreadCount = 0;
		for (SendDetails sd : sendDetailsList) {
			
			if (sd.getViewName().equals(DashboardViewType.DASHBOARD.getViewName())
					|| sd.getViewName().equals("")) {
				unreadCount++;
			}
		}
		NotificationsCountUpdatedEvent event = new DashboardEvent.NotificationsCountUpdatedEvent();
		event.setCount(unreadCount);
		notificationsButton.updateNotificationsCount(event);
	}
    
	public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";
	private Transaction editableTrans = null; 	//可编辑的编辑transaction
	private Company editableCompany = null;	 	//前台所在机构
	
	public User loggedInUser;	//登录用户
	public Site editableSite;	//业务站点
	public String uuid;	//UUID业务与原文关联号
	public int batch;	//业务批次号。默认最大1000个批次，每批次最多放5000文件夹。
	public String vin;	//车辆识别代码。用于分表。
	public String businessCode; //业务CODE
	
    private Label titleLabel;
    private Window notificationsWindow;
    private VerticalLayout root;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private Binder<Transaction> binder = new Binder<>();
    
    private BasicInfoPane basicInfoPane = new BasicInfoPane(this);
    private BusinessTypePane businessTypePane = new BusinessTypePane(this);
    public ThumbnailGrid fileGrid = new ThumbnailGrid(this);
    public CapturePane capturePane = new CapturePane(this);
    
    private Button btnPrint = new Button();
    private Button btnAdd = new Button();
    private Button btnCommit = new Button();
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
    private HorizontalLayout spliterNorth = new HorizontalLayout();
    private HorizontalLayout spliterSouth = new HorizontalLayout();
}
