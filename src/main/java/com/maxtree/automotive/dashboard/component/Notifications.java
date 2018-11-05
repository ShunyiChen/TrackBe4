package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.Callback;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class Notifications {

	/**
	 * 
	 * @param msg
	 */
	public static void warning(String msg) {
//		Notification notification = new Notification("提示：", msg, Type.WARNING_MESSAGE);
//		notification.setDelayMsec(2000);
//		notification.show(Page.getCurrent());
		warning(msg, Type.WARNING_MESSAGE);
	}
	
	
	public static void warning(String msg, Type type) {
		Notification notification = new Notification("提示：", msg, type);
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
	}
	
	/**
	 * 
	 * @param msg
	 * @param callback
	 */
	public static void warning(String msg, Callback callback) {
		Notification notification = new Notification("提示：", msg, Type.WARNING_MESSAGE);
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
		notification.addCloseListener(e->{
			callback.onSuccessful();
		});
	}
	
	/**
	 * 
	 * @param msg
	 */
	public static void bottomWarning(String msg) {
		Notification success = new Notification(msg);
		success.setDelayMsec(3000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
	}
}
