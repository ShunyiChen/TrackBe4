package com.maxtree.automotive.dashboard.view.quality;

import java.io.InputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ImageStage extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageStage() {
		
		initComponents();
	}
	
	private void initComponents() {
		HorizontalLayout header = new HorizontalLayout();
		header.setHeight("20px");
		header.setWidth("100%");
		header.addStyleName("imagestage-header");
		HorizontalLayout footer = new HorizontalLayout();
		footer.setHeight("20px");
		footer.setWidth("100%");
		footer.addStyleName("imagestage-header");
		this.setSizeFull();
		this.setSpacing(false);
		this.setMargin(false);
		this.addComponents(header,scroll,footer);
		this.setExpandRatio(scroll, 1);
		scroll.setSizeFull();
		scroll.setContent(picture);
	}
	
	/**
	 * 
	 * @param site
	 * @param doc
	 */
	public void display(Site site, Document doc) {
		document2Image(site, doc);
		scroll.setContent(picture);
	}
	
	/**
	 * 
	 * @param site
	 * @param document
	 */
	private void document2Image(Site site, Document document) {
		try {
			
			System.out.println(document.getFileFullPath());
			FileObject fileObj = new TB4FileSystem().resolveFile(site, document.getFileFullPath());
			
			com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
				@Override
				public InputStream getStream() {
					try {
						return fileObj.getContent().getInputStream();
					} catch (FileSystemException e) {
						Notifications.warning("读取文件失败。"+e.getMessage());
					}
					return null;
				}
			};
			StreamResource streamResource = new StreamResource(streamSource, fileObj.getName().getBaseName());
			streamResource.setCacheTime(0);
			picture = new Image(null, streamResource);
		} catch (FileException e) {
			Notifications.warning(e.getMessage());
		}
	}
	
	private Panel scroll = new Panel();
	private Image picture = new Image();
}
