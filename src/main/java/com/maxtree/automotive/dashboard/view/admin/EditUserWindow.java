package com.maxtree.automotive.dashboard.view.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.domain.UserProfile;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.security.PasswordSecurity;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditUserWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EditUserWindow() {
		this.setWidth("433px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新用户");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setWidth("100%");
		form.setHeightUndefined();
		userNameField = new TextField("用户名:");
		userNameField.setIcon(VaadinIcons.USER);
		userNameField.focus();
		passwordField = new PasswordField("密码:");
		passwordField.setIcon(VaadinIcons.PASSWORD);
		confirmPasswordField = new PasswordField("重复密码:");
		confirmPasswordField.setIcon(VaadinIcons.PASSWORD);
		firstNameField = new TextField("名:");
		firstNameField.setIcon(VaadinIcons.TEXT_LABEL);
		lastNameField = new TextField("姓:");
		lastNameField.setIcon(VaadinIcons.FACEBOOK);
		
		List<Company> companies = ui.companyService.findAll();
		companySelector.setCaption("所在机构:");
		companySelector.setEmptySelectionAllowed(true);
		companySelector.setTextInputAllowed(false);
		companySelector.setItems(companies);
		companySelector.setIcon(VaadinIcons.GROUP);
		
		activateBox.setEmptySelectionAllowed(false);
		activateBox.setTextInputAllowed(false);
		activateBox.setSelectedItem("是");
		form.addComponents(userNameField,lastNameField,firstNameField,passwordField,confirmPasswordField,companySelector,activateBox);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		btnApply = new Button("应用");
		subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("195px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnAdd, btnApply);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnApply, Alignment.BOTTOM_RIGHT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(form,buttonPane);
		this.setContent(mainLayout);
		btnCancel.addClickListener(e -> {
			close();
		});
		
		setComponentSize(240, 27);
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		bindFields();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(user);
		// Validating Field Values
	    validatingFieldValues();
	}
	
	private void setComponentSize(int w, int h) {
		userNameField.setWidth(w+"px");
		passwordField.setWidth(w+"px");
		confirmPasswordField.setWidth(w+"px");
		firstNameField.setWidth(w+"px");
		lastNameField.setWidth(w+"px");
		companySelector.setWidth(w+"px");
		activateBox.setWidth(w+"px");
		
		userNameField.setHeight(h+"px");
		passwordField.setHeight(h+"px");
		confirmPasswordField.setHeight(h+"px");
		firstNameField.setHeight(h+"px");
		lastNameField.setHeight(h+"px");
		companySelector.setHeight(h+"px");
		activateBox.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
//		binder.bind(userNameField, User::getUserName, User::setUserName);
//		binder.bind(firstNameField, User::getFirstName, User::setFirstName);
//		binder.bind(lastNameField, User::getLastName, User::setLastName);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(userNameField).withValidator(new StringLengthValidator(
	        "用户名长度应在1至20个字符范围内",
	        1, 20))
	    .bind(User::getUserName, User::setUserName);
		
		binder.forField(passwordField).withValidator(new StringLengthValidator(
		        "密码长度应在1至20个字符范围内",
		        1, 20))
		    .bind(User::getHashed, User::setHashed);
		
		binder.forField(confirmPasswordField).withValidator(new StringLengthValidator(
		        "重复密码长度应在1至20个字符范围内",
		        1, 20))
		    .bind(User::getHashed, User::setHashed);
		
