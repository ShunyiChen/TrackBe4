package com.maxtree.automotive.dashboard.view.admin;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.EmptyValidator;
import com.maxtree.automotive.dashboard.domain.EmbeddedServer;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author chens
 *
 */
public class EditEmbeddedServerWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EditEmbeddedServerWindow() {
		this.setWidth("513px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加服务器");
//		this.addStyleName("edit-window");
		main.setSpacing(true);
		main.setMargin(false);
		
		// 服务器名
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		serverNameField = new TextField("服务器名:");
		serverNameField.setIcon(VaadinIcons.NOTEBOOK);
		serverNameField.focus();
		// 服务器类型
		serverTypeBox = new ComboBox<String>("服务器类型:");
		serverTypeBox.setTextInputAllowed(false);
		serverTypeBox.setIcon(VaadinIcons.SERVER);
		serverTypeBox.setEmptySelectionAllowed(false);
		serverTypeBox.setItems(serverTypes);
		// 主机地址
		hostAddrField = new TextField("主机地址:");
		hostAddrField.setIcon(VaadinIcons.EDIT);
		//端口
		portField = new TextField("端口:");
		portField.setIcon(VaadinIcons.AIRPLANE);
		// 默认远程目录
		defaultRemoteDirectoryField = new TextField("默认远程目录:");
		defaultRemoteDirectoryField.setIcon(VaadinIcons.FOLDER);
		//用户名
		userNameField = new TextField("用户名:");
		userNameField.setIcon(VaadinIcons.USER);
		//密码
		passwordField = new PasswordField("密码");
		passwordField.setIcon(VaadinIcons.PASSWORD);
		//编码
		codeField = new TextField("编码:");
		codeField.setIcon(VaadinIcons.CODE);
		form.addComponents(serverNameField,serverTypeBox,hostAddrField,portField,defaultRemoteDirectoryField,userNameField,passwordField,codeField);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
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
		
		main.addComponents(form,buttonPane);
		this.setContent(main);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		btnTest.addClickListener(e -> {
//			UI.getCurrent().access(() -> {
//				boolean testConn = new TB4FileSystem().testConnection(embeddedServer);
//				if (testConn) {
//					Notification notification = new Notification("测试：", "连接成功", Type.HUMANIZED_MESSAGE);
//					notification.setDelayMsec(2000);
//					notification.show(Page.getCurrent());
//				} else {
//					Notification.show("测试：","连接失败", Type.WARNING_MESSAGE);
//				}
//			});
		});
		
		setComponentSize(330, 27);
		
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		bindFields();
		
		// Validating Field Values
		validatingFieldValues();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(embeddedServer);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		serverNameField.setWidth(w+"px");
		serverTypeBox.setWidth(w+"px");
		hostAddrField.setWidth(w+"px");
		portField.setWidth(w+"px");
		defaultRemoteDirectoryField.setWidth(w+"px");
		userNameField.setWidth(w+"px");
		passwordField.setWidth(w+"px");
		codeField.setWidth(w+"px");
		
