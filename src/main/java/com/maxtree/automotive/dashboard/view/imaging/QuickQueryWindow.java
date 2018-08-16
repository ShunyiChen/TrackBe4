package com.maxtree.automotive.dashboard.view.imaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class QuickQueryWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public QuickQueryWindow() {
		this.setWidth("700px");
		this.setHeight("450px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("基本查询");
		
		mainLayout = new VerticalLayout(); 
		mainLayout.setSizeFull();
 
		FormLayout form = new FormLayout();
		form.setSizeFull();
		btnSearch.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
		btnSearch.setIcon(VaadinIcons.SEARCH);
		plateField.setIcon(VaadinIcons.CAR);
		Address addr = Yaml.readAddress();
		plateField.setValue(addr.getLicenseplate());
		plateField.focus();
		form.addComponents(plateField, btnSearch);
		
		grid.setSizeFull();
		grid.addColumn(Transaction::getPlateType).setCaption("号牌种类");
		grid.addColumn(Transaction::getPlateNumber).setCaption("号码号牌");
		grid.addColumn(Transaction::getVin).setCaption("车辆识别代码");
		grid.addColumn(Transaction::getStatus).setCaption("状态");
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.SINGLE);
		
		// 按钮
		buttonPane = new HorizontalLayout();
		buttonPane.setWidth("90%");
		buttonPane.setHeight("30px");
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidthUndefined();
		subButtonPane.setHeightUndefined();
		subButtonPane.addComponents(btnCancel, Box.createHorizontalBox(5), btnOK);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		mainLayout.addComponents(form,grid,buttonPane,Box.createVerticalBox(5));
		mainLayout.setComponentAlignment(form, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		btnSearch.addClickListener(e -> {
			List<Transaction> rs = ui.transactionService.findAll(20, 0, plateField.getValue());
			setPerPageData(rs);
		});
	}
	
	/**
	 * 
	 * @param perPageData
	 */
	private void setPerPageData(List<Transaction> perPageData) {
    	grid.setItems(perPageData);
    }
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(ResultCallback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        QuickQueryWindow w = new QuickQueryWindow();
        w.btnOK.addClickListener(e -> {
			Set<Transaction> set = w.grid.getSelectedItems();
			if (set.size() > 0) {
				w.close();
				callback.onSuccessful(new ArrayList<Transaction>(set));
			}
			else {
				Notifications.warning("请选择一行记录。");
			}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Grid<Transaction> grid = new Grid();
	private TextField plateField = new TextField("车牌号:");
	private HorizontalLayout buttonPane = null;
	private VerticalLayout mainLayout =null;
	private Button btnSearch = new Button();
	private Button btnOK = new Button("确定");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
