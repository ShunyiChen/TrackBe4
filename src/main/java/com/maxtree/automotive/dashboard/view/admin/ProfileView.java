package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.ChangePasswordWindow;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class ProfileView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public ProfileView(String parentTitle, AdminMainView rootView, Callback2 callback) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		main.addStyleName("ProfileView_main");
		
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		User loggedinUser = ui.userService.getUserByUserName(username);
//		personalPhoto = new Image(null, new ThemeResource(loggedinUser.getProfile().getPicture()));
		Label userNameLabel = new Label();
		userNameLabel.setWidth("140px");
		userNameLabel.setHeight("23px");
//		userNameLabel.setValue(username);
		HorizontalLayout userRow = new HorizontalLayout();
		userRow.addComponents(personalPhoto, Box.createHorizontalBox(5), userNameLabel);
		userRow.setComponentAlignment(personalPhoto, Alignment.MIDDLE_LEFT);
		userRow.setComponentAlignment(userNameLabel, Alignment.MIDDLE_LEFT);
		
		main.addComponents(userRow,Box.createVerticalBox(20));
		main.setComponentAlignment(userRow, Alignment.TOP_LEFT);
		
		HorizontalLayout row1 = null;
		for (int i = 0; i < 27; i++) {
			if (i % 9 == 0) {
				row1 = new HorizontalLayout();
				row1.setMargin(false);
				row1.setSpacing(false);
				row1.setWidthUndefined();
				row1.setHeight("50px");
				main.addComponents(row1, Box.createVerticalBox(23));
				main.setComponentAlignment(row1, Alignment.TOP_CENTER);
				
			}
			String src = "img/adminmenu/users/user"+(i+1)+".png";
			Image photo1 = new Image(null, new ThemeResource(src));
			photo1.addStyleName("image-cursor");
			photo1.addClickListener(e -> {
				userRow.removeComponent(personalPhoto);
				personalPhoto = new Image(null, new ThemeResource(src));
				userRow.addComponent(personalPhoto, 0);

//				loggedinUser.getProfile().setPicture(src);
//				ui.userService.updateProfile(loggedinUser.getProfile());
				
				callback.onSuccessful(src);
			});
			row1.addComponents(photo1, Box.createHorizontalBox(23));
		}
		
		Button btnPass = new Button();
		btnPass.setIcon(VaadinIcons.KEY);
		btnPass.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btnPass.addStyleName("ProfileView_btnPass");
		btnPass.setCaption("更改密码");
		btnPass.addClickListener(e -> {
//			ChangePasswordWindow.open(loggedinUser);
		});
		main.addComponents(Box.createVerticalBox(23),btnPass);
		main.setComponentAlignment(btnPass, Alignment.TOP_LEFT);
		
		this.addComponent(main);
		this.setComponentAlignment(main, Alignment.TOP_CENTER);
		this.setExpandRatio(main, 1);
	}
	
	private Image personalPhoto = null;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
}
