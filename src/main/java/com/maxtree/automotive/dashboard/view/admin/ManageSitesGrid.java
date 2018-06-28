package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManageSitesGrid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ManageSitesGrid() {
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
			if (loginUser.isPermitted(PermissionCodes.K1)) {
				Callback callback = new Callback() {

					@Override
					public void onSuccessful() {
						refreshTable();
					}
				};
				EditSiteWindow.open(callback);
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
		Label columnAddr = new Label("主机地址");
		columnAddr.addStyleName("grid-title");
		Label columnPort = new Label("端口");
		columnPort.addStyleName("grid-title");
		Label runStatus = new Label("运行状态");
		runStatus.addStyleName("grid-title");
		header.addComponents(columnName, columnAddr, columnPort, runStatus);
		header.setComponentAlignment(columnName, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnAddr, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(columnPort, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(runStatus, Alignment.MIDDLE_LEFT);
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
		List<Site> lstSite = ui.siteService.findAll();
		for (Site site : lstSite) {
			HorizontalLayout row1 = createDataRow(site);
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
	private HorizontalLayout createDataRow(Site site) {
		HorizontalLayout row = new HorizontalLayout();
		row.setSpacing(false);
		row.setMargin(false);
		row.setWidthUndefined();
		row.setHeightUndefined();
		row.addStyleName("grid-header-line");
		Label name = new Label(site.getSiteName()+"("+site.getSiteType()+")");
		Label hostAddr = new Label(site.getHostAddr());
		Label port = new Label(site.getPort()+"");
		Image runStatus = null;
		if (site.getRunningStatus() == STOP) {
			runStatus = new Image(null, new ThemeResource("img/adminmenu/Stop.png"));
		} else {
			runStatus = new Image(null, new ThemeResource("img/adminmenu/Start.png"));
		}
		runStatus.setWidth("12px");
		runStatus.setHeight("12px");
		
		Image moreImg = new Image(null, new ThemeResource("img/adminmenu/more.png"));
		moreImg.addStyleName("mycursor");
		moreImg.addClickListener(e -> {
			// Create a context menu for 'someComponent'
			ContextMenu menu = new ContextMenu(moreImg, true);
			
			menu.addItem("分配社区", VaadinIcons.USER, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.K5)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						AssigningCommunitiesToSiteWindow.open(callback, site);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			menu.addSeparator();
			
			if (site.getRunningStatus() == STOP) {
				menu.addItem("启动", VaadinIcons.FILE_START, new Command() {
					@Override
					public void menuSelected(MenuItem selectedItem) {
						User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
						if (loginUser.isPermitted(PermissionCodes.K6)) {
							UI.getCurrent().access(() -> {
								boolean testTrue = new TB4FileSystem().testConnection(site);
								if (testTrue) {
									site.setRunningStatus(RUNNING);
									ui.siteService.update(site);
									refreshTable();
								} else {
									Notification.show("提示：","启动失败，无法创建连接。", Type.WARNING_MESSAGE);
								}
							});
						}
						else {
			        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
			        	}
						
					}
				});
			} else {
				menu.addItem("停止", VaadinIcons.STOP, new Command() {
					@Override
					public void menuSelected(MenuItem selectedItem) {
						User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
						if (loginUser.isPermitted(PermissionCodes.K6)) {
							site.setRunningStatus(STOP);
							ui.siteService.update(site);
							refreshTable();
						}
						else {
			        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
			        	}
					}
				});
			}
			
			menu.addItem("测试", VaadinIcons.BUG, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.K6)) {
						UI.getCurrent().access(() -> {
							boolean testTrue = new TB4FileSystem().testConnection(site);
							if (testTrue) {
								Notification notification = new Notification("测试：", "连接成功", Type.HUMANIZED_MESSAGE);
								notification.setDelayMsec(2000);
								notification.show(Page.getCurrent());
							} else {
								Notification.show("测试：","连接失败", Type.WARNING_MESSAGE);
							}
						});
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			
			menu.addSeparator();
			
			menu.addItem("编辑", VaadinIcons.EDIT, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.K2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								refreshTable();
							}
						};
						EditSiteWindow.edit(site, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
				}
			});
			menu.addItem("从列表删除", VaadinIcons.FILE_REMOVE, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.K3)) {
						
						Callback okevent = new Callback() {

							@Override
							public void onSuccessful() {
								ui.siteService.delete(site.getSiteUniqueId());
								refreshTable();
							}
						};
						MessageBox.showMessage("删除提示", "请确认是否彻底删除该站点。", MessageBox.WARNING, okevent, "删除");
					
					} else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		name.setWidth("143.05px");
		hostAddr.setWidth("143.05px");
		port.setWidth("143.05px");
//		runStatus.setWidth("100.05px");
		row.addComponents(name, hostAddr, port, runStatus, Box.createHorizontalBox(90), moreImg, Box.createHorizontalBox(13));
		
		row.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(hostAddr, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(port, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(runStatus, Alignment.MIDDLE_LEFT);
		row.setComponentAlignment(moreImg, Alignment.MIDDLE_RIGHT);
		return row;
	}
	
	/**
	 * 
	 */
	private void refreshTable() {
		tableBody.removeAllComponents();
		List<Site> lstSite = ui.siteService.findAll();
		for (Site site : lstSite) {
			HorizontalLayout row1 = createDataRow(site);
			tableBody.addComponents(row1);
			tableBody.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		}
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout tableBody;
	private static final int STOP = 0;
	private static final int RUNNING = 1;
}
