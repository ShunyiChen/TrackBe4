package com.maxtree.automotive.dashboard.domain;

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

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
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
	
	private Integer documentUniqueId = 0; // 文档ID
	private String uuid; // 文件UUID
	private String businessCode; // 业务CODE
	private String dictionarycode; // 资料CODE
	private String fileFullPath; // 文件实际存放全路径
	public String vin;			// 车辆识别代码，用于获取表索引（非数据表字段）
	public int location = 1;    // 存储位置1:主文档表2:次文档表
}
