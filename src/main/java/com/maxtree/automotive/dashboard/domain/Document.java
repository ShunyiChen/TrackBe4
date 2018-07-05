package com.maxtree.automotive.dashboard.domain;

public class Document {

	/**
	 * Constructor without any parameters
	 */
	public Document() {
	}

	/**
	 * Constructor with alias
	 * 
	 * @param alias
	 */
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

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
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

	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		// return String.format("Transaction[documentUniqueId=%d,
		// transactionUniqueId='%s', name='%s', path='%s',]",
		// documentUniqueId, transactionUniqueId, name, path);
		return alias;
	}

	private Integer documentUniqueId = 0; 	// 文档ID
	private String uuid;                  	// 文件UUID
	private String businessCode;		    // 业务code
	private String fileFullPath; 			// 文件实际存放全路径
	private Integer category = 1; 			// 1:主要材料  2:次要材料(非数据库字段)
	private Integer batch = 0;          	// 批次号（非数据库字段）
	private String vin;                     // 车辆识别代码（非数据库字段）
	private String alias;                   // 材料别名
}
