package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Location;
import com.maxtree.automotive.dashboard.domain.Tenant;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
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
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditCommunityWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditCommunityWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新社区");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		
 
		nameField = new TextField("社区名:");
		nameField.setIcon(VaadinIcons.EDIT);
		//设置焦点
		nameField.focus();
		descField = new TextField("描述:");
		descField.setIcon(VaadinIcons.DEINDENT);
		//社区
		List<Tenant> lstTenant = ui.tenantService.findAll();
		tenantNameBox.setEmptySelectionAllowed(false);
		tenantNameBox.setTextInputAllowed(false);
		tenantNameBox.setItems(lstTenant);
		tenantNameBox.setIcon(VaadinIcons.USER_STAR);
		//省份
		List<Location> items1 = ui.locationService.findByCategory("省份");
		List<String> p = new ArrayList<>();
		for(Location l : items1) {
			p.add(l.getName());
		}
		provinceBox.setEmptySelectionAllowed(false);
		provinceBox.setTextInputAllowed(false);
		provinceBox.setItems(p);
		provinceBox.setIcon(VaadinIcons.LOCATION_ARROW);
		
		
		//地级市
		List<Location> items2 = ui.locationService.findByCategory("地级市");
		List<String> c = new ArrayList<>();
		for(Location l : items2) {
			c.add(l.getName());
		}
		cityBox.setEmptySelectionAllowed(false);
		cityBox.setTextInputAllowed(false);
		cityBox.setItems(c);
		cityBox.setIcon(VaadinIcons.LOCATION_ARROW);
		
		//地级市
		List<Location> items3 = ui.locationService.findByCategory("市、县级市");
		List<String> d = new ArrayList<>();
		for(Location l : items3) {
			d.add(l.getName());
		}
		districtBox.setEmptySelectionAllowed(false);
		districtBox.setTextInputAllowed(false);
		districtBox.setItems(d);
		districtBox.setIcon(VaadinIcons.LOCATION_ARROW);
		
		form.addComponents(nameField,descField,tenantNameBox,provinceBox,cityBox, districtBox);
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
		binder.setBean(community);
	}
	
	private void setComponentSize(int w, int h) {
		tenantNameBox.setWidth(w+"px");
		provinceBox.setWidth(w+"px");
		cityBox.setWidth(w+"px");
		districtBox.setWidth(w+"px");
		nameField.setWidth(w+"px");
		descField.setWidth(w+"px");
		
		tenantNameBox.setHeight(h+"px");
		provinceBox.setHeight(h+"px");
		cityBox.setHeight(h+"px");
		districtBox.setHeight(h+"px");
		nameField.setHeight(h+"px");
		descField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
//		binder.forField(groupField)
//		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
//		  .bind(Community::getGroupId, Community::setGroupId);
//		
//		binder.forField(levelField)
//		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
//		  .bind(Community::getLevel, Community::setLevel);
		
		binder.bind(nameField, Community::getCommunityName, Community::setCommunityName);
		binder.bind(descField, Community::getCommunityDescription, Community::setCommunityDescription);
//		binder.bind(tenantNameBox,Community::getTenantName, Community::setTenantName);
//		binder.bind(provinceBox, Community::getCommunityDescription, Community::setCommunityDescription);
//		binder.bind(cityBox, Community::getCommunityDescription, Community::setCommunityDescription);
//		binder.bind(districtBox, Community::getCommunityDescription, Community::setCommunityDescription);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(nameField).withValidator(new StringLengthValidator(
	        "社区名长度范围在2~20个字符",
	        2, 20)) .bind(Community::getCommunityName, Community::setCommunityName);
		// Validating Field Values
		binder.forField(descField).withValidator(new StringLengthValidator(
	        "描述长度范围在2~40个字符",
	        2, 40)) .bind(Community::getCommunityDescription, Community::setCommunityDescription);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(community.getCommunityName())) {
//			Notification notification = new Notification("提示：", "社区名不能为空", Type.WARNING_MESSAGE);
//			notification.setDelayMsec(2000);
//			notification.show(Page.getCurrent());
			Notifications.warning("社区名不能为空");
			return false;
		}
		else if(tenantNameBox.getValue()==null) {
			Notifications.warning("租户不能为空");
			return false;
		}
		else if(provinceBox.getValue()==null) {
			Notifications.warning("省份不能为空");
			return false;
		}
		else if(cityBox.getValue()==null) {
			Notifications.warning("地级市不能为空");
			return false;
		}
		else if(districtBox.getValue()==null) {
			Notifications.warning("市、县级市不能为空");
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
	public static void open(Callback2 callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCommunityWindow w = new EditCommunityWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		w.community.setTenantName(w.tenantNameBox.getValue().getName());
        		w.community.setProvince(w.provinceBox.getValue());
        		w.community.setCity(w.cityBox.getValue());
        		w.community.setDistrict(w.districtBox.getValue());
        		
    			int communityuniqueid = ui.communityService.insert(w.community);
    			w.close();
    			callback.onSuccessful(communityuniqueid);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param community
	 * @param callback
	 */
	public static void edit(Community community, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCommunityWindow w = new EditCommunityWindow();
        Community c = ui.communityService.findById(community.getCommunityUniqueId());
        w.community.setCommunityUniqueId(c.getCommunityUniqueId());
        List<Tenant> items = ui.tenantService.findAll();
        w.tenantNameBox.setItems(items);
        for(Tenant t : items) {
        	if(t.getName().equals(c.getTenantName())) {
        		w.tenantNameBox.setSelectedItem(t);
        	}
        }
        w.nameField.setValue(c.getCommunityName());
        w.descField.setValue(c.getCommunityDescription());
        w.provinceBox.setSelectedItem(c.getProvince());
        w.cityBox.setSelectedItem(c.getCity());
        w.districtBox.setSelectedItem(c.getDistrict());
        
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑社区");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.community.setTenantName(w.tenantNameBox.getValue().getName());
        		w.community.setProvince(w.provinceBox.getValue());
        		w.community.setCity(w.cityBox.getValue());
        		w.community.setDistrict(w.districtBox.getValue());
    			ui.communityService.update(w.community);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField nameField;
	private TextField descField;
	private ComboBox<Tenant> tenantNameBox = new ComboBox<>("租户:");
	private ComboBox<String> provinceBox = new ComboBox<>("省份:");
	private ComboBox<String> cityBox = new ComboBox<>("地级市:");
	private ComboBox<String> districtBox = new ComboBox<>("市、县级市:");
	private Button btnAdd;
	private Binder<Community> binder = new Binder<>();
	private Community community = new Community();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

