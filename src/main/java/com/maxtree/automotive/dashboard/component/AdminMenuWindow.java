package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
        this.addCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        this.setPosition(-255, 0);
        UI.getCurrent().addWindow(this);
//        this.setPosition(0, 0);
        addStyleName("adminmenuwindow");
        
        addBlurListener(event -> {
//        	removeStyleName("adminmenuwindow");
//        	addStyleName("fade-out");
        	this.close();
        });
        
        this.addCloseListener(e -> {
//        	UI.getCurrent().addWindow(AdminMenuWindow.this);
        });
//        
//        setVisible(false);
//        this.addAttachListener(e->{
//        	removeStyleName("fade-out");
//        	 addStyleName("adminmenuwindow");
//        	 setVisible(true);
//        });

        
        
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(false);
        content.setSpacing(false);
        content.setHeightUndefined();
        content.addStyleName("admin-menu-window-verticallayout");
        
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
        
        content.addComponents(settings, hr, people, scanentry, appearance, search, defaultBrowser, onstartup);
        setContent(content);
	}
	
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
        Window w = new AdminMenuWindow();
        
//        w.focus();
    }
}
