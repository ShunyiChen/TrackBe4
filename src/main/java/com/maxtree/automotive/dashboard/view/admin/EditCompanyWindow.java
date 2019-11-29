package com.maxtree.automotive.dashboard.view.admin;

import java.util.Arrays;
import java.util.List;

import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.server.VaadinSession;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
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
public class EditCompanyWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 */
//	public EditCompanyWindow() {
//		this.setWidth("513px");
//		this.setHeightUndefined();
//		this.setModal(true);
//		this.setResizable(false);
//		this.setCaption("添加新机构");
//		this.addStyleName("edit-window");
//		VerticalLayout mainLayout = new VerticalLayout();
//		mainLayout.setSpacing(true);
//		mainLayout.setMargin(false);
//
//		FormLayout form = new FormLayout();
//		form.setSpacing(false);
//		form.setMargin(false);
//		form.setSizeFull();
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		User loggedInUser = ui.userService.getUserByUserName(username);
//		communities = ui.communityService.findAll(loggedInUser);
//		communitySelector.setEmptySelectionAllowed(true);
//		communitySelector.setTextInputAllowed(false);
//		communitySelector.setIcon(VaadinIcons.GROUP);
//		communitySelector.setItems(communities);
//
//
//		nameField.setIcon(VaadinIcons.EDIT);
//		nameField.focus(); // 设置焦点
//		addrField.setIcon(VaadinIcons.BUILDING);
//
//		List<String> lst = ui.companyCategoryService.findAll();
//		categorySelector.setEmptySelectionAllowed(false);
//		categorySelector.setTextInputAllowed(false);
//		categorySelector.setItems(lst);
//		categorySelector.setIcon(VaadinIcons.TABLE);
//
//		frameNumbers = ui.frameService.findAllStorehouse();
//		storeSelector.setEmptySelectionAllowed(true);
//		storeSelector.setTextInputAllowed(false);
//		storeSelector.setItems(frameNumbers);
//		storeSelector.setIcon(VaadinIcons.STORAGE);
//
//		qcsupportSelector.setEmptySelectionAllowed(false);
//		qcsupportSelector.setTextInputAllowed(false);
//		qcsupportSelector.setSelectedItem("支持");
//		qcsupportSelector.setIcon(VaadinIcons.USER_CHECK);
//
//		form.addComponents(communitySelector,nameField,addrField,categorySelector,storeSelector,qcsupportSelector);
//		HorizontalLayout buttonPane = new HorizontalLayout();
//		buttonPane.setSizeFull();
//		buttonPane.setSpacing(false);
//		buttonPane.setMargin(false);
//		Button btnCancel = new Button("取消");
//		btnAdd = new Button("添加");
//		HorizontalLayout subButtonPane = new HorizontalLayout();
//		subButtonPane.setSpacing(false);
//		subButtonPane.setMargin(false);
//		subButtonPane.setWidth("128px");
//		subButtonPane.setHeight("100%");
//		subButtonPane.addComponents(btnCancel, btnAdd);
//		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
//		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
//		buttonPane.addComponent(subButtonPane);
//		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
//		mainLayout.addComponents(form,buttonPane);
//		this.setContent(mainLayout);
//
//		btnCancel.addClickListener(e -> {
//			close();
//		});
//		setComponentSize(350, 27);
//
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		bindFields();
//
//		// Validating Field Values
//		validatingFieldValues();
//
//		// Bind an actual concrete Person instance.
//		// After this, whenever the user changes the value
//		// of nameField, p.setName is automatically called.
//		binder.setBean(company);
//	}
//
//	/**
//	 *
//	 * @param w
//	 * @param h
//	 */
//	private void setComponentSize(int w, int h) {
//		nameField.setWidth(w+"px");
//		addrField.setWidth(w+"px");
//		communitySelector.setWidth(w+"px");
//		categorySelector.setWidth(w+"px");
//		storeSelector.setWidth(w+"px");
//		qcsupportSelector.setWidth(w+"px");
//
//		nameField.setHeight(h+"px");
//		addrField.setHeight(h+"px");
//		communitySelector.setHeight(h+"px");
//		categorySelector.setHeight(h+"px");
//		storeSelector.setHeight(h+"px");
//		qcsupportSelector.setHeight(h+"px");
//	}
//
//	/**
//	 *
//	 */
//	private void bindFields() {
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		binder.bind(nameField, Company::getCompanyName, Company::setCompanyName);
////		binder.bind(categorySelector, Company::getCategory, Company::setCategory);
////		binder.bind(provinceSelector, Company::getProvince, Company::setProvince);
////		binder.bind(citySelector, Company::getCity, Company::setCity);
////		binder.bind(districtSelector, Company::getDistrict, Company::setDistrict);
//		binder.bind(addrField, Company::getAddress, Company::setAddress);
//	}
//
//	/**
//	 *
//	 */
//	private void validatingFieldValues () {
//		// Validating Field Values
//		binder.forField(nameField).withValidator(new StringLengthValidator(
//	        "机构名长度范围在2~20个字符",
//	        2, 20)) .bind(Company::getCompanyName, Company::setCompanyName);
//
//
////		binder.forField(provinceSelector).withValidator(new StringLengthValidator(
////		        "省长度范围在2~20个字符",
////		        2, 20)) .bind(Company::getProvince, Company::setProvince);
////		binder.forField(citySelector).withValidator(new StringLengthValidator(
////		        "市长度范围在0~20个字符",
////		        0, 20)) .bind(Company::getCity, Company::setCity);
////		binder.forField(districtSelector).withValidator(new StringLengthValidator(
////		        "区长度范围在0~20个字符",
////		        0, 20)) .bind(Company::getDistrict, Company::setDistrict);
//
//
//		// Validating Field Values
//		binder.forField(addrField).withValidator(new StringLengthValidator(
//	        "地址长度范围在2~60个字符",
//	        2, 40)) .bind(Company::getAddress, Company::setAddress);
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private boolean checkEmptyValues() {
//		if (StringUtils.isEmpty(company.getCompanyName())) {
//			Notifications.warning("公司名不能为空");
//			return false;
//		}
//		else if (StringUtils.isEmpty(categorySelector.getValue())) {
//			Notifications.warning("类别不能为空");
//			return false;
//		}
//		else if (StringUtils.isEmpty(communitySelector.getValue())) {
//			Notifications.warning("社区不能为空");
//			return false;
//		}
//		if (nameField.getErrorMessage() != null) {
//			nameField.setComponentError(nameField.getErrorMessage());
//			return false;
//		}
////		if (StringUtils.isEmpty(company.getProvince())) {
////			Notification notification = new Notification("提示：", "省不能为空", Type.WARNING_MESSAGE);
////			notification.setDelayMsec(2000);
////			notification.show(Page.getCurrent());
////			return false;
////		}
//		return true;
//	}
//
//	/**
//	 *
//	 * @param callback
//	 */
//	public static void open(Callback2 callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        EditCompanyWindow w = new EditCompanyWindow();
//        w.btnAdd.setCaption("添加");
//        w.btnAdd.addClickListener(e -> {
//        	if (w.checkEmptyValues()) {
//        		w.company.setCategory(w.categorySelector.getValue().trim());
//        		w.company.setCommunityUniqueId(w.communitySelector.getValue() == null?0:w.communitySelector.getValue().getCommunityUniqueId());
//        		if(w.storeSelector.getValue() != null) {
//        			w.company.setStorehouseName(w.storeSelector.getValue().getStorehouseName());
//        		}
//        		w.company.setQcsupport(w.qcsupportSelector.getValue().equals("支持")?true:false);
//        		int companyuniqueid = ui.companyService.insert(w.company);
//    			w.close();
//    			callback.onSuccessful(companyuniqueid);
//        	}
//		});
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	/**
//	 *
//	 * @param company
//	 * @param callback
//	 */
//	public static void edit(Company company, Callback callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        EditCompanyWindow w = new EditCompanyWindow();
//        Company c = ui.companyService.findById(company.getCompanyUniqueId());
//        w.company.setCompanyUniqueId(c.getCompanyUniqueId());
//        w.nameField.setValue(c.getCompanyName());
//        w.addrField.setValue(c.getAddress() == null? "":c.getAddress());
//        for(Community community : w.communities) {
//        	if(c.getCommunityUniqueId() == community.getCommunityUniqueId())
//        		w.communitySelector.setSelectedItem(community);
//        }
//        w.categorySelector.setSelectedItem(c.getCategory());
//        w.qcsupportSelector.setSelectedItem(c.getQcsupport()?"支持":"不支持");
//        for(FrameNumber fn : w.frameNumbers) {
//        	if(fn.getStorehouseName().equals(c.getStorehouseName())) {
//        		 w.storeSelector.setSelectedItem(fn);
//        	}
//        }
//        w.btnAdd.setCaption("保存");
//        w.setCaption("编辑机构");
//        w.btnAdd.addClickListener(e -> {
//        	if (w.checkEmptyValues()) {
//
//        		w.company.setCategory(w.categorySelector.getValue().trim());
//        		w.company.setEmployees(c.getEmployees());
//        		w.company.setCommunityUniqueId(w.communitySelector.getValue() == null?0:w.communitySelector.getValue().getCommunityUniqueId());
//        		if(w.storeSelector.getValue() != null) {
//        			w.company.setStorehouseName(w.storeSelector.getValue().getStorehouseName());
//        		}
//        		w.company.setQcsupport(w.qcsupportSelector.getValue().equals("支持")?true:false);
//    			ui.companyService.update(w.company);
//    			w.close();
//    			callback.onSuccessful();
//        	}
//		});
//
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//	//社区
//	private ComboBox<Community> communitySelector = new ComboBox<Community>("选择社区");
//	private TextField nameField = new TextField("机构名:");
//	private TextField addrField = new TextField("详细地址:");
//	private ComboBox<String> categorySelector = new ComboBox<String>("类别:");
//	private ComboBox<FrameNumber> storeSelector = new ComboBox<FrameNumber>("选择库房:");
//	private ComboBox<String> qcsupportSelector = new ComboBox<String>("质检支持:", Arrays.asList(new String[] {"支持","不支持"}));
//	private List<Community> communities;
//	private List<FrameNumber> frameNumbers;
//
//	private Button btnAdd;
//	private Binder<Company> binder = new Binder<>();
//	private Company company = new Company();
//	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

