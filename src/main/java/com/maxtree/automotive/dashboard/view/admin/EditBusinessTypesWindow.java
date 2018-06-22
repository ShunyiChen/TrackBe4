package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.SwitchButton;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditBusinessTypesWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditBusinessTypesWindow() {
		this.setWidth("513px");
		this.setHeight("306px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加业务类型");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
//		form.setSizeFull();
		form.setWidth("100%");
		form.setHeight("80px");
		textField = new TextField("业务类型:");
		textField.setIcon(VaadinIcons.EDIT);
		textField.setWidth("370px");
		textField.setHeight("27px");
		textField.focus();
//		tf1.setRequiredIndicatorVisible(true);
		
		form.addComponents(textField);
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("136px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnAdd);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		HorizontalLayout hlayout1 = new HorizontalLayout();
		hlayout1.setWidthUndefined();
		hlayout1.setHeight("40px");
		Image txt1 = new Image("是否需要审档:");
		txt1.setIcon(VaadinIcons.CLIPBOARD_CHECK);
		hlayout1.addComponents(txt1, btnCheck);
		
		HorizontalLayout hlayout2 = new HorizontalLayout();
		hlayout2.setWidthUndefined();
		hlayout2.setHeight("40px");
		Image txt2 = new Image("业务索引号从1开始:");
		txt2.setIcon(VaadinIcons.INDENT);
		hlayout2.addComponents(txt2, btnIndex);
		
		HorizontalLayout hlayout3 = new HorizontalLayout();
		hlayout3.setWidthUndefined();
		hlayout3.setHeight("40px");
		Image txt3 = new Image("是否内部审档:");
		txt3.setIcon(VaadinIcons.DIAMOND);
		hlayout3.addComponents(txt3, btnLocalCheck);
		
		mainLayout.addComponents(form, hlayout1,hlayout2,hlayout3,buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditBusinessTypesWindow w = new EditBusinessTypesWindow();
        w.btnAdd.addClickListener(e -> {
        	w.btnAdd.setCaption("添加");
			DashboardUI ui = (DashboardUI) UI.getCurrent();
			Business newInstance = new Business();
			newInstance.setName(w.textField.getValue());
			newInstance.setFileCheck(w.btnCheck.isSelected() ? 1:0);
			newInstance.setHasFirstIndex(w.btnIndex.isSelected() ? 1:0);
			newInstance.setLocalCheck(w.btnLocalCheck.isSelected() ? 1:0);
			ui.businessService.insert(newInstance);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(Business business, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditBusinessTypesWindow w = new EditBusinessTypesWindow();
        w.textField.setValue(business.getName());
        w.textField.focus();
        w.btnCheck.setSelected((business.getFileCheck() == 1));
        w.btnIndex.setSelected((business.getHasFirstIndex() == 1));
        w.btnLocalCheck.setSelected((business.getLocalCheck()==1));
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑业务类型");
        w.btnAdd.addClickListener(e -> {
        	// 设置新名称
        	business.setName(w.textField.getValue());
        	business.setFileCheck(w.btnCheck.isSelected() ? 1 : 0);
        	business.setHasFirstIndex(w.btnIndex.isSelected() ? 1:0);
        	business.setLocalCheck(w.btnLocalCheck.isSelected()?1:0);
        	ui.businessService.update(business);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private SwitchButton btnCheck = new SwitchButton(false, null,"",SwitchButton.WHITE);
	private SwitchButton btnIndex = new SwitchButton(false, null,"", SwitchButton.WHITE);
	private SwitchButton btnLocalCheck = new SwitchButton(false, null,"", SwitchButton.WHITE);
	private TextField textField;
	private Button btnAdd;
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
