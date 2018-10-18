package com.maxtree.automotive.dashboard.view.admin;

import java.util.Date;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.AdminMenuWindow;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.domain.SystemSettings;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.Commands;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.schlichtherle.license.LicenseContent;

public class AdminMainView extends AbsoluteLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public AdminMainView() {
        setSizeFull();
        screen.addLayoutClickListener(e->{
        	hideNavigationBar();
        	System.out.println("hideNavigationBar");
        });
        
        
        
		// 搜索
//		searchPane = createSearchPane();
        // 客户信息
//		customerInformationLayout = customerInformation();
//		// 社区
//		communityLayout = community();
//		// 库房
//		storehouseLayout = storehouse();
//		// 数据存储
//		dataLayout = dataStorage();
//		// 消息
//		messageLayout = broadcastMessage();
//		// 系统
//		systemLayout = systemSettings();
//		// 帮助
//		helpLayout = help();
//	 	main.setSpacing(false);
//	 	main.setMargin(false);
//	 	main.setWidth("98%");
//	 	main.setHeight("98%");
//	 	main.addComponents(customerInformationLayout,communityLayout,storehouseLayout,dataLayout,messageLayout,systemLayout,helpLayout);
//	 	main.setComponentAlignment(customerInformationLayout, Alignment.TOP_CENTER);
//	 	main.setComponentAlignment(communityLayout, Alignment.TOP_CENTER);
//	 	main.setComponentAlignment(storehouseLayout, Alignment.TOP_CENTER);
//	 	main.setComponentAlignment(dataLayout, Alignment.TOP_CENTER);
//	 	main.setComponentAlignment(messageLayout, Alignment.TOP_CENTER);
//	 	main.setComponentAlignment(systemLayout, Alignment.TOP_CENTER);
//	 	main.setComponentAlignment(helpLayout, Alignment.TOP_CENTER);
        
	 	
	 	
	 	this.addComponent(toolbar, "left: 0px; top: 0px;");
	 	this.addComponent(table, "left: 0px; top: 56px;");
	 	
	 	
//        addComponents(searchPane, main);
//        setComponentAlignment(searchPane, Alignment.TOP_CENTER);
//        setComponentAlignment(main, Alignment.TOP_CENTER);

//        Page.getCurrent().addBrowserWindowResizeListener(e -> {
//        	manageBusinessType.setHeight((e.getHeight()-58)+"px");
//        	manageSites.setHeight((e.getHeight()-58)+"px");
//        });
        
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
							
							LicenseContent main = (LicenseContent) objects[0];
					        long endTimes = main.getNotAfter().getTime();
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
	public void showNavigationBar() {
		navigationBar.removeStyleName("NavigationBar_moveOut");
		navigationBar.addStyleName("NavigationBar_moveIn");
		
		addComponent(screen, "left: 0px; top: 0px;");
		addComponent(navigationBar, "left: 0px; top: 0px;z-index:999;");
		System.out.println("+++"+this.getComponentCount());
	}
	
	public void hideNavigationBar() {
		navigationBar.removeStyleName("NavigationBar_moveIn");
		navigationBar.addStyleName("NavigationBar_moveOut");
		
		
		System.out.println("----"+this.getComponentCount());
		addComponent(navigationBar, "left: -255px; top: 0px;z-index:999;");
		removeComponent(screen);
	}
	
	
	/**
	 * 客户信息模块
	 * 
	 * @return
	 */
	private VerticalLayout customerInformation() {
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		
		Label label = new Label("用户信息");
		label.addStyleName("setting-text");
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false);
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("250px");
        vmain.addStyleName("setting-layout-shadow");
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
		
