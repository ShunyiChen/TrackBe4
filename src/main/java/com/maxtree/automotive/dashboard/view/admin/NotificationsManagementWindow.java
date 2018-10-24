package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
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
	 */
	public NotificationsManagementWindow() {
		initComponents();
	}
	
	private void initComponents() {
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("显示通知列表");
		this.setWidth("980px");
		this.setHeight("525px");
		this.addStyleName("NotificationsManagementWindow");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		
		progressBar.setIndeterminate(true);
		Button markAllAsRead = new Button("全部标记已读");
		TabSheet tabSheet = new TabSheet();
		tabSheet.setWidth(100.0f, Unit.PERCENTAGE);
        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        
        VerticalLayout navigationBar = new VerticalLayout();
		navigationBar.setWidth("220px");
		navigationBar.setHeightUndefined();
		navigationBar.setSpacing(false);
		navigationBar.setMargin(false);
		navigationBar.addStyleName("NotificationsManagementWindow_navigationBar");
		
		NavigationButton unread = new NavigationButton("未读", "2342");
		NavigationButton all = new NavigationButton("全部消息", "2");
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
		
		unread.addLayoutClickListener(e->{
			if(unread.isSelected()) {
				return;
			}
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
							loadNotifications(true, callback);
						}
					});
				}
			};
			t.start();
			
		});
		all.addLayoutClickListener(e->{
			if(all.isSelected()) {
				System.out.println("return");
				return;
			}
			
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
							loadNotifications(false, callback);
							
						}
					});
				}
			};
			t.start();
		});
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
	 * @param callback
	 */
	private void loadNotifications(boolean onlyShowingUnread, Callback callback) {
		List<Notification> lst = new ArrayList<Notification>();
		for(int i = 0; i < 5; i++) {
			Notification n1 = new Notification();
			n1.setChecked(false);
			n1.setContent("都是错的商店出售的山地车山地车山地车是"+i);
			n1.setRelativeTime(new Date());
			n1.setUserName(loggedInUser.getUserName());
			lst.add(n1);
			Notification n2 = new Notification();
			n2.setChecked(false);
			n2.setContent("22323rfddfdfdsvfdv反对法vDVD发v地方v地方v地方反感不反感边防官兵v反对法v地方v的v发的发的发模拟"+i);
			n2.setRelativeTime(new Date());
			n2.setUserName(loggedInUser.getUserName());
			lst.add(n2);
		}
		
		table.setItems(lst);
		callback.onSuccessful();
	}
	
	/**
	 * 
	 * @param frame
	 * @param callback
	 */
	public static void open() {
		// DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		NotificationsManagementWindow w = new NotificationsManagementWindow();
		UI.getCurrent().addWindow(w);
		w.center();
	}
	
	private User loggedInUser;
	private ProgressBar progressBar = new ProgressBar(0.0f);
	private List<NavigationButton> buttonGroup = new ArrayList<>();
	private NotificationTable table = new NotificationTable("通知列表");
	private AbsoluteLayout main = new AbsoluteLayout();
	private Image notificationImage = new Image();
	private Label notificationLabel = new Label("消息列表");
	private TextField numField = new TextField();
	private Button btnOk = new Button("确定");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

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
	public NavigationButton(String text, String unreadCount) {
		setWidth("100%");
		setHeight("36px");
		addStyleName("NotificationsManagementWindow_buttons");
		Label txt = new Label(text);
		Label unread = new Label(unreadCount);
		
		txt.addStyleName("NotificationsManagementWindow_txt");
		unread.addStyleName("NotificationsManagementWindow_unread");
		
		addComponents(txt,unread);
		setComponentAlignment(txt, Alignment.MIDDLE_LEFT);
		setComponentAlignment(unread, Alignment.MIDDLE_RIGHT);
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
	
	private boolean selected;
}