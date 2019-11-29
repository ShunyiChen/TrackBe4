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
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author chens
 */
public class CommunityView {//extends ContentView {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param parentTitle
//	 * @param rootView
//	 */
//	public CommunityView(String parentTitle, AdminMainView rootView) {
//		super(parentTitle, rootView);
//		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		loggedInUser = ui.userService.getUserByUserName(username);
//		main.setWidth("100%");
//		main.setHeightUndefined();
//		main.setSpacing(false);
//		main.setMargin(false);
//
//		GridColumn[] columns = {new GridColumn("社区名",240), new GridColumn("描述",240), new GridColumn("机构数",80),new GridColumn("", 20)};
//		List<CustomGridRow> data = new ArrayList<>();
//		List<Community> list = ui.communityService.findAll(loggedInUser);
//		for (Community c : list) {
//			Object[] rowData = generateOneRow(c);
//			data.add(new CustomGridRow(rowData));
//		}
//		grid = new CustomGrid("社区列表",columns, data);
//
//		Callback addEvent = new Callback() {
//
//			@Override
//			public void onSuccessful() {
//				if (loggedInUser.isPermitted(PermissionCodes.G1)) {
//					Callback2 callback = new Callback2() {
//
//						@Override
//						public void onSuccessful(Object... objects) {
//
//							int communityUniqueId = (int) objects[0];
//							Community community = ui.communityService.findById(communityUniqueId);
//							Object[] rowData = generateOneRow(community);
//							grid.insertRow(new CustomGridRow(rowData));
//						}
//					};
//					EditCommunityWindow.open(callback);
//
//				} else {
//	        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//	        	}
//			}
//		};
//		grid.setAddEvent(addEvent);
//		main.addComponents(grid);
//		main.setComponentAlignment(grid, Alignment.TOP_CENTER);
//
//		this.addComponent(main);
//		this.setComponentAlignment(main, Alignment.TOP_CENTER);
//		this.setExpandRatio(main, 1);
//	}
	
//	/**
//	 *
//	 * @param community
//	 * @return
//	 */
//	private Object[] generateOneRow(Community community) {
//		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
//		img.addStyleName("PeopleView_menuImage");
//		img.addClickListener(e->{
//			ContextMenu menu = new ContextMenu(img, true);
//			menu.addItem("分配机构", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.G4)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Community c = ui.communityService.findById(community.getCommunityUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),community.getCommunityUniqueId());
//							}
//						};
//						AssigningCompaniesToCommunityWindow.open(community, callback);
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//				}
//			});
//			menu.addSeparator();
//
//			menu.addItem("分配站点", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.G6)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Community c = ui.communityService.findById(community.getCommunityUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),community.getCommunityUniqueId());
//							}
//						};
//						AssigningSitesToCommunityWindow.open(community, callback);
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//
//				}
//			});
//			menu.addSeparator();
//
//			menu.addItem("编辑", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//
//					if (loggedInUser.isPermitted(PermissionCodes.G2)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Community c = ui.communityService.findById(community.getCommunityUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),community.getCommunityUniqueId());
//							}
//						};
//						EditCommunityWindow.edit(community, callback);
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//				}
//			});
//
//			menu.addItem("从列表删除", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//
//					if (loggedInUser.isPermitted(PermissionCodes.G3)) {
//
//						Callback event = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								try {
//									ui.communityService.delete(community.getCommunityUniqueId());
//								} catch (DataException e) {
//									e.printStackTrace();
//								}
//								grid.deleteRow(community.getCommunityUniqueId());
//							}
//						};
//						MessageBox.showMessage("提示", "请确定是否删除该社区。", MessageBox.WARNING, event, "删除");
//
//					} else {
//						Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//					}
//				}
//			});
//			menu.open(e.getClientX(), e.getClientY());
//		});
//		return new Object[] {community.getCommunityName(),community.getCommunityDescription(), community.getCompanies().size(),img,community.getCommunityUniqueId()};
//	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private CustomGrid grid;
}
