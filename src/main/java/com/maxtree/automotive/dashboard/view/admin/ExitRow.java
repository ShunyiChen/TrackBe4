package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;

public class ExitRow extends FlexTableRow {

    /**
     *
     * @param rootView
     */
    public ExitRow(AdminMainView rootView) {
        this.rootView = rootView;
    }

    @Override
    public String getSearchTags() {
        return "退出,"+getTitle();
    }

    @Override
    public int getOrderID() {
        return 7;
    }

    @Override
    public String getTitle() {
        return "退出系统";
    }

    @Override
    public String getImageName() {
        return "exit.png";
    }

    private AdminMainView rootView;
}
