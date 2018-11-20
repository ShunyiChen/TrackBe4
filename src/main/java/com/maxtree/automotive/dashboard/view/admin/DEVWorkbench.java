package com.maxtree.automotive.dashboard.view.admin;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class DEVWorkbench extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DEVWorkbench() {
		initComponents();
	}
	
	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		HorizontalLayout hLayout1 = new HorizontalLayout();
		hLayout1.setMargin(false);
		hLayout1.setSpacing(false);
		hLayout1.setWidthUndefined();
		hLayout1.setHeight("40px");
		Label name = new Label(TB4Application.NAME);
		hLayout1.addComponents(name);
		hLayout1.setComponentAlignment(name, Alignment.MIDDLE_LEFT);
		
		tabSheet.setCloseHandler(new CloseHandler() {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent) {
				Tab tab = tabsheet.getTab(tabContent);
				tabsheet.removeTab(tab);
			}
		});
//        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
		tabSheet.setSizeFull();
		tabSheet.setHeight((Page.getCurrent().getBrowserWindowHeight()- 240)+"px");
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.addStyleName(ValoTheme.TABSHEET_ONLY_SELECTED_TAB_IS_CLOSABLE);
        
        this.addComponents(tabSheet);
        
//		HorizontalLayout hLayout2 = new HorizontalLayout();
//		hLayout2.setMargin(false);
//		hLayout2.setSpacing(false);
//		hLayout2.setWidthUndefined();
//		hLayout2.setHeight("40px");
//		Label verTitle = new Label("版本:");
//		Label version = new Label(TB4Application.VERSION);
//		hLayout2.addComponents(verTitle, Box.createHorizontalBox(5), version);
//		hLayout2.setComponentAlignment(verTitle, Alignment.MIDDLE_LEFT);
//		hLayout2.setComponentAlignment(version, Alignment.MIDDLE_LEFT);
//		
//		HorizontalLayout hLayout3 = new HorizontalLayout();
//		hLayout3.setMargin(false);
//		hLayout3.setSpacing(false);
//		hLayout3.setWidthUndefined();
//		hLayout3.setHeight("40px");
//		Label binTitle = new Label("构建ID:");
//		Label buildid = new Label(TB4Application.BUILD_ID);
//		hLayout3.addComponents(binTitle, Box.createHorizontalBox(5), buildid);
//		hLayout3.setComponentAlignment(binTitle, Alignment.MIDDLE_LEFT);
//		hLayout3.setComponentAlignment(buildid, Alignment.MIDDLE_LEFT);
//		this.addComponents(hLayout1, hLayout2, hLayout3);
	}
	
	public void newSQLScript() {
		VerticalLayout comp = createTabComponent();
//    	Resource icon = new ThemeResource("img/gear.png");
		tabNum++;
    	Tab tab = tabSheet.addTab(comp, "SQL脚本"+tabNum, VaadinIcons.COG_O);
    	tab.setClosable(true);
    	
    	tabSheet.setSelectedTab(tab);
	}
	
	public void run(Callback callback) throws SQLException {
		start = System.currentTimeMillis();
		TabComponent tcomp = (TabComponent) tabSheet.getSelectedTab();
		if(tcomp == null) {
			Notifications.warning("请新建一个SQL脚本");
			callback.onSuccessful();
			return;
		}
		tcomp.scrollTable.clearData();
		String sql = tcomp.sql().toLowerCase().trim();
		if(StringUtils.isEmpty(sql)) {
			Notifications.warning("SQL语句不能为空。");
			callback.onSuccessful();
			return;
		}
		if(sql.startsWith("update")
				|| sql.startsWith("delete")
				|| sql.startsWith("insert")
				|| sql.startsWith("create")
				|| sql.startsWith("drop")
				|| sql.startsWith("do")) {
			Connection conn = ui.transactionService.getConnection();
			Statement stmt = conn.createStatement();
			boolean bool = stmt.execute(tcomp.sql());
			stmt.close();
			conn.close();
			long end = System.currentTimeMillis();
			
			tcomp.succeed("执行成功 ("+(end-start)+"毫秒)");
			tcomp.logSheet.setSelectedTab(0);
			tcomp.errorConsole.setValue("");
		}
		else if(sql.startsWith("select")){
			Connection conn = ui.transactionService.getConnection();
		    Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(tcomp.sql());
            ResultSetMetaData rsmd = res.getMetaData();
            int numOfColumns = rsmd.getColumnCount();
            String[] columnNames = new String[numOfColumns];
            for(int i = 0;i < numOfColumns;i++) {
            	columnNames[i] = rsmd.getColumnName(1+i);
            }
            tcomp.scrollTable.setColumnNames(columnNames);
            
			List<List<String>> data = new ArrayList<>();
			while (res.next()) {
				List<String> row = new ArrayList<>();
				for (int i = 0; i < numOfColumns; i++) {
					java.sql.Types dl;
					int j = i + 1;
					if (rsmd.getColumnType(j) == java.sql.Types.VARCHAR) {
						String val = res.getString(rsmd.getColumnName(j));
						row.add(val);
					} else if (rsmd.getColumnType(j) == java.sql.Types.INTEGER) {
						int val = res.getInt(rsmd.getColumnName(j));
						row.add(val+"");
					} else if (rsmd.getColumnType(j) == java.sql.Types.SMALLINT) {
						int val = res.getInt(rsmd.getColumnName(j));
						row.add(val+"");
					} else if (rsmd.getColumnType(j) == java.sql.Types.TIMESTAMP) {
						Timestamp val = res.getTimestamp(rsmd.getColumnName(j));
						row.add(val == null? "NULL" : val.toString());
					} else if (rsmd.getColumnType(j) == java.sql.Types.BINARY) {
						InputStream is = res.getBinaryStream(rsmd.getColumnName(j));
						if(is != null) {
							row.add("XXX");
						} else {
							row.add("NULL");
						}
						// ps.setBinaryStream(4, new ByteArrayInputStream(document.getThumbnail()),
						// document.getThumbnail().length);
					}
					else if (rsmd.getColumnType(j) == java.sql.Types.BIT) {
						Boolean bool = res.getBoolean(rsmd.getColumnName(j));
						row.add(bool+"");
					}
					else {
						
						System.out.println(rsmd.getColumnType(j)+"========");
						
						int val = res.getInt(rsmd.getColumnName(j));
						row.add(val+"");
					}
				}
				data.add(row);
			}
			long end = System.currentTimeMillis();
			tcomp.succeed("执行成功 ("+(end-start)+"毫秒),总行数："+data.size());
		 
			res.close();
			stmt.close();
			conn.close();
			
			tcomp.scrollTable.setItems(data);
			tcomp.logSheet.setSelectedTab(0);
			tcomp.errorConsole.setValue("");
		}
		else {
			long end = System.currentTimeMillis();
			tcomp.failure("执行失败("+(end-start)+"毫秒)");
		}
		callback.onSuccessful();
	}
	
	/**
	 * 
	 * @param e
	 */
	public void throwException(SQLException ex) {
		TabComponent tcomp = (TabComponent) tabSheet.getSelectedTab();
		if(tcomp == null) {
			Notifications.warning("请新建一个SQL脚本");
			return;
		}
		StringBuffer sb = new StringBuffer(500);
	    StackTraceElement[] st = ex.getStackTrace();
	    sb.append(new Date()+"  "+ex.getClass().getName() + ": " + ex.getMessage() + "\n");
	    for (int i = 0; i < st.length; i++) {
	       sb.append("\t at " + st[i].toString() + "\n");
	    }
	    tcomp.errorConsole.setValue(sb.toString());
	    tcomp.logSheet.setSelectedTab(1);
	    long end = System.currentTimeMillis();
	    tcomp.failure("执行失败("+(end-start)+"毫秒)");
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createTabComponent() {
    	return new TabComponent();
	}
	

	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TabSheet tabSheet = new TabSheet();
	private int tabNum = 0;
	private long start = 0;
}

