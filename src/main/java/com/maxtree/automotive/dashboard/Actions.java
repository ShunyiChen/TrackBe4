package com.maxtree.automotive.dashboard;

public enum Actions {

	INPUT("前台录入"),REJECTED("质检退回");
	
	private Actions(String name) {
		this.name = name;
	}
	
	public String name;
}
