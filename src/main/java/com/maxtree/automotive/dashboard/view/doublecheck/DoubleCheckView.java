package com.maxtree.automotive.dashboard.view.doublecheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.maxtree.automotive.dashboard.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.FetchButton;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.NotificationsPopup;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;

import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.automotive.dashboard.view.check.ImageWindow;
import com.maxtree.automotive.dashboard.view.check.Manual;
import com.maxtree.automotive.dashboard.view.quality.ConfirmInformationGrid;
import com.maxtree.automotive.dashboard.view.quality.FeedbackWindow;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
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

/**
 * 
 * @author Chen
 *
 */
public class DoubleCheckView extends Panel implements View, FrontendViewIF{

	private static final Logger log = LoggerFactory.getLogger(DoubleCheckView.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DoubleCheckView() {
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
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//	 	loggedInUser = ui.userService.getUserByUserName(username);
        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void layoutClick(final LayoutClickEvent event) {
            	if (notificationsWindow != null) {
            		notificationsWindow.close();
            	}
//                DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
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
        titleLabel = new Label("确认审档");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        buildFetchButton();
        buildCommitButton();
        buildNotificationsButton();
        HorizontalLayout tools = new HorizontalLayout(fetchButton,btnCommit,notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

//    private void openNotificationsPopup(final ClickEvent event) {
//    	VerticalLayout mainVLayout = new VerticalLayout();
//    	mainVLayout.setSpacing(false);
//        Label title = new Label("事件提醒");
//        title.addStyleName(ValoTheme.LABEL_H3);
//        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        mainVLayout.addComponent(title);
//        Panel scrollPane = new Panel();
//    	scrollPane.addStyleName("reminder-scrollpane");
//    	scrollPane.setHeight("250px");
//    	VerticalLayout listLayout = new VerticalLayout();
//    	User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//    	List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(currentUser, DashboardViewType.QUALITY.getViewName());
//        for (Map<String, Object> m : allMessages) {
//        	VerticalLayout vLayout = new VerticalLayout();
//        	vLayout.setMargin(false);
//        	vLayout.setSpacing(false);
//        	vLayout.addStyleName("notification-item");
//            Label timeLabel = new Label();
//            Label subjectLabel = new Label();
//            subjectLabel.addStyleName("notification-title");
//            int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
//            String subject = m.get("subject").toString();
//            subjectLabel.setValue(subject);
//            String content = m.get("content").toString();
//            Label contentLabel = new Label(content);
//            String matedata = m.get("matedata").toString();
//            Map<String, String> matedataMap = jsonHelper.json2Map(matedata);
//            contentLabel.addStyleName("notification-content");
//            Date dateCreated = (Date) m.get("datecreated");
//            long duration = new Date().getTime() - dateCreated.getTime();
//            timeLabel.setValue(new TimeAgo().toDuration(duration));
//            timeLabel.addStyleName("notification-time");
//            vLayout.addComponents(subjectLabel,timeLabel,contentLabel);
//            listLayout.addComponent(vLayout);
//            vLayout.addStyleName("switchbutton");
//            vLayout.addLayoutClickListener(e -> {
//            	notificationsWindow.close();
//            	String openWith = matedataMap.get("openwith");
//            	Callback callback = new Callback() {
//
//					@Override
//					public void onSuccessful() {
//						//更改已读状态
//						ui.messagingService.markAsRead(messageUniqueId, loggedInUser.getUserUniqueId());
//						CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
//					}
//        		};
//            	if(openWith.equals(Openwith.MESSAGE)) {
//            		MessageView.open(m, callback);
//            	}
//            });
//            
//        }
//        scrollPane.setContent(listLayout);
//        mainVLayout.addComponent(scrollPane);
//        mainVLayout.setExpandRatio(scrollPane, 0.9f);
//        
//        HorizontalLayout footer = new HorizontalLayout();
//        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
//        footer.setWidth("100%");
//        footer.setSpacing(false);
//        Button showAll = new Button("查看全部事件");
//        showAll.addClickListener(e->{
//        	showAll(allMessages, 0);
//        	notificationsWindow.close();
//        });
//        
//        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
//        footer.addComponent(showAll);
//        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
//        mainVLayout.addComponent(footer);
//        mainVLayout.setExpandRatio(footer, 0.1f);
//        if (notificationsWindow == null) {
//            notificationsWindow = new Window();
//            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
//            notificationsWindow.addStyleName("notifications");
//            notificationsWindow.setClosable(false);
//            notificationsWindow.setResizable(false);
//            notificationsWindow.setDraggable(false);
//            notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
//            notificationsWindow.setContent(mainVLayout);
//        } else {
//        	notificationsWindow.setContent(mainVLayout);
//        }
//
//        if (!notificationsWindow.isAttached()) {
//            notificationsWindow.setPositionY(event.getClientY()
//                    - event.getRelativeY() + 40);
//            getUI().addWindow(notificationsWindow);
//            notificationsWindow.focus();
//        } else {
//            notificationsWindow.close();
//        }
//    }

    @Override
    public void enter(final ViewChangeEvent event) {
    	updateUnreadCount();
    }
    
    /**
     * 
     */
    private void resetComponents() {
    	main.setSpacing(false);
    	main.setMargin(false);
    	main.removeAllComponents();
//    	confirmInformationGrid = new ConfirmInformationGrid(editableTrans);
//    	manualPane = new Manual(editableTrans);
//	    main.addComponents(confirmInformationGrid,manualPane);
//	    main.setExpandRatio(manualPane, 1);
    }
    
    /**
     * 获取按钮
     * 
     * @return
     */
    private void buildFetchButton() {
    	fetchButton = new FetchButton();
    	fetchButton.setEnabled(true);
    	fetchButton.setId(EDIT_ID);
    	fetchButton.setIcon(VaadinIcons.RANDOM);
    	fetchButton.addStyleName("icon-edit");
    	fetchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//    	fetchButton.addClickListener(e -> {
//    		if(editableTrans == null) {
//    			fetchTransaction();
//    		}
//    		else {
//    			Notifications.warning("请先完成当前任务，再取下一条。");
//    		}
//        });
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
		btnCommit.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btnCommit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCommit.setDescription("提交或反馈给前台录入");
		btnCommit.addClickListener(e -> {
//			if (editableTrans == null) {
//        		Notifications.warning("暂无可提交的信息。");
//        	} else {
//        		commitTransaction();
//        	}
		});
	}
    
	/**
	 * 
	 */
	private void commitTransaction() {
//		Callback2 accept = new Callback2() {
//			@Override
//			public void onSuccessful(Object... objects) {
//				accept(objects[0].toString());
//			}
//		};
//		Callback2 reject = new Callback2() {
//			@Override
//			public void onSuccessful(Object... objects) {
//				reject(objects[0].toString());
//			}
//		};
//		FeedbackWindow.open(accept, reject);
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
//                popup.open(event);
            }
        });
    }
    
    /**
     * 
     */
    private void fetchTransaction() {
//    	Queue lockedQueue = ui.queueService.getLockedQueue(3, loggedInUser.getUserUniqueId());
//    	if (lockedQueue.getQueueUniqueId() > 0) {
//    		com.vaadin.ui.Notification notification = new com.vaadin.ui.Notification("提示：", "正在导入上次业务。", com.vaadin.ui.Notification.Type.WARNING_MESSAGE);
//			notification.setDelayMsec(1000);
//			notification.show(Page.getCurrent());
//			notification.addCloseListener(e -> {
//				editableTrans = ui.transactionService.findByUUID(lockedQueue.getUuid(), lockedQueue.getVin());
//				resetComponents();
//			});
//
//    	} else {
//    		//1:质检队列 2:审档队列,3,确认审档队列
//    		int serial = 3;
//    		Queue availableQueue = ui.queueService.poll(serial,loggedInUser.getCompanyUniqueId(),loggedInUser.getCommunityUniqueId());
//    		if (availableQueue.getQueueUniqueId() != 0) {
//    			availableQueue.setLockedByUser(loggedInUser.getUserUniqueId());
//    			ui.queueService.lock(availableQueue, serial);// 锁定记录
//    			editableTrans = ui.transactionService.findByUUID(availableQueue.getUuid(),availableQueue.getVin());
//    			resetComponents();
//
////    			Transition tran = ui.transitionService.findByUUID(availableQueue.getUuid(), availableQueue.getVin());
////    			TB4MessagingSystem messageSystem = new TB4MessagingSystem();
////    			Message newMessage = messageSystem.createNewMessage(loggedInUser, "获取一笔新业务", tran.getDetails());
////    			Set<Name> names = new HashSet<Name>();
////    			Name target = new Name(loggedInUser.getUserUniqueId(), Name.USER, loggedInUser.getProfile().getLastName()+loggedInUser.getProfile().getFirstName(), loggedInUser.getProfile().getPicture());
////    			names.add(target);
////    			messageSystem.sendMessageTo(newMessage.getMessageUniqueId(), names, DashboardViewType.QUALITY.getViewName());
////
////    			CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
//    		}
//    		else {
//    			Notifications.warning("没有可办的业务了。");
//    		}
//    	}
    }
    
    /**
     * 
     * @param comments
     */
    private void accept(String comments) {
//    	Business business = ui.businessService.findByCode(editableTrans.getBusinessCode());
//    	if (business.getCheckLevel().equals("一级审档")) {
//    		//1.删除锁定队列
//    		int serial = 3; //1:质检 2:审档 3:确认审档
//    		Queue queue = ui.queueService.getLockedQueue(serial, loggedInUser.getUserUniqueId());
//        	try {
//    			ui.queueService.delete(queue.getQueueUniqueId(), serial);
//    		} catch (DataException e) {
//    			log.info(e.getMessage());
//    		}
//        	//2.更改状态
//        	editableTrans.setStatus(ui.state().getName("B14"));
//        	editableTrans.setDateModified(new Date());
//    		ui.transactionService.update(editableTrans);
//    		//3.记录跟踪
//    		track(ui.state().getName("B14"),comments);
//    		//4.发信给前台
//    		String plate = Yaml.readSystemConfiguration().getLicenseplate();
//    		String matedata = "{\"UUID\":\""+editableTrans.getUuid()+"\",\"VIN\":\""+editableTrans.getVin()+"\",\"STATE\":\""+editableTrans.getStatus()+"\",\"CHECKLEVEL\":\""+business.getCheckLevel()+"\"}";
//    		User receiver = ui.userService.getUserByUserName(editableTrans.getCreator());
//    		String subject = loggedInUser.getUserName()+"通过了确认审档";
//    		String content = plate+" "+editableTrans.getPlateNumber()+","+comments;
//    		Message newMessage = messageSystem.createNewMessage(loggedInUser,subject,content, matedata);
//
//    		Set<Name> names = new HashSet<Name>();
//    		Name target = new Name(receiver.getUserUniqueId(), Name.USER, receiver.getProfile().getLastName()+receiver.getProfile().getFirstName(), receiver.getProfile().getPicture());
//    		names.add(target);
//    		messageSystem.sendMessageTo(newMessage.getMessageUniqueId(),names,DashboardViewType.INPUT.getViewName());
//    		// 更新消息轮询的缓存
//    		CacheManager.getInstance().getNotificationsCache().refresh(receiver.getUserUniqueId());
//    		// 5.清空
//    		cleanStage();
//    		// 6.提示信息
//    		Notifications.bottomWarning("提交成功！已完成复审待补充。");
//
//    		removeImageWindows();
//    	}
    }
    
    /**
     * 
     * @param comments
     */
    private void reject(String comments) {
//    	Business business = ui.businessService.findByCode(editableTrans.getBusinessCode());
//    	// 1.删除锁定队列
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		User loginUser = ui.userService.getUserByUserName(username);
//		int serial = 3; //1:质检 2:审档 3:确认审档
//		Queue queue = ui.queueService.getLockedQueue(serial, loginUser.getUserUniqueId());
//    	try {
//			ui.queueService.delete(queue.getQueueUniqueId(), serial);
//		} catch (DataException e) {
//			e.printStackTrace();
//		}
//    	// 2.更改状态
//    	editableTrans.setStatus(ui.state().getName("B17"));//确认审核不合格
//		editableTrans.setDateModified(new Date());
//		ui.transactionService.update(editableTrans);
//
//		//3.记录跟踪
//		track(ui.state().getName("B17"),comments);
//
//		//4.发信给前台
//		String plate = Yaml.readSystemConfiguration().getLicenseplate();
//		String matedata = "{\"UUID\":\""+editableTrans.getUuid()+"\",\"VIN\":\""+editableTrans.getVin()+"\",\"STATE\":\""+editableTrans.getStatus()+"\",\"CHECKLEVEL\":\""+business.getCheckLevel()+"\"}";
//		User receiver = ui.userService.getUserByUserName(editableTrans.getCreator());
//		String subject = loggedInUser.getUserName()+"退回了一笔业务";
//		String content = plate+" "+editableTrans.getPlateNumber()+","+comments;
//		Message newMessage = messageSystem.createNewMessage(loggedInUser, subject,content,matedata);
//
//		Set<Name> names = new HashSet<Name>();
//		Name target = new Name(receiver.getUserUniqueId(), Name.USER, receiver.getProfile().getLastName()+receiver.getProfile().getFirstName(), receiver.getProfile().getPicture());
//		names.add(target);
//		messageSystem.sendMessageTo(newMessage.getMessageUniqueId(), names, DashboardViewType.INPUT.getViewName());
//		// 更新消息轮询的缓存
//		CacheManager.getInstance().getNotificationsCache().refresh(receiver.getUserUniqueId());
//		// 5.清空
//		cleanStage();
//		// 6.提示信息
//		Notifications.bottomWarning("提交成功！已退回到前台更正。");
//
//		removeImageWindows();
    }
    
    /**
     * 清除图像对比窗口
     */
    private void removeImageWindows() {
    	List<Window> removable = new ArrayList<Window>();
    	//清除ImageWindow
    	Collection<Window> windows = UI.getCurrent().getWindows();
    	for(Window w : windows) {
    		if (w instanceof ImageWindow) {
    			removable.add(w);
    		}
    	}
    	
    	for(int i = removable.size() - 1; i >= 0; i--) {
    		removable.get(i).close();
    	}
    }
    
//    /**
//     *
//     * @param status
//     * @param comments
//     * @return
//     */
//    private int track(String status, String comments) {
//    	// 插入移行表
//		Transition transition = new Transition();
//		transition.setTransactionUUID(editableTrans.getUuid());
//		transition.setVin(editableTrans.getVin());
//		transition.setActivity(status);
//		transition.setComments(comments);
//		transition.setOperator(loggedInUser.getUserName());
//		transition.setDateCreated(new Date());
//		int transitionUniqueId = ui.transitionService.insert(transition, editableTrans.getVin());
//
//		return transitionUniqueId;
//    }
    
    @Override
	public void updateUnreadCount() {
//    	List<Notification> notifications = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
//    	int unreadCount = 0;
//		for (Notification n : notifications) {
//			if (n.getViewName().equals(DashboardViewType.QUALITY.getViewName())
//					|| n.getViewName().equals("")) {
//				unreadCount++;
//			}
//		}
//		// 更新通知未读数
//		notificationsButton.setUnreadCount(unreadCount);
//		DashboardMenu.getInstance().doubleCheckCount(unreadCount);
//
//		// 更新队列可取数
//		List<Queue> listQue = ui.queueService.findAvaliable(3,loggedInUser.getCommunityUniqueId(), loggedInUser.getCompanyUniqueId());
//   		fetchButton.setUnreadCount(listQue.size());
//
//   		// 变更取队列按钮背景颜色
//   		Queue lockedQ = ui.queueService.getLockedQueue(3, loggedInUser.getUserUniqueId());
//   		if(lockedQ.getQueueUniqueId() != 0) {
//   			fetchButton.changeFriendlyColor();
//   		}
//   		else {
//   			fetchButton.restoreColor();
//   		}
	}

	@Override
	public void cleanStage() {
		main.removeAllComponents();
		main.setHeight("100%");
		main.addComponents(blankLabel);
		main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
//		manualPane.closeToolWindow();
	}

//   	private Transaction editableTrans = null; 	//可编辑的编辑transaction
//   	public User loggedInUser;
    private Label titleLabel;
    private Window notificationsWindow;
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private ConfirmInformationGrid confirmInformationGrid;
    private Manual manualPane;
    private Button btnCommit = new Button("提交业务");
    private FetchButton fetchButton;
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
//    private NotificationsPopup popup = new NotificationsPopup(DashboardViewType.DOUBLECHECK.getViewName());
    private TB4MessagingSystem messageSystem = new TB4MessagingSystem();
}
