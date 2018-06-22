package com.maxtree.automotive.dashboard.view.qc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.imageprocessor.services.ImageProcessorAPI;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class EmbededWindowsPhotoViewer extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param site
	 */
	public EmbededWindowsPhotoViewer(Site site) {
		this.site = site;
		initComponents();
	}
	
	private void initComponents() {
		width = ui.getPage().getBrowserWindowWidth() - 372;
		height = ui.getPage().getBrowserWindowHeight() - 140;
		
		this.setWidth("100%");
		this.setHeight(height+"px");
	}
	
	private void openWith(Image image) {

		com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(image, true);
        menu.addItem("实际尺寸", VaadinIcons.RHOMBUS, new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				try {
					actualSize();
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		});
        
        menu.addSeparator();
        menu.addItem("放大", VaadinIcons.SEARCH_MINUS, new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				try {
					zoomIn();
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		});
        menu.addItem("缩小", VaadinIcons.SEARCH_PLUS,new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				try {
					zoomOut();
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		});
        menu.addSeparator();
        menu.addItem("上一张", VaadinIcons.LEVEL_UP, new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				try {
					previous();
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		});
        menu.addItem("下一张", VaadinIcons.LEVEL_DOWN,new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				try {
					next();
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		});
		this.setContent(image);
	}
	
	public void actualSize() throws FileException {
		Document document = documents.get(index);
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
		Image picture = new Image(null, streamResource);
		openWith(picture);
	}
	
	private void next() throws FileException {
		index++;
		if (index >= documents.size()) {
			index = 0;
		}
		actualSize();
	}
	
	private void previous() throws FileException {
		index--;
		if (index < 0) {
			index = documents.size() - 1;
		}
		actualSize();
	}
	
	// 放大
	private void zoomIn() throws FileException {
		Document document = documents.get(index);
		FileObject fileObj = new TB4FileSystem().resolveFile(site, document.getFileFullPath());
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
			@Override
			public InputStream getStream() {
				try {
					xscale += 0.1;
					yscale += 0.1;
					return api.scale(fileObj.getContent().getInputStream(), xscale, yscale);
				} catch (FileSystemException e) {
					Notifications.warning("读取文件失败。"+e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					Notifications.warning("读取文件失败。"+e.getMessage());
				}
				return null;
			}
		};
		StreamResource streamResource = new StreamResource(streamSource, fileObj.getName().getBaseName());
		streamResource.setCacheTime(0);
		Image newImage = new Image(null, streamResource);
		openWith(newImage);
	}
	
	// 缩小
	private void zoomOut() throws FileException {
		Document document = documents.get(index);
		FileObject fileObj = new TB4FileSystem().resolveFile(site, document.getFileFullPath());
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
			@Override
			public InputStream getStream() {
				try {
					xscale -= 0.1;
					yscale -= 0.1;
					return api.scale(fileObj.getContent().getInputStream(), xscale, yscale);
				} catch (FileSystemException e) {
					Notifications.warning("读取文件失败。"+e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					Notifications.warning("读取文件失败。"+e.getMessage());
				}
				return null;
			}
		};
		StreamResource streamResource = new StreamResource(streamSource, fileObj.getName().getBaseName());
		streamResource.setCacheTime(0);
		Image newImage = new Image(null, streamResource);
		openWith(newImage);
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	
	public List<Document> getDocuments() {
		return documents;
	}

	private int width = 0;
	private int height = 0;
	private Site site;
	private int index = 0;
	private List<Document> documents;
	private double xscale = 1.0d;
	private double yscale = 1.0d;
	private ImageProcessorAPI api = new ImageProcessorAPI();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
