package com.maxtree.automotive.dashboard.view.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditFrameWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public EditFrameWindow() {
//		initComponents();
//	}
//
//	private void initComponents() {
//		this.setWidth("320px");
//		this.setHeight("252px");
//		this.setClosable(true);
////		this.setResizable(true);
//		this.setModal(true);
//		VerticalLayout main = new VerticalLayout();
//		main.setWidth("100%");
//		main.setHeightUndefined();
//		maxColField.focus();
//		maxColField.setHeight("27px");
//		maxRowField.setHeight("27px");
//		frameCodeField.setHeight("27px");
//		maxFolderField.setHeight("27px");
//
//		FormLayout form = new FormLayout();
//		form.setSpacing(false);
//		form.setMargin(false);
//		form.setSizeFull();
//		form.addComponents(maxColField,maxRowField,maxFolderField,frameCodeField);
//		frameCodeField.setReadOnly(true);
//
//
//		HorizontalLayout buttons = new HorizontalLayout();
//		buttons.setSpacing(false);
//		buttons.setMargin(false);
//		buttons.setWidthUndefined();
//		buttons.setHeight("27px");
//		buttons.addComponents(btnCancel,Box.createHorizontalBox(5),btnAdd);
//		buttons.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
//		buttons.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
//
//		main.addComponents(form, buttons);
//		main.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
//		main.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
//		setContent(main);
//
//        btnCancel.addClickListener(e -> {
//        	close();
//        });
//        // Bind nameField to the Person.name property
// 		// by specifying its getter and setter
// 		bindFields();
// 		// Bind an actual concrete Person instance.
// 		// After this, whenever the user changes the value
// 		// of nameField, p.setName is automatically called.
// 		binder.setBean(frame);
//	}
//
//	/**
//	 *
//	 */
//	private void bindFields() {
//		// Bind nameField to the Person.name property
//		// by specifying its getter and setter
//		binder.forField(maxColField)
//		  .withValidator(new EmptyValidator("输入列数不能为空"))
//		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
//		  .bind(FrameNumber::getMaxColumn, FrameNumber::setMaxColumn);
//
//		binder.forField(maxRowField)
//		  .withValidator(new EmptyValidator("输入行数不能为空"))
//		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
//		  .bind(FrameNumber::getMaxRow, FrameNumber::setMaxRow);
//
//		binder.forField(maxFolderField)
//		  .withValidator(new EmptyValidator("输入最大文件夹数不能为空"))
//		  .withConverter(new StringToIntegerConverter("请输入一个数字"))
//		  .bind(FrameNumber::getMaxfolder, FrameNumber::setMaxfolder);
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private boolean checkEmptyValues() {
//		if (StringUtils.isEmpty(maxColField.getValue())) {
//			Notifications.warning("最大列数不能为空。");
//			return false;
//		}
//		else if (StringUtils.isEmpty(maxRowField.getValue())) {
//			Notifications.warning("最大行数不能为空。");
//			return false;
//		}
//		else {
//
//			try {
//				int maxCol = Integer.parseInt(maxColField.getValue());
//				int maxRow = Integer.parseInt(maxRowField.getValue());
//				int maxFolder = Integer.parseInt(maxFolderField.getValue());
//				if (maxCol < 1 || maxCol > 20 || maxRow < 1 || maxRow > 20) {
//					Notifications.warning("请输入一个数字，范围应该在1-20。");
//					return false;
//				}
//				else if(maxFolder < 1 || maxFolder > 10000) {
//					Notifications.warning("请输入一个数字，范围应该在1-10000。");
//					return false;
//				}
//			} catch(NumberFormatException e) {
//				Notifications.warning("请输入一个数字。");
//				return false;
//			}
//		}
//		return true;
//	}
//
//	/**
//	 *
//	 * @param store
//	 * @param frameCode
//	 * @param maxFolder
//	 */
//	private void insertCells(FrameNumber store, int frameCode, int maxFolder) {
//		int cellCode = 0;//单元格顺序号
//		for (int i = 1; i <= frame.getMaxRow(); i++) {
//
//			for (int j = 1; j <= frame.getMaxColumn(); j++) {
//				cellCode++;
//				FrameNumber cell = new FrameNumber();
//				cell.setStorehouseName(store.getStorehouseName());
//				cell.setFrameCode(frameCode);
//				cell.setMaxColumn(frame.getMaxColumn());
//				cell.setMaxRow(frame.getMaxRow());
//				cell.setCol(j);
//				cell.setRow(i);
//				cell.setCellCode(cellCode);
//
//				int cellId = ui.frameService.insert(cell);
//				cell.setFrameUniqueId(cellId);
//
//				List<FrameNumber> batch = new ArrayList<FrameNumber>();
//				for(int z=1; z<=maxFolder; z++) {
//					StringBuilder codes = new StringBuilder();
//					codes.append(formatter.format(frame.getFrameCode()));
//					codes.append("-");
//					codes.append(formatter.format(j));
//					codes.append("-");
//					codes.append(formatter.format(i));
//					codes.append("-");
//					codes.append(formatter.format(z));
//
//					FrameNumber bag = new FrameNumber();
//					bag.setStorehouseName(store.getStorehouseName());
//					bag.setFrameCode(frameCode);
//					bag.setMaxColumn(frame.getMaxColumn());
//					bag.setMaxRow(frame.getMaxRow());
//					bag.setCol(j);
//					bag.setRow(i);
//					bag.setCellCode(cellCode);
//					bag.setCode(codes.toString());
//					bag.setVin(null);
//					batch.add(bag);
//				}
//				ui.frameService.insert(batch);
//
//			}
//		}
//	}
//
//	/**
//	 *
//	 * @param storehouseCode
//	 * @param callback
//	 */
//	public static void open(FrameNumber store, Callback2 callback) {
////        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        EditFrameWindow w = new EditFrameWindow();
//        w.setCaption("添加新密集架");
//        int frameCode = ui.frameService.findNextCodeOfFrame(store.getStorehouseName());
//        w.maxFolderField.setValue("100");
//        w.frameCodeField.setValue(frameCode+"");
//        w.btnAdd.addClickListener(e -> {
//			if (w.checkEmptyValues()) {
//				w.frame.setStorehouseName(store.getStorehouseName());
//				w.frame.setFrameCode(frameCode);
//				int frameId = ui.frameService.insert(w.frame);
//				w.frame.setFrameUniqueId(frameId);
//
//				w.insertCells(store, frameCode, w.frame.getMaxfolder());
//
//    			w.close();
//    			callback.onSuccessful(w.frame);
//			}
//        });
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	/**
//	 *
//	 * @param denseFrame
//	 * @param callback
//	 */
//	public static void edit(FrameNumber store, FrameNumber frame, Callback2 callback) {
////        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        EditFrameWindow w = new EditFrameWindow();
//        w.setCaption("更改密集架-"+frame.getFrameCode());
//        w.frame.setStorehouseName(frame.getStorehouseName());
//        w.frame.setFrameCode(frame.getFrameCode());
//        w.frame.setMaxColumn(frame.getMaxColumn());
//        w.frame.setMaxRow(frame.getMaxRow());
//        w.maxFolderField.setValue(frame.getMaxfolder()+"");
//        w.maxColField.setValue(frame.getMaxColumn()+"");
//        w.maxRowField.setValue(frame.getMaxRow()+"");
//        w.frameCodeField.setValue(frame.getFrameCode()+"");
//
//        w.btnAdd.setCaption("更改");
//        w.btnAdd.addClickListener(e -> {
//        	if (w.checkEmptyValues()) {
//        		//更新frame信息
//        		ui.frameService.updateFrame(w.frame);
//
//        		//取frame全部文件夹带有vin不为空的记录
//        		List<FrameNumber> folerWithVINList = ui.frameService.findAllFolderWithVIN(w.frame.getStorehouseName(), w.frame.getFrameCode());
//
//        		//删除原来frame下的所有cells
//        		ui.frameService.deleteFrameCells(w.frame.getStorehouseName(), w.frame.getFrameCode());
//
//        		//插入新的cells
//        		w.insertCells(store, frame.getFrameCode(),w.frame.getMaxfolder());
//
//        		//更新frame文件夹记录，把vin更新到新纪录上。
//        		ui.frameService.batchUpdateVIN(folerWithVINList);
//
//    			w.close();
//    			callback.onSuccessful(w.frame);
//			}
//        });
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private DoubleField maxColField = new DoubleField("最大列数:");
//	private DoubleField maxRowField = new DoubleField("最大行数:");
//	private DoubleField maxFolderField = new DoubleField("最大文件夹数:");
//	private TextField frameCodeField = new TextField("顺序号:");
//	private Button btnAdd = new Button("添加");
//	private Button btnCancel = new Button("取消");
//	private Binder<FrameNumber> binder = new Binder<>();
//	private FrameNumber frame = new FrameNumber();
//	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private static DecimalFormat formatter = new DecimalFormat("000");
	
}
