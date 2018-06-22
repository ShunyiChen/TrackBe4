package com.maxtree.automotive.dashboard.view.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class ImageComparator extends HorizontalSplitPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param transaction
	 */
	public ImageComparator(Transaction transaction) {
		this.transaction = transaction;
		initComponents();
	}
	
	private void initComponents() {
		this.setSizeFull();
		this.setSplitPosition(150, Unit.PIXELS);
		leftTree.setSelectionMode(SelectionMode.SINGLE);
		leftTree.setWidth("180px");
    	leftTree.setHeight("100%");
    	// 获取site
    	Site site = ui.siteService.findById(transaction.getSiteUniqueId());
    	Document primary = new Document("主要材料");
    	Document secondary = new Document("次要材料");
        TreeData<Document> treeData = new TreeData<Document>();
        // Couple of childless root items
        treeData.addItem(null, primary);
        treeData.addItem(null, secondary);

        // 主要文件
    	List<Document> primaryDocs = ui.documentService.findPrimary(transaction.getUuid(), transaction.getBusinessUniqueId());
    	// 次要文件
    	List<Document> secondaryDocs = ui.documentService.findSecondary(transaction.getUuid(), transaction.getBusinessUniqueId());
        
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
        
        com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(leftTree, true);
        menu.addItem("加入比对", new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				Set<Document> set = leftTree.getSelectedItems();
				List<Document> list = new ArrayList<Document>(set);
				Document selectedDocument = list.get(0);
				addForComparing(site, selectedDocument);
			}
		});
        
        
        ///////////////////////////// 历史右树
        
        rightTree.setSelectionMode(SelectionMode.SINGLE);
        rightTree.setWidthUndefined();
        rightTree.setHeight("100%");
        // 历史记录
        List<Transaction> transList = ui.transactionService.findForList(transaction.getVin());
        TreeData<Document> rightTreeData = new TreeData<Document>();
        for (Transaction historyTrans : transList) {
        	// 避免与历史业务重复
        	if (historyTrans.getTransactionUniqueId() == transaction.getTransactionUniqueId()) {
        		continue;
        	}
        	
        	Business business = ui.businessService.findById(historyTrans.getBusinessUniqueId());
        	Document rootDoc = new Document(business.getName());
            // Couple of childless root items
        	rightTreeData.addItem(null, rootDoc);
        	Document primaryNode = new Document("主要材料");
        	Document secondaryNode = new Document("次要材料");
        	
        	rightTreeData.addItem(rootDoc, primaryNode);
        	rightTreeData.addItem(rootDoc, secondaryNode);
        	 // 展开树节点
            rightTree.expand(primaryNode, secondaryNode);
        	
            
            // 主要文件
        	List<Document> primaryDocuments = ui.documentService.findPrimary(historyTrans.getUuid(), historyTrans.getBusinessUniqueId());
        	// 次要文件
        	List<Document> secondaryDocuments = ui.documentService.findSecondary(historyTrans.getUuid(), historyTrans.getBusinessUniqueId());
            for (Document d : primaryDocuments) {
            	rightTreeData.addItem(primaryNode, d);
            }
            for (Document d : secondaryDocuments) {
            	rightTreeData.addItem(secondaryNode, d);
            }
        }
        
        rightTree.setDataProvider(new TreeDataProvider<>(rightTreeData));
        rightTree.setItemIconGenerator(item -> {
            return VaadinIcons.FILE_PICTURE;
        });
        rightTree.addContextClickListener(event -> {
        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
        	Document item = e.getItem();
        	rightTree.select(item);
        });
        rightTree.setSelectionMode(SelectionMode.SINGLE);
        
        com.vaadin.contextmenu.ContextMenu menu2 = new com.vaadin.contextmenu.ContextMenu(rightTree, true);
        menu2.addItem("加入对比", new com.vaadin.contextmenu.Menu.Command() {
			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				Set<Document> set = rightTree.getSelectedItems();
				List<Document> list = new ArrayList<Document>(set);
				Document selectedDocument = list.get(0);
				selectedDocument.setAlias(selectedDocument.getAlias());
				addForComparing(site, selectedDocument);
			}
        });
        
        TabSheet sheet = createCenterTabSheet();
  
        HorizontalSplitPanel subSplitor = new HorizontalSplitPanel();
        subSplitor.setSizeFull();
        subSplitor.setSplitPosition(750, Unit.PIXELS);
        subSplitor.setFirstComponent(sheet);
        subSplitor.setSecondComponent(rightTree);
        
        this.setFirstComponent(leftTree);
        this.setSecondComponent(subSplitor);
        
//        this.addComponents(leftTree, sheet, rightTree);
//        this.setComponentAlignment(leftTree, Alignment.TOP_LEFT);
//        this.setComponentAlignment(sheet, Alignment.TOP_CENTER);
//        this.setComponentAlignment(rightTree, Alignment.TOP_LEFT);
//        
//        this.setExpandRatio(leftTree, 2.0f);
//        this.setExpandRatio(sheet, 8.0f);
//        this.setExpandRatio(rightTree, 2.0f);
	}
	
	/**
	 * Constructor
	 */
	private TabSheet createCenterTabSheet() {
		TabSheet tabSheet = new TabSheet();
		tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		tabSheet.setWidth("100%");
		tabSheet.setHeight("100px");
        VerticalLayout hierarchy = new VerticalLayout();
        hierarchy.setMargin(false);
        hierarchy.setSpacing(false);
        hierarchy.setSizeFull();
        VerticalLayout AI = new VerticalLayout();
        AI.setMargin(false);
        AI.setSpacing(false);
        AI.setSizeFull();
        tabSheet.addTab(hierarchy, "人工对比");
        tabSheet.addTab(AI, "智能对比");
        tabSheet.setTabCaptionsAsHtml(true);
        tabSheet.setCaption(null);
        tabSheet.addSelectedTabChangeListener(e -> {
        	// Find the tabsheet
            TabSheet tabsheet = e.getTabSheet();
            // Find the tab (here we know it's a layout)
            Layout tab = (Layout) tabsheet.getSelectedTab();
            // Get the tab caption from the tab object
            String caption = tabsheet.getTab(tab).getCaption();
            if ("人工对比".equals(caption)) {
            	toolbar.center2(false);
            } else {
            	toolbar.close();
            }
        });
        
        return tabSheet;
	}
	
	private void addForComparing(Site site, Document doc) {
		if (doc.getFileFullPath() != null) {
			try {
				FileObject fobj = new TB4FileSystem().resolveFile(site, doc.getFileFullPath());
				
				ImageWindow imageWindow = new ImageWindow(doc.getAlias()+"-历史", fobj, 1.0f);
				toolbar.setEditingWindow(imageWindow);
				imageWindow.addFocusListener(e -> {
					toolbar.setEditingWindow(imageWindow);
		        });
				imageWindow.center();
				
			} catch (FileException e) {
				e.printStackTrace();
				Notifications.warning("读取文件异常。"+e.getMessage());
			}
		}
	}
	
	public ToolbarWindow getToolbar() {
		return toolbar;
	}
	
	private ToolbarWindow toolbar = new ToolbarWindow();
	private Transaction transaction;
	private Tree<Document> leftTree = new Tree<>();
	private Tree<Document> rightTree = new Tree<>();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
