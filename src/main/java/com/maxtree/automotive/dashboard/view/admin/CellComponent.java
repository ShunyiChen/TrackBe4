package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.ui.Button;

/**
 * 单元格组件
 * 
 * @author Chen
 *
 */
public class CellComponent extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param cell
	 */
	public CellComponent(FrameNumber cell) {
		this.cell = cell;
		initComponents();
		initActions();
	}
	
	private void initComponents() {
		this.setCaption("单元格-列"+cell.getCol()+",行"+cell.getRow());
		this.setWidth("130px");
		this.setHeight("40px");
		
		
		this.addStyleName("fileboxcomponent-background0");
//		if (fileBox.getStatus() == 0) {
//			this.addStyleName("fileboxcomponent-background0");
//		} else if (fileBox.getStatus() == 1) {
//			this.addStyleName("fileboxcomponent-background1");
//		} else if (fileBox.getStatus() == 2) {
//			this.addStyleName("fileboxcomponent-background1");
//		}
	}
	
	private void initActions() {
		addClickListener(e -> {
			
			Callback2 callback = new Callback2() {
				@Override
				public void onSuccessful(Object... objects) {
					
				}
			};
			
			ViewBagsWindow.open(cell, callback);
		});
	}
	
	private FrameNumber cell;
}
