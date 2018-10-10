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

	public String getK() {
		return K;
	}

	public void setK(String k) {
		K = k;
	}

	public String getV() {
		return V;
	}

	public void setV(String v) {
		V = v;
	}

	private Integer settingUniqueId = 0;
	private String K;
	private String V;
}
