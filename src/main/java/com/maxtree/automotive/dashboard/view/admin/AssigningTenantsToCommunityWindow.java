package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Tenant;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
		this.setWidth("413px");
		this.setHeight("151px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为社区分配租户");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		
		FormLayout hlayout = new FormLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		
		selector.setTextInputAllowed(false);
		selector.setIcon(VaadinIcons.CALC_BOOK);
		selector.setWidth("100%");
		selector.setHeight("27px");
		List<Tenant> allTenants = ui.communityService.findAllTenants();
		selector.setItems(allTenants);
		
		
		List<Tenant> assignedTenants = ui.communityService.findAssignedTenants(community.getCommunityUniqueId());
		outer:for(Tenant t : allTenants) {
			for(Tenant tenant : assignedTenants) {
				if (t.getTenantUniqueId() == tenant.getTenantUniqueId()) {
					selector.setValue(t);
					break outer;
				}
			}
		}
		
        hlayout.addComponent(selector);
        hlayout.setComponentAlignment(selector, Alignment.TOP_CENTER);
		
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
		mainLayout.addComponents(hlayout, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}
	
	/**
	 * 
	 * @param community
	 */
	private void apply(Community community) {
		
		ui.communityService.deleteCommunityTenants(community.getCommunityUniqueId());
		
		Tenant selected = selector.getValue();
		if (selected != null) {
			ui.communityService.insertCommunityTenants(community.getCommunityUniqueId(), selected.getTenantUniqueId());
		}
	}
	
	public static void open(Community community, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningTenantsToCommunityWindow w = new AssigningTenantsToCommunityWindow(community);
        w.btnApply.addClickListener(e -> {
        	w.apply(community);
			callback.onSuccessful();
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(community);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnApply;
	private Button btnOK;
	private ComboBox<Tenant> selector = new ComboBox<Tenant>("租户");
	private DashboardUI ui = (DashboardUI) UI.getCurrent();

}
