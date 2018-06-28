package com.maxtree.automotive.dashboard.view.qc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class ImageChecker extends HorizontalSplitPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ImageChecker(Transaction transaction) {
		this.setSizeFull();
		leftTree.setSelectionMode(SelectionMode.SINGLE);
		leftTree.setHeight("100%");
		leftTree.setWidth("180px");
    	// 获取site
    	site = ui.siteService.findById(transaction.getSiteUniqueId());
    	Document primary = new Document("主要材料");
    	Document secondary = new Document("次要材料");
        TreeData<Document> treeData = new TreeData<Document>();
        // Couple of childless root items
        treeData.addItem(null, primary);
        treeData.addItem(null, secondary);

        List<Document> primaryDocs = ui.documentService.findPrimary(transaction.getUuid(), transaction.getVin());
    	// 次要文件
    	List<Document> secondaryDocs = ui.documentService.findSecondary(transaction.getUuid(), transaction.getVin());
        for (Document d : primaryDocs) {
        	treeData.addItem(primary, d);
        }
        for (Document d : secondaryDocs) {
        	treeData.addItem(secondary, d);
        }
        
        leftTree.setDataProvider(new TreeDataProvider<>(treeData));
        // 展开树节点
        leftTree.expand(primary, secondary);
        
        leftTree.setItemIconGenerator(item -> {
            return VaadinIcons.FILE_PICTURE;
        });
        leftTree.addContextClickListener(event -> {
        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
        	Document item = e.getItem();
        	leftTree.select(item);
        });
        leftTree.setSelectionMode(SelectionMode.SINGLE);
        leftTree.addItemClickListener(e -> {
			selectedDocument = e.getItem();
			if (selectedDocument.getDocumentUniqueId() > 0) {
				
//				embeddedImageViewer.showPicture(selectedDocument.getDocumentUniqueId(), selectedDocument.getCategory());
//				Image image = convertDocument2Image(selectedDocument);
				
				
				int index = photoViewer.getDocuments().indexOf(selectedDocument);
				photoViewer.setIndex(index);
				try {
					photoViewer.actualSize();
				} catch (FileException e1) {
					e1.printStackTrace();
				}
				
			}
        });
        
        leftTree.addAttachListener(e->{
        	System.out.println(e);
        });
        
        com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(leftTree, true);
        menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
//				Set<Document> set = leftTree.getSelectedItems();
//				List<Document> list = new ArrayList<Document>(set);
//				Document selectedDocument = list.get(0);
//				embeddedImageViewer.showPicture(selectedDocument.getDocumentUniqueId(), selectedDocument.getCategory());
			
			}
		});
        
        embeddedImageViewer = new EmbeddedImageViewer(transaction, site);
		this.setFirstComponent(leftTree);
//		this.setSecondComponent(embeddedImageViewer);
		
		photoViewer = new EmbededWindowsPhotoViewer(site);
		this.setSecondComponent(photoViewer);
		
		List<Document> all = new ArrayList<Document>();
		all.addAll(primaryDocs);
		all.addAll(secondaryDocs);
		photoViewer.setDocuments(all);
		
		
		embeddedImageViewer.setCascadingCallback(new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				for (Document d : primaryDocs) {
					if (d.getDocumentUniqueId() == ((Document)objects[0]).getDocumentUniqueId()) {
						leftTree.select(d);
						break;
					}
				}
			}
        });
		
		// Set the position of the splitter as percentage
		this.setSplitPosition(20, Unit.PERCENTAGE);
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
//			if (cascadingCallback != null)
//			cascadingCallback.onSuccessful(document);
			
			return picture;
		} catch (FileException e) {
			Notifications.warning(e.getMessage());
		}
		return new Image();
	}
	
	private Tree<Document> leftTree = new Tree<>();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private EmbeddedImageViewer embeddedImageViewer;
	private Site site;
	private Document selectedDocument;
	private EmbededWindowsPhotoViewer photoViewer;
}
