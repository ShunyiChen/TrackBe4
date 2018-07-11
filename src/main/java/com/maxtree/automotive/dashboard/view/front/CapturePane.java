package com.maxtree.automotive.dashboard.view.front;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.UploadedFileQueue;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.event.UIEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import elemental.json.JsonArray;

public class CapturePane extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public CapturePane(FrontView view) {
		this.setCaption("拍照");
		this.view = view;
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("picture-pane");
		UI.getCurrent().getPage().addBrowserWindowResizeListener(e->{
			int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 173;
			this.setHeight(height+"px");
		});
		
		int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 173;
		this.setWidth("100%");
		this.setHeight(height+"px");
		browser.setSizeFull();
		this.setContent(browser);
	}
	
	
	/**
	 * 显示拍照影像
	 * 
	 * @param uuid
	 */
	public void displayImage(String uuid) {
		try {
			generateNewHTML(uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream("devices/"+user.getUserUniqueId()+"/capture.html");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return inputStream;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, "capture.html");
 		streamResource.setCacheTime(0);
		browser.setSource(streamResource);
	}
	
	/**
	 * 生成新的带参数的HTML文件
	 * 
	 * @param uuid
	 * @throws IOException
	 */
	private void generateNewHTML(String uuid) throws IOException {
		// 读取原来的html模板
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		String everything = "";
		File template = new File("devices/Sample_CamOCX_HTML_Device_IE.html");
		FileInputStream in = new FileInputStream(template);
		BufferedReader br = new BufferedReader(new UnicodeReader(in));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			if (line.contains("var userUniqueId = \"\";")) {
				line = line.replace("var userUniqueId = \"\";", "var userUniqueId = \""+user.getUserUniqueId()+"\";");
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
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("devices/" + user.getUserUniqueId() + "/capture.html")), "utf-8");
		oStreamWriter.append(everything);
		oStreamWriter.close();
	}
	
	private FrontView view;
	private BrowserFrame browser = new BrowserFrame(null);
}
