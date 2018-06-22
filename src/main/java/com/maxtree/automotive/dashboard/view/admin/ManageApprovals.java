package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ManageApprovals extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param mainView
	 */
	public ManageApprovals(AdminMainView mainView) {
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
		searchHLayout.setHeight("88px");
		Image leftarrowImg = new Image(null, new ThemeResource("img/adminmenu/leftarrow.png"));
		leftarrowImg.addStyleName("left-arrow-image");
		leftarrowImg.addClickListener(e -> {
	 		mainView.getContent().removeComponent(this);
	 		mainView.showPanes();
        });
		Label parentTitle = new Label("等待审批");
		parentTitle.setWidth("420px");
		parentTitle.addStyleName("parent-title");
		Image magnifyingglass = new Image(null, new ThemeResource("img/adminmenu/magnifyingglass.png"));
		magnifyingglass.addStyleName("left-arrow-image");
		TextField searchField = new TextField();
		searchField.setPlaceholder("搜索");
		searchField.setWidth("162px");
		searchField.setHeight("23px");
		searchHLayout.addComponents(leftarrowImg, Box.createHorizontalBox(20), parentTitle, magnifyingglass, Box.createHorizontalBox(7), searchField);
		searchHLayout.setComponentAlignment(leftarrowImg, Alignment.TOP_LEFT);
		searchHLayout.setComponentAlignment(parentTitle, Alignment.TOP_LEFT);
		searchHLayout.setComponentAlignment(magnifyingglass, Alignment.TOP_RIGHT);
		searchHLayout.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
		// 业务类型标题
		Label gridTitle = new Label("业务类型");
		gridTitle.addStyleName("grid-title");
		// 表格
		ManageApprovalsGrid grid = new ManageApprovalsGrid();
		content.addComponents(searchHLayout, gridTitle, grid);
		content.setComponentAlignment(grid, Alignment.TOP_CENTER);
		addComponents(content);
		setComponentAlignment(content, Alignment.TOP_CENTER);
	}
}
