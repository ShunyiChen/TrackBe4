package com.maxtree.automotive.dashboard.view.front;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ImageViewerWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ImageViewerWindow(FrontView view, int selectDocumentId) {
		this.view = view;
		this.selectDocumentId = selectDocumentId;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("原文");
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(true);
		this.setWidth("800px");
		this.setHeight("700px");
		
		main.setSizeFull();
		
		List<Document> list1 = ui.documentService.findAllDocument1(view.vin, view.uuid);
		List<Document> list2 = ui.documentService.findAllDocument2(view.vin, view.uuid);
		allDocuments.addAll(list1);
		allDocuments.addAll(list2);
		
		for(Document doc : allDocuments) {
			if (doc.getDocumentUniqueId()==selectDocumentId) {
				
				display(doc);
		 		index++;
				break;
			}
		}
		
		ShortcutListener leftListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_LEFT,
				null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				System.out.println("leftListener");
				if (index > 0) {
					index--;
				} else {
					index = allDocuments.size() - 1;
				}
				Document doc = allDocuments.get(index);
				display(doc);
			}
		};
		ShortcutListener rightListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_RIGHT,
				null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				System.out.println("rightListener");
				if (index < allDocuments.size() - 1) {
					index++;
				} else {
					index = 0;
				}
				Document doc = allDocuments.get(index);
				display(doc);
			}
		};
		this.addShortcutListener(leftListener);
		this.addShortcutListener(rightListener);
		this.setContent(main);
		this.addCloseListener(e->{
			ui.setPollInterval(1000);
		});
	}
	
	/**
	 * 
	 * @param doc
	 */
	private void display(Document doc) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				FileObject fileObj = null;
				try {
					fileObj = fileSystem.resolveFile(view.editableSite, doc.getFileFullPath());
					return fileObj.getContent().getInputStream();
				} catch (FileException e) {
					e.printStackTrace();
				} catch (FileSystemException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, "1.jpg");
 		streamResource.setCacheTime(0);
 		main.removeAllComponents();
 		image = new Image(null, streamResource);
 		main.addComponent(image);
 		main.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
 		String alias = StringUtils.isEmpty(doc.getAlias())?"其它材料":doc.getAlias();
 		this.setCaption("原文-"+alias);
	}
	
	public static void open(FrontView view, int selectDocumentId) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ImageViewerWindow w = new ImageViewerWindow(view, selectDocumentId);
        UI.getCurrent().addWindow(w);
        w.center();
        ui.setPollInterval(-1);
    }
	
	private List<Document> allDocuments = new ArrayList<Document>();
	private VerticalLayout main = new VerticalLayout();
	private Image image = new Image();
	private FrontView view;
	private int selectDocumentId;
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TB4FileSystem fileSystem = new TB4FileSystem();
	private int index = 0;
}
