package com.maxtree.automotive.dashboard.view;

import com.maxtree.automotive.dashboard.view.business.BusinessCheckView;
import com.maxtree.automotive.dashboard.view.front.FrontView;
import com.maxtree.automotive.dashboard.view.qc.QCView;
import com.maxtree.automotive.dashboard.view.search.SearchView;
import com.maxtree.automotive.dashboard.view.shelf.ShelfView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.icons.VaadinIcons;

public enum DashboardViewType {
    
	DASHBOARD("扫描录入", FrontView.class, FontAwesome.HOME, true),
    SENDBACK("质量检查", QCView.class, FontAwesome.BAR_CHART_O, true),
    HISTORY("业务审核", BusinessCheckView.class, FontAwesome.TABLE, true),
	SEARCH("数据查询", SearchView.class, FontAwesome.SEARCH, true),
	SHELF("上架下架", ShelfView.class, VaadinIcons.ADD_DOCK, true);
	
    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
}
