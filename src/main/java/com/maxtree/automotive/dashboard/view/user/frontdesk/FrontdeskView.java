package com.maxtree.automotive.dashboard.view.user.frontdesk;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.jpa.entity.Master;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.util.List;
import java.util.regex.Pattern;

public class FrontdeskView extends VerticalLayout implements View {

//    private DashboardUI ui = (DashboardUI) UI.getCurrent();
//    private boolean flag;
//    private Label none = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>没有可显示的内容</span>", ContentMode.HTML);
//    private Button btnNew = new Button("新建",VaadinIcons.NEWSPAPER);
//    private Button btnDiscard = new Button("作废",VaadinIcons.DEL_A);
//    private Button btnSave = new Button("保存",VaadinIcons.STAR);
//    private Button btnCommit = new Button("提交",VaadinIcons.CLOUD_UPLOAD);
//    private Button btnPrint = new Button("打印", VaadinIcons.PRINT);
//    private Button btnNotification = new Button("通知", VaadinIcons.BELL_O);
//
//    private HorizontalLayout mainLayout = new HorizontalLayout();
//    private ThumbnailGrid grid;
//    private CameraArea cameraArea;
//
//    public FrontdeskView() {
//        this.addStyleName("FrontdeskView");
//        DashboardEventBus.register(this);
//
//        HorizontalLayout toolbar = new HorizontalLayout();
//        Image menu = new Image(null, new ThemeResource("user/frontdesk/images/menu.png"));
//        menu.addStyleName("my-img-button");
//        menu.addClickListener(e ->{
//            flag = !flag;
//            ui.mainView.hideMenu(flag);
//        });
//        toolbar.setWidth("100%");
//        toolbar.setHeight("56px");
//        toolbar.addStyleName("FrontdeskView_toolbar");
//        toolbar.addComponent(menu);
//        toolbar.setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
//        HorizontalLayout subtoolbar = new HorizontalLayout();
//        toolbar.addComponent(subtoolbar);
//        toolbar.setComponentAlignment(subtoolbar, Alignment.MIDDLE_RIGHT);
//        subtoolbar.addComponents(btnNew,btnDiscard,btnSave,btnCommit,btnPrint,btnNotification);
//        subtoolbar.setSizeUndefined();
//
//        subtoolbar.setComponentAlignment(btnNew, Alignment.MIDDLE_RIGHT);
//        subtoolbar.setComponentAlignment(btnDiscard, Alignment.MIDDLE_RIGHT);
//        subtoolbar.setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
//        subtoolbar.setComponentAlignment(btnCommit, Alignment.MIDDLE_RIGHT);
//        subtoolbar.setComponentAlignment(btnPrint, Alignment.MIDDLE_RIGHT);
//        subtoolbar.setComponentAlignment(btnNotification, Alignment.MIDDLE_RIGHT);
//
//        addComponent(toolbar);
//        setComponentAlignment(toolbar, Alignment.TOP_LEFT);
//        setExpandRatio(toolbar, 0);
//
//        mainLayout.setSizeFull();
//        addComponent(mainLayout);
//        setExpandRatio(mainLayout, 1);
//
//        mainLayout.addComponent(none);
//        mainLayout.setComponentAlignment(none, Alignment.MIDDLE_CENTER);
//        registerEvents();
//    }
//
//    private void registerEvents() {
//        btnNew.addClickListener(e -> {
//            newOne();
//        });
//        btnSave.addClickListener(e -> {
//
//        });
//        btnDiscard.addClickListener(e -> {
//            discard();
//        });
//        btnCommit.addClickListener(e -> {
//
//        });
//        btnPrint.addClickListener(e -> {
//
//        });
//        btnNotification.addClickListener(e -> {
//
//        });
//    }
//
//    public void newOne() {
//        mainLayout.removeAllComponents();
//
//        // form layout
//        FormLayout formLayout = new FormLayout();
//        formLayout.addStyleName("my-formLayout");
//        formLayout.setMargin(false);
//        formLayout.setSpacing(false);
//        formLayout.setWidth("225px");
//        formLayout.setHeight("100%");
//        mainLayout.addComponent(formLayout);
//        int compHeight = 28;
//
//        ComboBox<Business> businessTypeValues = new ComboBox<>();
//        businessTypeValues.setTabIndex(1);
//        businessTypeValues.addValueChangeListener(e ->{
//            changingBusinessType(e.getValue());
//        });
//        TextField barcodeValue = new TextField();
//        barcodeValue.setTabIndex(2);
//        ComboBox<DataDictionary> plateTypeValue = new ComboBox<>();
//        plateTypeValue.setTabIndex(3);
//        TextField plateNumberValue = new TextField();
//        plateNumberValue.setTabIndex(4);
//        plateNumberValue.addBlurListener(e -> {
//            String pattern = ".*辽[Bb].*";
//            boolean matches = Pattern.matches(pattern, plateNumberValue.getValue());
//            if(!matches) {
//                plateNumberValue.setValue("辽B"+plateNumberValue.getValue());
//            }
//        });
//        TextField vinValue = new TextField();
//        vinValue.setTabIndex(5);
//        Label businessTypeLabel = new Label();
//        businessTypeLabel.setValue("业务类型:");
//        businessTypeValues.setHeight(compHeight+"px");
//        formLayout.addComponents(businessTypeLabel, businessTypeValues);
//        Label barcodeLabel = new Label();
//        barcodeLabel.setValue("业务流水号:");
//        Label plateTypeLabel = new Label();
//        plateTypeLabel.setValue("号牌种类:");
//        Label plateNumberLabel = new Label();
//        plateNumberLabel.setValue("号码号牌:");
//        Label vinLabel = new Label();
//        vinLabel.setValue("车辆识别代号:");
//        formLayout.addComponents(barcodeLabel,barcodeValue,plateTypeLabel,plateTypeValue,plateNumberLabel,plateNumberValue,vinLabel,vinValue);
//        barcodeValue.setHeight(compHeight+"px");
//        plateTypeValue.setHeight(compHeight+"px");
//        plateNumberValue.setHeight(compHeight+"px");
//        vinValue.setHeight(compHeight+"px");
//
//        // thumbnail
//        grid = new ThumbnailGrid();
//        mainLayout.addComponent(grid);
//        mainLayout.setExpandRatio(grid,2);
//
//        cameraArea = new CameraArea();
//        mainLayout.addComponent(cameraArea);
//        mainLayout.setExpandRatio(cameraArea,2);
//        //Turn camera on
//        cameraArea.turnCameraOn();
//
//        UI.getCurrent().access(new Runnable() {
//            @Override
//            public void run() {
//                List<Business> list = ui.userService.findAssignedBusinesses(ui.getLoggedInUser().getUserUniqueId());
//                businessTypeValues.setDataProvider(new ListDataProvider<>(list));
//
//                List<DataDictionary> list2 = ui.dataItemService.findAllByType(1);
//                plateTypeValue.setDataProvider(new ListDataProvider<>(list2));
//
//
//            }
//        });
//    }
//
//    private void changingBusinessType(Business b) {
//        grid.removeAllRows();
//        if(b != null) {
//            List<DataDictionary> list = ui.businessService.getRequiredDataDictionaries(b.getCode());
//            int i = 0;
//            int tab = 6;
//            for (DataDictionary dd : list) {
//                i++;
//                ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName());
//                row.setTabIndex(tab);
//                tab++;
//                row.setDataDictionary(dd);
////                row.getContent().addListener(new Focus)/
//
//
//                grid.addRow(row);
//                // 选中第一个
////                if (i == 1) {
////                    row.selected();
////                UploadInDTO inDto = new UploadInDTO(view.loggedInUser().getUserUniqueId(), view.vin(), view.batch()+"", view.editableSite().getSiteUniqueId(),view.uuid(),dd.getCode());
////                UploadFileServlet.IN_DTOs.put(view.loggedInUser().getUserUniqueId(), inDto);
////                }
//            }
//            List<DataDictionary> list2 = ui.businessService.getOptionalDataDictionaries(b.getCode());
//            for (DataDictionary dd : list2) {
//                i++;
//                ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName()+" (可选项)", false);
//                row.setTabIndex(tab);
//                tab++;
//                row.setDataDictionary(dd);
//                grid.addRow(row);
//                // 选中第一个
////                if (i == 1) {
////                    row.selected();
////                UploadInDTO inDto = new UploadInDTO(view.loggedInUser().getUserUniqueId(), view.vin(), view.batch()+"", view.editableSite().getSiteUniqueId(),view.uuid(),dd.getCode());
////                UploadFileServlet.IN_DTOs.put(view.loggedInUser().getUserUniqueId(), inDto);
////                }
//            }
//            grid.focus();
//        }
//    }
//
//    public void edit(Master master) {
//
//    }
//
//    public void save() {
//
//    }
//
//    public void discard() {
//        mainLayout.removeAllComponents();
//        mainLayout.addComponent(none);
//        mainLayout.setComponentAlignment(none, Alignment.MIDDLE_CENTER);
//
//        cameraArea.turnCameraOff();
//    }
//
//    public void commit() {
//
//    }
//
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//
//    }
//
//    @Override
//    public void beforeLeave(ViewBeforeLeaveEvent event) {
//
//    }
}
