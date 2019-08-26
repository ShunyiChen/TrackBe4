package com.maxtree.automotive.dashboard.view.admin;

/**
 * @author Chen
 */
public class DataDictionaryRow extends FlexTableRow {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private RowItemWithTitle manageBusinessType = new RowItemWithTitle("管理业务类型");
    private RowItemWithTitle manageMaterialName = new RowItemWithTitle("管理材料名称");
    private RowItemWithTitle managePlatenumType = new RowItemWithTitle("管理号牌种类");
    private AdminMainView rootView;

    /**
     * @param rootView
     */
    public DataDictionaryRow(AdminMainView rootView) {
        this.rootView = rootView;
        initComponents();
    }

    private void initComponents() {
        this.addComponents(manageBusinessType, manageMaterialName, managePlatenumType);
        manageBusinessType.addLayoutClickListener(e -> {
            rootView.forward(new BusinessView("管理业务类型", rootView));
        });
        manageMaterialName.addLayoutClickListener(e -> {
            rootView.forward(new MaterialView("管理材料名称", rootView));
        });
        managePlatenumType.addLayoutClickListener(e -> {
            rootView.forward(new NumberplateView("管理号牌种类", rootView));
        });
    }

    @Override
    public String getSearchTags() {
        return "管理业务类型,管理材料名称,管理号牌种类,管理地区," + getTitle();
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
}
