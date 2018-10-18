package com.maxtree.automotive.dashboard.view.admin;

public class DeviceRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeviceRow() {
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(HighqualityShooting);
	}

	@Override
	public String getSearchTags() {
		return "选择高拍仪,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 2;
	}
	
	@Override
	public String getTitle() {
		return "设备";
	}
	
	private RowItemWithOptions HighqualityShooting = new RowItemWithOptions("选择高拍仪");
}
