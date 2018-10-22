package com.maxtree.automotive.dashboard.view.admin;

/**
 * 
 * @author Chen
 *
 */
public class CommunityRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommunityRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		this.addComponents(manageCommunity,manageCompany,manageStore);
		manageCommunity.addLayoutClickListener(e->{
			rootView.forward(new CommunityView("管理社区", rootView));
		});
		manageCompany.addLayoutClickListener(e->{
			rootView.forward(new CompanyView("管理机构", rootView));
		});
		manageStore.addLayoutClickListener(e->{
			rootView.forward(new StorehouseView("管理库房", rootView));
		});
	}

	@Override
	public String getSearchTags() {
		return "管理社区,管理机构,管理库房";
	}

	@Override
	public int getOrderID() {
		return 1;
	}
	
	@Override
	public String getTitle() {
		return "社区";
	}
	
	@Override
	public String getImageName() {
		return "community.png";
	}
	
	private RowItemWithTitle manageCommunity = new RowItemWithTitle("管理社区");
	private RowItemWithTitle manageCompany = new RowItemWithTitle("管理机构");
	private RowItemWithTitle manageStore = new RowItemWithTitle("管理库房");
	private AdminMainView rootView;
}
