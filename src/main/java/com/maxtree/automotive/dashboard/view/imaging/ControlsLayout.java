package com.maxtree.automotive.dashboard.view.imaging;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
	public ControlsLayout(TodoListGrid grid) {
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
		numField.addShortcutListener(createFocusableShortcutListener(numField, KeyCode.ENTER));
//		pageCount = ui.imagingService.findPagingCount(20,grid.keyword);
		pageSizeLabel.setValue("总共"+pageCount+"页");
		
		setCurrentPageIndex(0);
		setSizePerPage(20);
	}
	
	/**
	 * 重新计算页数
	 */
	public void recount() {
//		pageCount = ui.imagingService.findPagingCount(20,grid.keyword);
//		pageSizeLabel.setValue("总共"+pageCount+"页");
	}
	
	public void first() {
//		if (grid != null) {
//			List<Imaging> items = ui.imagingService.findAll(sizePerPage, 0, grid.keyword);
//			grid.setPerPageData(items);
//
//			// Update inputs
//			currentPageIndex = 1;
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
//		}
	}
	
	private void previous() {
		if (grid != null) {
//			currentPageIndex -= 1;
//			if (currentPageIndex < 1) {
//				currentPageIndex = 1;
//			}
//			int offset = (currentPageIndex - 1) * sizePerPage;
//			List<Imaging> items = ui.imagingService.findAll(sizePerPage,offset,grid.keyword);
//			grid.setPerPageData(items);
//
//			// Update inputs
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	private void next() {
		if (grid != null) {
//			currentPageIndex++;
//			if (currentPageIndex > pageCount) {
//				currentPageIndex = pageCount;
//			}
//
//			int offset = (currentPageIndex -1) * sizePerPage;
//			List<Imaging> items = ui.imagingService.findAll(sizePerPage,offset,grid.keyword);
//			grid.setPerPageData(items);
//			// Update inputs
//			numField.setValue(currentPageIndex+"");
//			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	private void last() {
		if (grid != null) {
//			currentPageIndex = pageCount;
//			int offset = (currentPageIndex -1) * sizePerPage;
//			List<Imaging> items = ui.imagingService.findAll(sizePerPage, offset, grid.keyword);
//			grid.setPerPageData(items);
//
//			// Update inputs
//			numField.setValue((pageCount)+"");
//			currentPageIndexLabel.setValue("第"+(pageCount)+"页 ，");
		}
	}
	
	private ShortcutListener createFocusableShortcutListener(Focusable focusable, int keyCode, int... modifiers) {
        return new ShortcutListener(null, keyCode, modifiers) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void handleAction(Object sender, Object target) {
            	if (!numField.getValue().trim().equals("")) {
            		String num = numField.getValue();
                	jumpTo(Integer.parseInt(num));
            	}
            }
        };
    }
	
	private void jumpTo(int num) {
		num--;
		if (num > pageCount || num <0) {
			Notifications.warning("当前页数范围应该在1-"+pageCount+"");
		} else {
//			int fromIndex = num * sizePerPage;
//			List<Imaging> data = ui.imagingService.findAll(sizePerPage, fromIndex,grid.keyword);
//			grid.setPerPageData(data);
		}
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public int getSizePerPage() {
		return sizePerPage;
	}

	public void setSizePerPage(int sizePerPage) {
		this.sizePerPage = sizePerPage;
		sizePerPageLabel.setValue("每页显示"+sizePerPage+"行");
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
		currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		numField.setValue(currentPageIndex+"");
	}
	private int pageCount;
	private int sizePerPage;
	private int currentPageIndex;
	private Button first = new Button("<<");
	private Button previous = new Button("<");
	private DoubleField numField = new DoubleField();
	private Button next = new Button(">");
	private Button last = new Button(">>");
	private TodoListGrid grid;
	private Label currentPageIndexLabel = new Label();
	private Label pageSizeLabel = new Label();
	private Label sizePerPageLabel = new Label();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
