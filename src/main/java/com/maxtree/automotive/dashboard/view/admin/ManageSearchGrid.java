package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageSearchGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageSearchGrid() {
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
			Callback callback = new Callback() {

				@Override
				public void onSuccessful() {
					refreshTable();
				}
			};
//			EditMaterialWindow.open(callback);
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
		Label columnName = new Label("用户名");
		columnName.addStyleName("grid-title");
		Label columnLicensePlate = new Label("车牌号");
		columnLicensePlate.addStyleName("grid-title");
		header.addComponents(columnName,columnLicensePlate);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnLicensePlate, Alignment.MIDDLE_LEFT);
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
//		List<Material> lstMaterial = ui.materialService.findAll();
//		for (Material material : lstMaterial) {
//			HorizontalLayout row1 = createDataRow(material);
//			tableBody.addComponents(row1);
//			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
//		}
		gridPanel.setContent(tableBody);
		return gridPanel;
	}
	
	/**
	 * 
	 * @param business
	 * @return
	 */
	private HorizontalLayout createDataRow(Transaction trans) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidth("100%");
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label labelName = new Label(trans.getPlateNumber());
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("编辑", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					Callback callback = new Callback() {

						@Override
						public void onSuccessful() {
							refreshTable();
						}
					};
//					EditMaterialWindow.edit(material, callback);
				}
			});
			menu.addItem("从列表删除", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
//					DashboardUI ui = (DashboardUI) UI.getCurrent();
//					try {
//						ui.materialService.delete(material.getMaterialUniqueId());
//					} catch (DataException e) {
//						Notifications.warning("无法删除正在引用的资料。");
//					}
					refreshTable();
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		labelName.setWidth("203px");
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
		DashboardUI ui = (DashboardUI) UI.getCurrent();
//		List<Material> lstMaterial = ui.materialService.findAll();
//		for (Material material : lstMaterial) {
//			HorizontalLayout row1 = createDataRow(material);
//			tableBody.addComponents(row1);
//			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
//		}
	}
	
	private VerticalLayout tableBody;
}
