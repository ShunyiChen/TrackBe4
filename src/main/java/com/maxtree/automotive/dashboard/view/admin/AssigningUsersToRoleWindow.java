package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class AssigningUsersToRoleWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param role
	 */
	public AssigningUsersToRoleWindow(Role role) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为角色分配用户");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label roleName = new Label(role.getRoleName());
		HorizontalLayout title = new HorizontalLayout();
		title.setWidthUndefined();
		title.setHeightUndefined();
		title.setSpacing(false);
		title.setMargin(false);
		title.addComponents(img, Box.createHorizontalBox(5), roleName);
		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		title.setComponentAlignment(roleName, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		
		List<User> allUsers = null;
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		User operator = ui.userService.getUserByUserName(username);
		if (operator.getUserName().equals("system")) {
			allUsers = ui.userService.findAll(true);
		} else {
			allUsers = ui.userService.findAll(operator);
		}
		
		select = new TwinColSelect<>(null, allUsers);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的用户");
		select.setRightColumnCaption("已分配的用户");
		List<User> assignedUsers = ui.roleService.assignedUsers(role.getRoleUniqueId());
		List<User> selectedUsers = new ArrayList<>();
		for (User user : allUsers) {
			for (User assignedUser : assignedUsers) {
				if (assignedUser.getUserUniqueId().intValue() == user.getUserUniqueId().intValue()) {
					selectedUsers.add(user);
				}
			}
		}
		// set select
		select.select(selectedUsers.toArray(new User[selectedUsers.size()]));
		
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
	
	private void apply(Role role) {
		Set<User> set = select.getSelectedItems();
		List<User> list = new ArrayList<>(set);
		// update database
		ui.roleService.updateRoleUsers(role.getRoleUniqueId(), list);
		// 更新缓存
		for (User user : list) {
			// update the cache
		    CacheManager.getInstance().getPermissionCache().refresh(user.getUserUniqueId());
		}
	}
	
	public static void open(Callback callback, Role role) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningUsersToRoleWindow w = new AssigningUsersToRoleWindow(role);
        w.btnApply.addClickListener(e -> {
        	w.apply(role);
			callback.onSuccessful();
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(role);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TwinColSelect<User> select;
}
