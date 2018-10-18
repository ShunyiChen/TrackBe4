package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

public class TitleRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param title
	 */
	public TitleRow(String title) {
		this.title = title;
		initComponents();
	}
	
	private void initComponents() {
		this.removeStyleName("FlexTableRow");
		
		Label name = new Label();
		name.setValue(title);
		name.addStyleName("TitleRow_name");
		removeAllComponents();
		this.addComponent(name);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.addStyleName("TitleRow");
	}

	protected String getTitle() {
		return title;
	}
	
	private String title;
}
