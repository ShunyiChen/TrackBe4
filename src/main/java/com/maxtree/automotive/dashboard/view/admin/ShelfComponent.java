package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 密集架结构UI
 * 
 * @author Chen
 *
 */
public class ShelfComponent extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShelfComponent(FrameNumber frame) {
		this.frame = frame;
		initComponents();
	}
	
	private void initComponents() {
		addStyleName("denseframecomponent-border");
		this.setSizeUndefined();
		Label title = new Label(frame.getFrameCode()+"");
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		hlayout.setWidthUndefined();
		hlayout.setHeight("40px");
		hlayout.addComponents(checkBox, title);
		hlayout.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		
		GridLayout grid = new GridLayout();
		grid.setRows(frame.getMaxRow());
		grid.setColumns(frame.getMaxColumn());
		
		
		List<FrameNumber> cells = ui.frameService.findAllCell(frame.getStorehouseName(), frame.getFrameCode());
		for (FrameNumber cell : cells) {
			CellComponent child = new CellComponent(cell);
			grid.addComponent(child);
//			grid.setRowExpandRatio(i, 0.0f);
//			grid.setColumnExpandRatio(j, 0.0f);
		}
		this.addComponents(hlayout, grid);
	}
	
	public boolean isSelected() {
		return checkBox.getValue();
	}
	
	public void sertSelected(boolean bool) {
		checkBox.setValue(bool);
	}
	
	public FrameNumber getFrame() {
		return frame;
	}
	
	private FrameNumber frame;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private CheckBox checkBox = new CheckBox("密集架:", false);
}
