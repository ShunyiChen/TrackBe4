package com.maxtree.automotive.dashboard.view.admin;

/**
 * 
 * @author Chen
 *
 */
public class FileTransferRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param rootView
	 */
	public FileTransferRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(manageSite,manageEmbeddedServer);
		manageSite.addLayoutClickListener(e->{
			rootView.forward(new SiteView("管理站点", rootView));
		});
		manageEmbeddedServer.addLayoutClickListener(e->{
			rootView.forward(new EmbeddedServerView("FTP服务器", rootView));
		});
	}

	@Override
	public String getSearchTags() {
		return "管理站点,FTP服务器,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 2;
	}
	
	@Override
	public String getTitle() {
		return "文件传输";
	}
	
	@Override
	public String getImageName() {
		return "filetransfer.png";
	}
	
	private RowItemWithTitle manageSite = new RowItemWithTitle("管理站点");
	private RowItemWithTitle manageEmbeddedServer = new RowItemWithTitle("管理FTP服务器");
	private AdminMainView rootView;
}
