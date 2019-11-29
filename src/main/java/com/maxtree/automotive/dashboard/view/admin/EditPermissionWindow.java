package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;

public class EditPermissionWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 */
//	public EditPermissionWindow() {
//		this.setWidth("513px");
//		this.setHeightUndefined();
//		this.setModal(true);
//		this.setResizable(false);
//		this.setCaption("");
//		this.addStyleName("edit-window");
//		VerticalLayout mainLayout = new VerticalLayout();
//		mainLayout.setSpacing(true);
//		mainLayout.setMargin(false);
//
//		FormLayout form = new FormLayout();
//		form.setSizeFull();
//
//		permissionName = new TextField("权限名称:");
//		permissionName.setWidth("100%");
//		permissionName.setHeight("27px");
//		permissionName.setIcon(VaadinIcons.EDIT);
//		permissionName.focus(); // 设置焦点
//
//		descriptionField = new TextArea("描述:");
//		descriptionField.setValue("");
//		descriptionField.setRows(10);
//		descriptionField.setSizeFull();
//		descriptionField.setIcon(VaadinIcons.EDIT);
//
////		tf1.setRequiredIndicatorVisible(true);
//		form.addComponents(permissionName, descriptionField);
//		HorizontalLayout buttonPane = new HorizontalLayout();
//		buttonPane.setSizeFull();
//		buttonPane.setSpacing(false);
//		buttonPane.setMargin(false);
//		Button btnCancel = new Button("取消");
//		btnAdd = new Button("添加");
//		HorizontalLayout subButtonPane = new HorizontalLayout();
//		subButtonPane.setSpacing(false);
//		subButtonPane.setMargin(false);
//		subButtonPane.setWidth("130px");
//		subButtonPane.setHeight("100%");
//		subButtonPane.addComponents(btnCancel, btnAdd);
//		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
//		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
//		buttonPane.addComponent(subButtonPane);
//		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
//		mainLayout.addComponents(form, buttonPane);
//		this.setContent(mainLayout);
//
//		btnCancel.addClickListener(e -> {
//			close();
//		});
//
//
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		bindFields();
//
//		// Validating Field Values
//		validatingFieldValues();
//
//		// Bind an actual concrete Person instance.
//		// After this, whenever the user changes the value
//		// of nameField, p.setName is automatically called.
//		binder.setBean(permission);
//	}
//
//	/**
//	 *
//	 */
//	private void bindFields() {
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		binder.bind(permissionName, Permission::getName, Permission::setName);
////		binder.bind(boxSiteType, Site::getSiteType, Site::setSiteType);
////		binder.bind(textHostAddr, Site::getHostAddr, Site::setHostAddr);
////		binder.forField(textPort)
////		  .withValidator(new EmptyValidator("端口不能为空"))
////		  .withConverter(new StringToIntegerConverter("Please enter a number"))
////		  .bind(Site::getPort, Site::setPort);
////		binder.bind(textRemoteDirectory, Site::getDefaultRemoteDirectory, Site::setDefaultRemoteDirectory);
////		binder.bind(userName, Site::getUserName, Site::setUserName);
////		binder.bind(password, Site::getPassword, Site::setPassword);
//	}
//
//	/**
//	 *
//	 */
//	private void validatingFieldValues () {
//		// Validating Field Values
//		binder.forField(permissionName).withValidator(new StringLengthValidator(
//	        "角色名称长度范围在2~20个字符",
//	        2, 20))
//	    .bind(Permission::getName, Permission::setName);
//		binder.forField(descriptionField).withValidator(new StringLengthValidator(
//	        "描述长度范围在2~50个字符",
//	        2, 50))
//	    .bind(Permission::getDescription, Permission::setDescription);
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private boolean checkEmptyValues() {
//		if (StringUtils.isEmpty(permissionName.getValue())) {
//			Notification notification = new Notification("提示：", "权限名称不能为空", Type.WARNING_MESSAGE);
//			notification.setDelayMsec(2000);
//			notification.show(Page.getCurrent());
//
//			return false;
//		}
//		else if (StringUtils.isEmpty(descriptionField.getValue())) {
//			Notification notification = new Notification("提示：", "描述不能为空", Type.WARNING_MESSAGE);
//			notification.setDelayMsec(2000);
//			notification.show(Page.getCurrent());
//
//			return false;
//		}
//		if (permissionName.getErrorMessage() != null) {
//			permissionName.setComponentError(permissionName.getErrorMessage());
//			return false;
//		}
//		if (descriptionField.getErrorMessage() != null) {
//			descriptionField.setComponentError(descriptionField.getErrorMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public static void edit(Permission p, Callback callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        EditPermissionWindow w = new EditPermissionWindow();
//        Permission per = ui.permissionService.findById(p.getPermissionUniqueId());
//        w.permission.setPermissionUniqueId(per.getPermissionUniqueId());
//        w.permission.setName(per.getName());
//        w.permission.setDescription(per.getDescription());
//        w.permissionName.setValue(per.getName());
//        w.descriptionField.setValue(per.getDescription());
//        w.btnAdd.setCaption("保存");
//        w.setCaption("编辑权限");
//        w.btnAdd.addClickListener(e -> {
//        	if (w.checkEmptyValues()) {
//    			ui.permissionService.update(w.permission);
//    			w.close();
//    			callback.onSuccessful();
//        	}
//		});
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private TextField permissionName;
//	private TextArea descriptionField;
//	private Button btnAdd;
//	private Binder<Permission> binder = new Binder<>();
//	private Permission permission = new Permission();
//	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
