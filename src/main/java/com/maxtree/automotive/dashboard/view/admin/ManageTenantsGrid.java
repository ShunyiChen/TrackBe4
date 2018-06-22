package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Tenant;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageTenantsGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageTenantsGrid() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("90%");
		this.setHeightUndefined();
		// 表头
		HorizontalLayout header = createGridHeader();
		// 表体
		Panel body = createGridBody();
		Button addButton = new Button("添加");
		addButton.addStyleName("grid-button-without-border");
		addButton.addClickListener(e-> {
			User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
			if (loginUser.isPermitted(PermissionCodes.J1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditTenantWindow.open(callback);
			} 
			else {
        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
        	}
		});
		
		this.addComponents(header, body, Box.createVerticalBox(15), addButton);
		this.setComponentAlignment(header, Alignment.TOP_CENTER);
		this.setComponentAlignment(body, Alignment.TOP_CENTER);
		this.setComponentAlignment(addButton, Alignment.TOP_RIGHT);
	}

	private HorizontalLayout createGridHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidth("100%");
		header.setHeight("50px");
		header.addStyleName("grid-header-line");
		Label columnName = new Label("租户名");
		columnName.addStyleName("grid-title");
		
		header.addComponents(columnName);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		return header;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createGridBody() {
		Panel gridPanel = new Panel();
		gridPanel.addStyleName("grid-body-without-border");
		gridPanel.setHeight("340px");
		tableBody = new VerticalLayout(); 
		tableBody.setMargin(false);
		tableBody.setSpacing(false);
		
		List<Tenant> data = ui.communityService.findAllTenants();
		for (Tenant t : data) {
			HorizontalLayout row1 = createDataRow(t);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
		gridPanel.setContent(tableBody);
		return gridPanel;
	}
	
	/**
	 * 
	 * @param community
	 * @return
	 */
	private HorizontalLayout createDataRow(Tenant tenant) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label labelName = new Label(tenant.getTenantName());
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("编辑", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.J2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditTenantWindow.edit(tenant, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			
			menu.addItem("从列表删除", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.J3)) {
						ui.communityService.deleteTenant(tenant);
						refreshTable();
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			
			menu.open(e.getClientX(), e.getClientY());
		});
		
		labelName.setWidth("540px");
		row.addComponents(labelName, moreImg);
		row.setComponentAlignment(labelName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		List<Tenant> data = ui.communityService.findAllTenants();
		for (Tenant t : data) {
			HorizontalLayout row1 = createDataRow(t);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private VerticalLayout tableBody;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}


