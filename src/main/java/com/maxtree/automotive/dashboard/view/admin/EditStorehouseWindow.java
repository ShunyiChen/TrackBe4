package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author chens
 *
 */
public class EditStorehouseWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditStorehouseWindow() {
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加库房");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		FormLayout form = new FormLayout();
		form.setSizeFull();
		nameField = new TextField("库房名:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus(); //设置焦点
		form.addComponents(nameField);
		
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidthUndefined();
		subButtonPane.setHeightUndefined();
		subButtonPane.addComponents(btnCancel,Box.createHorizontalBox(3),btnAdd);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		mainLayout.addComponents(form, subButtonPane);
		mainLayout.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		setComponentSize(350, 27);
		
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
//		bindFields();
		
		// Validating Field Values
		validatingFieldValues();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(storehouse);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		nameField.setWidth(w+"px");
		nameField.setHeight(h+"px");
	}
	
//	/**
//	 * 
//	 */
//	private void bindFields(){
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		binder.bind(nameField, FrameNumber::getStorehouseName, FrameNumber::setStorehouseName);
//	}
	
	/**
	 * 
	 */
	private void validatingFieldValues(){
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "库房名称长度范围在1~20个字符",
	        1, 20)) .bind(FrameNumber::getStorehouseName, FrameNumber::setStorehouseName);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(nameField.getValue())) {
			Notification notification = new Notification("提示：", "库房名不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		if (nameField.getErrorMessage() != null) {
			nameField.setComponentError(nameField.getErrorMessage());
			return false;
		}
		
		
		List<FrameNumber> list = ui.frameService.findAllStorehouse();
		for(FrameNumber f : list) {
			if (f.getStorehouseName().equals(nameField.getValue())) {
				Notifications.warning("库房名"+nameField.getValue()+"已存在，请重新命名。");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(Callback2 callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditStorehouseWindow w = new EditStorehouseWindow();
        w.btnAdd.setCaption("添加库房");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			int frameUniqueId = ui.frameService.insert(w.storehouse);
    			w.close();
    			callback.onSuccessful(frameUniqueId);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param storehouse
	 * @param callback
	 */
	public static void edit(FrameNumber store, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditStorehouseWindow w = new EditStorehouseWindow();
        w.nameField.setValue(store.getStorehouseName());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑库房");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		
        		
    			ui.frameService.updateStorehouse(w.storehouse, store.getStorehouseName());
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField nameField;
	private Button btnAdd;
	private Binder<FrameNumber> binder = new Binder<>();
	private FrameNumber storehouse = new FrameNumber();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	
	
}
