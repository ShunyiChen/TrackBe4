package com.maxtree.automotive.dashboard.view.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.NotificationsPopup;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutListener;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
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
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
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
        root.addComponent(grid);
        root.setExpandRatio(grid, 1.0f);
        
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
	
	/**
	 * 
	 */
	private void startPolling() {
		SystemConfiguration sc = Yaml.readSystemConfiguration();
		ui.setPollInterval(sc.getInterval());
		ui.addPollListener(new UIEvents.PollListener() {
			/** */
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
        titleLabel = new Label("车辆查询");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        HorizontalLayout searchbar = new HorizontalLayout();
        searchbar.setSpacing(false);
        searchbar.setMargin(false);
        searchbar.setWidthUndefined();
        communityBox.setWidth("100px");
        communityBox.setHeight("30px");
        communityBox.setEmptySelectionAllowed(false);
        Community current = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
        List<Community> list = ui.communityService.findAll();
        List<Community> items = new ArrayList<Community>();
        for(Community c : list) {
        	if(current.getGroupId() == c.getGroupId()
        			&& current.getLevel() <= c.getLevel()) {
        		items.add(c);
        	}
        	if(current.getCommunityUniqueId() == c.getCommunityUniqueId()) {
        		current = c;
        	}
        }
        communityBox.setItems(items);
        communityBox.setSelectedItem(current);
        communityBox.setHeight("30px");
        keywordField.setWidth("400px");
        keywordField.setHeight("30px");
        keywordField.setPlaceholder("请输入车牌号\\车架号\\业务流水号");
        ShortcutListener enter = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				doSearch();
			}
		};
        keywordField.addShortcutListener(enter);
        Button searchButton = new Button();
        searchButton.addClickListener(e->{
        	doSearch();
        });
        searchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        searchButton.setIcon(VaadinIcons.SEARCH);
        Label textLabel = new Label("选择社区:");
        
        // 号牌种类
        Label plateTypeLabel = new Label("号牌种类:");
        List<String> types = ui.dataItemService.findNamesByType(1);
		plateTypeField.setItems(types);
		plateTypeField.setEmptySelectionAllowed(false);
		plateTypeField.setWidth("140px");
		plateTypeField.setHeight("30px");
		// 号牌号码
		Label plateNumberLabel = new Label("号牌号码:");
		Label plateNumberTitle = new Label(""+addr.getLicenseplate());
		plateNumberField.setWidth("140px");
		plateNumberField.setHeight("30px");
		// 车辆识别代号
		Label vinLabel = new Label("或者 车辆识别代号:");
		vinField.setWidth("220px");
		vinField.setHeight("30px");
        List<String> data = Arrays.asList("基本查询","模糊查询");
        radio.setItems(data);
        radio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        radio.setItemCaptionGenerator(item -> item);
        radio.addValueChangeListener(e ->{
        	if(e.getValue().equals("模糊查询")) {
        		searchbar.removeAllComponents();
                searchbar.addComponents(radio,textLabel,communityBox,Box.createHorizontalBox(3),keywordField,Box.createHorizontalBox(3),searchButton);
                searchbar.setComponentAlignment(radio, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(communityBox, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(keywordField, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(searchButton, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(textLabel, Alignment.MIDDLE_LEFT);
        	} 
        	else {
        		searchbar.removeAllComponents();
                searchbar.addComponents(radio,textLabel,communityBox,
                		Box.createHorizontalBox(3),plateTypeLabel,plateTypeField,
                		Box.createHorizontalBox(3),plateNumberLabel,plateNumberTitle,plateNumberField,
                		Box.createHorizontalBox(3),vinLabel,vinField,
                		Box.createHorizontalBox(3),searchButton);
                searchbar.setComponentAlignment(radio, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(communityBox, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(textLabel, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(plateTypeLabel, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(plateTypeField, Alignment.MIDDLE_LEFT);
                
                searchbar.setComponentAlignment(plateNumberLabel, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(plateNumberTitle, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(plateNumberField, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(vinLabel, Alignment.MIDDLE_LEFT);
                searchbar.setComponentAlignment(vinField, Alignment.MIDDLE_LEFT);
                
                searchbar.setComponentAlignment(searchButton, Alignment.MIDDLE_LEFT);
                
        	}
        });
        radio.setSelectedItem(data.get(0));
        header.addComponents(titleLabel,searchbar);
        buildNotificationsButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        return header;
    }

    /**
     * 
     */
    private void doSearch() {
    	String communityName = communityBox.getSelectedItem().get().getCommunityName();
		if(StringUtils.isEmpty(communityName)) {
			Notifications.warning("没有可用的社区选项。");
			return;
		}
    	if(radio.getValue().equals("基本查询")) {
    		grid.setCommunityName(communityName);
    		grid.setPlateType(plateTypeField.getValue());
    		grid.setPlateNumber(plateNumberField.getValue());
    		grid.setVin(vinField.getValue());
    		grid.executeByPlateNumberOrVIN();
    	}
    	else {
			grid.setCommunityName(communityName);
        	grid.setKeyword(keywordField.getValue());
        	grid.executeByKeyword();
    	}
    }
    
    /**
     * 
     * @param event
     */
//    private void openNotificationsPopup(final ClickEvent event) {
//    	VerticalLayout mainVLayout = new VerticalLayout();
//    	mainVLayout.setSpacing(false);
//        Label title = new Label("事件提醒");
//        title.addStyleName(ValoTheme.LABEL_H3);
//        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        mainVLayout.addComponent(title);
//
//        Panel scrollPane = new Panel();
//    	scrollPane.addStyleName("reminder-scrollpane");
//    	scrollPane.setHeight("250px");
//    	VerticalLayout listLayout = new VerticalLayout();
//    	List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(loggedInUser, DashboardViewType.SEARCH.getViewName());
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
//        	
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
//            notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
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
     * 提醒按钮
     * 
     * @return
     */
    private void buildNotificationsButton() {
        notificationsButton = new NotificationsButton();
    	notificationsButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                popup.open(event);
            }
        });
    }
    
    @Override
	public void updateUnreadCount() {
    	List<Notification> notifications = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
		int unreadCount = 0;
		for (Notification n : notifications) {
			if (n.getViewName().equals(DashboardViewType.SEARCH.getViewName())
					|| n.getViewName().equals("")) {
				unreadCount++;
			}
		}
		
		DashboardMenu.getInstance().searchCount(unreadCount);
		notificationsButton.setUnreadCount(unreadCount);
	}

	@Override
	public void cleanStage() {
	}
	
    public Transaction getCurrentTransaction() {
    	return transaction;
    }
    
    
    private RadioButtonGroup<String> radio = new RadioButtonGroup<>(null);
    private ComboBox<Community> communityBox = new ComboBox<Community>();// community box
    private ComboBox<String> plateTypeField = new ComboBox<>(); //号牌种类
    private TextField plateNumberField = new TextField();//号牌号码
    private TextField vinField = new TextField();//车辆识别代号
    private TextField keywordField = new TextField();
    
    private Address addr = Yaml.readAddress();
    private MessageBodyParser jsonHelper = new MessageBodyParser();
    private User loggedInUser;	//登录用户
    private Label titleLabel;
    private Window notificationsWindow;
    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private NotificationsButton notificationsButton;
    private Transaction transaction = null;
    private SearchResultsGrid grid = new SearchResultsGrid();
    private Label blankLabel = new Label("<span style='font-size:30px;color: #8D99A6;font-family: Microsoft YaHei;'>无查询结果</span>", ContentMode.HTML);
    private NotificationsPopup popup = new NotificationsPopup(DashboardViewType.SEARCH.getViewName());
}
