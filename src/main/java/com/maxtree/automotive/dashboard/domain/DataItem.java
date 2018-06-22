package com.maxtree.automotive.dashboard.domain;

public class DataItem {

	public Integer getDataItemUniqueId() {
		return dataItemUniqueId;
	}

	public void setDataItemUniqueId(Integer dataItemUniqueId) {
		this.dataItemUniqueId = dataItemUniqueId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public String toString() {
		return itemName;
	}
	
	private Integer dataItemUniqueId;
	private String categoryName;
	private String itemName;
}
