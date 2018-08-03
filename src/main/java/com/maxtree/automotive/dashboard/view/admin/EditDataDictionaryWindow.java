package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
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

public class EditDataDictionaryWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditDataDictionaryWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加字典");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSizeFull();
		DataDictionaryType type1 = new DataDictionaryType(1, "号牌种类"); 
		DataDictionaryType type2 = new DataDictionaryType(2, "地区代号"); 
		DataDictionaryType type3 = new DataDictionaryType(2, "业务材料"); 
		types.add(type1);
		types.add(type2);
		types.add(type3);
		typeField.setEmptySelectionAllowed(false);
		typeField.setCaption("分类:");
		typeField.setItems(types);
		typeField.setTextInputAllowed(false);
		typeField.setIcon(VaadinIcons.DATABASE);
		typeField.addValueChangeListener(e-> {
			typeField.setComponentError(null);
		});
		typeField.setSelectedItem(type1);
		
		nameField = new TextField("字典名:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus();
		// 设置焦点
		codeField = new TextField("代号:");
		codeField.setIcon(VaadinIcons.CODE);
		
		//顺序
		orderField = new DoubleField();
		orderField.setCaption("顺序号:");
		orderField.setIcon(VaadinIcons.CALC);
		
		form.addComponents(typeField, nameField, codeField, orderField);
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
		binder.setBean(dict);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		nameField.setWidth(w+"px");
		typeField.setWidth(w+"px");
		codeField.setWidth(w+"px");
		orderField.setWidth(w+"px");
		
		nameField.setHeight(h+"px");
		typeField.setHeight(h+"px");
		codeField.setHeight(h+"px");
		orderField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
//	private void bindFields() {
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		binder.bind(nameField, DataDictionary::getItemName, DataDictionary::setItemName);
//		binder.bind(codeField, DataDictionary::getCode, DataDictionary::setCode);
//	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "项目名长度范围在2~20个字符",
	        1, 20)) .bind(DataDictionary::getItemName, DataDictionary::setItemName);
		binder.forField(codeField).withValidator(new StringLengthValidator(
		        "快捷编码长度为1-7个字符",
		        1, 7)) .bind(DataDictionary::getCode, DataDictionary::setCode);
		binder.forField(orderField)
		  .withValidator(new EmptyValidator("顺序号不能为空"))
		  .withConverter(new StringToFloatConverter("请输入一个浮点数"))
		  .bind(DataDictionary::getOrderNumber, DataDictionary::setOrderNumber);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(typeField.getValue())) {
			typeField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					// TODO Auto-generated method stub
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					// TODO Auto-generated method stub
					return "请选择一个分类。";
				}
			});
		}
		else if (StringUtils.isEmpty(dict.getItemName())) {
			nameField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					// TODO Auto-generated method stub
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					// TODO Auto-generated method stub
					return "项目名不能为空。";
				}
			});
		}
		else if (StringUtils.isEmpty(dict.getCode())) {
			codeField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					// TODO Auto-generated method stub
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					// TODO Auto-generated method stub
					return "快捷编码不能为空。请输入字母、数字或字母数字组合的4位编码。";
				}
			});
		}
		else if (StringUtils.isEmpty(dict.getOrderNumber())) {
			orderField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					// TODO Auto-generated method stub
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					// TODO Auto-generated method stub
					return "顺序号不能为空。请输入一个浮点型数字。";
				}
			});
		}
		
		if (typeField.getErrorMessage() != null) {
			nameField.setComponentError(nameField.getErrorMessage());
			return false;
		}
		else if (nameField.getErrorMessage() != null) {
			nameField.setComponentError(nameField.getErrorMessage());
			return false;
		}
		else if (codeField.getErrorMessage() != null) {
			codeField.setComponentError(codeField.getErrorMessage());
			return false;
		}
		else if (orderField.getErrorMessage() != null) {
			orderField.setComponentError(orderField.getErrorMessage());
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
        EditDataDictionaryWindow w = new EditDataDictionaryWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.dict.setItemType(w.typeField.getValue().getType());
        		w.dict.setOrderNumber(Float.parseFloat(w.orderField.getValue().toString()));
    			ui.dataItemService.insert(w.dict);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param dataItem
	 * @param callback
	 */
	public static void edit(DataDictionary dataItem, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDataDictionaryWindow w = new EditDataDictionaryWindow();
        DataDictionary r = ui.dataItemService.findById(dataItem.getDictionaryUniqueId());
        w.dict.setDictionaryUniqueId(r.getDictionaryUniqueId());
        for(DataDictionaryType type : w.types) {
        	if(type.getType() == r.getItemType()) {
        		w.typeField.setValue(type);
        	}
        }
        w.nameField.setValue(r.getItemName());
        w.codeField.setValue(r.getCode());
        w.orderField.setValue(r.getOrderNumber()+"");
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑字典");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		w.dict.setItemType(w.typeField.getValue().getType());
        		
    			ui.dataItemService.update(w.dict);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private ComboBox<DataDictionaryType> typeField = new ComboBox<DataDictionaryType>();
	private TextField nameField;
	private TextField codeField;
	private DoubleField orderField;
	private Button btnAdd;
	private Binder<DataDictionary> binder = new Binder<>();
	private DataDictionary dict = new DataDictionary();
	private List<DataDictionaryType> types = new ArrayList<DataDictionaryType>();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
