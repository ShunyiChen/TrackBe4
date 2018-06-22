package com.maxtree.automotive.dashboard.domain;

public class Document {

	public Document() {
	}

	public Document(String alias) {
		this.alias = alias;
	}

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

	public Integer getBusinessUniqueId() {
		return businessUniqueId;
	}

	public void setBusinessUniqueId(Integer businessUniqueId) {
		this.businessUniqueId = businessUniqueId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	@Override
	public String toString() {
		// return String.format("Transaction[documentUniqueId=%d,
		// transactionUniqueId='%s', name='%s', path='%s',]",
		// documentUniqueId, transactionUniqueId, name, path);
		return alias;
	}

	private Integer documentUniqueId = 0; // 文档ID
	private String uuid;                  // 文件UUID
	private Integer businessUniqueId = 0; // 业务类型ID
	private String alias; 	 			// 别名,例如：身份证，托银莫
	private String fileName; 			// 文件名
	private String fileFullPath; 		// 文件实际存放全路径
	private Integer category = 0; 		// 0:主要材料  1:次要材料
}
