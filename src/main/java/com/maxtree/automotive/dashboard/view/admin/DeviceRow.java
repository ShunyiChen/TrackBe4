package com.maxtree.automotive.dashboard.view.admin;

public class DeviceRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param rootView
	 */
	public DeviceRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(rowItemWithOptions);
		rowItemWithOptions.addLayoutClickListener(e -> {
			rootView.forward(new DeviceView("管理高拍仪", rootView));
		});
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
	
	private RowItemWithOptions rowItemWithOptions = new RowItemWithOptions("高拍仪设备", new String[] {"无","无锡华通H6-1","维山VSA305FD"});
	private AdminMainView rootView;
}
