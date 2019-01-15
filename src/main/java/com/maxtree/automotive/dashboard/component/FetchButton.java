package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class FetchButton extends Button {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String STYLE_UNREAD = "unread";
    public static final String ID = "dashboard-notifications";
    /**
     * 
     */
    public FetchButton() {
        setIcon(VaadinIcons.BELL);
        setId(ID);
        addStyleName("notifications");
        addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        DashboardEventBus.register(this);
    }

    /**
     * 
     * @param count
     */
    public void setUnreadCount(final int count) {
        setCaption(String.valueOf(count));
        String description = "业务队列";
        if (count > 0) {
            addStyleName(STYLE_UNREAD);
            description += " (" + count + " 未提取)";
        } else {
            removeStyleName(STYLE_UNREAD);
        }
        setDescription(description);
    }
    
    /**
     * 
     */
    public void changeFriendlyColor() {
    	addStyleName(ValoTheme.BUTTON_FRIENDLY);
    }
    
    public void restoreColor() {
    	this.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
    }
}