package com.maxtree.automotive.dashboard.servlet;

import java.io.InputStream;

/**
 * 上传出参
 * 
 * @author chens
 *
 */
public class UploadOutDTO {
	
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getDictionaryCode() {
		return dictionaryCode;
	}
	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}
	public int getDocumentUniqueId() {
		return documentUniqueId;
	}
	public void setDocumentUniqueId(int documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}
	public int getUserUniqueId() {
		return userUniqueId;
	}
	public void setUserUniqueId(int userUniqueId) {
		this.userUniqueId = userUniqueId;
	}
	public int getRemovable() {
		return removable;
	}
	public void setRemovable(int removable) {
		this.removable = removable;
	}
	
	public int location;
	public InputStream thumbnail;
	private String dictionaryCode;
	private int documentUniqueId;
	private int userUniqueId;
	private int removable = 0;
}
