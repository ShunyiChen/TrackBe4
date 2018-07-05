package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Thumbnail extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Thumbnail() {
		setMargin(false);
		setSpacing(false);
		this.setWidth("135px");
		this.setHeight("135px");
		this.addStyleName("drop-area");
		Image image = new Image(null, new ThemeResource("img/empty.png"));
		Label text = new Label("文件(2)");
		this.addComponents(image, text);
		this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		this.setComponentAlignment(text, Alignment.BOTTOM_CENTER);
	}
}
