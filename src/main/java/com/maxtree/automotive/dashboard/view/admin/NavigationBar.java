package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.component.Hr;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class NavigationBar extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public NavigationBar(AdminMainView view) {
		this.view = view;
		initComponents();
	}
	
	private void initComponents() {
		this.setWidth("255px");
		this.setHeight("100%");
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setMargin(false);
		main.setSpacing(false);
		
		Label settings = new Label("设置");
	    settings.addStyleName("NavigationBar_settings");
	    HorizontalLayout settingHLayout = new HorizontalLayout();
	    settingHLayout.setWidth("100%");
	    settingHLayout.setHeight("57px");
	    settingHLayout.addComponent(settings);
	    settingHLayout.setComponentAlignment(settings, Alignment.MIDDLE_LEFT);
	    
	    Hr hr = new Hr();
	    main.addComponents(settingHLayout,hr);
	    main.setComponentAlignment(settingHLayout, Alignment.TOP_LEFT);
	    for(FlexTableRow row : view.table.data) {
	    	HorizontalLayout item = menuItem(row);
	    	main.addComponent(item);
	    	main.setComponentAlignment(item, Alignment.TOP_LEFT);
	    }
	    
	    this.addStyleName("NavigationBar");
	    this.setContent(main);
	}

	/**
	 * 
	 * @param row
	 * @return
	 */
	private HorizontalLayout menuItem(FlexTableRow row) {
		Image icon = new Image(null, new ThemeResource("img/adminmenu/searchmenu/"+row.getImageName()));
	    Label label = new Label(row.getTitle());
	    HorizontalLayout item = new HorizontalLayout();
	    item.addComponents(icon,label);
	    item.setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
	    item.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    item.addLayoutClickListener(e->{
	    	view.hideNavigationBar();
	    	view.table.doFilter(row.getOrderID());
	    });
	    item.addStyleName("NavigationBar_menuItem");
	    return item;
	}
	
	private VerticalLayout main = new VerticalLayout();
	private AdminMainView view;
}
