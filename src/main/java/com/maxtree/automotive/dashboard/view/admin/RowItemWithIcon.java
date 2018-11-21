package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.component.LoggingWrapper;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithIcon extends FlexTableRowItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public RowItemWithIcon() {
		super("");
		initComponents();
	}
	
	private void initComponents() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		Label separater = new Label();
		separater.addStyleName("RowItemWithIcon_separater");
		imageIcon = new Image(null, new ThemeResource(loggedInUser.getProfile().getPicture()));
		this.addComponents(imageIcon,name,arrowIcon,separater,btnExit);
		this.setComponentAlignment(imageIcon, Alignment.TOP_LEFT);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
		this.setComponentAlignment(btnExit, Alignment.MIDDLE_RIGHT);
		
		this.setExpandRatio(imageIcon, 0);
		this.setExpandRatio(name, 1);
		name.setValue(loggedInUser.getUserName());
		
		btnExit.setIcon(VaadinIcons.EXIT);
		btnExit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		btnExit.setDescription("退出系统");
		btnExit.addClickListener(e->{
			loggingWrapper.info(loggedInUser.getUserName(), LoggingWrapper.LOGIN, "logout.");
			VaadinSession.getCurrent().close();
    		Page.getCurrent().reload();
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
	
	private User loggedInUser;
	private Image imageIcon;
	private Button btnExit = new Button();
	private LoggingWrapper loggingWrapper = new LoggingWrapper(RowItemWithIcon.class);
}
