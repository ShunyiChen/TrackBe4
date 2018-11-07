package com.maxtree.automotive.dashboard.component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.maxtree.automotive.dashboard.view.front.PrintingFiletagsWindow;
import com.maxtree.automotive.dashboard.view.front.PrintingResultsWindow;
import com.maxtree.trackbe4.messagingsystem.MatedataJsonParser;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
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
	 * @param inputView
	 */
	public Openwith(Message message, InputViewIF inputView) {
		this.message = message;
		this.inputView = inputView;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("打开通知");
		this.setWidth("621px");
		this.setHeight("322px");
		this.setResizable(false);
		this.setModal(true);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Image img = new Image(null, new ThemeResource("img/adminmenu/info-circle-o.png"));
		labelSubject.setValue(message.getSubject());
		labelTime.setValue("发送时间:"+format.format(message.getDateCreated()));
		labelContent.setValue(message.getContent());
		labelSubject.addStyleName("Openwith_labelSubject");
		labelTime.addStyleName("Openwith_labelTime");
		
		HorizontalLayout subjectlayout = new HorizontalLayout();
		subjectlayout.setWidthUndefined();
		subjectlayout.setHeight("100%");
		subjectlayout.addComponents(img, labelSubject);
		subjectlayout.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		subjectlayout.setComponentAlignment(labelSubject, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout timelayout = new HorizontalLayout();
		timelayout.setSizeFull();
		timelayout.addComponent(labelTime);
		
		Panel scrollPanel = new Panel();
		scrollPanel.setWidth("98%");
		scrollPanel.setHeight("120px");
		HorizontalLayout contentlayout = new HorizontalLayout();
		contentlayout.setSizeFull();
		contentlayout.addComponent(labelContent);
		scrollPanel.setContent(contentlayout);
		
		
		HorizontalLayout footerLayout = new HorizontalLayout();
//		close.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		edit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		print.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		edit.setVisible(false);
		print.setVisible(false);
		
		footerLayout.setWidthUndefined();
		footerLayout.setHeight("30px");
		footerLayout.setSpacing(false);
		footerLayout.addComponents(edit,Box.createHorizontalBox(5),print,Box.createHorizontalBox(5),close);
		footerLayout.setComponentAlignment(edit, Alignment.MIDDLE_LEFT);
		footerLayout.setComponentAlignment(print, Alignment.MIDDLE_LEFT);
		footerLayout.setComponentAlignment(close, Alignment.MIDDLE_LEFT);

		main.addComponents(subjectlayout,timelayout,scrollPanel,footerLayout);
		main.setComponentAlignment(subjectlayout, Alignment.TOP_LEFT);
		main.setComponentAlignment(scrollPanel, Alignment.TOP_CENTER);
		main.setComponentAlignment(footerLayout, Alignment.BOTTOM_RIGHT);
		main.setExpandRatio(subjectlayout, 0);
		main.setExpandRatio(scrollPanel, 1);
		main.setExpandRatio(footerLayout, 0);
		this.setContent(main);
		if(!StringUtils.isEmpty(message.getMatedata())) {
			Map<String, String> matedata = parser.json2Map(message.getMatedata());
			String uuid = matedata.get("UUID");
			String vin = matedata.get("VIN");
			String state = matedata.get("STATE");
			String checkLevel = matedata.get("CHECKLEVEL");
			String popupAutomatically = matedata.get("POPUPAUTOMATICALLY");
			trans = ui.transactionService.findByUUID(uuid, vin);
			
			if(checkLevel.contains("注册登记")) {
				if(state.equals(ui.state().getName("B15"))) {
					edit.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B2"))) {
					print.setVisible(true);
				}
			}
			else if(checkLevel.equals("无")) {
				if(state.equals(ui.state().getName("B15"))) {
					edit.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B2"))) {
					print.setVisible(true);
				}
			}
			else if(checkLevel.equals("一级审档")) {
				if(state.equals(ui.state().getName("B15"))) {
					edit.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B16"))) {
					edit.setVisible(true);
					print.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B14"))) {
					print.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B17"))) {
					edit.setVisible(true);
					print.setVisible(true);
				}
			}
			else if(checkLevel.equals("二级审档")) {
				if(state.equals(ui.state().getName("B15"))) {
					edit.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B17"))) {
					edit.setVisible(true);
					print.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B5"))) {
					print.setVisible(true);
				}
				else if(state.equals(ui.state().getName("B14"))) {
					print.setVisible(true);
				}
			}
		}
		
		edit.addClickListener(e->{
			Callback closebySelf = new Callback() {

				@Override
				public void onSuccessful() {
					close();
					if(closeNotificationsManagementWindow != null) {
						closeNotificationsManagementWindow.onSuccessful();
					}
				}
			};
			inputView.openTransaction(trans, message.getMessageUniqueId(), closebySelf);
		});
		print.addClickListener(e->{
			
			close();
			
  	    	// 注册登记业务
  	    	if(trans.getBusinessName().contains("注册登记")) {
  	    		List<String> options = Arrays.asList("车辆标签", "文件标签");
  	    		PrintingFiletagsWindow.open("车辆和文件标签-打印预览",trans,options);
  	    	}
  	    	else {
  	    		Business bus = ui.businessService.findByCode(trans.getBusinessCode());
  	    		if(bus.getCheckLevel().equals("无")) {
  	    			// 非审档业务
  	    			List<String> options = Arrays.asList("文件标签");
  	    			PrintingFiletagsWindow.open("文件标签-打印预览",trans,options);
  	    		}
  	    		else if(bus.getCheckLevel().equals("一级审档")
  	    				|| bus.getCheckLevel().equals("二级审档")) {//一级审档
      	    		Callback callback = new Callback() {
						@Override
						public void onSuccessful() {
						}
					};
  	    			PrintingResultsWindow.open("审核结果单-打印预览", trans, callback);
  	    		}
  	    	}
		});
		close.addClickListener(e->{
			close();
		});
		
	}
	
	/**
	 * 
	 * @param message
	 * @param inputView
	 */
	public static void open(Message message, InputViewIF inputView) {
		open(message, inputView, null);
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void open(Message message, InputViewIF inputView, Callback closeNotificationsManagementWindow) {
		Openwith ow = new Openwith(message,inputView);
		ow.closeNotificationsManagementWindow = closeNotificationsManagementWindow;
		UI.getCurrent().addWindow(ow);
		ow.center();
	}
	
	
	private Callback closeNotificationsManagementWindow;
	private MatedataJsonParser parser = new MatedataJsonParser();
	private Label labelSubject = new Label("");
	private Label labelTime = new Label("");
	private Label labelContent = new Label("");
	private Button edit = new Button("编辑");
	private Button print = new Button("打印");
	private Button close = new Button("关闭");
	private VerticalLayout main = new VerticalLayout();
	private Message message;
	private Transaction trans = null;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private User loggedInUser;
	private InputViewIF inputView;
}
