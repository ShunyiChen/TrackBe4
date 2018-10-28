package com.maxtree.automotive.dashboard.view.shelf;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class DownGrid extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DownGrid() {
		initComponents();
	}
	
	private void initComponents() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		community = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		plateType.setReadOnly(true);
		plateNumber.setReadOnly(true);
		plateVIN.setReadOnly(true);
		
		HorizontalLayout info = new HorizontalLayout();
		info.setWidthUndefined();
		info.setSpacing(false);
		info.setMargin(false);
		Label plateTypeLabel = new Label("号牌种类:");
		Label plateNumberLabel = new Label("号码号牌:");
		Label plateVINLabel = new Label("车辆识别代号:");
		info.addComponents(Box.createHorizontalBox(15),plateTypeLabel,plateType,Box.createHorizontalBox(15),plateNumberLabel,plateNumber,Box.createHorizontalBox(15),plateVINLabel,plateVIN);
		info.setComponentAlignment(plateTypeLabel, Alignment.MIDDLE_LEFT);
		info.setComponentAlignment(plateNumberLabel, Alignment.MIDDLE_LEFT);
		info.setComponentAlignment(plateVINLabel, Alignment.MIDDLE_LEFT);
		info.setComponentAlignment(plateType, Alignment.MIDDLE_LEFT);
		info.setComponentAlignment(plateNumber, Alignment.MIDDLE_LEFT);
		info.setComponentAlignment(plateVIN, Alignment.MIDDLE_LEFT);
		
		
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
        this.addComponents(info, grid);
        this.setExpandRatio(info, 0);
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
	
	/**
	 * 
	 */
	public void execute() {
		// 按车牌号查询
		if (keyword.length() == 5 || keyword.length() == 6) {
			 List<Transaction> rs = ui.transactionService.search_by_keyword(-1, 0, keyword, community.getCommunityName());
			 grid.setItems(rs);
		} else {
			Notifications.warning("关键字长度应该在5~6位。");
		}
	}
	
	/**
	 * 
	 */
	public void clearSortOrder() {
		grid.clearSortOrder();
	}
	
	private Community community;
	private User loggedInUser;
	private String keyword;
	private List<Transaction> allData;
	private Grid<Transaction> grid = new Grid<>();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField plateType = new TextField();
	private TextField plateNumber = new TextField();
	private TextField plateVIN = new TextField();
	public Transaction editableTrans = null;
}
