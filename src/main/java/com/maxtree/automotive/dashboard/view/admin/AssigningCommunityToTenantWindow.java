package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Tenant;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AssigningCommunityToTenantWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param community
	 */
	public AssigningCommunityToTenantWindow(Tenant tenant) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为租户分配一个社区");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label companyName = new Label(tenant.getTenantName());
		HorizontalLayout title = new HorizontalLayout();
		title.setWidthUndefined();
		title.setHeightUndefined();
		title.setSpacing(false);
		title.setMargin(false);
		title.addComponents(img, Box.createHorizontalBox(5), companyName);
		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		title.setComponentAlignment(companyName, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		
		List<Community> allCommunities = ui.tenantService.findUnassignedCommunities(tenant.getTenantUniqueId());
		List<Community> assignedCommunities = ui.tenantService.findAssignedCommunites(tenant.getTenantUniqueId());
		
		select = new TwinColSelect<>(null, allCommunities);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的社区");
		select.setRightColumnCaption("已分配的社区");
		List<Community> selected = new ArrayList<>();
		for (Community c : allCommunities) {
			for (Community assignedCommunity : assignedCommunities) {
				if (assignedCommunity.getCommunityUniqueId().intValue() == c.getCommunityUniqueId().intValue()) {
					selected.add(c);
				}
			}
		}
		// set select
		select.select(selected.toArray(new Community[selected.size()]));
		
        hlayout.addComponent(select);
        hlayout.setComponentAlignment(select, Alignment.TOP_CENTER);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnOK = new Button("确定");
		btnApply = new Button("应用");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("196px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnOK, btnApply);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_RIGHT);
		subButtonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_RIGHT);
		subButtonPane.setComponentAlignment(btnApply, Alignment.BOTTOM_RIGHT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(title, hlayout, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}
	
	/**
	 * 
	 * @param tenant
	 * @return
	 */
	private boolean apply(Tenant tenant) {
		Set<Community> set = select.getSelectedItems();
		if (set.size() > 1) {
			Notifications.warning("只能为租户分配一个社区。");
			return false;
		}
		else {
			ui.tenantService.deleteCommunityByTenantUniqueId(tenant.getTenantUniqueId());
			Iterator<Community> iter = set.iterator();
			while(iter.hasNext()) {
				Community c = iter.next();
				ui.tenantService.insertCommunityTenants(c.getCommunityUniqueId(), tenant.getTenantUniqueId());
			}
			return true;
		}
	}
	
	/**
	 * 
	 * @param tenant
	 * @param callback
	 */
	public static void open(Tenant tenant, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningCommunityToTenantWindow w = new AssigningCommunityToTenantWindow(tenant);
        w.btnApply.addClickListener(e -> {
        	if(w.apply(tenant)) {
        		callback.onSuccessful();
        	}
		});
        w.btnOK.addClickListener(e -> {
        	if(w.apply(tenant)) {
        		w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TwinColSelect<Community> select = null;
}
