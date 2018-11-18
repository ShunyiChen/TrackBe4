package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.domain.Log;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class ExceptionViewer extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param log
	 */
	public ExceptionViewer(Log log) {
		this.setModal(false);
		this.setCaption("查看异常");
		this.setClosable(true);
		this.setResizable(false);
		this.setWidth("600px");
		this.setHeight("308px");
		
		txtArea.setSizeFull();
		txtArea.setValue(log.getException()==null?"":log.getException());
		
		main.setSizeFull();
		main.addComponents(txtArea,ok);
		main.setComponentAlignment(txtArea, Alignment.TOP_CENTER);
		main.setComponentAlignment(ok, Alignment.BOTTOM_RIGHT);
		main.setExpandRatio(txtArea, 1);
		main.setExpandRatio(ok, 0);
		this.setContent(main);
		ok.addClickListener(e->{
			close();
		});
	}
	
	/**
	 * 
	 * @param log
	 */
	public static void open(Log log) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        final ExceptionViewer w = new ExceptionViewer(log);
        UI.getCurrent().addWindow(w);
        w.center();
	}
	
	private TextArea txtArea = new TextArea();
	private VerticalLayout main = new VerticalLayout();
	private Button ok = new Button("确定");
}
