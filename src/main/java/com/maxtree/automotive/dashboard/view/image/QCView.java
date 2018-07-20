package com.maxtree.automotive.dashboard.view.image;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Hr;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.component.TimeAgo;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.domain.SendDetails;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.automotive.dashboard.view.front.MessageInboxWindow;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
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
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
        dynamicallyVLayout.addStyleName("dynamicallyVLayout-check");
        dynamicallyVLayout.setWidth("100%");
        dynamicallyVLayout.setHeight("100%");
        dynamicallyVLayout.addComponents(blankLabel);
        dynamicallyVLayout.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
        root.addComponents(dynamicallyVLayout);
        root.setExpandRatio(dynamicallyVLayout, 7.0f);
        
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
		ui.setPollInterval(sc.getPollinginterval());
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

        titleLabel = new Label("质量检查");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        buildNotificationsButton();
        buildFetchButton();
        buildCommitButton();
        
        HorizontalLayout tools = new HorizontalLayout(btnFetch, btnCommit, notificationsButton);
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
        
    	User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(currentUser, DashboardViewType.SENDBACK.getViewName());
        for (Map<String, Object> m : allMessages) {
        	
        	VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.setMargin(false);
            notificationLayout.setSpacing(false);
            notificationLayout.addStyleName("notification-item");
            String readStr = m.get("markedasread").toString().equals("0")?"(未读)":"";
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
//					int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
//					ui.messagingService.deleteMessageRecipient(messageUniqueId, currentUser.getUserUniqueId());
//					continue;
//				}
//            }

            notificationLayout.addComponents(titleLabel, timeLabel, contentLabel);
            listLayout.addComponent(notificationLayout);
            notificationLayout.addStyleName("switchbutton");
            notificationLayout.addLayoutClickListener(e -> {
            	notificationsWindow.close();
            	
            	int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
            	
            	if ("text".equals(type)) {
            		
            		showAll(allMessages, messageUniqueId);
            		
    			} else if ("transaction".equals(type)) {
    				int transactionUniqueId = Integer.parseInt(map.get("transactionUniqueId").toString());
    				// 标记已读
    				ui.messagingService.markAsRead(messageUniqueId, currentUser.getUserUniqueId());
    				getUnreadCount();
    				// 打开业务
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
    	getUnreadCount();
    }

    public static final class NotificationsButton extends Button {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
        	log.info("===============QCView Polling");
        	DashboardMenu.getInstance().qcCount(event.getCount());
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
    
    public static final class FetchButton extends Button {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public FetchButton() {
            setIcon(VaadinIcons.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            DashboardEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(NotificationsCountUpdatedEvent event) {
//        	log.info("===============QCView Polling");
//        	DashboardMenu.getInstance().qcCount(event.getCount());
        	setUnreadCount(event.getCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));
            String description = "可用队列";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " 未取)";
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
//    	dynamicallyVLayout.setSpacing(false);
//    	dynamicallyVLayout.setMargin(false);
//    	dynamicallyVLayout.removeAllComponents();
//    	dynamicallyVLayout.setHeightUndefined();
//    	confirmInformationGrid = new ConfirmInformationGrid(transaction);
//		imageChecker = new ImageChecker(transaction);
//		Hr hr = new Hr();
//	    dynamicallyVLayout.addComponents(confirmInformationGrid, hr, imageChecker);
    }
    
    /**
     * 获取按钮
     * 
     * @return
     */
    private void buildFetchButton() {
    	btnFetch.setEnabled(true);
    	btnFetch.setId(EDIT_ID);
    	btnFetch.setIcon(VaadinIcons.RANDOM);
    	btnFetch.addStyleName("icon-edit");
    	btnFetch.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnFetch.setDescription("从队列取一条记录检查");
    	btnFetch.addClickListener(e -> {
    		fetchTransaction();
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
        	if (transaction == null) {
        		Notifications.warning("暂无可提交的信息。");
        	} else {
        		commitTransaction(transaction.getTransactionUniqueId());
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
    	
//    	User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//    	Queue lockedQueue = ui.queueService.getLockedQueue(1, loginUser.getUserUniqueId());
//    	if (lockedQueue.getQueueUniqueId() > 0) {
//    		
//			Notification notification = new Notification("提示：", "正在导入已经获取的业务。", Type.WARNING_MESSAGE);
//			notification.setDelayMsec(1000);
//			notification.show(Page.getCurrent());
//			notification.addCloseListener(e -> {
//				transaction = ui.transactionService.findById(lockedQueue.getTransactionUniqueId());
//				resetComponents();
//			});
//
//    	} else {
//    		int serial = 1;//1:质检 2:审档
//    		Queue availableQueue = ui.queueService.poll(serial, loginUser.getCommunityUniqueId());
//    		if (availableQueue.getQueueUniqueId() != 0) {
//    			availableQueue.setLockedByUser(loginUser.getUserUniqueId());
//    			ui.queueService.update(availableQueue, serial); // 锁定记录
//    			
//    			transaction = ui.transactionService.findById(availableQueue.getTransactionUniqueId());
//    			
//    			resetComponents();
//    			
//    			User sender = ui.userService.findById(availableQueue.getSentByUser());
//    			 // 发送消息
//    			String subject = "前台消息";
//    	        StringBuilder msg = new StringBuilder();
//    	        msg.append("收到一笔业务，车牌号："+transaction.getPlateNumber()+",车辆识别代码："+transaction.getVin()+"。");
//    	        String messageBody = "{\"type\":\"transaction\",\"status\":\""+Status.S1.name+"\",\"transactionUniqueId\":\""+transaction.getTransactionUniqueId()+"\",\"message\":\""+msg.toString()+"\"}";
//    	        new TB4MessagingSystem().sendMessageTo(sender, loginUser.getUserName(), 0, 0, loginUser.getUserUniqueId(), subject, messageBody, DashboardViewType.SENDBACK.getViewName());
//    	    	// 获取未读数
//    	    	getUnreadCount();
//    	    	
//    	    	seeAvailableQueueSize();
//    		}
//    		else {
//    			Notifications.warning("没有可办的业务了。");
//    		}
//    	}
    }
    
    /**
     * 合格并退回前台打印标签
     * 
     * @param comments
     */
    private void acceptAndBack(String comments) {
    	
    	// 1.设置上架号
//    	if (transaction.getCode() == null) {
//         	Portfolio portfolio = ui.storehouseService.findAvailablePortfolio();
//         	if (portfolio.getCode() == null) {
//         		Notifications.warning("没有对应的上架号，请联系管理员设置库房。");
//         		return;
//         	}
//         	transaction.setCode(portfolio.getCode());// 上架号
//         	
//         	portfolio.setVin(transaction.getVin());
//         	ui.storehouseService.updatePortfolio(portfolio);
//         }
    	
    	// 2.删除锁定队列
    	User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		int serial = 1; //1:质检 2:审档
		Queue queue = ui.queueService.getLockedQueue(serial, loginUser.getUserUniqueId());
    	try {
			ui.queueService.delete(queue.getQueueUniqueId(), serial);
		} catch (DataException e) {
			e.printStackTrace();
		}
//    	User receiver = ui.userService.findById(transaction.getTypist());
//    	
//    	// 3.更改状态
//		transaction.setStatus(Status.S4.name);
//		transaction.setDateModified(new Date());
//		int index = ui.transactionService.findIndexByVIN(transaction.getVin());
//		transaction.setIndexNumber(index);
//		ui.transactionService.update(transaction);
//		
//		// 4.发送消息
//		String subject = "质检合格";
//        StringBuilder msg = new StringBuilder();
//        msg.append(comments);
//        String messageBody = "{\"type\":\"transaction\",\"status\":\""+Status.S4.name+"\",\"transactionUniqueId\":\""+transaction.getTransactionUniqueId()+"\",\"message\":\""+msg.toString()+"\"}";
//        new TB4MessagingSystem().sendMessageTo(loginUser, receiver.getUserName(), 0, 0, receiver.getUserUniqueId(), subject, messageBody, DashboardViewType.DASHBOARD.getViewName());
//		
//        // 5.审批记录
//        Audit audit = new Audit();
//        audit.setTransactionUniqueId(transaction.getTransactionUniqueId());
//        audit.setAuditor(loginUser.getUserUniqueId());
//        audit.setAuditDate(new Date());
//        audit.setAuditResults(msg.toString());
//        ui.auditService.createAudit(audit);
        
		// 6.清空
		cleanStage();
		
		// 提示信息
		Notification success = new Notification("已完成！");
		success.setDelayMsec(2000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
    }
    
    /**
     * 不合格退回至前台
     * 
     * @param comments
     */
    private void back(String comments) {
    	// 1.删除锁定队列
    	User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		int serial = 1; //1:质检 2:审档
		Queue queue = ui.queueService.getLockedQueue(serial, loginUser.getUserUniqueId());
    	try {
			ui.queueService.delete(queue.getQueueUniqueId(), serial);
		} catch (DataException e) {
			e.printStackTrace();
		}
    	User receiver = ui.userService.findById(queue.getSentByUser());
    	
//    	// 2.更改状态
//		transaction.setStatus(Status.S1.name);///无状态，即前台可以再修改
//		transaction.setDateModified(new Date());
//		ui.transactionService.update(transaction);
//		
//		// 3.发送消息
//		String subject = "质检不合格";
//        StringBuilder msg = new StringBuilder();
//        msg.append(comments);
//        String messageBody = "{\"type\":\"transaction\",\"status\":\""+Status.S1.name+"\",\"transactionUniqueId\":\""+transaction.getTransactionUniqueId()+"\",\"message\":\""+msg.toString()+"\"}";
//        new TB4MessagingSystem().sendMessageTo(loginUser, receiver.getUserName(), 0, 0, receiver.getUserUniqueId(), subject, messageBody, DashboardViewType.DASHBOARD.getViewName());
		
//        // 4.审批记录
//        Audit audit = new Audit();
//        audit.setTransactionUniqueId(transaction.getTransactionUniqueId());
//        audit.setAuditor(loginUser.getUserUniqueId());
//        audit.setAuditDate(new Date());
//        audit.setAuditResults(msg.toString());
//        ui.auditService.createAudit(audit);
        
		// 5.清空
		cleanStage();
		
		// 提示信息
		Notification success = new Notification("已退回！");
		success.setDelayMsec(2000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
				
    }
    
    /**
     * 合格并提交给审档
     * 
     * @param comments
     */
    private void submit(String comments) {
    	// 1.删除锁定队列
    	User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		int serial = 1; //1:质检 2:审档
		Queue queue = ui.queueService.getLockedQueue(serial, loginUser.getUserUniqueId());
    	try {
			ui.queueService.delete(queue.getQueueUniqueId(), serial);
		} catch (DataException e) {
			e.printStackTrace();
		}
    	
//    	// 2.更改状态
//		transaction.setStatus(Status.S2.name);
//		transaction.setDateModified(new Date());
//		ui.transactionService.update(transaction);
//    	
		// 3.添加到审核队列
		Queue newQueue = new Queue();
		newQueue.setTransactionUniqueId(transaction.getTransactionUniqueId());
		newQueue.setLockedByUser(0);	 // 默认为0标识任何人都可以取，除非被某人锁定
		newQueue.setSentByUser(queue.getSentByUser());  // 前台发送者
		newQueue.setCommunityUniqueId(loginUser.getCommunityUniqueId());
	    serial = 2;// 1:质检，2：审档
		ui.queueService.create(newQueue, serial);
		
//		// 4.审批记录
//        Audit audit = new Audit();
//        audit.setTransactionUniqueId(transaction.getTransactionUniqueId());
//        audit.setAuditor(loginUser.getUserUniqueId());
//        audit.setAuditDate(new Date());
//        audit.setAuditResults(comments);
//        ui.auditService.createAudit(audit);
		
		// 5.清空
		cleanStage();
 
		// 6.提示当前操作成功消息
		Notification success = new Notification("信息提交成功！");
		success.setDelayMsec(2000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
    }
    
    /**
     * 打开业务
     * 
     * @param transactionUniqueId
     * @param messageDateCreated
     */
    public void openTransaction(int transactionUniqueId, Date messageDateCreated) {
//    	transaction = ui.transactionService.findById(transactionUniqueId);
//		if (transaction != null) {
//			long time1 = transaction.getDateModified().getTime();
//			long time2 = messageDateCreated.getTime();
//			if (time1 > time2) {
//				Notifications.warning("该行记录已过时。");
//				transaction = null;
//				return;
//			}
//			else if (transaction.getStatus().equals(Status.S1.name)) {
//				this.resetComponents();
//			}
//			
//		} else {
//			Notifications.warning("当前业务不存在。");
//		}
    }
    
    /**
     * 
     * @param transactionUniqueId
     */
    public void commitTransaction(int transactionUniqueId) {
//    	Business business = ui.businessService.findById(transaction.getBusinessUniqueId());
//    	// 不需要审档，合格退回前台，不合格也退回前台
//    	if (business.getFileCheck() == 0) {
//    		Callback2 accept = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//					acceptAndBack(objects[0].toString());
//				}
//    		};
//    		Callback2 back = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//					back(objects[0].toString());
//				}
//    		};
//    		EditFeedbackWindow.open(accept, back);
//    	}
//    	// 需要审档，合格提交到审档，不合格退回前台
//    	else {
//    		Callback2 accept = new Callback2() {
//				@Override
//				public void onSuccessful(Object... objects) {
//					submit(objects[0].toString());
//				}
//    		};
//    		Callback2 back = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//					back(objects[0].toString());
//				}
//    		};
//    		EditFeedbackWindow.open(accept, back);
//    	}
    }
    
    @Override
   	public void getUnreadCount() {
   		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
   		List<SendDetails> sendDetailsList = CacheManager.getInstance().getSendDetailsCache().asMap().get(loginUser.getUserUniqueId());
    	int unreadCount = 0;
		for (SendDetails sd : sendDetailsList) {
			if (sd.getViewName().equals(DashboardViewType.SENDBACK.getViewName())
					|| sd.getViewName().equals("")) {
				unreadCount++;
			}
		}
   		
   		NotificationsCountUpdatedEvent event = new DashboardEvent.NotificationsCountUpdatedEvent();
   		event.setCount(unreadCount);
   		notificationsButton.updateNotificationsCount(event);
   		
   		seeAvailableQueueSize();
   	}
    
    /**
     * 查看可用的队列记录数
     */
    private void seeAvailableQueueSize() {
    	List<Queue> listQue = ui.queueService.findAvaliable(1);
    	NotificationsCountUpdatedEvent event = new DashboardEvent.NotificationsCountUpdatedEvent();
   		event.setCount(listQue.size());
   		btnFetch.updateNotificationsCount(event);
    }

   	@Override
   	public void cleanStage() {
   		dynamicallyVLayout.removeAllComponents();
		dynamicallyVLayout.setHeight("100%");
		dynamicallyVLayout.addComponents(blankLabel);
		dynamicallyVLayout.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
		
		transaction = null;
   	}
    
    private Label titleLabel;
    private Window notificationsWindow;
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private VerticalLayout dynamicallyVLayout = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
//    private ConfirmInformationGrid confirmInformationGrid;
//    private ImageChecker imageChecker;
    private FetchButton btnFetch = new FetchButton();
    private Button btnCommit = new Button();
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
    private Transaction transaction = null;
}
