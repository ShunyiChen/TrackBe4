package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.ClickLabel;
import com.maxtree.automotive.dashboard.component.PermissionBox;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class AssigningPermissionsToRoleWindow extends Window {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param role
//	 */
//	public AssigningPermissionsToRoleWindow(Role role) {
//		this.setWidth("1024px");
//		this.setHeight("768px");
//		this.setModal(true);
//		this.setClosable(true);
//		this.setResizable(true);
//		this.setCaption("为角色分配权限 - "+role.getRoleName());
//		this.addStyleName("set-permissions-to-role-window-"+this.hashCode());
//
//		Styles styles = Page.getCurrent().getStyles();
//    	String css = ".set-permissions-to-role-window-"+this.hashCode()+" { padding:5px !important; }";
//    	String css2 = ".set-permissions-to-role-window-"+this.hashCode()+" .v-image {font-size: 13px !important; }";
//    	String css3 = ".set-permissions-to-role-window-"+this.hashCode()+" .v-label {font-size: 13px !important; font-weight: bold;}";
//    	styles.add(css);
//    	styles.add(css2);
//    	styles.add(css3);
//
//    	HorizontalLayout header = selectAllLink();
//
//    	// 初始化获取已分配的权限
//        List<Permission> assignedPermissions = ui.roleService.assignedPermissions(role.getRoleUniqueId());
//        for (Permission p : assignedPermissions) {
//        	assignedPermissionUniqueIDs.add(p.getPermissionUniqueId());
//        }
//
//        /*
//         Did you mean 100% height and undefined width? (to get horizontal scrollbar, but no vertical one)
//		 Of course - if you want to get both scrollbars and the content size can not be set in pixels, one should use setSizeUndefined().
//         */
//        rows.setWidthUndefined();
//        rows.setHeightUndefined();
//        rows.setMargin(false);
//        rows.setSpacing(false);
//
//        scroll.setContent(rows);
//        scroll.setSizeFull();
//        List<PermissionCategory> categories = ui.permissionCategoryService.findAll();
//        for(PermissionCategory category : categories) {
//        	HorizontalLayout row = new HorizontalLayout();
//        	row.setWidthUndefined();
//        	Label title = new Label(category.getName()+":");
//        	title.setWidth("100px");
//        	row.addComponent(title);
//        	row.setComponentAlignment(title, Alignment.TOP_LEFT);
//
//        	List<Permission> lst = ui.permissionService.findByCategoryUniqueId(category.getCategoryUniqueId());
//        	for(Permission p : lst) {
//        		PermissionBox box = new PermissionBox(p);
//        		allBoxes.add(box);
//        	    if (assignedPermissionUniqueIDs.contains(p.getPermissionUniqueId())) {
//        	    	box.setSelected(true);
//        	    }
//        		box.setWidth("100px");
//        		row.addComponent(box);
//        		row.setComponentAlignment(box, Alignment.MIDDLE_LEFT);
//        	}
//
//        	rows.addComponent(row);
//        	rows.setComponentAlignment(row, Alignment.MIDDLE_LEFT);
//        }
//
//        Button btnCancel = new Button("取消");
//		btnOK = new Button("确定");
//		btnApply = new Button("应用");
//		HorizontalLayout subButtonPane = new HorizontalLayout();
//		subButtonPane.setWidthUndefined();
//		subButtonPane.setHeight("40px");
//		subButtonPane.setMargin(false);
//		subButtonPane.setSpacing(false);
//		subButtonPane.addComponents(btnCancel,Box.createHorizontalBox(5),btnOK,Box.createHorizontalBox(5),btnApply);
//		subButtonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
//		subButtonPane.setComponentAlignment(btnOK, Alignment.MIDDLE_LEFT);
//		subButtonPane.setComponentAlignment(btnApply, Alignment.MIDDLE_LEFT);
//		btnCancel.addClickListener(e -> {
//			close();
//		});
//
//		main.setSizeFull();
//		main.setMargin(false);
//		main.setSpacing(false);
//		main.addComponents(header,scroll,subButtonPane);
//		main.setComponentAlignment(header, Alignment.TOP_LEFT);
//		main.setComponentAlignment(scroll, Alignment.TOP_LEFT);
//		main.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
//		main.setExpandRatio(header, 0);
//		main.setExpandRatio(scroll, 1);
//		main.setExpandRatio(subButtonPane, 0);
//		this.setContent(main);
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private HorizontalLayout selectAllLink() {
//		HorizontalLayout main = new HorizontalLayout();
//		main.setWidth("100%");
//		main.setHeightUndefined();
//		main.setSpacing(false);
//		main.setMargin(false);
//		HorizontalLayout sub = new HorizontalLayout();
//		sub.setWidthUndefined();
//		sub.setHeightUndefined();
//		sub.setSpacing(false);
//		sub.setMargin(false);
//
//		ClickLabel checkAll = new ClickLabel("<span style='cursor:pointer;font-size:13px;font-weight: bold;color: #2499DD;'>全选</span>");
//		checkAll.addLayoutClickListener(new LayoutClickListener() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void layoutClick(LayoutClickEvent event) {
//				for(PermissionBox box : allBoxes) {
//					box.setSelected(true);
//				}
//			}
//		});
//		ClickLabel checkNone = new ClickLabel("<span style='cursor:pointer;font-size:13px;font-weight: bold;color: #2499DD;'>全不选</span>");
//		checkNone.addLayoutClickListener(new LayoutClickListener() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void layoutClick(LayoutClickEvent event) {
//				for(PermissionBox box : allBoxes) {
//					box.setSelected(false);
//				}
//			}
//		});
//		Label separater = new Label("<span style='font-size:13px;font-weight: bold;color: #2499DD;'>|</span>", ContentMode.HTML);
//		sub.addComponents(checkAll, separater, checkNone, Box.createHorizontalBox(21));
//		sub.setComponentAlignment(checkAll, Alignment.MIDDLE_CENTER);
//		sub.setComponentAlignment(separater, Alignment.MIDDLE_CENTER);
//		sub.setComponentAlignment(checkNone, Alignment.MIDDLE_CENTER);
//
//		main.addComponent(sub);
//		main.setComponentAlignment(sub, Alignment.MIDDLE_RIGHT);
//		return main;
//	}
//
//	/**
//	 *
//	 * @param role
//	 */
//	private void apply(Role role) {
//		List<Integer> permissionUniqueIDs = new ArrayList<>();
//		for(PermissionBox box : allBoxes) {
//			if (box.isSelected()) {
//				permissionUniqueIDs.add(box.getPermission().getPermissionUniqueId());
//			}
//		}
//		ui.roleService.updateRolePermissions(role.getRoleUniqueId(), permissionUniqueIDs);
//
//		List<User> assignedUsers = ui.roleService.assignedUsers(role.getRoleUniqueId());
//		for (User user : assignedUsers) {
//			// update the cache
//		    CacheManager.getInstance().getPermissionCache().refresh(user.getUserUniqueId());
//		}
//	}
//
//	public static void open(Callback2 callback, Role role) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        AssigningPermissionsToRoleWindow w = new AssigningPermissionsToRoleWindow(role);
//        w.btnApply.addClickListener(e -> {
//        	w.apply(role);
//			callback.onSuccessful();
//		});
//        w.btnOK.addClickListener(e -> {
//        	w.apply(role);
//			w.close();
//			callback.onSuccessful();
//		});
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private List<PermissionBox> allBoxes = new ArrayList<>();
//	private Panel scroll = new Panel();
//	private VerticalLayout rows = new VerticalLayout();
//	private VerticalLayout main = new VerticalLayout();
//	private Button btnApply;
//	private Button btnOK;
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private List<Integer> assignedPermissionUniqueIDs = new ArrayList<>();
}
