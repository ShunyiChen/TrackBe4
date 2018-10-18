package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.VerticalLayout;

/**
 * 父类
 * 
 * @author Chen
 *
 */
public class FlexTableRow extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FlexTableRow() {
		initComponents();
	}
	
	private void initComponents() {
		setWidth("680px");
		setHeightUndefined();
		this.setSpacing(false);
		this.setMargin(true);
		this.addStyleName("FlexTableRow");
	}
	
	
	protected String getTitle() {
		return "Unknown";
	}
	
	protected String getSearchTags() {
		return "Unknown";
	}
	
	protected int getOrderID() {
		return 0;
	}
}
