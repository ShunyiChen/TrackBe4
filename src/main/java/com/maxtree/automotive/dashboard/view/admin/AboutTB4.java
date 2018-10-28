package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.TB4Application;
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
public class AboutTB4 extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutTB4() {
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		this.setWidth("400px");
		this.setHeight("200px");
		
		HorizontalLayout info = new HorizontalLayout();
		Label infoLabel = new Label(TB4Application.NAME+" "+TB4Application.VERSION);
		info.setWidth("100%");
		info.addComponent(infoLabel);
		info.setComponentAlignment(infoLabel, Alignment.TOP_LEFT);
		
		HorizontalLayout date = new HorizontalLayout();
		Label buildInLabel = new Label(TB4Application.BUILD_ID);
		info.setWidth("100%");
		info.addComponent(buildInLabel);
		info.setComponentAlignment(buildInLabel, Alignment.TOP_LEFT);
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.setHeight("30px");
		buttons.addComponent(okButton);
		buttons.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		
		main.addComponents(info,date,buttons);
		main.setComponentAlignment(info, Alignment.TOP_LEFT);
		main.setComponentAlignment(date, Alignment.TOP_LEFT);
		main.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		this.setContent(main);
		
		okButton.addClickListener(e->{
			close();
		});
	}
	
	
	public static void open() {
		AboutTB4 AboutTB4 = new AboutTB4();
		UI.getCurrent().addWindow(AboutTB4);
		AboutTB4.center();
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button okButton = new Button("确定");
	private VerticalLayout main = new VerticalLayout();
}
