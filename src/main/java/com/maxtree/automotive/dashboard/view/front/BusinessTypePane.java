package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class BusinessTypePane extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public BusinessTypePane(FrontView view) {
		this.view = view;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("业务类型");
		this.addStyleName("picture-pane");
		this.setWidth("100%");
		this.setHeightUndefined();
		// 业务类型选择
		BusinessTypeSelector selector = new BusinessTypeSelector(view);
		HorizontalLayout main = new HorizontalLayout();
		main.setSpacing(false);
		main.setMargin(new MarginInfo(false, false, true, true));
		main.setWidth("100%");
		main.setHeightUndefined();
		main.addComponent(selector);
		
		this.setContent(main);
	}
	
	private FrontView view = null;
}
