package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 影响化记录
 * 
 * @author chens
 *
 */
public class Imaging {

	public Integer getImagingUniqueId() {
		return imagingUniqueId;
	}

	public void setImagingUniqueId(Integer imagingUniqueId) {
		this.imagingUniqueId = imagingUniqueId;
	}

	public String getPlateType() {
		return plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return String.format(
				"Imaging[imagingUniqueId=%d, plateType='%s', plateNumber='%s',vin='%s', "
						+ "dateCreated='%s', dateModified='%s', status='%s',creator='%s']",
				imagingUniqueId, plateType, plateNumber, vin, dateCreated, dateModified, status, creator);
	}
	
	private Integer imagingUniqueId = 0; // 自增ID
	private String plateType; // 号牌种类
	private String plateNumber; // 号码号牌
	private String vin; // 车辆识别代码
	private Date dateCreated; // 创建日期
	private Date dateModified; // 最后修改日期
	private String status; // 业务状态（待查看，待提档，待归档，完成）
	private String creator; // 录入人用户名
}
