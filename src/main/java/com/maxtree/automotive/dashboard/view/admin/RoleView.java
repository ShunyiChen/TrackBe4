package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;

/**
 * 
 * @author Chen
 *
 */
public class RoleView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public RoleView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("角色名称",190), new GridColumn("用户数",180),new GridColumn("访问权限数"),new GridColumn("", 20)};
		List<CustomGridRow> data = new ArrayList<>();
		List<Role> lstRole = ui.roleService.findAll(false);
		for (Role role : lstRole) {
			Object[] rowData = generateOneRow(role);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("角色列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.D1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							
							int roleUniqueId = (int) objects[0];
							Role role = ui.roleService.findById(roleUniqueId);
							Object[] rowData = generateOneRow(role);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditRoleWindow.open(callback);
				} else {
					Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
				}
			}
		};
		grid.setAddEvent(addEvent);
		main.addComponents(grid);
		main.setComponentAlignment(grid, Alignment.TOP_CENTER);
		
		this.addComponent(main);
		this.setComponentAlignment(main, Alignment.TOP_CENTER);
		this.setExpandRatio(main, 1);
	}
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	private Object[] generateOneRow(Role role) {
		int usernum = ui.roleService.getUserCount(role.getRoleUniqueId());
		int capabilitynum = ui.roleService.getCapabilityCount(role.getRoleUniqueId());
		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("分配用户", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.D4)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								Role newRole = ui.roleService.findById(role.getRoleUniqueId());
								Object[] rowData = generateOneRow(newRole);
								grid.setValueAt(new CustomGridRow(rowData), role.getRoleUniqueId());
							}
						};
						AssigningUsersToRoleWindow.open(callback, role);
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
				}
			});
			menu.addItem("设置权限", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.D5)) {
						Callback2 callback = new Callback2() {

							@Override
							public void onSuccessful(Object... objects) {
								Role newRole = ui.roleService.findById(role.getRoleUniqueId());
								Object[] rowData = generateOneRow(newRole);
								grid.setValueAt(new CustomGridRow(rowData), role.getRoleUniqueId());
							}
						};
						AssigningPermissionsToRoleWindow.open(callback, role);
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
				}
			});
			menu.addSeparator();
			menu.addItem("编辑", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
					User loginUser = ui.userService.getUserByUserName(username);
					if (loginUser.isPermitted(PermissionCodes.D2)) {
						Callback2 callback = new Callback2() {

							@Override
							public void onSuccessful(Object... objects) {
								Role newRole = ui.roleService.findById(role.getRoleUniqueId());
								Object[] rowData = generateOneRow(newRole);
								grid.setValueAt(new CustomGridRow(rowData), role.getRoleUniqueId());
							}
						};
						EditRoleWindow.edit(role, callback);
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
				}
			});
			menu.addItem("从列表删除", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
					User loginUser = ui.userService.getUserByUserName(username);
					if (loginUser.isPermitted(PermissionCodes.D3)) {

							boolean force = (usernum != 0 || capabilitynum != 0);
							Callback okAction = new Callback() {
								@Override
								public void onSuccessful() {
									ui.roleService.delete(role.getRoleUniqueId(), force);
									grid.deleteRow(role.getRoleUniqueId());
								}
							};
							MessageBox.showMessage("删除提示", "请确认是否删除当前角色。", MessageBox.WARNING, okAction, "删除");
 

					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
					
				}
			});
			
			
			menu.open(e.getClientX(), e.getClientY());
		});
		
		return new Object[] {role.getRoleName(), usernum, capabilitynum, img, role.getRoleUniqueId()};
	}
	
	private User loggedInUser;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private CustomGrid grid;
}
