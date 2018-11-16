package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.TB4Application;
import com.vaadin.icons.VaadinIcons;
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
		this.setIcon(VaadinIcons.INFO_CIRCLE_O);
		this.setCaption("关于");
		this.setModal(false);
		this.setClosable(false);
		this.setResizable(false);
		this.setWidth("460px");
		this.setHeight("200px");
		
		HorizontalLayout titleLine = new HorizontalLayout();
		Label infoLabel = new Label(TB4Application.NAME);
		titleLine.setWidth("100%");
		titleLine.addComponent(infoLabel);
		titleLine.setComponentAlignment(infoLabel, Alignment.TOP_LEFT);
		
		VerticalLayout middleLine = new VerticalLayout();
		middleLine.setMargin(false);
		middleLine.setSpacing(false);
		Label versionLabel = new Label("版本: "+TB4Application.VERSION);
		Label buildInLabel = new Label("构建时间: "+TB4Application.BUILD_ID);
		middleLine.addComponents(versionLabel, buildInLabel);
		middleLine.addStyleName("AboutTB4-middleLine");
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.setHeight("30px");
		buttons.addComponent(okButton);
		buttons.setComponentAlignment(okButton, Alignment.MIDDLE_RIGHT);
		
		main.setSizeFull();
		main.addComponents(titleLine,middleLine,buttons);
		main.setComponentAlignment(titleLine, Alignment.TOP_CENTER);
		main.setComponentAlignment(middleLine, Alignment.TOP_CENTER);
		main.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		main.setExpandRatio(buttons, 0);
		this.setContent(main);
		this.addStyleName("AboutTB4");
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
