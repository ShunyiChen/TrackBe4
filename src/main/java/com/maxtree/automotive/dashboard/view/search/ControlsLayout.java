package com.maxtree.automotive.dashboard.view.search;

import java.util.List;

import com.maxtree.automotive.dashboard.service.AuthService;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * 按关键字查询的翻页
 * 
 * @author chens
 *
 */
public class ControlsLayout extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param grid
	 */
	public ControlsLayout(SearchResultsGrid grid) {
		this.grid = grid;
		initComponents();
	}
	
	private void initComponents() {
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		User loggedInUser = ui.userService.getUserByUserName(username);
		community = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
//		localCodes = new LocationCode(ui.locationService);
		this.setWidthUndefined();
		numField.setWidth("73px");
		this.addComponents(first, previous, numField, next, last, currentPageIndexLabel, pageSizeLabel, sizePerPageLabel);
		this.setComponentAlignment(currentPageIndexLabel, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(pageSizeLabel, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(sizePerPageLabel, Alignment.MIDDLE_LEFT);
		first.addClickListener(e -> {
			first();
		});
		previous.addClickListener(e -> {
			previous();
		});
		next.addClickListener(e -> {
			next();
		});
		last.addClickListener(e -> {
			last();
		});
		
		sizePerPageLabel.setValue("每页显示"+sizePerPage+"行");
		currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		numField.setValue(currentPageIndex+"");
		
		numField.addValueChangeListener(e->{
			try {
				int num = Integer.parseInt(numField.getValue());
				if (num > pageCount || num <0) {
					Notifications.warning("当前页数范围应该在1~"+pageCount+"");
				} 
				currentPageIndex = num;
				jumpTo();
			} catch (NumberFormatException e2) {
			}
			
		});
	}
	
	/**
	 * 
	 */
	public void execute() {
		if(StringUtils.isEmpty(grid.getKeyword().trim())) {
			Notifications.warning("查询关键字不能为空。");
			return;
		}
//		String locationCode = localCodes.getCompleteLocationCode(community);
//		pageCount = ui.transactionService.search_by_keyword_pagingcount(sizePerPage, grid.getKeyword(),grid.getTenantName(),locationCode);
		pageSizeLabel.setValue("总共"+pageCount+"页");
		first();
	}
	
	/**
	 * 
	 */
	private void first() {
		if (grid != null) {
//			String locationCode = localCodes.getCompleteLocationCode(community);
//			List<Transaction> items = ui.transactionService.search_by_keyword(sizePerPage, 0, grid.getKeyword(),locationCode);
//			grid.setPerPageData(items);
//
//			// Update inputs
//			currentPageIndex = 1;
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	/**
	 * 
	 */
	private void previous() {
//		if (grid != null) {
//			currentPageIndex -= 1;
//			if (currentPageIndex < 1) {
//				currentPageIndex = 1;
//			}
//			String locationCode = localCodes.getCompleteLocationCode(community);
//			int offset = (currentPageIndex - 1) * sizePerPage;
//			List<Transaction> items = ui.transactionService.search_by_keyword(sizePerPage,offset,grid.getKeyword(),locationCode);
//			grid.setPerPageData(items);
//
//			// Update inputs
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
//		}
	}
	
	/**
	 * 
	 */
	private void next() {
//		if (grid != null) {
//			currentPageIndex++;
//			if (currentPageIndex > pageCount) {
//				currentPageIndex = pageCount;
//			}
//			String locationCode = localCodes.getCompleteLocationCode(community);
//			int offset = (currentPageIndex -1) * sizePerPage;
//			List<Transaction> items = ui.transactionService.search_by_keyword(sizePerPage,offset,grid.getKeyword(),locationCode);
//			grid.setPerPageData(items);
//			// Update inputs
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
//		}
	}
	
	/**
	 * 
	 */
	private void last() {
//		if (grid != null) {
//			String locationCode = localCodes.getCompleteLocationCode(community);
//			currentPageIndex = pageCount;
//			int offset = (currentPageIndex -1) * sizePerPage;
//			List<Transaction> items = ui.transactionService.search_by_keyword(sizePerPage,offset,grid.getKeyword(), locationCode);
//			grid.setPerPageData(items);
//			// Update inputs
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
//		}
	}
	
	/**
	 * 
	 */
	private void jumpTo() {
//		String locationCode = localCodes.getCompleteLocationCode(community);
//		int offset = (currentPageIndex - 1) * sizePerPage;
//		List<Transaction> data = ui.transactionService.search_by_keyword(sizePerPage,offset,grid.getKeyword(),locationCode);
//		grid.setPerPageData(data);
	}
	
	private int pageCount;
	private int sizePerPage = 20;//每页显示行数
	private int currentPageIndex = 1;
	private Button first = new Button("<<");
	private Button previous = new Button("<");
	private TextField numField = new TextField();
	private Button next = new Button(">");
	private Button last = new Button(">>");
	private SearchResultsGrid grid;
	private Label currentPageIndexLabel = new Label();
	private Label pageSizeLabel = new Label();
	private Label sizePerPageLabel = new Label();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private User loggedInUser;
	private Community community;
}
