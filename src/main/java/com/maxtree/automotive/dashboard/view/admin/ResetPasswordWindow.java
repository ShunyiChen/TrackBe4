package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ResetPasswordWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 */
//	public ResetPasswordWindow(User user) {
//		this.user = user;
//		this.setResizable(false);
//		this.setCaption("修改密码");
//		this.setModal(true);
//		this.setWidth("370px");
//		this.setHeightUndefined();
//		this.addStyleName("edit-window");
//		initComponents();
//	}
//
//	private void initComponents() {
//		VerticalLayout vlayout = new VerticalLayout();
//        vlayout.setWidth("100%");
//        vlayout.setHeightUndefined();
//        vlayout.setMargin(false);
//        vlayout.setSpacing(false);
//
//		FormLayout mainLayout = new FormLayout();
//		mainLayout.setWidth("100%");
//		mainLayout.setSpacing(false);
//		mainLayout.setMargin(false);
//		passwordField = new PasswordField("新密码:");
//		passwordField.setIcon(VaadinIcons.PASSWORD);
//		passwordField.focus();
//		passwordField.setWidth("200px");
//		passwordField.setHeight("25px");
//		confirmField = new PasswordField("重复密码:");
//		confirmField.setIcon(VaadinIcons.PASSWORD);
//		confirmField.setWidth("200px");
//		confirmField.setHeight("25px");
//
//		HorizontalLayout bottomPane = new HorizontalLayout();
//		bottomPane.setSpacing(false);
//		bottomPane.setMargin(false);
//		bottomPane.setWidth("100%");
//		bottomPane.setHeightUndefined();
//
//		HorizontalLayout buttonPane = new HorizontalLayout();
//		buttonPane.setWidth("130px");
//		buttonPane.setHeight("30px");
//		buttonPane.setSpacing(false);
//		buttonPane.setMargin(false);
//		Button btnCancel = new Button("取消");
//		Button btnSave = new Button("确定");
//		btnCancel.addStyleName("grid-button-without-border");
//		btnSave.addStyleName("grid-button-without-border");
//
//		buttonPane.addComponents(btnCancel, btnSave);
//		buttonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);
//		buttonPane.setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
//
//		mainLayout.addComponents(passwordField,confirmField);
//
//		btnCancel.addClickListener(e -> {
//			close();
//		});
//
//		btnSave.addClickListener(e -> {
//			if(validate()) {
//				// Update the password
//				ui.userService.updatePassword(user.getUserUniqueId(), passwordField.getValue());
//				close();
//			}
//		});
//
//		bottomPane.addComponent(buttonPane);
//		bottomPane.setComponentAlignment(buttonPane, Alignment.MIDDLE_RIGHT);
//
//		vlayout.addComponents(mainLayout, Box.createVerticalBox(5), bottomPane);
//
//		this.setContent(vlayout);
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private boolean validate() {
//		if (StringUtils.isEmpty(passwordField.getValue())
//				|| StringUtils.isEmpty(confirmField.getValue())) {
//			Notifications.warning("文本框不能为空。");
//			return false;
//
//		} else if (!passwordField.getValue().equals(confirmField.getValue())) {
//			Notifications.warning("输入密码与确认密码不一致。");
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 *
//	 * @param user
//	 */
//	public static void open(User user) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        ResetPasswordWindow w = new ResetPasswordWindow(user);
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private PasswordField passwordField;
//	private PasswordField confirmField;
//	private User user;
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
