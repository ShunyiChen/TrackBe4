package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AssigningCommunitiesToSiteWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param site
	 */
	public AssigningCommunitiesToSiteWindow(Site site) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为站点分配社区");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
 
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label roleName = new Label(site.getSiteName());
		HorizontalLayout title = new HorizontalLayout();
		title.setWidthUndefined();
		title.setHeightUndefined();
		title.setSpacing(false);
		title.setMargin(false);
		title.addComponents(img, Box.createHorizontalBox(5), roleName);
		title.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		title.setComponentAlignment(roleName, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSizeFull();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);

		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		User loggedInUser = ui.userService.getUserByUserName(username);
		List<Community> allCommunities = ui.communityService.findAll(loggedInUser);
		
		select = new TwinColSelect<>(null, allCommunities);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的社区");
		select.setRightColumnCaption("已分配的社区");
		
		List<Community> selectedCommunities = new ArrayList<>();
		
		List<Community> assignedCommunities = ui.siteService.assignedCommunities(site.getSiteUniqueId());
		
		for (Community c : allCommunities) {
			for (Community assignedCommunity : assignedCommunities) {
				if (assignedCommunity.getCommunityUniqueId().intValue() == c.getCommunityUniqueId().intValue()) {
					selectedCommunities.add(c);
				}
			}
		}
		// set select
		select.select(selectedCommunities.toArray(new Community[selectedCommunities.size()]));
		
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
	
	private void apply(Site site) {
		Set<Community> set = select.getSelectedItems();
		List<Community> list = new ArrayList<>(set);
		// update database
		ui.siteService.updateSiteCommunities(site.getSiteUniqueId(), list);
	}
	
	public static void open(Callback callback, Site site) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningCommunitiesToSiteWindow w = new AssigningCommunitiesToSiteWindow(site);
        w.btnApply.addClickListener(e -> {
        	w.apply(site);
			callback.onSuccessful();
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(site);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TwinColSelect<Community> select;
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();

}
