package com.maxtree.automotive.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限分类
 * 
 * @author chens
 *
 */
public class PermissionCategory {

	public int getCategoryUniqueId() {
		return categoryUniqueId;
	}

	public void setCategoryUniqueId(int categoryUniqueId) {
		this.categoryUniqueId = categoryUniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		// return String.format(
		// "PermissionCategory[categoryUniqueId=%d, name='%s']",
		// categoryUniqueId, name);
		return name;
	}
	
	private List<Permission> permissions = new ArrayList<>();
	private int categoryUniqueId;
	private String name;
}
