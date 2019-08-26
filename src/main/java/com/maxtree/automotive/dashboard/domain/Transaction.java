package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 
 * 主要业务
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getDomicilePlace() {
		return domicilePlace;
	}

	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}

	public Integer getSiteUniqueId() {
		return siteUniqueId;
	}

	public void setSiteUniqueId(Integer siteUniqueId) {
		this.siteUniqueId = siteUniqueId;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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

	@Override
	public String toString() {
		 return String.format(
		 "Transaction[transactionUniqueId=%d, barcode='%s', plateType='%s',plateNumber='%s',vin='%s', "
		 +"'status='%s',businessName='%s',code='%s',indexNumber='%d',domicilePlace='%s',siteUniqueId='%d', communityUniqueID='%d',companyUniqueId='%d',"
		 + "createBy='%s', dateCreated='%s',dateModified='%s']",
		 transactionUniqueId,
		 barcode,
		 plateType,
		 plateNumber,
		 vin,
		 status,
		 businessName,
		 code,
		 indexNumber,
  		 domicilePlace,
		 siteUniqueId,
		 communityUniqueId,
		 companyUniqueId,
		 createBy,
		 dateCreated,
		 dateModified
		);
	}

	private Integer transactionUniqueId = 0;// ID
	private String barcode; 				// 条形码,即业务流水号
	private String plateType; 				// 号牌种类
	private String plateNumber; 			// 号码号牌
	private String vin; 					// 车辆识别代码
	private String status; 					// 业务状态,比如，待上架，待质检
	private String businessName;			// 业务名称
	private String code;                 	// 上架号
	private Integer indexNumber = 0; 		// 业务顺序号（记录先后顺序的）
	private String domicilePlace;			// 户籍所在地（大连市,）
	private Integer siteUniqueId; 			// 站点Id
	private Integer communityUniqueId = 0; 	// 办理社区ID
	private Integer companyUniqueId = 0;    // 办理机构ID
	private String createBy;			    // 录入人用户名
	private Date dateCreated; 				// 创建日期
	private Date dateModified; 				// 最后修改日期
}
