package com.maxtree.automotive.dashboard.view.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
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

public class EditCompanyWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditCompanyWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新机构");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		List<Community> items = ui.communityService.findAll();
		communitySelector.setCaption("加入社区:");
		communitySelector.setEmptySelectionAllowed(true);
		communitySelector.setTextInputAllowed(false);
		communitySelector.setIcon(VaadinIcons.GROUP);
		communitySelector.setItems(items);
		
		nameField = new TextField("机构名:");
		nameField.setIcon(VaadinIcons.EDIT);
		nameField.focus(); // 设置焦点
		addrField = new TextField("详细地址:");
		addrField.setIcon(VaadinIcons.EDIT);
		
		categorySelector.setItems("车管所","二手车","4S店");
		
		Address addr = Yaml.readAddress();
		provinceSelector.setItems(addr.getProvince());
		citySelector.setItems(addr.getCity());
		districtSelector.setItems(addr.getDistrict());
		
		hasStore.setEmptySelectionAllowed(false);
		hasStore.setTextInputAllowed(false);
		hasStore.setSelectedItem("是");
		hasChecker.setEmptySelectionAllowed(false);
		hasChecker.setTextInputAllowed(false);
		hasChecker.setSelectedItem("是");
		form.addComponents(nameField,categorySelector,provinceSelector,citySelector,districtSelector,addrField,communitySelector,hasStore,hasChecker);
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
		binder.setBean(company);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		nameField.setWidth(w+"px");
		addrField.setWidth(w+"px");
		communitySelector.setWidth(w+"px");
		categorySelector.setWidth(w+"px");
		provinceSelector.setWidth(w+"px");
		citySelector.setWidth(w+"px");
		districtSelector.setWidth(w+"px");
		hasStore.setWidth(w+"px");
		hasChecker.setWidth(w+"px");
		
		nameField.setHeight(h+"px");
		addrField.setHeight(h+"px");
		communitySelector.setHeight(h+"px");
		categorySelector.setHeight(h+"px");
		provinceSelector.setHeight(h+"px");
		citySelector.setHeight(h+"px");
		districtSelector.setHeight(h+"px");
		hasStore.setHeight(h+"px");
		hasChecker.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(nameField, Company::getCompanyName, Company::setCompanyName);
		binder.bind(categorySelector, Company::getCategory, Company::setCategory);
		binder.bind(provinceSelector, Company::getProvince, Company::setProvince);
		binder.bind(citySelector, Company::getCity, Company::setCity);
		binder.bind(districtSelector, Company::getDistrict, Company::setDistrict);
		binder.bind(addrField, Company::getAddress, Company::setAddress);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "机构名长度范围在2~20个字符",
	        2, 20)) .bind(Company::getCompanyName, Company::setCompanyName);
		
		
		binder.forField(provinceSelector).withValidator(new StringLengthValidator(
		        "省长度范围在2~20个字符",
		        2, 20)) .bind(Company::getProvince, Company::setProvince);
		binder.forField(citySelector).withValidator(new StringLengthValidator(
		        "市长度范围在0~20个字符",
		        0, 20)) .bind(Company::getCity, Company::setCity);
		binder.forField(districtSelector).withValidator(new StringLengthValidator(
		        "区长度范围在0~20个字符",
		        0, 20)) .bind(Company::getDistrict, Company::setDistrict);
		
		
		// Validating Field Values
		binder.forField(addrField).withValidator(new StringLengthValidator(
	        "地址长度范围在2~60个字符",
	        2, 40)) .bind(Company::getAddress, Company::setAddress);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		
		if (StringUtils.isEmpty(company.getCompanyName())) {
			Notification notification = new Notification("提示：", "机构名不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		if (StringUtils.isEmpty(company.getCategory())) {
			Notification notification = new Notification("提示：", "类别不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		if (nameField.getErrorMessage() != null) {
			nameField.setComponentError(nameField.getErrorMessage());
			return false;
		}
		if (StringUtils.isEmpty(company.getProvince())) {
			Notification notification = new Notification("提示：", "省不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		if (provinceSelector.getErrorMessage() != null) {
			provinceSelector.setComponentError(provinceSelector.getErrorMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param communityUniqueID
	 */
	private void selectItem(int communityUniqueID) {
		communitySelector.setSelectedItem(null);
		ListDataProvider<Community> listDataProvider = (ListDataProvider<Community>) communitySelector.getDataProvider();
		for (Community c : listDataProvider.getItems()) {
			if (c.getCommunityUniqueId() == communityUniqueID) {
				communitySelector.setSelectedItem(c);
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCompanyWindow w = new EditCompanyWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		String name1 = w.provinceSelector.getValue();
        		String code1 = ui.dataItemService.findCodeByName(name1);
        		String name2 = w.citySelector.getValue();
        		String code2 = ui.dataItemService.findCodeByName(name2);
        		String name3 = w.districtSelector.getValue();
        		String code3 = ui.dataItemService.findCodeByName(name3);
        		w.company.setCategory(w.categorySelector.getValue().trim());
        		w.company.setProvince(code1);
        		w.company.setCity(code2);
        		w.company.setDistrict(code3);
        		w.company.setCommunityUniqueId(w.communitySelector.getValue() == null?0:w.communitySelector.getValue().getCommunityUniqueId());
        		w.company.setHasStoreHouse(w.hasStore.getValue().equals("是")?1:0);
        		w.company.setIgnoreChecker(w.hasChecker.getValue().equals("是")?1:0);
        		int companyuniqueid = ui.companyService.create(w.company);
    			w.close();
    			callback.onSuccessful(companyuniqueid);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
 
	/**
	 * 
	 * @param company
	 * @param callback
	 */
	public static void edit(Company company, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCompanyWindow w = new EditCompanyWindow();
        Company c = ui.companyService.findById(company.getCompanyUniqueId());
        w.selectItem(c.getCommunityUniqueId());
        w.company.setCompanyUniqueId(c.getCompanyUniqueId());
        w.nameField.setValue(c.getCompanyName());
        String code1 = company.getProvince();
        String name1 = ui.dataItemService.findNameByCode(code1);
        String code2 = company.getCity();
        String name2 = ui.dataItemService.findNameByCode(code2);
        String code3 = company.getDistrict();
        String name3 = ui.dataItemService.findNameByCode(code3);
        
        w.categorySelector.setSelectedItem(c.getCategory().trim());
        w.provinceSelector.setSelectedItem(name1);
        w.citySelector.setSelectedItem(name2);
        w.districtSelector.setSelectedItem(name3);
        w.hasStore.setSelectedItem(c.getHasStoreHouse()==1?"是":"否");
        w.hasChecker.setSelectedItem(c.getIgnoreChecker()==1?"是":"否");
        w.addrField.setValue(c.getAddress() == null? "":c.getAddress());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑机构");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		String name11 = w.provinceSelector.getValue();
        		String code11 = ui.dataItemService.findCodeByName(name11);
        		String name22 = w.citySelector.getValue();
        		String code22 = ui.dataItemService.findCodeByName(name22);
        		String name33 = w.districtSelector.getValue();
        		String code33 = ui.dataItemService.findCodeByName(name33);
        		w.company.setCategory(w.categorySelector.getValue().trim());
        		w.company.setProvince(code11);
        		w.company.setCity(code22);
        		w.company.setDistrict(code33);
        		w.company.setEmployees(c.getEmployees());
        		w.company.setCommunityUniqueId(w.communitySelector.getValue() == null?0:w.communitySelector.getValue().getCommunityUniqueId());
        		w.company.setHasStoreHouse(w.hasStore.getValue().equals("是")?1:0);
        		w.company.setIgnoreChecker(w.hasChecker.getValue().equals("是")?1:0);
    			ui.companyService.update(w.company);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private ComboBox<String> categorySelector = new ComboBox<String>("类别:");
	private ComboBox<String> provinceSelector = new ComboBox<String>("省份:");
	private ComboBox<String> citySelector = new ComboBox<String>("地级市:");
	private ComboBox<String> districtSelector = new ComboBox<String>("市、县级市:");
	private ComboBox<String> hasStore = new ComboBox<String>("是否有库房:", Arrays.asList(new String[] {"是","否"}));
	private ComboBox<String> hasChecker = new ComboBox<String>("忽略质检:", Arrays.asList(new String[] {"是","否"}));
	private ComboBox<Community> communitySelector = new ComboBox<Community>();
	private TextField nameField;
	private TextField addrField;
	private Button btnAdd;
	private Binder<Company> binder = new Binder<>();
	private Company company = new Company();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

