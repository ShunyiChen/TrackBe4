package com.maxtree.automotive.dashboard.view.dashboard;

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
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.tb4beans.PrintableBean;
import com.maxtree.trackbe4.reports.TB4Reports;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
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
public class PrintingConfirmationWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param transactionUniqueId
	 */
	public PrintingConfirmationWindow(String caption, int transactionUniqueId) {
		this.caption = caption;
		this.transactionUniqueId = transactionUniqueId;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption(caption);
		this.setWidthUndefined();
		this.setHeight("150px");
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(true);
		
		List<String> options = Arrays.asList("封皮标签", "文件标签");
		radios = new RadioButtonGroup<>(null, options);
		radios.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		HorizontalLayout optionsLayout = new HorizontalLayout();
		optionsLayout.addComponents(Box.createHorizontalBox(10), radios);
		optionsLayout.setComponentAlignment(radios, Alignment.MIDDLE_LEFT);
		
		
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
    	vlayout.addComponents(optionsLayout, Box.createVerticalBox(5), buttonLayout);
    	vlayout.setComponentAlignment(optionsLayout, Alignment.MIDDLE_CENTER);
    	vlayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
        
    	this.setContent(vlayout);
    	
    	
    	radios.addValueChangeListener(e->{
    		btnOk.setEnabled(false);
    		
    		if (opener != null) {
    			opener.remove();
    			
    			buttonLayout.removeComponent(btnOk);
    			// re-add button
    			btnOk = new Button("确定");
    			btnOk.setEnabled(false);
    			btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
    			buttonLayout.addComponent(btnOk, 2);
    			buttonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
    		}
    		
    		Transaction trans = ui.transactionService.findById(transactionUniqueId);
    		if(e.getValue().equals("封皮标签")) {
    			List<PrintableBean> beans = new ArrayList<PrintableBean>();
    			PrintableBean bean = new PrintableBean();
    			bean.setPlateType(trans.getPlateType());
    			bean.setPlateNumber(trans.getPlateNumber());
    			bean.setVin(trans.getVin());
    			bean.setPutawayCode(trans.getCode());
    			beans.add(bean);
    			
    			Callback callback = new Callback() {
					@Override
					public void onSuccessful() {
						btnOk.setEnabled(true);
						opener = new BrowserWindowOpener(PrintUI.class);
						opener.setFeatures("height=595,width=842,resizable");
						opener.extend(btnOk);
						opener.setParameter("htmlFilePath", "reports/generates/"+transactionUniqueId+"/report.html");
					}
    			};
    			try {
					new TB4Reports().jasperToHtml(beans, transactionUniqueId, "report3.jasper", callback);
					
				} catch (ReportException e1) {
					e1.printStackTrace();
				}
    		} else {
    			List<PrintableBean> beans = new ArrayList<PrintableBean>();
    			PrintableBean bean = new PrintableBean();
    			bean.setPlateType(trans.getPlateType());
    			bean.setPlateNumber(trans.getPlateNumber());
    			Business business = ui.businessService.findById(trans.getBusinessUniqueId());
    			bean.setBusinessType(business.getName());
    			bean.setPutawayCode(trans.getCode()); // 上架号
    			bean.setIndexNumber(trans.getIndexNumber()+""); // 索引号
    			bean.setBarcode(trans.getBarcode()); // 流水号
    			beans.add(bean);
    			
    			Callback callback = new Callback() {
					@Override
					public void onSuccessful() {
						
						btnOk.setEnabled(true);
						
						// 打印PDF
						FileResource resource = new FileResource(new File("reports/generates/"+transactionUniqueId+"/report.pdf"));
						// Extend the print button with an opener
			            // for the PDF resource
			            opener = new BrowserWindowOpener(resource);
			            opener.extend(btnOk);
					}
    			};
    			try {
					new TB4Reports().jasperToPDF(beans, transactionUniqueId, "report2.jasper", callback);
				} catch (ReportException e1) {
					// TODO Auto-generated catch block
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
    	
    	radios.setSelectedItem(options.get(0));
	}
	
	/**
	 * 
	 */
	private void deleteReportFiles() {
		new TB4Reports().deleteReportFiles("reports/generates/" + transactionUniqueId);
	}
	
	/**
	 * 
	 * @param caption
	 */
	public static void open(String caption, int transactionUniqueId) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        PrintingConfirmationWindow w = new PrintingConfirmationWindow(caption, transactionUniqueId);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button btnCancel = new Button("取消");
	private Button btnOk = new Button("确定");
	private RadioButtonGroup<String> radios;
	private String caption;
	private int transactionUniqueId;
	private BrowserWindowOpener opener;
	private HorizontalLayout buttonLayout = new HorizontalLayout();
}
