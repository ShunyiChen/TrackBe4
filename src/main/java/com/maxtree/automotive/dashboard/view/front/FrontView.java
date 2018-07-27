package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.Actions;
import com.maxtree.automotive.dashboard.BusinessState;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.component.TimeAgo;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.domain.SendDetails;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.domain.UserEvent;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.servlet.UploadFileServlet;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.vaadin.data.Binder;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
public final class FrontView extends Panel implements View,InputViewIF {
    
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
		ui.setPollInterval(sc.getPollinginterval());
		ui.addPollListener(new UIEvents.PollListener() {
			@Override
			public void poll(UIEvents.PollEvent event) {
				updateUnreadCount();
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
        
        List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(loggedInUser, DashboardViewType.INPUT.getViewName());
        for (Map<String, Object> m : allMessages) {
        	VerticalLayout vLayout = new VerticalLayout();
        	vLayout.setMargin(false);
        	vLayout.setSpacing(false);
        	vLayout.addStyleName("notification-item");
            Label timeLabel = new Label();
            String readStr = m.get("markedasread").toString().equals("0")?"(未读)":"";
            Label titleLabel = new Label(m.get("subject")+readStr);
            titleLabel.addStyleName("notification-title");
            String json = m.get("messagebody").toString();
            Map<String, String> map = jsonHelper.json2Map(json);
            Label plateNumber = new Label(map.get("4"));//PLATENUMBER
            String vin = map.get("5");
            String uuid = map.get("7");
            plateNumber.addStyleName("notification-content");
            
            Date dateCreated = (Date) m.get("datecreated");
            long duration = new Date().getTime() - dateCreated.getTime();
            timeLabel.setValue(new TimeAgo().toDuration(duration));
            timeLabel.addStyleName("notification-time");
            vLayout.addComponents(titleLabel, timeLabel, plateNumber);
            listLayout.addComponent(vLayout);
            vLayout.addStyleName("switchbutton");
            vLayout.addLayoutClickListener(e -> {
            	notificationsWindow.close();
            	
            	if(editableTrans == null) {
        			editMode = 1;//进入更新模式
        			openTransaction(uuid, vin);
        		}
        		else {
        			Notifications.warning("请确保完成当前任务，再执行下一操作。");
        		}
            });
        	
        	
//        	int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
//        	VerticalLayout notificationLayout = new VerticalLayout();
//            notificationLayout.setMargin(false);
//            notificationLayout.setSpacing(false);
//            notificationLayout.addStyleName("notification-item");
//            String readStr = m.get("markedasread").toString().equals("0")?"(未读)":"(已读)";
//            Label titleLabel = new Label(m.get("subject")+readStr);
//            titleLabel.addStyleName("notification-title");
//            
//            Date dateCreated = (Date) m.get("datecreated");
//            long duration = new Date().getTime() - dateCreated.getTime();
//            Label timeLabel = new Label();
//            timeLabel.setValue(new TimeAgo().toDuration(duration));
//            timeLabel.addStyleName("notification-time");
//            String json = m.get("messagebody").toString();
//            Map<String, String> map = new MessageBodyParser().json2Map(json);
//            String type = map.get("type").toString();
//            String messageContent = map.get("message");
//            Label contentLabel = new Label(messageContent);
//            contentLabel.addStyleName("notification-content");
//
//            
//            notificationLayout.addComponents(titleLabel, timeLabel, contentLabel);
//            listLayout.addComponent(notificationLayout);
//            notificationLayout.addStyleName("switchbutton");
//            notificationLayout.addLayoutClickListener(e -> {
//            	
//            	notificationsWindow.close();
//            	if ("text".equals(type)) {
//            		
//            		 showAll(allMessages, messageUniqueId);
//            		 
//    			} else if ("transaction".equals(type)) {
////    				String senderUserName = m.get("username").toString();
////    				String senderPicture = "../VAADIN/themes/dashboard/"+ m.get("picture").toString();
////    				String subject = m.get("subject").toString();
//    				int transactionUniqueId = 0;
////    				String status = null;
//    				if (type.equals("transaction")) {
//    					transactionUniqueId = Integer.parseInt(map.get("transactionUniqueId").toString());
////    					status = map.get("status").toString();
//    				}
////    				String read = m.get("read").toString().equals("1")?"已读":"未读";
////    				MessageWrapper wrapper = new MessageWrapper(messageUniqueId, senderPicture+" "+senderUserName, senderPicture, subject, messageContent, transactionUniqueId, read, dateCreated, type, status);
//    				// 标记已读
//    				ui.messagingService.markAsRead(messageUniqueId, loggedInUser.getUserUniqueId());
//    				
//    				CacheManager.getInstance().refreshSendDetailsCache();
//    				
//    				getUnreadCount();
//    				
////    				openTransaction(transactionUniqueId, dateCreated);
//    				
//    			}
//            });
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
//    	updateUnreadCount();
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
//				getUnreadCount();
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
					
//					openTransaction(Integer.parseInt(objects[0].toString()), new Date());
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
    		if(editableTrans == null) {
    			editMode = 0;//进入录入模式
    			startTransaction();
    		}
    		else {
    			Notifications.warning("请确保完成当前任务，再执行下一操作。");
    		}
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
        	// 新建
        	if (editMode == 0) {
        		newTransaction();
        	} 
        	// 更改
        	else if(editMode == 1){
        		updateTransaction();
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
    private void startTransaction() {
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
    	
    	Address addr = Yaml.readAddress();
    	editableTrans = new Transaction();
    	editableTrans.setBarcode("");
    	editableTrans.setPlateType("");
    	editableTrans.setPlateNumber(addr.getLicenseplate());
    	editableTrans.setVin("");
    	basicInfoPane.populate2(editableTrans);
    	
    	// validating the transaction information
    	basicInfoPane.validatingFieldValues(binder);
    	binder.setBean(editableTrans);
    	
    	resetComponents();
    }
    
    /**
     * 
     * @param transUUID
     * @param transVIN
     */
    private void openTransaction(String transUUID, String transVIN) {
    	editableTrans = ui.transactionService.findByUUID(transUUID, transVIN);
    	editableSite = ui.siteService.findByCode(editableTrans.getSiteCode());
    	uuid = editableTrans.getUuid();
    	batch = editableTrans.getBatch();
    	vin = editableTrans.getVin();
    	stoppedAtAnException = false;
    	
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
    	
    	// validating the transaction information
    	basicInfoPane.validatingFieldValues(binder);
    	binder.setBean(editableTrans);
    	
    	resetComponents();
    	
    	basicInfoPane.populate2(editableTrans);
    	businessTypePane.populate(editableTrans.getBusinessCode());
    	
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
    public void newTransaction() {
    	if(!basicInfoPane.emptyChecks()) {
			Notifications.warning("有效性验证失败。");
			return;
    	}
    	if (fileGrid.emptyChecks()) {
			Notifications.warning("请将业务材料上传完整。");
			return;
    	}
    	//4大流程
    	//新车注册流程
    	if (businessTypePane.getSelected().getName().equals("注册登记")) {
    		basicInfoPane.populate(editableTrans);//赋值基本信息
        	editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	String provinceCode = ui.dataItemService.findCodeByName(editableCompany.getProvince());//省份
        	String city = ui.dataItemService.findCodeByName(editableCompany.getCity());//地级市
        	String district = ui.dataItemService.findCodeByName(editableCompany.getDistrict());//市、县级市
        	editableTrans.setLocationCode(provinceCode+""+city+""+district);
        	editableTrans.setBatch(batch);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	editableTrans.setIndexNumber(1);
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 获得社区内的全部机构
        		List<Company> companies = ui.communityService.findAllCompanies(loggedInUser.getCommunityUniqueId());
        		Company com = null;
        		for(int i = 0; i < companies.size(); i++) {
        			com = companies.get(i);
        			if (com.getHasStoreHouse() == 1) {
        				break;
        			}
        		}
        		FrameNumber frame = ui.frameService.getNewCode(com.getStorehouseName());
        		if(StringUtils.isEmpty(frame.getCode())) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(frame.getCode()+"");//上架号
        			ui.frameService.updateVIN(basicInfoPane.getVIN(), frame.getCode());
        		}
        		
        		editableTrans.setStatus(BusinessState.B2.name);
        		ui.transactionService.insert(editableTrans);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		//清空舞台
            	cleanStage();
            	Notifications.info("操作成功。已完成逻辑上架。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(BusinessState.B7.name);
        		ui.transactionService.insert(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.info("操作成功。记录已提交到质检队列等待质检。");
        	}
    	}
    	
    	
    	
    	// 非审档流程
    	else if (businessTypePane.getSelected().getNeedToCheck() == 0) {
    		basicInfoPane.populate(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	String provinceCode = ui.dataItemService.findCodeByName(editableCompany.getProvince());//省份
        	String city = ui.dataItemService.findCodeByName(editableCompany.getCity());//地级市
        	String district = ui.dataItemService.findCodeByName(editableCompany.getDistrict());//市、县级市
        	editableTrans.setLocationCode(provinceCode+""+city+""+district);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findFirstCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(BusinessState.B2.name);
        		ui.transactionService.insert(editableTrans);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		//清空舞台
            	cleanStage();
            	Notifications.info("操作成功。已完成逻辑上架。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(BusinessState.B7.name);
        		ui.transactionService.insert(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.info("操作成功。记录已提交到质检队列等待质检。");
        	}
    	}
    	
    	// 需要审档（一级）流程
    	else if (businessTypePane.getSelected().getNeedToCheck() == 1 && businessTypePane.getSelected().getCheckLevel().equals("一级")) {
    		
    		basicInfoPane.populate(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	String provinceCode = ui.dataItemService.findCodeByName(editableCompany.getProvince());//省份
        	String city = ui.dataItemService.findCodeByName(editableCompany.getCity());//地级市
        	String district = ui.dataItemService.findCodeByName(editableCompany.getDistrict());//市、县级市
        	editableTrans.setLocationCode(provinceCode+""+city+""+district);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findFirstCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(BusinessState.B4.name);
        		ui.transactionService.insert(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		//清空舞台
            	cleanStage();
            	Notifications.info("操作成功。已完成逻辑上架。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(BusinessState.B7.name);
        		ui.transactionService.insert(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.info("操作成功。记录已提交到质检队列等待质检。");
        	}
    		
    	}
    	
    	
    	// 需要审档（二级）流程
    	else if (businessTypePane.getSelected().getNeedToCheck() == 1 && businessTypePane.getSelected().getCheckLevel().equals("二级")) {
    		basicInfoPane.populate(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	String provinceCode = ui.dataItemService.findCodeByName(editableCompany.getProvince());//省份
        	String city = ui.dataItemService.findCodeByName(editableCompany.getCity());//地级市
        	String district = ui.dataItemService.findCodeByName(editableCompany.getDistrict());//市、县级市
        	editableTrans.setLocationCode(provinceCode+""+city+""+district);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findFirstCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(BusinessState.B4.name);
        		ui.transactionService.insert(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		//清空舞台
            	cleanStage();
            	Notifications.info("操作成功。已完成逻辑上架。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(BusinessState.B7.name);
        		ui.transactionService.insert(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.info("操作成功。记录已提交到质检队列等待质检。");
        	}
    	}
    	editableTrans = null;
    }
    
    /**
     * 
     */
    private void updateTransaction() {
    	if(!basicInfoPane.emptyChecks()) {
			Notifications.warning("有效性验证失败。");
			return;
    	}
    	if (fileGrid.emptyChecks()) {
			Notifications.warning("请将业务材料上传完整。");
			return;
    	}
    	
    	//2大流程
    	// 需要审档（一级）流程
    	else if (businessTypePane.getSelected().getNeedToCheck() == 1 && businessTypePane.getSelected().getCheckLevel().equals("一级")) {
    		basicInfoPane.populate(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	String provinceCode = ui.dataItemService.findCodeByName(editableCompany.getProvince());//省份
        	String city = ui.dataItemService.findCodeByName(editableCompany.getCity());//地级市
        	String district = ui.dataItemService.findCodeByName(editableCompany.getDistrict());//市、县级市
        	editableTrans.setLocationCode(provinceCode+""+city+""+district);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findFirstCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(BusinessState.B4.name);
        		ui.transactionService.update(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		//清空舞台
            	cleanStage();
            	Notifications.info("操作成功。本次业务已添加到待审档队列中，等待审档。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(BusinessState.B7.name);
        		ui.transactionService.update(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.info("操作成功。本次业务已提交到质检队列中，等待质检。");
        	}
    		
    	}
    	
    	
    	// 需要审档（二级）流程
    	else if (businessTypePane.getSelected().getNeedToCheck() == 1 && businessTypePane.getSelected().getCheckLevel().equals("二级")) {
    		basicInfoPane.populate(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	String provinceCode = ui.dataItemService.findCodeByName(editableCompany.getProvince());//省份
        	String city = ui.dataItemService.findCodeByName(editableCompany.getCity());//地级市
        	String district = ui.dataItemService.findCodeByName(editableCompany.getDistrict());//市、县级市
        	editableTrans.setLocationCode(provinceCode+""+city+""+district);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findFirstCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(BusinessState.B4.name);
        		ui.transactionService.update(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		//清空舞台
            	cleanStage();
            	Notifications.info("操作成功。本次业务已添加到审档队列中，等待审档。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(BusinessState.B7.name);
        		ui.transactionService.update(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(Actions.INPUT);
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.info("操作成功。本次业务已添加到质检队列中，等待质检。");
        	}
    	}
    	editableTrans = null;
    }
    
    /**
     * 
     * @param act
     */
    private void track(Actions act) {
    	Map<String, String> details = new HashMap<String, String>();
    	details.put("1", editableTrans.getStatus());//STATUS
		details.put("2", basicInfoPane.getBarCode());//BARCODE
		details.put("3", basicInfoPane.getPlateType());//PLATETYPE
		details.put("4", basicInfoPane.getPlateNumber());//PLATENUMBER
		details.put("5", basicInfoPane.getVIN());//VIN
		details.put("6", businessTypePane.getSelected().getName());//BUSINESSTYPE
		details.put("7", editableTrans.getUuid());//UUID
		String json = jsonHelper.map2Json(details);
    	
    	// 插入移行表
		Transition transition = new Transition();
		transition.setTransactionUUID(uuid);
		transition.setAction(act.name);
		transition.setDetails(json);
		transition.setUserName(loggedInUser.getUserName());
		transition.setDateUpdated(new Date());
		ui.transitionService.insert(transition,basicInfoPane.getVIN());
		
		// 插入用户事件
		UserEvent event = new UserEvent();
		event.setAction(Actions.INPUT.name);
		event.setUserName(loggedInUser.getUserName());
		event.setDateUpdated(new Date());
		event.setDetails(json);
		ui.userEventService.insert(event, loggedInUser.getUserName());
    }
    
	@Override
	public void cleanStage() {
		main.removeAllComponents();
		main.setHeight("100%");
		main.addComponents(blankLabel);
		main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
		//清空当前用户上传缓存
		UploadFileServlet.IN_DTOs.remove(loggedInUser.getUserUniqueId());
		UploadFileServlet.OUT_DTOs.remove(loggedInUser.getUserUniqueId());
		try {
			FileUtils.forceDelete(new File("devices/"+loggedInUser.getUserUniqueId()+".html"));
		} catch (IOException e) {
			log.info("删除"+"devices/"+loggedInUser.getUserUniqueId()+".html失败。");
		}
	}

	@Override
	public void updateUnreadCount() {
		List<SendDetails> sendDetailsList = CacheManager.getInstance().getSendDetailsCache().asMap().get(loggedInUser.getUserUniqueId());
		int unreadCount = 0;
		for (SendDetails sd : sendDetailsList) {
			if (sd.getViewName().equals(DashboardViewType.INPUT.getViewName())
					|| sd.getViewName().equals("")) {
				unreadCount++;
			}
		}
		NotificationsCountUpdatedEvent event = new DashboardEvent.NotificationsCountUpdatedEvent();
		event.setCount(unreadCount);
		notificationsButton.updateNotificationsCount(event);
	}
	
	@Override
	public User loggedInUser() {
		return loggedInUser;
	}
	
	@Override
	public int batch() {
		return batch;
	}
	
	@Override
	public String uuid() {
		return uuid;
	}
	
	@Override
	public String vin() {
		return vin;
	}

	@Override
	public Site editableSite() {
		return editableSite;
	}
	
	@Override
	public void stoppedAtAnException(boolean stop) {
		this.stoppedAtAnException = stop;
	}
	
	@Override
	public BasicInfoPane basicInfoPane() {
		return basicInfoPane;
	}
	
	@Override
	public BusinessTypePane businessTypePane() {
		return businessTypePane;
	}
	
	@Override
	public ThumbnailGrid thumbnailGrid() {
		return fileGrid;
	}
	
	@Override
	public CapturePane capturePane() {
		return capturePane;
	}
	
	public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";
	private int editMode;//0-新建 1:修改
	private MessageBodyParser jsonHelper = new MessageBodyParser();
	private Transaction editableTrans = null; 	//可编辑的编辑transaction
	private Company editableCompany = null;	 	//前台所在机构
	private User loggedInUser;	//登录用户
	private Site editableSite;	//业务站点
	private String uuid;			//UUID业务与原文关联号
	private int batch;			//业务批次号。默认最大1000个批次，每批次最多放5000文件夹。
	private String vin = "LGB12YEA9DY001226";			//车辆识别代码。用于分表。
	private boolean stoppedAtAnException = false;// true：异常停止 false:继续正常录入。
    private Label titleLabel;
    private Window notificationsWindow;
    private VerticalLayout root;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private Binder<Transaction> binder = new Binder<>();
    //各个区域面板
    private BasicInfoPane basicInfoPane = new BasicInfoPane(this);
    private BusinessTypePane businessTypePane = new BusinessTypePane(this);
    private ThumbnailGrid fileGrid = new ThumbnailGrid(this);
    private CapturePane capturePane = new CapturePane();
    private Button btnPrint = new Button();
    private Button btnAdd = new Button();
    private Button btnCommit = new Button();
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
    private HorizontalLayout spliterNorth = new HorizontalLayout();
    private HorizontalLayout spliterSouth = new HorizontalLayout();
}
