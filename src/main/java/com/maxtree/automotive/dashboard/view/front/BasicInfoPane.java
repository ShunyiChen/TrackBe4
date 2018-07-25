package com.maxtree.automotive.dashboard.view.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.trackbe4.external.tmri.InterF;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class BasicInfoPane extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public BasicInfoPane(FrontView view) {
		this.view = view;
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		this.setCaption("基础信息");
		this.addStyleName("picture-pane");
		this.setWidth("100%");
		this.setHeightUndefined();
		
		HorizontalLayout main = new HorizontalLayout();
		main.setSpacing(false);
		main.setMargin(new MarginInfo(false, false, true, true));
		main.setWidth("100%");
		main.setHeightUndefined();
		
		FormLayout form1 = new FormLayout();
		form1.setSizeFull();
		form1.setSpacing(false);
		form1.setMargin(false);
		form1.addComponent(barCodeField);
		
		FormLayout form2 = new FormLayout();
		form2.setSizeFull();
		form2.setSpacing(false);
		form2.setMargin(false);
		form2.addComponent(plateTypeField);
		
		FormLayout form3 = new FormLayout();
		form3.setSizeFull();
		form3.setSpacing(false);
		form3.setMargin(false);
		form3.addComponent(plateNumberField);
		
		FormLayout form4 = new FormLayout();
		form4.setSizeFull();
		form4.setSpacing(false);
		form4.setMargin(false);
		form4.addComponent(vinField);
		
		main.addComponents(form1,form2,form3,form4);
		setContent(main);
		
		List<String> data = ui.dataItemService.findNamesByType(1);
		plateTypeField.setItems(data);
		plateTypeField.setEmptySelectionAllowed(false);
 
		barCodeField.setWidth("100%");
		plateTypeField.setWidth("100%");
		plateNumberField.setWidth("100%");
		vinField.setWidth("100%");
		
		barCodeField.setHeight(fieldHeight);
		plateTypeField.setHeight(fieldHeight);
		plateNumberField.setHeight(fieldHeight);
		vinField.setHeight(fieldHeight);
		vinField.setReadOnly(true);
		
		barCodeField.addBlurListener(e -> {
			populateVIN();
		});
		plateTypeField.addBlurListener(e -> {
			populateVIN();
		});
		plateNumberField.addBlurListener(e -> {
			populateVIN();
		});
	}
	
	/**
	 * 为VIN赋值
	 */
	public void populateVIN() {
		// 通过失去焦点获得，如果有条形码，可以通过条形码查询vin.如果没有条形码，可以通过车牌号和号牌种类查询出vin.
		if (StringUtils.isEmpty(barCodeField.getValue())) {
//			ArrayList<HashMap<String, String>> lst = interF.getCarView(plateTypeField.getValue(), plateNumberField.getValue());
//			view.vin = lst.get(0).get("clsbdh");
		} else {
//			ArrayList<HashMap<String, String>> lst = interF.getbusView(barCodeField.getValue(), plateTypeField.getValue(), plateNumberField.getValue());
//			view.vin = lst.get(0).get("clsbdh");
		}
		view.vin = "LGB12YEA9DY001226"; /// 这句话可以删除
		vinField.setValue(view.vin);
		// 有效性验证
		if(StringUtils.isEmpty(view.vin)) {
			Notifications.warning("有效性验证失败。");
			view.stoppedAtAnException = true;
		}
	}
	
	
	
	/**
	 * 
	 */
	public void validatingFieldValues(Binder<Transaction> binder) {
		// Validating Field Values
//		binder.forField(barCodeField).withValidator(new StringLengthValidator( "条形码长度范围在2~20个字符", 2, 20))
//		.bind(Transaction::getBarcode, Transaction::setBarcode);
		binder.forField(plateTypeField).withValidator(new StringLengthValidator( "号牌种类长度范围在2~20个字符", 2, 20))
		.bind(Transaction::getPlateType, Transaction::setPlateType);
		binder.forField(plateNumberField).withValidator(new StringLengthValidator( "号码号牌长度应为7~8个字符", 7, 8))
		.bind(Transaction::getPlateNumber, Transaction::setPlateNumber);
//		binder.forField(vinField).withValidator(new StringLengthValidator( "车辆识别码长度是17个字符", 17, 17))
//		.bind(Transaction::getVin, Transaction::setVin);
	}
	
	/**
	 * 有效性验证
	 * @return
	 */
	public boolean emptyChecks() {
		if (StringUtils.isEmpty(plateTypeField.getSelectedItem())) {
			
			plateTypeField.setComponentError(new ErrorMessage() {
				@Override 
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "号牌种类不能为空。";
				}
			});
			return false;
		} else if (StringUtils.isEmpty(plateNumberField.getValue())) {
			plateNumberField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "号码号牌不能为空。";
				}
			});
			return false;
		} else if (StringUtils.isEmpty(vinField.getValue())) {
			vinField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "VIN不能为空。";
				}
			});
			return false;
		}
		
		else if (plateTypeField.getErrorMessage() != null) {
			plateTypeField.setComponentError(plateTypeField.getErrorMessage());
			return false;
		}
		else if (plateNumberField.getErrorMessage() != null) {
			plateNumberField.setComponentError(plateNumberField.getErrorMessage());
			return false;
		}
		else if (vinField.getErrorMessage() != null) {
			vinField.setComponentError(vinField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	public void reset() {
		barCodeField.clear();
		plateTypeField.clear();
		plateNumberField.clear();
		vinField.clear();
		
		Address addr = Yaml.readAddress();
		plateNumberField.setValue(addr.getLicenseplate());
	}
	
	/**
	 * 
	 * @param transaction
	 */
	public void populate2(Transaction transaction) {
		barCodeField.setValue(transaction.getBarcode());
		plateTypeField.setValue(transaction.getPlateType());
		plateNumberField.setValue(transaction.getPlateNumber());
		vinField.setValue(transaction.getVin());
	}

	/**
	 * 
	 * @param transaction
	 */
	public void populate(Transaction transaction) {
		transaction.setBarcode(barCodeField.getValue()==null?"":barCodeField.getValue());
		transaction.setPlateType(plateTypeField.getValue());
		transaction.setPlateNumber(plateNumberField.getValue());
		transaction.setVin(vinField.getValue());
	}
	
	public String getBarCode() {
		return barCodeField.getValue();
	}
	
	public String getPlateNumber() {
		return plateNumberField.getValue();
	}
	
	public String getPlateType() {
		return plateTypeField.getValue();
	}
 
	public String getVIN() {
		return vinField.getValue();
	}
	
//	/**
//	 * 
//	 * @param enabled
//	 */
//	public void setEnabled2(boolean enabled) {
//		barCodeField.setEnabled(enabled);
//		plateNumberField.setEnabled(enabled);
//		plateTypeField.setEnabled(enabled);
//		vinField.setEnabled(enabled);
//	}
//	
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField barCodeField = new TextField("条形码:"); 			// 条形码文本框
	private ComboBox<String> plateTypeField = new ComboBox<>("号牌种类:");// 号牌种类文本框
	private TextField plateNumberField = new TextField("号码号牌:"); 		// 号码号牌文本框
	private TextField vinField = new TextField("车辆识别代号:"); 			// 车辆识别码文本框
	private String fieldHeight = "27px";
	private InterF interF = new InterF();
	private FrontView view;
}
