package com.maxtree.automotive.dashboard.view.search;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

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
		
		ShortcutListener enter = new ShortcutListener("Shortcut123 Name", com.vaadin.event.ShortcutAction.KeyCode.DELETE,
				null) {
			@Override
			public void handleAction(Object sender, Object target) {
				
			}
		};
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
		
		sizePerPageLabel.setValue("每页显示"+sizePerPage+"行");
		currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		numField.setValue(currentPageIndex+"");
	}
	
	/**
	 * 
	 */
	public void execute() {
		pageCount = ui.transactionService.findPagingCount(sizePerPage, grid.getKeyword());
		pageSizeLabel.setValue("总共"+pageCount+"页");
		first();
	}
	
	/**
	 * 
	 */
	private void first() {
		if (grid != null) {
			List<Transaction> items = ui.transactionService.findAll(sizePerPage, 0, grid.getKeyword()); 
			grid.setPerPageData(items);
			
			// Update inputs
			currentPageIndex = 1;
			numField.setValue(currentPageIndex+"");
			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	/**
	 * 
	 */
	private void previous() {
		if (grid != null) {
			currentPageIndex -= 1;
			if (currentPageIndex < 1) {
				currentPageIndex = 1;
			}
			int offset = (currentPageIndex - 1) * sizePerPage;
			List<Transaction> items = ui.transactionService.findAll(sizePerPage, offset, grid.getKeyword()); 
			grid.setPerPageData(items);
			
			// Update inputs
			numField.setValue(currentPageIndex+"");
			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		 
		}
	}
	
	/**
	 * 
	 */
	private void next() {
		if (grid != null) {
			currentPageIndex++;
			if (currentPageIndex > pageCount) {
				currentPageIndex = pageCount;
			}
			
			int offset = (currentPageIndex -1) * sizePerPage;
			List<Transaction> items = ui.transactionService.findAll(sizePerPage, offset, grid.getKeyword()); 
			grid.setPerPageData(items);
			// Update inputs
			numField.setValue(currentPageIndex+"");
			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	/**
	 * 
	 */
	private void last() {
		if (grid != null) {
			
			currentPageIndex = pageCount;
			int offset = (currentPageIndex -1) * sizePerPage;
			List<Transaction> items = ui.transactionService.findAll(sizePerPage, offset, grid.getKeyword()); 
			grid.setPerPageData(items);
			// Update inputs
			numField.setValue(currentPageIndex+"");
			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	private void jumpTo() {
		int offset = (currentPageIndex - 1) * sizePerPage;
		List<Transaction> data = ui.transactionService.findAll(sizePerPage, offset, grid.getKeyword());
		grid.setPerPageData(data);
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
}
