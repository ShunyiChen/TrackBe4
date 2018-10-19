package com.maxtree.automotive.dashboard.view.admin;

/**
 * 
 * @author Chen
 *
 */
public class DataDictionaryRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataDictionaryRow() {
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(manageBusinessType,manageMaterialName,managePlatenumType,manageLocation);
	}

	@Override
	public String getSearchTags() {
		return "管理业务类型,管理材料名称,管理号牌种类,管理地点,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 3;
	}
	
	@Override
	public String getTitle() {
		return "数据字典";
	}
	
	@Override
	public String getImageName() {
		return "DD.png";
	}
	
	private RowItemWithTitle manageBusinessType = new RowItemWithTitle("管理业务类型");
	private RowItemWithTitle manageMaterialName = new RowItemWithTitle("管理材料名称");
	private RowItemWithTitle managePlatenumType = new RowItemWithTitle("管理号牌种类");
	private RowItemWithTitle manageLocation = new RowItemWithTitle("管理地点");
	
}
