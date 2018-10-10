package com.maxtree.automotive.dashboard.domain;

/**
 * 角色与权限关系
 * 
 * @author chens
 *
 */
public class RoleRight {

	public int getRoleRightUniqueId() {
		return roleRightUniqueId;
	}

	public void setRoleRightUniqueId(int roleRightUniqueId) {
		this.roleRightUniqueId = roleRightUniqueId;
	}

	public int getRoleUniqueId() {
		return roleUniqueId;
	}

	public void setRoleUniqueId(int roleUniqueId) {
		this.roleUniqueId = roleUniqueId;
	}

	public int getPermissionUniqueId() {
		return permissionUniqueId;
	}

	public void setPermissionUniqueId(int permissionUniqueId) {
		this.permissionUniqueId = permissionUniqueId;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	private int roleRightUniqueId; // Role right unique Id
	private int roleUniqueId; // Role unique Id
	private int permissionUniqueId; // Permission unique Id
	private Role role; // Role that has this role right
	private Permission permission; // Permission
}
