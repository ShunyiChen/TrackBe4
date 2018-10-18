package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdminMenuWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminMenuWindow() {
		setModal(true);
//        this.addCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setPosition(0, 0);
//        addStyleName("adminmenuwindow");
        
        main.setSizeFull();
        main.setMargin(false);
        main.setSpacing(false);
        main.setHeightUndefined();
//        main.addStyleName("admin-menu-window-verticallayout");
        
        main.addStyleName("sidebar-in");
        
        addBlurListener(event -> {
        	if (main.getStyleName().contains("sidebar-in"))
            {
        		main.removeStyleName("sidebar-in");
        		main.setStyleName("sidebar-out");
        		main.setWidth(0,Unit.PIXELS);
                return;
            }

        	main.removeStyleName("sidebar-out");
        	main.setStyleName("sidebar-in");
        	main.setWidth(300,Unit.PIXELS);
//        	main.removeStyleName("admin-menu-window-verticallayout");
        	
//        	close();
        });
        
        Label settings = new Label("设置");
        settings.addStyleName("admin-menu-window-labelh1");
        Hr hr = new Hr();
        // 用户
        Image peopleImage = new Image(null, new ThemeResource("img/adminmenu/people.png"));
        HorizontalLayout people = createMenuItem(peopleImage, "用户");
        // 扫描录入
        Image scanentryImage = new Image(null, new ThemeResource("img/adminmenu/scanentry.png"));
        HorizontalLayout scanentry = createMenuItem(scanentryImage, "扫描录入");
        // 外观
        Image appearImage = new Image(null, new ThemeResource("img/adminmenu/appearance.png"));
        HorizontalLayout appearance = createMenuItem(appearImage, "外观");
        // 搜索引擎
        Image searchImage = new Image(null, new ThemeResource("img/adminmenu/search_engine.png"));
        HorizontalLayout search = createMenuItem(searchImage, "搜索引擎");
        // 默认浏览器
        Image defaultBrowserImage = new Image(null, new ThemeResource("img/adminmenu/defaultbrowser.png"));
        HorizontalLayout defaultBrowser = createMenuItem(defaultBrowserImage, "默认浏览器");
        // 在启动时
        Image onStartupImage = new Image(null, new ThemeResource("img/adminmenu/onstartup.png"));
        HorizontalLayout onstartup = createMenuItem(onStartupImage, "在启动时");
        
        main.addComponents(settings, hr, people, scanentry, appearance, search, defaultBrowser, onstartup);
        setContent(main);
	}
	
//	public void infade() {
//		UI.getCurrent().access(new Runnable() {
//            @Override
//            public void run() {
////            	 this.setPosition(0, 0);
//            	for(int i= -255;i < 0; i++) {
//            		setPosition(i, 0);
//            	}
//            }
//        });
//	}
//	
//	private void outfade() {
//		UI.getCurrent().access(new Runnable() {
//            @Override
//            public void run() {
////            	 this.setPosition(0, 0);
//            	for(int i= 0;i > -255;i--) {
//            		setPosition(i, 0);
//            	}
//            	close();
//            }
//        });
//	}
	
	private HorizontalLayout createMenuItem(Image image, String text) {
		 HorizontalLayout itemLayout = new HorizontalLayout();
		 itemLayout.setWidthUndefined();
		 itemLayout.setSpacing(true);
		 itemLayout.addLayoutClickListener( e -> {
			 this.close();
		 });
		 Label textLabel = new Label(text);
		 itemLayout.addStyleName("createMenuItem");
		 itemLayout.addComponents(image, new Label("&nbsp;", ContentMode.HTML), textLabel);
		 itemLayout.setComponentAlignment(image, Alignment.MIDDLE_LEFT);
		 itemLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_LEFT);
		 return itemLayout;
	}
	
	public static void open() {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AdminMenuWindow w = new AdminMenuWindow();
        UI.getCurrent().addWindow(w);
    }
	
	private VerticalLayout main = new VerticalLayout();
}
