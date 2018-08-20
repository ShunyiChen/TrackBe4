package com.maxtree.automotive.dashboard.view.check;

import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

public class TabbedPane extends TabSheet implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param transaction
	 */
	public TabbedPane(Transaction transaction) {
		this.transaction = transaction;
		initComponents();
	}
	
	private void initComponents() {
		addStyleName(ValoTheme.TABSHEET_FRAMED);
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		this.setSizeFull();
		this.addSelectedTabChangeListener(this);
		addTab(new Manual(transaction), "手动图像比对");
		addTab(new Label(""), "智能图像比对");
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		
	}
	
	private Transaction transaction;
}
