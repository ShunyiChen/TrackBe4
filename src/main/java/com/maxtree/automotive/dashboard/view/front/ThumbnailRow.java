package com.maxtree.automotive.dashboard.view.front;

import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.vaadin.ui.HorizontalLayout;

public class ThumbnailRow extends HorizontalLayout {

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
		this.setWidth("100%");
		this.setHeight("120px");
		this.setCaption(materialName);
		this.addStyleName("ThumbnailRow-border");
	}
	
	/**
	 * 
	 */
	public void selected() {
		this.removeStyleName("ThumbnailRow-border");
		this.addStyleName("ThumbnailRow-border-selected");
	}
	
	/**
	 * 
	 */
	public void deselected() {
		this.removeStyleName("ThumbnailRow-border-selected");
		this.addStyleName("ThumbnailRow-border");
	}
	
	/**
	 * 
	 * @param thumbnail
	 */
	public void addThumbnail(Thumbnail thumbnail) {
		this.addComponent(thumbnail);
	}
	
	public DataDictionary getDataDictionary() {
		return dataDictionary;
	}

	public void setDataDictionary(DataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}



	private String materialName;
	private DataDictionary dataDictionary;
}
