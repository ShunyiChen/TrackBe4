package com.maxtree.automotive.dashboard.view.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;

import com.maxtree.automotive.dashboard.BusinessCode;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.DoubleField;
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
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class Manual extends VerticalLayout implements ImageViewIF,ClickListener {

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
		numField.setWidth("80px");
		numField.setHeight("25px");
		ShortcutListener enterListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void handleAction(Object sender, Object target) {
				System.out.println("======"+sender+","+target);
				
			}
		};
		numField.addShortcutListener(enterListener);
		
		slider.setMin(0.0);
		slider.setMax(100.0);
		slider.setValue(50.0);
		slider.setWidth("150px");
		slider.addStyleName("v-slider");
		slider.addValueChangeListener(e->{
			numField.setValue(e.getValue()+"");
		});
		lineLabel_1.setIcon(VaadinIcons.LINE_V);
		lineLabel_2.setIcon(VaadinIcons.LINE_V);
		functionImg.setIcon(VaadinIcons.CURSOR);
		
		fittedSize.setIcon(VaadinIcons.EXPAND_FULL);
		fittedSize.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		fittedSize.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		fittedSize.setWidth("18px");
		fittedSize.setHeight("18px");
		fittedSize.setId("fittedSize");
		fittedSize.setDescription("适应窗口大小");
		fittedSize.addClickListener(this);
		actualSize.setIcon(VaadinIcons.BULLSEYE);
		actualSize.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		actualSize.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		actualSize.setWidth("18px");
		actualSize.setHeight("18px");
		actualSize.setId("actualSize");
		actualSize.setDescription("原图");
		actualSize.addClickListener(this);
		
		subtoolbar.setSpacing(false);
		subtoolbar.setMargin(false);
		subtoolbar.setWidthUndefined();
		subtoolbar.setHeight("25px");
		subtoolbar.addComponents(fittedSize,Box.createHorizontalBox(5),actualSize,Box.createHorizontalBox(25),lineLabel_1,functionImg,lineLabel_2,functionDesc);
