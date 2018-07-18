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

public class AssigningTenantsToCommunityWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param community
	 */
	public AssigningTenantsToCommunityWindow(Community community) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为社区分配租户");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label companyName = new Label(community.getCommunityName());
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
		
		List<Tenant> allTenants = ui.communityService.findUnassignedTenants(community.getCommunityUniqueId());
		List<Tenant> assignedTenants = ui.communityService.findAssignedTenants(community.getCommunityUniqueId());
		
		select = new TwinColSelect<>(null, allTenants);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的租户");
		select.setRightColumnCaption("已分配的租户");
		List<Tenant> selected = new ArrayList<>();
		for (Tenant t : allTenants) {
			for (Tenant assignedTenant : assignedTenants) {
				if (assignedTenant.getTenantUniqueId().intValue() == t.getTenantUniqueId().intValue()) {
					selected.add(t);
				}
			}
		}
		// set select
		select.select(selected.toArray(new Tenant[selected.size()]));
		
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
	 * @param community
	 * @return
	 */
	private boolean apply(Community community) {
		Set<Tenant> set = select.getSelectedItems();
		if (set.size() > 1) {
			Notifications.warning("只能为社区分配一个租户。");
			return false;
		}
		else {
			ui.tenantService.deleteTenantByCommunityUniqueId(community.getCommunityUniqueId());
			Iterator<Tenant> iter = set.iterator();
			while(iter.hasNext()) {
				Tenant t = iter.next();
				ui.tenantService.insertCommunityTenants(community.getCommunityUniqueId(), t.getTenantUniqueId());
			}
			return true;
		}
	}
	
	/**
	 * 
	 * @param community
	 * @param callback
	 */
	public static void open(Community community, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningTenantsToCommunityWindow w = new AssigningTenantsToCommunityWindow(community);
        w.btnApply.addClickListener(e -> {
        	if(w.apply(community)) {
        		callback.onSuccessful();
        	}
			
		});
        w.btnOK.addClickListener(e -> {
        	if(w.apply(community)) {
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
	private TwinColSelect<Tenant> select = null;
}
