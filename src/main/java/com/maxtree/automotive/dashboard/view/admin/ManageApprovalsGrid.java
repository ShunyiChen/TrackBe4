package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageApprovalsGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ManageApprovalsGrid() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("90%");
		this.setHeightUndefined();
		// 表头
		HorizontalLayout header = createGridHeader();
		// 表体
		Panel body = createGridBody();
		this.addComponents(header, body);
		this.setComponentAlignment(header, Alignment.TOP_CENTER);
		this.setComponentAlignment(body, Alignment.TOP_CENTER);
	}

	private HorizontalLayout createGridHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidth("100%");
		header.setHeight("50px");
		header.addStyleName("grid-header-line");
		Label columnName = new Label("社区名");
		columnName.addStyleName("grid-title");
		Label colCheckFile = new Label("用户名");
		colCheckFile.addStyleName("grid-title");
		Label colMaterials = new Label("公司名");
		colMaterials.addStyleName("grid-title");
		header.addComponents(columnName, colCheckFile, colMaterials);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(colCheckFile, Alignment.MIDDLE_LEFT);
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
//		List<Audit> lst = new ArrayList<>();//ui.approvalService.findAll();
//		for (Audit app : lst) {
//			HorizontalLayout row1 = createDataRow(app);
//			tableBody.addComponents(row1);
//			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
//		}
//		gridPanel.setContent(tableBody);
		return gridPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout createDataRow() {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
//		// 社区名称
//		Label labelName = new Label(approval.getCommunity().getCommunityName());
//		// 用户名
//		Label labelUserName = new Label(approval.getUserName());
//		// 公司名
//		Label labelCompanyName = new Label(approval.getCompany().getCompanyName());
		
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("接受", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					Callback callback = new Callback() {

						@Override
						public void onSuccessful() {
							refreshTable();
						}
					};
//					EditBusinessTypesWindow.edit(business, callback);
				}
			});
			menu.addItem("拒绝", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
//					DashboardUI ui = (DashboardUI) UI.getCurrent();
//					try {
//						ui.businessService.delete(business.getBusinessUniqueId());
//					} catch (DataException e) {
//						Notifications.warning("无法删除正在引用的业务类型。");
//					}
//					refreshTable();
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
//		labelName.setWidth("190px");
//		labelUserName.setWidth("190px");
//		labelCompanyName.setWidth("154px");
//		
//		row.addComponents(labelName, labelUserName, labelCompanyName, moreImg);
//		row.setComponentAlignment(labelName, Alignment.MIDDLE_LEFT);
//		row.setComponentAlignment(labelUserName, Alignment.MIDDLE_LEFT);
//		row.setComponentAlignment(labelCompanyName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
//		List<Audit> lst = new ArrayList<>();//ui.approvalService.findAll();
//		for (Audit app : lst) {
//			HorizontalLayout row1 = createDataRow(app);
//			tableBody.addComponents(row1);
//			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
//		}
	}
	
	private VerticalLayout tableBody;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
