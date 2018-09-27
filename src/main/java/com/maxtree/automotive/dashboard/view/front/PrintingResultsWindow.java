package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
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
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

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
		this.setWidthUndefined();
		this.setHeight("150px");
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		this.addCloseListener(e->{
			closableEvent.onSuccessful();
		});
		
		List<String> options = Arrays.asList("打印审核结果单");
		radios = new RadioButtonGroup<>(null, options);
		radios.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnOk.setEnabled(false);
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
    	btnCancel.addClickListener(e->{
    		close();
    	});
    	radios.addValueChangeListener(e->{
    		generateReport();
    	});
    	
    	this.addCloseListener(e -> {
    		deleteReportFiles();
    	});
    	
    	radios.setSelectedItem(options.get(0));
	}
	
	private void generateReport() {
		List<PrintableBean> list = new ArrayList<PrintableBean>();
		PrintableBean bean = new PrintableBean();
//		if(transition.getComments()) {
//			
//		}
//		
//		
//		bean.setDateCreated(format.format(trans.getDateCreated()));
//		bean.setPlateType(trans.getPlateType());
//		bean.setPlateNumber(trans.getPlateNumber());
//		bean.setVin(trans.getVin());
//		
//		StringBuilder info = new StringBuilder();
//		info.append("号码种类:"+trans.getPlateType()+"\n");
//		info.append("号码号牌:"+trans.getPlateNumber()+"\n");
//		info.append("车辆识别代码:"+trans.getVin()+"\n");
//		bean.setBasicInformation(info.toString());// 基本信息
//		
//		Transition transition = ui.transitionService.findByUUID(trans.getUuid(), trans.getVin());
//		Map<String, String> map = jsonHelper.json2Map(transition.getDetails());
////		Iterator<String> iter = map.keySet().iterator();
//		StringBuilder details = new StringBuilder();
//		details.append(transition.getComments());
//		
//		bean.setObjection(details.toString());
//		bean.setChecker(transition.getUserName());
//		bean.setDateChecked(format.format(transition.getDateUpdated()));
//		
//		list.add(bean);
//		
//		Callback callback = new Callback() {
//			@Override
//			public void onSuccessful() {
//				opener = new BrowserWindowOpener(PrintUI.class);
//				opener.setFeatures("height=595,width=842,resizable");
//				opener.extend(btnOk);
//				opener.setParameter("htmlFilePath", "reports/generates/"+trans.getTransactionUniqueId()+"/report.html");
//				btnOk.setEnabled(true);
//			}
//		};
//		try {
//			new TB4Reports().jasperToHtml(list, trans.getTransactionUniqueId(), "report1.jasper", callback);
//			
//		} catch (ReportException e1) {
//			e1.printStackTrace();
//		}
	}
	
	/**
	 * 
	 */
	private void deleteReportFiles() {
		new TB4Reports().deleteReportFiles("reports/generates/" + trans.getTransactionUniqueId());
	}
	
	/**
	 * 
	 * @param caption
	 * @param trans
	 */
	public static void open(String caption, Transaction trans, Callback closableEvent) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        PrintingResultsWindow w = new PrintingResultsWindow(caption, trans);
        w.closableEvent = closableEvent;
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private User loggedInUser;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Button btnCancel = new Button("取消");
	private Button btnOk = new Button("确定");
	private RadioButtonGroup<String> radios;
	private String caption;
	private BrowserWindowOpener opener;
	private HorizontalLayout buttonLayout = new HorizontalLayout();
	private Transaction trans;
	private Callback closableEvent;
	private MessageBodyParser jsonHelper = new MessageBodyParser();
}
