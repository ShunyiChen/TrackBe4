package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.SystemSettings;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

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
		selector.addValueChangeListener(e->{
			SystemSettings device = new SystemSettings();
			device.setItemName("高拍仪");
			device.setItemSettings(e.getValue());
			ui.settingsService.update(device);
		});
		SystemSettings settings = ui.settingsService.findByName("高拍仪");
		selector.setSelectedItem(settings.getItemSettings());
		
		this.removeAllComponents();
		this.addComponents(name, selector);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(selector, Alignment.MIDDLE_RIGHT);
	}

	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private ComboBox<String> selector = new ComboBox<>();
	private String[] options;
}
