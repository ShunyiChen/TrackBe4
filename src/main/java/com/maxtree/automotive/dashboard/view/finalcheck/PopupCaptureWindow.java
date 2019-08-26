package com.maxtree.automotive.dashboard.view.finalcheck;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.*;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.maxtree.automotive.dashboard.servlet.CaptureServlet;
import com.maxtree.automotive.dashboard.servlet.UploadInDTO;
import com.maxtree.automotive.dashboard.servlet.UploadOutDTO;
import com.vaadin.event.UIEvents;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.*;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.*;
import java.util.List;

/**
 * 
 * @author Chen
 *
 */
public class PopupCaptureWindow extends Window implements CloseListener, Receiver, SucceededListener, ProgressListener, StartedListener, FailedListener, FinishedListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param trans
	 */
	public PopupCaptureWindow(Transaction trans) {
		this.trans = trans;
		initComponents();
	}
	
	private void initComponents() {
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
		this.setCaption("拍摄");
		this.setWidth("1024px");
		this.setHeight("768px");
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(false);
		this.setCaption("拍照");

		main.setMargin(false);
		main.setSpacing(false);
		main.setSizeFull();
		link.addStyleName(ValoTheme.BUTTON_LINK);
//		settings = ui.settingsService.findByName("高拍仪");
//		if("无".equals(settings.getValue())) {
//			Upload upload = new Upload(null, this);
//			upload.setButtonCaption("选择文件");
//			upload.setButtonStyleName("upload-button");
//			upload.setImmediateMode(true);
//			upload.addSucceededListener(this);
////			this.setContent(upload);
//
//			main.addComponent(upload);
//		}
//		else {
//			browser.setSizeFull();
////			this.setContent(browser);
//			main.addComponent(browser);
//			main.setComponentAlignment(browser, Alignment.TOP_CENTER);
//			main.setExpandRatio(browser, 1);
//		}
		
		this.setContent(main);
		this.addCloseListener(this);
		
		startPolling();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void generateNewHTML() throws IOException {
		// 读取原来的html模板
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		User loggedInUser = ui.userService.getUserByUserName(username);
		String everything = "";
		File template = null;
//		if(settings.getValue().equals("无锡华通H6-1")) {
//			template = new File("devices/templates/HtmlDemo3.html");//无锡华通H6-1
//		}
//		else {
//			template = new File("devices/templates/Sample_CamOCX_HTML_Device_IE.html");//维山VSA305FD
//		}
//		File template = new File("devices/templates/TempHtml.html"); // 选择本地图片上传
		FileInputStream in = new FileInputStream(template);
		BufferedReader br = new BufferedReader(new UnicodeReader(in));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			if (line.contains("var userUniqueId = \"\";")) {
				line = line.replace("var userUniqueId = \"\";", "var userUniqueId = \""+loggedInUser.getUserUniqueId()+"\";");
			}
			
			if (line.contains("hello")) {
				line = line.replace("hello", "capture");
			}
			// System.out.println(line);
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		everything = sb.toString();
		br.close();
		in.close();
		
		// 动态生成新的html
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("devices/capture/" + loggedInUser.getUserUniqueId() + ".html")), "utf-8");
		oStreamWriter.append(everything);
		oStreamWriter.close();
	}
	
	/**
	 * 显示影像
	 */
	public void displayCamera() {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
 			public InputStream getStream() {
				
				File generatedFile = new File("devices/capture/"+loggedInUser.getUserUniqueId()+".html");
				if(!generatedFile.exists()) {
					try {
						generateNewHTML();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
 				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream("devices/capture/"+loggedInUser.getUserUniqueId()+".html");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return inputStream;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, loggedInUser.getUserUniqueId()+".html");
 		streamResource.setCacheTime(0);
		browser.setSource(streamResource);
	}
	
	@Override
	public void uploadFinished(FinishedEvent event) {
	}

	@Override
	public void uploadFailed(FailedEvent event) {
	}

	@Override
	public void uploadStarted(StartedEvent event) {
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		return null;
	}
	
	@Override
	public void windowClose(CloseEvent e) {
	}
	
	private void startPolling() {
		CaptureServlet.OUT_DTOs.put(loggedInUser.getUserUniqueId(), null);
		
		SystemConfiguration sc = Yaml.readSystemConfiguration();
		ui.setPollInterval(sc.getInterval());
		
		
	    UIEvents.PollListener listener = new UIEvents.PollListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void poll(UIEvents.PollEvent event) {
				UploadOutDTO dto = CaptureServlet.OUT_DTOs.get(loggedInUser.getUserUniqueId());
				if(dto != null) {
					
					callback.onSuccessful();
					
					close();
					
					ui.removePollListener(this);
				}
			}
		};
		
		ui.addPollListener(listener);
	}
	
	/**
	 * 
	 */
	public static void open(Transaction trans, DataDictionary dd, Callback2 callback) {
		PopupCaptureWindow w = new PopupCaptureWindow(trans);
		UI.getCurrent().addWindow(w);
		w.center();
		w.displayCamera();
		w.callback = callback;
		
//		w.p.setBatch(trans.getBatch()+"");
		w.p.setDictionaryCode(dd.getCode());
		w.p.setSiteUniqueId(trans.getSiteUniqueId());
		w.p.setUserUniqueId(w.loggedInUser.getUserUniqueId());
//		w.p.setUuid(trans.getUuid());
		w.p.setVin(trans.getVin());
		CaptureServlet.IN_DTOs.put(w.loggedInUser.getUserUniqueId(), w.p);
	}
	
	/**
	 * 
	 * @param trans
	 * @param doc
	 * @param callback
	 */
	public static void edit(Transaction trans, Document doc, Callback2 callback) {
		PopupCaptureWindow w = new PopupCaptureWindow(trans);
		UI.getCurrent().addWindow(w);
		w.center();
		w.displayCamera();
		w.callback = callback;
 
		w.p.setDocumentUniqueId(doc.getDocumentUniqueId());
//		w.p.setBatch(trans.getBatch()+"");
		w.p.setDictionaryCode(doc.getDictionarycode());
		w.p.setSiteUniqueId(trans.getSiteUniqueId());
		w.p.setUserUniqueId(w.loggedInUser.getUserUniqueId());
//		w.p.setUuid(trans.getUuid());
		w.p.setVin(trans.getVin());
		CaptureServlet.IN_DTOs.put(w.loggedInUser.getUserUniqueId(), w.p);
	}
	
	
	private VerticalLayout main = new VerticalLayout();
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private User loggedInUser;
	private BrowserFrame browser = new BrowserFrame(null);
	private Button link = new Button();
	private Transaction trans;
	private List<DataDictionary> list;
	private UploadInDTO p = new UploadInDTO();
	private Callback2 callback;
}
