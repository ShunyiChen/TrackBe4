package com.maxtree.automotive.dashboard.domain;

public class Tenant {

	public Tenant() {}
	
	public Tenant(String tenantName) {
		this.tenantName = tenantName;
	}
	
	public Integer getTenantUniqueId() {
		return tenantUniqueId;
	}

	public void setTenantUniqueId(Integer tenantUniqueId) {
		this.tenantUniqueId = tenantUniqueId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	@Override
	public String toString() {
		return tenantName;
	}
	
	private Integer tenantUniqueId = 0;
	private String tenantName;
}
