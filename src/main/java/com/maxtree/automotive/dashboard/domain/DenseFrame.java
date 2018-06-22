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

	public Integer getStorehouseUniqueId() {
		return storehouseUniqueId;
	}

	public void setStorehouseUniqueId(Integer storehouseUniqueId) {
		this.storehouseUniqueId = storehouseUniqueId;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return String.format(
				"DenseFrame[denseFrameUniquedId=%d, storehouseUniqueId=%d, rowCount=%d, columnCount=%d, code='%s']",
				denseFrameUniqueId, storehouseUniqueId, rowCount, columnCount, code);
	}

	private Integer denseFrameUniqueId = 0;
	private Integer storehouseUniqueId = 0;
	private Integer rowCount = 0;
	private Integer columnCount = 0;
	private String code; // 编号
}
