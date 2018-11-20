package com.maxtree.automotive.dashboard.view.front;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;

import com.maxtree.automotive.dashboard.domain.Document;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Thumbnail extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Thumbnail(InputStream is) {
		setMargin(false);
		setSpacing(true);
		this.setSizeUndefined();
		this.addStyleName("Thumbnail");
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
				return is;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, "1.jpg");
 		streamResource.setCacheTime(0);
		
		Image image = new Image(null, streamResource);
		this.addComponents(image);
		this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	}
	
	public Thumbnail(String txt,InputStream is) {
		this(is);
		Label name = new Label();
		name.addStyleName("Thumbnail-name");
		name.setValue(txt);
		this.addComponent(name);
		this.setComponentAlignment(name, Alignment.BOTTOM_CENTER);
	}
	
}
