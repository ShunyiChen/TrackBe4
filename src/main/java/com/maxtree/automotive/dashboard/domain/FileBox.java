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

	public Integer getDenseFrameUniquedId() {
		return denseFrameUniquedId;
	}

	public void setDenseFrameUniquedId(Integer denseFrameUniquedId) {
		this.denseFrameUniquedId = denseFrameUniquedId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return String.format("FileBox[fileboxUniqueId=%d, denseFrameUniquedId=%d, code='%s', row=%d, col=%d]", fileboxUniqueId,
				denseFrameUniquedId, code, row, col);
	}

	private Integer fileboxUniqueId = 0;
	private Integer denseFrameUniquedId = 0;
	private String code; 		 // 编号
	private Integer row = 0;  	// 位于密集架行
	private Integer col = 0; 	// 位于密集架列
}
