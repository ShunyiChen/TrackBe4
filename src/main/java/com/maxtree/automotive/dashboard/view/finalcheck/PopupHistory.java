package com.maxtree.automotive.dashboard.view.finalcheck;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.DocumentHistory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
/**
 * 
 * @author chens
 *
 */
public class PopupHistory extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param document
	 */
	public PopupHistory(Document document) {
		this.document = document;
		this.setWidth("360px");
		this.setHeight("360px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("历史提交记录");
		
		main.setSpacing(false);
		main.setMargin(false);
		main.setWidth("100%");
		main.setHeightUndefined();
		this.setContent(main);
	}
	
	/**
	 * 
	 */
	private void loadItems() {
		main.removeAllComponents();
		List<DocumentHistory> lst = ui.documentService.findHistoryById(document.getDocumentUniqueId());
		for(DocumentHistory history : lst) {
			HistoryRow row = new HistoryRow(history);
			main.addComponent(row);
			main.setComponentAlignment(row, Alignment.TOP_CENTER);
		}
		if(lst.size() == 0) {
			main.addComponent(new Label("无历史记录"));
		}
	}
	
	public static void open(Document document) {
		PopupHistory w = new PopupHistory(document);
		UI.getCurrent().addWindow(w);
		w.center();
		w.loadItems();
	}
	
	private Document document;
	private VerticalLayout main = new VerticalLayout(); 
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
