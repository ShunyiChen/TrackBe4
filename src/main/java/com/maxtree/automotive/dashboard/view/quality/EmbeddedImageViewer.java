package com.maxtree.automotive.dashboard.view.quality;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class EmbeddedImageViewer extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param transaction
	 * @param site
	 * @param cascadingTree
	 */
	public EmbeddedImageViewer(Transaction transaction, Site site) {
		this.setCaption(null);
		this.site = site;
//		// 主要材料
//		List<Document> primaryDocs = ui.documentService.findPrimary(transaction.getUuid(), transaction.getVin());
//    	// 次要文件
//    	List<Document> secondaryDoes = ui.documentService.findSecondary(transaction.getUuid(), transaction.getVin());
//		
//		this.allDocuments.addAll(primaryDocs);
//		this.allDocuments.addAll(secondaryDoes);
		initComponents();
	}
	
	private void initComponents() {
		this.setWidth("100%");
		this.setHeightUndefined();
		frame.setSizeFull();
		btnLeft.setIcon(VaadinIcons.ANGLE_LEFT);
		btnRight.setIcon(VaadinIcons.ANGLE_RIGHT);
		btnPane = new HorizontalLayout();
		btnPane.setWidth("100px");
		btnPane.setHeight("27px");
		btnPane.addComponents(btnLeft, btnRight);
		btnPane.setComponentAlignment(btnLeft, Alignment.MIDDLE_LEFT);
		btnPane.setComponentAlignment(btnRight, Alignment.MIDDLE_LEFT);
		btnLeft.addClickListener(e -> {
			index++;
			if (index >= allDocuments.size()) {
				index = 0;
			}
			Image img = convertDocument2Image(allDocuments.get(index));
			displayImage(img);
		});
		btnRight.addClickListener(e -> {
			index--;
			if (index < 0) {
				index = allDocuments.size() - 1;
			}
			Image img = convertDocument2Image(allDocuments.get(index));
			displayImage(img);
		});
		
		vlayout.setSpacing(false);
		vlayout.setMargin(false);
		vlayout.addComponent(blank);
		vlayout.setComponentAlignment(blank, Alignment.MIDDLE_CENTER);
		
		this.setContent(vlayout);
	}
	
	/**
	 * 
	 * @param documentUniqueId
	 * @param location // 1:主要材料  2:次要材料
	 */
	public void showPicture(int documentUniqueId, int location) {
		vlayout.removeAllComponents();
		vlayout.addComponents(frame, Box.createVerticalBox(5), btnPane, Box.createVerticalBox(5));
		vlayout.setComponentAlignment(frame, Alignment.TOP_CENTER);
		vlayout.setComponentAlignment(btnPane, Alignment.BOTTOM_CENTER);
		for (int i = 0; i < allDocuments.size(); i++) {
			Document doc = allDocuments.get(i);
			if (doc.getDocumentUniqueId() == documentUniqueId 
					&& doc.location == location) {
				displayImage(convertDocument2Image(doc));
				index = i;
			}
		}
	}
	
	private Image convertDocument2Image(Document document) {
		try {
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
//			labelAlias.setCaption("查看-"+document.getAlias());
//			cascadingTree.select(document);
			if (cascadingCallback != null)
			cascadingCallback.onSuccessful(document);
			
			return picture;
		} catch (FileException e) {
			Notifications.warning(e.getMessage());
		}
		return new Image();
	}
	
	private void displayImage(Image image) {
		frame.setContent(image);
	}
	
	public void setCascadingCallback(Callback2 cascadingCallback) {
		this.cascadingCallback = cascadingCallback;
	}

	private Callback2 cascadingCallback;
	private HorizontalLayout btnPane = new HorizontalLayout();
	private Label blank = new Label("没有可显示的图片");
	private Panel frame = new Panel();
	private VerticalLayout vlayout = new VerticalLayout();
	private Button btnLeft = new Button();
	private Button btnRight = new Button();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private List<Document> allDocuments = new ArrayList<Document>();
	private Site site;
	private int index = 0;
//	private Tree<Document> cascadingTree;
}
