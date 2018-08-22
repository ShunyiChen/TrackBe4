package com.maxtree.automotive.dashboard.view.front;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.servlet.UploadFileServlet;
import com.maxtree.automotive.dashboard.servlet.UploadInDTO;
import com.maxtree.automotive.dashboard.servlet.UploadOutDTO;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import net.coobird.thumbnailator.Thumbnails;

public class CapturePane extends Panel implements Receiver, SucceededListener, ProgressListener, StartedListener, FailedListener, FinishedListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CapturePane() {
		this.setCaption("拍照");
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("picture-pane");
		UI.getCurrent().getPage().addBrowserWindowResizeListener(e->{
			int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 173;
			this.setHeight(height+"px");
		});
		loggedinUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 173;
		this.setWidth("100%");
		this.setHeight(height+"px");
//		browser.setSizeFull();
//		this.setContent(browser);
		
		Upload upload = new Upload(null, this);
		upload.setButtonCaption("选择文件");
		upload.setButtonStyleName("upload-button");
		upload.setImmediateMode(true);
		upload.addSucceededListener(this);
		this.setContent(upload);
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
					inputStream = new FileInputStream("devices/"+user.getUserUniqueId()+".html");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return inputStream;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, user.getUserUniqueId()+".html");
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
		File template = new File("devices/templates/HtmlDemo3.html");
//		File template = new File("devices/templates/TempHtml.html");
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
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("devices/" + user.getUserUniqueId() + ".html")), "utf-8");
		oStreamWriter.append(everything);
		oStreamWriter.close();
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
		//生成缩略图
		FileObject fobject;
		try {
			fobject = fileSystem.resolveFile(site, targetPath);
	//		byte[] bytes = IOUtils.toByteArray(inputStream);
	//		ByteArrayInputStream is = new ByteArrayInputStream(fobject.getContent().getInputStream());
			InputStream is = fobject.getContent().getInputStream();
			ByteArrayOutputStream smallOutputStream = new ByteArrayOutputStream();
			Thumbnails.of(is).size(100, 100)/*.scale(0.033f)*/.toOutputStream(smallOutputStream);//.toFile("devices/"+userUniqueId+"/thumbnails/"+documentUniqueId+".jpg");  
			is.close();
		
			//创建Document
			Document document = new Document();
			document.vin = p.getVin();
			document.location =  1;//1:主要材料 2:次要材料 
			document.setUuid(p.getUuid());
			document.setDictionarycode(p.getDictionaryCode());
			document.setFileFullPath(targetPath);
			document.setThumbnail(smallOutputStream.toByteArray());
			int documentUniqueId = ui.documentService.insert(document);
			
			UploadOutDTO ufq = new UploadOutDTO();
			ufq.location = 1;//1:主要材料 2:次要材料  
			ufq.thumbnail = new ByteArrayInputStream(smallOutputStream.toByteArray());
			ufq.setDictionaryCode(p.getDictionaryCode());
			ufq.setDocumentUniqueId(documentUniqueId);
			ufq.setUserUniqueId(loggedinUser.getUserUniqueId());
			ufq.setFileFullPath(targetPath);
			ufq.setRemovable(0);
			smallOutputStream.close();
			
			if(UploadFileServlet.OUT_DTOs.get(loggedinUser.getUserUniqueId()) == null) {
				UploadFileServlet.OUT_DTOs.put(loggedinUser.getUserUniqueId(), new ArrayList<UploadOutDTO>());
			}
			List<UploadOutDTO> list = UploadFileServlet.OUT_DTOs.get(loggedinUser.getUserUniqueId());
			list.add(ufq);
			
		} catch (FileException e) {
			e.printStackTrace();
		} catch (FileSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		p = UploadFileServlet.IN_DTOs.get(loggedinUser.getUserUniqueId());
		///上传文件
		site = ui.siteService.findById(p.getSiteID());
		targetPath = p.getBatch()+"/"+p.getUuid()+"/"+filename;
		
		OutputStream os = null;
		try {
			os = fileSystem.receiveUpload(site, targetPath);
		} catch (FileException e) {
			e.printStackTrace();
		}
		return os;
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private BrowserFrame browser = new BrowserFrame(null);
	private User loggedinUser;
	private TB4FileSystem fileSystem = new TB4FileSystem();
	private Site site = null;
	private String targetPath = null;
	private UploadInDTO p = null;
}
