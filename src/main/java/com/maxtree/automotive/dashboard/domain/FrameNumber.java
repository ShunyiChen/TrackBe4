package com.maxtree.automotive.dashboard.domain;

/**
 * 库房/密集架/单元格/文件盒
 * 
 * @author chens
 *
 */
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
	
	public Integer getMaxfolder() {
		return maxfolder;
	}

	public void setMaxfolder(Integer maxfolder) {
		this.maxfolder = maxfolder;
	}

	@Override
	public String toString() {
		return storehouseName;
//		return String.format(
//				"FrameNumber[frameUniqueId=%d, storehouseName='%s', frameCode=%d,maxColumn=%d,maxRow=%d,cellCode=%d,col=%d,row=%d,vin='%s',code='%s']",
//				frameUniqueId, storehouseName, frameCode, maxColumn, maxRow, cellCode, col, row, vin, code);
	}

	private Integer frameUniqueId = 0;
	private String storehouseName;
	private Integer frameCode = 0;
	private Integer maxColumn = 0;
	private Integer maxRow = 0;
	private Integer cellCode = 0;
	private Integer col = 0;
	private Integer row = 0;
	private String vin;		// 车辆识别代码
	private String code;	// 上架号，例如014-002-003-001(密集架号-列-行-文件夹序号）
	private Integer maxfolder = 0;//单元格内最大文件夹数
}
