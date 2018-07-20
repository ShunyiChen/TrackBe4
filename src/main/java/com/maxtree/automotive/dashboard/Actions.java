package com.maxtree.automotive.dashboard;

public enum Actions {

	INPUT("前台录入");
	
	private Actions(String name) {
		this.name = name;
	}
	
	public String name;
}
