package com.maxtree.automotive.dashboard.view.admin.storehouse;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.domain.FileBox;
import com.vaadin.ui.Button;

public class FileBoxComponent extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param fileBox
	 */
	public FileBoxComponent(FileBox fileBox) {
		this.fileBox = fileBox;
		initComponents();
		initActions();
	}
	
	private void initComponents() {
		this.setCaption(fileBox.getCode());
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
			
			FileBoxListWindow.open(fileBox, callback);
		});
	}
	
	private FileBox fileBox;
}
