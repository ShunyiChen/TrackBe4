package com.maxtree.automotive.dashboard.domain;

/**
 * 密集架
 * 
 * 一个密集架包含多个档案盒FileBox
 * 
 * @author chens
 *
 */
public class DenseFrame {

	public Integer getDenseFrameUniqueId() {
		return denseFrameUniqueId;
	}

	public void setDenseFrameUniqueId(Integer denseFrameUniqueId) {
		this.denseFrameUniqueId = denseFrameUniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxCol() {
		return maxCol;
	}

	public void setMaxCol(Integer maxCol) {
		this.maxCol = maxCol;
	}

	public Integer getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(Integer maxRow) {
		this.maxRow = maxRow;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStorehouseCode() {
		return storehouseCode;
	}

	public void setStorehouseCode(String storehouseCode) {
		this.storehouseCode = storehouseCode;
	}

	@Override
	public String toString() {
		return String.format(
				"DenseFrame[denseFrameUniquedId=%d,name=%s,maxCol=%d,maxRow=%d,serialNumber='%d',code='%s',storehouseCode='%s']",
				denseFrameUniqueId, name, maxCol, maxRow, serialNumber, code, storehouseCode);
	}

	private Integer denseFrameUniqueId = 0;
	private String name;//密集架名
	private Integer maxCol = 0;//最大列数
	private Integer maxRow = 0;//最大行数
	private Integer serialNumber = 0;//顺序号
	private String code;//UUID,密集架CODE
	private String storehouseCode;//UUID,库房CODE
}
