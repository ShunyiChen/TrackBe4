package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.SystemSettings;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author chens
 *
 */
public class EditSystemSettingsWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditSystemSettingsWindow() {
		this.setClosable(true);
		this.setResizable(true);
//		this.setWidth("1024px");
//		this.setHeight("768px");
		
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新参数");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		
		
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus(); // 设置焦点
		valueField.setIcon(VaadinIcons.VIMEO);
		commentsField.setIcon(VaadinIcons.COMMENT);
		
		form.addComponents(nameField,nameField,valueField,commentsField);
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
		mainLayout.addComponents(form,buttonPane);
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
		binder.setBean(systemSettings);
	}
	
	private void setComponentSize(int w, int h) {
		nameField.setWidth(w+"px");
		valueField.setWidth(w+"px");
		commentsField.setWidth(w+"px");
		nameField.setHeight(h+"px");
		valueField.setHeight(h+"px");
		commentsField.setHeight(h+"px");
	}
	
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(nameField, SystemSettings::getName, SystemSettings::setName);
		binder.bind(valueField, SystemSettings::getValue, SystemSettings::setValue);
		binder.bind(commentsField, SystemSettings::getComments, SystemSettings::setComments);
	}
	
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(valueField).withValidator(new StringLengthValidator(
	        "值字符串长度1~200个字符",
	        1, 200)).bind(SystemSettings::getValue, SystemSettings::setValue);
		
		// Validating Field Values
		binder.forField(commentsField).withValidator(new StringLengthValidator(
	        "注释字符串长度0~200个字符",
	        0, 200)).bind(SystemSettings::getComments, SystemSettings::setComments);
	}
	
	private boolean checkEmptyValues() {
		if(StringUtils.isEmpty(systemSettings.getValue())) {
			Notifications.warning("参数值不能为空");
			return false;
		}
		if (valueField.getErrorMessage() != null) {
			valueField.setComponentError(valueField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param settings
	 * @param callback
	 */
	public static void edit(SystemSettings settings, Callback callback) {
		EditSystemSettingsWindow w = new EditSystemSettingsWindow();
		w.systemSettings.setSettingUniqueId(settings.getSettingUniqueId());
		w.nameField.setValue(settings.getName());
		w.nameField.setEnabled(false);
		
		w.valueField.setValue(settings.getValue());
		w.commentsField.setValue(settings.getComments());
		
		w.btnAdd.setCaption("保存");
        w.setCaption("编辑参数");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.systemSettings.setName(w.nameField.getValue());
        		w.systemSettings.setValue(w.systemSettings.getValue());
        		w.systemSettings.setComments(w.commentsField.getValue()==null?"":w.commentsField.getValue());
        		
    		    ui.settingsService.update(w.systemSettings);
    		    w.close();
    			callback.onSuccessful();
        	}
		});
		UI.getCurrent().addWindow(w);
		w.center();
	}
	
	private TextField nameField = new TextField("参数名:");
	private TextField valueField = new TextField("值:");
	private TextField commentsField = new TextField("注释:");
	private Button btnAdd;
	private Binder<SystemSettings> binder = new Binder<>();
	private SystemSettings systemSettings = new SystemSettings();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
