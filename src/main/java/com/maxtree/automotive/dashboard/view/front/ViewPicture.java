package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.ui.Image;

public class ViewPicture {

	/**
	 * 
	 * @param fileName
	 * @param picture
	 */
	public ViewPicture(String fileName, Image picture) {
		this.fileName = fileName;
		this.picture = picture;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Image getPicture() {
		return picture;
	}
	public void setPicture(Image picture) {
		this.picture = picture;
	}

	private String fileName;
	private Image picture;
}
