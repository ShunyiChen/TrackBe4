package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.jpa.entity.Camera;
import com.vaadin.ui.*;

import java.util.List;

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

	public RowItemWithOptions(String title, List<Camera> options) {
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
		for(Camera c : options) {
			if(c.getEnable() != null && c.getEnable()) {
				selector.setSelectedItem(c);
			}
		}
		selector.addStyleName("RowItemWithOptions_selector");
		selector.addSelectionListener(e -> {
			Iterable<Camera> iter = TB4Application.getInstance().cameraRepository.findAll();
			iter.forEach(c -> {
				if(c.getId() == e.getValue().getId()) {
					c.setEnable(true);
				}
				else {
					c.setEnable(false);
				}
				TB4Application.getInstance().cameraRepository.save(c);
			});
		});
		selector.addFocusListener(e->{});
		this.removeAllComponents();
		this.addComponents(name, selector, arrowIcon);
		this.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(selector, Alignment.MIDDLE_RIGHT);
		this.setComponentAlignment(arrowIcon, Alignment.MIDDLE_RIGHT);
		this.setExpandRatio(name, 1);
		this.setExpandRatio(selector, 0);
		this.setExpandRatio(arrowIcon, 0);
	}

	private ComboBox<Camera> selector = new ComboBox<>();
	private List<Camera> options;
}
