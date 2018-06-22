package com.maxtree.automotive.dashboard.view;

import com.maxtree.automotive.dashboard.DashboardNavigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);
        menu = new DashboardMenu();
        addComponent(menu);

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new DashboardNavigator(content);
    }
    
    private DashboardMenu menu;
}
