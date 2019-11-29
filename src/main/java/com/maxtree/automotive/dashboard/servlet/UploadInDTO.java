package com.maxtree.automotive.dashboard.servlet;

/**
 * 上传入参
 * 
 * @author chens
 *
 */
public class UploadInDTO {

	private int userUniqueId;
	private String vin;
	private int siteID;
	private String uuid;
	private String dictionaryCode;
	private int siteUniqueId;
	private Integer documentUniqueId;

	public UploadInDTO(int userUniqueId, String vin, int siteID, String uuid, String dictionaryCode, int siteUniqueId, Integer documentUniqueId) {
		this.userUniqueId = userUniqueId;
		this.vin = vin;
		this.siteID = siteID;
		this.uuid = uuid;
		this.dictionaryCode = dictionaryCode;
		this.siteUniqueId = siteUniqueId;
		this.documentUniqueId = documentUniqueId;
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

	public int getSiteUniqueId() {
		return siteUniqueId;
	}

	public void setSiteUniqueId(int siteUniqueId) {
		this.siteUniqueId = siteUniqueId;
	}

	public Integer getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(Integer documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}

	@Override
	public String toString() {
		return "UploadInDTO{" +
				"userUniqueId=" + userUniqueId +
				", vin='" + vin + '\'' +
				", siteID=" + siteID +
				", uuid='" + uuid + '\'' +
				", dictionaryCode='" + dictionaryCode + '\'' +
				", siteUniqueId=" + siteUniqueId +
				", documentUniqueId=" + documentUniqueId +
				'}';
	}
}
