package com.maxtree.automotive.dashboard.view.quality;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class SplitPanel extends Panel implements ImageViewIF {
	
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
    	root.setAlias("当前业务材料");
    	
        // Couple of childless root items
        treeData.addItem(null, root);
        List<Document> primaryDocs = ui.documentService.findAllDocument1(transaction.getVin(),transaction.getUuid());
    	List<Document> secondaryDocs = ui.documentService.findAllDocument2(transaction.getVin(),transaction.getUuid());
        for (Document d : primaryDocs) {
        	treeData.addItem(root, d);
        }
        for (Document d : secondaryDocs) {
        	treeData.addItem(root, d);
        }
        tree.setDataProvider(new TreeDataProvider<>(treeData));
        // 展开树节点
        tree.expand(root);
        tree.setItemIconGenerator(item -> {
            return VaadinIcons.FILE_PICTURE;
        });
//        tree.addContextClickListener(event -> {
//        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
//        	Document item = e.getItem();
//        	tree.select(item);
//        });
        tree.setSelectionMode(SelectionMode.SINGLE);
        tree.addItemClickListener(e -> {
			// 排除根节点
			if (!e.getItem().getAlias().equals("当前业务材料")) {
				selectedNode = e.getItem();
				stage.display(site, selectedNode);
			}
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
        
        ShortcutListener leftListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_LEFT, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				previous();
			}
		};
		ShortcutListener rightListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_RIGHT, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				next();
			}
		};
		ShortcutListener upListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_UP, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				previous();
			}
		};
		ShortcutListener downListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_DOWN, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				next();
			}
		};
		tree.addShortcutListener(leftListener);
		tree.addShortcutListener(rightListener);
		tree.addShortcutListener(upListener);
		tree.addShortcutListener(downListener);
        
        HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		this.setContent(hsplit);
		// Set the position of the splitter as percentage
		hsplit.setSplitPosition(25, Unit.PERCENTAGE);
		hsplit.setFirstComponent(tree);
		hsplit.setSecondComponent(stage);
		this.setSizeFull();
	}
	
	@Override
	public void previous() {
		List<Document> children = treeData.getChildren(root);
		for(int i=0; i < children.size(); i++) {
			Document doc = children.get(i);
			if(doc == selectedNode) {
				i--;
				if(i < 0) {
					i = children.size() - 1;
				}
				selectedNode = children.get(i);
				tree.select(selectedNode);
				stage.display(site, selectedNode);
				break;
			}
		}
	}
	
	@Override
	public void next() {
		List<Document> children = treeData.getChildren(root);
		for(int i=0; i < children.size(); i++) {
			Document doc = children.get(i);
			if(doc == selectedNode) {
				i++;
				if(i > children.size()-1) {
					i = 0;
				}
				selectedNode = children.get(i);
				tree.select(selectedNode);
				stage.display(site, selectedNode);
				break;
			}
		}
	}
	
	private TreeData<Document> treeData = new TreeData<Document>();
	private Document root = new Document();
	private Tree<Document> tree = new Tree<>();
	private ImageStage stage = new ImageStage(this);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Site site;
	private Document selectedNode;
}
