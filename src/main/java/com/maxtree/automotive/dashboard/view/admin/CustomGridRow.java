package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author Chen
 *
 */
public class CustomGridRow extends HorizontalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param rowData
	 */
	public CustomGridRow(Object[] rowData) {
		this.rowData = rowData;
		this.entityId = Integer.parseInt(rowData[rowData.length-1].toString());
		initComponents();
	}
	
	private void initComponents() {
		setSpacing(false);
		setMargin(false);
		setWidthUndefined();
		setHeightUndefined();
		addStyleName("CustomGridRow");
	}
	
	/**
	 * 
	 * @return
	 */
	public int getEntityId() {
		return entityId;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getRowData() {
		return rowData;
	}
	
	private Object[] rowData; // row data
	private int entityId; // ID
}
