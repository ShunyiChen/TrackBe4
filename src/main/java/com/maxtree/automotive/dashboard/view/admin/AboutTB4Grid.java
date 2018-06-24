package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AboutTB4Grid extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutTB4Grid() {
		
		initComponents();
	}
	
	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("100%");
		
		HorizontalLayout hLayout1 = new HorizontalLayout();
		hLayout1.setMargin(false);
		hLayout1.setSpacing(false);
		hLayout1.setWidthUndefined();
		hLayout1.setHeight("40px");
		Image logo = new Image(null, new ThemeResource("img/current-channel-logo@1x.png"));
		Label name = new Label(TB4Application.NAME);
		hLayout1.addComponents(logo, Box.createHorizontalBox(5), name);
		hLayout1.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);
		hLayout1.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hLayout2 = new HorizontalLayout();
		hLayout2.setMargin(false);
		hLayout2.setSpacing(false);
		hLayout2.setWidthUndefined();
		hLayout2.setHeight("40px");
		Label verTitle = new Label("版本:");
		Label version = new Label(TB4Application.VERSION);
		hLayout2.addComponents(verTitle, Box.createHorizontalBox(5), version);
		hLayout2.setComponentAlignment(verTitle, Alignment.MIDDLE_LEFT);
		hLayout2.setComponentAlignment(version, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hLayout3 = new HorizontalLayout();
		hLayout3.setMargin(false);
		hLayout3.setSpacing(false);
		hLayout3.setWidthUndefined();
		hLayout3.setHeight("40px");
		Label binTitle = new Label("构建ID:");
		Label buildid = new Label(TB4Application.BUILD_ID);
		hLayout3.addComponents(binTitle, Box.createHorizontalBox(5), buildid);
		hLayout3.setComponentAlignment(binTitle, Alignment.MIDDLE_LEFT);
		hLayout3.setComponentAlignment(buildid, Alignment.MIDDLE_LEFT);
		
		this.addComponents(hLayout1, hLayout2, hLayout3);
	}
}
