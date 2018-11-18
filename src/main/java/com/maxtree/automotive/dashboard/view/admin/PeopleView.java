package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class PeopleView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public PeopleView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("用户名",130), new GridColumn("机构",120), new GridColumn("社区",120),new GridColumn("角色"),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<User> users = ui.userService.findAll(false);
		for(User user : users) {
			Object[] rowData = generateOneRow(user);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("用户列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.C1)) {
					Callback2 callback = new Callback2() {
						@Override
						public void onSuccessful(Object... objects) {
							int userUniqueId = (int) objects[0];
							User user = ui.userService.findById(userUniqueId);
							Object[] rowData = generateOneRow(user);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditUserWindow.open(callback);
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
	 * @param user
	 * @return
	 */
	private Object[] generateOneRow(User user) {
		StringBuilder roles = new StringBuilder();
		for (Role r : user.getRoles()) {
			roles.append(r.getRoleName());
			roles.append(",");
		}
		if (roles.toString().endsWith(",")) {
			roles.deleteCharAt(roles.length() -1);
		}

		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("分配角色", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.C5)) {
						Callback callback = new Callback() {
							@Override
							public void onSuccessful() {
								User newUser = ui.userService.findById(user.getUserUniqueId());
								Object[] rowData = generateOneRow(newUser);
								grid.setValueAt(new CustomGridRow(rowData), newUser.getUserUniqueId());
							}
						};
						AssigningRolesToUserWindow.open(callback, user);
					} else {
						Notifications.warning("没有权限。");
					}
				}
			});
			menu.addItem("分配业务类型", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.C6)) {
						AssigningBusinessesToUserWindow.open(user);
					}
				}
			});
			menu.addSeparator();
			menu.addItem("重置密码", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.C7)) {
						ResetPasswordWindow.open(user);
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
					if (loggedInUser.isPermitted(PermissionCodes.C2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								User newUser = ui.userService.findById(user.getUserUniqueId());
								Object[] rowData = generateOneRow(newUser);
								grid.setValueAt(new CustomGridRow(rowData), newUser.getUserUniqueId());
							}
						};
						EditUserWindow.edit(user, callback);
					} else {
						Notifications.warning("没有权限。");
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
					if (loggedInUser.isPermitted(PermissionCodes.C3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								ui.userService.delete(user);
								grid.deleteRow(user.getUserUniqueId());
							}
						};
						
						MessageBox.showMessage("提示", "请确定是否要删除用户"+user.getUserName()+"。", MessageBox.WARNING, event, "删除");
					} else {
						Notifications.warning("没有权限。");
					}
					
				}
			});
			
			
			menu.open(e.getClientX(), e.getClientY());
		});
		
		Image photo = new Image(null, new ThemeResource(user.getProfile().getPicture()));
		photo.setWidth("30px");
		photo.setHeight("30px");
		ImageWithString photoWithName = new ImageWithString(photo, user.getUserName());
		
		return new Object[] {photoWithName, user.getCompanyName(), user.getCommunityName(), roles.toString(), img, user.getUserUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
