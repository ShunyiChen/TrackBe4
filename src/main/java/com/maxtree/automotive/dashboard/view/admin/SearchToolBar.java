package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class SearchToolBar extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchToolBar() {
		
		initComponents();
	}
	
	private void initComponents() {
		this.setWidth("100%");
		this.setHeight("56px");
		this.setSpacing(false);
		this.setMargin(false);
		// Image of menu
		Image imgMenu = new Image(null, new ThemeResource("img/adminmenu/menu_24px_1130584_easyicon.net.png"));
		imgMenu.addClickListener(e -> {
//        	AdminMenuWindow.open();
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
            String value = e.getValue();
        });
       
        // menu
        HorizontalLayout menuHLayout = new HorizontalLayout();
        menuHLayout.setSizeUndefined();
        menuHLayout.setMargin(false);
        menuHLayout.setSpacing(false);
        menuHLayout.addComponents(imgMenu, txtSettings);
        // text field
        HorizontalLayout fieldHLayout = new HorizontalLayout();
        fieldHLayout.setMargin(false);
        fieldHLayout.setSpacing(false);
        fieldHLayout.addComponents(menuHLayout, imgSearchInField, keywordField);
        fieldHLayout.addStyleName("search-settings-field");
        fieldHLayout.setExpandRatio(menuHLayout, 0);
        
        this.addComponents(fieldHLayout);
//        this.setComponentAlignment(menuHLayout, Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(fieldHLayout, Alignment.MIDDLE_CENTER);
	}
}
