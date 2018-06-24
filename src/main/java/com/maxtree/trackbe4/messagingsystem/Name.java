package com.maxtree.trackbe4.messagingsystem;

public class Name {

	/**
	 * 
	 * @param uniqueId  接收方的ID
	 * @param type	1-社区 2-机构 3-用户
	 * @param name  接收方名称
	 * @param picture 接收方图标
	 */
	public Name(int uniqueId, int type, String name, String picture) {
		this.uniqueId = uniqueId;
		this.type = type;
		this.name = name;
		this.picture = picture;
	}
	
	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	private int uniqueId;
	private int type;
	private String name;
	private String picture;
	// 接收方类型
	public static int COMMUNITY = 1;
	public static int COMPANY = 2;
	public static int USER = 3;
}
