package com.maxtree.automotive.dashboard.view.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.DenseFrame;
import com.maxtree.automotive.dashboard.domain.FileBox;
import com.maxtree.automotive.dashboard.domain.Portfolio;
import com.maxtree.automotive.dashboard.domain.Storehouse;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

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
		this.setWidth("310px");
		this.setHeightUndefined();
		this.setClosable(true);
		this.setResizable(true);
		this.setModal(true);
		VerticalLayout main = new VerticalLayout();
		main.setWidth("100%");
		main.setHeightUndefined();
		nameField.setWidth("200px");
		maxColField.setWidth("200px");
		maxRowField.setWidth("200px");
		SNField.setWidth("200px");
		nameField.setHeight("27px");
		maxColField.setHeight("27px");
		maxRowField.setHeight("27px");
		SNField.setHeight("27px");
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		form.addComponents(nameField,maxColField,maxRowField,SNField);
		SNField.setReadOnly(true);
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(false);
		buttons.setMargin(false);
		buttons.setWidthUndefined();
		buttons.setHeight("27px");
		buttons.addComponents(btnAdd, Box.createHorizontalBox(5), btnCancel);
		buttons.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		main.addComponents(form, buttons);
		main.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		main.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
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
		binder.forField(nameField).withValidator(new StringLengthValidator(
		        "密集架名称长度为1-20个字符",
		        1, 20)) .bind(DenseFrame::getName, DenseFrame::setName);
		
		binder.forField(maxColField)
		  .withValidator(new EmptyValidator("输入列数不能为空"))
		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
		  .bind(DenseFrame::getMaxCol, DenseFrame::setMaxCol);
		
		binder.forField(maxRowField)
		  .withValidator(new EmptyValidator("输入行数不能为空"))
		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
		  .bind(DenseFrame::getMaxRow, DenseFrame::setMaxRow);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(nameField.getValue())) {
			Notifications.warning("名称不能为空。");
			return false;
		}
		else if (StringUtils.isEmpty(maxColField.getValue())) {
			Notifications.warning("最大列数不能为空。");
			return false;
		}
		else if (StringUtils.isEmpty(maxRowField.getValue())) {
			Notifications.warning("最大行数不能为空。");
			return false;
		}
		else {
			
			try {
				int maxCol = Integer.parseInt(maxColField.getValue());
				int maxRow = Integer.parseInt(maxRowField.getValue());
				if (maxCol < 1 || maxCol > 20 || maxRow < 1 || maxRow > 20) {
					Notifications.warning("请输入一个数字，范围应该在1-20。");
					return false;
				}
			} catch(NumberFormatException e) {
				Notifications.warning("请输入一个数字，范围应该在1-20。");
				return false;
			} 
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
        int serialNumber = ui.storehouseService.findNextSerialnumberOfDenseFrame(storehouse.getSerialNumber());
        w.denseFrame.setStorehouseSN(storehouse.getSerialNumber());
        w.denseFrame.setSerialNumber(serialNumber);
        w.SNField.setValue(serialNumber+"");
        w.btnAdd.addClickListener(e -> {
			if (w.checkEmptyValues()) {
				w.denseFrame.setSerialNumber(serialNumber);
				int id = ui.storehouseService.insertDenseFrame(w.denseFrame);
				w.denseFrame.setDenseFrameUniqueId(id);
				
				int SN = 0;//单元格顺序号
				for (int i = 0; i < w.denseFrame.getMaxRow(); i++) {
					
					for (int j = 0; j < w.denseFrame.getMaxCol(); j++) {
						SN++;
						FileBox fileBox = new FileBox();
						fileBox.setCol(j);
						fileBox.setRow(i);
						fileBox.setSerialNumber(SN);
						fileBox.setDenseframeSN(w.denseFrame.getSerialNumber());
						
						int fileboxUniqueId = ui.storehouseService.insertFileBox(fileBox);
						fileBox.setFileboxUniqueId(fileboxUniqueId);
						
						
						List<Portfolio> list = new ArrayList<Portfolio>();
						for(int z=0; z < 100; z++) {
							StringBuilder codes = new StringBuilder();
							codes.append(formatter.format(w.denseFrame.getSerialNumber()));
							codes.append("-");
							codes.append(formatter.format(j));
							codes.append("-");
							codes.append(formatter.format(i));
							codes.append("-");
							codes.append(formatter.format(z));
							
							Portfolio p = new Portfolio();
							p.setCode(codes.toString());
							p.setFileBoxSN(SN);
							p.setVin(null);
						}
						ui.storehouseService.insertPortfolio(list);
						
//						FileBoxComponent child = new FileBoxComponent(fileBox);
//						grid.addComponent(child);
//						grid.setRowExpandRatio(i, 0.0f);
//						grid.setColumnExpandRatio(j, 0.0f);
					}
				}
				
				
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
	public static void edit(DenseFrame df, Callback2 callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditDenseFrameWindow w = new EditDenseFrameWindow();
        w.denseFrame.setDenseFrameUniqueId(df.getDenseFrameUniqueId());
        w.denseFrame.setName(df.getName());
        w.denseFrame.setMaxCol(df.getMaxCol());
        w.denseFrame.setMaxRow(df.getMaxRow());
        w.denseFrame.setSerialNumber(df.getSerialNumber());
        w.denseFrame.setStorehouseSN(df.getStorehouseSN());
        
        w.nameField.setValue(df.getName());
        w.maxColField.setValue(df.getMaxCol()+"");
        w.maxRowField.setValue(df.getMaxRow()+"");
        w.SNField.setValue(df.getSerialNumber()+"");
        
        w.btnAdd.setCaption("更改");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		ui.storehouseService.updateDenseFrame(w.denseFrame);
    			w.close();
    			callback.onSuccessful(w.denseFrame);
			}
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField nameField = new TextField("密集架名:");
	private DoubleField maxColField = new DoubleField("最大列数:");
	private DoubleField maxRowField = new DoubleField("最大行数:");
	private TextField SNField = new TextField("顺序号:");
	private Button btnAdd = new Button("添加");
	private Button btnCancel = new Button("取消");
	private Binder<DenseFrame> binder = new Binder<>();
	private DenseFrame denseFrame = new DenseFrame();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private static DecimalFormat formatter = new DecimalFormat("000");
	
}
