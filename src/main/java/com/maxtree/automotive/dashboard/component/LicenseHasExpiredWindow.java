package com.maxtree.automotive.dashboard.component;


import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LicenseHasExpiredWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public LicenseHasExpiredWindow(String message) {
		this.message = message;
		this.setResizable(false);
		this.setCaption("购买许可");
		this.setModal(true);
		this.setWidthUndefined();
		this.setHeightUndefined();
		initComponents();
	}
	
	private void initComponents() {
		VerticalLayout vlayout = new VerticalLayout();
        vlayout.setWidthUndefined();
        vlayout.setHeightUndefined();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        Label msg = new Label(message);
        vlayout.addComponents(msg, Box.createVerticalBox(15), btnOk);
        btnOk.focus();
        vlayout.setComponentAlignment(msg, Alignment.MIDDLE_LEFT);
        vlayout.setComponentAlignment(btnOk, Alignment.MIDDLE_CENTER);
        
        this.setContent(vlayout);
    }
	
	/**
	 * 
	 * @param user
	 * @param licenseEndDate
	 */
	public static void open(String message, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        LicenseHasExpiredWindow w = new LicenseHasExpiredWindow(message);
        w.btnOk.addClickListener(e-> {
        	w.close();
        	if (callback != null) 
        		callback.onSuccessful();
        });
        w.addCloseListener(e -> {
        	if (callback != null) 
        		callback.onSuccessful();
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnOk = new Button("确定");
	private String message;
}
