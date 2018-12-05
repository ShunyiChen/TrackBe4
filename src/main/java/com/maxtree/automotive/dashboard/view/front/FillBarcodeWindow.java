package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Car;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.view.finalcheck.PopupCaptureWindow;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class FillBarcodeWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public FillBarcodeWindow() {
		initComponents();
	}
	
	
	private void initComponents() {
		this.setCaption("补充业务流水号");
		this.setWidth("401px");
//		this.setHeight("300px");
		this.setClosable(false);
		this.setResizable(false);
		List<String> data = ui.dataItemService.findNamesByType(1);
		plateTypeField.setItems(data);
		plateTypeField.setEmptySelectionAllowed(false);
		
		//车辆照片
		HorizontalLayout picture = new HorizontalLayout();
		picture.setCaption("申请表:");
		picture.setWidthUndefined();
		picture.setHeight("28px");
		Button captureButton = new Button();
		captureButton.setIcon(VaadinIcons.CAMERA);
		captureButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		captureButton.addClickListener(e->{
			capture();
		});
		link.addStyleName(ValoTheme.BUTTON_LINK);
		picture.addComponents(captureButton,link);
		picture.setComponentAlignment(captureButton, Alignment.MIDDLE_LEFT);
		picture.setComponentAlignment(link, Alignment.MIDDLE_LEFT);
		
		//4个文本框都是手动录入
		FormLayout form = new FormLayout();
		form.setSizeFull();
		form.setMargin(new MarginInfo(false,false,false,true));
		form.addComponents(barcode,plateTypeField,plateNumber,plateVIN,picture);
		barcode.setHeight("28px");
		plateTypeField.setHeight("28px");
		plateNumber.setHeight("28px");
		plateVIN.setHeight("28px");
		
		barcode.setWidth("228px");
		plateTypeField.setWidth("228px");
		plateNumber.setWidth("228px");
		plateVIN.setWidth("228px");
		
		plateNumber.setPlaceholder("请输入车牌号的后5位或6位");
		btnFill.addStyleName(ValoTheme.BUTTON_PRIMARY);
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidthUndefined();
		buttons.setHeight("40px");
		buttons.addComponents(btnCancel,btnFill,Box.createHorizontalBox(10));
		buttons.setComponentAlignment(btnCancel, Alignment.BOTTOM_RIGHT);
		buttons.setComponentAlignment(btnFill, Alignment.BOTTOM_RIGHT);
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		main.addComponents(form,Box.createVerticalBox(20), buttons);
		main.setComponentAlignment(form, Alignment.TOP_CENTER);
		main.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		this.setContent(main);
		
		btnCancel.addClickListener(e->{
			cancel();
		});
		
		btnFill.addClickListener(e->{
			
			fill();
			
//			Notifications.warning("找不到接口");
			//标识是否存在需要补充的流水号
//			boolean flag = false;
//			//通过车辆识别代号找到车最后一笔业务，补识别代号
//			List<Transaction> lst = ui.transactionService.findForList(plateVIN.getValue(),null,0);
//			
//			for(Transaction trans : lst) {
//				if(StringUtils.isEmpty(trans.getBarcode())) {
//					flag = true;
//					modified = trans;
//					break;
//				}
//			}
//			if(!flag) {
//				Notifications.warning("没有要补充的记录。");
//				return;
//			}
//			else {
//				//最后一笔业务，如何要更改号牌的，则把所有这些vin记录的车号牌更改了。
//				if(check()) {
//					
//					Callback onOK = new Callback() {
//						@Override
//						public void onSuccessful() {
//							//补充业务流水号
//							modified.setBarcode(barcode.getValue());
//							ui.transactionService.update(modified);
//							
//							//判断当前业务是否需要更改号牌
//							Business bus = ui.businessService.findByCode(modified.getBusinessCode());
//							if(bus.getUpdatePlateNumber()) {
//								if(!StringUtils.isEmpty(modified.getVin())) {
//									ui.transactionService.batchUpdate(modified.getVin(), plateNumber.getValue());
//								}
//								else {
//									Notifications.warning("补充号牌失败，VIN为空。", Type.ERROR_MESSAGE);
//									return;
//								}
//							}
//						}
//					};
//					Callback onCancel = new Callback() {
//						@Override
//						public void onSuccessful() {
//							close();
//						}
//					};
//					
//					MessageBox.showMessage("确认", "请确定是否补充流水号:"+barcode.getValue(), MessageBox.WARNING, onOK, onCancel, "是", "否");
//					
//				}
//			}
			
		});
	
//		barcode.addFocusListener(e->{
//			ui.setPollInterval(-1);
//		});
//		plateTypeField.addFocusListener(e->{
//			ui.setPollInterval(-1);
//		});
//		plateNumber.addFocusListener(e->{
//			ui.setPollInterval(-1);
//		});
//		plateVIN.addFocusListener(e->{
//			ui.setPollInterval(-1);
//		});
	}
	
	/**
	 * 
	 */
	private void capture() {
		if(!StringUtils.isEmpty(barcode.getValue())
				&& !StringUtils.isEmpty(plateVIN.getValue())) {
			trans = ui.transactionService.find(plateTypeField.getValue(), plateNumber.getValue(),plateVIN.getValue());
			if(trans == null) {
				Notifications.warning("主记录不存在。");
				return;
			}
			dd = ui.dataItemService.findDDByName("申请表");
			if(dd == null) {
				Notifications.warning("申请表材料不存在，请联系管理员添加。");
				return;
			}
			
			Car car = ui.carService.findByVIN(plateVIN.getValue());
			car.setBarcode(barcode.getValue());
			car.setPlateNumber(plateNumber.getValue());
			car.setPlateType(plateTypeField.getValue());
			int opt = ui.carService.update(car);
			if(opt == 0) {
				Notifications.warning("车辆信息变更失败，原因：找不到VIN。");
				return;
			}
			
			Callback2 callback = new Callback2() {
				@Override
				public void onSuccessful(Object... objects) {
					link.setCaption("申请表原文");
				}
			};
			
			Transaction copy = new Transaction();
			copy.setTransactionUniqueId(trans.getTransactionUniqueId());
			copy.setBatch(trans.getBatch());
			copy.setCode(dd.getCode());
			copy.setSiteCode(trans.getSiteCode());
			copy.setUuid(trans.getUuid());
			copy.setVin(trans.getVin());
			
			PopupCaptureWindow.open(copy,dd,callback);
		}
		else {
			Notifications.warning("输入不能有空。");
		}
	}
	
	/**
	 * 
	 */
	private void cancel() {
		if(!StringUtils.isEmpty(link.getCaption())) {
			Document removable = ui.documentService.find(dd.getCode(), trans.getUuid(), trans.getVin());
			if(removable.getDocumentUniqueId() > 0) {
				ui.documentService.deleteById(removable.getDocumentUniqueId(), trans.getVin());
				
				Site site = ui.siteService.findByCode(trans.getSiteCode());
				
				try {
					fileSystem.deleteFile(site, removable.getFileFullPath());
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		}
		close();
		
//		ui.setPollInterval(config.getInterval());
	}
	
	private void fill() {
		if(check()) {
			Business business = ui.businessService.findByCode(trans.getBusinessCode());
    		if(business.getCheckLevel().equals("一级审档")) {
    			trans.setStatus(ui.state().getName("B2"));
    		}
    		else if(business.getCheckLevel().equals("二级审档")) {
    			trans.setStatus(ui.state().getName("B2"));
    		}
			trans.setBarcode(barcode.getValue());
			ui.transactionService.update(trans);
			close();
		}
//		ui.setPollInterval(config.getInterval());
	}
	
	private boolean check() {
		if(trans == null 
				|| StringUtils.isEmpty(barcode.getValue())
				|| StringUtils.isEmpty(link.getCaption())) {
			Notifications.warning("输入信息有误，或者缺少原文上传。");
			return false;
		}
		return true;
	}
	
	public static void open() {
		FillBarcodeWindow fbw = new FillBarcodeWindow();
		UI.getCurrent().addWindow(fbw);
		fbw.center();
	}
	
	private Transaction modified = null;
	private SystemConfiguration config = Yaml.readSystemConfiguration();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button btnCancel = new Button("取消");
	private Button btnFill = new Button("补充");
	private TextField barcode = new TextField("业务流水号:");
	private ComboBox<String> plateTypeField = new ComboBox<>("号牌种类:");// 号牌种类文本框
	private TextField plateNumber = new TextField("号码号牌:");
	private TextField plateVIN = new TextField("车辆识别代号:");
	private VerticalLayout main = new VerticalLayout();
	private Button link = new Button();
	private Transaction trans;
	private DataDictionary dd;
	private TB4FileSystem fileSystem = new TB4FileSystem();
}
