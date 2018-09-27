package com.maxtree.automotive.dashboard.view.search;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.domain.Imaging;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Grid.GridContextClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

public class SearchResultsGrid extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchResultsGrid() {
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
		grid.addColumn(Transaction::getDateFinished).setCaption("办结日期");
		grid.addColumn(Transaction::getStatus).setCaption("业务状态");
        
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addContextClickListener(e->{
        	GridContextClickEvent<Transaction> event = (GridContextClickEvent<Transaction>) e;
        	trans = event.getItem();
//        	grid.select(trans);
        });
        ContextMenu menu = new ContextMenu(grid, true);
		menu.addItem("查看原文", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				Callback callback = new Callback() {
					@Override
					public void onSuccessful() {
					}
        		};
        		
        		ViewFilesWindow.open(callback, trans);
			}
		});
		menu.addItem("删除记录", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				
				Callback onOK = new Callback() {
					@Override
					public void onSuccessful() {
						Set<Transaction> set = grid.getSelectedItems();
						Iterator<Transaction> iter = set.iterator();
						while(iter.hasNext()) {
							Transaction t = iter.next();
							
						}
					}
				};
				MessageBox.showMessage("删除提示","请确认是否彻底删除该记录？",MessageBox.WARNING, onOK, "彻底删除");
				
			}
		});
		
        grid.addItemClickListener(e->{
        	
        	if(e.getMouseEventDetails().isDoubleClick()) {
        		trans = e.getItem();
        		ui.access(new Runnable() {
                    @Override
                    public void run() {
                    	Callback callback = new Callback() {
        					@Override
        					public void onSuccessful() {
        						
        					}
                		};
                		
                		ViewFilesWindow.open(callback, trans);
                    }
                 });
        		
        		
        	}
        });
		 
        this.addComponents(grid, controls);
        this.setExpandRatio(grid, 1);
        this.setExpandRatio(controls, 0);
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
	
	/**
	 * 
	 * @param perPageData
	 */
	public void setPerPageData(List<Transaction> perPageData) {
    	grid.setItems(perPageData);
    }
	
	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void execute() {
		controls.execute();
	}
	
	
	private Transaction trans;
	private String communityName;
	private String keyword;
	private List<Transaction> allData;
	private Grid<Transaction> grid = new Grid<>();
	private ControlsLayout controls = new ControlsLayout(this);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
