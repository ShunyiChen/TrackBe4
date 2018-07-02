package com.maxtree.automotive.dashboard.view.front;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class HighShootGrid extends Panel {

	private static final Logger log = LoggerFactory.getLogger(HighShootGrid.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param isPrimary
	 */
	public HighShootGrid(String caption) {
		this.setCaption(caption);
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("picture-pane");
		this.setWidth("100%");
    	hLayout.setMargin(false);
    	hLayout.setSpacing(true);
    	hLayout.setWidthUndefined();
    	hLayout.setHeight("100%");
    	setContent(hLayout);
	}
	
//	public void enableRightClickMenu() {
//		com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(this, true);
//		menu.addItem("选择文件", new com.vaadin.contextmenu.Menu.Command() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
//			}
//		});
//	}
	
	public void addUploadCells(Site site, Document... documents) {
		hLayout.removeAllComponents();
		for (Document doc : documents) {
			HighShootGridCell cell = new HighShootGridCell(doc, site);
			hLayout.addComponents(cell);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean checkUploads() {
		boolean bool = true;
		Iterator<Component> iter = hLayout.iterator();
		while(iter.hasNext()) {
			Component c = iter.next();
			UploadGridCell cell = (UploadGridCell) c;
			if (cell.hasUploadFailed()) {
				bool = false;
				break;
			}
		}
		return bool;
	}
	
	public void reset() {
		hLayout.removeAllComponents();
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private HorizontalLayout hLayout = new HorizontalLayout();
}
