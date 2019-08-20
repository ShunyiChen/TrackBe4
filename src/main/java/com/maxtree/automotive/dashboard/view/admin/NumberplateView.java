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
import com.maxtree.automotive.dashboard.domain.User;
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
 * @author Chen
 *
 */
public class NumberplateView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public NumberplateView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("号牌种类名称",285), new GridColumn("编码",285),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<DataDictionary> list = ui.dataItemService.findAllByType(DataDictionaryType.NUMPLATE_TYPE);
		for (DataDictionary d : list) {
			Object[] rowData = generateOneRow(d);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("号牌种类列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.M1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							int dduniqueid = (int) objects[0];
							DataDictionary dd = ui.dataItemService.findById(dduniqueid);
							Object[] rowData = generateOneRow(dd);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditDataDictionaryWindow.open(callback, DataDictionaryType.MATERIAL);
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
	private Object[] generateOneRow(DataDictionary dd) {
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
								DataDictionary dict = ui.dataItemService.findById(dd.getDictionaryUniqueId());
								Object[] rowData = generateOneRow(dict);
								grid.setValueAt(new CustomGridRow(rowData), dd.getDictionaryUniqueId());
							}
						};
						EditDataDictionaryWindow.edit(dd, callback);
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
								try {
									ui.dataItemService.delete(dd);
								} catch (DataException e) {
									Notifications.warning("无法删除该项目。");
								}
								grid.deleteRow(dd.getDictionaryUniqueId());
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
		
		return new Object[] {dd.getItemName(),dd.getCode(),img,dd.getDictionaryUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
