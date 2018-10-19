package com.maxtree.automotive.dashboard.view.admin;

/**
 * 
 * @author Chen
 *
 */
public class AboutSystemRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutSystemRow() {
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(aboutTB4Sys,help,developTool);
	}

	@Override
	public String getSearchTags() {
		return "关于TB4系统,帮助文档,开发者工具,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 5;
	}
	
	@Override
	public String getTitle() {
		return "关于系统";
	}
	
	@Override
	public String getImageName() {
		return "about.png";
	}
	
	private RowItemWithTitle aboutTB4Sys = new RowItemWithTitle("关于TB4系统");
	private RowItemWithTitle help = new RowItemWithTitle("帮助文档");
	private RowItemWithTitle developTool = new RowItemWithTitle("开发者工具");
}
