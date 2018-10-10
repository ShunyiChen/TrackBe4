package com.maxtree.automotive.dashboard.view.imaging;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		community = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
		this.setWidth("700px");
		this.setHeight("450px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("基本查询");
		mainLayout = new VerticalLayout(); 
		mainLayout.setSizeFull();
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidthUndefined();
		toolbar.setHeight("30px");
		btnSearch.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
		btnSearch.setIcon(VaadinIcons.SEARCH);
		Address addr = Yaml.readAddress();
		plateField.setPlaceholder("请输入车牌号");
		plateField.setValue(addr.getLicenseplate());
		plateField.focus();
		ShortcutListener keyListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				doSearch();
			}
		};
		plateField.addShortcutListener(keyListener);
		
		Label fieldName = new Label(VaadinIcons.CAR.getHtml()+"车牌号:");
		fieldName.setContentMode(ContentMode.HTML);
		toolbar.addComponents(fieldName,plateField,btnSearch);
		toolbar.setComponentAlignment(fieldName, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(plateField, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(btnSearch, Alignment.MIDDLE_LEFT);
		
		grid.setSizeFull();
		grid.addColumn(Transaction::getPlateType).setCaption("号牌种类");
		grid.addColumn(Transaction::getPlateNumber).setCaption("号码号牌");
		grid.addColumn(Transaction::getVin).setCaption("车辆识别代码");
		grid.addColumn(Transaction::getBusinessName).setCaption("业务名称");
		grid.addColumn(Transaction::getStatus).setCaption("状态");
		// Set the selection mode
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addItemClickListener(e->{
        	if(e.getMouseEventDetails().isDoubleClick()) {
        		close();
            	Set<Transaction> set = new HashSet<>();
            	set.add(e.getItem());
    			callback.onSuccessful(new ArrayList<Transaction>(set));
        	}
        });
		
		// 按钮
		buttonPane = new HorizontalLayout();
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
		mainLayout.addComponents(toolbar,grid,buttonPane);
		mainLayout.setExpandRatio(toolbar, 0);
		mainLayout.setExpandRatio(grid, 1);
		mainLayout.setExpandRatio(buttonPane, 0);
		
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		btnSearch.addClickListener(e -> {
			doSearch();
		});
	}
	
	private void doSearch() {
		List<Transaction> rs = ui.transactionService.searchByKeyword(20, 0, plateField.getValue(), community.getCommunityName());
		setPerPageData(rs);
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
        w.callback = callback;
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
	
	private Community community;
	private User loggedInUser;
	private ResultCallback callback;
	private Grid<Transaction> grid = new Grid<>();
	private TextField plateField = new TextField();
	private HorizontalLayout buttonPane = null;
	private VerticalLayout mainLayout =null;
	private Button btnSearch = new Button();
	private Button btnOK = new Button("确定");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
