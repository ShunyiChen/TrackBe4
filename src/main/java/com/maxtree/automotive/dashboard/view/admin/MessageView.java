package com.maxtree.automotive.dashboard.view.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class MessageView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public MessageView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("消息标题",125), new GridColumn("发送至",125), new GridColumn("发送时间",125),new GridColumn("发送次数",115),new GridColumn("读取率",80),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<Message> lst = ui.messagingService.findAll(loggedInUser.getUserUniqueId());
		for (Message m : lst) {
			Object[] rowData = generateOneRow(m);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("消息列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.F1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							Message msg = (Message) objects[0];
							Object[] rowData = generateOneRow(msg);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditBroadCastWindow.open(callback);
				} else {
	        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
	        	}
			}
		};
		grid.setAddEvent(addEvent);
		main.addComponents(grid);
		main.setComponentAlignment(grid, Alignment.TOP_CENTER);
		
		this.addComponent(main);
		this.setComponentAlignment(main, Alignment.TOP_CENTER);
		this.setExpandRatio(main, 1);
	}
	
	/**
	 * 
	 * @param company
	 * @return
	 */
	private Object[] generateOneRow(Message message) {
		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("设置定时发送", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					if (loggedInUser.isPermitted(PermissionCodes.L3)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								Message msg = ui.messagingService.findById(message.getMessageUniqueId());
								Object[] rowData = generateOneRow(msg);
								grid.setValueAt(new CustomGridRow(rowData),message.getMessageUniqueId());
							}
						};
						FrequencySettingsWindow.open(message, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			
			menu.addSeparator();
			menu.addItem("编辑", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					Callback callback = new Callback() {

						@Override
						public void onSuccessful() {
							Message msg = ui.messagingService.findById(message.getMessageUniqueId());
							Object[] rowData = generateOneRow(msg);
							grid.setValueAt(new CustomGridRow(rowData),message.getMessageUniqueId());
						}
					};
					EditBroadCastWindow.edit(message, callback);
				}
			});
			menu.addItem("从列表删除", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.L4)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								
								Timer timer = TB4MessagingSystem.SCHEDULED.get(message.getMessageUniqueId());
								if (timer != null) {
									timer.cancel();
								}
								
								ui.messagingService.markAsDeleted(message.getMessageUniqueId());
								//刷新UI
								grid.deleteRow(message.getMessageUniqueId());
							}
						};
						
						MessageBox.showMessage("提示", "请确定是否删除当前消息。", MessageBox.WARNING, event, "确定");
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			
			menu.open(e.getClientX(), e.getClientY());
		});
		
		StringBuilder names = new StringBuilder();
		List<MessageRecipient> recipients = ui.messagingService.findRecipientsByMessageId(message.getMessageUniqueId());
		for (MessageRecipient mr : recipients) {
			names.append(mr.getRecipientName());
			names.append(";");
		}
		
		return new Object[] {message.getSubject(),names,format.format(message.getDateCreated()),message.getSentTimes()+"",message.getReadRate()+"%",img,message.getMessageUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
	private String pattern = "yyyy年MM月dd日 HH:mm:ss";
	private SimpleDateFormat format = new SimpleDateFormat(pattern);
}
