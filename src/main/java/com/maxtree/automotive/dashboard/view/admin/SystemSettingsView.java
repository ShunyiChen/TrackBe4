package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.SystemSettings;
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
public class SystemSettingsView extends ContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public SystemSettingsView(String parentTitle, AdminMainView rootView) {
		super(parentTitle, rootView);
		this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		
		GridColumn[] columns = {new GridColumn("参数名",190), new GridColumn("值",190),new GridColumn("注释",190),new GridColumn("", 20)}; 
		List<CustomGridRow> data = new ArrayList<>();
		List<SystemSettings> lst = ui.settingsService.findAll();
		for (SystemSettings ss : lst) {
			Object[] rowData = generateOneRow(ss);
			data.add(new CustomGridRow(rowData));
		}
		grid = new CustomGrid("系统参数列表",columns, data,false);
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
	private Object[] generateOneRow(SystemSettings ss) {
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
					User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
					if (loginUser.isPermitted(PermissionCodes.N2)) {
						Callback callback = new Callback() {

							@Override
							public void onSuccessful() {
								SystemSettings settings = ui.settingsService.findById(ss.getSettingUniqueId());
								Object[] rowData = generateOneRow(settings);
								grid.setValueAt(new CustomGridRow(rowData),ss.getSettingUniqueId());
							}
						};
						EditSystemSettingsWindow.edit(ss, callback);
					}
					else {
		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
		        	}
					
				}
			});
			menu.open(e.getClientX(), e.getClientY());
		});
		
		return new Object[] {ss.getName(),ss.getValue(),ss.getComments(),img,ss.getSettingUniqueId()};
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout();
	private User loggedInUser;
	private CustomGrid grid;
}
