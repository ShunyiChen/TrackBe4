package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Company;
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

public class AssigningCompaniesToCommunityWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param material
	 */
	public AssigningCompaniesToCommunityWindow(Community community) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为社区分配机构");
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
		
		List<Company> allCompanies = ui.communityService.findAllCompanies(community.getCommunityUniqueId());
		List<Company> assignedCompanies = ui.communityService.findAssignedCompanies(community.getCommunityUniqueId());
		
		select = new TwinColSelect<>(null, allCompanies);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的机构");
		select.setRightColumnCaption("已分配的机构");
		
		List<Company> selected= new ArrayList<>();
		for (Company c : allCompanies) {
			for (Company assignedCompany : assignedCompanies) {
				if (assignedCompany.getCompanyUniqueId() == c.getCompanyUniqueId()) {
					selected.add(c);
				}
			}
		}
		// set select
		select.select(selected.toArray(new Company[selected.size()]));
		
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
	
	private void apply(Community community) {
		List<Company> oldAssignedCompanies = ui.communityService.findAssignedCompanies(community.getCommunityUniqueId());
		for (Company company : oldAssignedCompanies) {
			company.setCommunityUniqueId(0);
			ui.companyService.update(company);
		}
		
		Set<Company> set = select.getSelectedItems();
		List<Company> newAssignedCompanies = new ArrayList<>(set);
		for (Company company : newAssignedCompanies) {
			company.setCommunityUniqueId(community.getCommunityUniqueId());
			ui.companyService.update(company);
		}
	}
	
	public static void open(Community community, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningCompaniesToCommunityWindow w = new AssigningCompaniesToCommunityWindow(community);
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
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TwinColSelect<Company> select = null;

}
