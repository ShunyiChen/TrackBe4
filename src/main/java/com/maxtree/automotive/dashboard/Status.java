package com.maxtree.automotive.dashboard;

/**
 * 业务状态
 * 
 * @author chens
 *
 */
public enum Status {
	ReturnedToThePrint("审核不合格") // 审档不合格
	,S1("待质检")
	,S2("待审档")
	,S3("待补充")
	,S4("待上架")
	,S5("已归档");
	 
	private Status(String name) {
		this.name = name;
	}
	
	public String name;
}
