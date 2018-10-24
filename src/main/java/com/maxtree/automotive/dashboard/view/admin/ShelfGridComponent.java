package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 密集架表格形式
 * 
 * @author Chen
 *
 */
public class ShelfGridComponent extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param frame
	 */
	public ShelfGridComponent(FrameNumber frame) {
		this.frame = frame;
		initComponents();
	}
	
	private void initComponents() {
		this.setSizeUndefined();
		Label title = new Label(frame.getFrameCode()+"("+frame.getMaxColumn()+"列 x "+frame.getMaxRow()+"行 x "+frame.getMaxfolder()+"个文件夹)");
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		hlayout.setWidthUndefined();
		hlayout.setHeight("40px");
		hlayout.addComponents(title);
		hlayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		
		GridLayout grid = new GridLayout();
		grid.setRows(frame.getMaxRow());
		grid.setColumns(frame.getMaxColumn());
		
		List<FrameNumber> cells = ui.frameService.findAllCell(frame.getStorehouseName(), frame.getFrameCode());
		for (FrameNumber cell : cells) {
			CellComponent child = new CellComponent(cell);
			grid.addComponent(child);
		}
		this.addComponents(hlayout, grid);
	}
	
	public FrameNumber getFrame() {
		return frame;
	}
	
	private FrameNumber frame;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
