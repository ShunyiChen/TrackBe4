package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class SearchToolBar extends AbsoluteLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public SearchToolBar(AdminMainView view) {
		this.view = view;
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("SearchToolBar");
		this.setWidth("100%");
		this.setHeight("56px");
		// Image of menu
		Image imgMenu = new Image(null, new ThemeResource("img/adminmenu/menu_24px_1130584_easyicon.net.png"));
		imgMenu.addClickListener(e -> {
			view.showNavigationBar();
        });
		imgMenu.addStyleName("imgMenu");
        
        // Text of settings
        Label txtSettings = new Label("设置");
        txtSettings.addStyleName("txtSettings");
        
        // Search image
        Image imgSearchInField = new Image();
        imgSearchInField.setIcon(VaadinIcons.SEARCH);
        
        TextField keywordField = new TextField();
        keywordField.setPlaceholder("搜索设置");
        keywordField.addStyleName("search-text-field");
        keywordField.addStyleName("v-textfield:focus");
        keywordField.addStyleName("v-textfield");
        keywordField.addValueChangeListener(e ->{
            String keyword = e.getValue();
            view.table.doFilter(keyword);
        });
       
        // menu
        HorizontalLayout menuHLayout = new HorizontalLayout();
        menuHLayout.setSizeUndefined();
//        menuHLayout.setMargin(false);
//        menuHLayout.setSpacing(false);
        menuHLayout.addComponents(imgMenu, txtSettings);
        // text field
        HorizontalLayout fieldHLayout = new HorizontalLayout();
        fieldHLayout.setMargin(false);
        fieldHLayout.setSpacing(false);
        fieldHLayout.addComponents(imgSearchInField, keywordField);
        fieldHLayout.addStyleName("search-settings-field");
        
        this.addComponent(menuHLayout,"left: 25px; top: 15px;");
        this.addComponent(fieldHLayout,"left: 50%;");
	}
	
	private AdminMainView view;
}
