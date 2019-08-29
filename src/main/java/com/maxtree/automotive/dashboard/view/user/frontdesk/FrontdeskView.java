package com.maxtree.automotive.dashboard.view.user.frontdesk;

import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

public class FrontdeskView extends VerticalLayout implements View {


    public FrontdeskView() {
        this.addStyleName("FrontdeskView");
        DashboardEventBus.register(this);

        HorizontalLayout toolbar = new HorizontalLayout();
        Image menu = new Image(null, new ThemeResource("user/frontdesk/images/menu.png"));
        Button btn = new Button("sdsd");
        toolbar.setWidth("100%");
        toolbar.setHeight("56px");
        toolbar.addStyleName("FrontdeskView_toolbar");
        toolbar.addComponent(menu);
        toolbar.setComponentAlignment(menu, Alignment.MIDDLE_LEFT);

        addComponent(toolbar);
        setComponentAlignment(toolbar, Alignment.TOP_LEFT);
        setExpandRatio(toolbar, 0);


    }

    public void create() {

    }

    public void save() {

    }

    public void discard() {

    }

    public void commit() {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {

    }
}
