package com.maxtree.automotive.dashboard.view.finalcheck;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.front.Thumbnail;
import com.maxtree.automotive.dashboard.view.quality.ConfirmInformationGrid;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class MainPane extends VerticalLayout implements ImageViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainPane() {
		this.setWidth("100%");
		int h = Page.getCurrent().getBrowserWindowHeight();
		this.setHeight((h-100)+"px");
		Page.getCurrent().addBrowserWindowResizeListener(e->{
			this.setHeight((e.getHeight()-100)+"px");
			
			imageStage.setWidth((e.getWidth()-495)+"px");
			imageStage.setHeight((e.getHeight()-138)+"px");
			imageStage.fittedSize();
		});
		
		this.setMargin(false);
		this.setSpacing(false);
		this.addStyleName("SplitPane");
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loginUser = ui.userService.getUserByUserName(username);
		initNavigationBar();
		infoGrid.setReadOnly();
        splitPane.addStyleName("SplitPane-splitPane");
        splitPane.setSizeFull();
		splitPane.setSplitPosition(290, Unit.PIXELS);
        splitPane.setFirstComponent(navigationBar);
        splitPane.setSecondComponent(imageStage);
        
        this.addComponents(infoGrid,splitPane);
		this.setComponentAlignment(infoGrid, Alignment.TOP_LEFT);
		this.setComponentAlignment(splitPane, Alignment.TOP_CENTER);
		this.setExpandRatio(infoGrid, 0);
		this.setExpandRatio(splitPane, 1);
	}
	
	/**
	 * 
	 * @param trans
	 */
	public void load(Transaction trans) {
		this.trans = trans;
		rows.removeAllComponents();
//		allDocs = ui.documentService.findAllDocument1(trans.getVin(), trans.getUuid());
		infoGrid.setValues(trans);
		site = ui.siteService.findById(trans.getSiteUniqueId());
		for (Document doc : allDocs) {
			ByteArrayInputStream is = new ByteArrayInputStream(doc.getThumbnail());
			Thumbnail thumbnail = new Thumbnail(doc.getAlias(), is);
			thumbnail.addLayoutClickListener(e->{
				index = allDocs.indexOf(doc);
				imageStage.display(site, doc);
			});
			ContextMenu menu = new ContextMenu(thumbnail, true);
			menu.addItem("新增", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					
					Callback2 callback1 = new Callback2() {
						@Override
						public void onSuccessful(Object... objects) {
							load(trans);
						}
					};
					
					Callback2 callback = new Callback2() {
						@Override
						public void onSuccessful(Object... objects) {
							DataDictionary dd = (DataDictionary) objects[0];
							PopupCaptureWindow.open(trans, dd, callback1);
						}
					};
					PopupNameSelector.open(callback);
					
				}
			});
			menu.addItem("变更", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					Callback2 callback = new Callback2() {
						@Override
						public void onSuccessful(Object... objects) {
							load(trans);
						}
					};
					PopupCaptureWindow.edit(trans,doc,callback);
				}
			});
			menu.addItem("历史记录", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					Callback callback = new Callback() {

						@Override
						public void onSuccessful() {
							load(trans);
						}
					};
					PopupHistory.open(doc,callback);
				}
			});
			menu.addSeparator();
			menu.addItem("删除", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					Callback onOK = new Callback() {

						@Override
						public void onSuccessful() {
							//从文件系统删除
							try {
								fileSystem.deleteFile(site, doc.getFileFullPath());
							} catch (FileException e) {
								e.printStackTrace();
							}
							
							//从数据库删除
							ui.documentService.deleteById(doc.getDocumentUniqueId(),trans.getVin());
							//remove from UI
							rows.removeComponent(thumbnail);
							allDocs.remove(doc);
						}
					};
					MessageBox.showMessage("提示", "请确认是否删除当前资料。", MessageBox.WARNING, onOK, "确定");
				}
			});
			
			rows.addComponent(thumbnail);
			rows.setComponentAlignment(thumbnail, Alignment.TOP_CENTER);
			if(allDocs.size() > 0) {
				int w = Page.getCurrent().getBrowserWindowWidth();
				int h = Page.getCurrent().getBrowserWindowHeight();
				imageStage.setWidth((w-495)+"px");
				imageStage.setHeight((h-138)+"px");
				imageStage.display(site, allDocs.get(0));
			}
		}
	}
	
	/**
	 * 
	 */
	private void initNavigationBar() {
		navigationBar = new VerticalLayout();
		navigationBar.setWidth("100%");
		navigationBar.setHeight("100%");
		navigationBar.setSpacing(false);
		navigationBar.setMargin(false);
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("SplitPane-toolbar");
		toolbar.setSpacing(false);
		toolbar.setMargin(false);
		Button add = new Button();
		add.addClickListener(e->{
			Callback2 callback1 = new Callback2() {
				@Override
				public void onSuccessful(Object... objects) {
					load(trans);
				}
			};
			
			Callback2 callback = new Callback2() {
				@Override
				public void onSuccessful(Object... objects) {
					DataDictionary dd = (DataDictionary) objects[0];
					PopupCaptureWindow.open(trans, dd, callback1);
				}
			};
			PopupNameSelector.open(callback);
		});
		add.setWidth("25px");
		add.setHeight("25px");
		add.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		add.addStyleName(ValoTheme.BUTTON_LINK);
		add.setIcon(VaadinIcons.PLUS_CIRCLE);
		toolbar.addComponent(add);
		
		Panel scroll = new Panel();
		scroll.addStyleName("SplitPane-scroll");
		scroll.setSizeFull();
		scroll.setContent(rows);
		rows.setMargin(false);
		rows.setSpacing(false);
		rows.setSizeUndefined();
		
		navigationBar.addComponents(toolbar,scroll);
		navigationBar.setComponentAlignment(toolbar, Alignment.TOP_LEFT);
		navigationBar.setComponentAlignment(scroll, Alignment.TOP_LEFT);
		
		navigationBar.setExpandRatio(toolbar, 0);
		navigationBar.setExpandRatio(scroll, 1);
	}
	
	@Override
	public void previous() {
		index--;
		if(index < 0) {
			index = allDocs.size() - 1;
		}
		imageStage.display(site, allDocs.get(index));
	}

	@Override
	public void next() {
		index++;
		if(index > allDocs.size()-1) {
			index = 0;
		}
		imageStage.display(site, allDocs.get(index));
	}
	
	private Transaction trans;
	private TB4FileSystem fileSystem = new TB4FileSystem();
	private User loginUser;
	private Site site;
	private int index = 0;
	private List<Document> allDocs = null;
	private ImageStage imageStage = new ImageStage(this);
	private VerticalLayout navigationBar;
	private VerticalLayout rows = new VerticalLayout();
	private HorizontalSplitPanel splitPane = new HorizontalSplitPanel();
	private ConfirmInformationGrid infoGrid = new ConfirmInformationGrid(null);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
