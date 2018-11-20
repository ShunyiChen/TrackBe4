package com.maxtree.automotive.dashboard.servlet;

import java.io.InputStream;

/**
 * 上传出参
 * 
 * @author chens
 *
 */
public class UploadOutDTO {

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

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public int getRemovable() {
		return removable;
	}

	public void setRemovable(int removable) {
		this.removable = removable;
	}

	public InputStream thumbnail;
	private String dictionaryCode;// 字典CODE
	private int documentUniqueId; // 文档ID
	private int userUniqueId; // 用户ID
	private String fileFullPath; // 文件全路径
	private int removable = 0; // 是否可以从缓存清除
}
