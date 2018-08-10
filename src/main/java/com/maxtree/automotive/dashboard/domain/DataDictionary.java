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
	
	@Override
	public String toString() {
		return itemName;
	}
	
	private Integer dictionaryUniqueId; // 数据字典id
	private Integer itemType;// 1-号牌种类 ；2-地区；3-材料名称
	private String itemName;//数据项名称
	private String code;//7位代码
}
