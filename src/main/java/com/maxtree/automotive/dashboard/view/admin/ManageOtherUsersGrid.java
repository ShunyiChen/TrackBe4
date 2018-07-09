package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageOtherUsersGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ManageOtherUsersGrid() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("90%");
		this.setHeightUndefined();
		// 表头
		HorizontalLayout header = createGridHeader();
		// 表体
		Panel body = createGridBody();
		Button addButton = new Button("添加");
		addButton.addStyleName("grid-button-without-border");
		addButton.addClickListener(e-> {
			
			User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
			if (loginUser.isPermitted(PermissionCodes.C1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditUserWindow.open(callback);
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
		Label columnName = new Label("用户名");
		columnName.addStyleName("grid-title");
		Label columnFullName = new Label("姓名");
		columnFullName.addStyleName("grid-title");
		Label columnRole = new Label("角色");
		columnRole.addStyleName("grid-title");
		Label columnCompany = new Label("机构");
		columnCompany.addStyleName("grid-title");
		header.addComponents(columnName, columnFullName, columnRole, columnCompany);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnFullName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnRole, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnCompany, Alignment.MIDDLE_LEFT);
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
		
		List<User> allUsers = null;
		User operator = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		if (operator.getUserName().equals("root")) {
			allUsers = ui.userService.findAll(true);
		} else if (operator.getUserName().equals("system")) {
			allUsers = ui.userService.findAll(false);
		} else {
			allUsers = ui.userService.findAll(operator);
		}
		for (User u : allUsers) {
			HorizontalLayout row1 = createDataRow(u);
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
	private HorizontalLayout createDataRow(User user) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Image p = new Image(null, new ThemeResource(user.getProfile().getPicture()));
		p.setWidth("30px");
		p.setHeight("30px");
		// 用户名
		Label username = new Label(user.getUserName());
		HorizontalLayout rowWithImage = new HorizontalLayout();
		rowWithImage.setSpacing(false);
		rowWithImage.setMargin(false);
		rowWithImage.addComponents(p, Box.createHorizontalBox(10), username);
		rowWithImage.setComponentAlignment(username, Alignment.MIDDLE_CENTER);
		// 姓名
		Label fullname = new Label(user.getProfile().getLastName()+""+user.getProfile().getFirstName());
		StringBuilder roles = new StringBuilder();
		for (Role r : user.getRoles()) {
			roles.append(r.getRoleName());
			roles.append(",");
		}
		if (roles.toString().endsWith(",")) {
			roles.deleteCharAt(roles.length() -1);
		}
		// 角色
		Label roleLabel = new Label(roles.toString());
		// 机构
		Label companyLabel = new Label(user.getCompanyName());

		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("分配角色", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					if (loginUser.isPermitted(PermissionCodes.C5)) {
						Callback callback = new Callback() {
							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningRolesToUserWindow.open(callback, user);
					} else {
						Notifications.warning("没有权限。");
					}
				}
			});
			menu.addItem("分配业务类型", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loginUser.isPermitted(PermissionCodes.C6)) {
						AssigningBusinessesToUserWindow.open(user);
					}
				}
			});
			menu.addSeparator();
			menu.addItem("重置密码", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loginUser.isPermitted(PermissionCodes.C7)) {
						ResetPasswordWindow.open(user);
					}
				}
			});
			menu.addSeparator();
			menu.addItem("编辑", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loginUser.isPermitted(PermissionCodes.C2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditUserWindow.edit(user, callback);
					} else {
						Notifications.warning("没有权限。");
					}
				}
			});
			menu.addItem("从列表删除", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loginUser.isPermitted(PermissionCodes.C3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								ui.userService.delete(user);
								refreshTable();
							}
						};
						
						MessageBox.showMessage("删除提示", "您确定要删除用户"+user.getUserName()+"?", MessageBox.WARNING, event, "删除");
					} else {
						Notifications.warning("没有权限。");
					}
					
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		username.setWidth("100px");
		fullname.setWidth("142px");
		roleLabel.setWidth("140px");
		companyLabel.setWidth("110px");
		
		row.addComponents(rowWithImage, fullname, roleLabel, companyLabel, moreImg, Box.createHorizontalBox(13));
		
		row.setComponentAlignment(rowWithImage, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(fullname, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(roleLabel, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(companyLabel, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_LEFT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		
		List<User> allUsers = null;
		User operator = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		if (operator.getUserName().equals("root")) {
			allUsers = ui.userService.findAll(true);
		} else if (operator.getUserName().equals("system")) {
			allUsers = ui.userService.findAll(false);
		} else {
			allUsers = ui.userService.findAll(operator);
		}
		
		for (User u : allUsers) {
			HorizontalLayout row1 = createDataRow(u);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout tableBody;
}
