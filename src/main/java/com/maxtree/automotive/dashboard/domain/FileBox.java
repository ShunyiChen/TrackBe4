package com.maxtree.automotive.dashboard.domain;

/**
 * 档案盒
 * 
 * 一个档案盒包含多个档案袋Portfolio
 * 
 * @author chens
 *
 */
public class FileBox {

	public Integer getFileboxUniqueId() {
		return fileboxUniqueId;
	}

	public void setFileboxUniqueId(Integer fileboxUniqueId) {
		this.fileboxUniqueId = fileboxUniqueId;
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
	
	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getDenseframeSN() {
		return denseframeSN;
	}

	public void setDenseframeSN(Integer denseframeSN) {
		this.denseframeSN = denseframeSN;
	}

	public Integer getStorehouseSN() {
		return storehouseSN;
	}

	public void setStorehouseSN(Integer storehouseSN) {
		this.storehouseSN = storehouseSN;
	}

	@Override
	public String toString() {
		return String.format("FileBox[fileboxUniqueId=%d, col=%d,row=%d, serialNumber=%d,denseframeSN=%d，storehouseSN=%d]", fileboxUniqueId, col, row,
				serialNumber,denseframeSN,storehouseSN);
	}

	private Integer fileboxUniqueId = 0;
	private Integer col = 0; // 位于密集架列
	private Integer row = 0; // 位于密集架行
	private Integer serialNumber = 0;
	private Integer denseframeSN = 0;// 密集架顺序号
	private Integer storehouseSN = 0;// 库房顺序号
}
