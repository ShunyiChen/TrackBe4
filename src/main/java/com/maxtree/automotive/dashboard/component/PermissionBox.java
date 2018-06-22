package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.domain.Permission;

public class PermissionBox extends SwitchButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionBox(Permission permission) {
		super(false, permission.getName(), permission.getDescription(), SwitchButton.WHITE);
		this.permission = permission;
		img.setDescription(permission.getDescription());
	}

	public Permission getPermission() {
		return permission;
	}

	private Permission permission;
	
	
//	public PermissionBox(PermissionCodes permissionCode) {
//		super(false, permissionCode.name, SwitchButton.WHITE);
//		img.setDescription(permissionCode.description);
//	}
	
}
