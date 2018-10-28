package com.maxtree.automotive.dashboard.view.front;

import com.maxtree.automotive.dashboard.component.Notifications;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

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
		this.setWidth("331px");
		this.setHeight("330px");
		this.setResizable(false);
//		Label l1 = new Label("流水号:");
//		Label l2 = new Label("号牌种类:");
//		Label l3 = new Label("号牌号码:");
//		Label l4 = new Label("车辆识别代号:");
//		HorizontalLayout info = new HorizontalLayout();
		FormLayout form = new FormLayout();
		form.setSizeFull();
		form.addComponents(barcode,plateType,plateNumber,plateVIN);
		
		barcode.setHeight("27px");
		plateType.setHeight("27px");
		plateNumber.setHeight("27px");
		plateVIN.setHeight("27px");
		 
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.setSpacing(false);
		buttons.setMargin(false);
		buttons.addComponent(btnFill);
		buttons.setComponentAlignment(btnFill, Alignment.MIDDLE_RIGHT);
		
		main.setSizeFull();
//		main.setSpacing(false);
//		main.setMargin(false);
		main.addComponents(form, buttons);
		main.setComponentAlignment(form, Alignment.TOP_CENTER);
		main.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		
		this.setContent(main);
		
		btnFill.addClickListener(e->{
			Notifications.warning("找不到接口");
		});
	}
	
	
	public static void open() {
		FillBarcodeWindow fbw = new FillBarcodeWindow();
		UI.getCurrent().addWindow(fbw);
		fbw.center();
	}
	
	private Button btnFill = new Button("确定");
	private TextField barcode = new TextField("业务流水号:");
	private TextField plateType = new TextField("号牌种类:");
	private TextField plateNumber = new TextField("号码号牌:");
	private TextField plateVIN = new TextField("车辆识别代号:");
	private VerticalLayout main = new VerticalLayout();
}
