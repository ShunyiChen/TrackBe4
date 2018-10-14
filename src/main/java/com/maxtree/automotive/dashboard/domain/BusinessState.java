package com.maxtree.automotive.dashboard.domain;

/**
 * 业务状态
 * 
 * @author Chen
 *
 */
public class BusinessState {

	public Integer getStateUniqueId() {
		return stateUniqueId;
	}

	public void setStateUniqueId(Integer stateUniqueId) {
		this.stateUniqueId = stateUniqueId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Integer stateUniqueId = 0;
	private String code;
	private String name;
}
