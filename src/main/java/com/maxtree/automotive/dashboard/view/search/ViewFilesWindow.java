package com.maxtree.automotive.dashboard.view.search;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(true);
		this.setCaption("查看原文");
		this.setWidth("1200px");
		this.setHeight("660px");
		
		VerticalLayout west = new VerticalLayout();
		west.setSizeFull();
		west.setSpacing(false);
		west.setMargin(false);
		
		HorizontalLayout center = new HorizontalLayout();
		center.setSizeFull();
		center.setSpacing(false);
		center.setMargin(false);
		center.addComponents(west, imgStage);
		center.setExpandRatio(west, 0.2f);
		center.setExpandRatio(imgStage, 0.8f);
		
		Panel south = new Panel();
		south.setSizeFull();
		
		VerticalLayout main = new VerticalLayout();
		main.setSizeFull();
		main.setSpacing(false);
		main.setMargin(false);
		main.addComponents(center, south);
		main.setExpandRatio(center, 0.8f);
		main.setExpandRatio(south, 0.2f);
		
		this.setContent(main);
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
