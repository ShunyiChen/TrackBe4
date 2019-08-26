package com.maxtree.automotive.dashboard.view.front;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.FinalCheck;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.maxtree.tb4beans.PrintableBean;
import com.maxtree.trackbe4.reports.TB4Reports;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Chen
 *
 */
public class PrintingResultsWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param trans
	 */
	public PrintingResultsWindow(String caption, Transaction trans) {
		this.caption = caption;
		this.trans = trans;
		initComponents();
	}
	
	private void initComponents() {
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
		this.setCaption(caption);
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		this.setWidth("300px");
		this.setHeight("120px");
		this.addCloseListener(e->{
			closableEvent.onSuccessful();
		});
		
		List<String> options = Arrays.asList("车辆标签", "文件标签","打印审核结果单");
		radios = new RadioButtonGroup<>(null, options);
		radios.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnReady.addStyleName(ValoTheme.BUTTON_PRIMARY);
//		btnReady.setEnabled(false);
		HorizontalLayout optionsLayout = new HorizontalLayout();
		optionsLayout.addComponents(Box.createHorizontalBox(10), radios);
		optionsLayout.setComponentAlignment(radios, Alignment.MIDDLE_LEFT);
    	footer.setSpacing(false);
    	footer.setMargin(false);
    	footer.setWidthUndefined();
    	footer.setHeight("40px");
    	footer.addComponents(btnCancel, Box.createHorizontalBox(5), btnReady, Box.createHorizontalBox(5));
    	footer.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
    	footer.setComponentAlignment(btnReady, Alignment.MIDDLE_LEFT);
    	
		main.setSpacing(false);
		main.setMargin(false);
		main.setSizeFull();
    	main.addComponents(optionsLayout,footer);
    	main.setComponentAlignment(optionsLayout, Alignment.MIDDLE_CENTER);
    	main.setComponentAlignment(footer, Alignment.TOP_RIGHT);
    	this.setContent(main);
    	btnCancel.addClickListener(e->{
    		close();
    	});
    	radios.addValueChangeListener(e->{
    		generateReport();
    	});
    	// 窗体关闭后自动删除报表文件
    	this.addCloseListener(e -> {
    		deleteReportFiles();
    	});
    	radios.setSelectedItem(options.get(0));
	}
	
	/**
	 * 
	 */
	private void generateReport() {
		
		radios.addValueChangeListener(e->{
			if(e.getValue().equals("打印审核结果单")) {
				printingPDFForResults();
			}
			else if(e.getValue().equals("车辆标签")) {
				List<PrintableBean> lst = new ArrayList<PrintableBean>();
				PrintableBean b = new PrintableBean();
				b.setPlateType(trans.getPlateType());
				b.setPlateNum(trans.getPlateNumber());
				b.setCLSBDH(trans.getVin());
				b.setShelvesNum(trans.getCode());
				lst.add(b);
				
				printingPDFForTags(lst, "上架标签-车.jasper");
				
			} else {
				List<PrintableBean> lst = new ArrayList<PrintableBean>();
				PrintableBean b = new PrintableBean();
				b.setPlateType(trans.getPlateType());//号码种类
				b.setPlateNum(trans.getPlateNumber());//号牌号码
//				Business business = ui.businessService.findByCode(trans.getBusinessCode());
//				b.setBusType(business.getName());//业务类型
				b.setShelvesNum(trans.getCode()); //上架号
				b.setIndex(trans.getIndexNumber()); //索引号
				b.setCode(trans.getBarcode()); //流水号
				lst.add(b);
				printingPDFForTags(lst, "上架标签.jasper");
			}
		});
		
	}
	
	/**
	 * 
	 */
	private void printingPDFForResults() {
//		Transition transition = ui.transitionService.findByUUID(trans.getUuid(), trans.getVin());
//		List<PrintableBean> list = new ArrayList<PrintableBean>();
//		PrintableBean bean = new PrintableBean();
//		bean.setOwner(trans.getCreator());//机动车所有人
//		bean.setIDNum("");//证件号
//		bean.setPlateType(trans.getPlateType());
//		bean.setPlateNum(trans.getPlateNumber());
//		bean.setCLSBDH(trans.getVin());
//		bean.setShelvesNum(trans.getCode());
//		bean.setVerifier(transition.getOperator());
//		bean.setVerifytime(format.format(transition.getDateCreated()));
//		bean.setBusName(trans.getBusinessName());
//		bean.setWrongContent(transition.getComments());
//		list.add(bean);
//		Callback callback = new Callback() {
//			@Override
//			public void onSuccessful() {
//				com.vaadin.server.StreamResource.StreamSource source = new com.vaadin.server.StreamResource.StreamSource() {
//		 			/**
//					 *
//					 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//		 			public InputStream getStream() {
//		 				FileInputStream inputStream = null;
//						try {
//							inputStream = new FileInputStream("reports/generates/"+loggedInUser.getUserUniqueId()+"/report.pdf");
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						}
//						return inputStream;
//		 			}
//		 		};
//
//		        // Create the stream resource and give it a file name
//		        String filename = "report.pdf";
//		        StreamResource resource = new StreamResource(source, filename);
//
//		        // These settings are not usually necessary. MIME type
//		        // is detected automatically from the file name, but
//		        // setting it explicitly may be necessary if the file
//		        // suffix is not ".pdf".
//		        resource.setMIMEType("application/pdf");
//		        resource.getStream().setParameter(
//		                "Content-Disposition",
//		                "attachment; filename="+filename);
//
//		        // Extend the print button with an opener
//		        // for the PDF resource
//		        opener =  new BrowserWindowOpener(resource);
//		        opener.extend(btnReady);
//
//				FinalCheck finalCheck = new FinalCheck();
//				finalCheck.setBarcode(trans.getBarcode());
//				finalCheck.setVin(trans.getVin());
//				ui.transactionService.insertFinalCheck(finalCheck);
//
//				if(trans.getStatus().equals("待上架")) {
//					ui.transactionService.updateStatus(trans.getVin(), trans.getUuid(), ui.state().getName("B19"));
//				}
//
//				try {
//					resource.getStreamSource().getStream().close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		};
//		if(trans.getStatus().equals(ui.state().getName("B16"))
//				|| trans.getStatus().equals(ui.state().getName("B17"))) {
//			try {
//				new TB4Reports().jasperToPDF(list, loggedInUser.getUserUniqueId(), "影像化档案审核退办单.jasper", callback);
//			} catch (ReportException e1) {
//				e1.printStackTrace();
//			}
//		}
//		else {
//			try {
//				new TB4Reports().jasperToPDF(list, loggedInUser.getUserUniqueId(), "影像化档案审核合格证明书.jasper", callback);
//			} catch (ReportException e1) {
//				e1.printStackTrace();
//			}
//		}
	}
	
	/**
	 * 
	 * @param beans
	 * @param templateFileName
	 */
	private void printingPDFForTags(List<PrintableBean> beans, String templateFileName) {
		Callback callback = new Callback() {
			@Override
			public void onSuccessful() {
				com.vaadin.server.StreamResource.StreamSource source = new com.vaadin.server.StreamResource.StreamSource() {
		 			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
		 			public InputStream getStream() {
		 				FileInputStream inputStream = null;
						try {
							inputStream = new FileInputStream("reports/generates/"+loggedInUser.getUserUniqueId()+"/report.pdf");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						return inputStream;
		 			}
		 		}; 
				
		        // Create the stream resource and give it a file name
		        String filename = "report.pdf";
		        StreamResource resource = new StreamResource(source, filename);

		        // These settings are not usually necessary. MIME type
		        // is detected automatically from the file name, but
		        // setting it explicitly may be necessary if the file
		        // suffix is not ".pdf".
		        resource.setMIMEType("application/pdf");
		        resource.getStream().setParameter(
		                "Content-Disposition",
		                "attachment; filename="+filename);
		        resource.setCacheTime(0);
		        
		        try {
					resource.getStreamSource().getStream().close();
					  resource.getStream().getStream().close();
					 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        // Extend the print button with an opener
		        // for the PDF resource
		        opener = new BrowserWindowOpener(resource);
		        opener.extend(btnReady);
		        
		        if(trans.getStatus().equals("待上架")) {
//					ui.transactionService.updateStatus(trans.getVin(), trans.getUuid(), ui.state().getName("B19"));
				}
				
				FinalCheck finalCheck = new FinalCheck();
				finalCheck.setBarcode(trans.getBarcode());
				finalCheck.setVin(trans.getVin());
				ui.transactionService.insertFinalCheck(finalCheck);
			}
		};
		try {
			new TB4Reports().jasperToPDF(beans, loggedInUser.getUserUniqueId(), templateFileName, callback);
		} catch (ReportException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Deleting report file.
	 */
	private void deleteReportFiles() {
		System.gc();
		new TB4Reports().deleteReportFiles("reports/generates/" + loggedInUser.getUserUniqueId());
	}
	
	/**
	 * 
	 * @param caption
	 * @param trans
	 */
	public static void open(String caption, Transaction trans, Callback closableEvent) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        PrintingResultsWindow w = new PrintingResultsWindow(caption, trans);
        w.closableEvent = closableEvent;
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private User loggedInUser;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button btnCancel = new Button("取消");
	private Button btnReady = new Button("准备打印");
	private RadioButtonGroup<String> radios;
	private String caption;
	private BrowserWindowOpener opener;
	private Transaction trans;
	private Callback closableEvent;
	private HorizontalLayout footer = new HorizontalLayout();
	private VerticalLayout main = new VerticalLayout();
//	private Panel previewPane = new Panel();
	
}
