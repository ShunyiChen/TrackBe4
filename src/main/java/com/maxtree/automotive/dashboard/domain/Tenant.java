package com.maxtree.automotive.dashboard.domain;
/**
 * 租户
 * @author chens
 *
 */
public class Tenant {

	public Integer getTenantUniqueId() {
		return tenantUniqueId;
	}

	public void setTenantUniqueId(Integer tenantUniqueId) {
		this.tenantUniqueId = tenantUniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private Integer tenantUniqueId = 0;
	private String name; // 租户名
}
