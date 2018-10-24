package com.maxtree.automotive.dashboard.domain;

/**
 * 系统设置参数
 * 
 * @author chens
 *
 */
public class SystemSettings {

	public Integer getSettingUniqueId() {
		return settingUniqueId;
	}

	public void setSettingUniqueId(Integer settingUniqueId) {
		this.settingUniqueId = settingUniqueId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSettings() {
		return itemSettings;
	}

	public void setItemSettings(String itemSettings) {
		this.itemSettings = itemSettings;
	}

	private Integer settingUniqueId = 0;
	private String itemName;
	private String itemSettings;
}
