package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * 机构/公司
 * 
 * @author Chen
 *
 */
public class Company {

	public Integer getCompanyUniqueId() {
		return companyUniqueId;
	}

	public void setCompanyUniqueId(Integer companyUniqueId) {
		this.companyUniqueId = companyUniqueId;
	}

	public Integer getCommunityUniqueId() {
		return communityUniqueId;
	}

	public void setCommunityUniqueId(Integer communityUniqueId) {
		this.communityUniqueId = communityUniqueId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStorehouseName() {
		return storehouseName;
	}

	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}

	public Boolean getQcsupport() {
		return qcsupport;
	}

	public void setQcsupport(Boolean qcsupport) {
		this.qcsupport = qcsupport;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<User> getEmployees() {
		return employees;
	}

	public void setEmployees(List<User> employees) {
		this.employees = employees;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	@Override
	public String toString() {
		return companyName;
	}

	private Integer companyUniqueId = 0;
	private Integer communityUniqueId = 0;
	private String companyName;
	private String address; // 详细地址
	private String storehouseName; // 库房名称
	private Boolean qcsupport = false; // 质检支持
	private String category; // 车管所/二手车/4S店

	private List<User> employees;//非字段
	private String communityName;//非字段
}
