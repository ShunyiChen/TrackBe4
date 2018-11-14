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
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Location;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;
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
 * @author Chen
 *
 */
public class LocationView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public LocationView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("分类",190),new GridColumn("名称",190), new GridColumn("编号",190),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<Location> list = ui.locationService.findAll();
		for (Location l : list) {
			Object[] rowData = generateOneRow(l);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("地址列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.M1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							int locationUniqueId = (int) objects[0];
							Location l = ui.locationService.findById(locationUniqueId);
							Object[] rowData = generateOneRow(l);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditDataDictionaryWindow.open(callback, DataDictionaryType.LOCATION);
				} else {
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
	 * @param dd
	 * @return
	 */
	private Object[] generateOneRow(Location l) {
		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("编辑", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (loggedInUser.isPermitted(PermissionCodes.M2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								Location location = ui.locationService.findById(l.getLocationUniqueId());
								Object[] rowData = generateOneRow(location);
								grid.setValueAt(new CustomGridRow(rowData), l.getLocationUniqueId());
							}
						};
						EditLocationWindow.edit(l, callback);
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
					if (loggedInUser.isPermitted(PermissionCodes.M3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								ui.locationService.delete(l.getLocationUniqueId());
								grid.deleteRow(l.getLocationUniqueId());
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
		
		return new Object[] {l.getCategory(),l.getName(),l.getCode(),img,l.getLocationUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
