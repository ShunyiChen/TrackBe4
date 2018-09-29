package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.tb4beans.PrintableBean;
import com.maxtree.trackbe4.reports.TB4Reports;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class PrintingFileTagOnlyWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param trans
	 */
	public PrintingFileTagOnlyWindow(String caption, Transaction trans) {
		this.caption = caption;
		this.trans = trans;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption(caption);
		this.setWidthUndefined();
		this.setHeight("150px");
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(true);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		
    	buttonLayout.setSpacing(false);
    	buttonLayout.setMargin(false);
    	buttonLayout.setWidthUndefined();
    	buttonLayout.setHeight("40px");
    	buttonLayout.addComponents(btnCancel, Box.createHorizontalBox(5), btnOk, Box.createHorizontalBox(5));
    	buttonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
    	
    	VerticalLayout vlayout = new VerticalLayout();
		vlayout.setSpacing(false);
		vlayout.setMargin(false);
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
    	vlayout.addComponents(buttonLayout);
    	vlayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
    	this.setContent(vlayout);
    	
    	
//    	radios.addValueChangeListener(e->{
//    		btnOk.setEnabled(false);
//    		
//    		if (opener != null) {
//    			opener.remove();
//    			
//    			buttonLayout.removeComponent(btnOk);
//    			// re-add button
//    			btnOk = new Button("确定");
//    			btnOk.setEnabled(false);
//    			btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
//    			buttonLayout.addComponent(btnOk, 2);
//    			buttonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
//    		}
//    		
//    		if(e.getValue().equals("车辆标签")) {
//    			List<PrintableBean> list = new ArrayList<PrintableBean>();
//    			PrintableBean bean = new PrintableBean();
//    			bean.setPlateType(trans.getPlateType());
//    			bean.setPlateNum(trans.getPlateNumber());
//    			bean.setCLSBDH(trans.getVin());
//    			bean.setShelvesNum(trans.getCode());
//    			list.add(bean);
//    			
//    			Callback callback = new Callback() {
//					@Override
//					public void onSuccessful() {
//						btnOk.setEnabled(true);
//						opener = new BrowserWindowOpener(PrintUI.class);
//						opener.setFeatures("height=595,width=842,resizable");
//						opener.extend(btnOk);
//						opener.setParameter("htmlFilePath", "reports/generates/"+loggedInUser.getUserUniqueId()+"/report.html");
//					}
//    			};
//    			try {
//					new TB4Reports().jasperToHtml(list, trans.getTransactionUniqueId(), "上架标签-车.jasper", callback);
//					
//				} catch (ReportException e1) {
//					e1.printStackTrace();
//				}
//    		} else {
//    			List<PrintableBean> list = new ArrayList<PrintableBean>();
//    			PrintableBean bean = new PrintableBean();
//    			bean.setPlateType(trans.getPlateType());//号码种类
//    			bean.setPlateNum(trans.getPlateNumber());//号牌号码
//    			Business business = ui.businessService.findByCode(trans.getBusinessCode());
//    			bean.setBusType(business.getName());//业务类型
//    			bean.setShelvesNum(trans.getCode()); // 上架号
//    			bean.setIndex(trans.getIndexNumber()); // 索引号
//    			bean.setCode(Integer.parseInt(trans.getBarcode())); // 流水号
//    			list.add(bean);
//    			
//    			Callback callback = new Callback() {
//					@Override
//					public void onSuccessful() {
//						btnOk.setEnabled(true);
//						opener = new BrowserWindowOpener(PrintUI.class);
//						opener.setFeatures("height=595,width=842,resizable");
//						opener.extend(btnOk);
//						opener.setParameter("htmlFilePath", "reports/generates/"+loggedInUser.getUserUniqueId()+"/report.html");
//					}
//    			};
//    			try {
//					new TB4Reports().jasperToHtml(list, trans.getTransactionUniqueId(), "上架标签.jasper", callback);
//					
//				} catch (ReportException e1) {
//					e1.printStackTrace();
//				}
//    			
//    		}
//    		
//    	});
    	
    	
    	ui.access(new Runnable() {

			@Override
			public void run() {
				List<PrintableBean> list = new ArrayList<PrintableBean>();
    			PrintableBean bean = new PrintableBean();
    			bean.setPlateType(trans.getPlateType());//号码种类
    			bean.setPlateNum(trans.getPlateNumber());//号牌号码
    			Business business = ui.businessService.findByCode(trans.getBusinessCode());
    			bean.setBusType(business.getName());//业务类型
    			bean.setShelvesNum(trans.getCode()); // 上架号
    			bean.setIndex(trans.getIndexNumber()); // 索引号
    			bean.setCode(Integer.parseInt(trans.getBarcode())); // 流水号
    			list.add(bean);
    			
    			Callback callback = new Callback() {
					@Override
					public void onSuccessful() {
						btnOk.setEnabled(true);
						opener = new BrowserWindowOpener(PrintUI.class);
						opener.setFeatures("height=595,width=842,resizable");
						opener.extend(btnOk);
						opener.setParameter("htmlFilePath", "reports/generates/"+loggedInUser.getUserUniqueId()+"/report.html");
					}
    			};
    			try {
					new TB4Reports().jasperToHtml(list,loggedInUser.getUserUniqueId(), "上架标签.jasper", callback);
				} catch (ReportException e1) {
					e1.printStackTrace();
				}
			}
    		
    	});
    	
    	btnCancel.addClickListener(e->{
    		deleteReportFiles();
    		
    		close();
    	});
    	this.addCloseListener(e -> {
    		deleteReportFiles();
    	});
     
	}
	
	/**
	 * 
	 */
	private void deleteReportFiles() {
		new TB4Reports().deleteReportFiles("reports/generates/" + loggedInUser.getUserUniqueId());
	}
	
	/**
	 * 
	 * @param caption
	 */
	public static void open(String caption, Transaction trans) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        PrintingFileTagOnlyWindow w = new PrintingFileTagOnlyWindow(caption, trans);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private User loggedInUser;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button btnCancel = new Button("取消");
	private Button btnOk = new Button("确定");
	private String caption;
	private Transaction trans;
	private BrowserWindowOpener opener;
	private HorizontalLayout buttonLayout = new HorizontalLayout();
}
