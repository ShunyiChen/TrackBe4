package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class BusinessTypePane extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessTypePane() {
	
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("业务类型");
		this.addStyleName("picture-pane");
		this.setWidth("100%");
		this.setHeightUndefined();
		
		
		// 业务类型选择
		BusinessTypeSelector selector = new BusinessTypeSelector();
		
		VerticalLayout main = new VerticalLayout();
		main.setSpacing(false);
		main.setMargin(new MarginInfo(false, false, true, true));
		main.setWidth("100%");
		main.setHeightUndefined();
		main.addComponent(selector);
		this.setContent(main);
	}
}
