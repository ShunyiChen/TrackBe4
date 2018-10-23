package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

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
		this.setWidth("980px");
		this.setHeight("525px");
		this.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
		
		Button markAllAsRead = new Button("全部标记已读");
		
		TabSheet tabSheet = new TabSheet();
		tabSheet.setWidth(100.0f, Unit.PERCENTAGE);
        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
//        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
//        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
       
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setMargin(false);
        hlayout.setSpacing(false);
        tabSheet.addTab(hlayout, "消息提醒");
		main.addComponent(tabSheet,"top:45px; left:10px;");
		main.addComponent(markAllAsRead,"top:40px; right:10px;");
		this.setContent(main);
		
		VerticalLayout navigationBar = new VerticalLayout();
//		navigationBar.setSpacing(false);
//		navigationBar.setMargin(false);
		navigationBar.setWidth("220px");
		navigationBar.setHeight("100%");
		
		HorizontalLayout unread = NavigationButton("未读", "2");
		HorizontalLayout all = NavigationButton("全部消息", "");
		navigationBar.addComponents(unread, all);
		navigationBar.setComponentAlignment(unread, Alignment.TOP_CENTER);
		navigationBar.setComponentAlignment(all, Alignment.TOP_CENTER);
		
		
//		HorizontalLayout toolbar = new HorizontalLayout();
//		toolbar.setSpacing(false);
//		toolbar.setMargin(false);
//		toolbar.addComponents(notificationImage,notificationLabel);
//		HorizontalLayout buttonLayout = new HorizontalLayout();
//		buttonLayout.setWidth("100px");
//		buttonLayout.setHeight("40px");
//		HorizontalLayout rightButtonLayout = new HorizontalLayout();
//		rightButtonLayout.setSizeUndefined();
//		rightButtonLayout.addComponents(btnOk,btnCancel);
//		rightButtonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
//		rightButtonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
//		buttonLayout.addComponent(rightButtonLayout);
//		buttonLayout.setComponentAlignment(rightButtonLayout, Alignment.MIDDLE_RIGHT);
	}
	
	/**
	 * 
	 * @param text
	 * @param unreadCount
	 * @return
	 */
	private HorizontalLayout NavigationButton(String text, String unreadCount) {
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.setHeight("36px");
		Label txt = new Label(text);
		Label unread = new Label(text);
		buttons.addComponents(txt,unread);
		buttons.setComponentAlignment(txt, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(unread, Alignment.MIDDLE_RIGHT);
		
		return buttons;
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
	
	private AbsoluteLayout main = new AbsoluteLayout();
	private Image notificationImage = new Image();
	private Label notificationLabel = new Label("消息列表");
	private TextField numField = new TextField();
	private Button btnOk = new Button("确定");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
