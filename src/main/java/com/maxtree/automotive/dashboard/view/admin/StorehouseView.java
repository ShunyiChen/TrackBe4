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
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.domain.User;
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
public class StorehouseView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public StorehouseView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("库房名",430),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<FrameNumber> results = ui.frameService.findAllStorehouse();
		for (FrameNumber store : results) {
			Object[] rowData = generateOneRow(store);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("库房列表",columns, data);
		
		Callback addEvent = new Callback() {

			@Override
			public void onSuccessful() {
				if (loggedInUser.isPermitted(PermissionCodes.P1)) {
					Callback2 callback = new Callback2() {

						@Override
						public void onSuccessful(Object... objects) {
							int storehouseUniqueId = (int) objects[0];
							FrameNumber storehosue = ui.frameService.findById(storehouseUniqueId);//(storehouseName, frameCode).f.findById(storehouseUniqueId);
							Object[] rowData = generateOneRow(storehosue);
							grid.insertRow(new CustomGridRow(rowData));
						}
					};
					EditStorehouseWindow.open(callback);
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
	 * @param company
	 * @return
	 */
	private Object[] generateOneRow(FrameNumber storehouse) {
		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
		img.addStyleName("PeopleView_menuImage");
		img.addClickListener(e->{
			ContextMenu menu = new ContextMenu(img, true);
			menu.addItem("分配车管所", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.P5)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								FrameNumber f = ui.frameService.findById(storehouse.getFrameUniqueId());
								Object[] rowData = generateOneRow(f);
								grid.setValueAt(new CustomGridRow(rowData), storehouse.getFrameUniqueId());
							}
						};
						AssigningCompanyToStoreWindow.open(storehouse, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			menu.addSeparator();
			menu.addItem("管理密集架", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.P6)) {
						ManageFrameWindow.open(storehouse);
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
					if (loginUser.isPermitted(PermissionCodes.P2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								FrameNumber f = ui.frameService.findById(storehouse.getFrameUniqueId());
								Object[] rowData = generateOneRow(f);
								grid.setValueAt(new CustomGridRow(rowData), storehouse.getFrameUniqueId());
							}
						};
						EditStorehouseWindow.edit(storehouse, callback);
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
					if (loginUser.isPermitted(PermissionCodes.P3)) {
						
						Callback event = new Callback() {
							@Override
							public void onSuccessful() {
								ui.frameService.deleteStorehouse(storehouse);
								grid.deleteRow(storehouse.getFrameUniqueId());
							}
						};
						MessageBox.showMessage("提示", "注意：删除库房将会同步删除其下所有密集架、单元格和文件夹。<br>请确定是否彻底删除该库房?", MessageBox.WARNING, event, "删除");
					}
				}
			});
			menu.addSeparator();
			menu.addItem("属性", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					ShelfPropertiesWindow.open(storehouse);
				}
			});
			
			menu.open(e.getClientX(), e.getClientY());
		});
		return new Object[] {storehouse.getStorehouseName(),img, storehouse.getFrameUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
