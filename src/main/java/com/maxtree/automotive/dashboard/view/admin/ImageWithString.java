package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.Image;

/**
 * 
 * @author Chen
 *
 */
public class ImageWithString {

	/**
	 * 
	 * @param image
	 * @param name
	 */
	public ImageWithString(Image image, String name) {
		this.image = image;
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Image image;
	private String name;
}
