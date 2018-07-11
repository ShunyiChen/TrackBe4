package com.maxtree.automotive.dashboard.domain;

public class UploadedFileQueue {

	public Integer getQueueUniqueId() {
		return queueUniqueId;
	}

	public void setQueueUniqueId(Integer queueUniqueId) {
		this.queueUniqueId = queueUniqueId;
	}

	public Integer getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(Integer userUniqueId) {
		this.userUniqueId = userUniqueId;
	}

	public String getDictionaryCode() {
		return dictionaryCode;
	}

	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}

	public Integer getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(Integer documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}

	public Integer getRemovable() {
		return removable;
	}

	public void setRemovable(Integer removable) {
		this.removable = removable;
	}

	private Integer queueUniqueId;
	private Integer userUniqueId;
	private String dictionaryCode;
	private Integer documentUniqueId;
	private Integer removable;
	public int location;
}
