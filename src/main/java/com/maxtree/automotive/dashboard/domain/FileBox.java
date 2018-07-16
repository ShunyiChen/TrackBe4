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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDenseframeCode() {
		return denseframeCode;
	}

	public void setDenseframeCode(String denseframeCode) {
		this.denseframeCode = denseframeCode;
	}

	@Override
	public String toString() {
		return String.format("FileBox[fileboxUniqueId=%d, col=%d,row=%d, code='%s',denseframeCode='%s']",
				fileboxUniqueId, col, row, code, denseframeCode);
	}

	private Integer fileboxUniqueId = 0;
	private Integer col = 0; // 位于密集架列
	private Integer row = 0; // 位于密集架行
	private String code;// 单元格CODE
	private String denseframeCode;// 密集架CODE
}
