package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithOptions extends FlexTableRowItem implements LayoutClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RowItemWithOptions(String title, String[] options) {
		super(title);
		this.options = options;
		initComponents();
	}
	
	private void initComponents() {
		selector.setWidth("200px");
		selector.setHeight("32px");
		selector.setEmptySelectionAllowed(false);
		selector.setTextInputAllowed(false);
		selector.setItems(options);
		selector.setSelectedItem(options[0]);
		selector.addStyleName("RowItemWithOptions_selector");
		this.removeAllComponents();
		this.addComponents(name, selector);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(selector, Alignment.MIDDLE_RIGHT);
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		
	}

	private ComboBox<String> selector = new ComboBox<>();
	private String[] options;
}
