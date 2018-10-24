package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ShelfPropertiesWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ShelfPropertiesWindow() {
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("库房属性");
		this.setWidth("310px");
		this.setHeightUndefined();
		this.setClosable(true);
		this.setResizable(true);
		this.setModal(true);
		
		name.setCaption("库房名称:");
		shelfCount.setCaption("密集架总数:");
		cellCount.setCaption("单元格总数:");
		recordPerCellCount.setCaption("每单元格记录数:");
		recordCount.setCaption("记录数总数:");
		usedCount.setCaption("使用记录数总数:");
		
		VerticalLayout main = new VerticalLayout();
		main.setWidth("100%");
		main.setHeightUndefined();
		
		FormLayout form = new FormLayout();
		form.addComponents(name,shelfCount,cellCount,recordPerCellCount,recordCount,usedCount);
		main.addComponent(form);
		this.setContent(main);
	}
	
	/**
	 * 
	 * @param storename
	 */
	private void populate(String storename) {
		ui.access(new Runnable() {

			@Override
			public void run() {
				List<FrameNumber> allShelf = ui.frameService.findAllFrame(storename);
				name.setValue(storename);
				shelfCount.setValue(allShelf.size()+"");
				int cellAllCount = ui.frameService.findCellTotalCount(storename);
				cellCount.setValue(cellAllCount+"");
				recordPerCellCount.setValue("300");
				int folderCount = ui.frameService.findFolderTotalCount(storename, false);
				recordCount.setValue(folderCount+"");
				int usedFolderCount = ui.frameService.findFolderTotalCount(storename, true);
				usedCount.setValue(usedFolderCount+"");
			}
			
		});
		
	} 
	
	public static void open(FrameNumber store) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ShelfPropertiesWindow w = new ShelfPropertiesWindow();
        w.populate(store.getStorehouseName());
//        int frameCode = ui.frameService.findNextCodeOfFrame(store.getStorehouseName());
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	
	private Label name = new Label();
	private Label shelfCount = new Label();
	private Label cellCount = new Label();
	private Label recordPerCellCount = new Label();
	private Label recordCount = new Label();
	private Label usedCount = new Label();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
