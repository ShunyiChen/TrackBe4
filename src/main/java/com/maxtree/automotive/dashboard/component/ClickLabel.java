package com.maxtree.automotive.dashboard.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ClickLabel extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClickLabel(String value) {
		this.setSpacing(false);
		this.setMargin(false);
		
		Label label = new Label(value, ContentMode.HTML);
		addComponent(label);
	}
}
