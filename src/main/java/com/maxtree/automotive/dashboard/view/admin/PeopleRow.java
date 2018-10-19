package com.maxtree.automotive.dashboard.view.admin;

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

	public PeopleRow() {
		initComponents();
	}
	
	private void initComponents() {
		
		this.addComponents(profile,manageUser,manageRole,managePermission);
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
}
