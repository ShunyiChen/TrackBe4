package com.maxtree.automotive.dashboard.view.admin;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Log;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class LoggingTable extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param sortBy
	 */
	public LoggingTable(String sortBy) {
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		initTable();
	}
	
	private void initTable() {
		this.setSpacing(false);
		this.setMargin(false);
		
		body.setWidth("100%");
		body.setHeight("605px");
		LoggingTableHeader header = new LoggingTableHeader();
		this.addComponents(header,body);
		this.setComponentAlignment(header, Alignment.TOP_LEFT);
		this.setComponentAlignment(body, Alignment.TOP_LEFT);
		
		main.setSpacing(false);
		main.setMargin(false);
		body.setContent(main);
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param desc
	 */
	public void query(int limit,int offset,String sortBy, String desc) {
		if(sortBy == null)
			sortBy = "DATECREATED";
		if(desc == null)
			desc = "DESC";
		main.removeAllComponents();
		List<Log> lstLog = ui.loggingService.findByPaging(limit, offset, sortBy, desc);
		int i = 0;
		for (Log log : lstLog) {
			LoggingTableRow row = new LoggingTableRow(log);
			if(i % 2== 0) {
				row.addStyleName("LoggingTableRow_dark");
			}
			row.addStyleName("LoggingTableRow");
			
			main.addComponent(row);
			i++;
		}
		body.setContent(main);
	}
	
	private Panel body = new Panel();
	private VerticalLayout main = new VerticalLayout();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
