package com.maxtree.automotive.dashboard.view.admin;

/**
 * 
 * @author Chen
 *
 */
public class GridColumn {
	
	/**
	 * 
	 * @param name
	 * @param width
	 */
	public GridColumn(String name, int width) {
		this.name = name;
		this.width = width;
	}
	
	/**
	 * 
	 * @param name
	 */
	public GridColumn(String name) {
		this(name, 200);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	private String name = "";
	private int width = 200;
}
