package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.tb4beans.PrintableBean;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.maxtree.trackbe4.reports.TB4Reports;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import Tmri.InterF;

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
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		this.setCaption(caption);
		this.setSizeFull();
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		this.addCloseListener(e->{
			closableEvent.onSuccessful();
		});
		
		List<String> options = Arrays.asList("打印审核结果单");
		radios = new RadioButtonGroup<>(null, options);
		radios.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnReady.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnReady.setEnabled(false);
		HorizontalLayout optionsLayout = new HorizontalLayout();
		optionsLayout.addComponents(Box.createHorizontalBox(10), radios);
		optionsLayout.setComponentAlignment(radios, Alignment.MIDDLE_LEFT);
		
		previewPane.setSizeFull();
		
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
    	main.addComponents(optionsLayout,previewPane,footer);
    	main.setComponentAlignment(optionsLayout, Alignment.MIDDLE_CENTER);
    	main.setComponentAlignment(previewPane, Alignment.TOP_CENTER);
    	main.setComponentAlignment(footer, Alignment.TOP_RIGHT);
    	
    	main.setExpandRatio(optionsLayout, 0.1f);
    	main.setExpandRatio(previewPane, 0.8f);
    	main.setExpandRatio(footer, 0.1f);
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
		Transition transition = ui.transitionService.findByUUID(trans.getUuid(), trans.getVin());
//		ArrayList<Map> arry = interF.getDriverBusView(trans.getBarcode());
		List<PrintableBean> list = new ArrayList<PrintableBean>();
		PrintableBean bean = new PrintableBean();
		bean.setOwner(trans.getCreator());//机动车所有人
		bean.setIDNum("");//证件号
		bean.setPlateType(trans.getPlateType());
		bean.setPlateNum(trans.getPlateNumber());
		bean.setCLSBDH(trans.getVin());
		bean.setShelvesNum(trans.getCode());
		bean.setVerifier(transition.getOperator());
		bean.setVerifytime(format.format(transition.getDateCreated()));
		bean.setBusName(trans.getBusinessName());
		list.add(bean);
		
		Callback callback = new Callback() {
			@Override
			public void onSuccessful() {
				generatePreview("reports/generates/"+loggedInUser.getUserUniqueId()+"/report.html","report.html");
				
				opener = new BrowserWindowOpener(PrintUI.class);
				opener.setFeatures("height=595,width=842,resizable");
				opener.extend(btnReady);
				opener.setParameter("htmlFilePath", "reports/generates/"+loggedInUser.getUserUniqueId()+"/report.html");
				btnReady.setEnabled(true);
			}
		};
		try {
			new TB4Reports().jasperToHtml(list, loggedInUser.getUserUniqueId(), "影像化档案审核合格证明书.jasper", callback);
			
		} catch (ReportException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @param fileName
	 */
	private void generatePreview(String filePath, String fileName) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
 			public InputStream getStream() {
 				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream(filePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return inputStream;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, fileName);
 		streamResource.setCacheTime(0);

 		if(fileName.endsWith("html")) {
 			BrowserFrame bf = new BrowserFrame(null);
 			bf.setSource(streamResource);
 			bf.setSizeFull();
 			previewPane.setContent(bf);
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
	private Panel previewPane = new Panel();
	
}
