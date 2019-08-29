package com.maxtree.automotive.dashboard.view;

import com.maxtree.automotive.dashboard.view.check.BusinessCheckView;
import com.maxtree.automotive.dashboard.view.doublecheck.DoubleCheckView;
import com.maxtree.automotive.dashboard.view.front.FrontView;
import com.maxtree.automotive.dashboard.view.quality.QCView;
import com.maxtree.automotive.dashboard.view.search.SearchView;
import com.maxtree.automotive.dashboard.view.shelf.ShelfView;
import com.maxtree.automotive.dashboard.view.imaging.ImagingManagerView;
import com.maxtree.automotive.dashboard.view.imaging.ImagingInputView;
import com.maxtree.automotive.dashboard.view.imaging.ImagingQualityView;
import com.maxtree.automotive.dashboard.view.finalcheck.FinalCheckView;
import com.maxtree.automotive.dashboard.view.user.frontdesk.FrontdeskView;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.icons.VaadinIcons;

public enum DashboardViewType {
    
	INPUT("前台录入", FrontdeskView.class, VaadinIcons.HOME, true),
	QUALITY("质量检查", QCView.class, VaadinIcons.SPLIT, true),
    CHECK("业务审核", BusinessCheckView.class, VaadinIcons.TABLE, true),
	SEARCH("车辆查询", SearchView.class, VaadinIcons.SEARCH, true),
	DOUBLECHECK("确认审档", DoubleCheckView.class, VaadinIcons.ADD_DOCK, true),
	SHELF("上架下架", ShelfView.class, VaadinIcons.ROCKET, true),
	IMAGING_MANAGER("影像管理", ImagingManagerView.class, VaadinIcons.STORAGE, true),
	IMAGING_INPUT("影像录入", ImagingInputView.class, VaadinIcons.PENCIL, true),
	IMAGING_QUALITY("影像质检", ImagingQualityView.class, VaadinIcons.CLIPBOARD_CHECK, true),
	FINAL_CHECK("最终审档", FinalCheckView.class, VaadinIcons.CAR, true);
	
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
