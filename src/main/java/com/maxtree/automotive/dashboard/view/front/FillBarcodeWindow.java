package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification.Type;
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
		this.setHeight("300px");
		this.setResizable(false);
		List<String> data = ui.dataItemService.findNamesByType(1);
		plateTypeField.setItems(data);
		plateTypeField.setEmptySelectionAllowed(false);
		
		//4个文本框都是手动录入
		FormLayout form = new FormLayout();
		form.setSizeFull();
		form.setMargin(new MarginInfo(false,false,false,true));
		form.addComponents(barcode,plateTypeField,plateNumber,plateVIN);
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
			close();
		});
		
		btnFill.addClickListener(e->{
//			Notifications.warning("找不到接口");
			//标识是否存在需要补充的流水号
			boolean flag = false;
			//通过车辆识别代号找到车最后一笔业务，补识别代号
			List<Transaction> lst = ui.transactionService.findForList(plateVIN.getValue(), 0);
			
			for(Transaction trans : lst) {
				if(StringUtils.isEmpty(trans.getBarcode())) {
					flag = true;
					modified = trans;
					break;
				}
			}
			if(!flag) {
				Notifications.warning("没有要补充的记录。");
				return;
			}
			else {
				//最后一笔业务，如何要更改号牌的，则把所有这些vin记录的车号牌更改了。
				if(check()) {
					
					Callback onOK = new Callback() {
						@Override
						public void onSuccessful() {
							//补充业务流水号
							modified.setBarcode(barcode.getValue());
							ui.transactionService.update(modified);
							
							//判断当前业务是否需要更改号牌
							Business bus = ui.businessService.findByCode(modified.getBusinessCode());
							if(bus.getUpdatePlateNumber()) {
								if(!StringUtils.isEmpty(modified.getVin())) {
									ui.transactionService.batchUpdate(modified.getVin(), plateNumber.getValue());
								}
								else {
									Notifications.warning("补充号牌失败，VIN为空。", Type.ERROR_MESSAGE);
									return;
								}
							}
						}
					};
					Callback onCancel = new Callback() {
						@Override
						public void onSuccessful() {
							close();
						}
					};
					
					MessageBox.showMessage("确认", "请确定是否补充流水号:"+barcode.getValue(), MessageBox.WARNING, onOK, onCancel, "是", "否");
					
				}
			}
			
		});
	
		barcode.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		plateTypeField.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		plateNumber.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		plateVIN.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		
		this.addCloseListener(e->{
			ui.setPollInterval(config.getInterval());
		});
	}
	
	private boolean check() {
		if(StringUtils.isEmpty(barcode.getValue())) {
			Notifications.warning("流水号不能空。");
			return false;
		}
		else if(StringUtils.isEmpty(plateTypeField.getValue())) {
			Notifications.warning("号牌种类不能空。");
			return false;
		}
		else if(StringUtils.isEmpty(plateNumber.getValue())) {
			Notifications.warning("号牌号码不能空。");
			return false;
		}
		else if(StringUtils.isEmpty(plateNumber.getValue())) {
			Notifications.warning("车辆识别代号不能空。");
			return false;
		}
		else if(plateNumber.getValue().length() != 5
				&& plateNumber.getValue().length() != 6) {
			Notifications.warning("号牌号码输入位数错误。");
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
}
