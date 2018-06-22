package com.maxtree.automotive.dashboard.view.search;

import java.util.List;

import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

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
		numField.addShortcutListener(createFocusableShortcutListener(numField, KeyCode.ENTER));
		
		setPageCount(0);
		setCurrentPageIndex(0);
		setSizePerPage(0);
	}
	
	private void first() {
		if (grid != null) {
			if (grid.getAllData().size() > sizePerPage) {
				List<Transaction> data = grid.getAllData().subList(0, sizePerPage);
				grid.setItems(data);
			} else {
				grid.setItems(grid.getAllData());
			}
			// Update inputs
			numField.setValue((1)+"");
			currentPageIndexLabel.setValue("第"+(1)+"页 ，");
		}
	}
	
	private void previous() {
		if (grid != null) {
			currentPageIndex -= 2;
			if (currentPageIndex < 0) {
				currentPageIndex = 0;
			}
			int fromIndex = currentPageIndex * sizePerPage;
			int toIndex = fromIndex + sizePerPage;
			if (grid.getAllData().size() > sizePerPage) {
				List<Transaction> data = grid.getAllData().subList(fromIndex , toIndex);
				grid.setItems(data);
			} else {
				grid.setItems(grid.getAllData());
			}
			
			// Update inputs
			numField.setValue((currentPageIndex+1)+"");
			currentPageIndexLabel.setValue("第"+(currentPageIndex+1)+"页 ，");
			
			// For next 
			if (currentPageIndex == 0) {
				currentPageIndex = 1;
			}
		}
	}
	
	private void next() {
		if (grid != null) {
			currentPageIndex++;
			if (currentPageIndex > pageCount) {
				currentPageIndex = pageCount;
			}
			
			int fromIndex = (currentPageIndex -1) * sizePerPage;
			int toIndex = fromIndex + sizePerPage;
			if (toIndex > grid.getAllData().size()) {
				toIndex = grid.getAllData().size();
			}
			if (grid.getAllData().size() > sizePerPage) {
				List<Transaction> data = grid.getAllData().subList(fromIndex , toIndex);
				grid.setItems(data);
			} else {
				grid.setItems(grid.getAllData());
			}

			// Update inputs
			numField.setValue(currentPageIndex+"");
			currentPageIndexLabel.setValue("第"+currentPageIndex+"页 ，");
		}
	}
	
	private void last() {
		if (grid != null) {
			if (grid.getAllData().size() > sizePerPage) {
				
				int pageIndex = grid.getAllData().size() / sizePerPage;
				int remainder = grid.getAllData().size() % sizePerPage;
				int fromIndex = pageIndex * sizePerPage;
				int toIndex = fromIndex + remainder;
				if (toIndex > grid.getAllData().size()) {
					toIndex = grid.getAllData().size();
				}
				List<Transaction> data = grid.getAllData().subList(fromIndex, toIndex);
				grid.setItems(data);
			} else {
				grid.setItems(grid.getAllData());
			}
			
			// Update inputs
			numField.setValue((pageCount)+"");
			currentPageIndexLabel.setValue("第"+(pageCount)+"页 ，");
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
			int fromIndex = num * sizePerPage;
			int toIndex = fromIndex + sizePerPage;
			if (toIndex > grid.getAllData().size()) {
				toIndex = grid.getAllData().size();
			}
			List<Transaction> data = grid.getAllData().subList(fromIndex, toIndex);
			grid.setItems(data);
		}
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		pageSizeLabel.setValue("总共"+pageCount+"页");
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
	private SearchResultsGrid grid;
	private Label currentPageIndexLabel = new Label();
	private Label pageSizeLabel = new Label();
	private Label sizePerPageLabel = new Label();
}
