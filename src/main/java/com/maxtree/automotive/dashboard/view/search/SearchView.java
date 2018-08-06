package com.maxtree.automotive.dashboard.view.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardNavigator;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Hr;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.component.TimeAgo;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.domain.SendDetails;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.service.BusinessService;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.automotive.dashboard.view.front.MessageInboxWindow;
import com.maxtree.automotive.dashboard.view.front.MessageWrapper;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.schlichtherle.license.LicenseContent;

public class SearchView extends Panel implements View, FrontendViewIF{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(SearchView.class);
	
	public SearchView() {
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
        
        HorizontalLayout searchbar = new HorizontalLayout();
        searchbar.setSpacing(false);
        searchbar.setMargin(false);
        searchbar.setWidthUndefined();
        
        TextField searchField = new TextField();
        searchField.setWidth("400px");
        searchField.setPlaceholder("请输入车牌号\\车架号\\业务流水号");
        Button searchButton = new Button();
        searchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        searchButton.setIcon(VaadinIcons.SEARCH);
        searchbar.addComponents(searchField,searchButton);
        
        root.addComponent(buildHeader());
//        root.addComponent(buildSparklines());
        root.addComponent(searchbar);
        root.addComponent(grid);
        root.setExpandRatio(grid, 1.0f);
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
        titleLabel = new Label("车辆查询");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        buildNotificationsButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        return header;
    }

    /**
     * 
     * @param event
     */
    private void openNotificationsPopup(final ClickEvent event) {
    	VerticalLayout mainVLayout = new VerticalLayout();
    	mainVLayout.setSpacing(false);
        Label title = new Label("事件提醒");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        mainVLayout.addComponent(title);

        Panel scrollPane = new Panel();
    	scrollPane.addStyleName("reminder-scrollpane");
    	scrollPane.setHeight("250px");
//        VerticalLayout notificationsLayout = new VerticalLayout();
    	VerticalLayout listLayout = new VerticalLayout();
    	
    	User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    	
    	List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(currentUser, DashboardViewType.SEARCH.getViewName());
        for (Map<String, Object> m : allMessages) {
//         	int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
//         	VerticalLayout notificationLayout = new VerticalLayout();
//             notificationLayout.setMargin(false);
//             notificationLayout.setSpacing(false);
//             notificationLayout.addStyleName("notification-item");
//             String readStr = m.get("markedasread").toString().equals("0")?"(未读)":"(已读)";
//             Label titleLabel = new Label(m.get("subject")+readStr);
//             titleLabel.addStyleName("notification-title");
//             
//             Date dateCreated = (Date) m.get("datecreated");
//             long duration = new Date().getTime() - dateCreated.getTime();
//             Label timeLabel = new Label();
//             timeLabel.setValue(new TimeAgo().toDuration(duration));
//             timeLabel.addStyleName("notification-time");
//             String json = m.get("messagebody").toString();
//             Map<String, String> map = new MessageBodyParser().json2Map(json);
//             String type = map.get("type").toString();
//             String messageContent = map.get("message");
//             Label contentLabel = new Label(messageContent);
//             contentLabel.addStyleName("notification-content");
//
//             notificationLayout.addComponents(titleLabel, timeLabel, contentLabel);
//             listLayout.addComponent(notificationLayout);
//             notificationLayout.addStyleName("switchbutton");
//             notificationLayout.addLayoutClickListener(e -> {
//             	notificationsWindow.close();
//             });
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
//        notificationsButton.updateNotificationsCount(null);
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
        	DashboardMenu.getInstance().searchCount(event.getCount());
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
//				updateUnreadCount();
			}
    	};
    	MessageInboxWindow.open(allMessages, event, selectedMessageUniqueId);
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
    
//    /**
//     * 
//     * @param Transaction
//     */
//    private void advanceSearch() {
//    	User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//    	ResultCallback callback = new ResultCallback() {
//
//			@Override
//			public void onSuccessful(List<Transaction> results) {
//				System.out.println("resulkt  ="+results.size()+"   perPageSize="+searchModel.getResultsPerPage());
//				
//				grid.setAllData(results);
//				
//				if (results.size() > searchModel.getResultsPerPage()) {
//					List<Transaction> currentList = results.subList(0, searchModel.getResultsPerPage());
//					grid.setItems(currentList);
//				} else {
//					grid.setItems(results);
//				}
//				
//				int pageCount = results.size() / searchModel.getResultsPerPage() + 1;
//				grid.getControlsLayout().setCurrentPageIndex(1);
////				grid.getControlsLayout().setPageCount(pageCount);
//				grid.getControlsLayout().setSizePerPage(searchModel.getResultsPerPage());
//			}
//    	};
//    	AdvancedSearchWindow.open(callback, searchModel);
//    }
//    
//    private void basicSearch() {
//    	ResultCallback callback = new ResultCallback() {
//
//			@Override
//			public void onSuccessful(List<Transaction> results) {
//				grid.setAllData(results);
//				
//				if (results.size() > searchModel.getResultsPerPage()) {
//					List<Transaction> currentList = results.subList(0, searchModel.getResultsPerPage());
//					grid.setItems(currentList);
//				} else {
//					grid.setItems(results);
//				}
//				
//				int pageCount = results.size() / searchModel.getResultsPerPage() + 1;
//				grid.getControlsLayout().setCurrentPageIndex(1);
////				grid.getControlsLayout().setPageCount(pageCount);
//				grid.getControlsLayout().setSizePerPage(searchModel.getResultsPerPage());
//			}
//    	};
//    	BasicSearchWindow.open(callback);
//    }
    
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
	public void cleanStage() {
		// TODO Auto-generated method stub
		
	}
	
    public Transaction getCurrentTransaction() {
    	return transaction;
    }
    
    
    private User loggedInUser;	//登录用户
    private Label titleLabel;
    private Window notificationsWindow;
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>无查询结果</span>", ContentMode.HTML);
    private Transaction transaction = null;
    private SearchResultsGrid grid = new SearchResultsGrid();
}
