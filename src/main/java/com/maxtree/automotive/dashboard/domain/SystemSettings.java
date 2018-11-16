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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	private Integer settingUniqueId = 0;
	private String name;
	private String value;
	private String comments;
}
