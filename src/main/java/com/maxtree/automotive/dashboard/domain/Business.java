package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * 
 * @author Chen
 *
 */
public class Business {

	public Integer getBusinessUniqueId() {
		return businessUniqueId;
	}

	public void setBusinessUniqueId(Integer businessUniqueId) {
		this.businessUniqueId = businessUniqueId;
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

	public Integer getNeedToCheck() {
		return needToCheck;
	}

	public void setNeedToCheck(Integer needToCheck) {
		this.needToCheck = needToCheck;
	}

	public String getCheckLevel() {
		return checkLevel;
	}

	public void setCheckLevel(String checkLevel) {
		this.checkLevel = checkLevel;
	}

	public List<DataDictionary> getItems() {
		return items;
	}

	public void setItems(List<DataDictionary> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return name + "(" + (needToCheck == 0 ? "不需要审档" : "需要审档") + ")";
	}

	private Integer businessUniqueId = 0;
	private String name; // 业务类型名称
	private String code; // 快捷编码
	private Integer needToCheck = 0; // 是否需要审档
	private String checkLevel;// 审档级别（一级/二级）
	private List<DataDictionary> items;
}
