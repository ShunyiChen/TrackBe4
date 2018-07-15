package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;
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

public class ManageDataDictionaryGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageDataDictionaryGrid() {
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
			if (loginUser.isPermitted(PermissionCodes.M1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditDataDictionaryWindow.open(callback);
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
		Label columnCategory = new Label("分类名");
		columnCategory.addStyleName("grid-title");
		Label columnProject = new Label("项目名");
		columnProject.addStyleName("grid-title");
		Label columnCode = new Label("代码");
		columnCode.addStyleName("grid-title");
		Label columnOrder = new Label("顺序");
		columnOrder.addStyleName("grid-title");
		header.addComponents(columnCategory,columnProject,columnCode,columnOrder);
		header.setComponentAlignment(columnCategory, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnProject, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnCode, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnOrder, Alignment.MIDDLE_LEFT);
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
		
		List<DataDictionary> data = ui.dataItemService.findAll();
		for (DataDictionary d : data) {
			HorizontalLayout row1 = createDataRow(d);
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
	private HorizontalLayout createDataRow(DataDictionary dataItem) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label labelCategoryName;
		if (dataItem.getItemType()==1) {
			labelCategoryName = new Label("号牌种类");
		} 
		else if(dataItem.getItemType()==2) {
			labelCategoryName = new Label("地区代号");
		}
		else {
			labelCategoryName = new Label("业务材料");
		}
		// 名称
		Label labelName = new Label(dataItem.getItemName());
		// 代码
		Label labelCode = new Label(dataItem.getCode());
		// 顺序
		Label labelOrder = new Label(dataItem.getOrderNumber()+"");
		
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("编辑", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.M2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditDataDictionaryWindow.edit(dataItem, callback);
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
					if (loginUser.isPermitted(PermissionCodes.M3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								try {
									ui.dataItemService.delete(dataItem);
								} catch (DataException e) {
									Notifications.warning("无法删除该项目。");
								}
								refreshTable();
							}
						};
						
						MessageBox.showMessage("提示", "请确认是否彻底删除当前数据？", MessageBox.WARNING, event, "删除");
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			
			menu.open(e.getClientX(), e.getClientY());
		});
		
		labelCategoryName.setWidth("137px");
		labelName.setWidth("145px");
		labelCode.setWidth("145px");
		labelOrder.setWidth("95px");
		
		row.addComponents(labelCategoryName,labelName,labelCode,labelOrder,moreImg);
		row.setComponentAlignment(labelCategoryName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelCode, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelOrder, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		List<DataDictionary> data = ui.dataItemService.findAll();
		for (DataDictionary d : data) {
			HorizontalLayout row1 = createDataRow(d);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private VerticalLayout tableBody;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}


