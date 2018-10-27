package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author chens
 *
 */
public class NotificationsManagementWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param viewName
	 * @param inputView
	 */
	public NotificationsManagementWindow(String viewName, InputViewIF inputView) {
		this.viewName = viewName;
		this.inputView = inputView;
		initComponents();
	}
	
	private void initComponents() {
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("显示通知列表");
		this.setWidth("980px");
		this.setHeight("525px");
		this.addStyleName("NotificationsManagementWindow");
		Callback closeBySelf = new Callback() {

			@Override
			public void onSuccessful() {
				close();
			}
		};
		table = new NotificationTable("通知列表", inputView, closeBySelf, changeQueryRanges);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		progressBar.setIndeterminate(true);
		Button markAllAsRead = new Button("全部标记已读");
		markAllAsRead.addClickListener(e->{
			ui.messagingService.allMarkAsRead(loggedInUser.getUserUniqueId());
			if(unread.isSelected()) {
				ui.access(new Runnable() {

					@Override
					public void run() {
						loadUnreaded(10, 1);
					}
				});
			}
			else {
				ui.access(new Runnable() {

					@Override
					public void run() {
						loadAll(10, 1);
					}
				});
			}
		});
		TabSheet tabSheet = new TabSheet();
		tabSheet.setWidth(100.0f, Unit.PERCENTAGE);
        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        VerticalLayout navigationBar = new VerticalLayout();
		navigationBar.setWidth("220px");
		navigationBar.setHeightUndefined();
		navigationBar.setSpacing(false);
		navigationBar.setMargin(false);
		navigationBar.addStyleName("NotificationsManagementWindow_navigationBar");
		buttonGroup.add(unread);
		buttonGroup.add(all);
		navigationBar.addComponents(unread,all);
		navigationBar.setComponentAlignment(unread, Alignment.TOP_CENTER);
		navigationBar.setComponentAlignment(all, Alignment.TOP_CENTER);
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setWidth("100%");
		hlayout.setHeight("100%");
        hlayout.setMargin(false);
        hlayout.setSpacing(false);
		hlayout.addComponents(navigationBar, table);
		hlayout.setComponentAlignment(navigationBar, Alignment.TOP_LEFT);
		hlayout.setComponentAlignment(table, Alignment.TOP_RIGHT);
		hlayout.setExpandRatio(table, 1);
        tabSheet.addTab(hlayout, "通知");
		main.addComponent(tabSheet,"top:15px; left:10px;");
		main.addComponent(markAllAsRead,"top:10px; right:10px;");
		this.setContent(main);
		
		ui.access(new Runnable() {

			@Override
			public void run() {
				loadUnreaded(10, 1);
			}
		});
		unread.addLayoutClickListener(e->{
			if(unread.isSelected()) {
				return;
			}
			loadUnreaded(10, 1);
			
		});
		all.addLayoutClickListener(e->{
			if(all.isSelected()) {
				return;
			}
			loadAll(10, 1);
		});
	}
	
	/**
	 * 
	 */
	private void loadUnreaded(int limit, int offset) {
		showProgressBar();
		Thread t = new Thread() {
			@Override
			public void run() {
				ui.access(new Runnable() {
					@Override
					public void run() {
						for (NavigationButton b : buttonGroup) {
							if (b == unread) {
								b.select();
							} else {
								b.deselect();
							}
						}
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								hideProgressBar();
								ui.push();
							}
						};
						loadNotifications(true, callback,limit,offset);
					}
				});
			}
		};
		t.start();
	}
	
	/**
	 * 
	 */
	private void loadAll(int limit, int offset) {
		showProgressBar();
		Thread t = new Thread() {
			@Override
			public void run() {
				ui.access(new Runnable() {
					@Override
					public void run() {
						for (NavigationButton b : buttonGroup) {
							if (b == all) {
								b.select();
							} else {
								b.deselect();
							}
						}
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								hideProgressBar();
								ui.push();
							}
						};
						loadNotifications(false, callback,limit,offset);
						
					}
				});
			}
		};
		t.start();
	}
	
	
	/**
	 * 
	 */
	private void showProgressBar() {
		main.addComponent(progressBar,"top:50%; left:50%;");
	}
	
	/**
	 * 
	 */
	private void hideProgressBar() {
		main.removeComponent(progressBar);
	}
	
	
	/**
	 * 
	 * @param onlyShowingUnread
	 * @param hideProgressbar
	 * @param limit
	 * @param offset
	 */
	private void loadNotifications(boolean onlyShowingUnread, Callback hideProgressbar, int limit, int offset) {
		if (onlyShowingUnread) {
			List<Notification> notificationList = ui.messagingService.findAllNotificationsByPaging(loggedInUser.getUserUniqueId(),true,viewName,limit,offset);
			table.setItems(notificationList);
			unread.setUnreadCount(notificationList.size());
			hideProgressbar.onSuccessful();
		} else {
			List<Notification> notificationList = ui.messagingService.findAllNotificationsByPaging(loggedInUser.getUserUniqueId(),false,viewName,limit,offset);
			table.setItems(notificationList);
			all.setUnreadCount(notificationList.size());
			hideProgressbar.onSuccessful();
		}
	}
	
	/**
	 * 
	 */
	private Callback2 changeQueryRanges = new Callback2() {

		@Override
		public void onSuccessful(Object...objects) {
			
			int limit = Integer.parseInt(objects[0].toString());
			int offset = Integer.parseInt(objects[1].toString());
			
			Callback callback = new Callback() {

				@Override
				public void onSuccessful() {
					
					hideProgressBar();
					ui.push();
				}
			};
			loadNotifications(false, callback,limit,offset);
		}
	};
	
	/**
	 * 
	 * @param viewName
	 * @param inputView
	 */
	public static void open(String viewName,InputViewIF inputView) {
		// DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		NotificationsManagementWindow w = new NotificationsManagementWindow(viewName, inputView);
		UI.getCurrent().addWindow(w);
		w.center();
	}
	
	private NavigationButton unread = new NavigationButton("未读",0);
	private NavigationButton all = new NavigationButton("全部消息",0);
	private User loggedInUser;
	private ProgressBar progressBar = new ProgressBar(0.0f);
	private List<NavigationButton> buttonGroup = new ArrayList<>();
	private NotificationTable table;
	private AbsoluteLayout main = new AbsoluteLayout();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private String viewName = "";
	private InputViewIF inputView;
}

