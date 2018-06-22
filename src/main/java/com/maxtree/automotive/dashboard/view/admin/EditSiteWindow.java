package com.maxtree.automotive.dashboard.view.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.SiteCapacity;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

public class EditSiteWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EditSiteWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新站点");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSizeFull();
		
		textFieldName = new TextField("站点名称:");
		textFieldName.setIcon(VaadinIcons.EDIT);
		textFieldName.focus(); // 设置焦点
		
		// 站点类型
		boxSiteType = new ComboBox<String>("站点类型");
		boxSiteType.setTextInputAllowed(false);
		boxSiteType.setIcon(VaadinIcons.SITEMAP);
		boxSiteType.setEmptySelectionAllowed(false);
		boxSiteType.setItems(fileSystems);
//		boxSiteType.setValue(fileSystems[2]);//.setSelectedItem(fileSystems[2]);
//		tf1.setRequiredIndicatorVisible(true);
		
		textHostAddr = new TextField("主机地址:");
		textHostAddr.setIcon(VaadinIcons.EDIT);
		textPort = new TextField("端口:");
		textPort.setIcon(VaadinIcons.EDIT);
		textRemoteDirectory = new TextField("默认远程目录");
		textRemoteDirectory.setIcon(VaadinIcons.FOLDER);
		userName = new TextField("用户名:");
		userName.setIcon(VaadinIcons.USER);
		password = new PasswordField("密码");
		password.setIcon(VaadinIcons.PASSWORD);
		form.addComponents(textFieldName, boxSiteType, textHostAddr, textPort, textRemoteDirectory, userName, password);
		
		
		HorizontalLayout capacityHLayout = new HorizontalLayout();
		capacityHLayout.setWidthUndefined();
		capacityHLayout.setHeight("35px");
		capacityHLayout.setSpacing(false);
		capacityHLayout.setMargin(false);
		num.setWidth("60px");
		
		String[] unit = {"GB", "MB"};
		units.setWidth("100px");
		units.setTextInputAllowed(false);
		units.setEmptySelectionAllowed(false);
		units.setItems(unit);
		units.setSelectedItem("GB");
		
