package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 
 * @author chens
 *
 */
public class DocumentHistory {

	public Integer getHistoryUniqueId() {
		return historyUniqueId;
	}

	public void setHistoryUniqueId(Integer historyUniqueId) {
		this.historyUniqueId = historyUniqueId;
	}

	public Integer getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(Integer documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}
	
	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDictionarycode() {
		return dictionarycode;
	}

	public void setDictionarycode(String dictionarycode) {
		this.dictionarycode = dictionarycode;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return String.format(
				"DocumentHistory[historyUniqueId=%d, tableId=%d,,documentUniqueId=%d, uuid='%s',dictionarycode='%s', "
						+ "fileFullPath='%s', fileFullPath='%s', dateCreated='%s']",
						historyUniqueId,tableId,documentUniqueId,uuid,dictionarycode,fileFullPath,userName,dateCreated);
	}
	
	private Integer historyUniqueId = 0;
	private Integer tableId = 0;
	private Integer documentUniqueId = 0;
	private String uuid;
	private String dictionarycode; // 资料CODE
	private String fileFullPath; // 文件实际存放全路径
	private byte[] thumbnail;// 缩略图
	private String userName;
	private Date dateCreated;
}
