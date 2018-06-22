package com.maxtree.automotive.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 库房
 * 
 * 一个库房包含多个密集架DenseFrame
 * 
 * @author chens
 *
 */
public class Storehouse {

	public Integer getStorehouseUniqueId() {
		return storehouseUniqueId;
	}

	public void setStorehouseUniqueId(Integer storehouseUniqueId) {
		this.storehouseUniqueId = storehouseUniqueId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<DenseFrame> getDenseFrameList() {
		return denseFrameList;
	}

	public void setDenseFrameList(List<DenseFrame> denseFrameList) {
		this.denseFrameList = denseFrameList;
	}

//	@Override
//	public String toString() {
//		return String.format("Storehouse[storehouseUniqueId=%d, name='%s', description='%s', status=%d, code='%s']", storehouseUniqueId, name, description, status, code);
//	}

	private Integer storehouseUniqueId = 0;
	private String code;
	private List<DenseFrame> denseFrameList = new ArrayList<>();
}
