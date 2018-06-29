package com.maxtree.automotive.dashboard.view.front;

import java.io.OutputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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

public class ClosableUploadGridCell extends VerticalLayout implements Receiver, SucceededListener, ProgressListener, StartedListener, FailedListener, FinishedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param document
	 * @param site
	 * @param vin
	 */
	public ClosableUploadGridCell(Document document, Site site) {
		this.document = document;
		this.site = site;
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("180px");
		this.setHeight("140px");
		this.addStyleName("drop-area");
		content.setSpacing(false);
		content.setMargin(false);
		content.setWidth("100%");
		content.setHeightUndefined();
		Image scan = new Image(null);
		scan.addClickListener(e->{
			System.out.println("扫描");
		});
		scan.setIcon(VaadinIcons.CAMERA);
		upload = new Upload(null, this);
		upload.setButtonCaption(document.getAlias());
		upload.setButtonStyleName("upload-button");
		upload.setImmediateMode(true);
		upload.addSucceededListener(this);
		HorizontalLayout row2 = new HorizontalLayout();
		row2.setSpacing(false);
		row2.setMargin(false);
		row2.setWidth("100%");
		row2.setWidthUndefined();
		row2.setHeightUndefined();
		row2.addComponents(upload);
		row2.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
		
		// 右键菜单
		com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(this, true);
		menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				showFile(document.getAlias());
			}
		});
		menu.addSeparator();
		menu.addItem("删除", new com.vaadin.contextmenu.Menu.Command() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
				
				ui.documentService.deleteById(document.getDocumentUniqueId());
				
				try {
					new TB4FileSystem().deleteFile(site, document.getFileFullPath());
				} catch (FileException e) {
					e.printStackTrace();
					Notifications.warning("删除文件"+document.getFileFullPath()+" 失败。");
				}
				
				if (deleteCallback != null)
					deleteCallback.onSuccessful();
				
			}
		});
		
		content.addComponents(row2);
		content.setComponentAlignment(row2, Alignment.MIDDLE_LEFT);
		if (!document.getFileFullPath().equals("")) {
			displayLink(document.getFileName());
		}
		this.addComponent(content);
		this.setComponentAlignment(content, Alignment.TOP_CENTER);
	}

	@Override
	public void uploadFinished(FinishedEvent event) {
	}

	@Override
	public void uploadFailed(FailedEvent event) {
	}

	@Override
	public void uploadStarted(StartedEvent event) {
//		progressBar.setValue(0f);
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
//		progressBar.setValue(readBytes / (float) contentLength);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		if (upload.getComponentError() != null) {
			upload.setComponentError(null);
		}
		updateDocument(event.getFilename());
		displayLink(event.getFilename());
		
		// 更新已用存储大小
		new TB4FileSystem().increaseUsedSize(site.getSiteUniqueId(), upload.getUploadSize());
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		if (filename.length() > 60) {
			Notifications.warning("文件名不能超出60个字符。");
			return null;
		}
		// 容量check
		long usedSize = site.getSiteCapacity().getUsedSpace() + upload.getUploadSize();
		if (usedSize >= site.getSiteCapacity().getCapacity()) {
			Notifications.warning("站点("+site.getSiteName()+")容量已满，请联系管理员切换其它站点。");
			return null;
		}
		
		String oldPath = document.getFileFullPath();
		try {
			fileFullPath = document.getBatch() + "/"+document.getUuid()+"/"+System.currentTimeMillis()+"_"+filename;
			document.setFileFullPath(fileFullPath);
			
			return new TB4FileSystem().receiveUpload(site, fileFullPath);
		} catch (FileException e) {
			Notifications.warning(e.getMessage());
		} finally {
			// 删除上一文件
			if (!StringUtils.isEmpty(oldPath)) {
				try {
					new TB4FileSystem().deleteFile(site, oldPath);
				} catch (FileException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private HorizontalLayout createImageLink(String fileName) {
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setHeightUndefined();
		hlayout.setWidth("178px");
		Image image = new Image(fileName);
		image.setIcon(VaadinIcons.FILE_PICTURE);
		hlayout.addComponents(image);
		hlayout.setComponentAlignment(image, Alignment.MIDDLE_LEFT);
		return hlayout;
	}
	
	public void updateDocument(String fileName) {
		document.setFileName(fileName);
		document.setFileFullPath(fileFullPath);
		if (document.getDocumentUniqueId() == 0) {
			int documentUniqueId = ui.documentService.create(document);
			document.setDocumentUniqueId(documentUniqueId);
		} else {
			ui.documentService.update(document);
		}
	}
	
	private void displayLink(String realFileName) {
		if (link != null) {
			content.removeComponent(link);
		}
		link = createImageLink(realFileName);
		content.addComponent(link);
		content.setComponentAlignment(link, Alignment.MIDDLE_CENTER);
	}
	
	private void showFile(final String alias) {
		if (document.getFileFullPath() == null) {
			Notifications.warning("请先上传图片再查看。");
			return;
		}
		
        // resource for serving the file contents
        final StreamSource streamSource = () -> {
			FileObject fileObj;
			try {
				fileObj = new TB4FileSystem().resolveFile(site, document.getFileFullPath());
				return fileObj.getContent().getInputStream();
			} catch (FileException e) {
			} catch (FileSystemException e) {
			}
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, alias);
 
        // show the file contents - images only for now
        resource.setCacheTime(0);
 		Image picture = new Image(null, resource);
        showComponent(picture, alias);
    }
	
	private void showComponent(final Component c, final String alias) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setMargin(true);
        final Window w = new Window(alias, layout);
        w.addStyleName("dropdisplaywindow");
        w.setSizeUndefined();
        w.setModal(true);
        w.setResizable(true);
        c.setSizeUndefined();
        layout.addComponent(c);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	public Callback getDeleteCallback() {
		return deleteCallback;
	}

	public void setDeleteCallback(Callback deleteCallback) {
		this.deleteCallback = deleteCallback;
	}

	private Upload upload ;
	private VerticalLayout content = new VerticalLayout();
	private HorizontalLayout link;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Document document;
	private Site site;
	private String fileFullPath;
	private Callback deleteCallback;
}
