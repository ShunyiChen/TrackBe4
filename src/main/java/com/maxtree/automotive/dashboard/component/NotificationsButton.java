package com.maxtree.automotive.dashboard.component;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.admin.NotificationsManagementWindow;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class NotificationsButton extends Button implements com.vaadin.ui.Button.ClickListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String STYLE_UNREAD = "unread";
    public static final String ID = "dashboard-notifications";
    /**
     * 
     */
    public NotificationsButton() {
        setIcon(VaadinIcons.BELL);
        setId(ID);
        addStyleName("notifications");
        addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.addClickListener(this);
        DashboardEventBus.register(this);
    }

    @Subscribe
    public void updateNotificationsCount(NotificationsCountUpdatedEvent event) {
    	DashboardMenu.getInstance().updateNotificationsCount(event.getCount());
    	setUnreadCount(event.getCount());
    }

    /**
     * 
     * @param count
     */
    public void setUnreadCount(final int count) {
        setCaption(String.valueOf(count));
        String description = "事件提醒";
        if (count > 0) {
            addStyleName(STYLE_UNREAD);
            description += " (" + count + " 未执行)";
        } else {
            removeStyleName(STYLE_UNREAD);
        }
        setDescription(description);
    }

	@Override
	public void buttonClick(ClickEvent event) {
		NotificationsManagementWindow.open();
	}
    
}