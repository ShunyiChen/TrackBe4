package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

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

	public Integer getSiteUniqueId() {
		return siteUniqueId;
	}

	public void setSiteUniqueId(Integer siteUniqueId) {
		this.siteUniqueId = siteUniqueId;
	}

	public Integer getBusinessUniqueId() {
		return businessUniqueId;
	}

	public void setBusinessUniqueId(Integer businessUniqueId) {
		this.businessUniqueId = businessUniqueId;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPrefecture() {
		return prefecture;
	}

	public void setPrefecture(String prefecture) {
		this.prefecture = prefecture;
	}
	
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
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
	
	public Integer getTypist() {
		return typist;
	}

	public void setTypist(Integer typist) {
		this.typist = typist;
	}
	
	public Integer getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
	}

	@Override
	public String toString() {
		 return String.format(
		 "Transaction[transactionUniqueId=%d, barcode='%s', plateType='%s',plateNumber='%s',vin='%s', "
		 + "dateCreated='%s', dateModified='%s', dateFinished='%s', status='%s',siteUniqueId='%d',"
		 + "businessUniqueId='%d', communityUniqueID='%d',companyUniqueId='%d',province='%s',city='%s'"
		 + ",prefecture='%s',district='%s',uuid='%s',code='%s',typist='%d',indexNumber='%d']",
		 transactionUniqueId,
		 barcode,
		 plateType,
		 plateNumber,
		 vin,
		 dateCreated,
		 dateModified,
		 dateFinished,
		 status,
		 siteUniqueId,
		 businessUniqueId,
		 communityUniqueId,
		 companyUniqueId,
		 province,
		 city,
		 prefecture,
		 district,
		 uuid,
		 code,
		 typist,
		 indexNumber
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
	private String status; 					// 业务状态
	private Integer siteUniqueId = 0; 		// 站点ID
	private Integer businessUniqueId = 0;   // 业务ID
	private Integer communityUniqueId = 0; 	// 办理社区ID
	private Integer companyUniqueId = 0;    // 办理机构ID
	private String province;   				// 车辆所在省
	private String city;       				// 车辆所在市
	private String prefecture; 				// 车辆所在县
	private String district;				// 车辆所在区
	private String uuid;					// 文件挂接UUID
	private String code;                 	// 上架号
	private Site site;						// 文件站点
	private Integer typist = 0;				// 录入员
	private Integer indexNumber = 0;		// 业务顺序号
}
