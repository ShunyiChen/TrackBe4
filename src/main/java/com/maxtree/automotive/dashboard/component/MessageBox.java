package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.Callback;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MessageBox extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MessageBox(int messageType) {
		this.setResizable(false);
		this.setModal(true);
		this.setWidth("500px");
		this.setIcon(VaadinIcons.WARNING);
		this.setClosable(false);
		initComponents();
	}

	private void initComponents() {
		VerticalLayout mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		HorizontalLayout wrapper = new HorizontalLayout();
		wrapper.setWidth("100%");
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setWidthUndefined();
		buttonPane.setHeightUndefined();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		btnCancel.addStyleName("grid-button-without-border");
		btnOK.addStyleName("grid-button-without-border");
		
		buttonPane.addComponents(btnCancel, Box.createHorizontalBox(7), btnOK, Box.createHorizontalBox(1));
		buttonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		buttonPane.setComponentAlignment(btnOK, Alignment.MIDDLE_LEFT);

		wrapper.addComponent(buttonPane);
		wrapper.setComponentAlignment(buttonPane, Alignment.MIDDLE_RIGHT);

		btnCancel.addClickListener(e -> {
			close();
			if (onCancel != null) 
				onCancel.onSuccessful();
			
		});
		btnOK.addClickListener(e -> {
			close();
			onOK.onSuccessful();
		});
		mainLayout.addComponents(messageLabel, wrapper);
	}

	/**
	 * 
	 * @param caption
	 * @param message
	 * @param messageType
	 * @param onOK
	 */
	public static void showMessage(String caption, String message, int messageType, Callback onOK, String buttonText) {
		MessageBox w = new MessageBox(messageType);
		w.setIcon(messageType == 1 ? VaadinIcons.WARNING
				: (messageType == 2) ? VaadinIcons.INFO_CIRCLE : VaadinIcons.INFO_CIRCLE_O);
		w.setCaption("&nbsp;&nbsp;" + caption);
		w.setCaptionAsHtml(true);
		w.btnOK.setCaption(buttonText);
		w.messageLabel.setValue("<span style='font-size:14px;color: #000000;'>" + message + "</span>");
		// w.messageLabel.setIcon(VaadinIcons.COMMENT);
		w.onOK = onOK;
		UI.getCurrent().addWindow(w);
		w.center();
	}
	
	/**
	 * 
	 * @param caption
	 * @param message
	 * @param messageType
	 * @param onOK
	 * @param onCancel
	 * @param okButtonText
	 * @param cancelButtonText
	 */
	public static void showMessage(String caption, String message, int messageType, Callback onOK, Callback onCancel, String okButtonText, String cancelButtonText) {
		MessageBox w = new MessageBox(messageType);
		w.setIcon(messageType == 1 ? VaadinIcons.WARNING
				: (messageType == 2) ? VaadinIcons.INFO_CIRCLE : VaadinIcons.INFO_CIRCLE_O);
		w.setCaption("&nbsp;&nbsp;" + caption);
		w.setCaptionAsHtml(true);
		w.btnOK.setCaption(okButtonText);
		w.btnCancel.setCaption(cancelButtonText);
		w.messageLabel.setValue("<span style='font-size:14px;color: #000000;'>" + message + "</span>");
		w.onOK = onOK;
		w.onCancel = onCancel;
		UI.getCurrent().addWindow(w);
		w.center();
	}

	public static int INFO = 0;
	public static int WARNING = 1;
	public static int ERROR = 2;
	private Label messageLabel = new Label("", ContentMode.HTML);
	private Callback onOK;
	private Callback onCancel;
	private Button btnCancel = new Button("取消");
	private Button btnOK = new Button("确定");

}