class TabComponent extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TabComponent() {
    	setSizeFull();
    	this.setMargin(false);
    	this.setSpacing(false);
    	sqlEditor.setWidth("100%");
    	sqlEditor.setHeight("100%");
    	sqlEditor.setWordWrap(false);
    	errorConsole.setSizeFull();
    	
    	logSheet.setSizeFull();
    	logSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
//    	Resource icon = new ThemeResource("img/chrome_menu.png");
    	logSheet.addTab(scrollTable, "结果",VaadinIcons.TABLE);
    	
    	Panel scroll = new Panel();
    	scroll.setSizeFull();
    	scroll.setContent(errorConsole);
    	logSheet.addTab(scroll, "错误信息",VaadinIcons.BUG);
    	
    	VerticalSplitPanel split = new VerticalSplitPanel();
    	split.setSplitPosition(165f,Unit.PIXELS);
    	split.setFirstComponent(sqlEditor);
    	split.setSecondComponent(logSheet);
    	
    	HorizontalLayout footer = new HorizontalLayout();
    	footer.setSpacing(false);
    	footer.setMargin(false);
    	footer.addComponent(infoLabel);
    	
    	this.addComponents(split,footer);
    	this.setExpandRatio(split, 1);
    	this.setExpandRatio(footer, 0);
	}
	
	public void succeed(String msg) {
		String basic = "<font size=\"2\" color=\"black\">状态:</font><font size=\"2\" color=\"green\">"+msg+"</font>";
		infoLabel.setValue(basic);
	}
	
	public void failure(String errMsg) {
		String basic = "<font size=\"2\" color=\"black\">状态:</font><font size=\"2\" color=\"red\">"+errMsg+"</font>";
		infoLabel.setValue(basic);
	}
	
	public String sql() {
		if(StringUtils.isEmpty(sqlEditor.getValue())) {
			return "";
		}
		return sqlEditor.getValue().trim();
	}
	
	public TextArea sqlEditor = new TextArea();
	public TextArea errorConsole = new TextArea();
	public ResultsTable scrollTable =  new ResultsTable();
	public Label infoLabel = new Label("<font size=\"2\" color=\"black\">状态:</font>", ContentMode.HTML);
	public TabSheet logSheet = new TabSheet();
}

