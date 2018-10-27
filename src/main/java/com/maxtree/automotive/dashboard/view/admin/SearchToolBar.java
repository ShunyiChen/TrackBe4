package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author chens
 *
 */
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
		Image menu = new Image(null, new ThemeResource("img/adminmenu/menu_24px_1130584_easyicon.net.png"));
		menu.addClickListener(e -> {
			view.showNavigationBar();
        });
		menu.addStyleName("SearchToolBar_menu");
        
        // Text of settings
        Label settings = new Label("设置");
        settings.addStyleName("SearchToolBar_settings");
        
        keywordField.setPlaceholder("搜索设置");
        keywordField.addStyleName("SearchToolBar_keywordField");
        keywordField.addStyleName("v-textfield:focus");
        keywordField.addStyleName("v-textfield");
        keywordField.addValueChangeListener(e ->{
            String keyword = e.getValue();
            view.table.doFilter(keyword);
            close.setVisible(!StringUtils.isEmpty(keyword));
        });
        close.addStyleName("SearchToolBar_close");
        close.addClickListener(e->{
        	close.setVisible(false);
        	keywordField.setValue("");
        });
        close.setVisible(false);
        
        // Search image
        Image search = new Image();
        search.setIcon(VaadinIcons.SEARCH);
        //Menu
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setSizeUndefined();
        menuLayout.addComponents(menu, settings);
        //Notification
        HorizontalLayout notificationLayout = new HorizontalLayout();
        notificationLayout.setSizeUndefined();
        notificationLayout.addComponents(notificationButton);
        notificationButton.addClickListener(e->{
        	NotificationsManagementWindow.open("", null);
        });
        
        // text field
        HorizontalLayout outerField = new HorizontalLayout();
        outerField.setMargin(false);
        outerField.setSpacing(false);
        outerField.addComponents(search, keywordField, close);
        outerField.addStyleName("SearchToolBar_outerField");
        
        this.addComponent(menuLayout,"left: 25px; top: 15px;");
        this.addComponent(outerField,"left: 50%;");
        this.addComponent(notificationLayout,"right:10px; top: 10px;");
	}
	
	private NotificationsButton notificationButton = new NotificationsButton();
	private TextField keywordField = new TextField();
	private Image close = new Image(null, new ThemeResource("img/adminmenu/close-circle.png"));
	private AdminMainView view;
}
