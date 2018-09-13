package com.maxtree.automotive.dashboard;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressBar;

public class WaitingPopupView implements PopupView.Content {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PopupView popupView = new PopupView(this);
	// Add a normal progress bar
	private ProgressBar progressBar = new ProgressBar();
	
	public void show() {
		popupView.setWidth("200px");
		popupView.setHeight("50px");
		popupView.setPopupVisible(true);
	}
	
	@Override
	public String getMinimizedValueAsHTML() {
		// TODO Auto-generated method stub
		return "11";
	}

	@Override
	public Component getPopupComponent() {
		progressBar.setValue(0.5f);
		
		// TODO Auto-generated method stub
		HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        layout.addComponent(progressBar);
        
		return layout;
	}
}
