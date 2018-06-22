package com.maxtree.automotive.dashboard.view.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.Status;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SearchAndPrintWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public SearchAndPrintWindow() {
		this.setResizable(true);
		this.setCaption("查询打印");
		this.setModal(true);
		this.setWidth("700px");
		this.setHeight("495px");
		initComponents();
	}
	
	private void initComponents() {
		VerticalLayout vlayout = new VerticalLayout();
        vlayout.setWidth("100%");
        vlayout.setHeightUndefined();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        Label barCodeLabel = new Label("条形码:");
        TextField barCodeField = new TextField();
        barCodeField.focus();
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("icon-edit");
        btnSearch.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnSearch.setDescription("按照条形码查找");
        btnSearch.addClickListener(e -> {
        	List<Transaction> items = ui.transactionService.findAllByBarCode(barCodeField.getValue());
        	grid.setItems(items);
        });
        ShortcutListener enterListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER,
				null) {
			@Override
			public void handleAction(Object sender, Object target) {
				List<Transaction> items = ui.transactionService.findAllByBarCode(barCodeField.getValue());
	        	grid.setItems(items);
			}
		};
        barCodeField.addShortcutListener(enterListener);
       
        HorizontalLayout inputsHLayout = new HorizontalLayout();
        inputsHLayout.setSpacing(false);
        inputsHLayout.setMargin(false);
        inputsHLayout.addComponents(barCodeLabel, Box.createHorizontalBox(2), barCodeField, Box.createHorizontalBox(2), btnSearch);
        inputsHLayout.setComponentAlignment(barCodeLabel, Alignment.MIDDLE_LEFT);
        inputsHLayout.setComponentAlignment(barCodeField, Alignment.MIDDLE_LEFT);
        inputsHLayout.setComponentAlignment(btnSearch, Alignment.MIDDLE_LEFT);
        
        // provides BugEntries
  		grid.removeAllColumns();
  		grid.setWidth("100%");
  		grid.setHeightUndefined();
  		grid.setSelectionMode(SelectionMode.SINGLE);
  		grid.setItems(new ArrayList<Transaction>());
      	grid.setHeightByRows(7);
//      	grid.addColumn(MessageWrapper::getSenderUserName).setCaption("收自").setRenderer(new ImageNameRenderer());
      	grid.addColumn(Transaction::getBarcode).setCaption("条形码");
      	grid.addColumn(Transaction::getPlateType).setCaption("号牌种类");
      	grid.addColumn(Transaction::getPlateNumber).setCaption("号码号牌");
      	grid.addColumn(Transaction::getVin).setCaption("VIN");
      	grid.addColumn(Transaction::getStatus).setCaption("状态");
      	grid.addColumn(Transaction::getDateCreated).setCaption("创建时间");
      	grid.addColumn(Transaction::getDateModified).setCaption("最近更改时间");
        
        HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
        HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidthUndefined();
		subButtonPane.addComponents(btnPrint, Box.createHorizontalBox(5), btnQuit);
		subButtonPane.setComponentAlignment(btnPrint, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnQuit, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
        
		vlayout.addComponents(inputsHLayout, grid, buttonPane);
		vlayout.setComponentAlignment(inputsHLayout, Alignment.TOP_LEFT);
		vlayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_CENTER);
        
        this.setContent(vlayout);
        
        btnQuit.addClickListener(e->{
        	close();
        });
        btnPrint.addClickListener(e->{
        	Set<Transaction> selected = grid.getSelectedItems();
      	    if (selected.size() > 0) {
      	    	
      	    	List<Transaction> list = new ArrayList<>(selected);
      	    	Transaction selectedTransaction = list.get(0);
      	    	if (selectedTransaction.getStatus().equals(Status.S4.name)) {
      	    		
      	    		// 打印文件标签和车辆标签
      	    		PrintingConfirmationWindow.open("打印确认", selectedTransaction.getTransactionUniqueId()); 
      	    		
      	    	} else if (selectedTransaction.getStatus().equals(Status.ReturnedToThePrint.name) 
      	    			|| selectedTransaction.getStatus().equals(Status.S3.name)) {
      	    		// 打印审核结果单
      	    		PrintingResultsWindow.open("打印确认", selectedTransaction.getTransactionUniqueId()); 
      	    	}
      	    	else {
      	    		
      	    		Notifications.warning("不存在打印。");
      	    	}
      	    	
      	    	
      	    } else {
      	    	Notifications.warning("请选择一条记录。");
      	    }
        });
    }
	
	/**
	 * 
	 * @param user
	 * @param licenseEndDate
	 */
	public static void open(String message, Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        SearchAndPrintWindow w = new SearchAndPrintWindow();
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Grid<Transaction> grid = new Grid<>(Transaction.class);
	private Button btnSearch = new Button();
	private Button btnPrint = new Button("打印");
	private Button btnQuit = new Button("关闭");
}
