package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Role;
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

public class EditRoleWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditRoleWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新角色");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSizeFull();
		
		roleNameField = new TextField("角色名称:");
		roleNameField.setIcon(VaadinIcons.EDIT);
		roleNameField.focus(); // 设置焦点
		
//		tf1.setRequiredIndicatorVisible(true);
		form.addComponents(roleNameField);
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		btnPermission = new Button("权限");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("195px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnAdd, btnPermission);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnPermission, Alignment.BOTTOM_RIGHT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(form, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		setComponentSize(350, 27);
		
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		bindFields();
		
		// Validating Field Values
		validatingFieldValues();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(role);
	}
	
	private void setComponentSize(int w, int h) {
		roleNameField.setWidth(w+"px");
		roleNameField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(roleNameField, Role::getRoleName, Role::setRoleName);
//		binder.bind(boxSiteType, Site::getSiteType, Site::setSiteType);
//		binder.bind(textHostAddr, Site::getHostAddr, Site::setHostAddr);
//		binder.forField(textPort)
//		  .withValidator(new EmptyValidator("端口不能为空"))
//		  .withConverter(new StringToIntegerConverter("Please enter a number"))
//		  .bind(Site::getPort, Site::setPort);
//		binder.bind(textRemoteDirectory, Site::getDefaultRemoteDirectory, Site::setDefaultRemoteDirectory);
//		binder.bind(userName, Site::getUserName, Site::setUserName);
//		binder.bind(password, Site::getPassword, Site::setPassword);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(roleNameField).withValidator(new StringLengthValidator(
	        "角色名称长度范围在2~20个字符",
	        2, 20))
	    .bind(Role::getRoleName, Role::setRoleName);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(role.getRoleName())) {
			Notification notification = new Notification("提示：", "角色名称不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			
			return false;
		}
		if (roleNameField.getErrorMessage() != null) {
			roleNameField.setComponentError(roleNameField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditRoleWindow w = new EditRoleWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.roleService.insert(w.role);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        w.btnPermission.addClickListener(e -> {
			if (w.checkEmptyValues()) {
				int newRoleUniqueId = ui.roleService.insert(w.role);
				Role r = ui.roleService.findById(newRoleUniqueId);
				w.close();
				callback.onSuccessful();
				AssigningPermissionsToRoleWindow.open(callback, r);
			}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(Role role, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditRoleWindow w = new EditRoleWindow();
        w.refreshAction = callback;
        Role r = ui.roleService.findById(role.getRoleUniqueId());
        w.role.setRoleUniqueId(r.getRoleUniqueId());
        w.roleNameField.setValue(r.getRoleName());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑角色");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.roleService.update(w.role);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        w.btnPermission.addClickListener(e -> {
			if (w.checkEmptyValues()) {
				AssigningPermissionsToRoleWindow.open(callback, r);
			}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField roleNameField;
	private Button btnAdd;
	private Button btnPermission;
	private Binder<Role> binder = new Binder<>();
	private Role role = new Role();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Callback refreshAction;
}
