package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 父类
 * @author Chen
 *
 */
public class ContentView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parentTitle
	 * @param rootView
	 */
	public ContentView(String parentTitle,AdminMainView rootView) {
		this.parentTitle = parentTitle;
		this.rootView = rootView;
		this.setMargin(false);
		this.setSpacing(false);
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("ContentView");
		this.setWidth("680px");
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidthUndefined();
		toolbar.setHeight("40px");
		toolbar.addStyleName("ContentView_toolbar");
		Image backImage = new Image(null, new ThemeResource("img/adminmenu/leftarrow.png"));
		backImage.addStyleName("ContentView_backImage");
		toolbar.addLayoutClickListener(e->{
			rootView.back();
		});
		
		Label parentTitleLabel = new Label(parentTitle);
		parentTitleLabel.addStyleName("ContentView_parentTitleLabel");
		toolbar.addComponents(backImage, parentTitleLabel);
		toolbar.setComponentAlignment(backImage, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(parentTitleLabel, Alignment.MIDDLE_LEFT);
		
		this.addComponent(toolbar);
		this.setComponentAlignment(toolbar, Alignment.TOP_LEFT);
		this.setExpandRatio(toolbar, 0);
	}
	
	private String parentTitle;
	private AdminMainView rootView;
}
