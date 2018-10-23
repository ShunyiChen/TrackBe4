package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 复制密集架数量输入框
 * 
 * @author chens
 *
 */
public class NumberofCopiesWindow extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NumberofCopiesWindow() {
		initComponents();
	}
	
	private void initComponents() {
		this.setModal(true);
		this.setResizable(false);
		this.setClosable(false);
		this.setWidth("200px");
		this.setHeight("100px");
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.addComponents(txtLabel,numField);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("100px");
		buttonLayout.setHeight("40px");
		HorizontalLayout rightButtonLayout = new HorizontalLayout();
		rightButtonLayout.setSizeUndefined();
		rightButtonLayout.addComponents(btnOk,btnCancel);
		rightButtonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
		rightButtonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		buttonLayout.addComponent(rightButtonLayout);
		buttonLayout.setComponentAlignment(rightButtonLayout, Alignment.MIDDLE_RIGHT);
		
		main.addComponents(hlayout,buttonLayout);
		this.setContent(main);
	}
	
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(numField.getValue())) {
			numField.setComponentError(new ErrorMessage() {
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
					return "复制个数不能为空。";
				}
			});
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param frame
	 * @param callback
	 */
	public static void open(Callback2 callback) {
		// DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		NumberofCopiesWindow w = new NumberofCopiesWindow();
		w.btnOk.addClickListener(e -> {
			if (w.checkEmptyValues()) {
				w.close();
				callback.onSuccessful(w.numField.getValue());
			}
		});
		w.btnCancel.addClickListener(e->{
			w.close();
			callback.onSuccessful();
		});
		UI.getCurrent().addWindow(w);
		w.center();
	}
	
	private VerticalLayout main = new VerticalLayout();
	private Label txtLabel = new Label("复制个数：");
	private TextField numField = new TextField();
	private Button btnOk = new Button("确定");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
