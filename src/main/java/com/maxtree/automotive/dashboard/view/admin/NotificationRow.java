package com.maxtree.automotive.dashboard.view.admin;

/**
 * 
 * @author Chen
 *
 */
public class NotificationRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param rootView
	 */
	public NotificationRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(messageManagement);
	}

	@Override
	public String getSearchTags() {
		return "管理消息,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 5;
	}
	
	@Override
	public String getTitle() {
		return "消息";
	}
	
	@Override
	public String getImageName() {
		return "notification.png";
	}
	
	private RowItemWithTitle messageManagement = new RowItemWithTitle("管理消息");
	private AdminMainView rootView;
}
