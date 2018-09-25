package com.maxtree.automotive.dashboard.view.search;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ViewFilesWindow extends Window implements ImageViewIF{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewFilesWindow() {
		initComponents();
	}
	
	private void initComponents() {
//		this.setSizeFull();
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("查看原文");
		this.setWidth("600px");
		this.setHeight("450px");
		this.setContent(imgStage);
	}

	@Override
	public void previous() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ViewFilesWindow w = new ViewFilesWindow();
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private ImageStage imgStage = new ImageStage(this, true);
}
