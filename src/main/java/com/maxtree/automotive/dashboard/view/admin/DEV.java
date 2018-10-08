package com.maxtree.automotive.dashboard.view.admin;

import java.sql.SQLException;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DEV extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DEV(AdminMainView mainView) {
		setMargin(false);
	    setSpacing(false);
	    setWidth("678px");
	    setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
	 	addStyleName("detail-pane-with-padding20");
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
		
		add.addClickListener(e->{
			bench.newSQLScript();
		});
		run.addClickListener(e->{
			run.setEnabled(false);
			Callback callback = new Callback() {

				@Override
				public void onSuccessful() {
					run.setEnabled(true);
				}
			};
			try {
				bench.run(callback);
			} catch (SQLException e1) {
				bench.throwException(e1);
				run.setEnabled(true);
			}
		});
		toolbar.addComponents(add,Box.createHorizontalBox(5),run);
		
		Image leftarrowImg = new Image(null, new ThemeResource("img/adminmenu/leftarrow.png"));
		leftarrowImg.addStyleName("left-arrow-image");
		leftarrowImg.addClickListener(e -> {
	 		mainView.getContent().removeComponent(this);
	 		mainView.showPanes();
        });
		Label parentTitle = new Label("开发");
		parentTitle.setWidth("420px");
		parentTitle.addStyleName("parent-title");
		
		searchHLayout.addComponents(leftarrowImg, Box.createHorizontalBox(20), parentTitle);
		searchHLayout.setComponentAlignment(leftarrowImg, Alignment.TOP_LEFT);
		searchHLayout.setComponentAlignment(parentTitle, Alignment.TOP_LEFT);
		 
		// 表格
		content.addComponents(searchHLayout, toolbar, bench);
		content.setComponentAlignment(bench, Alignment.TOP_CENTER);
		addComponents(content);
		setComponentAlignment(content, Alignment.TOP_CENTER);
	}
	
	private DEVWorkbench bench = new DEVWorkbench();
}
