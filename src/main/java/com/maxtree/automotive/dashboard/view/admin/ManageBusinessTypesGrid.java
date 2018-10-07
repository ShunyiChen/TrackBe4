package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
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

public class ManageBusinessTypesGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ManageBusinessTypesGrid() {
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
			if (loginUser.isPermitted(PermissionCodes.N1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditBusinessTypesWindow.open(callback);
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

	/**
	 * 
	 * @return
	 */
	private HorizontalLayout createGridHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidth("100%");
		header.setHeight("50px");
		header.addStyleName("grid-header-line");
		Label columnName = new Label("名称");
		columnName.addStyleName("grid-title");
		Label colLocal= new Label("审档级别");
		colLocal.addStyleName("grid-title");
		Label colMaterials = new Label("业务材料");
		colMaterials.addStyleName("grid-title");
		header.addComponents(columnName,colLocal,colMaterials);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(colLocal, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(colMaterials, Alignment.MIDDLE_LEFT);
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
		DashboardUI ui = (DashboardUI) UI.getCurrent();
		List<Business> lstBusiness = ui.businessService.findAll();
		for (Business business : lstBusiness) {
			HorizontalLayout row1 = createDataRow(business);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
		gridPanel.setContent(tableBody);
		return gridPanel;
	}
	
	/**
	 * 
	 * @param business
	 * @return
	 */
	private HorizontalLayout createDataRow(Business business) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		// 业务名称
		Label labelName = new Label(business.getName());
		// 审档级别
		Label labelLocal = new Label(business.getCheckLevel());
		// 所需资料
		StringBuilder materialStr = new StringBuilder("");
		for (DataDictionary item : business.getItems()) {
			materialStr.append(item.getItemName());
			materialStr.append(",");
		}
		if (materialStr.length() > 0) {
			materialStr.delete(materialStr.length() - 1, materialStr.length());
		}
		Label labelMaterials = new Label(materialStr.toString());
		
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("设置业务材料", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.N5)) {
						Callback callback = new Callback() {
							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningDataitemToBusinessWindow.open(callback, business);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			
			menu.addSeparator();
			menu.addItem("编辑", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.N2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditBusinessTypesWindow.edit(business, callback);
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
					if (loginUser.isPermitted(PermissionCodes.N3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								DashboardUI ui = (DashboardUI) UI.getCurrent();
								try {
									ui.businessService.delete(business.getBusinessUniqueId());
								} catch (DataException e) {
									Notifications.warning("无法删除正在引用的业务类型。");
								}
								refreshTable();
							}
						};
						MessageBox.showMessage("删除提示", "您确定要删除当前业务类型"+business.getName()+"?",MessageBox.WARNING, event, "删除");
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		labelName.setWidth("190px");
		labelLocal.setWidth("170px");
		labelMaterials.setWidth("170px");
		row.addComponents(labelName, labelLocal,labelMaterials, moreImg);
		row.setComponentAlignment(labelName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelLocal, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelMaterials, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		DashboardUI ui = (DashboardUI) UI.getCurrent();
		List<Business> lstBusiness = ui.businessService.findAll();
		for (Business business : lstBusiness) {
			HorizontalLayout row1 = createDataRow(business);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private VerticalLayout tableBody;
}
