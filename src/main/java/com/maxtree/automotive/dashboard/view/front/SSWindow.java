package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;

//public class SSWindow extends Window {
//
//	public SSWindow() {
//		// A resource reference to some object
//		Resource res = new ThemeResource("img/reindeer.svg");
//
//		// Display the object
//		Embedded object = new Embedded("My SVG", res);
//		object.setMimeType("image/svg+xml"); // Unnecessary
//		 .addComponent(object);
//	}
//}

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Tenant;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

public class SSWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public SSWindow() {
		this.setWidthUndefined();
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		BrowserFrame browser = new BrowserFrame("Browser", new ThemeResource("html/Sample_CamOCX_HTML_Device_IE.html"));
			browser.setWidth("1500px");
			browser.setHeight("1000px");
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("128px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		
		mainLayout.addComponents(browser, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(tenant);
	}
	
	public static void open() {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        SSWindow w = new SSWindow();
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(Tenant tenant, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        SSWindow w = new SSWindow();
        
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Binder<Tenant> binder = new Binder<>();
	private Tenant tenant = new Tenant();
	private String[] items = {"号牌种类", "业务材料"};
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
