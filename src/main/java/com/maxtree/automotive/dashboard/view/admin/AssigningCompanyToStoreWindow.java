package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
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

public class AssigningCompanyToStoreWindow extends Window {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param storehouse
//	 */
//	public AssigningCompanyToStoreWindow(FrameNumber storehouse) {
//		this.storehouse = storehouse;
//		this.setWidth("613px");
//		this.setHeight("513px");
//		this.setModal(true);
//		this.setResizable(false);
//		this.setCaption("为库房分配机构");
//		this.addStyleName("edit-window");
//		VerticalLayout mainLayout = new VerticalLayout();
//		mainLayout.setSpacing(true);
//		mainLayout.setMargin(false);
//		mainLayout.setWidth("100%");
//		mainLayout.setHeightUndefined();
//
//		Image img = new Image(null);
//		img.setIcon(VaadinIcons.GROUP);
//		Label storeName = new Label(storehouse.getStorehouseName());
//		HorizontalLayout title = new HorizontalLayout();
//		title.setWidthUndefined();
//		title.setHeightUndefined();
//		title.setSpacing(false);
//		title.setMargin(false);
//		title.addComponents(img, Box.createHorizontalBox(5), storeName);
//		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
//		title.setComponentAlignment(storeName, Alignment.MIDDLE_LEFT);
//
//		HorizontalLayout hlayout = new HorizontalLayout();
//		hlayout.setSizeFull();
//		hlayout.setSpacing(false);
//		hlayout.setMargin(false);
//
//		List<Company> allCompanies = ui.frameService.getAvailableCompanies(storehouse.getStorehouseName());
//		select = new TwinColSelect<>(null, allCompanies);
//		select.setWidth("100%");
//		select.setRows(14);
//		select.setLeftColumnCaption("未分配的机构");
//		select.setRightColumnCaption("已分配的机构");
//
//		List<Company> selectedCompanies = new ArrayList<>();
//		assignedCompany = ui.frameService.findAssignedCompany(storehouse.getStorehouseName());
//		for (Company com : allCompanies) {
//			if (assignedCompany.getCompanyUniqueId().intValue() == com.getCompanyUniqueId().intValue()) {
//				selectedCompanies.add(com);
//			}
//		}
//		// set select
//		select.select(selectedCompanies.toArray(new Company[selectedCompanies.size()]));
//
//		hlayout.addComponent(select);
//        hlayout.setComponentAlignment(select, Alignment.TOP_CENTER);
//
//		HorizontalLayout buttonPane = new HorizontalLayout();
//		buttonPane.setSizeFull();
//		buttonPane.setSpacing(false);
//		buttonPane.setMargin(false);
//		Button btnCancel = new Button("取消");
//		btnOK = new Button("确定");
//		btnApply = new Button("应用");
//		HorizontalLayout subButtonPane = new HorizontalLayout();
//		subButtonPane.setSpacing(false);
//		subButtonPane.setMargin(false);
//		subButtonPane.setWidth("196px");
//		subButtonPane.setHeight("100%");
//		subButtonPane.addComponents(btnCancel, btnOK, btnApply);
//		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_RIGHT);
//		subButtonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_RIGHT);
//		subButtonPane.setComponentAlignment(btnApply, Alignment.BOTTOM_RIGHT);
//		buttonPane.addComponent(subButtonPane);
//		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
//		mainLayout.addComponents(title, hlayout, buttonPane);
//		this.setContent(mainLayout);
//
//		btnCancel.addClickListener(e -> {
//			close();
//		});
//	}
//
//	private boolean apply() {
//		Set<Company> set = select.getSelectedItems();
//		List<Company> list = new ArrayList<>(set);
//		if (list.size() > 1) {
//			Notifications.warning("一个库房只能分配一个机构。");
//			return false;
//		}
//		//取消机构
//		else if(list.size() == 0) {
//			if (assignedCompany != null) {
//				assignedCompany.setStorehouseName(null);
//				ui.companyService.updateStorehouse(assignedCompany);
//			}
//		}
//		//保存设置
//		else {
//			for(Company com : list) {
//				com.setStorehouseName(storehouse.getStorehouseName());
//				// update database
//				ui.companyService.updateStorehouse(com);
//			}
//		}
//		return true;
//	}
//
//	public static void open(FrameNumber storehouse, Callback callback) {
////        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//		AssigningCompanyToStoreWindow w = new AssigningCompanyToStoreWindow(storehouse);
//        w.btnApply.addClickListener(e -> {
//        	if(w.apply())
//        		callback.onSuccessful();
//        });
//        w.btnOK.addClickListener(e -> {
//        	if(w.apply()) {
//        		w.close();
//    			callback.onSuccessful();
//        	}
//		});
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private Company assignedCompany;
//	private Button btnApply;
//	private Button btnOK;
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private TwinColSelect<Company> select;
//	private FrameNumber storehouse = new FrameNumber();
}
