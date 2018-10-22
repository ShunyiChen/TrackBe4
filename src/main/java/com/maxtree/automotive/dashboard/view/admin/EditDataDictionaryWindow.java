package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
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
 * @author Chen
 *
 */
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
		nameField = new TextField("名称:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus();
		// 设置焦点
		codeField = new TextField("编码:");
		codeField.setIcon(VaadinIcons.CODE);
		
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
		codeField.setWidth(w+"px");
		
		nameField.setHeight(h+"px");
		codeField.setHeight(h+"px");
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
//		binder.forField(orderField)
//		  .withValidator(new EmptyValidator("顺序号不能为空"))
//		  .withConverter(new StringToFloatConverter("请输入一个浮点数"))
//		  .bind(DataDictionary::getOrderNumber, DataDictionary::setOrderNumber);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(dict.getItemName())) {
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
	public static void open(Callback2 callback, int type) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDataDictionaryWindow w = new EditDataDictionaryWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.dict.setItemType(type);
    			int dictionaryuniqueid = ui.dataItemService.insert(w.dict);
    			w.close();
    			callback.onSuccessful(dictionaryuniqueid);
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
        w.dict.setItemType(dataItem.getItemType());
        w.nameField.setValue(r.getItemName());
        w.codeField.setValue(r.getCode());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑字典");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.dataItemService.update(w.dict);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField nameField;
	private TextField codeField;
	private Button btnAdd;
	private Binder<DataDictionary> binder = new Binder<>();
	private DataDictionary dict = new DataDictionary();
//	private List<DataDictionaryType> types = new ArrayList<DataDictionaryType>();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
