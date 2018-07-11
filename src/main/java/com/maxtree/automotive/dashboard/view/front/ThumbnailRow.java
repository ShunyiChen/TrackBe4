package com.maxtree.automotive.dashboard.view.front;

import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class ThumbnailRow extends Panel {

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
		this.setHeight("130px");
		this.setCaption(materialName);
		this.addStyleName("ThumbnailRow-border");
		
//		Panel scroll = new Panel();
//		scroll.setCaption(null);
//		scroll.setSizeFull();
//		scroll.setContent(main);
		
		main.setSpacing(false);
		main.setMargin(false);
		main.setWidthUndefined();
		main.setHeight("100%");
//		this.addComponent(scroll);
		this.setContent(main);
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
		main.addComponent(thumbnail);
		main.setComponentAlignment(thumbnail, Alignment.MIDDLE_LEFT);
	}
	
	public DataDictionary getDataDictionary() {
		return dataDictionary;
	}

	public void setDataDictionary(DataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}



	private String materialName;
	private DataDictionary dataDictionary;
	private HorizontalLayout main = new HorizontalLayout();
	
}
