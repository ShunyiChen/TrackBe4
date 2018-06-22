package com.maxtree.automotive.dashboard.component;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class Notifications {

	public static void warning(String msg) {
		Notification notification = new Notification("提示：", msg, Type.WARNING_MESSAGE);
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
	}
}
