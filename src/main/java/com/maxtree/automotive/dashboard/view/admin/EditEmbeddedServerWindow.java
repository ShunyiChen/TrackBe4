package com.maxtree.automotive.dashboard.view.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
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
		this.setWidth("370px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加服务器");
 
		// 服务器名
		FormLayout form = new FormLayout();
		form.setSizeFull();
		serverNameField = new TextField("服务器名:");
		serverNameField.setIcon(VaadinIcons.NOTEBOOK);
		serverNameField.focus();
		// 服务器类型
		serverTypeBox = new ComboBox<String>("服务器类型:");
		serverTypeBox.setEmptySelectionAllowed(false);
		serverTypeBox.setTextInputAllowed(false);
		serverTypeBox.setIcon(VaadinIcons.SERVER);
		serverTypeBox.setItems(serverTypes);
		//端口
		portField = new TextField("端口:");
		portField.setIcon(VaadinIcons.AIRPLANE);
		//编码
		codeField = new TextField("编码:");
		codeField.setIcon(VaadinIcons.CODE);
		//ftpserver配置模板
		templatesBox.setCaption("配置模板:");
		templatesBox.setTextInputAllowed(false);
		templatesBox.setEmptySelectionAllowed(false);
		templatesBox.setIcon(VaadinIcons.AUTOMATION);
		templatesBox.setItems(templates);
		form.addComponents(serverNameField,codeField,serverTypeBox,templatesBox,portField);
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setWidthUndefined();
		subButtonPane.setHeight("40px");
		subButtonPane.addComponents(btnCancel,Box.createHorizontalBox(5),btnAdd,Box.createHorizontalBox(5));
		subButtonPane.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		main.addComponents(form,buttonPane);
		main.setComponentAlignment(form, Alignment.TOP_CENTER);
		main.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
		this.setContent(main);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		setComponentSize(230, 27);
		
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
		portField.setWidth(w+"px");
		codeField.setWidth(w+"px");
		templatesBox.setWidth(w+"px");
		serverNameField.setHeight(h+"px");
		serverTypeBox.setHeight(h+"px");
		portField.setHeight(h+"px");
		codeField.setHeight(h+"px");
		templatesBox.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(serverNameField, EmbeddedServer::getServerName, EmbeddedServer::setServerName);
		binder.bind(serverTypeBox, EmbeddedServer::getServerType, EmbeddedServer::setServerType);
		binder.forField(portField)
		  .withValidator(new EmptyValidator("端口不能为空"))
		  .withConverter(new StringToIntegerConverter("请输入一个大于零的整数"))
		  .bind(EmbeddedServer::getPort, EmbeddedServer::setPort);
		binder.forField(codeField).withValidator(new StringLengthValidator(
		        "代码长度为7",
		        1, 7))
		    .bind(EmbeddedServer::getCode, EmbeddedServer::setCode);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(serverNameField).withValidator(new StringLengthValidator(
	        "服务器名长度范围为1-60个字符",
	        1, 60))
	    .bind(EmbeddedServer::getServerName, EmbeddedServer::setServerName);
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
		return true;
	}
	
	/**
	 * 
	 * @param serverUniqueId
	 * @throws IOException 
	 */
	private void generateServerXML(int serverUniqueId) throws IOException {
		//生成服务器配置
		FileInputStream in = new FileInputStream("configuration/ftpserver/template/"+templatesBox.getValue());
		BufferedReader br = new BufferedReader(new UnicodeReader(in));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			if (line.contains("{id}")) {
				line = line.replace("{id}", "myserver"+serverUniqueId);
			}
			else if(line.contains("{port}")) {
				line = line.replace("{port}", ""+embeddedServer.getPort());
			}
			else if(line.contains("{userProperties}")) {
				line = line.replace("{userProperties}", "myserver"+serverUniqueId+"_users.properties");
			}
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		String content = sb.toString();
		br.close();
		in.close();
		//写入到custom文件夹下
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("configuration/ftpserver/custom/myserver" + serverUniqueId + ".xml")), "utf-8");
		oStreamWriter.append(content);
		oStreamWriter.close();
	}
	
	
	private void generateUserProperties(int serverUniqueId) throws IOException {
		//生成服务器配置
		FileInputStream in = new FileInputStream("configuration/ftpserver/template/users.properties");
		BufferedReader br = new BufferedReader(new UnicodeReader(in));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			if (line.contains("{id}")) {
				line = line.replace("{id}", "myserver"+serverUniqueId);
			}
			else if(line.contains("{port}")) {
				line = line.replace("{port}", ""+embeddedServer.getPort());
			}
			else if(line.contains("{userProperties}")) {
				line = line.replace("{userProperties}", "myserver"+serverUniqueId+"_users.properties");
			}
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		String content = sb.toString();
		br.close();
		in.close();
		//写入到custom文件夹下
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("configuration/ftpserver/custom/myserver" + serverUniqueId + ".xml")), "utf-8");
		oStreamWriter.append(content);
		oStreamWriter.close();
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	private int insert() throws IOException {
		int serverUniqueId = ui.embeddedServerService.insert(embeddedServer);
		generateServerXML(serverUniqueId);
		
		return serverUniqueId;
	}
	
	/**
	 * 
	 */
	private void update() {
		ui.embeddedServerService.update(embeddedServer);
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(Callback2 callback) {
		EditEmbeddedServerWindow w = new EditEmbeddedServerWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
        		int serverUniqueId = 0;
				try {
					serverUniqueId = w.insert();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
        EditEmbeddedServerWindow w = new EditEmbeddedServerWindow();
        w.embeddedServer.setServerUniqueId(embeddedServer.getServerUniqueId());
        w.serverNameField.setValue(embeddedServer.getServerName()==null?"":embeddedServer.getServerName());
        w.serverTypeBox.setValue(embeddedServer.getServerType());
        w.portField.setValue(embeddedServer.getPort()==null?"":embeddedServer.getPort()+"");
        w.codeField.setValue(embeddedServer.getCode());
        w.codeField.setEnabled(false);
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑服务器");
        w.btnAdd.addClickListener(e -> {
        	
        	UI.getCurrent().access(() -> {
        		
        		if (w.checkEmptyValues()) {
//            		boolean testConnect = new TB4FileSystem().testConnection(embeddedServer);
//            		if (testConnect) {
//            			w.embeddedServer.setRunningStatus(embeddedServer.getRunningStatus());
//            		}
            		 
        			w.update();
        			w.close();
        			callback.onSuccessful();
            	}
        	});
        	
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private static final String[] serverTypes = {"FTP","FTPS"};//Supported server types
	private static final String[] templates = {"ftpd-full.xml","ftpd-typical.xml"};
	private ComboBox<String> serverTypeBox = new ComboBox<String>();
	private ComboBox<String> templatesBox = new ComboBox<String>();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private VerticalLayout main = new VerticalLayout(); 
	private TextField serverNameField = new TextField();
	private TextField portField = new TextField();
	private TextField codeField = new TextField();
	private Button btnAdd = new Button("添加");
	private Binder<EmbeddedServer> binder = new Binder<>();
	private EmbeddedServer embeddedServer = new EmbeddedServer();
}
