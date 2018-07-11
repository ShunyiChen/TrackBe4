package com.maxtree.automotive.dashboard.view.front;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Thumbnail extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Thumbnail(int userUniqueId, int documentUniqueId) {
		setMargin(false);
		setSpacing(true);
		this.setSizeUndefined();
		this.addStyleName("Thumbnail");
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream("devices/"+userUniqueId+"/thumbnails/"+documentUniqueId+".jpg");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return inputStream;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, documentUniqueId+".jpg");
 		streamResource.setCacheTime(0);
		
		
		Image image = new Image(null, streamResource);
		this.addComponents(image);
		this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	}
}
