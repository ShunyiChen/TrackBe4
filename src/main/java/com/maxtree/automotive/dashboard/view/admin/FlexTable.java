package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Chen
 *
 */
public class FlexTable extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FlexTable() {
		initComponents();
	}
	
	private void initComponents() {
		this.setSizeFull();
		main.setWidth("100%");
		main.setHeightUndefined();
		main.addStyleName("FlexTable_main");
		this.setContent(main);
		this.addStyleName("FlexTable");
		init();
		doFilter("");
	}
	
	/**
	 * 
	 */
	private void clearAllFlexTableRows() {
		main.removeAllComponents();
	}
	
	/**
	 * 
	 * @param rows
	 */
	private void addFlexTableRows(List<FlexTableRow> rows) {
		for(FlexTableRow row : rows) {
			addFlexTableRow(row);
		}
	}
	
	/**
	 * 
	 * @param row
	 */
	private void addFlexTableRow(FlexTableRow row) {
		TitleRow title = new TitleRow(row.getTitle());
		main.addComponents(title, row);
		main.setComponentAlignment(title, Alignment.TOP_CENTER);
		main.setComponentAlignment(row, Alignment.TOP_CENTER);
	}
	
	/**
	 * 
	 * @param keyword
	 */
	public void doFilter(String keyword) {
		
		clearAllFlexTableRows();
		
		if(StringUtils.isEmpty(keyword)) {
			addFlexTableRows(data);
		}
		else {
			for(FlexTableRow row : data) {
				if(row.getSearchTags().contains(keyword)) {
					addFlexTableRow(row);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void init() {
		PeopleRow people = new PeopleRow();
		CommunityRow community = new CommunityRow();
		FileTransferRow fileTransfer = new FileTransferRow();
		DataDictionaryRow DD = new DataDictionaryRow();
		DeviceRow device = new DeviceRow();
		AboutSystemRow aboutSys = new AboutSystemRow();
		
		data.add(people);
		data.add(community);
		data.add(fileTransfer);
		data.add(DD);
		data.add(device);
		data.add(aboutSys);
	}
	
	private List<FlexTableRow> data = new ArrayList<>();
	private VerticalLayout main = new VerticalLayout();
}
