package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

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
		Label separater = new Label();
		separater.addStyleName("RowItemWithIcon_separater");
		this.addComponents(imageIcon,name,arrowIcon,separater,btnExit);
		this.setComponentAlignment(imageIcon, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
		this.setComponentAlignment(btnExit, Alignment.MIDDLE_RIGHT);
		
		this.setExpandRatio(imageIcon, 0);
		this.setExpandRatio(name, 1);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		name.setValue(loggedInUser.getUserName());
		
		btnExit.setIcon(VaadinIcons.EXIT);
		btnExit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		btnExit.setDescription("退出系统");
		btnExit.addClickListener(e->{
			VaadinSession.getCurrent().close();
    		Page.getCurrent().reload();
		});
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
	private Button btnExit = new Button();
	private Image imageIcon = new Image(null, new ThemeResource("img/adminmenu/users/user14.png"));
	
}