//		binder.forField(firstNameField).withValidator(new StringLengthValidator(
//		        "名长度应在1至20个字符范围内",
//		        1, 20))
//		    .bind(User::getFirstName, User::setFirstName);
//	
//		binder.forField(lastNameField).withValidator(new StringLengthValidator(
//		        "姓长度应在1至20个字符范围内",
//		        1, 20))
//		    .bind(User::getLastName, User::setLastName);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues(boolean ignorePassword) {
		if (StringUtils.isEmpty(userNameField.getValue())) {
			userNameField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "用户名不能为空。";
				}
			});
			return false;
		} else if (!ignorePassword && StringUtils.isEmpty(passwordField.getValue())) {
			passwordField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "密码不能为空。";
				}
			});
			return false;
		}
		else if (!ignorePassword && !passwordField.getValue().equals(confirmPasswordField.getValue())) {
			confirmPasswordField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "确认密码不匹配。";
				}
			});
			return false;
		}
		if (userNameField.getErrorMessage() != null) {
			userNameField.setComponentError(userNameField.getErrorMessage());
			return false;
		} 
		else if (!ignorePassword && passwordField.getErrorMessage() != null) {
			passwordField.setComponentError(passwordField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param companyUniqueID
	 */
	private void selectItem(int companyUniqueID) {
		ListDataProvider<Company> listDataProvider = (ListDataProvider<Company>) companySelector.getDataProvider();
		for (Company c : listDataProvider.getItems()) {
			if (c.getCompanyUniqueId() == companyUniqueID) {
				companySelector.setSelectedItem(c);
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param user
	 */
	private boolean apply(boolean createNewUser) {
		if (createNewUser) {
			if (ui.userService.exist(user.getUserName(), "")) {
    			Notification notification = new Notification("提示：", "用户名"+user.getUserName()+"已存在。", Type.WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				notification.show(Page.getCurrent());
    			return false;
    		}
    		String plainText = passwordField.getValue();
    		String hashed = PasswordSecurity.hashPassword(plainText);
    		user.setHashed(hashed);
    		user.setActivated(activateBox.getValue().equals("是")?1:0);
    		
    		Company company = companySelector.getValue();
    		if (company != null) {
    			user.setCompanyUniqueId(company.getCompanyUniqueId());
        		user.setCompanyName(company.getCompanyName());
        		user.setCommunityUniqueId(company.getCommunityUniqueId());
    		} else {
    			user.setCompanyUniqueId(0);
        		user.setCompanyName("");
        		user.setCommunityUniqueId(0);
    		}
    		
    		UserProfile profile = new UserProfile();
    		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    		profile.setCreatedBy(loginUser.getUserName());
    		profile.setCreatorUniqueId(loginUser.getUserUniqueId());
    		profile.setTitle("职员");
    		profile.setPicture("img/adminmenu/users/user1.png");
    		profile.setFirstName(firstNameField.getValue());
    		profile.setLastName(lastNameField.getValue());
			ui.userService.create(user, profile);
		
		} else {
			
			if (ui.userService.exist(userNameField.getValue(), oldUserName)) {
    			Notification notification = new Notification("提示：", "用户名"+userNameField.getValue()+"已存在。", Type.WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				notification.show(Page.getCurrent());
    			return false;
    		}
    		user.setActivated(activateBox.getValue().equals("是")?1:0);
    		
    		Company company = companySelector.getValue();
    		if (company != null) {
    			user.setCompanyUniqueId(company.getCompanyUniqueId());
        		user.setCompanyName(company.getCompanyName());
        		user.setCommunityUniqueId(company.getCommunityUniqueId());
    		} else {
    			user.setCompanyUniqueId(0);
        		user.setCompanyName("");
        		user.setCommunityUniqueId(0);
    		}
    		
    		// 不允许改别人的密码，只能通知重置
    		ui.userService.update(user, true);
		}
		
		return true;
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditUserWindow w = new EditUserWindow();
        w.btnApply.setVisible(false);
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues(false)) {
        		if(w.apply(true)) {
        			w.close();
        			callback.onSuccessful();
        		}
        	}
		});
        w.subButtonPane.removeComponent(w.btnApply);
        w.subButtonPane.setWidth("135px");
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(User user, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditUserWindow w = new EditUserWindow();
        w.oldUserName = user.getUserName();
        w.user.setUserUniqueId(user.getUserUniqueId());
        w.userNameField.setValue(user.getUserName());
        w.firstNameField.setValue(user.getProfile().getFirstName());
        w.lastNameField.setValue(user.getProfile().getLastName());
        w.passwordField.setVisible(false);
		w.confirmPasswordField.setVisible(false);
		w.activateBox.setValue(user.getActivated()==1?"是":"否");
		
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑用户");
        w.selectItem(user.getCompanyUniqueId());
        
        w.btnAdd.addClickListener(e -> {
        	
        	if (w.checkEmptyValues(true)) {
        		if (w.apply(false)) {
        			w.close();
        			callback.onSuccessful();
        		}
        	}
		});
        w.btnApply.addClickListener(e -> {
        	
        	if (w.checkEmptyValues(true)) {
        		if (w.apply(false)) {
        			callback.onSuccessful();
        		}
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField userNameField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
	private TextField firstNameField;
	private TextField lastNameField;
	private ComboBox<Company> companySelector = new ComboBox<Company>();
	private String oldUserName;
	private ComboBox<String> activateBox = new ComboBox<String>("是否激活:", Arrays.asList(new String[] {"是","否"}));
	
	private Button btnAdd;
	private Button btnApply;
	private HorizontalLayout subButtonPane;
	private Binder<User> binder = new Binder<>();
	private User user = new User();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
