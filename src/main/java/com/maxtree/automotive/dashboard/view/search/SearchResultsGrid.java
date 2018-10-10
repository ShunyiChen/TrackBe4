package com.maxtree.automotive.dashboard.view.search;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.GridContextClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
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
		grid.addColumn(Transaction::getBusinessName).setCaption("业务类型");
		grid.addColumn(Transaction::getBatch).setCaption("批次号");
		grid.addColumn(Transaction::getCode).setCaption("上架号");
		grid.addColumn(Transaction::getIndexNumber).setCaption("业务顺序号");
		grid.addColumn(Transaction::getDateCreated).setCaption("创建日期");
		grid.addColumn(Transaction::getDateModified).setCaption("最后修改日期");
		grid.addColumn(Transaction::getDateFinished).setCaption("办结日期");
		grid.addColumn(Transaction::getCreator).setCaption("录入人");
		grid.addColumn(Transaction::getStatus).setCaption("状态");
        
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addContextClickListener(e->{
        	GridContextClickEvent<Transaction> event = (GridContextClickEvent<Transaction>) e;
        	trans = event.getItem();
        	
        });
        ContextMenu menu = new ContextMenu(grid, true);
		menu.addItem("查看原文", new Command() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				
				Callback onOK = new Callback() {
					@Override
					public void onSuccessful() {
						ui.transactionService.deleteByUniqueID(trans.getTransactionUniqueId(), trans.getVin());
						Notification.show("删除成功");
					}
				};
				MessageBox.showMessage("删除提示","请确认是否彻底删除该记录？",MessageBox.WARNING, onOK, "确定");
				
			}
		});
		
		menu.addItem("数据表索引", new Command() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				Callback onOK = new Callback() {

					@Override
					public void onSuccessful() {
						
					}
				};
				int index = ui.transactionService.getTableIndex(trans.getVin());
				MessageBox.showMessage("记录信息", "表索引："+index+" ID:"+trans.getTransactionUniqueId()+" UUID:"+trans.getUuid(), onOK);
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
	
	public String getPlateType() {
		return plateType == null? "" : plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public void executeByKeyword() {
		this.removeAllComponents();
		this.addComponents(grid, controls);
        this.setExpandRatio(grid, 1);
        this.setExpandRatio(controls, 0);
		controls.execute();
	}
	
	public void executeByPlateNumberOrVIN() {
		this.removeAllComponents();
		this.addComponents(grid, controls);
        this.setExpandRatio(grid, 1);
        this.setExpandRatio(controls, 0);
		controls2.execute();
	}
	
	private String plateType; 				// 号牌种类
	private String plateNumber; 			// 号码号牌
	private String vin; 					// 车辆识别代码
	private Transaction trans;
	private String communityName;
	private String keyword;
	private List<Transaction> allData;
	private Grid<Transaction> grid = new Grid<>();
	private ControlsLayout controls = new ControlsLayout(this); // 关键字查询分页
	private ControlsLayout2 controls2 = new ControlsLayout2(this); // 号牌查询分页
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
