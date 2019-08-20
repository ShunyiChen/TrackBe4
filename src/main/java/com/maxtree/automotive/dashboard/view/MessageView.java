package com.maxtree.automotive.dashboard.view;

import java.util.Date;
import java.util.Map;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MessageView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param map
	 */
	public MessageView(Map<String, Object> map) {
		this.map = map;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("查看消息");
		this.setWidth("600px");
		this.setHeight("440px");
		this.setResizable(false);
		this.setClosable(false);
		this.setModal(true);
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		User loggedInUser = ui.userService.getUserByUserName(username);
		int userUniqueId = Integer.parseInt(map.get("creatoruniqueid").toString());
		Date dateTime = (Date) map.get("datecreated");
		String subject = map.get("subject").toString();
		String content = map.get("content").toString();
		User fromUser = ui.userService.findById(userUniqueId);
		
		from.setValue(fromUser.getProfile().getFirstName()+" "+fromUser.getProfile().getLastName());
		to.setValue(loggedInUser.getProfile().getFirstName()+" "+loggedInUser.getProfile().getLastName());
		time.setValue(dateTime.toString());
		messageSubject.setValue(subject);
		messageContent.setValue(content);
		from.setIcon(VaadinIcons.USER);
		to.setIcon(VaadinIcons.USER_CARD);
		time.setIcon(VaadinIcons.TIMER);
		messageSubject.setIcon(VaadinIcons.SUBSCRIPT);
		messageContent.setIcon(VaadinIcons.COMMENTS);
		
		from.setWidth("100%");
		to.setWidth("100%");
		time.setWidth("100%");
		messageSubject.setWidth("100%");
		messageContent.setWidth("100%");
		
		from.setReadOnly(true);
		to.setReadOnly(true);
		time.setReadOnly(true);
		messageSubject.setReadOnly(true);
		messageContent.setReadOnly(true);
		
		messageContent.setRows(8);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		form.addComponents(from,to,time,messageSubject,messageContent);

		Button btnOK = new Button("确定");
		btnOK.addClickListener(e->{
			close();
			if(okEvent != null)
				okEvent.onSuccessful();
		});
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.addComponent(btnOK);
		buttons.setComponentAlignment(btnOK, Alignment.MIDDLE_CENTER);
		
		VerticalLayout main = new VerticalLayout();
		main.setSizeFull();
		main.addComponents(form,buttons);
		main.setExpandRatio(form, 1);
		this.setContent(main);
	}
	
	/**
	 * 
	 * @param map
	 * @param okEvent
	 */
	public static void open(Map<String, Object> map, Callback okEvent) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        MessageView w = new MessageView(map);
        w.okEvent = okEvent;
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Callback okEvent;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField from = new TextField("发消息人：");
	private TextField to = new TextField("收消息人：");
	private TextField time = new TextField("发送时间：");
	private TextField messageSubject = new TextField("标题：");
	private TextArea messageContent = new TextArea("消息内容：");
	private Map<String, Object> map;
}
