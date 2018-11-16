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

	/**
	 * 
	 * @param rootView
	 */
	public AboutSystemRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(aboutTB4Sys,sysSettings,logging,developTool);
		aboutTB4Sys.addLayoutClickListener(e->{
			AboutTB4.open();
		});
		sysSettings.addLayoutClickListener(e->{
			rootView.forward(new SystemSettingsView("系统参数", rootView));
		});
		logging.addLayoutClickListener(e->{
			LoggingWindow.open();
		});
		developTool.addLayoutClickListener(e->{
			DEV.open();
		});
	}

	@Override
	public String getSearchTags() {
		return "系统参数,系统日志,开发者工具,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 6;
	}
	
	@Override
	public String getTitle() {
		return "关于系统";
	}
	
	@Override
	public String getImageName() {
		return "about.png";
	}
	
	private RowItemWithTitle aboutTB4Sys = new RowItemWithTitle("关于系统");
	private RowItemWithTitle sysSettings = new RowItemWithTitle("系统参数");
	private RowItemWithTitle logging = new RowItemWithTitle("系统日志");
	private RowItemWithTitle developTool = new RowItemWithTitle("开发者工具");
	private AdminMainView rootView;
}
