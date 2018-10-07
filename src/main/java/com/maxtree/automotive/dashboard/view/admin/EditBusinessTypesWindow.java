package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
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
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加业务类型");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setWidth("100%");
		form.setHeightUndefined();
		nameField = new TextField("业务类型名:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.setWidth("350px");
		nameField.setHeight("27px");
		nameField.focus();
		codeField = new TextField("快捷编码:");
		codeField.setIcon(VaadinIcons.CODE);
		codeField.setWidth("350px");
		codeField.setHeight("27px");
		List<String> checkLevelItems = new ArrayList<String>();
		checkLevelItems.add("无");
		checkLevelItems.add("一级审档");
		checkLevelItems.add("二级审档");
		checkLevelBox = new ComboBox<String>("审档级别:", checkLevelItems);
		checkLevelBox.setIcon(VaadinIcons.MALE);
		checkLevelBox.setDescription("一级审档表示本社区本机构内部审档。二级审档表示本社区外部机构审档。");
		checkLevelBox.setWidth("350px");
		checkLevelBox.setHeight("27px");
		checkLevelBox.setEmptySelectionAllowed(false);
		checkLevelBox.setTextInputAllowed(false);
		checkLevelBox.setSelectedItem("一级审档");
		
		form.addComponents(nameField,codeField,checkLevelBox);
		
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
		
		mainLayout.addComponents(form,buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditBusinessTypesWindow w = new EditBusinessTypesWindow();
        w.btnAdd.addClickListener(e -> {
        	if (StringUtils.isEmpty(w.codeField.getValue())
        			|| w.codeField.getValue().length() != 4) {
        		Notifications.warning("快捷编码输入有误，请输入4位字母或数字组合编码。");
        		return;
        	}
        	w.btnAdd.setCaption("添加");
			DashboardUI ui = (DashboardUI) UI.getCurrent();
			Business newInstance = new Business();
			newInstance.setName(w.nameField.getValue());
			newInstance.setCode(w.codeField.getValue());
			newInstance.setCheckLevel(w.checkLevelBox.getValue());
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
        w.nameField.setValue(business.getName());
        w.nameField.focus();
        w.codeField.setValue(business.getCode());
        w.checkLevelBox.setValue(business.getCheckLevel());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑业务类型");
        w.btnAdd.addClickListener(e -> {
        	// 设置新名称
        	if (StringUtils.isEmpty(w.codeField.getValue())
        			|| w.codeField.getValue().length() != 4) {
        		Notifications.warning("快捷编码输入有误，请输入4位字母或数字组合编码。");
        		return;
        	}
        	
        	business.setName(w.nameField.getValue());
        	business.setCheckLevel(w.checkLevelBox.getValue());
        	business.setCode(w.codeField.getValue());
        	ui.businessService.update(business);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField nameField; // 业务类型名
	private TextField codeField; // 业务类型快捷编码
	private ComboBox<String> checkLevelBox;//“”/一级审档/二级审档 ，“”表示非审档；一级审档标识本机构内部审档；二级审档指车管所审档
	private Button btnAdd;
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
