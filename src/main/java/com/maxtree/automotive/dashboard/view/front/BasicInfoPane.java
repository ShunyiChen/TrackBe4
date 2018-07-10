package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.data.Area;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.trackbe4.external.tmri.InterF;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
			
			if (StringUtils.isEmpty(barCodeField.getValue())) {
				return;
			}
			
			// TODO
			// 调用外部Web service，返回车辆基本信息，然后再通过车辆基本信息查询Transaction，查询条件(状态：待补充，VIN和号码号牌，号牌种类全匹配)
//			Transaction externalTrans = new EI().getInfoByBarcode(barCodeField.getValue());
			
			InterF interF = new InterF();
			Transaction externalTrans = new Transaction();
			externalTrans.setPlateNumber("");
			externalTrans.setVin("");
			externalTrans.setPlateType("");
			
//			Transaction trans = ui.transactionService.find(externalTrans.getPlateType(), externalTrans.getVin(), externalTrans.getPlateNumber());
//			if (trans != null) {
//				setFieldValues(trans);
//				// 更新条形码
//				trans.setBarcode(barCodeField.getValue());
//				trans.setStatus(Status.S4.name);
//				trans.setDateModified(new Date());
//				
//				if (trans.getCode() == null) {
//		        	Portfolio portfolio = ui.storehouseService.findAvailablePortfolio();
//		        	if (portfolio.getCode() == null) {
//		        		Notifications.warning("没有对应的上架号。");
//		        		return;
//		        	}
//		        	trans.setCode(portfolio.getCode());// 上架号
//		        	
//		        	portfolio.setVin(trans.getVin());
//		        	ui.storehouseService.updatePortfolio(portfolio);
//		        }
//			
//				Callback callback = new Callback() {
//					@Override
//					public void onSuccessful() {
//						ui.transactionService.update(trans);
//						view.cleanStage();
//						
//						// 提示信息
//						Notification success = new Notification("业务流水号保存成功！");
//						success.setDelayMsec(2000);
//						success.setStyleName("bar success small");
//						success.setPosition(Position.BOTTOM_CENTER);
//						success.show(Page.getCurrent());
//					}
//				};
//				MessageBox.showMessage("提示", "是否补充条形码:"+barCodeField.getValue()+"？", MessageBox.INFO, callback, "保存");
//			}
		});
		
		
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
	
	public boolean checkEmptyValues() {
//		if (StringUtils.isEmpty(barCodeField.getValue())) {
//			barCodeField.setComponentError(new ErrorMessage() {
//				@Override
//				public ErrorLevel getErrorLevel() {
//					return ErrorLevel.ERROR;
//				}
//
//				@Override
//				public String getFormattedHtmlMessage() {
//					return "条形码不能为空。";
//				}
//			});
//			return false;
//		} else 
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
		
//		if (barCodeField.getErrorMessage() != null) {
//			barCodeField.setComponentError(barCodeField.getErrorMessage());
//			return false;
//		} 
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
		Area area = Yaml.readArea();
		
		barCodeField.clear();
		plateTypeField.clear();
		plateNumberField.clear();
		vinField.clear();
		
		
		plateNumberField.setValue(area.getLicenseplate());
		vinField.setValue("LGB12YEA9DY001226");
	}
	
	/**
	 * 
	 * @param transaction
	 */
	public void setFieldValues(Transaction transaction) {
		barCodeField.setValue(transaction.getBarcode());
		plateTypeField.setValue(transaction.getPlateType());
		plateNumberField.setValue(transaction.getPlateNumber());
		vinField.setValue(transaction.getVin());
	}

	/**
	 * 
	 * @param transaction
	 */
	public void assignValues(Transaction transaction) {
		transaction.setBarcode(barCodeField.getValue()==null?"":barCodeField.getValue());
		transaction.setPlateType(plateTypeField.getValue());
		transaction.setPlateNumber(plateNumberField.getValue());
		transaction.setVin(vinField.getValue());
	}
	
	public String getVIN() {
		return vinField.getValue();
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField barCodeField = new TextField("条形码:"); 			// 条形码文本框
	private ComboBox<String> plateTypeField = new ComboBox<>("号牌种类:");// 号牌种类文本框
	private TextField plateNumberField = new TextField("号码号牌:"); 		// 号码号牌文本框
	private TextField vinField = new TextField("车辆识别代号:"); 			// 车辆识别码文本框
	private String fieldHeight = "27px";
}
