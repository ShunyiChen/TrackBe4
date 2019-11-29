package com.maxtree.automotive.dashboard.view.user.todolist;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

public class ToDoListView extends VerticalLayout implements View {

    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private boolean flag;

    public ToDoListView() {
        this.addStyleName("FrontdeskView");
        DashboardEventBus.register(this);

        HorizontalLayout toolbar = new HorizontalLayout();
        Image menu = new Image(null, new ThemeResource("user/frontdesk/images/menu.png"));
        menu.addStyleName("my-img-button");
        menu.addClickListener(e ->{
            flag = !flag;
            ui.mainView.hideMenu(flag);
        });
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
}
