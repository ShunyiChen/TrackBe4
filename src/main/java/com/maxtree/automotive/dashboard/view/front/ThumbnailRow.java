package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.ui.HorizontalLayout;

public class ThumbnailRow extends HorizontalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param materialName
	 */
	public ThumbnailRow(String materialName) {
		this.materialName = materialName;
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		setMargin(false);
		setSpacing(false);
		setWidthUndefined();
		this.setHeight("120px");
		this.setCaption(materialName);
	}
	
	/**
	 * 
	 * @param thumbnail
	 */
	public void addThumbnail(Thumbnail thumbnail) {
		this.addComponent(thumbnail);
	}
	
	private String materialName;
}
