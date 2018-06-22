package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.DataItem;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

public class EditDataItemWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditDataItemWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加项目");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSizeFull();
		
		categorys.setEmptySelectionAllowed(false);
		categorys.setCaption("分类:");
		categorys.setItems(items);
		categorys.setTextInputAllowed(false);
		categorys.setIcon(VaadinIcons.DATABASE);
		
		nameField = new TextField("项目名:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus(); // 设置焦点
		
//		tf1.setRequiredIndicatorVisible(true);
		form.addComponents(categorys, nameField);
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("128px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnAdd);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(form, buttonPane);
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
		binder.setBean(dataItem);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		nameField.setWidth(w+"px");
		categorys.setWidth(w+"px");
		nameField.setHeight(h+"px");
		categorys.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(categorys, DataItem::getCategoryName, DataItem::setCategoryName);
		binder.bind(nameField, DataItem::getItemName, DataItem::setItemName);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "项目名长度范围在2~20个字符",
	        2, 20)) .bind(DataItem::getItemName, DataItem::setItemName);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(dataItem.getItemName())) {
			Notification notification = new Notification("提示：", "项目名不能为空", Type.WARNING_MESSAGE);
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
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDataItemWindow w = new EditDataItemWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.dataItemService.create(w.dataItem);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(DataItem dataItem, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDataItemWindow w = new EditDataItemWindow();
        DataItem r = ui.dataItemService.findById(dataItem.getDataItemUniqueId());
        w.dataItem.setDataItemUniqueId(r.getDataItemUniqueId());
        w.categorys.setValue(r.getCategoryName());
        w.nameField.setValue(r.getItemName());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑数据项");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.dataItemService.update(w.dataItem);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private ComboBox<String> categorys = new ComboBox<String>();
	private TextField nameField;
	private Button btnAdd;
	private Binder<DataItem> binder = new Binder<>();
	private DataItem dataItem = new DataItem();
	private String[] items = {"号牌种类", "业务材料"};
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
