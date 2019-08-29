package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithIcon extends FlexTableRowItem {

	// define the logger
	private static final Logger LOGGER = LoggerFactory.getLogger(RowItemWithIcon.class);

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public RowItemWithIcon() {
		super("");
		initComponents();
	}
	
	private void initComponents() {
		DashboardUI ui = (DashboardUI) UI.getCurrent();
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		User user = ui.userService.getUserByUserName(username);
		Label separater = new Label();
		separater.addStyleName("RowItemWithIcon_separater");
		imageIcon = new Image(null, new ThemeResource(user.getProfile().getPicture()));
		this.addComponents(imageIcon,name,arrowIcon,separater,btnExit);
		this.setComponentAlignment(imageIcon, Alignment.TOP_LEFT);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
		this.setComponentAlignment(btnExit, Alignment.MIDDLE_RIGHT);
		
		this.setExpandRatio(imageIcon, 0);
		this.setExpandRatio(name, 1);
		name.setValue(username);
		
		btnExit.setIcon(VaadinIcons.EXIT);
		btnExit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		btnExit.setDescription("退出系统");
		btnExit.addClickListener(e->{
			LOGGER.info(username, "You have logged out successfully..");
			ui.authService.logOut();
		});
	}

	/**
	 * 更新头像
	 * 
	 * @param photoPath
	 */
	public void updateImageIcon(String photoPath) {
		Image newImg = new Image(null, new ThemeResource(photoPath));
		this.removeComponent(imageIcon);
		this.addComponent(newImg, 0);
		this.setComponentAlignment(newImg, Alignment.MIDDLE_LEFT);
		this.imageIcon = newImg;
	}
	
	private Image imageIcon;
	private Button btnExit = new Button();
}
