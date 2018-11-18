package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class LoggingWindow extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoggingWindow() {
		this.setModal(false);
		this.setCaption("日志");
		this.setClosable(true);
		this.setResizable(false);
		this.setWidth("1044px");
		this.setHeight("788px");
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidth("100%");
		toolbar.setHeight("30px");
		toolbar.setSpacing(false);
		toolbar.setMargin(false);
		Sorting sorting = new Sorting();
		Paging paging = new Paging();
		toolbar.addComponents(sorting, paging);
		toolbar.setComponentAlignment(sorting, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(paging, Alignment.MIDDLE_RIGHT);
		
		
		loggingTable = new LoggingTable("dateCreated");
		main.addComponents(Box.createVerticalBox(5),toolbar,loggingTable);
		main.setComponentAlignment(toolbar, Alignment.TOP_CENTER);
		main.setComponentAlignment(loggingTable, Alignment.TOP_CENTER);
		
		main.setWidth("100%");
		main.setHeightUndefined();
//		main.setSpacing(false);
//		main.setMargin(false);
		this.setContent(main);
		
		//查询排序
		Callback2 queryFunction = new Callback2() {
			@Override
			public void onSuccessful(Object... objects) {
				SortItem si = (SortItem) objects[0];
				String desc = objects[1].toString();
				int page = Integer.parseInt(paging.getNumField().getValue()) - 1;
				int offset = (page) * Paging.ROWS_PER_PAGE;
				loggingTable.query(Paging.ROWS_PER_PAGE, offset, si.columnName(),desc);
			}
		};
		sorting.setQueryFunction(queryFunction);
		
		//按页数查询
		Callback2 queryFunction2 = new Callback2() {
			@Override
			public void onSuccessful(Object... objects) {
				Integer page =Integer.parseInt(objects[0].toString());
				loggingTable.query(Paging.ROWS_PER_PAGE, (page-1)*Paging.ROWS_PER_PAGE, sorting.sortBy.getValue().columnName(),sorting.desc.getValue());
			}
		};
		paging.setQueryFunction(queryFunction2);
	}

	/**
	 * 
	 */
	public static void open() {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        final LoggingWindow w = new LoggingWindow();
        UI.getCurrent().addWindow(w);
        w.center();
        UI.getCurrent().access(new Runnable() {

			@Override
			public void run() {
				 w.loggingTable.query(Paging.ROWS_PER_PAGE, 0, null, null);
			}
        });
       
	}
	
	private LoggingTable loggingTable;
	private VerticalLayout main = new VerticalLayout();
}
/**
 * 
 * @author Chen
 *
 */
class Sorting extends HorizontalLayout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sorting() {
		SortItem[] items = {
				new SortItem("日期","DATECREATED"),
				new SortItem("日志类型","LOGTYPE"),
				new SortItem("用户名","USERNAME"),
				new SortItem("IP地址","IPADDR"),
				new SortItem("模块","MODULE"),
				new SortItem("消息","MESSAGE"),
				new SortItem("异常","EXCEPTION")};
		
		sortBy.setWidth("100px");
		sortBy.setHeight("28px");
		sortBy.setItems(items);
		sortBy.setTextInputAllowed(false);
		sortBy.setEmptySelectionAllowed(false);
		sortBy.setSelectedItem(items[0]);
		
		String[] items2 = {"ASC","DESC"};
		desc.setWidth("100px");
		desc.setHeight("28px");
		desc.setItems(items2);
		desc.setTextInputAllowed(false);
		desc.setEmptySelectionAllowed(false);
		desc.setSelectedItem(items2[0]);
		
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidthUndefined();
		Label sortByName = new Label("排序:");
		this.addComponents(sortByName,sortBy,desc);
		this.setComponentAlignment(sortByName, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(sortBy, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(desc, Alignment.MIDDLE_LEFT);
		sortBy.addValueChangeListener(e->{
			if(queryFunction != null) {
				queryFunction.onSuccessful(e.getValue(),desc.getValue());
			}
		});
		desc.addValueChangeListener(e->{
			if(queryFunction != null) {
				queryFunction.onSuccessful(sortBy.getValue(), e.getValue());
			}
		});
	}
	
	/**
	 * 
	 * @param sortEvent
	 */
	public void setQueryFunction(Callback2 queryFunction) {
		this.queryFunction = queryFunction;
	}
	
	private Callback2 queryFunction;
	public ComboBox<SortItem> sortBy = new ComboBox<>();
	public ComboBox<String> desc = new ComboBox<>();
}

/**
 * 
 * @author Chen
 *
 */
class SortItem{
	
	/**
	 * 
	 * @param alias
	 * @param columnName
	 */
	public SortItem(String alias, String columnName) {
		this.alias = alias;
		this.columnName = columnName;
	}
	
	public String toString() {
		return alias;
	}
	
	public String columnName() {
		return columnName;
	}
	
	private String alias;
	private String columnName;
}

/**
 * 
 * @author Chen
 *
 */
class Paging extends HorizontalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Paging() {
		numField.setWidth("60px");
		numField.setHeight("28px");
		
		first.setWidth("28px");
		previous.setWidth("28px");
		next.setWidth("28px");
		last.setWidth("28px");
		
		first.setHeight("28px");
		previous.setHeight("28px");
		next.setHeight("28px");
		last.setHeight("28px");
		
		first.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		previous.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		next.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		last.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		
		first.setIcon(VaadinIcons.ANGLE_DOUBLE_LEFT);
		previous.setIcon(VaadinIcons.ANGLE_LEFT);
		next.setIcon(VaadinIcons.ANGLE_RIGHT);
		last.setIcon(VaadinIcons.ANGLE_DOUBLE_RIGHT);
		
		
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidthUndefined();
		this.addComponents(totalRows,first,previous,numField,numLabel,next,last);
		this.setComponentAlignment(totalRows, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(numField, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(numLabel, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
		
		
		initPagingInfo();
		numField.addValueChangeListener(e->{
			if(queryFunction != null)
				queryFunction.onSuccessful(e.getValue());
		});
		numField.setValue("1");
		
		first.addClickListener(e->{
			numField.setValue("1");
		});
		last.addClickListener(e->{
			numField.setValue(totalPages+"");
		});
		previous.addClickListener(e->{
			int page = (Integer.parseInt(numField.getValue())-1);
			if(page <= 0) {
				page = 1;
			}
			numField.setValue(page+"");
		});
		next.addClickListener(e->{
			int page = (Integer.parseInt(numField.getValue())+1);
			if(page > totalPages) {
				page = totalPages;
			}
			numField.setValue(page+"");
		});
	}
	
	public void setQueryFunction(Callback2 queryFunction) {
		this.queryFunction = queryFunction;
	}
	
	/**
	 * 
	 */
	private void initPagingInfo() {
		int[] info = ui.loggingService.findPagingInfo(ROWS_PER_PAGE);
		totalRows.setValue("总记录数:"+info[0]);
//		numLabel.setValue("/"+info[1]);
	
		totalPages = info[1];
		
		numLabel.setValue("/"+totalPages+"");
	}
	
	/**
	 * 
	 * @return
	 */
	public TextField getNumField() {
		return numField;
	}
	
	private int totalPages;
	private Callback2 queryFunction;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Label totalRows = new Label();
	private TextField numField = new TextField();
	private Label numLabel = new Label();
	private Button first = new Button();
	private Button previous = new Button();
	private Button next = new Button();
	private Button last = new Button();
	public static int ROWS_PER_PAGE = 50;
}
