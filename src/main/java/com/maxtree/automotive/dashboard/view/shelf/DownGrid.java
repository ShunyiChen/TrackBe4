package com.maxtree.automotive.dashboard.view.shelf;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class DownGrid extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DownGrid() {
		initComponents();
	}
	
	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		plateType.setReadOnly(true);
		plateNumber.setReadOnly(true);
		plateVIN.setReadOnly(true);
		
		plateType.setWidth("400px");
		plateNumber.setWidth("400px");
		plateVIN.setWidth("400px");
		
		form.addComponents(plateType, plateNumber, plateVIN);
		
		grid.setSizeFull();
		grid.addColumn(Transaction::getCode).setCaption("上架号");
		grid.addColumn(Transaction::getBarcode).setCaption("条形码");
		grid.addColumn(Transaction::getBusinessCode).setCaption("业务类型");
		grid.addColumn(Transaction::getStatus).setCaption("业务状态");
        
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addItemClickListener(e->{
        	editableTrans = e.getItem();
        	plateType.setValue(editableTrans.getPlateType());
        	plateNumber.setValue(editableTrans.getPlateNumber());
        	plateVIN.setValue(editableTrans.getVin());
        });
        this.addComponents(form, grid);
        this.setExpandRatio(form, 0);
        this.setExpandRatio(grid, 1);
        
	}
	
	public void setItems(List<Transaction> data) {
		grid.setItems(data);
	}
	
	public void setAllData(List<Transaction> allData) {
    	this.allData = allData;
    }
	
	public List<Transaction> getAllData() {
    	return allData;
    }
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public void execute() {
		if (keyword.length() == 7 || keyword.length() == 8) {
			 List<Transaction> rs = ui.transactionService.findAll(-1, 0, keyword);
			 grid.setItems(rs);
		} else {
			Notifications.warning("关键字长度应该在7~8位。");
		}
	}
	
	public void clearSortOrder() {
		grid.clearSortOrder();
	}
	
	private String keyword;
	private List<Transaction> allData;
	private Grid<Transaction> grid = new Grid<>();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField plateType = new TextField("号牌种类:");
	private TextField plateNumber = new TextField("号码号牌:");
	private TextField plateVIN = new TextField("车辆识别代码:");
	public Transaction editableTrans = null;
}
