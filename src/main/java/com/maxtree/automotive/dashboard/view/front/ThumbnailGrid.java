package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.component.Hr;
import com.vaadin.event.ShortcutListener;
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
	 * 
	 * @param lstRow
	 */
	public void addRow(ThumbnailRow row) {
		vLayout.addComponents(row);
		vLayout.setComponentAlignment(row, Alignment.TOP_LEFT);
	}
	
	private static final Logger log = LoggerFactory.getLogger(ThumbnailGrid.class);
	private VerticalLayout vLayout = new VerticalLayout();
	private int height;
	private int index = 0;
}
