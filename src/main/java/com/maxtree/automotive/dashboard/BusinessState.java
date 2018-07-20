package com.maxtree.automotive.dashboard;

/**
 * 
 * @author Chen
 *
 */
public enum BusinessState {

	B1("待录入"),
	B2("待上架"),
	B3("已入库"),
	B4("待审核"),
	B5("待复审"),
	B6("已归档"),
	B7("待质检"),
	B8("待查看"),//影像化检测
	B9("待提档"),//影像化检测
	B10("待归档"),//影像化检测
	B11("完成");//影像化检测
	private BusinessState(String name) {
		this.name = name;
	}

	public String name;
}