        // 管理员
        HorizontalLayout rowRoot = new HorizontalLayout();
        rowRoot.setMargin(false);
        rowRoot.setSpacing(false);
        rowRoot.setWidth("100%");
        rowRoot.setHeight("48px");
        HorizontalLayout row0 = new HorizontalLayout();
        row0.setMargin(false);
        row0.setSpacing(false);
        row0.addStyleName("detail-hlayout");
        row0.setWidth("100%");
        row0.setHeight("48px");
        User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        Label currentUserName = new Label(loginUser.getUserName());
        userAvatar = new Image(null, new ThemeResource(loginUser.getProfile().getPicture()));
        userAvatar.setWidth("40px");
        userAvatar.setHeight("40px");
        Image rightArrow0 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        subRow = new HorizontalLayout();
        subRow.setMargin(false);
        subRow.setSpacing(false);
        subRow.setWidth("120px");
        subRow.setHeight("100%");
        subRow.addComponents(userAvatar, currentUserName);
        subRow.setComponentAlignment(userAvatar, Alignment.MIDDLE_LEFT);
        subRow.setComponentAlignment(currentUserName, Alignment.MIDDLE_LEFT);
        row0.addComponents(subRow, rightArrow0);
        row0.setComponentAlignment(subRow, Alignment.MIDDLE_LEFT);
        row0.setComponentAlignment(rightArrow0, Alignment.MIDDLE_RIGHT);
        row0.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.B1)) {
        		showDetailPane(Commands.EDIT_PROFILE);
            	hidePanes();
        	} else {
        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
        	}
        });
        
        // 管理用户
        HorizontalLayout row4 = new HorizontalLayout();
        row4.setMargin(false);
        row4.setSpacing(false);
        row4.addStyleName("detail-hlayout");
        row4.setWidth("100%");
        row4.setHeight("48px");
        Label manageUsers = new Label("管理用户");
        manageUsers.addStyleName("detail-setting-text");
        Image rightArrow4 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row4.addComponents(manageUsers, rightArrow4);
        row4.setComponentAlignment(manageUsers, Alignment.MIDDLE_LEFT);
        row4.setComponentAlignment(rightArrow4, Alignment.MIDDLE_RIGHT);
        row4.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.C4)) {
        		showDetailPane(Commands.MANAGE_USERS);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
        
        // 管理角色
        HorizontalLayout row5 = new HorizontalLayout();
        row5.setMargin(false);
        row5.setSpacing(false);
        row5.addStyleName("detail-hlayout");
        row5.setWidth("100%");
        row5.setHeight("48px");
        Label manageRoles = new Label("管理角色");
        manageRoles.addStyleName("detail-setting-text");
        Image rightArrow5 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row5.addComponents(manageRoles, rightArrow5);
        row5.setComponentAlignment(manageRoles, Alignment.MIDDLE_LEFT);
        row5.setComponentAlignment(rightArrow5, Alignment.MIDDLE_RIGHT);
        row5.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.D6)) {
        		showDetailPane(Commands.MANAGE_ROLES);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        });
        
        // 管理权限
        HorizontalLayout row6 = new HorizontalLayout();
        row6.setMargin(false);
        row6.setSpacing(false);
        row6.addStyleName("detail-hlayout");
        row6.setWidth("100%");
        row6.setHeight("48px");
        Label managePermissions = new Label("管理权限");
        managePermissions.addStyleName("detail-setting-text");
        Image rightArrow6 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row6.addComponents(managePermissions, rightArrow6);
        row6.setComponentAlignment(managePermissions, Alignment.MIDDLE_LEFT);
        row6.setComponentAlignment(rightArrow6, Alignment.MIDDLE_RIGHT);
        row6.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.E3)) {
        		showDetailPane(Commands.MANAGE_PERMISSIONS);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
        
        // 管理机构
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setMargin(false);
        row1.setSpacing(false);
        row1.addStyleName("detail-hlayout");
        row1.setWidth("100%");
        row1.setHeight("48px");
        Label manageCompanies = new Label("管理机构");
        manageCompanies.addStyleName("detail-setting-text");
        Image rightArrow = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row1.addComponents(manageCompanies, rightArrow);
        row1.setComponentAlignment(manageCompanies, Alignment.MIDDLE_LEFT);
        row1.setComponentAlignment(rightArrow, Alignment.MIDDLE_RIGHT);
        row1.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.F6)) {
        		showDetailPane(Commands.MANAGE_COMPANIES);
            	hidePanes();
        	} else {
        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
        	}
        	
        });
        
        Component exitButton = buildExitButton();
        HorizontalLayout buttonHLayout = new HorizontalLayout();
        buttonHLayout.setMargin(false);
        buttonHLayout.setSpacing(false);
        buttonHLayout.setWidth("100%");
        buttonHLayout.setHeight("100%");
        buttonHLayout.addComponent(exitButton);
        buttonHLayout.setComponentAlignment(exitButton, Alignment.MIDDLE_CENTER);
        
        rowRoot.addComponents(row0, buttonHLayout);
        rowRoot.setComponentAlignment(row0, Alignment.TOP_LEFT);
        rowRoot.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_RIGHT);
        rowRoot.setExpandRatio(row0, 10);
        rowRoot.setExpandRatio(buttonHLayout, 1);
        
        vmain.addComponents(rowRoot, row4, row5, row6, row1);
        vmain.setComponentAlignment(row4, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row5, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row6, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row1, Alignment.TOP_CENTER);
        
