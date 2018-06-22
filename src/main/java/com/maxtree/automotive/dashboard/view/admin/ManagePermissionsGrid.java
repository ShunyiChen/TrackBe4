package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManagePermissionsGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ManagePermissionsGrid() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("90%");
		this.setHeightUndefined();
		// 表头
		HorizontalLayout header = createGridHeader();
		// 表体
		Panel body = createGridBody();
		this.addComponents(header, body, Box.createVerticalBox(15));
		this.setComponentAlignment(header, Alignment.TOP_CENTER);
		this.setComponentAlignment(body, Alignment.TOP_CENTER);
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
		Label columnName = new Label("权限名称");
		columnName.addStyleName("grid-title");
		Label columnDescption = new Label("描述");
		columnDescption.addStyleName("grid-title");
		Label columnRoleCount = new Label("角色数");
		columnRoleCount.addStyleName("grid-title");
		Label columnUserCount = new Label("用户数");
		columnUserCount.addStyleName("grid-title");
		header.addComponents(columnName, columnDescption, columnRoleCount, columnUserCount);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnDescption, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnRoleCount, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnUserCount, Alignment.MIDDLE_LEFT);
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
		List<Permission> result = ui.permissionService.findAll();
		for (Permission p : result) {
			HorizontalLayout row1 = createDataRow(p);
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
	private HorizontalLayout createDataRow(Permission permission) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label name = new Label(permission.getName());
		Label description = new Label(permission.getDescription());
		
		int roleNum = ui.permissionService.getRoleCount(permission.getPermissionUniqueId());
		int userNum = ui.permissionService.getUserCount(permission.getPermissionUniqueId());
		Label roleCount = new Label(roleNum+"");
		Label userCount = new Label(userNum+"");
		
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("分配角色", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.E1)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningRolesToPermissionWindow.open(callback, permission);
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
					if (loginUser.isPermitted(PermissionCodes.E2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditPermissionWindow.edit(permission, callback);
					} else {
						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
					}
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		name.setWidth("143.05px");
		description.setWidth("143.05px");
		roleCount.setWidth("140.05px");
		userCount.setWidth("70.05px");
		row.addComponents(name, description, roleCount, userCount, Box.createHorizontalBox(30), moreImg, Box.createHorizontalBox(13));
		
		row.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(description, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(roleCount, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(userCount, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		List<Permission> result = ui.permissionService.findAll();
		for (Permission p : result) {
			HorizontalLayout row1 = createDataRow(p);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout tableBody;
	private static final int STOP = 0;
	private static final int RUNNING = 1;
}
