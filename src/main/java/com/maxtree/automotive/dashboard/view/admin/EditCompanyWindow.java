package com.maxtree.automotive.dashboard.view.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.SwitchButton;
import com.maxtree.automotive.dashboard.data.Area;
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
import com.vaadin.ui.CheckBox;
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
		
		Area area = Yaml.readArea();
		provinceSelector.setItems(area.getProvince());
		citySelector.setItems(area.getCity());
		prefectureSelector.setItems(area.getPrefecture());
		districtSelector.setItems(area.getDistrict());
		
		hasStore.setEmptySelectionAllowed(false);
		hasStore.setTextInputAllowed(false);
		hasStore.setSelectedItem("是");
		hasChecker.setEmptySelectionAllowed(false);
		hasChecker.setTextInputAllowed(false);
		hasChecker.setSelectedItem("是");
		form.addComponents(nameField,provinceSelector,citySelector,prefectureSelector,districtSelector,addrField,communitySelector,hasStore,hasChecker);
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
		provinceSelector.setWidth(w+"px");
		citySelector.setWidth(w+"px");
		prefectureSelector.setWidth(w+"px");
		districtSelector.setWidth(w+"px");
		hasStore.setWidth(w+"px");
		hasChecker.setWidth(w+"px");
		
		nameField.setHeight(h+"px");
		addrField.setHeight(h+"px");
		communitySelector.setHeight(h+"px");
		provinceSelector.setHeight(h+"px");
		citySelector.setHeight(h+"px");
		prefectureSelector.setHeight(h+"px");
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
		binder.bind(provinceSelector, Company::getProvince, Company::setProvince);
		binder.bind(citySelector, Company::getCity, Company::setCity);
		binder.bind(prefectureSelector, Company::getPrefecture, Company::setPrefecture);
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
		binder.forField(prefectureSelector).withValidator(new StringLengthValidator(
		        "县长度范围在0~20个字符",
		        0, 20)) .bind(Company::getPrefecture, Company::setPrefecture);
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
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCompanyWindow w = new EditCompanyWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.company.setCommunityUniqueId(w.communitySelector.getValue() == null?0:w.communitySelector.getValue().getCommunityUniqueId());
        		w.company.setHasStoreHouse(w.hasStore.getValue().equals("是")?1:0);
        		w.company.setIgnoreChecker(w.hasChecker.getValue().equals("是")?1:0);
        		ui.companyService.create(w.company);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
 
	public static void edit(Company company, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCompanyWindow w = new EditCompanyWindow();
        Company c = ui.companyService.findById(company.getCompanyUniqueId());
        w.selectItem(c.getCommunityUniqueId());
        w.company.setCompanyUniqueId(c.getCompanyUniqueId());
        w.nameField.setValue(c.getCompanyName());
        w.provinceSelector.setSelectedItem(c.getProvince() ==null?"":c.getProvince());
        w.citySelector.setSelectedItem(c.getCity() == null?"":c.getCity());
        w.prefectureSelector.setSelectedItem(c.getPrefecture()==null?"":c.getPrefecture());
        w.districtSelector.setSelectedItem(c.getDistrict()==null?"":c.getDistrict());
        w.hasStore.setSelectedItem(c.getHasStoreHouse()==1?"是":"否");
        w.hasChecker.setSelectedItem(c.getIgnoreChecker()==1?"是":"否");
        w.addrField.setValue(c.getAddress() == null? "":c.getAddress());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑机构");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
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
	
	private ComboBox<String> provinceSelector = new ComboBox<String>("省:");
	private ComboBox<String> citySelector = new ComboBox<String>("市:");
	private ComboBox<String> prefectureSelector = new ComboBox<String>("县:");
	private ComboBox<String> districtSelector = new ComboBox<String>("区:");
	private ComboBox<String> hasStore = new ComboBox<String>("是否有库房:", Arrays.asList(new String[] {"是","否"}));
	private ComboBox<String> hasChecker = new ComboBox<String>("是否有质检:", Arrays.asList(new String[] {"是","否"}));
	private ComboBox<Community> communitySelector = new ComboBox<Community>();
	private TextField nameField;
	private TextField addrField;
	private Button btnAdd;
	private Binder<Company> binder = new Binder<>();
	private Company company = new Company();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

