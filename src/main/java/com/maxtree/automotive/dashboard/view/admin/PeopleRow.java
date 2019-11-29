package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback2;

/**
 * 
 * @author Chen
 *
 */
public class PeopleRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PeopleRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(profile,manageUser,manageRole,managePermission);
		
		profile.addLayoutClickListener(e->{
			Callback2 callback = new Callback2() {

				@Override
				public void onSuccessful(Object... objects) {
					profile.updateImageIcon(objects[0].toString());
				}
			};
			
			rootView.forward(new ProfileView("编辑个人信息", rootView, callback));
		});
		
//		manageUser.addLayoutClickListener(e->{
//			rootView.forward(new PeopleView("管理用户", rootView));
//		});
//		manageRole.addLayoutClickListener(e->{
//			rootView.forward(new RoleView("管理角色", rootView));
//		});
//		managePermission.addLayoutClickListener(e->{
//			rootView.forward(new PermissionView("管理权限", rootView));
//		});
	}

	@Override
	public String getSearchTags() {
		return "管理用户,管理角色,管理权限";
	}

	@Override
	public String getImageName() {
		return "users.png";
	}
	
	@Override
	public int getOrderID() {
		return 0;
	}
	
	@Override
	public String getTitle() {
		return "用户";
	}
	
	private RowItemWithIcon profile = new RowItemWithIcon();
	private RowItemWithTitle manageUser = new RowItemWithTitle("管理用户");
	private RowItemWithTitle manageRole = new RowItemWithTitle("管理角色");
	private RowItemWithTitle managePermission = new RowItemWithTitle("管理权限");
	private AdminMainView rootView;
}
