package com.maxtree.automotive.dashboard.domain;

/**
 * 数据字典
 * 
 * @author chens
 *
 */
public class DataDictionary {
	
	public Integer getDictionaryUniqueId() {
		return dictionaryUniqueId;
	}

	public void setDictionaryUniqueId(Integer dictionaryUniqueId) {
		this.dictionaryUniqueId = dictionaryUniqueId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Float getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Float orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return itemName;
	}
	
	private Integer dictionaryUniqueId; // 数据字典id
	private Integer itemType;//数据项类别 1-号牌种类 2-材料名称
	private String itemName;//数据项名称
	private String code;//dictionary快捷编码
	private Float orderNumber = 0f;//顺序号
}
