package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithIcon extends FlexTableRowItem implements LayoutClickListener {

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
		this.addComponents(imageIcon, name, arrowIcon);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
		this.setExpandRatio(imageIcon, 0);
		this.setExpandRatio(name, 1);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		name.setValue(loggedInUser.getUserName());
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		
	}

	/**
	 * 
	 * @param imageIcon
	 */
	public void setImageIcon(Image imageIcon) {
		this.imageIcon = imageIcon;
	}
	
	private User loggedInUser;
	private Image imageIcon = new Image(null, new ThemeResource("img/adminmenu/users/user14.png"));
	
}
