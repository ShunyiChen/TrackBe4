package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.domain.Message;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class Openwith extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public Openwith(Message message) {
		this.message = message;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("选择打开方式");
		this.setWidth("400px");
		this.setHeight("350px");
		this.setResizable(false);
		this.setModal(true);
		this.addStyleName("edit-window");
		
 
		HorizontalLayout headerLayout = new HorizontalLayout();
		Label title = new Label("请选择一种打开方式：");
		headerLayout.setWidth("100%");
		headerLayout.setSpacing(false);
		headerLayout.setMargin(false);
		headerLayout.addComponent(title);
		headerLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		
		
		HorizontalLayout footerLayout = new HorizontalLayout();
		view.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		footerLayout.setWidth("100%");
		footerLayout.setSpacing(false);
		footerLayout.addComponents(view,edit,print);
		
		main.setSpacing(true);
		main.setMargin(false);
		main.addComponents(headerLayout,footerLayout);
		
		this.setContent(main);
	}
	
	public static void open(Message message) {
		Openwith ow = new Openwith(message);
		UI.getCurrent().addWindow(ow);
		ow.center();
	}

	private Button view = new Button("查看");
	private Button edit = new Button("编辑");
	private Button print = new Button("打印");
	private VerticalLayout main = new VerticalLayout();
	private Message message;
}
