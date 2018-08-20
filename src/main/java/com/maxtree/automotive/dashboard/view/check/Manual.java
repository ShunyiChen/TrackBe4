package com.maxtree.automotive.dashboard.view.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;

import com.maxtree.automotive.dashboard.BusinessCode;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Manual extends Panel implements ImageViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param transaction
	 */
	public Manual(Transaction transaction) {
		this.transaction = transaction;
		initComponents();
	}
	
	private void initComponents() {
		// 右侧组件
		HorizontalSplitPanel rightSplit = new HorizontalSplitPanel();
		rightSplit.setSizeFull();
		rightRoot.setAlias("历史记录");
		rightTree.setSelectionMode(SelectionMode.SINGLE);
		rightTree.setSizeFull();
		rightTreeData.addItem(null, rightRoot);
		List<Transaction> list = ui.transactionService.findForList(transaction.getVin());
		for(Transaction trans : list) {
			Document businessDoc = new Document();
			businessDoc.setAlias(""+BusinessCode.get(trans.getBusinessCode()));
			rightTreeData.addItem(rightRoot, businessDoc);
			List<Document> primaryDocs = ui.documentService.findAllDocument1(trans.getVin(),trans.getUuid());
	    	List<Document> secondaryDocs = ui.documentService.findAllDocument2(trans.getVin(),trans.getUuid());
	        for (Document d : primaryDocs) {
	        	rightTreeData.addItem(businessDoc, d);
	        }
	        for (Document d : secondaryDocs) {
	        	rightTreeData.addItem(businessDoc, d);
	        }
		}
		rightTree.setDataProvider(new TreeDataProvider<>(rightTreeData));
		rightTree.expand(rightRoot);
        rightTree.setItemIconGenerator(item -> {
        	if(item.getDictionarycode() == null) {
        		return VaadinIcons.FOLDER;
        	} else {
        		return VaadinIcons.FILE_PICTURE;
        	}
        });
        rightTree.setItemDescriptionGenerator(item -> {
        	return item.getAlias();
        });
        rightSplit.setFirstComponent(imgStage);
        rightSplit.setSecondComponent(rightTree);
        // Set the position of the splitter as percentage
        rightSplit.setSplitPosition(100, Unit.PERCENTAGE);
		
		//左侧组件
		leftTree.setSelectionMode(SelectionMode.SINGLE);
		leftTree.setSizeFull();
    	site = ui.siteService.findByCode(transaction.getSiteCode());
    	leftRoot.setAlias("当前业务材料");
        leftTreeData.addItem(null, leftRoot);
        List<Document> primaryDocs = ui.documentService.findAllDocument1(transaction.getVin(),transaction.getUuid());
    	List<Document> secondaryDocs = ui.documentService.findAllDocument2(transaction.getVin(),transaction.getUuid());
        for (Document d : primaryDocs) {
        	leftTreeData.addItem(leftRoot, d);
        }
        for (Document d : secondaryDocs) {
        	leftTreeData.addItem(leftRoot, d);
        }
        leftTree.setDataProvider(new TreeDataProvider<>(leftTreeData));
        // 展开树节点
        leftTree.expand(leftRoot);
        leftTree.setItemIconGenerator(item -> {
            return VaadinIcons.FILE_PICTURE;
        });
//        leftTree.addContextClickListener(event -> {
//        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
//        	Document item = e.getItem();
//        	tree.select(item);
//        });
        leftTree.setSelectionMode(SelectionMode.SINGLE);
        leftTree.addItemClickListener(e -> {
			// 排除根节点
        	//TODO
			if (e.getItem().getAlias().startsWith("机动车")) {
				selectedNode = e.getItem();
				imgStage.display(site, selectedNode);
				// Set the position of the splitter as percentage
		        rightSplit.setSplitPosition(75, Unit.PERCENTAGE);
			} else {
				imgStage.clean();
				// Set the position of the splitter as percentage
		        rightSplit.setSplitPosition(100, Unit.PERCENTAGE);
			}
        });
        com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(leftTree, true);
        menu.addItem("加入对比", new com.vaadin.contextmenu.Menu.Command() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				Set<Document> set = leftTree.getSelectedItems();
				List<Document> list = new ArrayList<Document>(set);
				Document selectedDocument = list.get(0);
				addForComparing(site, selectedDocument);
			}
        });
		HorizontalSplitPanel main = new HorizontalSplitPanel();
		main.setSizeFull();
		// Set the position of the splitter as percentage
		main.setSplitPosition(25, Unit.PERCENTAGE);
		main.setFirstComponent(leftTree);
		main.setSecondComponent(rightSplit);
		this.setSizeFull();
		this.setContent(main);
	}
	
	/**
	 * 
	 * @param site
	 * @param doc
	 */
	private void addForComparing(Site site, Document doc) {
		if (doc.getFileFullPath() != null) {
			try {
				FileObject fobj = new TB4FileSystem().resolveFile(site, doc.getFileFullPath());
				ImageWindow imageWindow = new ImageWindow(doc.getAlias()+"-历史", fobj, 1.0f);
//				toolbar.setEditingWindow(imageWindow);
//				imageWindow.addFocusListener(e -> {
//					toolbar.setEditingWindow(imageWindow);
//		        });
//				imageWindow.center();
				
				
			} catch (FileException e) {
				e.printStackTrace();
				Notifications.warning("读取文件异常。"+e.getMessage());
			}
		}
	}
	
	@Override
	public void previous() {
		List<Document> children = leftTreeData.getChildren(leftRoot);
		for(int i=0; i < children.size(); i++) {
			Document doc = children.get(i);
			if(doc == selectedNode) {
				i--;
				if(i < 0) {
					i = children.size() - 1;
				}
				selectedNode = children.get(i);
				leftTree.select(selectedNode);
				imgStage.display(site, selectedNode);
				break;
			}
		}
	}

	@Override
	public void next() {
		List<Document> children = leftTreeData.getChildren(leftRoot);
		for(int i=0; i < children.size(); i++) {
			Document doc = children.get(i);
			if(doc == selectedNode) {
				i++;
				if(i > children.size()-1) {
					i = 0;
				}
				selectedNode = children.get(i);
				leftTree.select(selectedNode);
				imgStage.display(site, selectedNode);
				break;
			}
		}
	}
	
	private Document leftRoot = new Document();
	private Tree<Document> leftTree = new Tree<>();
	private TreeData<Document> leftTreeData = new TreeData<Document>();
	private Document rightRoot = new Document();
	private Tree<Document> rightTree = new Tree<>();
	private TreeData<Document> rightTreeData = new TreeData<Document>();
	private ImageStage imgStage = new ImageStage(this);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Transaction transaction;
	private Site site;
	private Document selectedNode;
}
