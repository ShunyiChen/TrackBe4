package com.maxtree.automotive.dashboard.view.admin;

public class PermissionView {//} extends ContentView {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param parentTitle
	 * @param rootView
	 */
//	public PermissionView(String parentTitle, AdminMainView rootView) {
//		super(parentTitle, rootView);
//		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		loggedInUser = ui.userService.getUserByUserName(username);
//		main.setWidth("100%");
//		main.setHeightUndefined();
//		main.setSpacing(false);
//		main.setMargin(false);
//
//		GridColumn[] columns = {new GridColumn("权限名称",130), new GridColumn("描述",240), new GridColumn("角色数",100),new GridColumn("用户数",100),new GridColumn("", 20)};
//		List<CustomGridRow> data = new ArrayList<>();
//		List<Permission> result = ui.permissionService.findAll();
//		for (Permission p : result) {
//			Object[] rowData = generateOneRow(p);
//			data.add(new CustomGridRow(rowData));
//		}
//		grid = new CustomGrid("权限列表",columns, data, false);
//		main.addComponents(grid);
//		main.setComponentAlignment(grid, Alignment.TOP_CENTER);
//
//		this.addComponent(main);
//		this.setComponentAlignment(main, Alignment.TOP_CENTER);
//		this.setExpandRatio(main, 1);
//	}
//
//	/**
//	 *
//	 * @param user
//	 * @return
//	 */
//	private Object[] generateOneRow(Permission permission) {
//		int roleNum = ui.permissionService.getRoleCount(permission.getPermissionUniqueId());
//		int userNum = ui.permissionService.getUserCount(permission.getPermissionUniqueId());
//		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
//		img.addStyleName("PeopleView_menuImage");
//		img.addClickListener(e->{
//			ContextMenu menu = new ContextMenu(img, true);
//			menu.addItem("分配角色", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.E1)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Permission p = ui.permissionService.findById(permission.getPermissionUniqueId());
//								Object[] rowData = generateOneRow(p);
//								grid.setValueAt(new CustomGridRow(rowData), permission.getPermissionUniqueId());
//							}
//						};
//						AssigningRolesToPermissionWindow.open(callback, permission);
//					} else {
//						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//					}
//				}
//			});
//			menu.addSeparator();
//			menu.addItem("编辑", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.E2)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Permission p = ui.permissionService.findById(permission.getPermissionUniqueId());
//								Object[] rowData = generateOneRow(p);
//								grid.setValueAt(new CustomGridRow(rowData), permission.getPermissionUniqueId());
//							}
//						};
//						EditPermissionWindow.edit(permission, callback);
//					} else {
//						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//					}
//				}
//			});
//			menu.open(e.getClientX(), e.getClientY());
//		});
//
//		return new Object[] {permission.getName(), permission.getDescription(),roleNum,userNum,img,permission.getPermissionUniqueId()};
//	}
//
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private VerticalLayout main = new VerticalLayout();
//	private User loggedInUser;
//	private CustomGrid grid;
}
