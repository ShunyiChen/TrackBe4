package com.maxtree.automotive.dashboard.view.admin;

public class DataDictionaryType {
	
	/**
	 * 
	 * @param type
	 * @param name
	 */
	public DataDictionaryType(int type, String name) {
		this.type = type;
		this.name = name;
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
	
	@Override
	public String toString() {
		return name;
	}
	
	private int type;
	private String name;

}
