package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.vaadin.addons.autocomplete.AutocompleteExtension;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
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
			nameSets.add(selectedName);
			
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
		buttonPane.setWidthUndefined();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		buttonPane.addComponents(btnCancel, Box.createHorizontalBox(5), btnAdd);
		buttonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_CENTER);
		buttonPane.setComponentAlignment(btnAdd, Alignment.MIDDLE_CENTER);
		mainLayout.addComponents(form, buttonPane);
		
		mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
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
			list.add(new Name(community.getCommunityUniqueId(), Name.COMMUNITY, community.getCommunityName(), "../VAADIN/themes/dashboard/img/broadcast/community.png"));
		}
		
		List<Company> lstCompanies = ui.companyService.findAll();
		for (Company company : lstCompanies) {
			list.add(new Name(company.getCompanyUniqueId(), Name.COMPANY, company.getCompanyName(), "../VAADIN/themes/dashboard/img/broadcast/company.png"));
		}
		
		List<User> lstUsers = ui.userService.findAll(true);
		for (User user : lstUsers) {
			list.add(new Name(user.getUserUniqueId(), Name.USER, user.getUserName(), "../VAADIN/themes/dashboard/img/broadcast/individual.png"));
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
		binder.bind(subjectField, Message::getSubject, Message::setSubject);
		binder.bind(descArea, Message::getContent, Message::setContent);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(subjectField).withValidator(new StringLengthValidator(
	        "标题长度范围在1~20个字符",
	        1, 20)) .bind(Message::getSubject, Message::setSubject);
		
		// Validating Field Values
		binder.forField(descArea).withValidator(new StringLengthValidator(
	        "消息体长度范围在1~200个字符",
	        1, 200)) .bind(Message::getContent, Message::setContent);
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
		if (subjectField.getErrorMessage() != null) {
			subjectField.setComponentError(subjectField.getErrorMessage());
			return false;
		}
		
		if(descArea.getValue().length() > 200) {
			Notification notification = new Notification("提示：", "消息内容不能超出200个字符。", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		
		if (nameSets.size() != 0) {
			String[] items = toField.getValue().split(";");
			Iterator<Name> iter = nameSets.iterator();
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
				// list里的值与文本框输入的不匹配，则直接从list里删除
				if (!exist) {
					iter.remove();
				}
			}
		}
		if (nameSets.size() == 0) {
			Notification notification = new Notification("提示：", "请至少选择一个有效的接收者。", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		
		return true;
	}
	
	public static void open(Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditBroadCastWindow w = new EditBroadCastWindow();
        w.btnAdd.setCaption("发送");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		if (w.descArea.getValue().length() > 160) {
        			Notifications.warning("内容不能超过160个字符。");
        			return;
        		}
        		User creator = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        		String subject = w.message.getSubject();
        		String matedata = "";
        		Message newMessage = w.messagingSystem.createNewMessage(creator, subject, w.message.getContent(), matedata);
        		
        		String viewName = "";// 如果viewName等于空，则表示消息将发送到对方的首个view上
        		new TB4MessagingSystem().sendMessageTo(newMessage.getMessageUniqueId(), w.nameSets, viewName);
        		
    			w.close();
    			callback.onSuccessful(newMessage);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	
	/**
	 * 
	 * @param msg
	 * @param callback
	 */
	public static void edit(Message msg, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditBroadCastWindow w = new EditBroadCastWindow();
        w.btnAdd.setCaption("重新发送");
        w.subjectField.setValue(msg.getSubject());
        w.toField.setEnabled(false);
        StringBuilder stb = new StringBuilder();
        List<MessageRecipient> list = ui.messagingService.findRecipientsByMessageId(msg.getMessageUniqueId());
        for (MessageRecipient mr : list) {
        	stb.append(mr.getRecipientName());
        	stb.append(";");
        	
        	Name n = new Name(mr.getRecipientUniqueId(), mr.getRecipientType(), mr.getRecipientName(), "");
        	w.nameSets.add(n);
        }
        w.toField.setValue(stb.toString());
        Map<String, String> map = w.jsonHelper.json2Map(msg.getMatedata());
        w.descArea.setValue(msg.getContent());
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		String viewName = "";// 如果viewName等于空，则表示消息将发送到对方的首个view上
        		
        		String subject = w.message.getSubject();
        		String matedata = "";
        		msg.setSubject(subject);
        		msg.setContent(w.descArea.getValue());
        		msg.setMatedata(matedata);
        		msg.setSentTimes(msg.getSentTimes()+1);
        		msg.setDateCreated(new Date());
        		int newMessageUniqueId = ui.messagingService.insertMessage(msg);
        		w.messagingSystem.sendMessageTo(newMessageUniqueId, w.nameSets, viewName);
        		
        		w.close();
        		
        		callback.onSuccessful();
        	}
        });
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
 
	private MessageBodyParser jsonHelper = new MessageBodyParser();
	private LinkedHashSet<Name> nameSets = new LinkedHashSet<Name>();
	private List<Name> list = new ArrayList<Name>();
	private TextField toField;
	private TextField subjectField;
	private TextArea descArea;
	private Button btnAdd;
	private Binder<Message> binder = new Binder<>();
	private Message message = new Message();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TB4MessagingSystem messagingSystem = new TB4MessagingSystem();
}

