package com.maxtree.automotive.dashboard.view;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.component.ChangePasswordWindow;
import com.maxtree.automotive.dashboard.component.ProfilePreferencesWindow;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({ "serial", "unchecked" })
public final class DashboardMenu extends CustomComponent {
    public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private Label inputBadge;
    private Label qualityBadge;
    private Label checkBadge;
    private Label doubleCheckBadge;
    private Label searchBadge;
    private Label shelfBadge;
    private Label imagingAdminBadge;
    private Label imagingInputBadge;
    private Label imagingQualityBadge;
    private Label finalCheckBadge;
    
    private MenuItem settingsItem;
    
    public static DashboardMenu DASHBOARD_MENU = null;
    
    public static DashboardMenu getInstance() { 
    	return DASHBOARD_MENU;
    }
    
    public DashboardMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        DASHBOARD_MENU = this;
        
        // There's only one DashboardMenu per UI so this doesn't need to be
        // unregistered from the UI-scoped DashboardEventBus.
        DashboardEventBus.register(this);

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("<font size=\"2px\" face=\"Microsoft YaHei\" color=\"white\">TrackBe4</font>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }

    private User getCurrentUser() {
        String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
        User loggedInUser = ui.userService.getUserByUserName(username);
        return loggedInUser;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        settings.addStyleName("v-menubar-menuitem:hover");
        final User user = getCurrentUser();
        
        Resource res = new ThemeResource(user.getProfile().getPicture());
        settingsItem = settings.addItem("", res, null);
        // new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(null);
        settingsItem.addItem("编辑个人资料", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                ProfilePreferencesWindow.open(user, false);
            }
        });
        settingsItem.addItem("首选项", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                ProfilePreferencesWindow.open(user, true);
            }
        });
        settingsItem.addItem("修改密码", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
            	ChangePasswordWindow.open(user);
            }
        });
        
        settingsItem.addSeparator();
        settingsItem.addItem("退 出", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                DashboardEventBus.post(new DashboardEvent.UserLoggedOutEvent());
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        // 不同角色登录不同界面
        String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
        User user = ui.userService.getUserByUserName(username);
        for (final DashboardViewType view : DashboardViewType.values()) {
        	// 前台录入check
        	if (view == DashboardViewType.INPUT) {
        		if(!user.isPermitted(PermissionCodes.A3)) {
            		continue;
            	}
        	} 
        	// 质检
        	else if (view == DashboardViewType.QUALITY) {
        		if(!user.isPermitted(PermissionCodes.A4)) {
            		continue;
            	}
        	}
        	// 审档
        	else if (view == DashboardViewType.CHECK) {
        		if(!user.isPermitted(PermissionCodes.A5)) {
            		continue;
            	}
        	}
        	// 确认审档
        	else if (view == DashboardViewType.DOUBLECHECK) {
        		if(!user.isPermitted(PermissionCodes.A6)) {
            		continue;
            	}
        	}
        	// 查询数据
        	else if (view == DashboardViewType.SEARCH) {
        		if(!user.isPermitted(PermissionCodes.A7)) {
            		continue;
            	}
        	}
        	// 上架下架
        	else if (view == DashboardViewType.SHELF) {
        		if(!user.isPermitted(PermissionCodes.A8)) {
            		continue;
            	}
        	}
        	// 影像化管理员
        	else if (view == DashboardViewType.IMAGING_MANAGER) {
        		if(!user.isPermitted(PermissionCodes.A9)) {
            		continue;
            	}
        	}
        	// 影像化录入
        	else if (view == DashboardViewType.IMAGING_INPUT) {
        		if(!user.isPermitted(PermissionCodes.A10)) {
            		continue;
            	}
        	}
        	// 影像化质检
        	else if (view == DashboardViewType.IMAGING_QUALITY) {
        		if(!user.isPermitted(PermissionCodes.A11)) {
            		continue;
            	}
        	}
        	//终审
        	else if (view == DashboardViewType.FINAL_CHECK) {
        		if(!user.isPermitted(PermissionCodes.A12)) {
            		continue;
            	}
        	}
        	// 保存首个界面名称
        	Object firstView = VaadinSession.getCurrent().getAttribute(user.getUserUniqueId()+"");
        	if (firstView == null) {
        		VaadinSession.getCurrent().setAttribute(user.getUserUniqueId()+"", view.getViewName());
        	}
            Component menuItemComponent = new ValoMenuItemButton(view);
            if (view == DashboardViewType.INPUT) {
                inputBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, inputBadge);
            }
            else if (view == DashboardViewType.QUALITY) {
            	qualityBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, qualityBadge);
            }
            else if (view == DashboardViewType.CHECK) {
            	checkBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, checkBadge);
            }
            else if (view == DashboardViewType.DOUBLECHECK) {
            	doubleCheckBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, doubleCheckBadge);
            }
            else if (view == DashboardViewType.SEARCH) {
            	searchBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, searchBadge);
            }
            else if (view == DashboardViewType.SHELF) {
            	shelfBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, shelfBadge);
            }
            else if (view == DashboardViewType.IMAGING_MANAGER) {
            	imagingAdminBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, imagingAdminBadge);
            }
            else if (view == DashboardViewType.IMAGING_INPUT) {
            	imagingInputBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, imagingInputBadge);
            }
            else if (view == DashboardViewType.IMAGING_QUALITY) {
            	imagingQualityBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, imagingQualityBadge);
            }
            else if (view == DashboardViewType.FINAL_CHECK) {
            	finalCheckBadge = new Label();
                menuItemComponent = buildBadgeWrapper(menuItemComponent, finalCheckBadge);
            }
            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;
    }

    private Component buildBadgeWrapper(final Component menuItemButton,
            final Component badgeLabel) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(false);
        dashboardWrapper.addComponent(badgeLabel);
        return dashboardWrapper;
    }

    @Override
    public void attach() {
        super.attach();
        updateNotificationsCount(0);
    }

    @Subscribe
    public void postViewChange(final DashboardEvent.PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    /**
     * 前台
     * @param count
     */
    @Subscribe
    public void updateNotificationsCount(int count) {
    	if (inputBadge != null) {
            inputBadge.setValue(String.valueOf(count));
            inputBadge.setVisible(count > 0);
    	}
    }

    /**
     * 质检
     * @param count
     */
    @Subscribe
    public void qcCount(int count) {
    	if (qualityBadge != null) {
    		qualityBadge.setValue(String.valueOf(count));
        	qualityBadge.setVisible(count > 0);
    	}
    }
    
    /**
     * 业务审核
     * @param count
     */
    @Subscribe
    public void checkerCount(int count) {
    	if (checkBadge != null) {
    		checkBadge.setValue(String.valueOf(count));
    		checkBadge.setVisible(count > 0);
    	}
    }
    
    @Subscribe
    public void doubleCheckCount(int count) {
    	if (doubleCheckBadge != null) {
    		doubleCheckBadge.setValue(String.valueOf(count));
    		doubleCheckBadge.setVisible(count > 0);
    	}
    }
    
    @Subscribe
    public void searchCount(int count) {
    	if (searchBadge != null) {
    		searchBadge.setValue(String.valueOf(count));
    		searchBadge.setVisible(count > 0);
    	}
    }
    
    @Subscribe
    public void shelfCount(int count) {
    	if (shelfBadge != null) {
    		shelfBadge.setValue(String.valueOf(count));
    		shelfBadge.setVisible(count > 0);
    	}
    }
    
    @Subscribe
    public void imagingAdminCount(int count) {
    	if (imagingAdminBadge != null) {
    		imagingAdminBadge.setValue(String.valueOf(count));
    		imagingAdminBadge.setVisible(count > 0);
    	}
    }

    @Subscribe
    public void imagingInputCount(int count) {
    	if (imagingInputBadge != null) {
    		imagingInputBadge.setValue(String.valueOf(count));
    		imagingInputBadge.setVisible(count > 0);
    	}
    }
    
    @Subscribe
    public void imagingQualityCount(int count) {
    	if (imagingQualityBadge != null) {
    		imagingQualityBadge.setValue(String.valueOf(count));
    		imagingQualityBadge.setVisible(count > 0);
    	}
    }
    
    @Subscribe
    public void finalCheckCount(int count) {
    	if (finalCheckBadge != null) {
    		finalCheckBadge.setValue(String.valueOf(count));
    		finalCheckBadge.setVisible(count > 0);
    	}
    }

    @Subscribe
    public void updateUserName(final DashboardEvent.ProfileUpdatedEvent event) {
        User user = getCurrentUser();
        settingsItem.setText(user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
    }

    @Subscribe
    public void updateUserPicture(Resource res) {
    	settingsItem.setIcon(res);
    }
    
    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final DashboardViewType view;

        public ValoMenuItemButton(final DashboardViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
//            setCaption(view.getViewName().substring(0, 1).toUpperCase()
//                    + view.getViewName().substring(1));
            this.setWidth("30px");

            DashboardEventBus.register(this);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().getNavigator().navigateTo(view.getViewName());
                }
            });
        }

        @Subscribe
        public void postViewChange(final DashboardEvent.PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }
}
