package com.maxtree.automotive.dashboard.domain;

import java.util.List;

import org.springframework.util.StringUtils;

/**
 * 业务类型
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
	
	public Boolean getUpdatePlateNumber() {
		return updatePlateNumber;
	}

	public void setUpdatePlateNumber(Boolean updatePlateNumber) {
		this.updatePlateNumber = updatePlateNumber;
	}

	public Boolean getUploadPicture() {
		return uploadPicture;
	}

	public void setUploadPicture(Boolean uploadPicture) {
		this.uploadPicture = uploadPicture;
	}
	
	public List<DataDictionary> getRequiredItems() {
		return requiredItems;
	}

	public void setRequiredItems(List<DataDictionary> requiredItems) {
		this.requiredItems = requiredItems;
	}

	public List<DataDictionary> getOptionalItems() {
		return optionalItems;
	}

	public void setOptionalItems(List<DataDictionary> optionalItems) {
		this.optionalItems = optionalItems;
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
	private String checkLevel;// ""/一级审档/二级审档,例如“”表示不需要审档；一级审档标识本机构内部审档；二级审档指同社区内车管所审档
	private Boolean updatePlateNumber = false; //上架后更新号码号牌
	private Boolean uploadPicture = false;//上架后更新车辆照片
	private List<DataDictionary> requiredItems; //必录材料名(非数据库字段)
	private List<DataDictionary> optionalItems; //选录材料名(非数据库字段)
}
