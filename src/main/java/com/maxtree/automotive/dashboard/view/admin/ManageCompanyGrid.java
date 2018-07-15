package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import org.apache.log4j.Logger;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.Storehouse;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.view.admin.storehouse.OpenStorehouseWindow;
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

public class ManageCompanyGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageCompanyGrid() {
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
			if (loginUser.isPermitted(PermissionCodes.F1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditCompanyWindow.open(callback);
			} else {
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
		Label columnName = new Label("机构名");
		columnName.addStyleName("grid-title");
		Label columnAddr = new Label("地址");
		columnAddr.addStyleName("grid-title");
		Label columnIgnore = new Label("跳过质检");
		columnIgnore.addStyleName("grid-title");
		Label columnCount = new Label("员工数");
		columnCount.addStyleName("grid-title");
		header.addComponents(columnName, columnAddr, columnIgnore, columnCount);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnAddr, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnIgnore, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnCount, Alignment.MIDDLE_LEFT);
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
		List<Company> data = ui.companyService.findAll();
		for (Company c : data) {
			HorizontalLayout row1 = createDataRow(c);
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
	private HorizontalLayout createDataRow(Company company) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label labelName = new Label(company.getCompanyName());
		Label labelAddr = new Label(company.getAddress());
		Label labelIgnore = new Label(company.getIgnoreChecker() == 1?"是":"否");
		Label labelCount = new Label(company.getEmployees().size()+"");
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("分配用户", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.F4)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningUsersToCompanyWindow.open(company, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			menu.addItem("设置业务类型", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.F7)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningBusinessesToCompanyWindow.open(company, callback);
					} else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			menu.addSeparator();
			// 如果是车管所
			if (company.getHasStoreHouse() == 1) {
				menu.addItem("库房", new Command() {
					@Override
					public void menuSelected(MenuItem selectedItem) {
						User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
						if (loginUser.isPermitted(PermissionCodes.F5)) {
							int storehouseUniqueId = company.getStorehouseUniqueId();
							Storehouse storehouse = new Storehouse();
							if (storehouseUniqueId == 0) {
								storehouse.setCode("001");
								storehouseUniqueId = ui.storehouseService.create(storehouse);
								storehouse.setStorehouseUniqueId(storehouseUniqueId);
								
								company.setStorehouseUniqueId(storehouseUniqueId);
								ui.companyService.updateStorehouse(company);
								
							} else {
								
								storehouse = ui.storehouseService.findById(storehouseUniqueId);
							}
							
							Callback callback = new Callback() {

								@Override
								public void onSuccessful() {
								}
							};
							OpenStorehouseWindow.open(storehouse, callback);
						}
						else {
			        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
			        	}
					}
				});
				menu.addSeparator();
			}
			
			menu.addItem("编辑", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.F2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditCompanyWindow.edit(company, callback);
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
					if (loginUser.isPermitted(PermissionCodes.F3)) {
						
						Callback event = new Callback() {

							@Override
							public void onSuccessful() {
								
								try {
									ui.companyService.deleteCompany(company);
								} catch (DataException e) {
									log.info("删除机构"+company.toString()+"出错，"+e.getMessage());
								}
								refreshTable();
							}
						};
						
						MessageBox.showMessage("提示", "请确定是否删除该机构。", MessageBox.WARNING, event, "删除");
						
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		labelName.setWidth("140px");
		labelAddr.setWidth("145px");
		labelIgnore.setWidth("160px");
		labelCount.setWidth("90px");
		row.addComponents(labelName, labelAddr, labelIgnore,labelCount, moreImg);
		row.setComponentAlignment(labelName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelAddr, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelIgnore, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelCount, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		DashboardUI ui = (DashboardUI) UI.getCurrent();
		List<Company> data = ui.companyService.findAll();
		for (Company c : data) {
			HorizontalLayout row1 = createDataRow(c);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private VerticalLayout tableBody;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	// define the logger
    private static Logger log = Logger.getLogger(ManageCompanyGrid.class);
    
}
