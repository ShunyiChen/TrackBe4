package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.security.PasswordSecurity;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

public class ChangePasswordWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ChangePasswordWindow(User user) {
		this.user = user;
		this.setResizable(false);
		this.setCaption("修改密码");
		this.setModal(true);
		this.setWidth("370px");
		this.setHeightUndefined();
		this.addStyleName("edit-window");
		initComponents();
	}

	private void initComponents() {
		VerticalLayout vlayout = new VerticalLayout();
        vlayout.setWidth("100%");
        vlayout.setHeightUndefined();
        vlayout.setMargin(false);
        vlayout.setSpacing(false);
		
		FormLayout mainLayout = new FormLayout();
		mainLayout.setWidth("100%");
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
		passwordField1 = new PasswordField("原始密码:");
		passwordField1.setIcon(VaadinIcons.PASSWORD);
		passwordField1.focus();
		passwordField1.setWidth("200px");
		passwordField1.setHeight("25px");
		passwordField2 = new PasswordField("新密码:");
		passwordField2.setIcon(VaadinIcons.PASSWORD);
		passwordField2.setWidth("200px");
		passwordField2.setHeight("25px");
		passwordField3 = new PasswordField("重复新密码:");
		passwordField3.setIcon(VaadinIcons.PASSWORD);
		passwordField3.setWidth("200px");
		passwordField3.setHeight("25px");
		
		HorizontalLayout bottomPane = new HorizontalLayout();
		bottomPane.setSpacing(false);
		bottomPane.setMargin(false);
		bottomPane.setWidth("100%");
		bottomPane.setHeightUndefined();
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setWidth("130px");
		buttonPane.setHeight("30px");
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		Button btnSave = new Button("确定");
		btnCancel.addStyleName("grid-button-without-border");
		btnSave.addStyleName("grid-button-without-border");
		
		buttonPane.addComponents(btnCancel, btnSave);
		buttonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);
		buttonPane.setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
		
		mainLayout.addComponents(passwordField1, passwordField2, passwordField3);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		
		btnSave.addClickListener(e -> {
			if(validate()) {
				// Update the password
				ui.userService.updatePassword(user.getUserUniqueId(), passwordField2.getValue());
				close();
			}
		});
		
		bottomPane.addComponent(buttonPane);
		bottomPane.setComponentAlignment(buttonPane, Alignment.MIDDLE_RIGHT);
		
		vlayout.addComponents(mainLayout, Box.createVerticalBox(5), bottomPane);
		
		this.setContent(vlayout);
	}
	
	private boolean validate() {
		if (PasswordSecurity.check(passwordField1.getValue(), user.getHashed())) {
			if (!passwordField2.getValue().equals("")) {
				if (passwordField2.getValue().equals(passwordField3.getValue())) {
					return true;
				} else {
					Notification notification = new Notification("提示：", "重复密码与新密码不匹配。", Type.ERROR_MESSAGE);
					notification.setDelayMsec(2000);
					notification.show(Page.getCurrent());
				}
			} else {
				Notification notification = new Notification("提示：", "新密码不能为空。", Type.ERROR_MESSAGE);
				notification.setDelayMsec(2000);
				notification.show(Page.getCurrent());
			}
		} else {
			Notification notification = new Notification("提示：", "原密码输入错误。", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
		}
		return false;
	}
	
	public static void open(User user) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ChangePasswordWindow w = new ChangePasswordWindow(user);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private PasswordField passwordField1;
	private PasswordField passwordField2;
	private PasswordField passwordField3;
	private User user;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
