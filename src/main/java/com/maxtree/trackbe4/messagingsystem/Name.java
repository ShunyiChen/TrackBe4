package com.maxtree.trackbe4.messagingsystem;

public class Name {

	/**
	 * 
	 * @param uniqueId
	 * @param type
	 * @param name
	 * @param picture
	 */
	public Name(int uniqueId, String type, String name, String picture) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
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
	private String type;
	private String name;
	private String picture;
}