		serverNameField.setHeight(h+"px");
		serverTypeBox.setHeight(h+"px");
		hostAddrField.setHeight(h+"px");
		portField.setHeight(h+"px");
		defaultRemoteDirectoryField.setHeight(h+"px");
		userNameField.setHeight(h+"px");
		passwordField.setHeight(h+"px");
		codeField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(serverNameField, EmbeddedServer::getServerName, EmbeddedServer::setServerName);
		binder.bind(serverTypeBox, EmbeddedServer::getServerType, EmbeddedServer::setServerType);
		binder.bind(hostAddrField, EmbeddedServer::getHostAddr, EmbeddedServer::setHostAddr);
		binder.forField(portField)
		  .withValidator(new EmptyValidator("端口不能为空"))
		  .withConverter(new StringToIntegerConverter("请输入一个大于零的整数"))
		  .bind(EmbeddedServer::getPort, EmbeddedServer::setPort);
		binder.bind(defaultRemoteDirectoryField, EmbeddedServer::getDefaultRemoteDirectory, EmbeddedServer::setDefaultRemoteDirectory);
		binder.bind(userNameField, EmbeddedServer::getUserName, EmbeddedServer::setUserName);
		binder.bind(passwordField, EmbeddedServer::getPassword, EmbeddedServer::setPassword);
		binder.forField(codeField).withValidator(new StringLengthValidator(
		        "代码长度为4",
		        4, 4))
		    .bind(EmbeddedServer::getCode, EmbeddedServer::setCode);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(serverNameField).withValidator(new StringLengthValidator(
	        "服务器名长度范围为2-20个字符",
	        2, 20))
	    .bind(EmbeddedServer::getServerName, EmbeddedServer::setServerName);
		binder.forField(hostAddrField).withValidator(new StringLengthValidator(
				 "主机地址长度范围为2-20个字符",
		        2, 20))
		    .bind(EmbeddedServer::getHostAddr, EmbeddedServer::setHostAddr);
		binder.forField(defaultRemoteDirectoryField).withValidator(new StringLengthValidator(
				 "默认远程目录长度范围为2-50个字符",
		        1, 50))
		    .bind(EmbeddedServer::getDefaultRemoteDirectory, EmbeddedServer::setDefaultRemoteDirectory);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(embeddedServer.getServerName())) {
			serverNameField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "服务器名称不能为空。";
				}
			});
			return false;
		} else if (StringUtils.isEmpty(embeddedServer.getHostAddr())) {
			hostAddrField.setComponentError(new ErrorMessage() {
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
		} else if (StringUtils.isEmpty(embeddedServer.getDefaultRemoteDirectory())) {
			defaultRemoteDirectoryField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "主机目录不能为空。";
				}
			});
			return false;
		}
		else if (StringUtils.isEmpty(embeddedServer.getCode())) {
			codeField.setComponentError(new ErrorMessage() {
				@Override
				public ErrorLevel getErrorLevel() {
					return ErrorLevel.ERROR;
				}

				@Override
				public String getFormattedHtmlMessage() {
					return "服务器编码不能为空。";
				}
			});
			return false;
		}
		if (codeField.getErrorMessage() != null) {
			codeField.setComponentError(codeField.getErrorMessage());
			return false;
		} 
		if (serverNameField.getErrorMessage() != null) {
			serverNameField.setComponentError(serverNameField.getErrorMessage());
			return false;
		} 
		else if (hostAddrField.getErrorMessage() != null) {
			hostAddrField.setComponentError(hostAddrField.getErrorMessage());
			return false;
		}
		else if (defaultRemoteDirectoryField.getErrorMessage() != null) {
			defaultRemoteDirectoryField.setComponentError(defaultRemoteDirectoryField.getErrorMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(Callback2 callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		EditEmbeddedServerWindow w = new EditEmbeddedServerWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		int serverUniqueId = ui.embeddedServerService.insert(w.embeddedServer);
    			w.close();
    			callback.onSuccessful(serverUniqueId);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param embeddedServer
	 * @param callback
	 */
	public static void edit(EmbeddedServer embeddedServer, Callback callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditEmbeddedServerWindow w = new EditEmbeddedServerWindow();
        w.embeddedServer.setServerUniqueId(embeddedServer.getServerUniqueId());
        w.serverNameField.setValue(embeddedServer.getServerName()==null?"":embeddedServer.getServerName());
        w.serverTypeBox.setValue(embeddedServer.getServerType());
        w.hostAddrField.setValue(embeddedServer.getHostAddr()==null?"":embeddedServer.getHostAddr());
        w.portField.setValue(embeddedServer.getPort()==null?"":embeddedServer.getPort()+"");
        w.defaultRemoteDirectoryField.setValue(embeddedServer.getDefaultRemoteDirectory()==null?"":embeddedServer.getDefaultRemoteDirectory());
        w.userNameField.setValue(embeddedServer.getUserName()==null?"":embeddedServer.getUserName());
        w.passwordField.setValue(embeddedServer.getPassword()==null?"":embeddedServer.getPassword());
        w.codeField.setValue(embeddedServer.getCode());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑服务器");
        w.btnAdd.addClickListener(e -> {
        	
        	UI.getCurrent().access(() -> {
        		
        		if (w.checkEmptyValues()) {
//            		boolean testConnect = new TB4FileSystem().testConnection(embeddedServer);
//            		if (testConnect) {
//            			w.embeddedServer.setRunningStatus(embeddedServer.getRunningStatus());
//            		}
//            		 
//            		
//        			ui.embeddedServerService.update(w.embeddedServer);
//        			w.close();
//        			callback.onSuccessful();
            	}
        	});
        	
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private static final String[] serverTypes = {"FTP","SSH"};//Supported server types
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout(); 
	private TextField serverNameField = new TextField();
	private ComboBox<String> serverTypeBox = new ComboBox<String>();
	private TextField hostAddrField = new TextField();
	private TextField portField = new TextField();
	private TextField defaultRemoteDirectoryField = new TextField();
	private TextField userNameField = new TextField();
	private PasswordField passwordField = new PasswordField();
	private TextField codeField = new TextField();
	private Button btnAdd = new Button("添加");
	private Binder<EmbeddedServer> binder = new Binder<>();
	private EmbeddedServer embeddedServer = new EmbeddedServer();
}
