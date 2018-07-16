package com.maxtree.automotive.dashboard.view.admin.storehouse;

import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.FileBox;
import com.maxtree.automotive.dashboard.domain.Portfolio;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class FileBoxListWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param fileBox
	 */
	public FileBoxListWindow(FileBox fileBox) {
		this.fileBox = fileBox;
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
		
		List<Portfolio> lst = ui.storehouseService.findAllPortfolio(fileBox.getFileboxUniqueId());
		
    	grid.setWidth("100%");
    	grid.setHeightUndefined();
    	
    	grid.setHeightByRows(12);
    	
    	grid.setItems(lst);
    	grid.removeAllColumns();
//    	grid.addColumn(Portfolio::getCode).setCaption("编号");
    	grid.addColumn(Portfolio::getVin).setCaption("车辆VIN");
    	grid.setSelectionMode(SelectionMode.SINGLE);
    	
    	grid.addSelectionListener(event -> {
    	    selected = event.getAllSelectedItems();
//    	    if (selected.size() > 0) {
//    	    	List<Reminder> selectreminders = new ArrayList<>(selected);
//        	    textarea.setValue(selectreminders.get(0).getContent());
//    	    } 
    	});
    	
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
	public static void open(FileBox fileBox, Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        FileBoxListWindow w = new FileBoxListWindow(fileBox);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Grid<Portfolio> grid = new Grid<>(Portfolio.class);
	private Set<Portfolio> selected = null;
	private FileBox fileBox;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
