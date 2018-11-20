package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.view.InputViewIF;
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

/**
 * 
 * @author Chen
 *
 */
public class BasicInfoPane extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public BasicInfoPane(InputViewIF view) {
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
//		vinField.setReadOnly(true);
		
		barCodeField.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		plateTypeField.addFocusListener(e->{
			ui.setPollInterval(-1);	
		});
		plateNumberField.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		vinField.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		
		barCodeField.addBlurListener(e -> {
			callInterface();
			ui.setPollInterval(config.getInterval());
		});
		plateTypeField.addBlurListener(e -> {
			callInterface();
			ui.setPollInterval(config.getInterval());
		});
		plateNumberField.addBlurListener(e -> {
			callInterface();
			ui.setPollInterval(config.getInterval());
		});
		vinField.addBlurListener(e -> {
			callInterface();
			ui.setPollInterval(config.getInterval());
		});
		
		
		barCodeField.addValueChangeListener(e->{
			barCodeField.setValue(barCodeField.getValue().toUpperCase());
		});
		plateNumberField.addValueChangeListener(e->{
			plateNumberField.setValue(plateNumberField.getValue().toUpperCase());
		});
		vinField.addValueChangeListener(e->{
			vinField.setValue(vinField.getValue().toUpperCase());
		});
	}
	
	/**
	 * 调用外部接口
	 */
	public void callInterface() {
//		if(!StringUtils.isEmpty(plateTypeField.getValue())
//				&& !StringUtils.isEmpty(plateNumberField.getValue())
//				&& !StringUtils.isEmpty(vinField.getValue())) {
//			view.businessTypePane().setSelectorEnabled(true);
//		}
//		else {
//			view.businessTypePane().setSelectorEnabled(false);
//		}
		// 通过失去焦点获得，如果有条形码，可以通过条形码查询vin.如果没有条形码，可以通过车牌号和号牌种类查询出vin.
//		if (!StringUtils.isEmpty(barCodeField.getValue())) {
////			ArrayList<HashMap<String, String>> lst = interF.getCarView(plateTypeField.getValue(), plateNumberField.getValue());
////			view.vin = lst.get(0).get("clsbdh");
//		} else {
////			ArrayList<HashMap<String, String>> lst = interF.getbusView(barCodeField.getValue(), plateTypeField.getValue(), plateNumberField.getValue());
////			view.vin = lst.get(0).get("clsbdh");
//		}
////		view.vin = "LGB12YEA9DY001226"; /// 这句话可以删除
//		// 辽BD01848
////		vinField.setValue(view.vin());
//		// 有效性验证
////		if(StringUtils.isEmpty(view.vin())) {
////			Notifications.warning("有效性验证失败，VIN不能为空。");
////			view.stoppedAtAnException(true);
////		}
		
//		ArrayList<HashMap<String, String>> lst = interF.getbusView(barCodeField.getValue(), plateTypeField.getValue(), plateNumberField.getValue());
//		String vin = lst.get(0).get("clsbdh");
//		String plateType = lst.get(0).get("hpzl");
//		String plateNum= lst.get(0).get("hphm");
//		
//		plateTypeField.setValue(plateType);
//		plateNumberField.setValue(plateNum);
//		
//		if(StringUtils.isEmpty(vin)) {
//			Notifications.warning("有效性验证失败，VIN不能为空。");
//			view.businessTypePane().setSelectorEnabled(false);
//		}
//		else {
//			vinField.setValue(vin);
//			view.businessTypePane().setSelectorEnabled(true);
//		}
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
		binder.forField(plateNumberField).withValidator(new StringLengthValidator( "号码号牌长度应为5~6个字符", 5,6))
		.bind(Transaction::getPlateNumber, Transaction::setPlateNumber);
		binder.forField(vinField).withValidator(new StringLengthValidator( "车辆识别代号不能空", 1, 17))
		.bind(Transaction::getVin, Transaction::setVin);
	}
	
	/**
	 * 有效性验证
	 * 
	 * @param checkBarcode
	 * @return
	 */
	public boolean emptyChecks(boolean checkBarcode) {
		if(checkBarcode) {
			if (StringUtils.isEmpty(barCodeField.getValue())) {
				
				barCodeField.setComponentError(new ErrorMessage() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override 
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.ERROR;
					}

					@Override
					public String getFormattedHtmlMessage() {
						return "业务流水号不能为空。";
					}
				});
				return false;
			}
		}
		
		
		if (StringUtils.isEmpty(plateTypeField.getSelectedItem())) {
			
			plateTypeField.setComponentError(new ErrorMessage() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

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
	}
	
	/**
	 * 
	 * @param transaction
	 */
	public void populateFields(Transaction transaction) {
		barCodeField.setValue(transaction.getBarcode());
		plateTypeField.setValue(transaction.getPlateType());
		if(transaction.getPlateNumber().equals("")) {
			plateNumberField.setValue("");
		}
		else {
			String plateNum = transaction.getPlateNumber();
			plateNumberField.setValue(plateNum);
		}
		vinField.setValue(transaction.getVin());
	}

	/**
	 * 
	 * @param transaction
	 */
	public void populateTransaction(Transaction transaction) {
		transaction.setBarcode(barCodeField.getValue()==null?"":barCodeField.getValue());
		transaction.setPlateType(plateTypeField.getValue());
		transaction.setPlateNumber(plateNumberField.getValue());
		transaction.setVin(vinField.getValue());
	}
	
	public String getBarCode() {
		return barCodeField.getValue();
	}
	
	public String getPlateNumber() {
		return config.getLicenseplate()+" "+plateNumberField.getValue();
	}
	
	public String getPlateType() {
		return plateTypeField.getValue();
	}
 
	public String getVIN() {
		return vinField.getValue();
	}
	
	private SystemConfiguration config = Yaml.readSystemConfiguration();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField barCodeField = new TextField("条形码:"); 			// 条形码文本框
	private ComboBox<String> plateTypeField = new ComboBox<>("号牌种类:");// 号牌种类文本框
	private TextField plateNumberField = new TextField("号码号牌:"+config.getLicenseplate());// 号码号牌文本框
	private TextField vinField = new TextField("车辆识别代号:"); 			// 车辆识别码文本框
	private String fieldHeight = "27px";
//	private InterF interF = new InterF();
	private InputViewIF view;
}
