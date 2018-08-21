package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

import com.maxtree.automotive.dashboard.BusinessCode;

/**
 * 
 * 事务
 * 
 * @author Chen
 *
 */
public class Transaction {

	public Integer getTransactionUniqueId() {
		return transactionUniqueId;
	}

	public void setTransactionUniqueId(Integer transactionUniqueId) {
		this.transactionUniqueId = transactionUniqueId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public Date getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(Date dateFinished) {
		this.dateFinished = dateFinished;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Integer getCommunityUniqueId() {
		return communityUniqueId;
	}

	public void setCommunityUniqueId(Integer communityUniqueId) {
		this.communityUniqueId = communityUniqueId;
	}

	public Integer getCompanyUniqueId() {
		return companyUniqueId;
	}

	public void setCompanyUniqueId(Integer companyUniqueId) {
		this.companyUniqueId = companyUniqueId;
	}

	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getBusinessName() {
		return BusinessCode.get(businessCode);
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Override
	public String toString() {
		 return String.format(
		 "Transaction[transactionUniqueId=%d, barcode='%s', plateType='%s',plateNumber='%s',vin='%s', "
		 + "dateCreated='%s', dateModified='%s', dateFinished='%s', status='%s',siteCode='%s',"
		 + "businessCode='%s', communityUniqueID='%d',companyUniqueId='%d',locationCode='%s',"
		 + "batch='%s', uuid='%s',code='%s',creator='%s',indexNumber='%d',businessName='%s']",
		 transactionUniqueId,
		 barcode,
		 plateType,
		 plateNumber,
		 vin,
		 dateCreated,
		 dateModified,
		 dateFinished,
		 status,
		 siteCode,
		 businessCode,
		 communityUniqueId,
		 companyUniqueId,
		 locationCode,
		 batch,
		 uuid,
		 code,
		 creator,
		 indexNumber,
		 businessName
		);
	}

	private Integer transactionUniqueId = 0;// ID
	private String barcode; 				// 条形码,即业务流水号
	private String plateType; 				// 号牌种类
	private String plateNumber; 			// 号码号牌
	private String vin; 					// 车辆识别代码
	private Date dateCreated; 				// 创建日期
	private Date dateModified; 				// 最后修改日期
	private Date dateFinished; 				// 完成日期
	private String status; 					// 业务状态,比如，待上架，待质检
	private String siteCode; 				// 站点CODE
	private String businessCode;   			// 业务CODE
	private Integer communityUniqueId = 0; 	// 办理社区ID
	private Integer companyUniqueId = 0;    // 办理机构ID
	private String locationCode;   			// 车辆所在地地点CODE
	private Integer batch = 0;					// 批次号
	private String uuid;					// 文件挂接UUID
	private String code;                 	// 上架号
	private Site site;						// 文件站点
	private String creator;					// 录入人用户名
	private Integer indexNumber = 0; 		// 业务顺序号
	private String businessName;			// 业务名称（非数据库字段）
}
