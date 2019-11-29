package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class AssigningDataitemToBusinessWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param business
//	 */
//	public AssigningDataitemToBusinessWindow(Business business) {
//		this.setWidth("1024px");
//		this.setHeight("513px");
//		this.setModal(true);
//		this.setResizable(false);
//		this.setCaption("为业务类型分配材料");
//		this.addStyleName("edit-window");
//
//		List<DataDictionary> allItems = ui.dataItemService.findAllByType(3);
//		List<DataDictionary> assignItems = ui.businessService.assignedItems(business.getCode());
//		List<DataDictionary> assignItems2 = ui.businessService.assignedItems2(business.getCode());
//		twinColSelect = new TwinColSelect<>(null, allItems);
//		twinColSelect.setWidth("100%");
//		twinColSelect.setRows(14);
//		twinColSelect.setLeftColumnCaption("未分配的材料");
//		twinColSelect.setRightColumnCaption("已分配的材料");
//		List<DataDictionary> selectedItems = new ArrayList<>();
//		for (DataDictionary item : allItems) {
//			for (DataDictionary assignItem : assignItems) {
//				if (assignItem.getDictionaryUniqueId().intValue() == item.getDictionaryUniqueId().intValue()) {
//					selectedItems.add(item);
//				}
//			}
//		}
//		// set select
//		twinColSelect.select(selectedItems.toArray(new DataDictionary[selectedItems.size()]));
//
//
//		twinColSelect2 = new TwinColSelect<>(null, allItems);
//		twinColSelect2.setWidth("100%");
//		twinColSelect2.setRows(14);
//		twinColSelect2.setLeftColumnCaption("未分配的材料");
//		twinColSelect2.setRightColumnCaption("已分配的材料");
//		List<DataDictionary> selectedItems2 = new ArrayList<>();
//		for (DataDictionary item : allItems) {
//			for (DataDictionary assignItem : assignItems2) {
//				if (assignItem.getDictionaryUniqueId().intValue() == item.getDictionaryUniqueId().intValue()) {
//					selectedItems2.add(item);
//				}
//			}
//		}
//		// set select
//		twinColSelect2.select(selectedItems2.toArray(new DataDictionary[selectedItems2.size()]));
//
//
//		//tab sheet
//		TabSheet tabSheet = new TabSheet();
//		tabSheet.setSizeFull();
//		tabSheet.addTab(createTab(business, true, twinColSelect),"必录材料");
//		tabSheet.addTab(createTab(business, false, twinColSelect2),"选录材料");
//
//		//button pane
//		HorizontalLayout buttonPane = new HorizontalLayout();
//		buttonPane.setWidth("98%");
//		buttonPane.setHeight("40px");
//		Button btnCancel = new Button("取消");
//		btnOK = new Button("确定");
//		btnApply = new Button("应用");
//		HorizontalLayout subButtonPane = new HorizontalLayout();
//		subButtonPane.setWidthUndefined();
//		subButtonPane.setHeight("38px");
//		subButtonPane.addComponents(btnCancel, btnOK, btnApply);
//		subButtonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_CENTER);
//		subButtonPane.setComponentAlignment(btnOK, Alignment.MIDDLE_CENTER);
//		subButtonPane.setComponentAlignment(btnApply, Alignment.MIDDLE_CENTER);
//		buttonPane.addComponent(subButtonPane);
//		buttonPane.setComponentAlignment(subButtonPane, Alignment.MIDDLE_RIGHT);
//
//		main.setSpacing(true);
//		main.setMargin(false);
//		main.setSizeFull();
//		main.addComponents(tabSheet,buttonPane);
//		main.setExpandRatio(tabSheet, 1.0f);
//		main.setExpandRatio(buttonPane, 0.0f);
//		this.setContent(main);
//
//		btnCancel.addClickListener(e->{
//			close();
//		});
////		btnOK.addClickListener(e->{
////			close();
////		});
////		btnApply.addClickListener(e->{
////			close();
////		});
//
//	}
//
//	/**
//	 *
//	 * @param business
//	 * @param inputRequired
//	 * @return
//	 */
//	private Component createTab(Business business, boolean inputRequired, TwinColSelect<DataDictionary> twinColSelect) {
//		/// Tab1
//		VerticalLayout tab1 = new VerticalLayout();
//		tab1.setSizeFull();
//		tab1.setSpacing(false);
//		tab1.setMargin(false);
//		Image img = new Image(null, new ThemeResource("img/adminmenu/scanentry.png"));
//		Label materialName = new Label(business.getName());
//		HorizontalLayout title = new HorizontalLayout();
//		title.setWidthUndefined();
//		title.setHeightUndefined();
//		title.setSpacing(false);
//		title.setMargin(false);
//		title.addComponents(img, Box.createHorizontalBox(5), materialName);
//		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
//		title.setComponentAlignment(materialName, Alignment.MIDDLE_LEFT);
//		HorizontalLayout hlayout = new HorizontalLayout();
//		hlayout.setSizeFull();
//		hlayout.setSpacing(false);
//		hlayout.setMargin(false);
//
//        hlayout.addComponent(twinColSelect);
//        hlayout.setComponentAlignment(twinColSelect, Alignment.TOP_CENTER);
//		tab1.addComponents(title, hlayout);
//		tab1.setExpandRatio(title, 0.0f);
//		tab1.setExpandRatio(hlayout, 1.0f);
//		return tab1;
//	}
//
//
//	/**
//	 *
//	 * @param business
//	 */
//	private void apply(Business business) {
//		Set<DataDictionary> set = twinColSelect.getSelectedItems();
//		List<DataDictionary> list = new ArrayList<>(set);
//
//		Set<DataDictionary> set2 = twinColSelect2.getSelectedItems();
//		List<DataDictionary> list2 = new ArrayList<>(set2);
//		// update database
//		ui.businessService.updateDataItems(business.getCode(),list,list2);
//	}
//
//	public static void open(Callback callback, Business b) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        AssigningDataitemToBusinessWindow w = new AssigningDataitemToBusinessWindow(b);
//        w.btnApply.addClickListener(e -> {
//        	w.apply(b);
//			callback.onSuccessful();
//		});
//        w.btnOK.addClickListener(e -> {
//        	w.apply(b);
//			w.close();
//			callback.onSuccessful();
//		});
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private VerticalLayout main = new VerticalLayout();
//	private TwinColSelect<DataDictionary> twinColSelect;
//	private TwinColSelect<DataDictionary> twinColSelect2;
//	private Button btnApply;
//	private Button btnOK;
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();

}
