package com.maxtree.automotive.dashboard.domain;

/**
 * 
 * @author Chen
 *
 */
public class Location {

	public Integer getLocationUniqueId() {
		return locationUniqueId;
	}

	public void setLocationUniqueId(Integer locationUniqueId) {
		this.locationUniqueId = locationUniqueId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return name;
	}

	private Integer locationUniqueId = 0;
	private String category;
	private String name;
	private String code;
}
