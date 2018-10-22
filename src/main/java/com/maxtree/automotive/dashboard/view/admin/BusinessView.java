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
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;
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
 * @author Chen
 *
 */
public class BusinessView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public BusinessView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("业务名称"), new GridColumn("审档级别",100), new GridColumn("业务材料",270), new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<Business> lstBusiness = ui.businessService.findAll();
		for (Business business : lstBusiness) {
			Object[] rowData = generateOneRow(business);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("业务列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.N1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							int businessuniqueid = (int) objects[0];
							Business business = ui.businessService.findById(businessuniqueid);
							Object[] rowData = generateOneRow(business);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditBusinessTypesWindow.open(callback);
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
	 * 
	 * @param business
	 * @return
	 */
	private Object[] generateOneRow(Business business) {
		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("设置业务材料", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.N5)) {
						Callback callback = new Callback() {
							@Override
							public void onSuccessful() {
								Business b = ui.businessService.findById(business.getBusinessUniqueId());
								Object[] rowData = generateOneRow(b);
								grid.setValueAt(new CustomGridRow(rowData),business.getBusinessUniqueId());
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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.N2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								Business b = ui.businessService.findById(business.getBusinessUniqueId());
								Object[] rowData = generateOneRow(b);
								grid.setValueAt(new CustomGridRow(rowData),business.getBusinessUniqueId());
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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.N3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								try {
									ui.businessService.delete(business.getBusinessUniqueId());
								} catch (DataException e) {
									Notifications.warning("无法删除正在引用的业务类型。");
								}
								grid.deleteRow(business.getBusinessUniqueId());
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
		
		// 所需资料
		StringBuilder materialStr = new StringBuilder("");
		for (DataDictionary item : business.getItems()) {
			materialStr.append(item.getItemName());
			materialStr.append(",");
		}
		if (materialStr.length() > 0) {
			materialStr.delete(materialStr.length() - 1, materialStr.length());
		}
		
		return new Object[] {business.getName(),business.getCheckLevel(),materialStr.toString(),img,business.getBusinessUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
