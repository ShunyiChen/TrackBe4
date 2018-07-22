package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AssigningDataitemToBusinessWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param material
	 */
	public AssigningDataitemToBusinessWindow(Business business) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为业务类型分配材料");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label materialName = new Label(business.getName());
		HorizontalLayout title = new HorizontalLayout();
		title.setWidthUndefined();
		title.setHeightUndefined();
		title.setSpacing(false);
		title.setMargin(false);
		title.addComponents(img, Box.createHorizontalBox(5), materialName);
		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		title.setComponentAlignment(materialName, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		
		List<DataDictionary> allItems = ui.dataItemService.findAllByType(3);
		List<DataDictionary> assignItems = ui.businessService.assignedItems(business.getCode());
		select = new TwinColSelect<>(null, allItems);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的材料");
		select.setRightColumnCaption("已分配的材料");
		List<DataDictionary> selectedItems = new ArrayList<>();
		for (DataDictionary item : allItems) {
			for (DataDictionary assignItem : assignItems) {
				if (assignItem.getDictionaryUniqueId().intValue() == item.getDictionaryUniqueId().intValue()) {
					selectedItems.add(item);
				}
			}
		}
		// set select
		select.select(selectedItems.toArray(new DataDictionary[selectedItems.size()]));
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
	
	private void apply(Business business) {
		Set<DataDictionary> set = select.getSelectedItems();
		List<DataDictionary> list = new ArrayList<>(set);
		// update database
		ui.businessService.updateDataItems(business.getCode(), list);
	}
	
	public static void open(Callback callback, Business b) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningDataitemToBusinessWindow w = new AssigningDataitemToBusinessWindow(b);
        w.btnApply.addClickListener(e -> {
        	w.apply(b);
			callback.onSuccessful();
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(b);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TwinColSelect<DataDictionary> select = null;
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();

}
