package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * Represents a company. Company contains Locations and users in it.
 * 
 * @author chens
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
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPrefecture() {
		return prefecture;
	}

	public void setPrefecture(String prefecture) {
		this.prefecture = prefecture;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCanCreateStorehouse() {
		return canCreateStorehouse;
	}

	public void setCanCreateStorehouse(Integer canCreateStorehouse) {
		this.canCreateStorehouse = canCreateStorehouse;
	}

	public Integer getStorehouseUniqueId() {
		return storehouseUniqueId;
	}

	public void setStorehouseUniqueId(Integer storehouseUniqueId) {
		this.storehouseUniqueId = storehouseUniqueId;
	}

	public Integer getIgnoreChecker() {
		return ignoreChecker;
	}

	public void setIgnoreChecker(Integer ignoreChecker) {
		this.ignoreChecker = ignoreChecker;
	}

	public List<User> getEmployees() {
		return employees;
	}

	public void setEmployees(List<User> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return companyName;
	}

	private Integer companyUniqueId = 0;
	private Integer communityUniqueId = 0;
	private String companyName;
	private String province;   				// 车辆所在省
	private String city;       				// 车辆所在市
	private String prefecture; 				// 车辆所在县
	private String district;				// 车辆所在区
	private String address;                 // 详细地址
	private Integer canCreateStorehouse = 0; // 1-可以创建库房，0-不能创建库房
	private Integer storehouseUniqueId = 0; // 库房ID
	private Integer ignoreChecker = 0;   	// 忽略质检，1-忽略 0-未忽略
	private List<User> employees;
}
