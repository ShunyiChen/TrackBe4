package com.maxtree.automotive.dashboard.view;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.front.MessageInboxWindow;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
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
	 * @param map
	 */
	public MessageView(Map<String, Object> map) {
		this.map = map;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("查看消息");
		this.setWidth("700px");
		this.setHeight("340px");
		this.setResizable(true);
		this.setClosable(true);
		this.setModal(true);
		User loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		int userUniqueId = Integer.parseInt(map.get("creatoruniqueid").toString());
		int messageUniqueId = Integer.parseInt(map.get("messageuniqueid").toString());
		Date dateTime = (Date) map.get("datecreated");
		String subject = map.get("subject").toString();
		String comments = map.get("comments").toString();
		User fromUser = ui.userService.findById(userUniqueId);
		List<MessageRecipient> recipients = ui.messagingService.findRecipientsByMessageId(messageUniqueId);
		
		recipients.forEach(new Consumer<MessageRecipient>() {

			@Override
			public void accept(MessageRecipient t) {
				 if(t.getMessageUniqueId().intValue() == messageUniqueId
						 && t.getMessageRecipientUniqueId().intValue() == loggedInUser.getUserUniqueId().intValue()) {
					 recipient = t;
				 }
			}
		});
		
		from.setValue(fromUser.getProfile().getFirstName()+" "+fromUser.getProfile().getLastName());
		to.setValue(recipient.getRecipientName());
		time.setValue(dateTime.toString());
		messageSubject.setValue(subject);
		messageContent.setValue(comments);
		
		from.setIcon(VaadinIcons.USER);
		to.setIcon(VaadinIcons.USER_CARD);
		
		FormLayout form = new FormLayout();
		form.setSizeFull();
		form.addComponents(from,to,time,messageSubject,messageContent);
		
		Button btnOK = new Button("确定");
		btnOK.addClickListener(e->close());
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(false);
		buttons.setMargin(false);
		buttons.setWidth("100%");
		buttons.setHeightUndefined();
		buttons.addComponent(btnOK);
		
		VerticalLayout main = new VerticalLayout();
		main.setSpacing(false);
		main.setMargin(false);
		main.addComponents(form,buttons);
		main.setExpandRatio(form, 1);
		this.setContent(main);
	}
	
	public static void open(Map<String, Object> map) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        MessageView w = new MessageView(map);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private MessageRecipient recipient;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField from = new TextField("发消息人：");
	private TextField to = new TextField("收消息人：");
	private TextField time = new TextField("发送时间：");
	private TextField messageSubject = new TextField("标题：");
	private TextArea messageContent = new TextArea("消息内容：");
	private Map<String, Object> map;
}
