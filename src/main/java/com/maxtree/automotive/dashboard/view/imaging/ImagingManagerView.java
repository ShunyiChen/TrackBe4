package com.maxtree.automotive.dashboard.view.imaging;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maxtree.automotive.dashboard.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.NotificationsPopup;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.trackbe4.messagingsystem.MatedataJsonParser;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
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
import com.vaadin.ui.themes.ValoTheme;

import de.schlichtherle.license.LicenseContent;

/**
 * 
 * @author Chen
 *
 */
public class ImagingManagerView extends Panel implements View, FrontendViewIF{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(ImagingManagerView.class);
	
	public ImagingManagerView() {
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
        VerticalLayout main = new VerticalLayout();
        main.setSpacing(false);
        main.setMargin(false);
        main.addStyleName("main-check");
        main.setWidth("100%");
        main.setHeight("100%");
        main.addComponents(grid);
        main.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        
        
        root.addComponents(main);
        root.setExpandRatio(main, 1.0f);
//        root.addComponent(grid);
//        root.setExpandRatio(grid, 1.0f);
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
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
	
	/**
	 * 
	 */
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

	/**
	 * 
	 * @return
	 */
    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);
        return sparks;
    }

    /**
     * 
     * @return
     */
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("影像化管理");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        buildNotificationsButton();
        buildSearchButton();
        
        HorizontalLayout tools = new HorizontalLayout(btnBasicRearch, notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

//    private void openNotificationsPopup(final ClickEvent event) {
//    	VerticalLayout mainVLayout = new VerticalLayout();
//    	mainVLayout.setSpacing(false);
//        
//        Label title = new Label("事件提醒");
//        title.addStyleName(ValoTheme.LABEL_H3);
//        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        mainVLayout.addComponent(title);
//
//        Panel scrollPane = new Panel();
//    	scrollPane.addStyleName("reminder-scrollpane");
//    	scrollPane.setHeight("250px");
//    	VerticalLayout listLayout = new VerticalLayout();
//    	
//    	 List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(loggedInUser, DashboardViewType.IMAGING_MANAGER.getViewName());
//         for (Map<String, Object> m : allMessages) {
//        	 VerticalLayout vLayout = new VerticalLayout();
//         	vLayout.setMargin(false);
//         	vLayout.setSpacing(false);
//         	vLayout.addStyleName("notification-item");
//             Label timeLabel = new Label();
//             Label subjectLabel = new Label();
//             subjectLabel.addStyleName("notification-title");
//             int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
//             String subject = m.get("subject").toString();
//             subjectLabel.setValue(subject);
//             String content = m.get("content").toString();
//             Label contentLabel = new Label(content);
//             String matedata = m.get("matedata").toString();
//             Map<String, String> matedataMap = jsonHelper.json2Map(matedata);
//             contentLabel.addStyleName("notification-content");
//             Date dateCreated = (Date) m.get("datecreated");
//             long duration = new Date().getTime() - dateCreated.getTime();
//             timeLabel.setValue(new TimeAgo().toDuration(duration));
//             timeLabel.addStyleName("notification-time");
//             vLayout.addComponents(subjectLabel,timeLabel,contentLabel);
//             listLayout.addComponent(vLayout);
//             vLayout.addStyleName("switchbutton");
//             vLayout.addLayoutClickListener(e -> {
//             	notificationsWindow.close();
//             	String openWith = matedataMap.get("openwith");
//            	Callback callback = new Callback() {
//
//					@Override
//					public void onSuccessful() {
//						//更改已读状态
//						ui.messagingService.markAsRead(messageUniqueId, loggedInUser.getUserUniqueId());
//						CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
//					}
//        		};
//        		// 显示消息
//        		MessageView.open(m,callback);
//             	 
//             });
//         }
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
//        	notificationsWindow.close();
//        	showAll(allMessages, 0);
//        });
//        
//        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
//        footer.addComponent(showAll);
//        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
//        mainVLayout.addComponent(footer);
//        mainVLayout.setExpandRatio(footer, 0.1f);
//        
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
//        notificationsButton.updateNotificationsCount(null);
    	grid.controls.first();
    }

    /**
     * 基本查询按钮
     */
    private void buildSearchButton() {
    	btnBasicRearch.setEnabled(true);
    	btnBasicRearch.setId(EDIT_ID);
    	btnBasicRearch.setIcon(VaadinIcons.SEARCH);
    	btnBasicRearch.addStyleName("icon-edit");
    	btnBasicRearch.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnBasicRearch.setDescription("基本查询");
    	btnBasicRearch.addClickListener(e -> {
    		basicSearch();
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
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(final ClickEvent event) {
                popup.open(event);
            }
        });
    }
    
    /**
     * 
     */
    private void basicSearch() {
    	FuzzyQueryWindow.open(grid);
    }
    
    @Override
	public void updateUnreadCount() {
    	List<Notification> notifications = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
		int unreadCount = 0;
		for (Notification n : notifications) {
			if (n.getViewName().equals(DashboardViewType.IMAGING_MANAGER.getViewName())) {
				unreadCount++;
				
				Message message = ui.messagingService.findById(n.getMessageUniqueId());
				if(!StringUtils.isEmpty(message.getMatedata())) {
					Map<String, String> matedata = parser.json2Map(message.getMatedata());
					String uuid = matedata.get("UUID");
					String vin = matedata.get("VIN");
					String state = matedata.get("STATE");
					String checkLevel = matedata.get("CHECKLEVEL");
					String popupAutomatically = matedata.get("POPUPAUTOMATICALLY");
					if(!StringUtils.isEmpty(popupAutomatically)
							&& popupAutomatically.equals("TRUE")) {
						
						Callback markAsRead = new Callback() {

							@Override
							public void onSuccessful() {
								ui.messagingService.markAsRead(n.getNotificationUniqueId());
								
								CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
							}
						};
						dia.showDialog(message.getSubject(), message.getContent(), markAsRead);
					}
				}
				
				
			}
		}
		
		DashboardMenu.getInstance().imagingAdminCount(unreadCount);
		notificationsButton.setUnreadCount(unreadCount);
    	
    	
//    	// 显示未读消息数
//		List<SendDetails> sendDetailsList = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
//		int unreadCount = 0;
//		for (SendDetails sd : sendDetailsList) {
//			if ((sd.getViewName().equals(DashboardViewType.IMAGING_MANAGER.getViewName())
//					|| sd.getViewName().equals("")) && sd.getMarkedAsRead()==0) {
//				unreadCount++;
//			}
//		}
//		NotificationsCountUpdatedEvent event = new DashboardEvent.NotificationsCountUpdatedEvent();
//		event.setCount(unreadCount);
//		notificationsButton.updateNotificationsCount(event);
//		// 自动弹出未读消息
//		if(sendDetailsList.size() > 0) {
//			SendDetails sd = sendDetailsList.get(sendDetailsList.size() -1);
//			Message message = ui.messagingService.findById(sd.getMessageUniqueId());
//			Map<String, String> matedataMap = new MessageBodyParser().json2Map(message.getMatedata());
//			String popup = matedataMap.get("popup");
//			if(popup != null && popup.equals(Popup.YES)) {
//				Callback callback = new Callback() {
//
//					@Override
//					public void onSuccessful() {
//						ui.messagingService.markAsRead(sd.getMessageUniqueId(), loggedInUser.getUserUniqueId());
//						
//						//更新消息轮询的缓存
//						CacheManager.getInstance().getNotificationsCache().refresh(loggedInUser.getUserUniqueId());
//					}
//				};
//				ConfirmDialog.showDialog("提示",message.getContent(),callback);
//			}
//			
//		}
	}
    
	@Override
	public void cleanStage() {
	}
	
	
	private MatedataJsonParser jsonHelper = new MatedataJsonParser();
    private TodoListGrid grid = new TodoListGrid();
    private Label titleLabel;
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private Button btnBasicRearch = new Button();
    private NotificationsButton notificationsButton;
    private User loggedInUser;
    private NotificationsPopup popup = new NotificationsPopup(DashboardViewType.IMAGING_MANAGER.getViewName());
    private MatedataJsonParser parser = new MatedataJsonParser();
    private PopupAutomatically dia = new PopupAutomatically();
}
