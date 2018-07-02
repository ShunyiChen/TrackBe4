package com.maxtree.automotive.dashboard.view.admin.storehouse;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.CodeGenerator;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.domain.DenseFrame;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Storehouse;
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

public class EditDenseFrameWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditDenseFrameWindow() {
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("添加新密集架");
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
		form.addComponents(storehouseCodeField, codeField, rowField, columnField);
		
		storehouseCodeField.setReadOnly(true);
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(false);
		buttons.setMargin(false);
		buttons.setWidthUndefined();
		buttons.addComponents(btnOK, Box.createHorizontalBox(5), btnCancel);
		buttons.setComponentAlignment(btnOK, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		main.addComponents(Box.createVerticalBox(5), form, Box.createVerticalBox(10), buttons, Box.createVerticalBox(5));
		main.setComponentAlignment(form, Alignment.TOP_CENTER);
		main.setComponentAlignment(buttons, Alignment.TOP_CENTER);
		
		setContent(main);
		
        btnCancel.addClickListener(e -> {
        	close();
        });
        
        // Bind nameField to the Person.name property
 		// by specifying its getter and setter
 		bindFields();
 		
 		// Bind an actual concrete Person instance.
 		// After this, whenever the user changes the value
 		// of nameField, p.setName is automatically called.
 		binder.setBean(denseFrame);
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.forField(codeField).withValidator(new StringLengthValidator(
		        "密集架编号长度为7个字符",
		        7, 7)) .bind(DenseFrame::getCode, DenseFrame::setCode);
		
		binder.forField(rowField)
		  .withValidator(new EmptyValidator("输入行数不能为空"))
		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
		  .bind(DenseFrame::getRowCount, DenseFrame::setRowCount);
		
		binder.forField(columnField)
		  .withValidator(new EmptyValidator("输入列数不能为空"))
		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
		  .bind(DenseFrame::getColumnCount, DenseFrame::setColumnCount);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(denseFrame.getCode())) {
			Notification notification = new Notification("提示：", "密集架编号不能为空", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			
			return false;
		}
		if (codeField.getErrorMessage() != null) {
			codeField.setComponentError(codeField.getErrorMessage());
			return false;
		}
		
		try {
			int maxRow = Integer.parseInt(rowField.getValue());
			if (maxRow < 1 || maxRow > 20) {
				Notification notification = new Notification("提示：", "输入行数范围应该在1-20", Type.WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				notification.show(Page.getCurrent());
				return false;
			}
		} catch(NumberFormatException e) {
			Notification notification = new Notification("提示：", "输入行数错误"+e.getMessage(), Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		
		try {
			int maxCol = Integer.parseInt(columnField.getValue());
			if (maxCol < 1 || maxCol > 20) {
				Notification notification = new Notification("提示：", "输入列数范围应该在1-20", Type.WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				notification.show(Page.getCurrent());
				return false;
			}
		} catch(NumberFormatException e) {
			Notification notification = new Notification("提示：", "输入列数错误"+e.getMessage(), Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param storehouseCode
	 * @param callback
	 */
	public static void open(Storehouse storehouse, Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDenseFrameWindow w = new EditDenseFrameWindow();
        w.denseFrame.setStorehouseUniqueId(storehouse.getStorehouseUniqueId());
        w.storehouseCodeField.setValue(storehouse.getCode());
        w.codeField.setValue(storehouse.getCode()+"-"+new CodeGenerator().generateDenseframeCode(storehouse.getStorehouseUniqueId()));
        w.btnOK.addClickListener(e -> {
			if (w.checkEmptyValues()) {
				int denseFrameUniqueId = ui.storehouseService.insertDenseFrame(w.denseFrame);
				w.denseFrame.setDenseFrameUniqueId(denseFrameUniqueId);
				
    			w.close();
    			callback.onSuccessful(w.denseFrame);
			}
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param denseFrame
	 * @param callback
	 */
	public static void edit(DenseFrame denseFrame, Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDenseFrameWindow w = new EditDenseFrameWindow();
        
        String storehouseCode = denseFrame.getCode().substring(0, 3);
        
        w.denseFrame.setStorehouseUniqueId(denseFrame.getStorehouseUniqueId());
        w.storehouseCodeField.setValue(storehouseCode);
        w.codeField.setValue(denseFrame.getCode());
        w.rowField.setValue(denseFrame.getRowCount()+"");
        w.columnField.setValue(denseFrame.getColumnCount()+"");
        w.btnOK.setCaption("更改");
        w.btnOK.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		ui.storehouseService.updateDenseFrame(w.denseFrame);
				
    			w.close();
    			callback.onSuccessful(w.denseFrame);
			}
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField storehouseCodeField = new TextField("库房编号:");
	private TextField codeField = new TextField("密集架编号:");
	private TextField rowField = new TextField("最大行数:");
	private TextField columnField = new TextField("最大列数:");
	private Button btnOK = new Button("添加");
	private Button btnCancel = new Button("取消");
	private Binder<DenseFrame> binder = new Binder<>();
	private DenseFrame denseFrame = new DenseFrame();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	
}
