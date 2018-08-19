package com.maxtree.automotive.dashboard.view.imaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Imaging;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Grid.GridContextClickEvent;
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
		grid.addColumn(Imaging::getStatus).setCaption("状态");
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addContextClickListener(e->{
        	GridContextClickEvent<Imaging> event = (GridContextClickEvent<Imaging>) e;
        	selectedItem = event.getItem();
        	grid.select(selectedItem);
        });
        this.addComponents(grid, controls);
        this.setExpandRatio(grid, 1);
        this.setExpandRatio(controls, 0);
        
        ContextMenu menu = new ContextMenu(grid, true);
		menu.addItem("待提档", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				updateStatus("待提档");
			}
		});
		menu.addItem("待归档", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				updateStatus("待归档");
			}
		});
		menu.addItem("完成", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				updateStatus("完成");
			}
		});
	}
	
	/**
	 * 
	 * @param newStatus
	 */
	public void updateStatus(String newStatus) {
		ui.imagingService.updateImaging(selectedItem.getImagingUniqueId(), newStatus);
		
		selectedItem.setStatus(newStatus);
		grid.getSelectionModel().deselectAll();
		grid.select(selectedItem);
		
	}
	
	/**
	 * 
	 * @param perPageData
	 */
	public void setPerPageData(List<Imaging> perPageData) {
    	grid.setItems(perPageData);
    }
	
	/**
	 * 
	 * @param imagingUniqueId
	 */
	public void select(int imagingUniqueId) {
		ListDataProvider<Imaging> listDataProvider = (ListDataProvider<Imaging>) grid.getDataProvider();
		for (Imaging img : listDataProvider.getItems()) {
			if (img.getImagingUniqueId() == imagingUniqueId) {
				grid.select(img);
				break;
			}
		}
		
	}
	

	private Imaging selectedItem;
	private Grid<Imaging> grid = new Grid<>();
	public String keyword ="";
	public ControlsLayout controls = new ControlsLayout(this);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
