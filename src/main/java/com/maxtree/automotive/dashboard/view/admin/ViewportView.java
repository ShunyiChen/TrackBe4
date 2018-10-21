package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class ViewportView extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ViewportView() {
		setSizeFull();
		main.setMargin(false);
		main.setSpacing(false);
		main.setWidth("100%");
		main.setHeightUndefined();
		main.addStyleName("ViewportView_main");
		setContent(main);
		addStyleName("ViewportView");
	}
	
	/**
	 * 
	 * @param customeView
	 */
	public void setCustomeView(ContentView customeView) {
		main.removeAllComponents();
		main.addComponents(customeView);
		main.setComponentAlignment(customeView, Alignment.TOP_CENTER);
	}

	private VerticalLayout main = new VerticalLayout();
}
