package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.component.Hr;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class ThumbnailGrid extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param isPrimary
	 */
	public ThumbnailGrid(String caption, int height) {
		this.height = height;
		this.setCaption(caption);
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		this.addStyleName("picture-pane");
		this.setWidth("100%");
		this.setHeight(height+"px");
		vLayout.setMargin(false);
		vLayout.setSpacing(true);
		vLayout.setWidth("100%");
		vLayout.setHeightUndefined();
    	setContent(vLayout);
	}
	
	/**
	 * 
	 * @param lstRow
	 */
	public void addRow(ThumbnailRow row) {
		Hr hr = new Hr();
		vLayout.addComponents(row, hr);
		vLayout.setComponentAlignment(row, Alignment.TOP_LEFT);
		vLayout.setComponentAlignment(hr, Alignment.TOP_LEFT);
	}
	
	private static final Logger log = LoggerFactory.getLogger(ThumbnailGrid.class);
	private VerticalLayout vLayout = new VerticalLayout();
	private int height;
}