//        vmain.addComponents(rowRoot);
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	/**
	 * 数据存储
	 * 
	 * @return
	 */
	private VerticalLayout dataStorage() {
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		Label label = new Label("数据存储");
		label.addStyleName("setting-text");
 
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false);
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("150px");
        vmain.addStyleName("setting-layout-shadow");
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
        // 站点
        HorizontalLayout row2 = new HorizontalLayout();
        row2.setMargin(false);
        row2.setSpacing(false);
        row2.addStyleName("detail-hlayout");
        row2.setWidth("100%");
        row2.setHeight("48px");
        Label manageSites = new Label("管理站点");
        manageSites.addStyleName("detail-setting-text");
        Image rightArrow2 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row2.addComponents(manageSites, rightArrow2);
        row2.setComponentAlignment(manageSites, Alignment.MIDDLE_LEFT);
        row2.setComponentAlignment(rightArrow2, Alignment.MIDDLE_RIGHT);
        row2.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.K4)) {
        		showDetailPane(Commands.MANAGE_SITES);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        });
        
        // 服务器
        HorizontalLayout row3 = new HorizontalLayout();
        row3.setMargin(false);
        row3.setSpacing(false);
        row3.addStyleName("detail-hlayout");
        row3.setWidth("100%");
        row3.setHeight("48px");
        Label manageServers = new Label("管理服务器");
        manageServers.addStyleName("detail-setting-text");
        Image rightArrow3 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row3.addComponents(manageServers, rightArrow3);
        row3.setComponentAlignment(manageServers, Alignment.MIDDLE_LEFT);
        row3.setComponentAlignment(rightArrow3, Alignment.MIDDLE_RIGHT);
        row3.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.Q1)) {
        		showDetailPane(Commands.MANAGE_SERVERS);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        });
        
        vmain.addComponents(row2, row3);
        vmain.setComponentAlignment(row2, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row3, Alignment.TOP_CENTER);
        
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	/**
	 * 社区模块
	 * 
	 * @return
	 */
	private VerticalLayout community() {
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		Label label = new Label("社区");
		label.addStyleName("setting-text");
 
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false);
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("150px");
        vmain.addStyleName("setting-layout-shadow");
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
        
        // 管理社区
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setMargin(false);
        row1.setSpacing(false);
        row1.addStyleName("detail-hlayout");
        row1.setWidth("100%");
        row1.setHeight("48px");
        Label manageCommunities = new Label("管理社区");
        manageCommunities.addStyleName("detail-setting-text");
        Image rightArrow = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row1.addComponents(manageCommunities, rightArrow);
        row1.setComponentAlignment(manageCommunities, Alignment.MIDDLE_LEFT);
        row1.setComponentAlignment(rightArrow, Alignment.MIDDLE_RIGHT);
        row1.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.G5)) {
        		showDetailPane(Commands.MANAGE_COMMUNITIES);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
//        // 等待审批
//        HorizontalLayout row2 = new HorizontalLayout();
//        row2.setMargin(false);
//        row2.setSpacing(false);
//        row2.addStyleName("detail-hlayout");
//        row2.setWidth("100%");
//        row2.setHeight("48px");
//        Label manageApproval = new Label("等待审批");
//        manageApproval.addStyleName("detail-setting-text");
//        Image rightArrow2 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
//        row2.addComponents(manageApproval, rightArrow2);
//        row2.setComponentAlignment(manageApproval, Alignment.MIDDLE_LEFT);
//        row2.setComponentAlignment(rightArrow2, Alignment.MIDDLE_RIGHT);
//        row2.addLayoutClickListener(e -> {
//        	showDetailPane(Commands.PENDING_APPROVAL);
//        	hidePanes();
//        });
//        // 邀请用户
//        HorizontalLayout row3 = new HorizontalLayout();
//        row3.setMargin(false);
//        row3.setSpacing(false);
//        row3.addStyleName("detail-hlayout");
//        row3.setWidth("100%");
//        row3.setHeight("48px");
//        Label manageInvitation = new Label("邀请用户");
//        manageInvitation.addStyleName("detail-setting-text");
//        Image rightArrow3 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
//        row3.addComponents(manageInvitation, rightArrow3);
//        row3.setComponentAlignment(manageInvitation, Alignment.MIDDLE_LEFT);
//        row3.setComponentAlignment(rightArrow3, Alignment.MIDDLE_RIGHT);
//        row3.addLayoutClickListener(e -> {
//        	showDetailPane(Commands.INVITE_USRES);
//        	hidePanes();
//        });
//        // 社区邀请
//        HorizontalLayout row4 = new HorizontalLayout();
//        row4.setMargin(false);
//        row4.setSpacing(false);
//        row4.addStyleName("detail-hlayout");
//        row4.setWidth("100%");
//        row4.setHeight("48px");
//        Label communityInvitation = new Label("社区邀请");
//        communityInvitation.addStyleName("detail-setting-text");
//        Image rightArrow4 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
//        row4.addComponents(communityInvitation, rightArrow4);
//        row4.setComponentAlignment(communityInvitation, Alignment.MIDDLE_LEFT);
//        row4.setComponentAlignment(rightArrow4, Alignment.MIDDLE_RIGHT);
//        row4.addLayoutClickListener(e -> {
//        	showDetailPane(Commands.COMMUNITY_INVITATION);
//        	hidePanes();
//        });
        
        vmain.addComponents(row1);//, row2, row3, row4);
        vmain.setComponentAlignment(row1, Alignment.TOP_CENTER);
//        vmain.setComponentAlignment(row2, Alignment.TOP_CENTER);
//        vmain.setComponentAlignment(row3, Alignment.TOP_CENTER);
//        vmain.setComponentAlignment(row4, Alignment.TOP_CENTER);
        
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	/**
	 * 库房
	 * 
	 * @return
	 */
	private VerticalLayout storehouse() {
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		Label label = new Label("库房");
		label.addStyleName("setting-text");
 
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false);
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("150px");
        vmain.addStyleName("setting-layout-shadow");
        
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
        
        // 管理库房
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setMargin(false);
        row1.setSpacing(false);
        row1.addStyleName("detail-hlayout");
        row1.setWidth("100%");
        row1.setHeight("48px");
        Label manageCommunities = new Label("管理库房");
        manageCommunities.addStyleName("detail-setting-text");
        Image rightArrow = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row1.addComponents(manageCommunities, rightArrow);
        row1.setComponentAlignment(manageCommunities, Alignment.MIDDLE_LEFT);
        row1.setComponentAlignment(rightArrow, Alignment.MIDDLE_RIGHT);
        row1.addLayoutClickListener(e -> {
        	if (loginUser.isPermitted(PermissionCodes.F5)) {
        		showDetailPane(Commands.MANAGE_STOREHOUSES);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
        
        vmain.addComponents(row1);
        vmain.setComponentAlignment(row1, Alignment.TOP_CENTER);
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	
	/**
	 * 广播消息
	 * 
	 * @return
	 */
	private VerticalLayout broadcastMessage() {
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		Label label = new Label("消息");
		label.addStyleName("setting-text");
 
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false);
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("150px");
        vmain.addStyleName("setting-layout-shadow");
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setMargin(false);
        row1.setSpacing(false);
        row1.addStyleName("detail-hlayout");
        row1.setWidth("100%");
        row1.setHeight("48px");
        Label broadcastMessage = new Label("广播消息");
        broadcastMessage.addStyleName("detail-setting-text");
        Image rightArrow = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row1.addComponents(broadcastMessage, rightArrow);
        row1.setComponentAlignment(broadcastMessage, Alignment.MIDDLE_LEFT);
        row1.setComponentAlignment(rightArrow, Alignment.MIDDLE_RIGHT);
        row1.addLayoutClickListener(e -> {
        	
        	if (loginUser.isPermitted(PermissionCodes.L2)) {
        		showDetailPane(Commands.BROADCAST_MESSAGE);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
        
        vmain.addComponents(row1);
        vmain.setComponentAlignment(row1, Alignment.TOP_CENTER);
        
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	/**
	 * 系统设置
	 * 
	 * @return
	 */
	private VerticalLayout systemSettings() {
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		Label label = new Label("系统设置");
		label.addStyleName("setting-text");
 
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false);
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("150px");
        vmain.addStyleName("setting-layout-shadow");
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
        
        // 数据字典
        HorizontalLayout row0 = new HorizontalLayout();
        row0.setMargin(false);
        row0.setSpacing(false);
        row0.addStyleName("detail-hlayout");
        row0.setWidth("100%");
        row0.setHeight("48px");
        Label dataDictionary = new Label("数据字典");
        dataDictionary.addStyleName("detail-setting-text");
        Image rightArrow0 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row0.addComponents(dataDictionary, rightArrow0);
        row0.setComponentAlignment(dataDictionary, Alignment.MIDDLE_LEFT);
        row0.setComponentAlignment(rightArrow0, Alignment.MIDDLE_RIGHT);
        row0.addLayoutClickListener(e -> {
        	
        	if (loginUser.isPermitted(PermissionCodes.M4)) {
        		showDetailPane(Commands.EDIT_DATA_DICTIONARY);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
        
        // 业务类型
        HorizontalLayout row3 = new HorizontalLayout();
        row3.setMargin(false);
        row3.setSpacing(false);
        row3.addStyleName("detail-hlayout");
        row3.setWidth("100%");
        row3.setHeight("48px");
        Label businessType = new Label("业务类型");
        businessType.addStyleName("detail-setting-text");
        Image rightArrow3 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row3.addComponents(businessType, rightArrow3);
        row3.setComponentAlignment(businessType, Alignment.MIDDLE_LEFT);
        row3.setComponentAlignment(rightArrow3, Alignment.MIDDLE_RIGHT);
        row3.addLayoutClickListener(e -> {
        	
        	if (loginUser.isPermitted(PermissionCodes.N4)) {
        		showDetailPane(Commands.MANAGE_BUSINESS_TYPES);
            	hidePanes();
        	} else {
        		Notifications.warning("没有权限。");
        	}
        	
        });
        
        // 业务类型
        HorizontalLayout row4 = new HorizontalLayout();
        row4.setMargin(false);
        row4.setSpacing(false);
        row4.addStyleName("detail-hlayout");
        row4.setWidth("100%");
        row4.setHeight("48px");
        Label capture = new Label("高拍仪");
        capture.addStyleName("detail-setting-text");
//        Image rightArrow4 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        ComboBox<String> list = new ComboBox<String>();
        list.setEnabled(false);
        if(loginUser.isPermitted(PermissionCodes.H1)) {
        	list.setEnabled(true);
        }
        list.addStyleName("mycombobox");
        list.setEmptySelectionAllowed(false);
        list.setTextInputAllowed(false);
        list.setWidth("193px");
        list.setHeight("26px");
        list.setItems(new String[] {"无锡华通H6-1","维山VSA305FD","选择上传"});
        SystemSettings settings = ui.settingsService.findByKey("高拍仪");
        list.setSelectedItem(settings.getV());
        list.addValueChangeListener(e->{
        	settings.setV(e.getValue());
        	ui.settingsService.update(settings);
        });
        row4.addComponents(capture, list);
        row4.setComponentAlignment(capture, Alignment.MIDDLE_LEFT);
        row4.setComponentAlignment(list, Alignment.MIDDLE_RIGHT);
        vmain.addComponents(row0,row3,row4);
        vmain.setComponentAlignment(row0, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row3, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row4, Alignment.TOP_CENTER);
        
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout help() {
		VerticalLayout vlayoutWithTitle = new VerticalLayout(); 
		vlayoutWithTitle.setMargin(false);
		vlayoutWithTitle.setSpacing(false);
		vlayoutWithTitle.setWidth("678px");
		vlayoutWithTitle.setHeightUndefined();
		Label label = new Label("帮助");
		label.addStyleName("setting-text");
 
		// 白色方块面板
        VerticalLayout vmain = new VerticalLayout();
        vmain.setMargin(false); 
        vmain.setSpacing(false);
        vmain.setWidth("100%");
        vmain.setHeight("150px");
        vmain.addStyleName("setting-layout-shadow");
        vmain.setWidth("100%");
        vmain.setHeightUndefined();
        
        // 关于系统
        HorizontalLayout row0 = new HorizontalLayout();
        row0.setMargin(false);
        row0.setSpacing(false);
        row0.addStyleName("detail-hlayout");
        row0.setWidth("100%");
        row0.setHeight("48px");
        Label about = new Label("关于TB4系统");
        about.addStyleName("detail-setting-text");
        Image rightArrow0 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row0.addComponents(about, rightArrow0);
        row0.setComponentAlignment(about, Alignment.MIDDLE_LEFT);
        row0.setComponentAlignment(rightArrow0, Alignment.MIDDLE_RIGHT);
        row0.addLayoutClickListener(e -> {
        	showDetailPane(Commands.ABOUT_TB4);
        	hidePanes();
        });
        
        // 帮助中心
        HorizontalLayout row2 = new HorizontalLayout();
        row2.setMargin(false);
        row2.setSpacing(false);
        row2.addStyleName("detail-hlayout");
        row2.setWidth("100%");
        row2.setHeight("48px");
        Label helpCenter = new Label("帮助中心");
        helpCenter.addStyleName("detail-setting-text");
        Image rightArrow2 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row2.addComponents(helpCenter, rightArrow2);
        row2.setComponentAlignment(helpCenter, Alignment.MIDDLE_LEFT);
        row2.setComponentAlignment(rightArrow2, Alignment.MIDDLE_RIGHT);
        row2.addLayoutClickListener(e -> {
        	
        });
        
        // 报告问题
        HorizontalLayout row3 = new HorizontalLayout();
        row3.setMargin(false);
        row3.setSpacing(false);
        row3.addStyleName("detail-hlayout");
        row3.setWidth("100%");
        row3.setHeight("48px");
        Label reportIssues = new Label("开发");
        reportIssues.addStyleName("detail-setting-text");
        Image rightArrow3 = new Image(null, new ThemeResource("img/adminmenu/rightarrow.png"));
        row3.addComponents(reportIssues, rightArrow3);
        row3.setComponentAlignment(reportIssues, Alignment.MIDDLE_LEFT);
        row3.setComponentAlignment(rightArrow3, Alignment.MIDDLE_RIGHT);
        row3.addLayoutClickListener(e -> {
        	showDetailPane(Commands.DEV);
        	hidePanes();
        });
        
        vmain.addComponents(row0, row2, row3);
        vmain.setComponentAlignment(row0, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row2, Alignment.TOP_CENTER);
        vmain.setComponentAlignment(row3, Alignment.TOP_CENTER);
        
        vlayoutWithTitle.addComponents(label, vmain);
        vlayoutWithTitle.setComponentAlignment(label, Alignment.TOP_LEFT);
        vlayoutWithTitle.setComponentAlignment(vmain, Alignment.TOP_CENTER);
        vlayoutWithTitle.addStyleName("adminmainview-item");
        return vlayoutWithTitle;
	}
	
	/**
	 * 查询
	 * @return
	 */
	private HorizontalLayout createSearchPane() {
		Image image = new Image(null, new ThemeResource("img/adminmenu/menu_24px_1130584_easyicon.net.png"));
        image.addClickListener(e -> {
        	AdminMenuWindow.open();
        });
        image.addStyleName("menu-button");
        
        // Settings image
        Label labelSettings = new Label("设置");
        labelSettings.addStyleName("search-settings-label");
        
        // Search image
        Image searchImage = new Image();
        searchImage.setIcon(VaadinIcons.SEARCH);
        
        // Search Text field
        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("搜索设置");
        searchTextField.addStyleName("search-text-field");
        searchTextField.addStyleName("v-textfield:focus");
        searchTextField.addStyleName("v-textfield");
        // Handle changes in the value
        searchTextField.addValueChangeListener(e ->{
        	// Assuming that the value type is a String
            String value = e.getValue();
//            System.out.println(value);
        });
        // Search text area
        HorizontalLayout textFieldArea = new HorizontalLayout();
        textFieldArea.setMargin(false);
        textFieldArea.setSpacing(false);
        textFieldArea.addComponents(searchImage, searchTextField);
        textFieldArea.addStyleName("search-settings-field");
        
        HorizontalLayout fittingLayout = new HorizontalLayout();
        fittingLayout.setMargin(false);
        fittingLayout.setSpacing(false); // Compact layout
        fittingLayout.addComponents(image, labelSettings, textFieldArea);
        fittingLayout.setComponentAlignment(labelSettings, Alignment.MIDDLE_CENTER);
        fittingLayout.addStyleName("searchpane");
        
        return fittingLayout;
	}
	
	/**
	 * Hide pane
	 */
	public void hidePanes() {
		customerInformationLayout.setVisible(false);
		communityLayout.setVisible(false);
		storehouseLayout.setVisible(false);
		dataLayout.setVisible(false);
		messageLayout.setVisible(false);
		systemLayout.setVisible(false);
		helpLayout.setVisible(false);
	}
	
	/**
	 * Show pane
	 */
	public void showPanes() {
		customerInformationLayout.setVisible(true);
		communityLayout.setVisible(true);
		storehouseLayout.setVisible(true);
		dataLayout.setVisible(true);
		messageLayout.setVisible(true);
		systemLayout.setVisible(true);
		helpLayout.setVisible(true);
	}
	
	/**
	 * 
	 * @param command
	 */
	private void showDetailPane(int command) {
		if (command == Commands.EDIT_PROFILE) {
			manageAdmin = new ManageAdmin(this);	// 管理员
			main.addComponent(manageAdmin);
			main.setComponentAlignment(manageAdmin, Alignment.TOP_CENTER);
		} 
		else if (command == Commands.MANAGE_COMPANIES) {
			manageCompany = new ManageCompany(this);	// 管理机构
			main.addComponent(manageCompany);
			main.setComponentAlignment(manageCompany, Alignment.TOP_CENTER);
		} 
		else if (command == Commands.MANAGE_USERS) {
			manageOtherUsers = new ManageOtherUsers(this);// 管理用户
			main.addComponent(manageOtherUsers);
			main.setComponentAlignment(manageOtherUsers, Alignment.TOP_CENTER);
		}
		else if (command == Commands.MANAGE_ROLES) {
			manageRoles = new ManageRoles(this);	// 管理角色
			main.addComponent(manageRoles);
			main.setComponentAlignment(manageRoles, Alignment.TOP_CENTER);
		}
		else if (command == Commands.MANAGE_PERMISSIONS) {
			managePermissions = new ManagePermissions(this);// 管理权限
			main.addComponent(managePermissions);
			main.setComponentAlignment(managePermissions, Alignment.TOP_CENTER);
		} 
		else if (command == Commands.MANAGE_COMMUNITIES) {
			manageCommunity = new ManageCommunity(this);	// 管理社区
			main.addComponent(manageCommunity);
			main.setComponentAlignment(manageCommunity, Alignment.TOP_CENTER);
		}
		else if (command == Commands.MANAGE_STOREHOUSES) {
			manageStorehouse = new ManageStorehouse(this);	// 管理库房
			main.addComponent(manageStorehouse);
			main.setComponentAlignment(manageStorehouse, Alignment.TOP_CENTER);
		} 
		else if (command == Commands.PENDING_APPROVAL) {
			manageApprovals = new ManageApprovals(this);
			main.addComponent(manageApprovals);
			main.setComponentAlignment(manageApprovals, Alignment.TOP_CENTER);
		} 
		else if (command == Commands.INVITE_USRES) {
			manageUserInvitations = new ManageUserInvitations(this);
			main.addComponent(manageUserInvitations);
			main.setComponentAlignment(manageUserInvitations, Alignment.TOP_CENTER);
		}
		else if (command == Commands.COMMUNITY_INVITATION) {
			manageCommunityInvitations = new ManageCommunityInvitations(this);
			main.addComponent(manageCommunityInvitations);
			main.setComponentAlignment(manageCommunityInvitations, Alignment.TOP_CENTER);
		}
		else if (command == Commands.MANAGE_SITES) {
			manageSites = new ManageSites(this);	// 管理站点
			main.addComponent(manageSites);
			main.setComponentAlignment(manageSites, Alignment.TOP_CENTER);
		}
		else if (command == Commands.MANAGE_SERVERS) {
			manageServers = new ManageServers(this);	// 管理服务器
			main.addComponent(manageServers);
			main.setComponentAlignment(manageServers, Alignment.TOP_CENTER);
		}
		else if (command == Commands.BROADCAST_MESSAGE) {
			manageBroadCast = new ManageBroadCast(this);
			main.addComponent(manageBroadCast);
			main.setComponentAlignment(manageBroadCast, Alignment.TOP_CENTER);
		}
		else if (command == Commands.EDIT_DATA_DICTIONARY) {
			manageDataDictionary = new ManageDataDictionary(this);//业务类型管理
			main.addComponent(manageDataDictionary);
			main.setComponentAlignment(manageDataDictionary, Alignment.TOP_CENTER);
		}
		else if (command == Commands.MANAGE_BUSINESS_TYPES) {
			manageBusinessType = new ManageBusinessTypes(this);//业务类型管理
			main.addComponent(manageBusinessType);
			main.setComponentAlignment(manageBusinessType, Alignment.TOP_CENTER);
		}
		else if (command == Commands.ABOUT_TB4) { //关于TB4
			aboutTB4 = new AboutTB4(this);
			main.addComponent(aboutTB4);
			main.setComponentAlignment(aboutTB4, Alignment.TOP_CENTER);
		}
		else if (command == Commands.DEV) { //开发
			dev = new DEV(this);
			main.addComponent(dev);
			main.setComponentAlignment(dev, Alignment.TOP_CENTER);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Component buildExitButton() {
        Button result = new Button("");
        result.setIcon(VaadinIcons.EXIT);
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("退出系统");
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	VaadinSession.getCurrent().close();
        		Page.getCurrent().reload();
            	System.out.println("logged out.");
            }
        });
        return result;
    }
	
	/**
	 * 
	 * @param photo
	 */
	public void reloadAdminPhoto(Image photo) {
		subRow.removeComponent(userAvatar);
		userAvatar = photo;
		userAvatar.setWidth("40px");
        userAvatar.setHeight("40px");
		subRow.addComponent(userAvatar, 0);
		subRow.setComponentAlignment(userAvatar, Alignment.MIDDLE_LEFT);
	}
	
	/**
	 * 
	 * @return
	 */
	public VerticalLayout getContent() {
		return main;
	}
	
	private ManageAdmin manageAdmin;// 管理管理员
	private ManageCompany manageCompany;// 管理机构
	private ManageOtherUsers manageOtherUsers;// 管理其他用户
 	private ManageBusinessTypes manageBusinessType;//业务类型管理
	private ManageSites manageSites;// 管理站点
	private ManageServers manageServers;//管理服务器
	private ManageRoles manageRoles;// 管理角色
	private ManagePermissions managePermissions;// 管理权限
	private ManageDataDictionary manageDataDictionary;
	private ManageCommunity manageCommunity; // 社区
	private ManageStorehouse manageStorehouse; // 库房
	private ManageApprovals manageApprovals;// 等待审批
	private ManageUserInvitations manageUserInvitations;// 邀请用户
	private ManageCommunityInvitations manageCommunityInvitations;//社区邀请
	private ManageBroadCast manageBroadCast;
	private AboutTB4 aboutTB4;
	private DEV dev;
	
	// 搜索
//	private HorizontalLayout searchPane;
	private SearchToolBar toolbar = new SearchToolBar(this);
	public NavigationBar navigationBar = new NavigationBar();
	private Screen screen = new Screen();
	public FlexTable table = new FlexTable();
	
	
	
	// 用户
	private VerticalLayout customerInformationLayout;
	// 社区
	private VerticalLayout communityLayout;
	// 库房
	private VerticalLayout storehouseLayout;
	// 数据
	private VerticalLayout dataLayout;
	// 消息
	private VerticalLayout messageLayout;
	// 系统
	private VerticalLayout systemLayout;
	// 帮助
	private VerticalLayout helpLayout;
    private Image userAvatar = null;
    private HorizontalLayout subRow = null;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
