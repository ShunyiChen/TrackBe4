package com.maxtree.automotive.dashboard.view.finalcheck;

import java.util.Date;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.LocationCode;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.NotificationsPopup;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Car;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.FinalCheck;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
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
import com.vaadin.ui.TextField;
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
public class FinalCheckView extends Panel implements View, FrontendViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FinalCheckView() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		community = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
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
        
        main.setSpacing(false);
        main.setMargin(false);
        main.addStyleName("main-check");
        main.setWidth("100%");
        main.setHeight("100%");
        main.addComponents(blankLabel);
        main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
        root.addComponents(main);
        root.setExpandRatio(main, 1f);
        loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
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
	
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Label titleLabel = new Label("最终审档");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        buildSubmittButton();
        buildNotificationsButton();
        buildSearchBar();
        Button searchButton = new Button();
        searchButton.addClickListener(e->{
        	doSearch();
        });
        searchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        searchButton.setIcon(VaadinIcons.SEARCH);
        Label inputlabel = new Label("按流水号查询:");
        submit.addClickListener(e->{
        	submit();
        });
        HorizontalLayout tools = new HorizontalLayout(inputlabel,searchField,searchButton,submit,notificationsButton);
        tools.setComponentAlignment(inputlabel, Alignment.MIDDLE_RIGHT);
        tools.setComponentAlignment(searchField, Alignment.MIDDLE_RIGHT);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        
        return header;
    }
    
    /**
     * 
     */
    private void doSearch() {
    	String barcode = searchField.getValue();
    	FinalCheck car = ui.transactionService.findFinalCheck(barcode);
    	if(car == null) {
    		Notifications.warning("找不到该车辆。");
    		cleanStage();
    		return;
    	}
    	trans = ui.transactionService.findByBarcode(barcode, car.getVin());
    	mainPage.load(trans);
    	resetComponents();
    	
//    	LocationCode locationCode = new LocationCode(ui.locationService);
//    	List<Transaction> list = ui.transactionService.search_by_keyword(-1, 0, barcode, community.getTenantName(), locationCode.getCompleteLocationCode(community));
//    	if(list.size() > 0) {
//    		trans = list.get(0);
//        	mainPage.load(trans);
//        	resetComponents();
//    	}
//    	else {
//    		Notifications.warning("找不到该业务流水号。");
//    	}
    }
    
    /**
     * 
     */
    private void submit() {
    	if(trans == null) {
    		Notifications.warning("没有可提交的业务。");
    	}
    	else {
    		trans.setStatus(ui.state().getName("B18"));
    		trans.setDateModified(new Date());
    		ui.transactionService.updateStatus(trans.getVin(), trans.getUuid(), trans.getStatus());
    		
    		track(trans.getStatus());
    		
    		//清空舞台
        	cleanStage();
        	Notifications.bottomWarning("提交成功！已改为待入库状态。");
    	}
    }
    
    /**
     * 
     * @param status
     */
    private void track(String status) {
    	// 插入移行表
		Transition transition = new Transition();
		transition.setTransactionUUID(trans.getUuid());
		transition.setVin(trans.getVin());
		transition.setActivity(status);
		transition.setComments(null);
		transition.setOperator(loggedInUser.getUserName());
		transition.setDateCreated(new Date());
		ui.transitionService.insert(transition, trans.getVin());
    }
    
//    @Override
//    public void beforeLeave(final ViewBeforeLeaveEvent event) {
//    	cleanStage();
//    }
    
    @Override
    public void enter(final ViewChangeEvent event) {
    	cleanStage();
    }
    
    /**
     * 
     */
    private void buildSearchBar() {
    	searchField.setPlaceholder("请输入业务流水号");
    	searchField.setHeight("30px");
    	ShortcutListener enterListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
 			/**
 			 * 
 			 */
 			private static final long serialVersionUID = 1L;

 			@Override
 			public void handleAction(Object sender, Object target) {
 				doSearch();
 			}
 		};
    	searchField.addShortcutListener(enterListener);
    }
    
    /**
     * 
     */
    private void buildSubmittButton() {
    	submit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	submit.setIcon(VaadinIcons.CLOUD_UPLOAD);
    	submit.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(final ClickEvent event) {
            }
        });
    }
    
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
    
    private Component buildSparklines() {
    	 CssLayout sparks = new CssLayout();
         sparks.addStyleName("sparks");
         sparks.setWidth("100%");
         Responsive.makeResponsive(sparks);
         return sparks;
    }
	
	@Override
	public void updateUnreadCount() {
		List<Notification> notifications = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
    	int unreadCount = 0;
		for (Notification n : notifications) {
			if (n.getViewName().equals(DashboardViewType.FINAL_CHECK.getViewName())
					|| n.getViewName().equals("")) {
				unreadCount++;
			}
		}
		// 更新通知未读数
		notificationsButton.setUnreadCount(unreadCount);
		DashboardMenu.getInstance().finalCheckCount(unreadCount);
	}

	@Override
	public void cleanStage() {
		main.removeAllComponents();
		main.setHeight("100%");
		main.addComponents(blankLabel);
		main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
	}
	
	/**
	 * 
	 */
	private void resetComponents() {
		main.removeAllComponents();
    	main.setHeightUndefined();
    	main.addComponent(mainPage);
    	main.setComponentAlignment(mainPage, Alignment.TOP_CENTER);
    	main.setExpandRatio(mainPage, 1);
	}
	
	private Transaction trans = null;
	private TextField searchField = new TextField();
	private MainPane mainPage = new MainPane();
	private NotificationsPopup popup = new NotificationsPopup(DashboardViewType.DOUBLECHECK.getViewName());
	private User loggedInUser;
	private Community community;
	private Button submit = new Button();
	private NotificationsButton notificationsButton;
	private Window notificationsWindow;
	private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
	private VerticalLayout root;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private TB4MessagingSystem messageSystem = new TB4MessagingSystem();
}
