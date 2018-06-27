package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AssigningRolesToPermissionWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param role
	 */
	public AssigningRolesToPermissionWindow(Permission p) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为权限分配角色");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
 
//		Image img = new Image(null, new ThemeResource("img/adminmenu/adminrole.png"));
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label permissionName = new Label(p.getName());
		HorizontalLayout title = new HorizontalLayout();
		title.setWidthUndefined();
		title.setHeightUndefined();
		title.setSpacing(false);
		title.setMargin(false);
		title.addComponents(img, Box.createHorizontalBox(5), permissionName);
		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		title.setComponentAlignment(permissionName, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		
		List<Role> allRoles = ui.roleService.findAll();
		
		List<Role> assignRoles = ui.permissionService.assignedRoles(p.getPermissionUniqueId());
		
		select = new TwinColSelect<>(null, allRoles);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的角色");
		select.setRightColumnCaption("已分配的角色");
		
		List<Role> selectedRoles = new ArrayList<>();
		for (Role role : allRoles) {
			for (Role assignRole : assignRoles) {
				if (assignRole.getRoleUniqueId() == role.getRoleUniqueId()) {
					selectedRoles.add(role);
				}
			}
		}
		// set select
		select.select(selectedRoles.toArray(new Role[selectedRoles.size()]));
		
        hlayout.addComponent(select);
        hlayout.setComponentAlignment(select, Alignment.TOP_CENTER);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnOK = new Button("确定");
		btnApply = new Button("应用");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("196px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnOK, btnApply);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_RIGHT);
		subButtonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_RIGHT);
		subButtonPane.setComponentAlignment(btnApply, Alignment.BOTTOM_RIGHT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(title, hlayout, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}
	
	private void apply(Permission permission) {
		Set<Role> set = select.getSelectedItems();
		List<Role> list = new ArrayList<>(set);
		// update database
		ui.permissionService.updatePermissionRoles(permission.getPermissionUniqueId(), list);
		// update the cache
		
		// 更新缓存
		List<User> assignedUsers = ui.permissionService.getPermissionAssignedUserIDs(permission.getPermissionUniqueId());
		for (User user : assignedUsers) {
			CacheManager.getInstance().getPermissionCache().refresh(user.getUserUniqueId());
		}
	}
	
	public static void open(Callback callback, Permission p) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningRolesToPermissionWindow w = new AssigningRolesToPermissionWindow(p);
        w.btnApply.addClickListener(e -> {
        	w.apply(p);
			callback.onSuccessful();
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(p);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TwinColSelect<Role> select = null;

}
