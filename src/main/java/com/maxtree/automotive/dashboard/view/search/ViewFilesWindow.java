package com.maxtree.automotive.dashboard.view.search;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.front.Thumbnail;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ViewFilesWindow extends Window implements ImageViewIF{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param trans
	 */
	public ViewFilesWindow(Transaction trans) {
		this.trans = trans;
		initComponents();
	}
	
	private void initComponents() {
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(true);
		this.setCaption("查看原文");
		this.setWidth("1200px");
		this.setHeight("660px");
		TextField boxBarCode = new TextField("条形码:");
		TextField boxPlateType = new TextField("号牌种类:");
		TextField boxPlateNumber = new TextField("号码号牌:");
		TextField boxVIN = new TextField("车辆识别代码:");
		boxBarCode.setWidth("100%");
		boxPlateType.setWidth("100%");
		boxPlateNumber.setWidth("100%");
		boxVIN.setWidth("100%");
		boxBarCode.setStyleName("ViewFilesWindow-TextField");
		boxPlateType.setStyleName("ViewFilesWindow-TextField");
		boxPlateNumber.setStyleName("ViewFilesWindow-TextField");
		boxVIN.setStyleName("ViewFilesWindow-TextField");
		boxBarCode.setValue(trans.getBarcode());
		boxPlateType.setValue(trans.getPlateType());
		boxPlateNumber.setValue(trans.getPlateNumber());
		boxVIN.setValue(trans.getVin());
		VerticalLayout west = new VerticalLayout();
		west.setSizeFull();
		west.setSpacing(false);
		west.setMargin(false);
		west.setStyleName("ViewFilesWindow-west");
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
		vlayout.setSpacing(false);
		vlayout.setMargin(false);
		vlayout.addComponents(Box.createVerticalBox(5),boxBarCode,Box.createVerticalBox(5),boxPlateType,Box.createVerticalBox(5),boxPlateNumber,Box.createVerticalBox(5),boxVIN);
		vlayout.setComponentAlignment(boxBarCode, Alignment.TOP_LEFT);
		vlayout.setComponentAlignment(boxPlateType, Alignment.TOP_LEFT);
		vlayout.setComponentAlignment(boxPlateNumber, Alignment.TOP_LEFT);
		vlayout.setComponentAlignment(boxVIN, Alignment.TOP_LEFT);
		west.addComponents(vlayout);
		Panel south = new Panel();
		south.setSizeFull();
		HorizontalLayout hlayout = new HorizontalLayout();
		south.setContent(hlayout);
		ui.access(new Runnable() {
			@Override
			public void run() {
				allDocs.clear();
				List<Document> list1 = ui.documentService.findAllDocument1(trans.getVin(), trans.getUuid());
				List<Document> list2 = ui.documentService.findAllDocument2(trans.getVin(), trans.getUuid());
				allDocs.addAll(list1);
				allDocs.addAll(list2);
				if(list1.size() > 0) {
					Site site = ui.siteService.findByCode(trans.getSiteCode());
					imgStage.display(site, list1.get(index));
					ViewFilesWindow.this.setCaption("查看原文-"+list1.get(index).getAlias());
				}
				
				/**
				 * 
				 */
				for(Document d : allDocs) {
					ByteArrayInputStream bis = new ByteArrayInputStream(d.getThumbnail());
					Thumbnail thumbnail = new Thumbnail(bis);
					thumbnail.addLayoutClickListener(e->{
						index = allDocs.indexOf(d);
						Site site = ui.siteService.findByCode(trans.getSiteCode());
						imgStage.display(site, d);
						ViewFilesWindow.this.setCaption("查看原文-"+d.getAlias());
					});
					hlayout.addComponent(thumbnail);
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		VerticalLayout center = new VerticalLayout();
		center.setSizeFull();
		center.setSpacing(false);
		center.setMargin(false);
		center.addComponents(imgStage, south);
		center.setExpandRatio(south, 0.2f);
		center.setExpandRatio(imgStage, 0.8f);
		HorizontalLayout main = new HorizontalLayout();
		main.setSizeFull();
		main.setSpacing(false);
		main.setMargin(false);
		main.addComponents(west, center);
		main.setExpandRatio(west, 0.2f);
		main.setExpandRatio(center, 0.8f);
		
		this.setContent(main);
	}

	@Override
	public void previous() {
		index--;
		if(index < 0) {
			index = allDocs.size() - 1;
		}
		Site site = ui.siteService.findByCode(trans.getSiteCode());
		imgStage.display(site, allDocs.get(index));
		setCaption("查看原文-"+allDocs.get(index).getAlias());
	}

	@Override
	public void next() {
		index++;
		if(index > allDocs.size()-1) {
			index = 0;
		}
		Site site = ui.siteService.findByCode(trans.getSiteCode());
		imgStage.display(site, allDocs.get(index));
		setCaption("查看原文-"+allDocs.get(index).getAlias());
	}
	
	/**
	 * 
	 * @param callback
	 * @param trans
	 */
	public static void open(Callback callback, Transaction trans) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ViewFilesWindow w = new ViewFilesWindow(trans);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private int index = 0;
	private List<Document> allDocs = new ArrayList<Document>();
	private Transaction trans;
	private ImageStage imgStage = new ImageStage(this, false);
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
