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

	public FileTransferRow() {
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(manageSite,manageEmbeddedServer);
	}

	@Override
	public String getSearchTags() {
		return "管理站点,管理内嵌服务器,"+getTitle();
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
	private RowItemWithTitle manageEmbeddedServer = new RowItemWithTitle("管理内嵌服务器");
}
