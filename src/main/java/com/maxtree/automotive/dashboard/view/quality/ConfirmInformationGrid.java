package com.maxtree.automotive.dashboard.view.quality;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class ConfirmInformationGrid extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ConfirmInformationGrid(Transaction transaction) {
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		hlayout.setHeightUndefined();
		hlayout.setWidthUndefined();
		
		hlayout.addComponents(barCodeText, 
				Box.createHorizontalBox(3),
				barCodeField, 
				Box.createHorizontalBox(5),
				plateTypeText, 
				Box.createHorizontalBox(3),
				plateTypeField, 
				Box.createHorizontalBox(5),
				plateNumberText, 
				Box.createHorizontalBox(3),
				plateNumberField,
				Box.createHorizontalBox(5),
				vinText, 
				Box.createHorizontalBox(3), 
				vinField,
				Box.createHorizontalBox(5), 
				businessTypeText,
				Box.createHorizontalBox(3), 
				businessTypeField);
		hlayout.setComponentAlignment(barCodeText, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(barCodeField, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(plateTypeText, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(plateTypeField, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(plateNumberText, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(plateNumberField, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(vinText, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(vinField, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(businessTypeText, Alignment.MIDDLE_LEFT);
		hlayout.setComponentAlignment(businessTypeField, Alignment.MIDDLE_LEFT);
		this.addComponent(hlayout);
		
		barCodeField.setWidth("150px");
		plateTypeField.setWidth("150px");
		plateNumberField.setWidth("150px");
		vinField.setWidth("150px");
		businessTypeField.setWidth("150px");
		
		barCodeField.setHeight("28px");
		plateTypeField.setHeight("28px");
		plateNumberField.setHeight("28px");
		vinField.setHeight("28px");
		businessTypeField.setHeight("28px");
		
		if(transaction != null)
			setValues(transaction);
	}
	
	/**
	 * 
	 * @param transaction
	 */
	public void setValues(Transaction transaction) {
		barCodeField.setValue(transaction.getBarcode());
		plateTypeField.setValue(transaction.getPlateType());
		plateNumberField.setValue(transaction.getPlateNumber());
		vinField.setValue(transaction.getVin());
//		Business business = ui.businessService.findByCode(transaction.getBusinessCode());
//		businessTypeField.setValue(business.toString());
	}
	
	public void setReadOnly() {
		barCodeField.setReadOnly(true);
		plateTypeField.setReadOnly(true);
		plateNumberField.setReadOnly(true);
		vinField.setReadOnly(true);
		businessTypeField.setReadOnly(true);
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	private String htmlText(String text) {
		return "<span style='cursor:pointer;font-size:14px;font-weight: bold;color: #000000;'>"+text+"</span>";
	}

	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Label barCodeText = new Label(htmlText("条形码:"), ContentMode.HTML);
	private Label plateTypeText = new Label(htmlText("号牌种类:"), ContentMode.HTML);
	private Label plateNumberText = new Label(htmlText("号码号牌:"), ContentMode.HTML);
	private Label vinText = new Label(htmlText("车辆识别码:"), ContentMode.HTML);
	private Label businessTypeText = new Label(htmlText("业务类型:"), ContentMode.HTML);
	private TextField barCodeField = new TextField(); 	// 条形码文本
	private TextField plateTypeField = new TextField();	// 号牌种类文本框
	private TextField plateNumberField = new TextField(); // 号码号牌文本框
	private TextField vinField = new TextField(); 			// 车辆识别码文本框
	private TextField businessTypeField = new TextField();  // 业务类型文本框
}
