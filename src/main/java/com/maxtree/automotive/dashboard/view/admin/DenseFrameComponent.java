package com.maxtree.automotive.dashboard.view.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.DenseFrame;
import com.maxtree.automotive.dashboard.domain.FileBox;
import com.maxtree.automotive.dashboard.domain.Portfolio;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class DenseFrameComponent extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DenseFrameComponent(DenseFrame denseFrame) {
		this.denseFrame = denseFrame;
		initComponents();
	}
	
	private void initComponents() {
		addStyleName("denseframecomponent-border");
		this.setSizeUndefined();
		Label title = new Label(denseFrame.getName()+"-"+denseFrame.getSerialNumber());
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		hlayout.setWidthUndefined();
		hlayout.setHeight("40px");
		hlayout.addComponents(checkBox, title);
		hlayout.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		
		GridLayout grid = new GridLayout();
		grid.setRows(denseFrame.getMaxRow());
		grid.setColumns(denseFrame.getMaxCol());
		
		
//		int serialNumber = 0;// 单元格SN
//		for (int i = 0; i < denseFrame.getMaxRow(); i++) {
//			
//			for (int j = 0; j < denseFrame.getMaxCol(); j++) {
//				serialNumber++;
//				FileBox fileBox = new FileBox();
//				fileBox.setCol(j);
//				fileBox.setRow(i);
//				fileBox.setSerialNumber(serialNumber);
//				fileBox.setDenseframeSN(denseFrame.getSerialNumber());
//				
//				FileBoxComponent child = new FileBoxComponent(fileBox);
//				grid.addComponent(child);
//				grid.setRowExpandRatio(i, 0.0f);
//				grid.setColumnExpandRatio(j, 0.0f);
//			}
//		}
		this.addComponents(hlayout, grid);
	}
	
	public boolean isSelected() {
		return checkBox.getValue();
	}
	
	public void sertSelected(boolean bool) {
		checkBox.setValue(bool);
	}
	
	public DenseFrame getDenseFrame() {
		return denseFrame;
	}
	
	private DenseFrame denseFrame;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private CheckBox checkBox = new CheckBox("密集架:", false);
}
