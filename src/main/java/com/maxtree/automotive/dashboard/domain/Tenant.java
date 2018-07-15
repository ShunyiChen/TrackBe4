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

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	@Override
	public String toString() {
		return tenantName;
	}
	
	private Integer tenantUniqueId = 0;//增长ID
	private String tenantName;//租户名
	private String communityName;// 分配的社区名（非数据库字段）
}
