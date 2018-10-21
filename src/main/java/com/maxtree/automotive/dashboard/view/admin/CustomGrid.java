package com.maxtree.automotive.dashboard.view.admin;

import java.util.Iterator;
import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class CustomGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param title
	 * @param columns
	 * @param data
	 */
	public CustomGrid(String title, GridColumn[] columns, List<CustomGridRow> data) {
		this.title = title;
		this.columns = columns;
		this.data = data;
		initComponents();
	}
	
	private void initComponents() {
		this.setSizeFull();
		this.setSpacing(false);
		this.setMargin(false);
		
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		//查找
		HorizontalLayout searchBar = new HorizontalLayout();
		searchBar.setWidthUndefined();
		searchBar.setHeight("30px");
		searchBar.setSpacing(false);
		searchBar.setMargin(false);
		searchBar.addStyleName("CustomGrid_searchBar");
		Image magnifyingglass = new Image(null, new ThemeResource("img/adminmenu/magnifyingglass.png"));
		TextField searchField = new TextField();
		searchField.addValueChangeListener(e->{
			doFilter(e.getValue());
		});
//		ShortcutListener keyListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
//			/**/
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void handleAction(Object sender, Object target) {
//				doFilter(searchField.getValue());
//			}
//		};
//		searchField.addShortcutListener(keyListener);
		searchField.addStyleName("v-textfield");
		searchField.addStyleName("v-textfield:focus");
		searchField.setPlaceholder("搜索");
		searchBar.addComponents(magnifyingglass,searchField);
		searchBar.setComponentAlignment(magnifyingglass, Alignment.MIDDLE_CENTER);
		searchBar.setComponentAlignment(searchField, Alignment.MIDDLE_CENTER);
		
		//标题
		HorizontalLayout titleBar = new HorizontalLayout();
		titleBar.setSpacing(false);
		titleBar.setMargin(false);
		titleBar.setWidth("100%");
		titleBar.setHeight("40px");
		titleBar.addStyleName("CustomGrid_titleBar");
		Label titleLabel = new Label(title);
		titleBar.addComponents(titleLabel,searchBar);
		titleBar.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);
		titleBar.setComponentAlignment(searchBar, Alignment.MIDDLE_RIGHT);
		
		//滚动
		scrollPanelHeight = Page.getCurrent().getBrowserWindowHeight()-320;
		scrollPanel.setWidth("100%");
		scrollPanel.setHeight(scrollPanelHeight+"px");
		scrollPanel.addStyleName("CustomGrid_scrollPanel");
		
		VerticalLayout grid = new VerticalLayout();
		grid.setSpacing(false);
		grid.setMargin(false);
		grid.setSizeUndefined();
		HorizontalLayout columnBar = new HorizontalLayout();
		columnBar.setSpacing(false);
		columnBar.setMargin(false);
		columnBar.setWidthUndefined();
		columnBar.setHeight("48px");
		columnBar.addStyleName("CustomGrid_columnBar");
		
		for(GridColumn col : columns) {
			Label colLabel = new Label(col.getName());
			colLabel.addStyleName("CustomGrid_colLabel");
			colLabel.setWidth(col.getWidth()+"px");
			colLabel.setHeight("100%");
			columnBar.addComponent(colLabel);
			columnBar.setComponentAlignment(colLabel, Alignment.MIDDLE_LEFT);
		}
		
		dataGrid.setWidthUndefined();
		dataGrid.setHeightUndefined();
		dataGrid.setSpacing(false);
		dataGrid.setMargin(false);
		ui.access(new Runnable() {

			@Override
			public void run() {
				initData();
			}
		});
		
		grid.addComponents(columnBar,dataGrid);
		scrollPanel.setContent(grid);
		main.addComponents(titleBar,scrollPanel);
		this.addComponent(main);
		this.setComponentAlignment(main, Alignment.TOP_CENTER);
	}
	
	/**
	 * 追加到最后
	 * 
	 * @param row
	 */
	private void addRow(CustomGridRow row) {
		addRow(row, null);
	}
	
	/**
	 * 
	 * @param rowData   最后一个元素存放entityId
	 * @param keyword   查询用的关键字
	 * @return
	 */
    private void addRow(CustomGridRow row, Integer index) {
		int i = 0;
		for(Object obj : row.getRowData()) {
			if (i == columns.length) {
				break;
			}
			GridColumn col = columns[i];
			if(obj instanceof String || obj instanceof StringBuilder) {
				
				Label objLabel = new Label(obj.toString());
				objLabel.addStyleName("CustomGrid_objLabel");
				objLabel.setWidth(col.getWidth()+"px");
				objLabel.setHeight("40px");
				row.addComponent(objLabel);
				row.setComponentAlignment(objLabel, Alignment.MIDDLE_LEFT);
			}
			else if(obj instanceof Integer) {

				Label objLabel = new Label(obj+"");
				objLabel.addStyleName("CustomGrid_objLabel");
				objLabel.setWidth(col.getWidth()+"px");
				objLabel.setHeight("40px");
				row.addComponent(objLabel);
				row.setComponentAlignment(objLabel, Alignment.MIDDLE_LEFT);
			}
			else if(obj instanceof Image) {

				Image img = (Image) obj;
				HorizontalLayout innerCell = new HorizontalLayout();
				innerCell.setSpacing(false);
				innerCell.setMargin(false);
				innerCell.setSizeUndefined();
				innerCell.addComponent(img);
				
				VerticalLayout cell = new VerticalLayout();
				cell.setSpacing(false);
				cell.setMargin(false);
				cell.setWidth(col.getWidth()+"px");
				cell.addComponent(innerCell);
				row.addComponent(cell);
				row.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
			}
			else if(obj instanceof ImageWithString) {
				
				ImageWithString iws = (ImageWithString) obj;
				Label objLabel = new Label(iws.getName());
				HorizontalLayout innerCell = new HorizontalLayout();
				innerCell.setSpacing(false);
				innerCell.setMargin(false);
				innerCell.setSizeUndefined();
				innerCell.addComponents(iws.getImage(),objLabel);
				
				VerticalLayout cell = new VerticalLayout();
				cell.setSpacing(false);
				cell.setMargin(false);
				cell.setWidth(col.getWidth()+"px");
				cell.addComponent(innerCell);
				row.addComponent(cell);
				row.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
			}
			
			i++;
		}
		if(index == null) {
			dataGrid.addComponent(row);
		} else {
			dataGrid.addComponent(row, index.intValue());
		}
		
		dataGrid.setComponentAlignment(row, Alignment.MIDDLE_LEFT);
	}
	
	/**
	 * 
	 * @param row
	 */
	public void insertRow(CustomGridRow row) {
		data.add(row);
		addRow(row);// add for UI
		scrollPanel.setScrollTop(scrollPanelHeight);
		refreshRowColor();
	}
	
	/**
	 * 
	 * @param rowData
	 * @param id
	 */
	public void setValueAt(CustomGridRow rowData, int entityId) {
		int count = dataGrid.getComponentCount();
		for(int i = 0; i < count; i++) {
			CustomGridRow row = (CustomGridRow) dataGrid.getComponent(i);
			if(row.getEntityId() == entityId) {
				int index = data.indexOf(row);
				data.remove(index);
				data.add(index, rowData);

				index = dataGrid.getComponentIndex(row);
				dataGrid.removeComponent(row);
				addRow(rowData, index);
				break;
			}
		}
		refreshRowColor();
	}

	/**
	 * 
	 * @param entityId
	 */
	public void deleteRow(int entityId) {
		int count = dataGrid.getComponentCount();
		for(int i = 0; i < count; i++) {
			CustomGridRow row = (CustomGridRow) dataGrid.getComponent(i);
			if(row.getEntityId() == entityId) {
				int index = data.indexOf(row);
				data.remove(index);

				index = dataGrid.getComponentIndex(row);
				dataGrid.removeComponent(row);
				break;
			}
		}
		refreshRowColor();
	}
	
	/**
	 * 初始化表格数据
	 */
	private void initData() {
		int i = 0;
		for(CustomGridRow rowData : data) {
		    addRow(rowData);
		    if(i % 2 != 0) {
				rowData.addStyleName("CustomGrid_row");
			}
			i++;
		}
	}
	
	/**
	 * 用关键字过滤表格
	 * 
	 * @param keyword
	 */
	private void doFilter(String keyword) {
		dataGrid.removeAllComponents();
		int i = 0;
		for(CustomGridRow row : data) {
			int j = 0;
			boolean matched = false;
			for(Object obj : row.getRowData()) {
				if (j == columns.length) {
					break;
				}
				if(obj instanceof String || obj instanceof StringBuilder) {
					if(obj.toString().contains(keyword)) {
						matched = true;
					}
				}
				else if(obj instanceof Integer) {
					if((obj+"").contains(keyword)) {
						matched = true;
					}
				}
			}
			if(matched) {
				dataGrid.addComponent(row);
				dataGrid.setComponentAlignment(row, Alignment.MIDDLE_LEFT);
				row.removeStyleName("CustomGrid_row");
				if(i % 2 != 0) {
					row.addStyleName("CustomGrid_row");
				}
				i++;
			}
		}
	}

	/**
	 * 刷新表行背景颜色
	 */
	private void refreshRowColor() {
		Iterator<Component> iter = dataGrid.iterator();
		int i = 0;
		while(iter.hasNext()) {
			Component row = iter.next();
			row.removeStyleName("CustomGrid_row");
			if(i % 2 != 0) {
				row.addStyleName("CustomGrid_row");
			}
			i++;
		}
	}
	
	
	private String title;
	private GridColumn[] columns;
	private List<CustomGridRow> data;
	private VerticalLayout main = new VerticalLayout();
	private VerticalLayout dataGrid = new VerticalLayout();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Panel scrollPanel = new Panel();
	private int scrollPanelHeight;
}
