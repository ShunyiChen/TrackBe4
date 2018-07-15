package com.maxtree.automotive.dashboard.view.search;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Tenant;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BasicSearchWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public BasicSearchWindow() {
		this.setWidthUndefined();
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("基本查询");
		
//		User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		mainLayout = new VerticalLayout(); 
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
 
		// 每页显示行数
		displayRowCount = new HorizontalLayout();
		displayRowCount.setSpacing(false);
		displayRowCount.setMargin(false);
		displayRowCount.setWidthUndefined();
		List<Integer> data = new ArrayList<>();
		data.add(20);
		data.add(50);
		data.add(100);
		resultsPerPage = new ComboBox<>(null, data);
		resultsPerPage.setEmptySelectionAllowed(false);
		resultsPerPage.setTextInputAllowed(false);
		resultsPerPage.setSelectedItem(20);
		resultsPerPage.setWidth("145px");
		
		Label txt = new Label("每页显示数据行数:");
		displayRowCount.addComponents(txt, Box.createHorizontalBox(3), resultsPerPage);
		displayRowCount.setComponentAlignment(txt, Alignment.MIDDLE_LEFT);
		displayRowCount.setComponentAlignment(resultsPerPage, Alignment.MIDDLE_LEFT);
		
		FormLayout form = new FormLayout();
		form.setWidthUndefined();
		form.setHeightUndefined();
		
		List<Tenant> items = ui.tenantService.findAllTenants();
		selectList.setItems(items);
		selectList.setEmptySelectionAllowed(false);
		selectList.setTextInputAllowed(false);
		selectList.setCaption("选择租户:");
		selectList.setIcon(VaadinIcons.GROUP);
		
		plateField.setCaption("车牌号:");
		plateField.setIcon(VaadinIcons.CAR);
		Address addr = Yaml.readAddress();
		plateField.setValue(addr.getLicenseplate());
		plateField.focus();
		form.addComponents(selectList, plateField);
		
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
		subButtonPane.addComponents(btnCancel, Box.createHorizontalBox(5), btnSearch);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnSearch, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		mainLayout.addComponents(form, displayRowCount, Box.createVerticalBox(5), buttonPane, Box.createVerticalBox(5));
		mainLayout.setComponentAlignment(form, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(displayRowCount, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		
		selectList.setSelectedItem(items.get(0));
	}
	
	public static void open(ResultCallback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        BasicSearchWindow w = new BasicSearchWindow();
        w.btnSearch.addClickListener(e -> {
//			List<Transaction> results = ui.transactionService.executeBasicSearch(0, w.resultsPerPage.getValue().intValue(), w.selectList.getValue().getTenantName(), w.plateField.getValue());
//			callback.onSuccessful(results);
			w.close();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private HorizontalLayout displayRowCount;
	private ComboBox<Integer> resultsPerPage;
	private ComboBox<Tenant> selectList = new ComboBox<>();
	private TextField plateField = new TextField();
	private HorizontalLayout buttonPane = null;
	private VerticalLayout mainLayout =null;
	private Button btnSearch = new Button("查询");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
