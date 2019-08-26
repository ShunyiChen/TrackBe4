package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EditCommunityWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
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
		form.addComponents(nameField, descField);
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

	/**
	 *
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		nameField.setWidth(w+"px");
		descField.setWidth(w+"px");
		nameField.setHeight(h+"px");
		descField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		binder.bind(nameField, Community::getCommunityName, Community::setCommunityName);
		binder.bind(descField, Community::getCommunityDescription, Community::setCommunityDescription);
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
	        "描述长度范围在0~40个字符",
	        0, 40)) .bind(Community::getCommunityDescription, Community::setCommunityDescription);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(community.getCommunityName())) {
			Notifications.warning("社区名不能为空");
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
        EditCommunityWindow w = new EditCommunityWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		w.community.setCommunityDescription(w.descField.getValue()==null?"":w.descField.getValue());
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
        w.nameField.setValue(c.getCommunityName());
        w.descField.setValue(c.getCommunityDescription()==null?"":c.getCommunityDescription());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑社区");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
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
	private Button btnAdd;
	private Binder<Community> binder = new Binder<>();
	private Community community = new Community();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

