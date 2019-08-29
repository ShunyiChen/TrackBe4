package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

/**
 * 
 * @author Chen
 *
 */
public class RowItemWithOptions extends FlexTableRowItem {

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
		selector.addSelectionListener(e -> {
			System.out.println(e.getSelectedItem().get()+"================");
		});
		this.removeAllComponents();
		this.addComponents(name, selector, arrowIcon);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(selector, Alignment.MIDDLE_RIGHT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
		this.setExpandRatio(name, 1);
		this.setExpandRatio(selector, 0);
		this.setExpandRatio(arrowIcon, 0);
	}

	private ComboBox<String> selector = new ComboBox<>();
	private String[] options;
}
