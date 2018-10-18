package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Label;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithTitle extends FlexTableRowItem implements LayoutClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RowItemWithTitle(String title) {
		super(title);
		initComponents();
	}

	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		this.addLayoutClickListener(this);
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
	}
	
}
