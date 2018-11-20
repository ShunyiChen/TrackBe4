package com.maxtree.automotive.dashboard.view.finalcheck;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.admin.AssigningDataitemToBusinessWindow;
import com.maxtree.automotive.dashboard.view.admin.CustomGridRow;
import com.maxtree.automotive.dashboard.view.front.Thumbnail;
import com.maxtree.automotive.dashboard.view.front.ThumbnailRow;
import com.maxtree.automotive.dashboard.view.quality.ConfirmInformationGrid;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class SplitPane extends VerticalLayout implements ImageViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SplitPane() {
		this.setSizeFull();
		this.setMargin(false);
		this.setSpacing(false);
		this.addStyleName("SplitPane");
		loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//		firstComponent = new CssLayout() {
//			@Override
//			protected String getCss(final Component c) {
//				return "font-size: " + (12 + getComponentIndex(c)) + "px";
//			}
//		};
//		firstComponent.setSizeFull();
//		firstComponent.addStyleName("outlined");
//		firstComponent.setHeight(100.0f, Unit.PERCENTAGE);

		initNavigationBar();
//        Label secondComponent = new Label("ddddeee", ContentMode.HTML);
//        secondComponent.setWidth(100, Unit.PERCENTAGE);
		int h = Page.getCurrent().getBrowserWindowHeight();
		imageStage.setHeight((h-150)+"px");
		Page.getCurrent().addBrowserWindowResizeListener(e->{
			imageStage.setHeight((e.getHeight()-150)+"px");
		});
		
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
		allDocs = ui.documentService.findAllDocument1(trans.getVin(), trans.getUuid());
		infoGrid.setValues(trans);
		site = ui.siteService.findByCode(trans.getSiteCode());
		
		for (Document doc : allDocs) {
			ByteArrayInputStream is = new ByteArrayInputStream(doc.getThumbnail());
			Thumbnail thumbnail = new Thumbnail(doc.getAlias(), is);
			thumbnail.addLayoutClickListener(e->{
				index = allDocs.indexOf(doc);
				imageStage.display(site, doc);
			});
			ContextMenu menu = new ContextMenu(thumbnail, true);
			menu.addItem("删除", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					//从文件系统删除
//					try {
//						fileSystem.deleteFile(site, ufq.getFileFullPath());
//					} catch (FileException e) {
//						e.printStackTrace();
//					}
					
					//从数据库删除
//					ui.documentService.deleteById(doc.getDocumentUniqueId(), ufq.getLocation() ,tranvin());
					
//					//从UI删除
//					ThumbnailRow row = view.thumbnailGrid().mapRows.get(ufq.getDictionaryCode());
//					row.removeThumbnail(thumbnail);
				}
			});
			
			rows.addComponent(thumbnail);
			rows.setComponentAlignment(thumbnail, Alignment.TOP_CENTER);
		}
		
		if(allDocs.size() > 0) {
			// 默认打开
			index = allDocs.indexOf(allDocs.get(0));
			imageStage.display(site, allDocs.get(0));
		}
	}
	
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
			PopupCaptureWindow.open();
		});
		add.setWidth("25px");
		add.setHeight("25px");
		add.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		add.addStyleName(ValoTheme.BUTTON_LINK);
		add.setIcon(VaadinIcons.PLUS_CIRCLE);
		toolbar.addComponent(add);
		
		Panel scroll = new Panel();
		scroll.setSizeFull();
		int h = Page.getCurrent().getBrowserWindowHeight();
		scroll.setHeight((h-150)+"px");
		Page.getCurrent().addBrowserWindowResizeListener(e->{
			scroll.setHeight((e.getHeight()-150)+"px");
		});
		
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
		setCaption("查看原文-"+allDocs.get(index).getAlias());
	}

	@Override
	public void next() {
		index++;
		if(index > allDocs.size()-1) {
			index = 0;
		}
		imageStage.display(site, allDocs.get(index));
		setCaption("查看原文-"+allDocs.get(index).getAlias());
	}
	
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
