package com.maxtree.automotive.dashboard.component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.admin.NotificationsManagementWindow;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class NotificationsPopup extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param viewName
	 */
	public NotificationsPopup(String viewName) {
		this.viewName = viewName;
		initComponents();
	}
	
	private void initComponents() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		setWidth(300.0f, Unit.PIXELS);
		addStyleName("notifications");
		setClosable(false);
		setResizable(false);
		setDraggable(false);
		addCloseShortcut(KeyCode.ESCAPE);
    	main.setSpacing(false);
    	setContent(main);
        
        Label title = new Label("事件提醒");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        main.addComponent(title);

        Panel scrollPane = new Panel();
    	scrollPane.addStyleName("reminder-scrollpane");
    	scrollPane.setHeight("220px");
    	scrollPane.setWidth("100%");
        VerticalLayout listLayout = new VerticalLayout();
        
        List<Notification> list = ui.messagingService.findAllNotifications(loggedInUser.getUserUniqueId(), true, viewName);
        for(Notification n : list) {
         	
         	Label subjectLabel = new Label();
         	subjectLabel.addStyleName("notification-title");
         	subjectLabel.setValue(n.getSubject());
         	
         	Label timeLabel = new Label();
         	Date dateCreated = n.getSendTime();
         	long duration = new Date().getTime() - dateCreated.getTime();
         	timeLabel.setValue(new TimeAgo().toDuration(duration));
         	timeLabel.addStyleName("notification-time");
         	
         	Label contentLabel = new Label();
         	
         	
         	VerticalLayout item = new VerticalLayout();
         	item.setMargin(false);
         	item.setSpacing(false);
         	item.addStyleName("notification-item");
            item.addComponents(subjectLabel,timeLabel,contentLabel);
            item.addStyleName("switchbutton");
            item.addStyleName("NotificationsPopup_item");
            listLayout.addComponent(item);
        }
        
        scrollPane.setContent(listLayout);
        main.addComponent(scrollPane);
        main.setExpandRatio(scrollPane, 0.9f);
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        footer.setSpacing(false);
        Button showAll = new Button("查看全部事件");
        showAll.addClickListener(e->{
        	close();
        	NotificationsManagementWindow.open(DashboardViewType.INPUT.getViewName());
        });
        
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        main.addComponent(footer);
        main.setExpandRatio(footer, 0.1f);
	}
	
	/**
	 * 
	 * @param event
	 */
	public void open(ClickEvent event) {
		if(!isAttached()) {
			setPositionY(event.getClientY() - event.getRelativeY() + 40);
			ui.addWindow(this);
			focus();
		}
		else {
			close();
		}
	}
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private String viewName;
}
