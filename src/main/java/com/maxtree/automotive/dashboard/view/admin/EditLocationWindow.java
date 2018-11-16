package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Location;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class EditLocationWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditLocationWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新地址");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSizeFull();
		String[] items = {"省份","地级市","市、县级市"};
		categoryBox.setItems(items);
		categoryBox.setTextInputAllowed(false);
		categoryBox.setEmptySelectionAllowed(false);
		categoryBox.setSelectedItem("省");
		categoryBox.setIcon(VaadinIcons.WORKPLACE);
		
		nameField = new TextField("名称:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus();
		// 设置焦点
		codeField = new TextField("编号:");
		codeField.setIcon(VaadinIcons.CODE);
		form.addComponents(categoryBox,nameField,codeField);
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
//		bindFields();
		
		// Validating Field Values
		validatingFieldValues();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(location);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		categoryBox.setWidth(w+"px");
		nameField.setWidth(w+"px");
		codeField.setWidth(w+"px");
		categoryBox.setHeight(h+"px");
		nameField.setHeight(h+"px");
		codeField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
//	private void bindFields() {
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		binder.bind(nameField, Location::getItemName, Location::setItemName);
//		binder.bind(codeField, Location::getCode, Location::setCode);
//	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "项目名长度范围在2~60个字符",
	        1, 20)) .bind(Location::getName, Location::setName);
		binder.forField(codeField).withValidator(new StringLengthValidator(
	        "快捷编码长度为1-60个字符",
	        1, 7)) .bind(Location::getCode, Location::setCode);
//		binder.forField(orderField)
//		  .withValidator(new EmptyValidator("顺序号不能为空"))
//		  .withConverter(new StringToFloatConverter("请输入一个浮点数"))
//		  .bind(Location::getOrderNumber, Location::setOrderNumber);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(location.getName())) {
			nameField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "项目名不能为空。";
				}
			});
		}
		else if (StringUtils.isEmpty(location.getCode())) {
			codeField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "地址编号不能为空。请输入地区行政编号。";
				}
			});
		}
		else if (nameField.getErrorMessage() != null) {
			nameField.setComponentError(nameField.getErrorMessage());
			return false;
		}
		else if (codeField.getErrorMessage() != null) {
			codeField.setComponentError(codeField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param callback
	 * @param type
	 */
	public static void open(Callback2 callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		EditLocationWindow w = new EditLocationWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.location.setCategory(w.categoryBox.getValue());
        		
    			int locationuniqueid = ui.locationService.insert(w.location);
    			w.close();
    			callback.onSuccessful(locationuniqueid);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param l
	 * @param callback
	 */
	public static void edit(Location l, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditLocationWindow w = new EditLocationWindow();
        Location r = ui.locationService.findById(l.getLocationUniqueId());
        w.location.setLocationUniqueId(r.getLocationUniqueId());
        w.categoryBox.setValue(r.getCategory());
        w.nameField.setValue(r.getName());
        w.codeField.setValue(r.getCode());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑地址");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		w.location.setCategory(w.categoryBox.getValue());
        		w.location.setName(w.nameField.getValue());
        		w.location.setCode(w.codeField.getValue());
        		
    			ui.locationService.update(w.location);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private ComboBox<String> categoryBox = new ComboBox<>("分类:");
	private TextField nameField;
	private TextField codeField;
	private Button btnAdd;
	private Binder<Location> binder = new Binder<>();
	private Location location = new Location();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
