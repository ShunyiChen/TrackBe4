package com.maxtree.automotive.dashboard;

/**
 * 
 * @author Chen
 *
 */
public enum BusinessState {

	TYPEIN("待录入"), PUTAWAY("待上架"), STORAGE("已入库"), APPROVAL("待审核"), REVIEW("待复审"), FILLED("已归档"), QUALITY("待质检");

	private BusinessState(String name) {
		this.name = name;
	}

	public String name;
}
