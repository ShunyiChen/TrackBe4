package com.maxtree.automotive.dashboard.view.shelf;

import java.util.Date;
import java.util.List;

import com.maxtree.automotive.dashboard.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.NotificationsPopup;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.FrontendViewIF;
import com.maxtree.automotive.dashboard.view.quality.QCView;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutListener;
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
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.schlichtherle.license.LicenseContent;

/**
 * 
 * @author Chen
 *
 */
public class ShelfView extends Panel implements View, FrontendViewIF{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(QCView.class);
	
	/**
	 * 
	 */
	public ShelfView() {
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
        main.addSelectedTabChangeListener(e->{
        	// Find the tabsheet
            TabSheet tabsheet = e.getTabSheet();
            // Find the tab (here we know it's a layout)
            Layout tab = (Layout) tabsheet.getSelectedTab();
            // Get the tab caption from the tab object
            String caption = tabsheet.getTab(tab).getCaption();
        	if (caption.equals(TAB1_TITLE)) {
        		tools.removeComponent(btnDown);
        		tools.addComponent(btnUp, 0);
        	} else {
        		tools.removeComponent(btnUp);
        		tools.addComponent(btnDown, 0);
        	}
        });
        main.setHeight(100.0f, Unit.PERCENTAGE);
        main.addStyleName(ValoTheme.TABSHEET_FRAMED);
        main.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        main.addTab(upgrid, TAB1_TITLE);
        main.addTab(downgrid, TAB2_TITLE);
        root.addComponents(main);
        root.setExpandRatio(main, 1.0f);
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
        titleLabel = new Label("上架下架");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        HorizontalLayout searchbar = new HorizontalLayout();
        searchbar.setSpacing(false);
        searchbar.setMargin(false);
        searchbar.setWidthUndefined();
        
        String plate = Yaml.readSystemConfiguration().getLicenseplate();
        Label location = new Label(plate);
        TextField searchField = new TextField();
        searchField.setWidth("400px");
        searchField.setPlaceholder("请输入车牌号后5位或后6位。 例如:B8K57");
        ShortcutListener enter = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				// Find the tabsheet
	            // Find the tab (here we know it's a layout)
	            Layout tab = (Layout) main.getSelectedTab();
	            // Get the tab caption from the tab object
	            String caption = main.getTab(tab).getCaption();
	        	if (caption.equals(TAB1_TITLE)) {
	        		upgrid.setKeyword(searchField.getValue());
	        		upgrid.execute();
	        	} else {
	        		downgrid.setKeyword(searchField.getValue());
	        		downgrid.execute();
	        	}
				
			}
		};
        searchField.addShortcutListener(enter);
        Button searchButton = new Button();
        searchButton.addClickListener(e->{
        	Layout tab = (Layout) main.getSelectedTab();
            // Get the tab caption from the tab object
            String caption = main.getTab(tab).getCaption();
        	if (caption.equals(TAB1_TITLE)) {
        		upgrid.setKeyword(searchField.getValue());
        		upgrid.execute();
        	} else {
        		downgrid.setKeyword(searchField.getValue());
        		downgrid.execute();
        	}
        });
        searchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        searchButton.setIcon(VaadinIcons.SEARCH);
        searchbar.addComponents(location,Box.createHorizontalBox(5),searchField,searchButton);
        searchbar.setComponentAlignment(location, Alignment.MIDDLE_LEFT);
        
        header.addComponents(titleLabel, searchbar);
        
        buildNotificationsButton();
        buildUpButton();
        buildDownButton();
        tools = new HorizontalLayout(btnUp,notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        return header;
    }
    
    /**
     * 
     */
    private void buildUpButton() {
		btnUp.setId(EDIT_ID);
		btnUp.setIcon(VaadinIcons.ARROW_UP);
		btnUp.addStyleName("icon-edit");
		btnUp.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btnUp.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnUp.setDescription(TAB1_TITLE);
		btnUp.addClickListener(e -> {
			putaway();
		});
	}
    
    /**
     * 
     */
    private void buildDownButton() {
    	btnDown.setId(EDIT_ID);
    	btnDown.setIcon(VaadinIcons.ARROW_DOWN);
    	btnDown.addStyleName("icon-edit");
    	btnDown.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
    	btnDown.addStyleName(ValoTheme.BUTTON_PRIMARY);
    	btnDown.setDescription(TAB2_TITLE);
    	btnDown.addClickListener(e -> {
    		removeOff();
		});
	}
    
    /**
     * 上架
     */
    private void putaway() {
    	if(upgrid.editableTrans == null) {
    		Notifications.warning("请选择一行上架。");
    	} else {
    		upgrid.editableTrans.setStatus(ui.state().getName("B3"));
    		upgrid.editableTrans.setDateModified(new Date());
    		ui.transactionService.update(upgrid.editableTrans);
    		track(ui.state().getName("B3"), upgrid.editableTrans);
    		upgrid.clearSortOrder();
    		Notifications.bottomWarning("操作成功。");
    	}
    }
    
    /**
     * 下架
     */
    private void removeOff() {
    	if(downgrid.editableTrans == null) {
    		Notifications.warning("请选择一行下架。");
    	} else {
    		Callback okEvent = new Callback() {

				@Override
				public void onSuccessful() {
					//更新库房
		    		ui.frameService.updateVIN(null, downgrid.editableTrans.getCode());
		    		//更新transaction
		    		downgrid.editableTrans.setStatus(ui.state().getName("B12"));
		    		downgrid.editableTrans.setDateModified(new Date());
		    		downgrid.editableTrans.setCode(null);
		    		ui.transactionService.update(downgrid.editableTrans);
		    		track(ui.state().getName("B12"),downgrid.editableTrans);
		    		downgrid.clearSortOrder();
		    		Notifications.bottomWarning("操作成功。");
				}
    		};
    		MessageBox.showMessage("提示", "上架号为"+downgrid.editableTrans.getCode()+"，请确认是否下架？", MessageBox.WARNING, okEvent, "确定");
    	}
    }
    
    /**
     * 
     * @param status
     * @param trans
     */
    private void track(String status, Transaction trans) {
    	// 插入移行表
		Transition transition = new Transition();
//		transition.setTransactionUUID(trans.getUuid());
		transition.setVin(trans.getVin());
		transition.setActivity(status);
		transition.setComments(null);
		transition.setOperator(loggedInUser.getUserName());
		transition.setDateCreated(new Date());
		int transitionUniqueId = ui.transitionService.insert(transition,trans.getVin());
    }

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
//    	scrollPane.setHeight("220px");
//    	scrollPane.setWidth("100%");
//        VerticalLayout listLayout = new VerticalLayout();
//        List<Map<String, Object>> allMessages = ui.messagingService.findAllMessagesByUser(loggedInUser, DashboardViewType.SHELF.getViewName());
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
			if (n.getViewName().equals(DashboardViewType.SHELF.getViewName())
					|| n.getViewName().equals("")) {
				unreadCount++;
			}
		}
   		
   		notificationsButton.setUnreadCount(unreadCount);
   		DashboardMenu.getInstance().shelfCount(unreadCount);
   	}

   	@Override
   	public void cleanStage() {
   	}
    
   	public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    private static final String TAB1_TITLE = "上架";
    private static final String TAB2_TITLE = "下架";
   	private User loggedInUser;
    private Label titleLabel;
    private VerticalLayout root;
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private NotificationsButton notificationsButton;
    private Button btnUp = new Button("上架");
    private Button btnDown = new Button("下架");
    private HorizontalLayout tools = null;
    private TabSheet main = new TabSheet();
    private UpGrid upgrid = new UpGrid();
    private DownGrid downgrid = new DownGrid();
    private NotificationsPopup popup = new NotificationsPopup(DashboardViewType.SHELF.getViewName());
}
