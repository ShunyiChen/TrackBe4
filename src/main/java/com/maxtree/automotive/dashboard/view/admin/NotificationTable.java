package com.maxtree.automotive.dashboard.view.admin;

import java.util.Date;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Openwith;
import com.maxtree.automotive.dashboard.component.TimeAgo;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class NotificationTable extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param title
	 * @param inputView
	 * @param closeNotificationsManagementWindow
	 * @param changeQueryRanges
	 */
	public NotificationTable(String title,InputViewIF inputView, Callback closeNotificationsManagementWindow,Callback2 changeQueryRanges) {
		this.title = title;
		this.inputView = inputView;
		this.closeNotificationsManagementWindow = closeNotificationsManagementWindow;
		this.changeQueryRanges = changeQueryRanges;
		initComponents();
	}

	private void initComponents() {
		this.setSizeFull();
		this.setSpacing(false);
		this.setMargin(false);
		this.addStyleName("NotificationTable");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setHeight("37px");
		Label titleLabel = new Label(title);
		
		Label range = new Label(rangeFrom+"~"+rangeTo);
		Image left = new Image(null, new ThemeResource("img/adminmenu/chevron-left.png"));
		Image right = new Image(null, new ThemeResource("img/adminmenu/chevron-right.png"));
		left.addStyleName("NotificationTable_left");
		right.addStyleName("NotificationTable_left");
		left.addClickListener(e->{
			if(rangeFrom > 10) {
				rangeFrom -= 10;
				rangeTo -= 10;
				range.setValue(rangeFrom+"~"+rangeTo);
				changeQueryRanges.onSuccessful(10,rangeFrom);
			}
			
		});
		right.addClickListener(e->{
			rangeFrom += 10;
			rangeTo += 10;
			range.setValue(rangeFrom+"~"+rangeTo);
			changeQueryRanges.onSuccessful(10, rangeFrom);
		});
		
		HorizontalLayout paging = new HorizontalLayout();
		paging.setSpacing(false);
		paging.setMargin(false);
		paging.setHeight("37px");
		paging.setWidthUndefined();
		paging.addComponents(range,Box.createHorizontalBox(5),left,Box.createHorizontalBox(5),right);
		paging.setComponentAlignment(range, Alignment.MIDDLE_CENTER);
		paging.setComponentAlignment(right, Alignment.TOP_CENTER);
		paging.setComponentAlignment(left, Alignment.TOP_CENTER);
		
		titleLabel.setWidth("100%");
		header.addComponents(titleLabel,paging);
		header.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(paging, Alignment.MIDDLE_RIGHT);
		header.addStyleName("NotificationTable_header");
		
		Panel scroll = new Panel();
		scroll.setSizeFull();
		scroll.addStyleName("NotificationTable_scroll");
		body.setSpacing(false);
		body.setMargin(false);
		body.setWidth("100%");
		body.setHeightUndefined();
		
		scroll.setContent(body);
		
		this.addComponents(header,scroll);
		this.setComponentAlignment(header, Alignment.TOP_LEFT);
		this.setComponentAlignment(scroll, Alignment.TOP_LEFT);
		this.setExpandRatio(header, 0);
		this.setExpandRatio(scroll, 1);
	}
	
	/**
	 * 
	 * @param notificationList
	 */
	public void setItems(List<Notification> notificationList) {
		body.removeAllComponents();
		
		for(Notification n : notificationList) {
			HorizontalLayout row = createRow(n);
			body.addComponent(row);
		}
	}
	
	/**
	 * 
	 * @param notification
	 * @return
	 */
	private HorizontalLayout createRow(Notification notification) {
		HorizontalLayout row = new HorizontalLayout();
		row.setWidth("100%");
		row.setHeight("40px");
		long duration = new Date().getTime() - notification.getSendTime().getTime();
		Label contentLabel = new Label(notification.getSubject());
		Label relativeTimeLabel = new Label(new TimeAgo().toDuration(duration));
		Image checkedImg = new Image(null, new ThemeResource("img/adminmenu/check.png"));
		checkedImg.addStyleName("NotificationTable_checkedImg");
		contentLabel.addStyleName("NotificationTable_contentLabel");
		relativeTimeLabel.addStyleName("NotificationTable_contentLabel");
		checkedImg.setVisible(false);
		if(notification.isMarkedAsRead()) {
			checkedImg.setVisible(true);
		}
		VerticalLayout cell = new VerticalLayout();
		cell.setSpacing(false);
		cell.setMargin(false);
		cell.setWidth("75%");
		cell.setHeight("100%");
		cell.addComponent(contentLabel);
		cell.setComponentAlignment(contentLabel, Alignment.MIDDLE_LEFT);
		contentLabel.setWidth("100%");
		Image infoImg = new Image(null, new ThemeResource("img/adminmenu/info-circle-o.png"));
		Image waringImg = new Image(null, new ThemeResource("img/adminmenu/warning.png"));
		Image img = notification.isWarning()?waringImg:infoImg;
		row.addComponents(img,cell, relativeTimeLabel, checkedImg);
		row.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(relativeTimeLabel, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(checkedImg, Alignment.MIDDLE_LEFT);
		row.setExpandRatio(cell, 0.75f);
		row.setExpandRatio(relativeTimeLabel, 0.2f);
		row.setExpandRatio(checkedImg, 0.05f);
		row.addStyleName("NotificationTable_row");
		row.addLayoutClickListener(e->{
			if(e.getMouseEventDetails().isDoubleClick()) {
				Message msg = ui.messagingService.findById(notification.getMessageUniqueId());
				Openwith.open(msg, inputView, closeNotificationsManagementWindow);
			}
		});
		return row;
	}
	
	private VerticalLayout body = new VerticalLayout();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private User loggedInUser;
	private String title;
	private InputViewIF inputView;
	private Callback closeNotificationsManagementWindow;
	private int rangeFrom = 1;
	private int rangeTo = 10;
	private Callback2 changeQueryRanges;
}