//		subtoolbar.setComponentAlignment(fittedSize, Alignment.TOP_LEFT);
//		subtoolbar.setComponentAlignment(actualSize, Alignment.TOP_LEFT);
		subtoolbar.setComponentAlignment(lineLabel_1, Alignment.MIDDLE_CENTER);
		subtoolbar.setComponentAlignment(functionImg, Alignment.MIDDLE_CENTER);
		subtoolbar.setComponentAlignment(lineLabel_2, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(false);
		toolbar.setMargin(false);
		toolbar.setHeight("26px");
		toolbar.setWidth("100%");
		toolbar.setStyleName("Manual-toolbar");
		toolbar.addComponents(subtoolbar);
		
		Panel scrollPane = new Panel();
		scrollPane.setSizeFull();
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
        	if(item.getThumbnail() == null) {
        		return VaadinIcons.FOLDER;
        	} else {
        		return VaadinIcons.FILE_PICTURE;
        	}
        });
        rightTree.setItemDescriptionGenerator(item -> {
        	return item.getAlias();
        });
        rightTree.addContextClickListener(event -> {
        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
        	Document item = e.getItem();
//        	tree.select(item);
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
        	if(item.getThumbnail() == null) {
        		return VaadinIcons.FOLDER;
        	} else {
        		return VaadinIcons.FILE_PICTURE;
        	}
        });
        leftTree.addContextClickListener(event -> {
        	TreeContextClickEvent<Document> e = (TreeContextClickEvent<Document>) event;
        	Document item = e.getItem();
//        	tree.select(item);
        });
        leftTree.setSelectionMode(SelectionMode.SINGLE);
        leftTree.addItemClickListener(e -> {
			// 排除根节点
        	//TODO
        	if(e.getItem().getThumbnail() == null) {
        		imgStage.clean();
				// Set the position of the splitter as percentage
		        rightSplit.setSplitPosition(100, Unit.PERCENTAGE);
        	}
        	else if (e.getItem().getAlias().startsWith("机动车")) {
				selectedNode = e.getItem();
				imgStage.display(site, selectedNode);
				// Set the position of the splitter as percentage
		        rightSplit.setSplitPosition(75, Unit.PERCENTAGE);
//		        System.out.println(tool.isVisible()+"=========");
		        tool.show();
		        
        	} else {
        		selectedNode = e.getItem();
				imgStage.display(site, selectedNode);
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
		scrollPane.setContent(main);
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		this.addComponents(toolbar,scrollPane);
		this.setExpandRatio(scrollPane,1);
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
				ImageWindow imageWindow = new ImageWindow(doc.getAlias(), fobj, 1.0f);
				tool.setEditingWindow(imageWindow);
				imageWindow.addFocusListener(e -> {
					tool.setEditingWindow(imageWindow);
		        });
				imageWindow.center();
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
	
	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton().getId().equals("actualSize")) {
			imgStage.actualSize();
			
		} else if(event.getButton().getId().equals("fittedSize")) {
			imgStage.fittedSize();
		}
	}
	
	public void updateToolbar(String function, double value) {
		subtoolbar.removeAllComponents();
		numField.setValue(value+"");
		slider.setValue(value);
		
		if(function.equals("撤销")) {
			functionImg.setIcon(VaadinIcons.ARROW_BACKWARD);
			createToolbar();
		} else if(function.equals("重做")) {
			functionImg.setIcon(VaadinIcons.ARROW_FORWARD);
			createToolbar();
		} else if(function.equals("原图")) {
			functionImg.setIcon(VaadinIcons.DOT_CIRCLE);
			createToolbar();
		} else if(function.equals("适应窗体")) {
			functionImg.setIcon(VaadinIcons.EXPAND_FULL);
			createToolbar();
		} else if(function.equals("锐化")) {
			functionImg.setIcon(VaadinIcons.EYE);
			createToolbar();
		} else if(function.equals("边缘")) {
			functionImg.setIcon(VaadinIcons.STAR_HALF_RIGHT_O);
			createToolbar();
		} else if(function.equals("上阴影")) {
			functionImg.setIcon(VaadinIcons.PADDING_TOP);
			createToolbar();
		} else if(function.equals("下阴影")) {
			functionImg.setIcon(VaadinIcons.PADDING_BOTTOM);
			createToolbar();
		} else if(function.equals("左阴影")) {
			functionImg.setIcon(VaadinIcons.PADDING_LEFT);
			createToolbar();
		} else if(function.equals("右阴影")) {
			functionImg.setIcon(VaadinIcons.PADDING_RIGHT);
			createToolbar2();
		} else if(function.equals("伸缩")) {
			functionImg.setIcon(VaadinIcons.EXPAND_SQUARE);
			createToolbar2();
		} else if(function.equals("旋转")) {
			functionImg.setIcon(VaadinIcons.ROTATE_LEFT);
			createToolbar2();
		} else if(function.equals("透明度")) {
			functionImg.setIcon(VaadinIcons.COINS);
			createToolbar2();
		} else if(function.equals("亮度")) {
			functionImg.setIcon(VaadinIcons.MORNING);
			createToolbar2();
		} else if(function.equals("对比度")) {
			functionImg.setIcon(VaadinIcons.ADJUST);
			createToolbar2();
		}
	}
	
	private void createToolbar() {
		subtoolbar.addComponents(fittedSize,Box.createHorizontalBox(5),actualSize,Box.createHorizontalBox(25),lineLabel_1,functionImg,lineLabel_2,functionDesc);
//		subtoolbar.setComponentAlignment(fittedSize, Alignment.MIDDLE_LEFT);
//		subtoolbar.setComponentAlignment(actualSize, Alignment.MIDDLE_LEFT);
		subtoolbar.setComponentAlignment(lineLabel_1, Alignment.MIDDLE_CENTER);
		subtoolbar.setComponentAlignment(functionImg, Alignment.MIDDLE_CENTER);
		subtoolbar.setComponentAlignment(lineLabel_2, Alignment.MIDDLE_CENTER);
//		subtoolbar.setComponentAlignment(functionDesc, Alignment.MIDDLE_CENTER);
	}
	
	private void createToolbar2() {
		subtoolbar.addComponents(fittedSize,Box.createHorizontalBox(5),actualSize,Box.createHorizontalBox(25),lineLabel_1,functionImg,lineLabel_2,slider,numField);
//		subtoolbar.setComponentAlignment(fittedSize, Alignment.MIDDLE_LEFT);
//		subtoolbar.setComponentAlignment(actualSize, Alignment.MIDDLE_LEFT);
		subtoolbar.setComponentAlignment(lineLabel_1, Alignment.MIDDLE_CENTER);
		subtoolbar.setComponentAlignment(functionImg, Alignment.MIDDLE_CENTER);
		subtoolbar.setComponentAlignment(lineLabel_2, Alignment.MIDDLE_CENTER);
//		subtoolbar.setComponentAlignment(slider, Alignment.MIDDLE_LEFT);
//		subtoolbar.setComponentAlignment(numField, Alignment.MIDDLE_LEFT);
	}
	
	private Label lineLabel_1 = new Label();
	private Label lineLabel_2 = new Label();
	private Button fittedSize = new Button();
	private Button actualSize = new Button();
	public Label functionDesc = new Label("此工具无其他选项");
	public Label functionImg = new Label();
	public DoubleField numField = new DoubleField();
	public Slider slider = new Slider();
	private Document leftRoot = new Document();
	private Tree<Document> leftTree = new Tree<>();
	private TreeData<Document> leftTreeData = new TreeData<Document>();
	private Document rightRoot = new Document();
	private Tree<Document> rightTree = new Tree<>();
	private TreeData<Document> rightTreeData = new TreeData<Document>();
	private ImageStage imgStage = new ImageStage(this,true);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Transaction transaction;
	private Site site;
	private Document selectedNode;
	private Tool tool = new Tool(this);
	private HorizontalLayout subtoolbar = new HorizontalLayout();
}
