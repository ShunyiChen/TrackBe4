package com.maxtree.automotive.dashboard.view.admin;

import java.sql.SQLException;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class DEV extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DEV() {
		this.setSizeFull();
		this.setClosable(true);
		this.setResizable(true);
		this.setCaption("SQL工具");
		
		main.setSizeFull();
		main.setSpacing(false);
		main.setMargin(false);
		
	 	// 必须有一个VerticalLayout，否则上下各50%显示
	 	VerticalLayout content = new VerticalLayout();
	 	content.setSpacing(false);
	 	content.setMargin(false);
	 	content.setWidth("100%");
	 	content.setHeightUndefined();
		// 标题布局
		HorizontalLayout searchHLayout = new HorizontalLayout();
		searchHLayout.setMargin(false);
		searchHLayout.setSpacing(false);
		searchHLayout.setWidthUndefined();
//		searchHLayout.setHeight("88px");
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setMargin(false);
		toolbar.setSpacing(false);
		Button add = new Button(VaadinIcons.FILE_ADD);
		add.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		add.addStyleName(ValoTheme.BUTTON_SMALL);
		
		Button run = new Button(new ThemeResource("img/adminmenu/Start.png"));
		run.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		run.addStyleName(ValoTheme.BUTTON_SMALL);
		run.setEnabled(false);
		
		add.addClickListener(e->{
			run.setEnabled(true);
//			bench.newSQLScript();
		});
		run.addClickListener(e->{
			run.setEnabled(false);
			Callback callback = new Callback() {

				@Override
				public void onSuccessful() {
					run.setEnabled(true);
				}
			};
//			try {
//				bench.run(callback);
//			} catch (SQLException e1) {
//				bench.throwException(e1);
//				run.setEnabled(true);
//			}
		});
		toolbar.addComponents(add,Box.createHorizontalBox(5),run);
		
		Image leftarrowImg = new Image(null, new ThemeResource("img/adminmenu/info-circle-o.png"));
		Label parentTitle = new Label("点击下面添加按钮创建SQL查询");
		parentTitle.setWidthUndefined();
		parentTitle.addStyleName("parent-title");
		
		searchHLayout.addComponents(leftarrowImg, Box.createHorizontalBox(20), parentTitle);
		searchHLayout.setComponentAlignment(leftarrowImg, Alignment.MIDDLE_LEFT);
		searchHLayout.setComponentAlignment(parentTitle, Alignment.MIDDLE_LEFT);
		 
		// 表格
		content.addComponents(searchHLayout, toolbar, bench);
		content.setComponentAlignment(bench, Alignment.TOP_CENTER);
		main.addComponents(content);
		main.setComponentAlignment(content, Alignment.TOP_CENTER);
		
		this.setContent(main);
	}
	
	/**
	 * 
	 */
	public static void open() {
		DEV dev = new DEV();
		UI.getCurrent().addWindow(dev);
		dev.center();
	}
	
	private VerticalLayout main = new VerticalLayout();
	private DEVWorkbench bench = new DEVWorkbench();
}
