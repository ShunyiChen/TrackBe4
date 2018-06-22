package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * 
 * @author chens
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

	public Integer getFileCheck() {
		return fileCheck;
	}

	public void setFileCheck(Integer fileCheck) {
		this.fileCheck = fileCheck;
	}

	public List<DataItem> getItems() {
		return items;
	}

	public void setItems(List<DataItem> items) {
		this.items = items;
	}

	public Integer getHasFirstIndex() {
		return hasFirstIndex;
	}

	public void setHasFirstIndex(Integer hasFirstIndex) {
		this.hasFirstIndex = hasFirstIndex;
	}
	
	public Integer getLocalCheck() {
		return localCheck;
	}

	public void setLocalCheck(Integer localCheck) {
		this.localCheck = localCheck;
	}

	@Override
	public String toString() {
		// return String.format(
		// "Business[businessUniqueId=%d, name='%s']", businessUniqueId, name);

		return name+"("+(fileCheck == 0 ? "不需要审档":"需要审档")+")";
	}
	
	private Integer businessUniqueId = 0;
	private String name;
	private Integer fileCheck = 0; // 0-不需要审档，1-需要审档
	private List<DataItem> items;
	private Integer hasFirstIndex = 0; // 0-不是第一个索引，1-是第一个索引
	private Integer localCheck = 0; // 0-不用本机构审档员，1-用本机构内审档员审档
}
