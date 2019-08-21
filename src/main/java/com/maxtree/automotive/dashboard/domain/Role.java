package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * 角色
 * 
 * @author chens
 *
 */
public class Role {

	public Integer getRoleUniqueId() {
		return roleUniqueId;
	}

	public void setRoleUniqueId(Integer roleUniqueId) {
		this.roleUniqueId = roleUniqueId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return roleName;
	}

	private Integer roleUniqueId = 0; // Role unique Id
	private String roleName; // Role name
	private List<Permission> permissions;
}
