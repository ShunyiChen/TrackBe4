package com.maxtree.automotive.dashboard.view.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Car;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
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
		this.setWidth("1100px");
		this.setHeight("500px");
		initComponents();
	}
	
	private void initComponents() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		final Community community = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
		VerticalLayout main = new VerticalLayout();
		main.setSizeFull();
        
        Label barCodeLabel = new Label("条形码:");
        TextField barCodeField = new TextField();
        barCodeField.setPlaceholder("请输入条形码");
        barCodeField.focus();
        barCodeField.addFocusListener(e->{
        	ui.setPollInterval(-1);
        });
        this.addCloseListener(e->{
        	SystemConfiguration sc = Yaml.readSystemConfiguration();
    		ui.setPollInterval(sc.getInterval());
        });
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("icon-edit");
        btnSearch.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnSearch.setDescription("按照条形码查找");
        btnSearch.focus();
        btnSearch.addClickListener(e -> {
        	if(StringUtils.isEmpty(barCodeField.getValue())) {
        		Notifications.warning("条形码不能为空");
        		return;
        	}
        	
        	Car car = ui.carService.findByBarcode(barCodeField.getValue());
        	if(car == null) {
        		List<Transaction> items = ui.transactionService.search_by_keyword(-1, 0, barCodeField.getValue(), community.getCommunityName());
            	grid.setItems(items);
        	}
        	else {
        		List<Transaction> items = ui.transactionService.findForList(car.getVin(), 0);
            	grid.setItems(items);
        	}
        	
        });
        ShortcutListener enterListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER,
				null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				if(StringUtils.isEmpty(barCodeField.getValue())) {
	        		Notifications.warning("条形码不能为空");
	        		return;
	        	}
				List<Transaction> items = ui.transactionService.search_by_keyword(-1, 0,barCodeField.getValue(), community.getCommunityName());
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
  		grid.setSizeFull();
  		grid.setSelectionMode(SelectionMode.SINGLE);
  		grid.setItems(new ArrayList<Transaction>());
      	grid.addColumn(Transaction::getBarcode).setCaption("条形码");
      	grid.addColumn(Transaction::getPlateType).setCaption("号牌种类");
      	grid.addColumn(Transaction::getPlateNumber).setCaption("号码号牌");
      	grid.addColumn(Transaction::getVin).setCaption("车辆识别代号");
      	grid.addColumn(Transaction::getStatus).setCaption("状态");
      	grid.addColumn(Transaction::getBusinessName).setCaption("业务名称");
      	grid.addColumn(Transaction::getCreator).setCaption("录入者");
      	grid.addColumn(Transaction::getDateCreated).setCaption("录入时间");
      	
      	btnChoose.addStyleName(ValoTheme.BUTTON_PRIMARY);
      	btnChoose.setClickShortcut(KeyCode.ENTER);
        HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setWidth("100%");
		buttonPane.setHeight("30px");
        HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidthUndefined();
		subButtonPane.setHeight("23px");
		subButtonPane.addComponents(btnChoose, Box.createHorizontalBox(5), btnQuit);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.TOP_RIGHT);
        
		main.addComponents(inputsHLayout, grid, buttonPane);
		main.setExpandRatio(inputsHLayout, 0);
		main.setExpandRatio(grid, 1);
		main.setExpandRatio(buttonPane, 0);
		
        this.setContent(main);
        btnQuit.addClickListener(e->{
        	close();
        });
        
        btnChoose.addClickListener(e->{
        	Set<Transaction> selected = grid.getSelectedItems();
      	    if (selected.size() > 0) {
      	    	
      	    	List<Transaction> list = new ArrayList<>(selected);
      	    	Transaction trans = list.get(0);
      	    	// 注册登记业务
      	    	if(trans.getBusinessName().contains("注册登记")) {
      	    		List<String> options = Arrays.asList("车辆标签", "文件标签");
      	    		PrintingFiletagsWindow.open("车辆和文件标签-打印预览",trans,options);
      	    	}
      	    	else {
      	    		Business bus = ui.businessService.findByCode(trans.getBusinessCode());
      	    		if(bus.getCheckLevel().equals("无")) {
      	    			// 非审档业务
      	    			List<String> options = Arrays.asList("文件标签");
      	    			PrintingFiletagsWindow.open("文件标签-打印预览",trans,options);
      	    		}
      	    		else if(bus.getCheckLevel().equals("一级审档")
      	    				|| bus.getCheckLevel().equals("二级审档")) {//一级审档
          	    		Callback callback = new Callback() {
    						@Override
    						public void onSuccessful() {
    						}
    					};
      	    			PrintingResultsWindow.open("审核结果单-打印预览", trans, callback);
      	    		}
      	    	}
      	    } else {
      	    	Notifications.warning("请至少选择一条记录。");
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
	
	private User loggedInUser;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Grid<Transaction> grid = new Grid<>(Transaction.class);
	private Button btnSearch = new Button();
	private Button btnChoose = new Button("打印预览");
	private Button btnQuit = new Button("关闭");
}
