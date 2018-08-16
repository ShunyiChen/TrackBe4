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

	public Integer getHasStoreHouse() {
		return hasStoreHouse;
	}

	public void setHasStoreHouse(Integer hasStoreHouse) {
		this.hasStoreHouse = hasStoreHouse;
	}

	public String getStorehouseName() {
		return storehouseName;
	}

	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}

	public Integer getIgnoreChecker() {
		return ignoreChecker;
	}

	public void setIgnoreChecker(Integer ignoreChecker) {
		this.ignoreChecker = ignoreChecker;
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

	@Override
	public String toString() {
		return companyName;
	}

	private Integer companyUniqueId = 0;
	private Integer communityUniqueId = 0;
	private String companyName;
	private String province;   				// 车辆所在省份
	private String city;       				// 车辆所在地级市
	private String district;				// 车辆所在市、县级市
	private String address;                 // 详细地址
	private Integer hasStoreHouse = 0; 		// 是否存在库房1存在,0不存在
	private String storehouseName;		 	// 库房名称（作为唯一标识）
	private Integer ignoreChecker = 0;   	// 忽略质检，1-忽略 0-未忽略
	private String category;				// 车管所/二手车/4S店
	private List<User> employees;
}
