package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
//import com.maxtree.automotive.dashboard.domain.FileBox;
//import com.maxtree.automotive.dashboard.domain.Portfolio;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ViewFoldersWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param fileBox
	 */
	public ViewFoldersWindow(FrameNumber cell) {
		this.cell = cell;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("文件列表");
		this.setWidth("634px");
		this.setHeightUndefined();
		this.setClosable(true);
		this.setResizable(true);
		this.setModal(true);
		VerticalLayout main = new VerticalLayout();
		main.setMargin(false);
		main.setSpacing(false);
		main.setWidth("100%");
    	
		setContent(main);
		
		List<FrameNumber> lst = ui.frameService.findAllFolders(cell.getStorehouseName(), cell.getFrameCode(), cell.getCellCode());
    	grid.setWidth("100%");
    	grid.setHeight("100%");
    	grid.setItems(lst);
    	grid.removeAllColumns();
    	grid.addColumn(FrameNumber::getCode).setCaption("上架号");
    	grid.addColumn(FrameNumber::getVin).setCaption("车辆VIN");
    	grid.setSelectionMode(SelectionMode.SINGLE);
    	
//    	grid.addSelectionListener(event -> {
//    	    selected = event.getAllSelectedItems();
//    	    if (selected.size() > 0) {
//    	    	List<Reminder> selectreminders = new ArrayList<>(selected);
//        	    textarea.setValue(selectreminders.get(0).getContent());
//    	    } 
//    	});
    	
    	Panel scorllPane = new Panel();
    	scorllPane.setWidth("100%");
    	scorllPane.setHeight("500px");
    	scorllPane.setContent(grid);
    	
    	main.addComponents(scorllPane);
	}
	
	/**
	 * 
	 * @param storehouse
	 * @param callback
	 */
	public static void open(FrameNumber cell, Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ViewFoldersWindow w = new ViewFoldersWindow(cell);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Grid<FrameNumber> grid = new Grid<>(FrameNumber.class);
	private Set<FrameNumber> selected = null;
	private FrameNumber cell;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
