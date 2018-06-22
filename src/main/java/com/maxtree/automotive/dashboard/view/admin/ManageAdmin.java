package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.ChangePasswordWindow;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageAdmin extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageAdmin(AdminMainView mainView) {
		setMargin(false);
	    setSpacing(false);
	    setWidth("678px");
	    setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
	 	addStyleName("detail-pane-with-padding20");
	    // 必须有一个VerticalLayout，否则上下各50%显示
 	 	VerticalLayout content = new VerticalLayout();
 	 	content.setSpacing(false);
 	 	content.setMargin(false);
 	 	content.setWidth("100%");
 	 	content.setHeightUndefined();
 	 	// 标题布局
		HorizontalLayout searchHLayout = new HorizontalLayout();
		searchHLayout.setMargin(false);
		searchHLayout.setSpacing(false);
		searchHLayout.setWidthUndefined();
		searchHLayout.setHeight("88px");
		Image leftarrowImg = new Image(null, new ThemeResource("img/adminmenu/leftarrow.png"));
		leftarrowImg.addStyleName("left-arrow-image");
		leftarrowImg.addClickListener(e -> {
	 		mainView.getContent().removeComponent(this);
	 		mainView.showPanes();
	 		mainView.reloadAdminPhoto(photo);
        });
		Label parentTitle = new Label("编辑用户");
		parentTitle.setWidth("420px");
		parentTitle.addStyleName("parent-title");
		Image magnifyingglass = new Image(null, new ThemeResource("img/adminmenu/magnifyingglass.png"));
		magnifyingglass.addStyleName("left-arrow-image");
		TextField searchField = new TextField();
		searchField.setPlaceholder("搜索");
		searchField.setWidth("162px");
		searchField.setHeight("23px");
		searchHLayout.addComponents(leftarrowImg, Box.createHorizontalBox(20), parentTitle, magnifyingglass, Box.createHorizontalBox(7), searchField);
		searchHLayout.setComponentAlignment(leftarrowImg, Alignment.TOP_LEFT);
		searchHLayout.setComponentAlignment(parentTitle, Alignment.TOP_LEFT);
		searchHLayout.setComponentAlignment(magnifyingglass, Alignment.TOP_RIGHT);
		searchHLayout.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
		
		// 编辑用户名
		TextField editUserNameField = new TextField();
		editUserNameField.setReadOnly(true);
		editUserNameField.setWidth("140px");
		editUserNameField.setHeight("23px");
		User usr = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		editUserNameField.setValue(usr.getUserName());
		editUserNameField.focus();
		
		photo = new Image(null, new ThemeResource(usr.getProfile().getPicture()));
		HorizontalLayout row0 = new HorizontalLayout();
		row0.setMargin(false);
		row0.setSpacing(false);
		row0.addComponents(photo, Box.createHorizontalBox(5), editUserNameField);
		row0.setComponentAlignment(photo, Alignment.TOP_CENTER);
		row0.setComponentAlignment(editUserNameField, Alignment.BOTTOM_CENTER);
		
		content.addComponents(searchHLayout, row0, Box.createVerticalBox(25));
		
		HorizontalLayout row1 = null;
		for (int i = 0; i < 28; i++) {
			if (i % 9 == 0) {
				row1 = new HorizontalLayout();
				row1.setMargin(false);
				row1.setSpacing(false);
				row1.setWidthUndefined();
				row1.setHeight("50px");
				content.addComponents(row1, Box.createVerticalBox(23));
			}
			String src = "img/adminmenu/users/user"+(i+1)+".png";
			Image photo1 = new Image(null, new ThemeResource(src));
			photo1.addStyleName("image-cursor");
			photo1.addClickListener(e -> {
				row0.removeComponent(photo);
				photo = new Image(null, new ThemeResource(src));
				row0.addComponent(photo, 0);
				
				usr.getProfile().setPicture(src);
				ui.userService.updateProfile(usr.getProfile());
				
			});
			row1.addComponents(photo1, Box.createHorizontalBox(23));
		}
		
		Button changePWD = new Button("修改密码");
		changePWD.setIcon(VaadinIcons.KEY);
		changePWD.addClickListener(e -> {
			ChangePasswordWindow.open(usr);
		});
		content.addComponents(changePWD);

		addComponents(content);
		setComponentAlignment(content, Alignment.TOP_CENTER);
	}
	
	private Image photo = null;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
