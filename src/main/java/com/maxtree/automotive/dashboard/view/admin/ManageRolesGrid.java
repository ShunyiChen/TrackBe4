package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;

public class ManageRolesGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ManageRolesGrid() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("90%");
		this.setHeightUndefined();
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		// 表头
		HorizontalLayout header = createGridHeader();
		// 表体
		Panel body = createGridBody();
		Button addButton = new Button("添加");
		addButton.addStyleName("grid-button-without-border");
		addButton.addClickListener(e-> {
			
			if (loginUser.isPermitted(PermissionCodes.D1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditRoleWindow.open(callback);
			} else {
				Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
			}
		});
		
		this.addComponents(header, body, Box.createVerticalBox(15), addButton);
		this.setComponentAlignment(header, Alignment.TOP_CENTER);
		this.setComponentAlignment(body, Alignment.TOP_CENTER);
		this.setComponentAlignment(addButton, Alignment.TOP_RIGHT);
	}

	/**
	 * 
	 * @return
	 */
	private HorizontalLayout createGridHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidth("100%");
		header.setHeight("50px");
		header.addStyleName("grid-header-line");
		Label columnName = new Label("角色名称");
		columnName.addStyleName("grid-title");
		Label columnType = new Label("类型");
		columnType.addStyleName("grid-title");
		Label columnUserCount = new Label("用户数");
		columnUserCount.addStyleName("grid-title");
		Label columnCapabilities = new Label("功能数");
		columnCapabilities.addStyleName("grid-title");
		header.addComponents(columnName, columnType, columnUserCount, columnCapabilities);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnType, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnUserCount, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnCapabilities, Alignment.MIDDLE_LEFT);
		return header;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createGridBody() {
		Panel gridPanel = new Panel();
		gridPanel.addStyleName("grid-body-without-border");
		gridPanel.setHeight("340px");
		tableBody = new VerticalLayout(); 
		tableBody.setMargin(false);
		tableBody.setSpacing(false);
		List<Role> lstRole = ui.roleService.findAll(false);
		for (Role role : lstRole) {
			HorizontalLayout row1 = createDataRow(role);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
		gridPanel.setContent(tableBody);
		return gridPanel;
	}
	
	/**
	 * 
	 * @param business
	 * @return
	 */
	private HorizontalLayout createDataRow(Role role) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label roleName = new Label(role.getRoleName());
		Label roleType = new Label(role.getRoleType());
		
		int usernum = ui.roleService.getUserCount(role.getRoleUniqueId());
		int capabilitynum = ui.roleService.getCapabilityCount(role.getRoleUniqueId());
		
		Label userCount = new Label(usernum+"");
		Label capabilities = new Label(capabilitynum+"");
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("分配用户", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.D4)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningUsersToRoleWindow.open(callback, role);
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
				}
			});
			menu.addItem("设置权限", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.D5)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
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
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.D2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditRoleWindow.edit(role, callback);
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
				}
			});
			menu.addItem("从列表删除", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.D3)) {
						if (role.getRoleType().equals("自定义")) {
							boolean force = (usernum != 0 || capabilitynum != 0);
							Callback okAction = new Callback() {
								@Override
								public void onSuccessful() {
									ui.roleService.delete(role.getRoleUniqueId(), force);
									refreshTable();
								}
							};
							MessageBox.showMessage("删除提示", "请确认是否删除当前角色。", MessageBox.WARNING, okAction, "删除");
 
						} else {
							Notification notification = new Notification("提示：", "内建角色无法删除。", Type.WARNING_MESSAGE);
							notification.setDelayMsec(2000);
							notification.show(Page.getCurrent());
						}
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
					
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		roleName.setWidth("144.05px");
		roleType.setWidth("154.05px");
		userCount.setWidth("144.05px");
		capabilities.setWidth("90.05px");
		row.addComponents(roleName, roleType, userCount, capabilities, moreImg, Box.createHorizontalBox(13));
		row.setComponentAlignment(roleName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(roleType, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(userCount, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(capabilities, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		List<Role> lstRole = ui.roleService.findAll(false);
		for (Role role : lstRole) {
			HorizontalLayout row1 = createDataRow(role);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout tableBody;
}
