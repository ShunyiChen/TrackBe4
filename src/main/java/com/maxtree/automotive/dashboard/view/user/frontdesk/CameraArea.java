package com.maxtree.automotive.dashboard.view.user.frontdesk;

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

import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.jpa.entity.Camera;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.ui.Upload;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.DashboardUI;
//import com.maxtree.automotive.dashboard.domain.Document;
//import com.maxtree.automotive.dashboard.domain.Site;
//import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.servlet.UploadInDTO;
import com.maxtree.automotive.dashboard.servlet.UploadOutDTO;
import com.maxtree.automotive.vfs.VFSUtils;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
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

/**
 * 
 * @author chens
 *
 */
public class CameraArea extends Panel implements Receiver, SucceededListener, ProgressListener, StartedListener, FailedListener, FinishedListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CameraArea() {
		this.setCaption(null);
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("picture-pane");
		UI.getCurrent().getPage().addBrowserWindowResizeListener(e->{
			int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 73;
			this.setHeight(height+"px");
		});
		int height = UI.getCurrent().getPage().getBrowserWindowHeight() - 73;
		this.setWidth("100%");
		this.setHeight(height+"px");
		Iterable<Camera> iterable = TB4Application.getInstance().cameraRepository.findAll();
		List<Camera> list = new ArrayList<>();
		iterable.forEach(single ->{list.add(single);});
		for(Camera c : list) {
			if(c.getEnable()) {
				camera = c;
			}
		}
		if(camera == null) {
			Upload upload = new Upload(null, this);
			upload.setButtonCaption("选择文件");
			upload.setButtonStyleName("upload-button");
			upload.setImmediateMode(true);
			upload.addSucceededListener(this);
			this.setContent(upload);
		}
		else {
			browser.setSizeFull();
			this.setContent(browser);
		}
	}
	
	/**
	 * 显示拍照影像
	 */
	public void turnCameraOn() {
//		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
// 			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
// 			public InputStream getStream() {
//				File generatedFile = new File("camera/personal/"+ui.getLoggedInUser().getUserUniqueId()+".html");
//				if(!generatedFile.exists()) {
//					try {
//						generateNewHTML();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
// 				FileInputStream inputStream = null;
//				try {
//					inputStream = new FileInputStream("camera/personal/"+ui.getLoggedInUser().getUserUniqueId()+".html");
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				return inputStream;
// 			}
// 		};
// 		StreamResource streamResource = new StreamResource(streamSource, ui.getLoggedInUser().getUserUniqueId()+".html");
// 		streamResource.setCacheTime(0);
//		browser.setSource(streamResource);
	}

	public void turnCameraOff() {
//		File generatedFile = new File("camera/personal/"+ui.getLoggedInUser().getUserUniqueId()+".html");
//		if(generatedFile.exists()) {
//			boolean delete = generatedFile.delete();
//			System.out.println("camera was deleted "+delete);
//		}
	}

	/**
	 * 生成新的带参数的HTML文件
	 * 
	 * @throws IOException
	 */
	private void generateNewHTML() throws IOException {
		String everything = "";
		File template = null;
//		if(settings.getValue().equals("无锡华通H6-1")) {
//			template = new File("devices/templates/HtmlDemo3.html");//无锡华通H6-1
//		}
//		else {
//			template = new File("devices/templates/Sample_CamOCX_HTML_Device_IE.html");//维山VSA305FD
//		}
//		File template = new File("devices/templates/TempHtml.html"); // 选择本地图片上传
		template = new File("camera/templates/维山.html");
		FileInputStream in = new FileInputStream(template);
		BufferedReader br = new BufferedReader(new UnicodeReader(in));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
//		while (line != null) {
//			if (line.contains("var userUniqueId = \"\";")) {
//				line = line.replace("var userUniqueId = \"\";", "var userUniqueId = \""+ui.getLoggedInUser().getUserUniqueId()+"\";");
//			}
//			// System.out.println(line);
//			sb.append(line);
//			sb.append(System.lineSeparator());
//			line = br.readLine();
//		}
		everything = sb.toString();
		br.close();
		in.close();
		
//		// 动态生成新的html
//		File targetFile = new File("camera/personal/" + ui.getLoggedInUser().getUserUniqueId() + ".html");
//		if(targetFile.exists()) {
//			targetFile.delete();
//		}
//		OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFile), "utf-8");
//		oStreamWriter.append(everything);
//		oStreamWriter.close();
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
//		try {
//			fobject = fileSystem.resolveFile(site, targetPath);
	//		byte[] bytes = IOUtils.toByteArray(inputStream);
	//		ByteArrayInputStream is = new ByteArrayInputStream(fobject.getContent().getInputStream());
//			InputStream is = fobject.getContent().getInputStream();
//			ByteArrayOutputStream smallOutputStream = new ByteArrayOutputStream();
//			Thumbnails.of(is).size(100, 100)/*.scale(0.033f)*/.toOutputStream(smallOutputStream);//.toFile("devices/"+userUniqueId+"/thumbnails/"+documentUniqueId+".jpg");
//			is.close();
//
			//创建Document
//			Document document = new Document();
//			document.setUuid(p.getUuid());
//			document.setDictionarycode(p.getDictionaryCode());
//			document.setFileFullPath(targetPath);
//			document.setThumbnail(smallOutputStream.toByteArray());
//			int documentUniqueId = ui.documentService.insert(document,p.getVin());
//
//			UploadOutDTO ufq = new UploadOutDTO();
//			ufq.thumbnail = new ByteArrayInputStream(smallOutputStream.toByteArray());
//			ufq.setDictionaryCode(p.getDictionaryCode());
//			ufq.setDocumentUniqueId(documentUniqueId);
//			ufq.setUserUniqueId(ui.getLoggedInUser().getUserUniqueId());
//			ufq.setFileFullPath(targetPath);
//			ufq.setRemovable(0);
//			smallOutputStream.close();
//
//			if(UploadFileServlet.OUT_DTOs.get(ui.getLoggedInUser().getUserUniqueId()) == null) {
//				UploadFileServlet.OUT_DTOs.put(ui.getLoggedInUser().getUserUniqueId(), new ArrayList<UploadOutDTO>());
//			}
//			List<UploadOutDTO> list = UploadFileServlet.OUT_DTOs.get(ui.getLoggedInUser().getUserUniqueId());
//			list.add(ufq);
			
//		} catch (FileException e) {
//			e.printStackTrace();
//		} catch (FileSystemException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
//		p = UploadFileServlet.IN_DTOs.get(ui.getLoggedInUser().getUserUniqueId());
//		///上传文件
//		site = ui.siteService.findById(p.getSiteID());
//		targetPath = "/"+p.getUuid()+"/"+filename;
		
//		OutputStream os = null;
//		try {
//			os = fileSystem.receiveUpload(site, targetPath);
//		} catch (FileException e) {
//			e.printStackTrace();
//		}
//		return os;
		return null;
	}

	private Camera camera = null;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private BrowserFrame browser = new BrowserFrame(null);
	private VFSUtils fileSystem = new VFSUtils();
	private String targetPath = null;
	private UploadInDTO p = null;
}
