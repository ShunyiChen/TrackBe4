package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.vaadin.addons.autocomplete.AutocompleteExtension;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.messagingsystem.Name;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditBroadCastWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditBroadCastWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("发送消息");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		
		toField= new TextField("发送至:");
		toField.setIcon(VaadinIcons.USERS);
		toField.focus();
		
		initNames();
		
		// Apply extension and set suggestion generator
		AutocompleteExtension<Name> planetExtension = new AutocompleteExtension<>(toField);
		planetExtension.setSuggestionGenerator(
                this::suggestPlanet,
                this::convertValueUser,
                this::convertCaptionUser);
		planetExtension.setSuggestionDelay(200);

		// Notify when suggestion is selected
		planetExtension.addSuggestionSelectListener(event -> {
			Name selectedName = event.getSelectedItem().get();
//		    event.getSelectedItem().ifPresent(Notification::show);
			selectedList.add(selectedName);
			
			
			String init = toField.getValue();
			if (init.contains(";")) {
				init = init.substring(0, init.lastIndexOf(";")+1);
			} else {
				init = "";
			}
			
			StringBuilder sb = new StringBuilder(init);
			sb.append(selectedName.getName());
			sb.append(";");
			toField.setValue(sb.toString());
		});
		
		subjectField = new TextField("标题:");
		subjectField.setIcon(VaadinIcons.SUBSCRIPT);
		descArea = new TextArea("描述:");
		descArea.setIcon(VaadinIcons.EDIT);
		descArea.setRows(3);
		
		form.addComponents(toField, subjectField, descArea);
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
		binder.setBean(message);
	}
	
	private void initNames() {
		List<Community> lstCommunities = ui.communityService.findAll();
		for (Community community : lstCommunities) {
			list.add(new Name(community.getCommunityUniqueId(), "community", community.getCommunityName(), "../VAADIN/themes/dashboard/img/broadcast/community.png"));
		}
		
		List<Company> lstCompanies = ui.companyService.findAll();
		for (Company company : lstCompanies) {
			list.add(new Name(company.getCompanyUniqueId(), "company", company.getCompanyName(), "../VAADIN/themes/dashboard/img/broadcast/company.png"));
		}
		
		List<User> lstUsers = ui.userService.findAll(true);
		for (User user : lstUsers) {
			list.add(new Name(user.getUserUniqueId(), "user", user.getUserName(), "../VAADIN/themes/dashboard/img/broadcast/individual.png"));
		}
	}
	
	// Suggestion generator function, returns a list of suggestions for a user query
	private List<Name> suggestPlanet(String query, int cap) {
		String[] items = query.split(";");
		return list.stream().filter(p -> contains2(p.getName(), items))
	        .limit(cap).collect(Collectors.toList());
	}
	
	private boolean contains2(String name, String[] items) {
		for (String item : items) {
			if (name.contains(item)) {
				return true;
			}
		}
		return false;
	}
	
	private String convertValueUser(Name name) {
        return name.getName();//WordUtils.capitalizeFully(user.getName(), ' ');
    }

	/**
	 * 
	 * @param name
	 * @param query
	 * @return
	 */
    private String convertCaptionUser(Name name, String query) {
    	
    	// "../VAADIN/themes/dashboard/img/broadcast/individual.png"
        return "<div class='suggestion-container'>"
                + "<img src='" + name.getPicture() + "' class='userimage'>"
                + "<span class='username'>"
                + name.getName().replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }
	
	private void setComponentSize(int w, int h) {
		toField.setWidth(w+"px");
		subjectField.setWidth(w+"px");
		descArea.setWidth(w+"px");
		toField.setHeight(h+"px");
		subjectField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
//		binder.bind(toField, Message::getRecipientName, Message::setRecipientName);
		binder.bind(subjectField, Message::getSubject, Message::setSubject);
		binder.bind(descArea, Message::getMessageBody, Message::setMessageBody);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
	    binder.forField(toField).withValidator(new StringLengthValidator(
	        "接收者名称为1~20个字符",
	        1, 20)) .bind(Message::getSubject, Message::setSubject);
		
		// Validating Field Values
		binder.forField(subjectField).withValidator(new StringLengthValidator(
	        "标题长度范围在1~20个字符",
	        1, 20)) .bind(Message::getSubject, Message::setSubject);
		
		// Validating Field Values
		binder.forField(descArea).withValidator(new StringLengthValidator(
	        "消息体长度范围在1~160个字符",
	        1, 160)) .bind(Message::getMessageBody, Message::setMessageBody);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(message.getSubject())) {
			Notification notification = new Notification("提示：", "标题不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			
			return false;
		}
		if (subjectField.getErrorMessage() != null) {
			subjectField.setComponentError(subjectField.getErrorMessage());
			return false;
		}
		
//		if (StringUtils.isEmpty(message.getRecipientName())) {
//			Notification notification = new Notification("提示：", "接收者名称不能为空", Type.WARNING_MESSAGE);
//			notification.setDelayMsec(2000);
//			notification.show(Page.getCurrent());
//			
//			return false;
//		}
		if (subjectField.getErrorMessage() != null) {
			subjectField.setComponentError(subjectField.getErrorMessage());
			return false;
		}
		if (selectedList.size() == 0) {
			Notification notification = new Notification("提示：", "请至少选择一个接收者。", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
		} else {
			String[] items = toField.getValue().split(";");
			Iterator<Name> iter = selectedList.iterator();
			while(iter.hasNext()) {
				Name n = iter.next();
				// 用文本框上的值与selectedList里的值做比对
				boolean exist = false;
				for (String item : items) {
					if (n.getName().equals(item)) {
						exist = true;
						break;
					}
				}
				// 文本上不存在的，直接从list里删除
				if (!exist) {
					iter.remove();
				}
			}
		}
		
		return true;
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditBroadCastWindow w = new EditBroadCastWindow();
        w.btnAdd.setCaption("发送");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		if (w.descArea.getValue().length() > 160) {
        			Notifications.warning("内容不能超过160个字符。");
        			return;
        		}
        		
//        		int communityUniqueId = 0;
//        		int companyUniqueId = 0;
//        		int userUniqueId = 0;
//        		String recipientName = w.selectedName.getName();
//        		if (w.selectedName.getType().equals("community")) {
//        			communityUniqueId = w.selectedName.getUniqueId();
//        		} else if (w.selectedName.getType().equals("company")) {
//        			companyUniqueId = w.selectedName.getUniqueId();
//        		} else if (w.selectedName.getType().equals("user")) {
//        			userUniqueId = w.selectedName.getUniqueId();
//        		}
        		
        		
        		
        		User creator = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        		TB4MessagingSystem messagingSystem = new TB4MessagingSystem();
        		String subject = w.message.getSubject();
        		String messageBody = "{\"type\":\"text\", \"message\":\""+w.descArea.getValue()+"\"}";
        		Message newMessage = messagingSystem.createNewMessage(creator, subject, messageBody);
        		String viewName = "";// 如果viewName等于空，则表示消息将发送到对方的首个view上
        		new TB4MessagingSystem().sendMessageTo(newMessage.getMessageUniqueId(), w.selectedList, viewName);
        		
    			w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
 
	
//	private Name selectedName;
	private List<Name> selectedList = new ArrayList<Name>();
	private List<Name> list = new ArrayList<Name>();
	private TextField toField;
	private TextField subjectField;
	private TextArea descArea;
	private Button btnAdd;
	private Binder<Message> binder = new Binder<>();
	private Message message = new Message();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

