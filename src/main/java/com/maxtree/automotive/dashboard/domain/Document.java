package com.maxtree.automotive.dashboard.domain;

import java.io.FileInputStream;
import java.io.InputStream;

public class Document {

	public Integer getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(Integer documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
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

	public InputStream getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(InputStream thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	
	private Integer documentUniqueId = 0; // 文档ID
	private String uuid; // 文件UUID
	private String dictionarycode; // 资料CODE
	private String fileFullPath; // 文件实际存放全路径
	private InputStream thumbnail;//缩略图
	public String vin;			// 车辆识别代码，用于获取表索引（非数据表字段）
	public int location = 1;    // 存储位置1:主文档表2:次文档表（非数据表字段）
}
