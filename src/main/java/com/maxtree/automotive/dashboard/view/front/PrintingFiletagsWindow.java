package com.maxtree.automotive.dashboard.view.front;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.tb4beans.PrintableBean;
import com.maxtree.trackbe4.reports.TB4Reports;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Extension;
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
 * @author chens
 *
 */
public class PrintingFiletagsWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param trans
	 * @param options
	 */
	public PrintingFiletagsWindow(String caption, Transaction trans, List<String> options) {
		this.caption = caption;
		this.trans = trans;
		this.options = options;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption(caption);
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		this.setWidth("300px");
		this.setHeight("120px");
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		radios = new RadioButtonGroup<>(null, options);
		radios.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		HorizontalLayout optionsLayout = new HorizontalLayout();
		optionsLayout.addComponents(Box.createHorizontalBox(10), radios);
		optionsLayout.setComponentAlignment(radios, Alignment.MIDDLE_LEFT);
    	footer.setSpacing(false);
    	footer.setMargin(false);
    	footer.setWidthUndefined();
    	footer.setHeight("40px");
    	footer.addComponents(btnCancel, Box.createHorizontalBox(5), btnOk, Box.createHorizontalBox(5));
    	footer.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
    	footer.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
    	
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
//    		btnOk.setEnabled(true);
    		if (opener != null) {
    			Collection<Extension> con = opener.getParent().getExtensions();
    			if(con.contains(opener)) {
    				opener.remove();
    			}
    			
    			footer.removeComponent(btnOk);
    			// re-add button
    			btnOk = new Button("准备打印");
//    			btnOk.setEnabled(false);
    			btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
    			footer.addComponent(btnOk, 2);
    			footer.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
    		}
    		
    		if(e.getValue().equals("车辆标签")) {
    			List<PrintableBean> list = new ArrayList<PrintableBean>();
    			PrintableBean bean = new PrintableBean();
    			bean.setPlateType(trans.getPlateType());
    			bean.setPlateNum(trans.getPlateNumber());
    			bean.setCLSBDH(trans.getVin());
    			bean.setShelvesNum(trans.getCode());
    			list.add(bean);
    			printingPDF(list, "上架标签-车.jasper");
    			
    		} else {
    			List<PrintableBean> list = new ArrayList<PrintableBean>();
    			PrintableBean bean = new PrintableBean();
    			bean.setPlateType(trans.getPlateType());//号码种类
    			bean.setPlateNum(trans.getPlateNumber());//号牌号码
    			Business business = ui.businessService.findByCode(trans.getBusinessCode());
    			bean.setBusType(business.getName());//业务类型
    			bean.setShelvesNum(trans.getCode()); //上架号
    			bean.setIndex(trans.getIndexNumber()); //索引号
    			bean.setCode(trans.getBarcode()); //流水号
    			list.add(bean);
    			printingPDF(list, "上架标签.jasper");
    		}
    	});
     
    	// 窗体关闭后自动删除报表文件
    	this.addCloseListener(e -> {
    		deleteReportFiles();
    	});
    	
    	radios.setSelectedItem(options.get(0));
	}
	
	/**
	 * 
	 * @param beans
	 * @param templateFileName
	 */
	private void printingPDF(List<PrintableBean> beans, String templateFileName) {
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
//						try {
//							inputStream.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
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
		        // Extend the print button with an opener
		        // for the PDF resource
		        opener = new BrowserWindowOpener(resource);
		        opener.extend(btnOk);
		        
				// Update status
				ui.transactionService.updateStatus(trans.getVin(), trans.getUuid(), ui.state().getName("B19"));
				
				try {
					resource.getStreamSource().getStream().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		try {
			new TB4Reports().jasperToPDF(beans, loggedInUser.getUserUniqueId(), templateFileName, callback);
		} catch (ReportException e1) {
			e1.printStackTrace();
		}
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
	public static void open(String caption, Transaction trans, List<String> options) {
//		List<String> options = Arrays.asList("车辆标签", "文件标签");
//      DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        PrintingFiletagsWindow w = new PrintingFiletagsWindow(caption, trans, options);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private User loggedInUser;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button btnCancel = new Button("取消");
	private Button btnOk = new Button("准备打印");
	private RadioButtonGroup<String> radios;
	private String caption;
	private Transaction trans;
	private List<String> options;
	private BrowserWindowOpener opener;
	private HorizontalLayout footer = new HorizontalLayout();
	private VerticalLayout main = new VerticalLayout();
//	private Panel previewPane = new Panel();
}
