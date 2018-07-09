package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.User;
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

public class AssigningBusinessesToUserWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param material
	 */
	public AssigningBusinessesToUserWindow(User user) {
		this.setWidth("613px");
		this.setHeight("513px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为用户分配业务类型");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		Image img = new Image(null, new ThemeResource("img/adminmenu/userrole.png"));
		Label companyName = new Label(user.getUserName());
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
		List<Business> allBusinesses = ui.businessService.findAllByCompanyUniqueId(user.getCompanyUniqueId());
		List<Business> assignedBusinesses = ui.userService.findAssignedBusinesses(user.getUserUniqueId());
		select = new TwinColSelect<>(null, allBusinesses);
		select.setWidth("100%");
		select.setRows(14);
		select.setLeftColumnCaption("未分配的业务");
		select.setRightColumnCaption("已分配的业务");
		
		List<Business> selected = new ArrayList<>();
		for (Business b : allBusinesses) {
			for (Business assignedBusiness : assignedBusinesses) {
				if (assignedBusiness.getBusinessUniqueId() == b.getBusinessUniqueId()) {
					selected.add(b);
				}
			}
		}
		// set select
		select.select(selected.toArray(new Business[selected.size()]));
		
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
	 * @param user
	 */
	private void apply(User user) {
		ui.userService.deleteBusinesses(user.getUserUniqueId());
		
		Set<Business> set = select.getSelectedItems();
		List<Business> lstBusiness = new ArrayList<>(set);
		
		ui.userService.assignBusinesses(user.getUserUniqueId(), lstBusiness);
	}
	
	/**
	 * 
	 * @param user
	 */
	public static void open(User user) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningBusinessesToUserWindow w = new AssigningBusinessesToUserWindow(user);
        w.btnApply.addClickListener(e -> {
        	w.apply(user);
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(user);
			w.close();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnApply;
	private Button btnOK;
	private TwinColSelect<Business> select = null;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();

}