//		Image img = new Image();
//		img.setSource(VaadinIcons.ABACUS);
		Label titleLabel = new Label("最大容量:");
		titleLabel.setWidth("120px");
		capacityHLayout.addComponents(titleLabel, Box.createHorizontalBox(5), num, Box.createHorizontalBox(5), units, Box.createHorizontalBox(5), infoLabel);
		capacityHLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);
		capacityHLayout.setComponentAlignment(num, Alignment.MIDDLE_LEFT);
		capacityHLayout.setComponentAlignment(units, Alignment.MIDDLE_LEFT);
		capacityHLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		Button btnTest = new Button("测试");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("195px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnAdd, btnTest);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnTest, Alignment.BOTTOM_RIGHT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(form, capacityHLayout, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		btnTest.addClickListener(e -> {
			UI.getCurrent().access(() -> {
				boolean testConn = new TB4FileSystem().testConnection(site);
				if (testConn) {
					Notification notification = new Notification("测试：", "连接成功", Type.HUMANIZED_MESSAGE);
					notification.setDelayMsec(2000);
					notification.show(Page.getCurrent());
				} else {
					Notification.show("测试：","连接失败", Type.WARNING_MESSAGE);
				}
			});
		});
		
		setComponentSize(350, 27);
		
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		bindFields();
		
		// Validating Field Values
		validatingFieldValues();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		site.setSiteType("FTP");
		binder.setBean(site);
	}
	
	private void setComponentSize(int w, int h) {
		textFieldName.setWidth(w+"px");
		boxSiteType.setWidth(w+"px");
		textHostAddr.setWidth(w+"px");
		textPort.setWidth(w+"px");
		textRemoteDirectory.setWidth(w+"px");
		userName.setWidth(w+"px");
		password.setWidth(w+"px");
		
		textFieldName.setHeight(h+"px");
		boxSiteType.setHeight(h+"px");
		textHostAddr.setHeight(h+"px");
		textPort.setHeight(h+"px");
		textRemoteDirectory.setHeight(h+"px");
		userName.setHeight(h+"px");
		password.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(textFieldName, Site::getSiteName, Site::setSiteName);
		binder.bind(boxSiteType, Site::getSiteType, Site::setSiteType);
		binder.bind(textHostAddr, Site::getHostAddr, Site::setHostAddr);
		binder.forField(textPort)
		  .withValidator(new EmptyValidator("端口不能为空"))
		  .withConverter(new StringToIntegerConverter("Please enter a number"))
		  .bind(Site::getPort, Site::setPort);
		binder.bind(textRemoteDirectory, Site::getDefaultRemoteDirectory, Site::setDefaultRemoteDirectory);
		binder.bind(userName, Site::getUserName, Site::setUserName);
		binder.bind(password, Site::getPassword, Site::setPassword);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(textFieldName).withValidator(new StringLengthValidator(
	        "站点名称长度范围为2-20个字符",
	        2, 20))
	    .bind(Site::getSiteName, Site::setSiteName);
		
		binder.forField(textHostAddr).withValidator(new StringLengthValidator(
				 "主机地址长度范围为2-20个字符",
		        2, 20))
		    .bind(Site::getHostAddr, Site::setHostAddr);
		binder.forField(textRemoteDirectory).withValidator(new StringLengthValidator(
				 "默认远程目录长度范围为2-50个字符",
		        1, 50))
		    .bind(Site::getDefaultRemoteDirectory, Site::setDefaultRemoteDirectory);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(site.getSiteName())) {
			
			textFieldName.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "站点名称不能为空。";
				}
			});
			return false;
		} else if (StringUtils.isEmpty(site.getHostAddr())) {
			textHostAddr.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "主机地址不能为空。";
				}
			});
			return false;
		} else if (StringUtils.isEmpty(site.getDefaultRemoteDirectory())) {
			textRemoteDirectory.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "远程目录不能为空。";
				}
			});
			return false;
		}
		
		if (textFieldName.getErrorMessage() != null) {
			textFieldName.setComponentError(textFieldName.getErrorMessage());
			return false;
		} 
		else if (textHostAddr.getErrorMessage() != null) {
			textHostAddr.setComponentError(textHostAddr.getErrorMessage());
			return false;
		}
		else if (textRemoteDirectory.getErrorMessage() != null) {
			textRemoteDirectory.setComponentError(textRemoteDirectory.getErrorMessage());
			return false;
		}
		else if (StringUtils.isEmpty(num.getValue())) {
			num.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "容量大小不能为空。";
				}
			});
			return false;
		} else {
			
			try {
				new Double(num.getValue());
		    } catch (NumberFormatException e) {
		    	
		    	num.setComponentError(new ErrorMessage() {
					@Override
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.ERROR;
					}

					@Override
					public String getFormattedHtmlMessage() {
						return "请输入数字或小数点。";
					}
				});
		    	return false;
		    }
		}
		return true;
	}
	
	public static void open(Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditSiteWindow w = new EditSiteWindow();
        
        w.infoLabel.setValue("使用率: 0.00%");
        
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		
        		SiteCapacity siteCapacity = new SiteCapacity();
        		siteCapacity.setUnit(w.units.getValue());
        		siteCapacity.setUnitNumber(w.num.getValue());
        		siteCapacity.setUpdateTimeMillis(System.currentTimeMillis());
        		
        		long totalSize = 0L;
        		if (w.units.getValue().equals("GB")) {
        			totalSize = (long) (new Double(w.num.getValue()) * 1024L * 1024L * 1024L); 
        		} else if (w.units.getValue().equals("MB")) {
        			totalSize = (long) (new Double(w.num.getValue()) * 1024L * 1024L); 
        		}
        		siteCapacity.setCapacity(totalSize);
        		siteCapacity.setUsedSpace(0L);
        		w.site.setSiteCapacity(siteCapacity);
        		
        		ui.siteService.save(w.site);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public static void edit(Site site, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditSiteWindow w = new EditSiteWindow();
        
        double c = ((double)site.getSiteCapacity().getUsedSpace()/site.getSiteCapacity().getCapacity() * 100d);
        DecimalFormat dec = new DecimalFormat("0.00");
        String result = dec.format(c);
        
        w.infoLabel.setValue("使用率: "+result+"%");
        
        w.site.setSiteUniqueId(site.getSiteUniqueId());
        w.textFieldName.setValue(site.getSiteName());
        w.boxSiteType.setValue(site.getSiteType());
        w.textHostAddr.setValue(site.getHostAddr());
        w.textPort.setValue(site.getPort()+"");
        w.textRemoteDirectory.setValue(site.getDefaultRemoteDirectory());
        w.userName.setValue(site.getUserName());
        w.password.setValue(site.getPassword());
        w.units.setValue(site.getSiteCapacity().getUnit());
        w.num.setValue(site.getSiteCapacity().getUnitNumber());
        
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑站点");
        w.btnAdd.addClickListener(e -> {
        	
        	
        	UI.getCurrent().access(() -> {
        		
        		if (w.checkEmptyValues()) {
            		boolean testConnect = new TB4FileSystem().testConnection(site);
            		if (testConnect) {
            			w.site.setRunningStatus(site.getRunningStatus());
            		}
            		
            		SiteCapacity siteCapacity = site.getSiteCapacity();
            		siteCapacity.setUnit(w.units.getValue());
            		siteCapacity.setUnitNumber(w.num.getValue());
            		siteCapacity.setUpdateTimeMillis(System.currentTimeMillis());
            		long totalSize = 0L;
            		if (w.units.getValue().equals("GB")) {
            			totalSize = (long) (new Double(w.num.getValue()) * 1024L * 1024L * 1024L); 
            		} else if (w.units.getValue().equals("MB")) {
            			totalSize = (long) (new Double(w.num.getValue()) * 1024L * 1024L); 
            		}
            		siteCapacity.setCapacity(totalSize);
            		w.site.setSiteCapacity(siteCapacity);
            		
        			ui.siteService.update(w.site);
        			w.close();
        			callback.onSuccessful();
            	}
        	});
        	
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
//	public static String humanReadableByteCount(long bytes, boolean si) {
//	    int unit = si ? 1000 : 1024;
//	    if (bytes < unit) return bytes + " B";
//	    int exp = (int) (Math.log(bytes) / Math.log(unit));
//	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
//	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
//	}
	
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TextField userName;
	private PasswordField password;
	private TextField textRemoteDirectory;
	private TextField textPort;
	private TextField textHostAddr;
	private ComboBox<String> boxSiteType;
	private TextField textFieldName;
	private Button btnAdd;
	private Binder<Site> binder = new Binder<>();
	private Site site = new Site();
	private static final String[] fileSystems = {"BZIP2", "File", "FTP", "FTPS", "GZIP", "HDFS", "HTTP", "HTTPS", "Jar", "RAM", "RES", "SFTP", "Tar", "Temp", "WebDAV", "Zip", "CIFS", "mime"};//Supported File Systems
	private ComboBox<String> units = new ComboBox<>();
	private Label infoLabel = new Label();
	private DoubleField num = new DoubleField();
}
