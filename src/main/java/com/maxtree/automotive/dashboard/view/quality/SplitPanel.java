package com.maxtree.automotive.dashboard.view.quality;

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
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author chens
 *
 */
public class SplitPanel extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public SplitPanel(Transaction transaction) {
		tree.setSelectionMode(SelectionMode.SINGLE);
		tree.setSizeFull();
    	// 获取site
    	site = ui.siteService.findByCode(transaction.getSiteCode());
    	Document primary = new Document();
    	primary.setAlias("当前业务材料");
        TreeData<Document> treeData = new TreeData<Document>();
        // Couple of childless root items
        treeData.addItem(null, primary);
        List<Document> primaryDocs = ui.documentService.findAllDocument1(transaction.getVin(),transaction.getUuid());
    	List<Document> secondaryDocs = ui.documentService.findAllDocument2(transaction.getVin(),transaction.getUuid());
        for (Document d : primaryDocs) {
        	treeData.addItem(primary, d);
        }
        for (Document d : secondaryDocs) {
        	treeData.addItem(primary, d);
        }
        tree.setDataProvider(new TreeDataProvider<>(treeData));
        // 展开树节点
        tree.expand(primary);
        tree.setItemIconGenerator(item -> {
            return VaadinIcons.FILE_PICTURE;
        });
        tree.addContextClickListener(event -> {
        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
        	Document item = e.getItem();
        	tree.select(item);
        });
        tree.setSelectionMode(SelectionMode.SINGLE);
        tree.addItemClickListener(e -> {
			selectedDocument = e.getItem();
			// 排除根节点
			if (!selectedDocument.getAlias().equals("当前业务材料")) {
//				embeddedImageViewer.showPicture(selectedDocument.getDocumentUniqueId(), selectedDocument.getCategory());
//				Image image = convertDocument2Image(selectedDocument);
//				int index = photoViewer.getDocuments().indexOf(selectedDocument);
//				photoViewer.setIndex(index);
//				try {
//					photoViewer.actualSize();
//				} catch (FileException e1) {
//					e1.printStackTrace();
//				}
				
				stage.display(site, selectedDocument);
				
			}
        });
        
        tree.addAttachListener(e->{
        	System.out.println(e);
        });
        
//        com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(tree, true);
//        menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
//			@Override
//			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
////				Set<Document> set = tree.getSelectedItems();
////				List<Document> list = new ArrayList<Document>(set);
////				Document selectedDocument = list.get(0);
////				embeddedImageViewer.showPicture(selectedDocument.getDocumentUniqueId(), selectedDocument.getCategory());
//			
//			}
//		});
//        vlayout.addComponent(tree);
//        vlayout.setExpandRatio(tree, 1);
        
        HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		this.setContent(hsplit);
		// Set the position of the splitter as percentage
		hsplit.setSplitPosition(25, Unit.PERCENTAGE);
		hsplit.setFirstComponent(tree);
		hsplit.setSecondComponent(stage);
		this.setSizeFull();
        
        
        
 
 
//		
//		
//		embeddedImageViewer.setCascadingCallback(new Callback2() {
//
//			@Override
//			public void onSuccessful(Object... objects) {
//				for (Document d : primaryDocs) {
//					if (d.getDocumentUniqueId() == ((Document)objects[0]).getDocumentUniqueId()) {
//						tree.select(d);
//						break;
//					}
//				}
//			}
//        });
		
	}
	
	private Tree<Document> tree = new Tree<>();
	private ImageStage stage = new ImageStage();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Site site;
	private Document selectedDocument;
}
