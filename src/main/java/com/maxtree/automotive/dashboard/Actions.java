package com.maxtree.automotive.dashboard;

public enum Actions {

	INPUT("前台录入"),
	REJECTED("退回前台"),
	APPROVED("质检合格"),
	SUBMIT("提交审档"),
	SUBMIT2("提交确认审档"),
	VERIFIED("审档通过"),
	CONFIRMED("确认审档通过"),
	PUTAWAY("上架"),
	REMOVEOFF("下架");
	
	private Actions(String name) {
		this.name = name;
	}
	
	public String name;
}
