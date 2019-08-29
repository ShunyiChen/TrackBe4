package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class LoggingWindow extends Window {

    public LoggingWindow() {
        initComponents();
    }

    private void initComponents() {
        this.setModal(true);
        this.setClosable(true);
        this.setResizable(true);
        this.setDraggable(true);
        this.setCaption("日志");

        TabSheet ts = new TabSheet();
        ts.setHeight(100.0f, Unit.PERCENTAGE);
        ts.addStyleName(ValoTheme.TABSHEET_FRAMED);
        ts.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        LoggingTableForUser loggingTable = new LoggingTableForUser();
        SystemLogging systemLogging = new SystemLogging();

        ts.addTab(loggingTable, "用户日志");
        ts.addTab(systemLogging, "系统日志");

        this.setContent(ts);
    }


    public static void open() {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        final LoggingWindow w = new LoggingWindow();
        UI.getCurrent().addWindow(w);
        w.center();
        w.setWindowMode(WindowMode.MAXIMIZED);
    }
}
