package com.maxtree.automotive.dashboard.view.shelf;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

public class UpGrid extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpGrid() {
		initComponents();
	}
	
	private void initComponents() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		community = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		grid.setSizeFull();
		grid.addColumn(Transaction::getBarcode).setCaption("条形码");
		grid.addColumn(Transaction::getPlateType).setCaption("号牌种类");
		grid.addColumn(Transaction::getPlateNumber).setCaption("号码号牌");
		grid.addColumn(Transaction::getVin).setCaption("车辆识别代码");
		grid.addColumn(Transaction::getBusinessCode).setCaption("业务类型");
		grid.addColumn(Transaction::getStatus).setCaption("业务状态");
        
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addItemClickListener(e->{
        	editableTrans = e.getItem();
        });
        this.addComponents(grid);
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
		// 按车牌号后5位或后6位查询
		if (keyword.length() == 5 || keyword.length() == 6) {
			 List<Transaction> rs = ui.transactionService.searchByKeyword(-1, 0, keyword, community.getCommunityName());
			 grid.setItems(rs);
		} else {
			Notifications.warning("关键字长度应该在7~8位。");
		}
	}

	public void clearSortOrder() {
		grid.clearSortOrder();
	}
	
	private Community community;
	private User loggedInUser;
	private String keyword;
	private List<Transaction> allData;
	private Grid<Transaction> grid = new Grid<>();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	public Transaction editableTrans = null;
}
