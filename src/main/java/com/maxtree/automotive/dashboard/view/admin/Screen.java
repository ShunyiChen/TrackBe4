package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.VerticalLayout;

/**
 * 屏障
 * 
 * @author Chen
 *
 */
public class Screen extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Screen() {
		initComponents();
	}
	
	private void initComponents() {
		this.setSizeFull();
		this.addStyleName("Screen");
	}
}
