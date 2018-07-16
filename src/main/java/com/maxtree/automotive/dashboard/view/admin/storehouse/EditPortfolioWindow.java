package com.maxtree.automotive.dashboard.view.admin.storehouse;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.domain.DenseFrame;
import com.maxtree.automotive.dashboard.domain.Portfolio;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

public class EditPortfolioWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditPortfolioWindow() {
		
		initComponents();
		initActions();
	}
	
	private void initComponents() {
		this.setCaption("编辑档案袋");
		this.setWidth("400px");
		this.setHeightUndefined();
		this.setClosable(true);
		this.setResizable(true);
		this.setModal(true);
		
		VerticalLayout main = new VerticalLayout();
		main.setMargin(false);
		main.setSpacing(false);
		
		FormLayout form = new FormLayout();
		form.setMargin(false);
		form.setSpacing(false);
		form.setHeightUndefined();
		form.setWidth("100%");
		form.addComponents(codeField);
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(false);
		buttons.setMargin(false);
		buttons.setWidthUndefined();
		buttons.addComponents(btnAdd, Box.createHorizontalBox(5), btnCancel);
		buttons.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		main.addComponents(Box.createVerticalBox(5), form, Box.createVerticalBox(10), buttons, Box.createVerticalBox(5));
		main.setComponentAlignment(form, Alignment.TOP_CENTER);
		main.setComponentAlignment(buttons, Alignment.TOP_CENTER);
		
		setContent(main);
		
        // Bind nameField to the Person.name property
 		// by specifying its getter and setter
 		bindFields();
 		
 		// Bind an actual concrete Person instance.
 		// After this, whenever the user changes the value
 		// of nameField, p.setName is automatically called.
 		binder.setBean(portfolio);
	}
	
	private void initActions() {
		btnCancel.addClickListener(e -> {
        	close();
        });
	}
	
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
//		binder.forField(codeField).withValidator(new StringLengthValidator(
//		        "档案袋编号长度为15个字符",
//		        15, 15)) .bind(Portfolio::getCode, Portfolio::setCode);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
//		if (StringUtils.isEmpty(portfolio.getCode())) {
//			Notification notification = new Notification("提示：", "档案袋编号不能为空", Type.WARNING_MESSAGE);
//			notification.setDelayMsec(2000);
//			notification.show(Page.getCurrent());
//			return false;
//		}
//		if (codeField.getErrorMessage() != null) {
//			codeField.setComponentError(codeField.getErrorMessage());
//			return false;
//		}
		return true;
	}
	
	public static void open(Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditPortfolioWindow w = new EditPortfolioWindow();
        
        
//        w.denseFrame.setStorehouseUniqueId(storehouse.getStorehouseUniqueId());
//        w.storehouseCodeField.setValue(storehouse.getCode());
//        w.codeField.setValue(storehouse.getCode()+"-"+new CodeGenerator().generateDenseframeCode(storehouse.getStorehouseUniqueId()));
        w.btnAdd.addClickListener(e -> {
			if (w.checkEmptyValues()) {
//				int denseFrameUniqueId = ui.storehouseService.insertDenseFrame(w.denseFrame);
//				w.denseFrame.setDenseFrameUniqueId(denseFrameUniqueId);
				
    			w.close();
    			callback.onSuccessful(w.portfolio);
			}
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditPortfolioWindow w = new EditPortfolioWindow();
        
        
//        w.denseFrame.setStorehouseUniqueId(storehouse.getStorehouseUniqueId());
//        w.storehouseCodeField.setValue(storehouse.getCode());
//        w.codeField.setValue(storehouse.getCode()+"-"+new CodeGenerator().generateDenseframeCode(storehouse.getStorehouseUniqueId()));
        w.btnAdd.addClickListener(e -> {
			if (w.checkEmptyValues()) {
//				int denseFrameUniqueId = ui.storehouseService.insertDenseFrame(w.denseFrame);
//				w.denseFrame.setDenseFrameUniqueId(denseFrameUniqueId);
				
    			w.close();
    			callback.onSuccessful(w.portfolio);
			}
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Binder<Portfolio> binder = new Binder<>();
	private Portfolio portfolio = new Portfolio();
	private Button btnAdd = new Button("添加");
	private Button btnCancel = new Button("取消");
	private TextField codeField = new TextField("档案袋编码");
}
