package com.maxtree.automotive.dashboard.view.quality;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Activity;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.Openwith;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.component.TimeAgo;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.automotive.dashboard.view.MessageView;
import com.maxtree.automotive.dashboard.view.front.MessageInboxWindow;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.maxtree.trackbe4.messagingsystem.Name;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
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

public class QCView extends Panel implements View, FrontendViewIF{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(QCView.class);
	
	public QCView() {
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
        main.addStyleName("main-check");
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
        
        this.startPolling();
        
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
            	
            	
            	Callback2 verifyEvent = new Callback2() {
					@Override
					public void onSuccessful(Object... objects) {
						// fails
						if (objects[0].equals("")) {
							
							LicenseHasExpiredWindow.open("您的许可证已经过期，请及时更新许可证文件。", new Callback() {

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
	
	private void startPolling() {
		SystemConfiguration sc = Yaml.readSystemConfiguration();
		ui.setPollInterval(sc.getInterval());
		ui.addPollListener(new UIEvents.PollListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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

        titleLabel = new Label("质量检查");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        buildNotificationsButton();
        buildFetchButton();
        buildCommitButton();
        
        HorizontalLayout tools = new HorizontalLayout(fatchButton, btnCommit, notificationsButton);
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
        List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(loggedInUser, DashboardViewType.QUALITY.getViewName());
        for (Map<String, Object> m : allMessages) {
        	VerticalLayout vLayout = new VerticalLayout();
        	vLayout.setMargin(false);
        	vLayout.setSpacing(false);
        	vLayout.addStyleName("notification-item");
            Label timeLabel = new Label();
            Label subjectLabel = new Label();
            subjectLabel.addStyleName("notification-title");
            int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
            String subject = m.get("subject").toString();
            subjectLabel.setValue(subject);
            String content = m.get("content").toString();
            Label contentLabel = new Label(content);
            String matedata = m.get("matedata").toString();
            Map<String, String> matedataMap = jsonHelper.json2Map(matedata);
            contentLabel.addStyleName("notification-content");
            Date dateCreated = (Date) m.get("datecreated");
            long duration = new Date().getTime() - dateCreated.getTime();
            timeLabel.setValue(new TimeAgo().toDuration(duration));
            timeLabel.addStyleName("notification-time");
            vLayout.addComponents(subjectLabel,timeLabel,contentLabel);
            listLayout.addComponent(vLayout);
            vLayout.addStyleName("switchbutton");
            vLayout.addLayoutClickListener(e -> {
            	notificationsWindow.close();
            	String openWith = matedataMap.get("openwith");
            	Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						//更改已读状态
						ui.messagingService.markAsRead(messageUniqueId, loggedInUser.getUserUniqueId());
						CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
					}
        		};
            	
            	if(openWith.equals(Openwith.MESSAGE)) {
            		MessageView.open(m, callback);
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
        	showAll(allMessages, 0);
        	notificationsWindow.close();
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
            notificationsWindow.setPositionY(event.getClientY()
                    - event.getRelativeY() + 40);
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
    	main.setSpacing(false);
    	main.setMargin(false);
    	main.removeAllComponents();
    	confirmInformationGrid = new ConfirmInformationGrid(editableTrans);
    	splitPanel = new SplitPanel(editableTrans);
	    main.addComponents(confirmInformationGrid,splitPanel);
	    main.setExpandRatio(splitPanel, 1);
    }
    
    /**
     * 获取按钮
     * 
     * @return
     */
    private void buildFetchButton() {
    	fatchButton = new NotificationsButton();
    	fatchButton.setEnabled(true);
    	fatchButton.setId(EDIT_ID);
    	fatchButton.setIcon(VaadinIcons.RANDOM);
    	fatchButton.addStyleName("icon-edit");
    	fatchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	fatchButton.setDescription("从队列取一条记录检查");
    	fatchButton.addClickListener(e -> {
    		if(editableTrans == null) {
    			fetchTransaction();
    		}
    		else {
    			Notifications.warning("请先完成当前任务，再取下一条。");
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
        btnCommit.addClickListener(e -> {
        	if (editableTrans == null) {
        		Notifications.warning("暂无可提交的信息。");
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
     * 获取一笔业务
     * 
     * @param Transaction
     */
    private void fetchTransaction() {
    	Queue lockedQueue = ui.queueService.getLockedQueue(1, loggedInUser.getUserUniqueId());
    	if (lockedQueue.getQueueUniqueId() > 0) {
    		com.vaadin.ui.Notification notification = new com.vaadin.ui.Notification("提示：", "正在导入上次业务。", com.vaadin.ui.Notification.Type.WARNING_MESSAGE);
			notification.setDelayMsec(1000);
			notification.show(Page.getCurrent());
			notification.addCloseListener(e -> {
				editableTrans = ui.transactionService.findByUUID(lockedQueue.getUuid(), lockedQueue.getVin());
				resetComponents();
			});

    	} else {
    		//1:质检队列 2:审档队列
    		int serial = 1;
    		Queue availableQueue = ui.queueService.poll(serial,loggedInUser.getCompanyUniqueId(),loggedInUser.getCommunityUniqueId());
    		if (availableQueue.getQueueUniqueId() != 0) {
    			availableQueue.setLockedByUser(loggedInUser.getUserUniqueId());
    			ui.queueService.lock(availableQueue, serial);// 锁定记录
    			editableTrans = ui.transactionService.findByUUID(availableQueue.getUuid(),availableQueue.getVin());
    			resetComponents();
    			
//    			Transition tran = ui.transitionService.findByUUID(availableQueue.getUuid(), availableQueue.getVin());
//    			TB4MessagingSystem messageSystem = new TB4MessagingSystem();
//    			Message newMessage = messageSystem.createNewMessage(loggedInUser, "获取一笔新业务", tran.getDetails());
//    			Set<Name> names = new HashSet<Name>();
//    			Name target = new Name(loggedInUser.getUserUniqueId(), Name.USER, loggedInUser.getProfile().getLastName()+loggedInUser.getProfile().getFirstName(), loggedInUser.getProfile().getPicture());
//    			names.add(target);
//    			messageSystem.sendMessageTo(newMessage.getMessageUniqueId(), names, DashboardViewType.QUALITY.getViewName());
//    		
//    			CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
    		}
    		else {
    			Notifications.warning("没有可办的业务了。");
    		}
    	}
    }
    
    /**
     * 合格并退回前台打印标签
     * 
     * @param comments
     */
    private void accept(String comments) {
    	Business business = ui.businessService.findByCode(editableTrans.getBusinessCode());
    	// 不需要审档直接退回前台
    	if (StringUtils.isEmpty(business.getCheckLevel())) {
    		//1.删除锁定队列
    		int serial = 1; //1:质检 2:审档 3:确认审档
    		Queue queue = ui.queueService.getLockedQueue(serial, loggedInUser.getUserUniqueId());
        	try {
    			ui.queueService.delete(queue.getQueueUniqueId(), serial);
    		} catch (DataException e) {
    			log.info(e.getMessage());
    		}
        	//2.更改状态
        	if (business.getName().contains("注册登记")) {
        		editableTrans.setStatus(ui.state().getName("B2"));
        	}
        	else if(StringUtils.isEmpty(business.getCheckLevel())) {
        		editableTrans.setStatus(ui.state().getName("B2"));
        	}
        	else if(business.getCheckLevel().equals("一级审档")) {
        		editableTrans.setStatus(ui.state().getName("B4"));
        	}
        	else if(business.getCheckLevel().equals("二级审档")) {
        		editableTrans.setStatus(ui.state().getName("B4"));
        	}
        	editableTrans.setDateModified(new Date());
    		ui.transactionService.update(editableTrans);
    		//3.记录跟踪
    		int transitionUniqueId = track(Activity.APPROVED, comments);
    		//4.发信给前台
    		String matedata = "{\"openwith\":\""+Openwith.PRINT+"\",\"uuid\":\""+editableTrans.getUuid()+"\",\"vin\":\""+editableTrans.getVin()+"\"}";
    		User receiver = ui.userService.getUserByUserName(editableTrans.getCreator());
    		TB4MessagingSystem messageSystem = new TB4MessagingSystem();
    		Message newMessage = messageSystem.createNewMessage(receiver, editableTrans.getPlateNumber()+",质检合格",comments,matedata);
    		Set<Name> names = new HashSet<Name>();
    		Name target = new Name(receiver.getUserUniqueId(), Name.USER, receiver.getProfile().getLastName()+receiver.getProfile().getFirstName(), receiver.getProfile().getPicture());
    		names.add(target);
    		messageSystem.sendMessageTo(newMessage.getMessageUniqueId(), names, DashboardViewType.INPUT.getViewName());
    		// 更新消息轮询的缓存
    		CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
    		// 5.清空
    		cleanStage();
    		// 6.提示信息
    		Notifications.bottomWarning("操作成功。已退回到前台。");
    		
    	}
    	// 需要审档则继续提交
    	else {
    		//1.删除锁定队列
    		int serial = 1; //1:质检 2:审档 3:确认审档
    		Queue queue = ui.queueService.getLockedQueue(serial, loggedInUser.getUserUniqueId());
        	try {
    			ui.queueService.delete(queue.getQueueUniqueId(), serial);
    		} catch (DataException e) {
    			log.info(e.getMessage());
    		}
        	//2.更改状态
        	editableTrans.setStatus(ui.state().getName("B4"));
        	editableTrans.setDateModified(new Date());
    		ui.transactionService.update(editableTrans);
    		//3.记录跟踪
    		track(Activity.SUBMIT, comments);
    		// 4.提交审档队列
    		Queue newQueue = new Queue();
    		newQueue.setUuid(editableTrans.getUuid());
    		newQueue.setVin(editableTrans.getVin());
    		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
    		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
    		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
    	    serial = 2;// 1:代表质检取队列，2：代表审档取队列
    		ui.queueService.create(newQueue, serial);
    		
    		// 清空舞台
        	cleanStage();
        	Notifications.bottomWarning("操作成功。本次业务已添加到审档队列中，等待审档。");
    	}
    }
    
    /**
     * 不合格退回
     * 
     * @param comments
     */
    private void reject(String comments) {
    	//1.删除锁定队列
		int serial = 1; //1:质检 2:审档 3:确认审档
		Queue queue = ui.queueService.getLockedQueue(serial, loggedInUser.getUserUniqueId());
    	try {
			ui.queueService.delete(queue.getQueueUniqueId(), serial);
		} catch (DataException e) {
			log.info(e.getMessage());
		}
    	//2.更改状态
    	editableTrans.setStatus(ui.state().getName("B15"));//质检不合格
    	editableTrans.setDateModified(new Date());
		ui.transactionService.update(editableTrans);
		//3.记录跟踪
		int transitionUniqueId = track(Activity.REJECTED,comments);
		//4.发信给前台
		String matedata = "{\"openwith\":\""+Openwith.TRANSACTION+"\",\"uuid\":\""+editableTrans.getUuid()+"\",\"vin\":\""+editableTrans.getVin()+"\"}";
		User receiver = ui.userService.getUserByUserName(editableTrans.getCreator());
		TB4MessagingSystem messageSystem = new TB4MessagingSystem();
		Message newMessage = messageSystem.createNewMessage(receiver,editableTrans.getPlateNumber()+",质检不合格",comments,matedata);
		Set<Name> names = new HashSet<Name>();
		Name target = new Name(receiver.getUserUniqueId(), Name.USER, receiver.getProfile().getLastName()+receiver.getProfile().getFirstName(), receiver.getProfile().getPicture());
		names.add(target);
		messageSystem.sendMessageTo(newMessage.getMessageUniqueId(), names, DashboardViewType.INPUT.getViewName());
		// 更新消息轮询的缓存
		CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
		// 5.清空
		cleanStage();
		// 6.提示信息
		Notifications.bottomWarning("操作成功。已退回到前台。");
    }
    
    /**
     * 
     * @param act
     * @param comments
     * @return
     */
    private int track(Activity act, String comments) {
//    	Map<String, String> details = new HashMap<String, String>();
//    	details.put("1", editableTrans.getStatus());//STATUS
//		details.put("2", editableTrans.getBarcode());//BARCODE
//		details.put("3", editableTrans.getPlateType());//PLATETYPE
//		details.put("4", editableTrans.getPlateNumber());//PLATENUMBER
//		details.put("5", editableTrans.getVin());//VIN
//		details.put("6", editableTrans.getBusinessName());//BUSINESSTYPE
//		details.put("7", editableTrans.getUuid());//UUID
//		String json = jsonHelper.map2Json(details);
    	
    	// 插入移行表
		Transition transition = new Transition();
		transition.setTransactionUUID(editableTrans.getUuid());
		transition.setVin(editableTrans.getVin());
		transition.setActivity(act.name);
		transition.setComments(comments);
		transition.setOperator(loggedInUser.getUserName());
		transition.setDateCreated(new Date());
		int transitionUniqueId = ui.transitionService.insert(transition,editableTrans.getVin());
		return transitionUniqueId;
    }
    
    
    /**
     * 
     * @param transactionUniqueId
     */
    public void commitTransaction() {
		Callback2 accept = new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				accept(objects[0].toString());
			}
		};
		Callback2 reject = new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				reject(objects[0].toString());
			}
		};
		RouterWindow.open(accept, reject);
    }
    
    @Override
   	public void updateUnreadCount() {
   		List<Notification> notifications = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
    	int unreadCount = 0;
		for (Notification n : notifications) {
			if (n.getViewName().equals(DashboardViewType.QUALITY.getViewName())
					|| n.getViewName().equals("")) {
				unreadCount++;
			}
		}
		// 更新提醒按钮未读数
		notificationsButton.setUnreadCount(unreadCount);
		DashboardMenu.getInstance().qcCount(unreadCount);
		// 更新取队列按钮可用数
		List<Queue> listQue = ui.queueService.findAvaliable(1);
    	fatchButton.setUnreadCount(listQue.size());
   	}
    
   	@Override
   	public void cleanStage() {
   		main.removeAllComponents();
		main.setHeight("100%");
		main.addComponents(blankLabel);
		main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
		editableTrans = null;
   	}
    
   	private MessageBodyParser jsonHelper = new MessageBodyParser();
   	private Transaction editableTrans = null; 	//可编辑的编辑transaction
   	public User loggedInUser;	//登录用户
    private Label titleLabel;
    private Window notificationsWindow;
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private ConfirmInformationGrid confirmInformationGrid;
    private SplitPanel splitPanel;
    private Button btnCommit = new Button();
    private NotificationsButton fatchButton;
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
}
