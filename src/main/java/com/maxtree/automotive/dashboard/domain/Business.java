package com.maxtree.automotive.dashboard.domain;

import java.util.List;

import org.springframework.util.StringUtils;

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
		if (StringUtils.isEmpty(checkLevel)) {
			return name + "(非审档)";
		}
		return name + "(" + checkLevel+ ")";
	}

	private Integer businessUniqueId = 0;
	private String name; // 业务类型名称
	private String code; // 4位代码
	private String checkLevel;// ""/一级审档/二级审档,例如“”表示不需要审档；一级审档标识本机构内部审档；二级审档指车管所审档
	private List<DataDictionary> items;
}
