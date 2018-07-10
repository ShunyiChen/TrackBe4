package com.maxtree.automotive.dashboard.view.front;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
	 */
	public ThumbnailGrid(String caption) {
		this.setCaption(caption);
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		this.addStyleName("picture-pane");
		UI.getCurrent().getPage().addBrowserWindowResizeListener(e->{
			int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 173;
			this.setHeight(height+"px");
		});
		
		int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 173;
		this.setWidth("100%");
		this.setHeight(height+"px");
		vLayout.setMargin(false);
		vLayout.setSpacing(true);
		vLayout.setWidth("100%");
		vLayout.setHeightUndefined();
    	setContent(vLayout);
    	ShortcutListener upListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_UP,
				null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				ThumbnailRow row = (ThumbnailRow) vLayout.getComponent(index);
				row.deselected();
				if (index > 0) {
					index--;
				} else {
					index = vLayout.getComponentCount() - 1;
				}
				row = (ThumbnailRow) vLayout.getComponent(index);
				row.selected();
				
				int pixels = (int) (row.getHeight()*index);
				
				System.out.println("up! current is "+index+"   pixels="+pixels);
				
				
				ThumbnailGrid.this.setScrollTop(pixels);
			}
		};
		ShortcutListener downListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_DOWN,
				null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				ThumbnailRow row = (ThumbnailRow) vLayout.getComponent(index);
				row.deselected();
				if (index < vLayout.getComponentCount() - 1) {
					index++;
				} else {
					index = 0;
				}
				row = (ThumbnailRow) vLayout.getComponent(index);
				row.selected();
				int pixels = (int) (row.getHeight()*index);
				System.out.println("down! current is "+index+"  pixels="+pixels);
				
				ThumbnailGrid.this.setScrollTop(pixels);
			}
		};
    	this.addShortcutListener(upListener);
    	this.addShortcutListener(downListener);
	}
	
	
	/**
	 * Remove all rows
	 */
	public void removeAllRows() {
		vLayout.removeAllComponents();
	}
	
	/**
	 * Add a new Row
	 * 
	 * @param lstRow
	 */
	public void addRow(ThumbnailRow row) {
		vLayout.addComponents(row);
		vLayout.setComponentAlignment(row, Alignment.TOP_LEFT);
	}
	
	/**
	 * Generate a new UUID string
	 */
	public void generateNewUUID() {
		uuid = UUID.randomUUID().toString();
		System.out.println("uuid="+uuid);
	}
	
	private static final Logger log = LoggerFactory.getLogger(ThumbnailGrid.class);
	private VerticalLayout vLayout = new VerticalLayout();
	private int index = 0;
	public String uuid = null;
}
