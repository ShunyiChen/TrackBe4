package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
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

public class ManageCommunityGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManageCommunityGrid() {
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
			if (loginUser.isPermitted(PermissionCodes.G1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditCommunityWindow.open(callback);
				
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
		Label columnName = new Label("社区名");
		columnName.addStyleName("grid-title");
		Label columnDesc = new Label("描述");
		columnDesc.addStyleName("grid-title");
		Label columnGroup = new Label("组");
		columnGroup.addStyleName("grid-title");
		Label columnLevel = new Label("查询级别");
		columnLevel.addStyleName("grid-title");
		Label columnCompany = new Label("机构数");
		columnCompany.addStyleName("grid-title");
		
		header.addComponents(columnName, columnDesc, columnGroup, columnLevel, columnCompany);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnDesc, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnGroup, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnLevel, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnCompany, Alignment.MIDDLE_LEFT);
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
		List<Community> data = ui.communityService.findAll();
		for (Community c : data) {
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
	private HorizontalLayout createDataRow(Community community) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label labelName = new Label(community.getCommunityName());
		Label labelDescription = new Label(community.getCommunityDescription());
		Label labelGroup = new Label(community.getGroupId()+"");
		Label labelLevel = new Label(community.getLevel()+"");
		Label labelCount = new Label(community.getCompanies().size()+"");
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			menu.addItem("分配机构", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.G4)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningCompaniesToCommunityWindow.open(community, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			menu.addSeparator();
			
			menu.addItem("分配租户", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.G6)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningTenantsToCommunityWindow.open(community, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			
			menu.addItem("分配站点", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.G7)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningSitesToCommunityWindow.open(community, callback);
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
					if (loginUser.isPermitted(PermissionCodes.G2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditCommunityWindow.edit(community, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			
			if (community.getLevel() > 1) {
				menu.addItem("从列表删除", new Command() {
					@Override
					public void menuSelected(MenuItem selectedItem) {
						
						User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
						if (loginUser.isPermitted(PermissionCodes.G3)) {
							
							Callback event = new Callback() {

								@Override
								public void onSuccessful() {
									try {
										ui.communityService.delete(community.getCommunityUniqueId());
									} catch (DataException e) {
										e.printStackTrace();
									}
									refreshTable();
								}
							};
							MessageBox.showMessage("提示", "请确定是否删除该社区。", MessageBox.WARNING, event, "删除");
							
						} else {
							Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
						}
					}
				});
			}
			
			
			menu.open(e.getClientX(), e.getClientY());
		});
		
		labelName.setWidth("110px");
		labelDescription.setWidth("120px");
		labelGroup.setWidth("120px");
		labelLevel.setWidth("120px");
		labelCount.setWidth("70px");
		row.addComponents(labelName, labelDescription, labelGroup, labelLevel, labelCount, moreImg);
		row.setComponentAlignment(labelName, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelDescription, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelGroup, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelLevel, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(labelCount, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		List<Community> data = ui.communityService.findAll();
		for (Community c : data) {
			HorizontalLayout row1 = createDataRow(c);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private VerticalLayout tableBody;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}


