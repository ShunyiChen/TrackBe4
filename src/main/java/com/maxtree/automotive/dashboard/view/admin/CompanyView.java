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
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
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
 *
 */
public class CompanyView {//extends ContentView {

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
//	public CompanyView(String parentTitle, AdminMainView rootView) {
//		super(parentTitle, rootView);
//		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		loggedInUser = ui.userService.getUserByUserName(username);
//		main.setWidth("100%");
//		main.setHeightUndefined();
//		main.setSpacing(false);
//		main.setMargin(false);
//
//		GridColumn[] columns = {new GridColumn("机构名",121), new GridColumn("社区",121), new GridColumn("库房名称",81), new GridColumn("质检支持",81),new GridColumn("类别",81),new GridColumn("员工数",82),new GridColumn("", 20)};
//		List<CustomGridRow> data = new ArrayList<>();
//		List<Company> list = ui.companyService.findAll(loggedInUser);
//		for (Company c : list) {
//			Object[] rowData = generateOneRow(c);
//			data.add(new CustomGridRow(rowData));
//		}
//		grid = new CustomGrid("机构列表",columns, data);
//
//		Callback addEvent = new Callback() {
//
//			@Override
//			public void onSuccessful() {
//				if (loggedInUser.isPermitted(PermissionCodes.F1)) {
//					Callback2 callback = new Callback2() {
//
//						@Override
//						public void onSuccessful(Object... objects) {
//							int companyUniqueId = (int) objects[0];
//							Company company = ui.companyService.findById(companyUniqueId);
//							Object[] rowData = generateOneRow(company);
//							grid.insertRow(new CustomGridRow(rowData));
//						}
//					};
//					EditCompanyWindow.open(callback);
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
//
//	/**
//	 *
//	 * @param company
//	 * @return
//	 */
//	private Object[] generateOneRow(Company company) {
//		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
//		img.addStyleName("PeopleView_menuImage");
//		img.addClickListener(e->{
//			ContextMenu menu = new ContextMenu(img, true);
//			menu.addItem("分配用户", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.F4)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Company c = ui.companyService.findById(company.getCompanyUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),company.getCompanyUniqueId());
//							}
//						};
//						AssigningUsersToCompanyWindow.open(company, callback);
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//
//				}
//			});
//			// 判断该机构是否可以有库房1：可以 2：不可以
//			menu.addItem("分配库房", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.F5)) {
//						Callback callback = new Callback() {
//							@Override
//							public void onSuccessful() {
//								Company c = ui.companyService.findById(company.getCompanyUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),company.getCompanyUniqueId());
//							}
//						};
//						AssigningStoreToCompanyWindow.open(company, callback);
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//				}
//			});
//			menu.addSeparator();
//
//			menu.addItem("设置业务类型", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.F7)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Company c = ui.companyService.findById(company.getCompanyUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),company.getCompanyUniqueId());
//							}
//						};
//						AssigningBusinessesToCompanyWindow.open(company, callback);
//					} else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//				}
//			});
//			menu.addSeparator();
//			menu.addItem("编辑", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.F2)) {
//						Callback callback = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//								Company c = ui.companyService.findById(company.getCompanyUniqueId());
//								Object[] rowData = generateOneRow(c);
//								grid.setValueAt(new CustomGridRow(rowData),company.getCompanyUniqueId());
//							}
//						};
//						EditCompanyWindow.edit(company, callback);
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//
//				}
//			});
//			menu.addItem("从列表删除", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.F3)) {
//
//						Callback event = new Callback() {
//
//							@Override
//							public void onSuccessful() {
//
//								try {
//									ui.companyService.deleteCompany(company);
//								} catch (DataException e) {
//									e.printStackTrace();
//								}
//								grid.deleteRow(company.getCompanyUniqueId());
//							}
//						};
//
//						MessageBox.showMessage("提示", "请确定是否删除该机构。", MessageBox.WARNING, event, "删除");
//
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
//				}
//			});
//
//			menu.open(e.getClientX(), e.getClientY());
//		});
//
//		return new Object[] {company.getCompanyName(),company.getCommunityName(),company.getStorehouseName(), company.getQcsupport()?"支持":"不支持", company.getCategory(), company.getEmployees().size(),img,company.getCompanyUniqueId()};
//	}
//
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private VerticalLayout main = new VerticalLayout();
//	private User loggedInUser;
//	private CustomGrid grid;
}
