package com.maxtree.automotive.dashboard.view.finalcheck;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.view.admin.DataDictionaryType;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class PopupNameSelector extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PopupNameSelector() {
		this.setCaption("拍摄");
		this.setWidth("370px");
		this.setHeightUndefined();
		this.setModal(false);
		this.setClosable(true);
		this.setResizable(false);
		this.setCaption("选择材料名称");
		
		list = ui.dataItemService.findAllByType(DataDictionaryType.MATERIAL);
		namebox.setItems(list);
		namebox.setTextInputAllowed(true);
		namebox.setEmptySelectionAllowed(false);
		namebox.setWidth("100%");
		namebox.setHeight("30px");
		namebox.setCaption("请选择材料名称：");
		
		toolbar.setWidth("100%");
		toolbar.addStyleName("PopupCaptureWindow-toolbar");
		toolbar.addComponents(namebox);
		toolbar.setComponentAlignment(namebox, Alignment.MIDDLE_LEFT);
		
		main.setWidth("100%");
		main.setHeightUndefined();
		main.addComponents(toolbar,ok);
		main.setComponentAlignment(toolbar, Alignment.TOP_LEFT);
		main.setComponentAlignment(ok, Alignment.BOTTOM_RIGHT);
		this.setContent(main);
	}
	
	/**
	 * 
	 * @param trans
	 * @param callback
	 */
	public static void open(Callback2 callback) {
		PopupNameSelector w = new PopupNameSelector();
		UI.getCurrent().addWindow(w);
		w.center();
		w.callback = callback;
		w.namebox.setValue(w.list.get(0));
		w.ok.addClickListener(e->{
			callback.onSuccessful(w.namebox.getValue());
			w.close();
		});
		
//		w.p.setBatch(trans.getBatch()+"");
//		w.p.setDictionaryCode(w.namebox.getValue().getCode());
//		w.p.setSiteCode(trans.getSiteCode());
//		w.p.setUserUniqueId(w.loggedInUser.getUserUniqueId());
//		w.p.setUuid(trans.getUuid());
//		w.p.setVin(trans.getVin());
//		CaptureServlet.IN_DTOs.put(w.loggedInUser.getUserUniqueId(), w.p);
	}
	
	private List<DataDictionary> list;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private ComboBox<DataDictionary> namebox = new ComboBox<>();
	private Button ok = new Button("确定");
	private HorizontalLayout toolbar = new HorizontalLayout();
	private VerticalLayout main = new VerticalLayout();
	private Callback2 callback;
}
