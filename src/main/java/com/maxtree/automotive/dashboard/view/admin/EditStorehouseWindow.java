package com.maxtree.automotive.dashboard.view.admin;

import java.util.UUID;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Storehouse;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

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
		bindFields();
		
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
	
	/**
	 * 
	 */
	private void bindFields(){
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(nameField, Storehouse::getName, Storehouse::setName);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues(){
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "库房名称长度范围在1~20个字符",
	        1, 20)) .bind(Storehouse::getName, Storehouse::setName);
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
		return true;
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditStorehouseWindow w = new EditStorehouseWindow();
        int serialNumber = ui.storehouseService.findNextSerialnumberOfStorehouse();
        w.storehouse.setSerialNumber(serialNumber);
        w.btnAdd.setCaption("添加库房");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.storehouseService.insertStorehouse(w.storehouse);
    			w.close();
    			callback.onSuccessful();
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
	public static void edit(Storehouse s, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditStorehouseWindow w = new EditStorehouseWindow();
        w.storehouse.setStorehouseUniqueId(s.getStorehouseUniqueId());
        w.storehouse.setSerialNumber(s.getSerialNumber());
        w.storehouse.setCompanyUniqueId(s.getCompanyUniqueId());
        w.nameField.setValue(s.getName());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑库房");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.storehouseService.updateStorehouse(w.storehouse);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField nameField;
	private Button btnAdd;
	private Binder<Storehouse> binder = new Binder<>();
	private Storehouse storehouse = new Storehouse();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
