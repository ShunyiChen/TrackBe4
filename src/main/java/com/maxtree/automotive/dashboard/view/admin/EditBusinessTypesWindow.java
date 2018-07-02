package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
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
//		form.setSizeFull();
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
		form.addComponents(nameField, codeField);
		
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
        	if (StringUtils.isEmpty(w.codeField.getValue())
        			|| w.codeField.getValue().length() != 4) {
        		Notifications.warning("快捷编码输入有误，请输入4位字母或数字组合编码。");
        		return;
        	}
        	w.btnAdd.setCaption("添加");
			DashboardUI ui = (DashboardUI) UI.getCurrent();
			Business newInstance = new Business();
			newInstance.setName(w.nameField.getValue());
			newInstance.setFileCheck(w.btnCheck.isSelected() ? 1:0);
			newInstance.setHasFirstIndex(w.btnIndex.isSelected() ? 1:0);
			newInstance.setLocalCheck(w.btnLocalCheck.isSelected() ? 1:0);
			newInstance.setCode(w.codeField.getValue());
			
			
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
        w.btnCheck.setSelected((business.getFileCheck()==1));
        w.btnIndex.setSelected((business.getHasFirstIndex()==1));
        w.btnLocalCheck.setSelected((business.getLocalCheck()==1));
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
        	business.setFileCheck(w.btnCheck.isSelected()?1:0);
        	business.setHasFirstIndex(w.btnIndex.isSelected()?1:0);
        	business.setLocalCheck(w.btnLocalCheck.isSelected()?1:0);
        	business.setCode(w.codeField.getValue());
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
	private TextField nameField; // 业务类型名
	private TextField codeField; // 业务类型快捷编码
	private Button btnAdd;
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
