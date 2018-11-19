package com.maxtree.automotive.dashboard.view.finalcheck;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.front.Thumbnail;
import com.maxtree.automotive.dashboard.view.quality.ConfirmInformationGrid;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class SplitPane extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SplitPane() {
		this.setSizeFull();
		this.setMargin(false);
		this.setSpacing(false);
		this.addStyleName("SplitPane");
		
		
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
		
		
        Label secondComponent = new Label("ddddeee", ContentMode.HTML);
        secondComponent.setWidth(100, Unit.PERCENTAGE);
        
        splitPane.setSizeFull();
		splitPane.setSplitPosition(250, Unit.PIXELS);
        splitPane.setFirstComponent(navigationBar);
        splitPane.setSecondComponent(secondComponent);
        
        this.addComponents(infoGrid, splitPane);
        
		this.setComponentAlignment(infoGrid, Alignment.TOP_LEFT);
		this.setComponentAlignment(splitPane, Alignment.TOP_CENTER);
		this.setExpandRatio(infoGrid, 0);
		this.setExpandRatio(splitPane, 1);
	}
	
	/**
	 * 
	 * @param trans
	 */
	public void fillCarInfo(Transaction trans) {
		infoGrid.setValues(trans);
	}
	
	/**
	 * 
	 * @param lstDocs
	 */
	public void loadThumbnails(List<Document> lstDocs) {
		for (Document doc : lstDocs) {
//			final Label child = new Label(doc.getAlias());
//			child.addStyleName("childcomponent");
//			child.setSizeUndefined();
			ByteArrayInputStream is = new ByteArrayInputStream(doc.getThumbnail());
			Thumbnail thumbnail = new Thumbnail(doc.getAlias(), is);
			rows.addComponent(thumbnail);
			rows.setComponentAlignment(thumbnail, Alignment.TOP_CENTER);
		}
	}
	
	public void displayBigImage() {
		
	}
	
	private void initNavigationBar() {
		navigationBar = new VerticalLayout();
		navigationBar.setWidth("100%");
		navigationBar.setHeight("100%");
		navigationBar.setSpacing(false);
		navigationBar.setMargin(false);
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(false);
		toolbar.setMargin(false);
		Button add = new Button();
		add.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		add.setIcon(VaadinIcons.PLUS_CIRCLE);
		toolbar.addComponent(add);
		
		Panel scroll = new Panel();
		scroll.setSizeFull();
		int h = Page.getCurrent().getBrowserWindowHeight();
		scroll.setHeight((h-100)+"px");
		Page.getCurrent().addBrowserWindowResizeListener(e->{
			scroll.setHeight((e.getHeight()-100)+"px");
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
	
//	private CssLayout firstComponent;
	private VerticalLayout navigationBar;
	private VerticalLayout rows = new VerticalLayout();
	private HorizontalSplitPanel splitPane = new HorizontalSplitPanel();
	private ConfirmInformationGrid infoGrid = new ConfirmInformationGrid(null);
}