class ResultsTable extends Panel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResultsTable() {
		this.setSizeFull();
		main.setSizeUndefined();
		main.setMargin(false);
		main.setSpacing(false);
		body.setSizeUndefined();
		body.setSpacing(false);
		body.setMargin(false);
		header.setSpacing(false);
		header.setMargin(false);
		header.setWidthUndefined();
		header.setHeight("23px");
		main.addComponents(header, body);
		main.setExpandRatio(body, 1);
		main.setExpandRatio(header, 0);
//		main
//		Panel scroll = new Panel();
//		scroll.setWidth("1024px");
//		scroll.setHeight("100%");
//		scroll.setContent(main);
//		this.addComponents(scroll);
//		this.setExpandRatio(scroll, 1);
		
		
		this.setContent(main);
	}
	
	/**
	 * 
	 * @param columnNames
	 */
	public void setColumnNames(String[] columnNames) {
		for(String colName : columnNames) {
			Label l = new Label(colName);
			l.setWidth("160px");
			header.addComponent(l);
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	public void setItems(List<List<String>> data) {
		for(List<String> list : data) {
			HorizontalLayout row = new HorizontalLayout();
			row.setSpacing(false);
			row.setMargin(false);
			row.setWidthUndefined();
			row.setHeightUndefined();
			for(String v : list) {
				Label l = new Label(v);
				l.setWidth("160px");
				l.setHeightUndefined();
				row.addComponent(l);
			}
			body.addComponent(row);
		}
	}
	
	public void clearData() {
		header.removeAllComponents();
		body.removeAllComponents();
	}
	
	private HorizontalLayout header = new HorizontalLayout();
	private VerticalLayout body = new VerticalLayout();
	private VerticalLayout main = new VerticalLayout();
}
