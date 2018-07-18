package com.maxtree.automotive.dashboard.domain;

public class FrameNumber {

	public Integer getFrameUniqueId() {
		return frameUniqueId;
	}

	public void setFrameUniqueId(Integer frameUniqueId) {
		this.frameUniqueId = frameUniqueId;
	}

	public String getStorehouseName() {
		return storehouseName;
	}

	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}

	public Integer getFrameCode() {
		return frameCode;
	}

	public void setFrameCode(Integer frameCode) {
		this.frameCode = frameCode;
	}

	public Integer getMaxColumn() {
		return maxColumn;
	}

	public void setMaxColumn(Integer maxColumn) {
		this.maxColumn = maxColumn;
	}

	public Integer getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(Integer maxRow) {
		this.maxRow = maxRow;
	}

	public Integer getCellCode() {
		return cellCode;
	}

	public void setCellCode(Integer cellCode) {
		this.cellCode = cellCode;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCompanyUniqueId() {
		return companyUniqueId;
	}

	public void setCompanyUniqueId(Integer companyUniqueId) {
		this.companyUniqueId = companyUniqueId;
	}

	@Override
	public String toString() {
		return String.format(
				"FrameNumber[frameUniqueId=%d, storehouseName='%s', frameCode=%d,maxColumn=%d,maxRow=%d,cellCode=%d,col=%d,row=%d,vin='%s',code='%s',companyUniqueId=%d]",
				frameUniqueId, storehouseName, frameCode, maxColumn, maxRow, cellCode, col, row, vin, code, companyUniqueId);
	}

	private Integer frameUniqueId = 0;
	private String storehouseName;
	private Integer frameCode = 0;
	private Integer maxColumn = 0;
	private Integer maxRow = 0;
	private Integer cellCode = 0;
	private Integer col = 0;
	private Integer row = 0;
	private String vin;// 车辆识别代码
	private String code;// 上架号，例如014-002-003-001(密集架号-列-行-文件夹序号）
	private Integer companyUniqueId = 0;//机构ID
}
