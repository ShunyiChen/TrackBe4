package com.maxtree.automotive.dashboard.view.user.frontdesk;

import java.util.Iterator;

//import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
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
		this(materialName, true);
	}
	
	/**
	 * 
	 * @param materialName 材料名称
	 * @param required   是必录项
	 */
	public ThumbnailRow(String materialName, boolean required) {
		this.materialName = materialName;
		this.required = required;
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
		if(!required) {
			this.addStyleName("ThumbnailRow-BGColor-"+this.hashCode());
			Styles styles = Page.getCurrent().getStyles();
	    	String css1 = ".ThumbnailRow-BGColor-"+this.hashCode()+" { background-color: rgb(195,195,195) !important; }";
	    	styles.add(css1);
		}
		main.setSpacing(false);
		main.setMargin(false);
		main.setWidthUndefined();
		main.setHeight("100%");
		this.setContent(main);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void addActionListener(ClickListener listener) {
		this.addClickListener(listener);
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
	
	/**
	 * 
	 * @param thumbnail
	 */
	public void removeThumbnail(Thumbnail thumbnail) {
		main.removeComponent(thumbnail);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasThumbnailUploaded() {
		Iterator<Component> iter = main.iterator();
		while(iter.hasNext()) {
			Component com = iter.next();
			if(com instanceof Thumbnail) {
				return true;
			}
		}
		return false;
	}
	
//	/**
//	 *
//	 * @return
//	 */
//	public DataDictionary getDataDictionary() {
//		return dataDictionary;
//	}
//
//	/**
//	 *
//	 * @param dataDictionary
//	 */
//	public void setDataDictionary(DataDictionary dataDictionary) {
//		this.dataDictionary = dataDictionary;
//	}
//
	/**
	 * 是否是必录项
	 * 
	 * @return
	 */
	public boolean isRequired() {
		return required;
	}

	private String materialName;
//	private DataDictionary dataDictionary;
	private HorizontalLayout main = new HorizontalLayout();
	private boolean required;
}
