package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;
import java.util.Map;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageBroadCastGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageBroadCastGrid() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("90%");
		this.setHeightUndefined();
		// 表头
		HorizontalLayout header = createGridHeader();
		// 表体
		Panel body = createGridBody();
		Button addButton = new Button("新建消息");
		addButton.addStyleName("grid-button-without-border");
		addButton.addClickListener(e-> {
			User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
			if (loginUser.isPermitted(PermissionCodes.L1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditBroadCastWindow.open(callback);
			} else {
        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
        	}
		});
		
		this.addComponents(header, body, Box.createVerticalBox(15), addButton);
		this.setComponentAlignment(header, Alignment.TOP_CENTER);
		this.setComponentAlignment(body, Alignment.TOP_CENTER);
		this.setComponentAlignment(addButton, Alignment.TOP_RIGHT);
	}

	/**
	 * 
	 * @return
	 */
	private HorizontalLayout createGridHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidth("100%");
		header.setHeight("50px");
		header.addStyleName("grid-header-line");
		Label columnSubject = new Label("消息标题");
		columnSubject.addStyleName("grid-title");
		Label columnSentTo = new Label("发送至");
		columnSentTo.addStyleName("grid-title");
		Label columnDate = new Label("发送时间");
		columnDate.addStyleName("grid-title");
		Label columnSent = new Label("发送次数");
		columnSent.addStyleName("grid-title");
		Label columnRead = new Label("读取率");
		columnRead.addStyleName("grid-title");
		header.addComponents(columnSubject, columnSentTo, columnDate, columnSent, columnRead);
		header.setComponentAlignment(columnSubject, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnSentTo, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnDate, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnSent, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnRead, Alignment.MIDDLE_LEFT);
		return header;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createGridBody() {
		Panel gridPanel = new Panel();
		gridPanel.addStyleName("grid-body-without-border");
		gridPanel.setHeight("340px");
		tableBody = new VerticalLayout(); 
		tableBody.setMargin(false);
		tableBody.setSpacing(false);
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		List<Message> data = ui.messagingService.findAll(loginUser.getUserUniqueId());
		for (Message m : data) {
			HorizontalLayout row1 = createDataRow(m);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
		gridPanel.setContent(tableBody);
		return gridPanel;
	}
	
	/**
	 * 
	 * @param community
	 * @return
	 */
	private HorizontalLayout createDataRow(Message message) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		
//		MessageBodyParser parser = new MessageBodyParser();
//		Map<String, String> map = parser.json2Map(message.getMessageBody());
		StringBuilder names = new StringBuilder();
		List<MessageRecipient> recipients = ui.messagingService.findRecipientsByMessageId(message.getMessageUniqueId());
		for (MessageRecipient mr : recipients) {
			names.append(mr.getRecipientName());
			names.append(";");
		}
		Label columnSubject = new Label(message.getSubject());
		Label columnSentTo = new Label(names.toString());
		Label columnDate = new Label(message.getDateCreated().toString());
		Label columnSent = new Label(message.getSentTimes()+"");
		Label columnRead = new Label("0%");
		
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			 
			menu.addItem("设置定时发送", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.L3)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						FrequencySettingsWindow.open(message, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			
			menu.addItem("重发", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
				}
			});
			menu.addSeparator();
			menu.addItem("查看内容", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
				}
			});
			menu.addItem("从列表删除", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.L4)) {
						ui.messagingService.deleteMessage(message.getMessageUniqueId());
						refreshTable();
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		columnSubject.setWidth("112px");
		columnSentTo.setWidth("113px");
		columnDate.setWidth("125px");
		columnSent.setWidth("115px");
		columnRead.setWidth("96px");
		row.addComponents(columnSubject, columnSentTo, columnDate, columnSent, columnRead ,moreImg);
		row.setComponentAlignment(columnSubject, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(columnSentTo, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(columnDate, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(columnSent, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(columnRead, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		tableBody.removeAllComponents();
		List<Message> data = ui.messagingService.findAll(loginUser.getUserUniqueId());
		for (Message m : data) {
			HorizontalLayout row1 = createDataRow(m);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private VerticalLayout tableBody;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
