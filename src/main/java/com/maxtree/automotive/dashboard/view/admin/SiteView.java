package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.maxtree.automotive.vfs.VFSUtils;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class SiteView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public SiteView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("站点名",100), new GridColumn("主机地址",170), new GridColumn("端口",100), new GridColumn("站点类型",100), new GridColumn("运行状态",100),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<Site> lstSite = ui.siteService.findAll();
		for (Site site : lstSite) {
			Object[] rowData = generateOneRow(site);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("站点列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.K1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							int siteUniqueId = (int) objects[0];
							Site site = ui.siteService.findById(siteUniqueId);
							Object[] rowData = generateOneRow(site);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditSiteWindow.open(callback);
				}
				else {
	        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
	        	}
			}
		};
		grid.setAddEvent(addEvent);
		main.addComponents(grid);
		main.setComponentAlignment(grid, Alignment.TOP_CENTER);
		
		this.addComponent(main);
		this.setComponentAlignment(main, Alignment.TOP_CENTER);
		this.setExpandRatio(main, 1);
	}
	
	/**
	 * Generate one row
	 * @param site
	 * @return
	 */
	private Object[] generateOneRow(Site site) {
		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("分配社区", VaadinIcons.USER, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.K5)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								// 更新表格UI
								Site s = ui.siteService.findById(site.getSiteUniqueId());
								Object[] rowData = generateOneRow(s);
								grid.setValueAt(new CustomGridRow(rowData), site.getSiteUniqueId());
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
			
			if (site.getRunningStatus() == RunningStatus.STOP) {
				menu.addItem("启动", VaadinIcons.FILE_START, new Command() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						if (loggedInUser.isPermitted(PermissionCodes.K6)) {
							UI.getCurrent().access(() -> {
								boolean testTrue = new VFSUtils().testConnection(site);
								if (testTrue) {
									site.setRunningStatus(RunningStatus.RUNNING);
									ui.siteService.update(site);
									// 更新表格UI
									Site s = ui.siteService.findById(site.getSiteUniqueId());
									Object[] rowData = generateOneRow(s);
									grid.setValueAt(new CustomGridRow(rowData), site.getSiteUniqueId());
									
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
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						if (loggedInUser.isPermitted(PermissionCodes.K6)) {
							site.setRunningStatus(RunningStatus.STOP);
							ui.siteService.update(site);
							// 更新表格UI
							Site s = ui.siteService.findById(site.getSiteUniqueId());
							Object[] rowData = generateOneRow(s);
							grid.setValueAt(new CustomGridRow(rowData), site.getSiteUniqueId());
						}
						else {
			        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
			        	}
					}
				});
			}
			
			menu.addItem("测试", VaadinIcons.BUG, new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.K6)) {
						UI.getCurrent().access(() -> {
							boolean testTrue = new VFSUtils().testConnection(site);
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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.K2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								// 更新表格UI
								Site s = ui.siteService.findById(site.getSiteUniqueId());
								Object[] rowData = generateOneRow(s);
								grid.setValueAt(new CustomGridRow(rowData), site.getSiteUniqueId());
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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.K3)) {
						
						Callback okevent = new Callback() {

							@Override
							public void onSuccessful() {
								ui.siteService.delete(site.getSiteUniqueId());
								// 更新表格UI
								grid.deleteRow(site.getSiteUniqueId());
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
		
		Image runStatus = null;
		if (site.getRunningStatus() == RunningStatus.STOP) {
			runStatus = new Image(null, new ThemeResource("img/adminmenu/Stop.png"));
		} else {
			runStatus = new Image(null, new ThemeResource("img/adminmenu/Start.png"));
		}
		runStatus.setWidth("12px");
		runStatus.setHeight("12px");
		
		return new Object[] {site.getSiteName(),site.getHostAddr(),site.getPort(), site.getSiteType(),runStatus,img,site.getSiteUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
