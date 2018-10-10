package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * 权限
 * 
 * @author chens
 *
 */
public class Permission {

	public Integer getPermissionUniqueId() {
		return permissionUniqueId;
	}

	public void setPermissionUniqueId(Integer permissionUniqueId) {
		this.permissionUniqueId = permissionUniqueId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RoleRight> getLstRoleRight() {
		return lstRoleRight;
	}

	public void setLstRoleRight(List<RoleRight> lstRoleRight) {
		this.lstRoleRight = lstRoleRight;
	}

	public Integer getCategoryUniqueId() {
		return categoryUniqueId;
	}

	public void setCategoryUniqueId(Integer categoryUniqueId) {
		this.categoryUniqueId = categoryUniqueId;
	}

	@Override
	public String toString() {
		return String.format("Permission[permissionUniqueId=%d, name='%s', description='%s']", permissionUniqueId, name,
				description);
	}

	private Integer permissionUniqueId = 0; // Permission unique Id
	private String code;
	private String name; // Permission name
	private String description; // Permission description
	private List<RoleRight> lstRoleRight; // Role right list
	private Integer categoryUniqueId = 0; // 权限类别ID(内置)
}
