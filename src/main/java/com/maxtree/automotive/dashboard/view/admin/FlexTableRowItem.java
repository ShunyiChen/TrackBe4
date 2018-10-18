package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 父类
 * 
 * @author Chen
 *
 */
public class FlexTableRowItem extends HorizontalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param data1
	 */
	public FlexTableRowItem(String title) {
		this.title = title;
		initComponents();
	}
	
	private void initComponents() {
		this.setWidth("680px");
		this.setHeight("49px");
		this.addStyleName("FlexTableRowItem");
		name.setValue(title);
		name.addStyleName("FlexTableRowItem_name");
		this.addComponents(name, arrowIcon);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
	}
	
	private String title = "Unknown";
	protected Image arrowIcon = new Image(null, new ThemeResource("img/adminmenu/caret-right.png"));
	protected Label name = new Label();
	
}
