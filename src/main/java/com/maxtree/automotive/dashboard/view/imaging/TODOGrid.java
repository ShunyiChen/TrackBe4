package com.maxtree.automotive.dashboard.view.imaging;

import java.util.List;

import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

public class TODOGrid extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TODOGrid() {
		initComponents();
	}
	
	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		grid.setSizeFull();
		grid.addColumn(Transaction::getBarcode).setCaption("条形码");
		grid.addColumn(Transaction::getPlateType).setCaption("号牌种类");
		grid.addColumn(Transaction::getPlateNumber).setCaption("号码号牌");
		grid.addColumn(Transaction::getVin).setCaption("车辆识别代码");
		grid.addColumn(Transaction::getDateCreated).setCaption("创建日期");
		grid.addColumn(Transaction::getDateModified).setCaption("最后修改日期");
		grid.addColumn(Transaction::getStatus).setCaption("业务状态");
        
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.MULTI);
        
        this.addComponents(grid);
        
        this.setExpandRatio(grid, 1);
	}
	
	public void setItems(List<Transaction> data) {
		grid.setItems(data);
	}
	
//	public ControlsLayout getControlsLayout() {
//		return controls;
//	}
	
	public void setAllData(List<Transaction> allData) {
    	this.allData = allData;
    }
	
	public List<Transaction> getAllData() {
    	return allData;
    }
	
	private List<Transaction> allData;
	private Grid<Transaction> grid = new Grid<>();
//	private ControlsLayout controls = new ControlsLayout(this);
}
