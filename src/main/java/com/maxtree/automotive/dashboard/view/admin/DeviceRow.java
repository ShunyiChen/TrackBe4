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
		return 4;
	}
	
	@Override
	public String getTitle() {
		return "设备";
	}
	
	@Override
	public String getImageName() {
		return "device.png";
	}
	
	private RowItemWithOptions HighqualityShooting = new RowItemWithOptions("高拍仪设备", new String[] {"无","无锡华通H6-1","维山VSA305FD"});
}