/**
 * 
 * @author Chen
 *
 */
class NavigationButton extends HorizontalLayout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param text
	 * @param unreadCount
	 */
	public NavigationButton(String text, int count) {
		setWidth("100%");
		setHeight("36px");
		addStyleName("NotificationsManagementWindow_buttons");
		Label txt = new Label(text);
		
		txt.addStyleName("NotificationsManagementWindow_txt");
		unread.addStyleName("NotificationsManagementWindow_unread");
		
		addComponents(txt,unread);
		setComponentAlignment(txt, Alignment.MIDDLE_LEFT);
		setComponentAlignment(unread, Alignment.MIDDLE_RIGHT);
		
		setUnreadCount(count);
	}
	
	/**
	 * 
	 */
	public void select() {
		selected = true;
		this.removeStyleName("NotificationsManagementWindow_buttons");
		this.addStyleName("NotificationsManagementWindow_buttons_selected");
	}
	
	/**
	 * 
	 */
	public void deselect() {
		selected = false;
		this.removeStyleName("NotificationsManagementWindow_buttons_selected");
		this.addStyleName("NotificationsManagementWindow_buttons");
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * 
	 * @param count
	 */
	public void setUnreadCount(int count) {
		unread.setValue(""+count);
	}


	private Label unread = new Label("0");
	private boolean selected;
}