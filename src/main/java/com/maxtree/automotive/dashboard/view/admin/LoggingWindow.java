package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * 
 * @author chens
 *
 */
public class LoggingWindow extends Window {
	
	public LoggingWindow() {
		this.setCaption("日志");
		this.setClosable(true);
		this.setResizable(true);
		this.setWidth("1024px");
		this.setHeight("768px");
	}
	

	public static void open() {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        LoggingWindow w = new LoggingWindow();
        UI.getCurrent().addWindow(w);
        w.center();
    }
}
