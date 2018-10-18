package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithSwitchButton extends FlexTableRowItem implements LayoutClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RowItemWithSwitchButton(String title) {
		super(title);
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
	}

}
