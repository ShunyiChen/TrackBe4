package com.maxtree.automotive.dashboard.servlet;

import com.maxtree.automotive.dashboard.domain.Site;

/**
 * 上传入参
 * 
 * @author chens
 *
 */
public class UploadInDTO {

	/**
	 * 
	 * @param userUniqueId
	 * @param vin
	 * @param batch
	 * @param siteID
	 * @param uuid
	 * @param dictionaryCode
	 */
	public UploadInDTO(int userUniqueId,String vin,String batch,int siteID,String uuid,String dictionaryCode) {
		this.userUniqueId = userUniqueId;
		this.vin = vin;
		this.batch = batch;
		this.siteID = siteID;
		this.uuid = uuid;
		this.dictionaryCode = dictionaryCode;
	}
	
	public UploadInDTO() {}
	
	public int getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(int userUniqueId) {
		this.userUniqueId = userUniqueId;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDictionaryCode() {
		return dictionaryCode;
	}

	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}
	
	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(Integer documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}

	@Override
	public String toString() {
		return String.format("UploadParameters[userUniqueId=%d,vin='%s',batch='%s',siteID=%d, uuid='%s',dictionaryCode='%s',siteCode='%s']",
				userUniqueId,vin,batch,siteID,uuid,dictionaryCode,siteCode);
	}

	private int userUniqueId;
	private String vin;
	private String batch;
	private int siteID;
	private String uuid;
	private String dictionaryCode;
	private String siteCode;
	private Integer documentUniqueId = null;
}
