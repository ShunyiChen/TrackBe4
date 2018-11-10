package com.maxtree.automotive.dashboard.view.front;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		this.setSizeFull();
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		radios = new RadioButtonGroup<>(null, options);
		radios.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		HorizontalLayout optionsLayout = new HorizontalLayout();
		optionsLayout.addComponents(Box.createHorizontalBox(10), radios);
		optionsLayout.setComponentAlignment(radios, Alignment.MIDDLE_LEFT);
		
		previewPane.setSizeFull();
		
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
    	main.addComponents(optionsLayout,previewPane,footer);
    	main.setComponentAlignment(optionsLayout, Alignment.MIDDLE_CENTER);
    	main.setComponentAlignment(previewPane, Alignment.TOP_CENTER);
    	main.setComponentAlignment(footer, Alignment.TOP_RIGHT);
    	
    	main.setExpandRatio(optionsLayout, 0.1f);
    	main.setExpandRatio(previewPane, 0.8f);
    	main.setExpandRatio(footer, 0.1f);
    	this.setContent(main);
    	
    	
    	radios.addValueChangeListener(e->{
    		btnOk.setEnabled(false);
    		
    		if (opener != null) {
    			Collection<Extension> con = opener.getParent().getExtensions();
    			if(con.contains(opener)) {
    				opener.remove();
    			}
    			
    			footer.removeComponent(btnOk);
    			// re-add button
    			btnOk = new Button("准备打印");
    			btnOk.setEnabled(false);
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
    			
    			Callback callback = new Callback() {
					@Override
					public void onSuccessful() {
						
						generatePreview("reports/generates/"+loggedInUser.getUserUniqueId()+"/report.png","report.png");
						
						btnOk.setEnabled(true);
						opener = new BrowserWindowOpener(PrintUI.class);
						opener.setFeatures("height=306,width=422,x=0,y=0,resizable");
						opener.extend(btnOk);
						opener.setParameter("htmlFilePath", "reports/generates/"+loggedInUser.getUserUniqueId()+"/report.png");
						
						// Update status
						ui.transactionService.updateStatus(trans.getVin(), trans.getUuid(),ui.state().getName("B19"));
					}
    			};
    			try {
					new TB4Reports().jasperToPNG(list, loggedInUser.getUserUniqueId(), "上架标签-车.jasper", callback);
					
				} catch (ReportException e1) {
					e1.printStackTrace();
				}
    			
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
    			
    			Callback callback = new Callback() {
					@Override
					public void onSuccessful() {
						
						generatePreview("reports/generates/"+loggedInUser.getUserUniqueId()+"/report.png","report.png");
						
						btnOk.setEnabled(true);
						opener = new BrowserWindowOpener(PrintUI.class);
						opener.setFeatures("height=306,width=422,x=0,y=0,resizable");
						opener.extend(btnOk);
						opener.setParameter("htmlFilePath", "reports/generates/"+loggedInUser.getUserUniqueId()+"/report.png");
						
						// Update status
						ui.transactionService.updateStatus(trans.getVin(), trans.getUuid(), ui.state().getName("B19"));
					}
    			};
    			try {
					new TB4Reports().jasperToPNG(list, loggedInUser.getUserUniqueId(), "上架标签.jasper", callback);
					
				} catch (ReportException e1) {
					e1.printStackTrace();
				}
    		}
    		
    	});
    	btnCancel.addClickListener(e->{
    		deleteReportFiles();
    		
    		close();
    	});
    	// 窗体关闭后自动删除报表文件
    	this.addCloseListener(e -> {
    		deleteReportFiles();
    	});
    	
    	radios.setSelectedItem(options.get(0));
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

 		if(fileName.endsWith("png")) {
 			Image image = new Image(null,streamResource);
 			VerticalLayout viewPort = new VerticalLayout();
 			viewPort.setSpacing(false);
 			viewPort.setMargin(false);
 			viewPort.setSizeFull();
 			viewPort.addComponents(image);
 			viewPort.setComponentAlignment(image, Alignment.TOP_CENTER);
 			previewPane.setContent(viewPort);
 		}
 		else if(fileName.endsWith("html")) {
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
	private Panel previewPane = new Panel();
}
