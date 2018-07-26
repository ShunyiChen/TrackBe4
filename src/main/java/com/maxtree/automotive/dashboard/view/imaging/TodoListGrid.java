package com.maxtree.automotive.dashboard.view.imaging;

import java.util.List;

import com.maxtree.automotive.dashboard.domain.Imaging;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

public class TodoListGrid extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TodoListGrid() {
		initComponents();
	}
	
	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		grid.setSizeFull();
		grid.addColumn(Imaging::getPlateType).setCaption("号牌种类");
		grid.addColumn(Imaging::getPlateNumber).setCaption("号码号牌");
		grid.addColumn(Imaging::getVin).setCaption("车辆识别代码");
		grid.addColumn(Imaging::getVin).setCaption("状态");
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.MULTI);
        
        this.addComponents(grid);
        
        this.setExpandRatio(grid, 1);
	}
	
	public void setItems(List<Imaging> data) {
		grid.setItems(data);
	}
	
//	public ControlsLayout getControlsLayout() {
//		return controls;
//	}
	
	public void setAllData(List<Imaging> allData) {
    	this.allData = allData;
    }
	
	public List<Imaging> getAllData() {
    	return allData;
    }
	
	private List<Imaging> allData;
	private Grid<Imaging> grid = new Grid<>();
//	private ControlsLayout controls = new ControlsLayout(this);
}
