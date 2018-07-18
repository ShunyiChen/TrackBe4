package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Community;
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
		
		List<Community> items = ui.communityService.findAll();
		parentCommunity.setEmptySelectionAllowed(true);
		parentCommunity.setTextInputAllowed(false);
		parentCommunity.setCaption("父级社区");
		parentCommunity.setIcon(VaadinIcons.GROUP);
		parentCommunity.setItems(items);
		parentCommunity.addValueChangeListener(e ->{
			Community community = e.getValue();
			if (community != null) {
				groupField.setValue(community.getGroupId()+"");
				levelField.setValue((community.getLevel() + 1)+"");
			
				groupField.setReadOnly(true);
				levelField.setReadOnly(true);
			} else {
				groupField.setValue("");
				levelField.setValue("");
				groupField.setReadOnly(false);
				levelField.setReadOnly(false);
			}
			
		});
		groupField = new TextField("组编号:");
		groupField.setIcon(VaadinIcons.CODE);
		groupField.setReadOnly(false);
		levelField = new TextField("级别:");
		levelField.setIcon(VaadinIcons.LEVEL_UP);
		levelField.setReadOnly(false);
		nameField = new TextField("社区名:");
		nameField.setIcon(VaadinIcons.EDIT);
		// 设置焦点
		nameField.focus();
		descField = new TextField("描述:");
		descField.setIcon(VaadinIcons.EDIT);
		form.addComponents(parentCommunity, groupField, levelField, nameField, descField);
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
		parentCommunity.setWidth(w+"px");
		groupField.setWidth(w+"px");
		levelField.setWidth(w+"px");
		nameField.setWidth(w+"px");
		descField.setWidth(w+"px");
		
		parentCommunity.setHeight(h+"px");
		groupField.setHeight(h+"px");
		levelField.setHeight(h+"px");
		nameField.setHeight(h+"px");
		descField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.forField(groupField)
		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
		  .bind(Community::getGroupId, Community::setGroupId);
		
		binder.forField(levelField)
		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
		  .bind(Community::getLevel, Community::setLevel);
		
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
	        "描述长度范围在2~40个字符",
	        2, 40)) .bind(Community::getCommunityDescription, Community::setCommunityDescription);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(community.getCommunityName())) {
			Notification notification = new Notification("提示：", "社区名不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			
			return false;
		}
		if (nameField.getErrorMessage() != null) {
			nameField.setComponentError(nameField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	private void selectItem(int communityUniqueID) {
		ListDataProvider<Community> listDataProvider = (ListDataProvider<Community>) parentCommunity.getDataProvider();
		for (Community c : listDataProvider.getItems()) {
			if (c.getCommunityUniqueId() == communityUniqueID) {
				parentCommunity.setSelectedItem(c);
				break;
			}
		}
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCommunityWindow w = new EditCommunityWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.communityService.create(w.community);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(Community community, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCommunityWindow w = new EditCommunityWindow();
        Community r = ui.communityService.findById(community.getCommunityUniqueId());
        w.community.setCommunityUniqueId(r.getCommunityUniqueId());
        
        w.selectItem(community.getCommunityUniqueId());
        w.groupField.setValue(r.getGroupId()+"");
        w.levelField.setValue(r.getLevel()+"");
        w.nameField.setValue(r.getCommunityName());
        w.descField.setValue(r.getCommunityDescription());
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
	
	private ComboBox<Community> parentCommunity = new ComboBox<>();
	private TextField nameField;
	private TextField descField;
	private TextField groupField;
	private TextField levelField;
	private Button btnAdd;
	private Binder<Community> binder = new Binder<>();
	private Community community = new Community();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

