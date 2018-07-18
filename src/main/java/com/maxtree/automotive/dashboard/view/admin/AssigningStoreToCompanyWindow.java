package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AssigningStoreToCompanyWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param company
	 */
	public AssigningStoreToCompanyWindow(Company company) {
		this.company = company;
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为机构分配库房");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
 
		Image img = new Image(null, VaadinIcons.GROUP);
		Label companyName = new Label(company.getCompanyName());
		HorizontalLayout title = new HorizontalLayout();
		title.setWidthUndefined();
		title.setHeightUndefined();
		title.setSpacing(false);
		title.setMargin(false);
		title.addComponents(img, Box.createHorizontalBox(5), companyName);
		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		title.setComponentAlignment(companyName, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		
		List<FrameNumber> allStores = ui.companyService.getAvailableStores(company.getCompanyUniqueId());
		select = new TwinColSelect<>(null, allStores);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的库");
		select.setRightColumnCaption("已分配的库");
		
		List<FrameNumber> selectedStored = new ArrayList<>();
		assignedStore = ui.companyService.findAssignedStores(company.getStorehouseUniqueId());
		for (FrameNumber store : allStores) {
			if (assignedStore.getFrameUniqueId().intValue() == store.getFrameUniqueId().intValue()) {
				selectedStored.add(store);
			}
		}
		// set select
		select.select(selectedStored.toArray(new FrameNumber[selectedStored.size()]));
		
		hlayout.addComponent(select);
        hlayout.setComponentAlignment(select, Alignment.TOP_CENTER);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnOK = new Button("确定");
		btnApply = new Button("应用");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("196px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnOK, btnApply);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_RIGHT);
		subButtonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_RIGHT);
		subButtonPane.setComponentAlignment(btnApply, Alignment.BOTTOM_RIGHT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(title, hlayout, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}
	
	private boolean apply() {
		Set<FrameNumber> set = select.getSelectedItems();
		List<FrameNumber> list = new ArrayList<>(set);
		if (list.size() > 1) {
			Notifications.warning("一个机构只能分配一个库房。");
			return false;
		} 
		//取消机构
		else if(list.size() == 0) {
			company.setStorehouseUniqueId(0);
			ui.companyService.updateStorehouse(company);
		}
		//保存设置
		else {
			for(FrameNumber store : list) {
				company.setStorehouseUniqueId(store.getFrameUniqueId());
				// update database
				ui.companyService.updateStorehouse(company);
			}
		}
		return true;
	}
	
	public static void open(Company company, Callback callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		AssigningStoreToCompanyWindow w = new AssigningStoreToCompanyWindow(company);
        w.btnApply.addClickListener(e -> {
        	if(w.apply())
        		callback.onSuccessful();
        });
        w.btnOK.addClickListener(e -> {
        	if(w.apply()) {
        		w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private FrameNumber assignedStore;
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TwinColSelect<FrameNumber> select;
	private Company company = new Company();
}
