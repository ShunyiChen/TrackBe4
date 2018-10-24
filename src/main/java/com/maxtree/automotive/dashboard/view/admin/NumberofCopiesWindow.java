package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
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
		this.setCaption("输入复制个数");
		this.setWidth("290px");
		this.setHeight("154px");
		this.setClosable(true);
		this.setResizable(false);
		this.setModal(true);
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.addComponent(numField);
		numField.focus();
		numField.setHeight("27px");
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setHeight("40px");
		buttonLayout.setSpacing(false);
		buttonLayout.setMargin(false);
		buttonLayout.addComponents(btnCancel,Box.createHorizontalBox(5),btnOk);
		buttonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
		buttonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		main.addComponents(form, buttonLayout);
		main.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		main.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		this.setContent(main);
	}
	
	/**
	 * 
	 * @return
	 */
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
		else {
			try {
				Integer.parseInt(numField.getValue());
			} catch(NumberFormatException e) {
				Notifications.warning("请输入一个整数。");
				return false;
			}
			
			if(Integer.parseInt(numField.getValue()) < 1 
					|| Integer.parseInt(numField.getValue()) > 10000) {
				Notifications.warning("请输入一个大于0小于10000的整数。");
			}
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
	private DoubleField numField = new DoubleField("复制个数:");
	private Button btnOk = new Button("确定");
	private Button btnCancel = new Button("取消");
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
