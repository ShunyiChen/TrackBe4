package com.maxtree.automotive.dashboard.view.imaging;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class PopupAutomatically extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PopupAutomatically() {
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
		btnOK.addStyleName("grid-button-without-border");
		
		buttonPane.addComponents(btnOK, Box.createHorizontalBox(1));
		buttonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_CENTER);

		wrapper.addComponent(buttonPane);
		wrapper.setComponentAlignment(buttonPane, Alignment.MIDDLE_RIGHT);
		btnOK.addClickListener(e -> {
			close();
			if(event != null)
				event.onSuccessful();
		});
		mainLayout.addComponents(messageLabel, wrapper);
	}
	
	/**
	 * 
	 * @param caption
	 * @param message
	 * @param event
	 */
	public void showDialog(String caption, String message, Callback event) {
		if(this.isAttached()) {
			close();
		}
		this.event = event;
		messageLabel.setValue("<span style='font-size:14px;color: #000000;'>" + message + "</span>");
		setIcon(VaadinIcons.WARNING);
		setCaption("&nbsp;&nbsp;" + caption);
		setCaptionAsHtml(true);
		UI.getCurrent().addWindow(this);
		center();
	}
	
	private Callback event;
	private Label messageLabel = new Label("", ContentMode.HTML);
	private Button btnOK = new Button("确定");
}
