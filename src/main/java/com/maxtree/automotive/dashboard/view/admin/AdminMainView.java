package com.maxtree.automotive.dashboard.view.admin;

import java.util.Date;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
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
        });
	 	this.addComponent(toolbar, "left: 0px; top: 0px;");
	 	this.addComponent(table, "left: 0px; top: 56px;");
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
	}
	
	/**
	 * 
	 */
	public void hideNavigationBar() {
		navigationBar.removeStyleName("NavigationBar_moveIn");
		navigationBar.addStyleName("NavigationBar_moveOut");
		addComponent(navigationBar, "left: -255px; top: 0px;z-index:999;");
		removeComponent(screen);
	}
	
	/**
	 * 
	 */
	public void back() {
		this.removeComponent(viewportView);
		this.addComponent(table, "left: 0px; top: 56px;");
	}

	/**
	 * 
	 * @param customeView
	 */
	public void forward(ContentView customeView) {
		this.removeComponent(table);
		viewportView.setCustomeView(customeView);
		this.addComponent(viewportView, "left: 0px; top: 56px;");
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
	
	// 搜索
	public FlexTable table = new FlexTable(this);
	private SearchToolBar toolbar = new SearchToolBar(this);
	public NavigationBar navigationBar = new NavigationBar(this);
	private Screen screen = new Screen();
	private ViewportView viewportView = new ViewportView();
	
	
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
}
