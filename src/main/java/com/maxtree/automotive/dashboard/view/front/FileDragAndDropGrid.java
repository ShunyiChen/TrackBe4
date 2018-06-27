package com.maxtree.automotive.dashboard.view.front;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.FileDropTarget;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

/**
 * 1.支持批量拖拽上传;
 * 2.支持选择文件上传；
 * 
 * @author chens
 *
 */
public class FileDragAndDropGrid extends VerticalLayout implements Receiver, SucceededListener, ProgressListener, StartedListener, FailedListener, FinishedListener, UploadFinishedHandler{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param caption
	 */
	public FileDragAndDropGrid(String caption) {
		this.caption = caption;
		
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		this.setSpacing(false);
		this.setMargin(false);
		
		hLayout.setSizeUndefined();
		panel.addStyleName("picture-pane");
		panel.setCaption(caption);
		panel.setWidth("100%");
		panel.setHeight("195px");
		panel.setContent(hLayout);
		
//		Upload upload = new Upload(null, this);
//		upload.setButtonCaption("选择文件");
//		upload.setButtonStyleName("upload-button");
//		upload.setImmediateMode(true);
//		upload.addSucceededListener(this);
		
		new FileDropTarget<>(panel, fileDropEvent -> {
			final int fileSizeLimit = 20 * 1024 * 1024; // 20MB

			fileDropEvent.getFiles().forEach(html5File -> {
				final String fileName = html5File.getFileName();
				if (html5File.getFileSize() > fileSizeLimit) {
					Notification.show("File rejected. Max 20MB files are accepted by Sampler",
							Notification.Type.WARNING_MESSAGE);
				}
				else if (fileName.length() > 60) {
					Notification.show("上传文件名不能超出60个字符。",
							Notification.Type.WARNING_MESSAGE);
				}
				else {
					
					if (businessUniqueId == 0) {
						Notification.show("请选择一个业务类型", Notification.Type.WARNING_MESSAGE);
						return;
					}
					// 容量check
					long usedSize = site.getSiteCapacity().getUsedSpace() + html5File.getFileSize();
					if (usedSize >= site.getSiteCapacity().getCapacity()) {
						Notifications.warning("站点("+site.getSiteName()+")容量已满，请联系管理员切换其它站点。");
						return;
					}
					
					
					final ByteArrayOutputStream bas = new ByteArrayOutputStream();
					final StreamVariable streamVariable = new StreamVariable() {
						String fileFullPath = "";
						@Override
						public OutputStream getOutputStream() {
							fileFullPath = uuid +"/"+1+"/"+System.currentTimeMillis()+"_"+fileName;
							try {
								return new TB4FileSystem().receiveUpload(site, fileFullPath);
							} catch (FileException e) {
								e.printStackTrace();
							}
							return bas;
						}

						@Override
						public boolean listenProgress() {
							return false;
						}

						@Override
						public void onProgress(final StreamingProgressEvent event) {
						}

						@Override
						public void streamingStarted(final StreamingStartEvent event) {
						}

						@Override
						public void streamingFinished(final StreamingEndEvent event) {
							
							addDocument(fileName, fileFullPath);
							
							// 更新站点已用大小
							new TB4FileSystem().checkAndUpdateDisk(site.getSiteUniqueId(), html5File.getFileSize());
						}

						@Override
						public void streamingFailed(final StreamingErrorEvent event) {
							// progress.setVisible(false);
						}

						@Override
						public boolean isInterrupted() {
							return false;
						}
					};
					html5File.setStreamVariable(streamVariable);
//					progressBar.setVisible(true);
				}
			});
		});
		
		UploadStateWindow uploadStateWindow = new UploadStateWindow();
		multi = new MultiFileUpload(this, uploadStateWindow, false);
		multi.setEnabled(false);
   		multi.setUploadButtonCaptions("单选", "选择文件");
   		multi.setUploadButtonIcon(VaadinIcons.FOLDER_OPEN);
   		this.addComponents(panel, multi);
	}
	
	public void setEnabledForUploadComponent(boolean bool) {
		multi.setEnabled(bool);
	}
	
	@Override
	public void uploadFinished(FinishedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadStarted(StartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
		System.out.println("uploadSucceeded");
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void handleFile(InputStream stream, String fileName, String mimeType, long length, int filesLeftInQueue) {
		
		if (fileName.length() > 20) {
			Notification.show("上传文件名不能超出20个字符。",
					Notification.Type.WARNING_MESSAGE);
		} 
		else {
			// 容量check
			long usedSize = site.getSiteCapacity().getUsedSpace() + length;
			if (usedSize >= site.getSiteCapacity().getCapacity()) {
				Notifications.warning("站点("+site.getSiteName()+")容量已满，请联系管理员切换其它站点。");
				return;
			}
			
			String filePath = "";
			try {
				filePath = uuid+"/1/"+System.currentTimeMillis()+"_"+fileName;
				OutputStream out = new TB4FileSystem().receiveUpload(site, filePath);
				IOUtils.copy(stream, out);
				
				out.close();
				stream.close();
				
			} catch (FileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 更新站点已用大小
				new TB4FileSystem().checkAndUpdateDisk(site.getSiteUniqueId(), length);
			}
			
			addDocument(fileName, filePath);
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	private void addDocument(String fileName, String filePath) {
		Document document = new Document();
		document.setAlias(fileName);
		document.setFileName(fileName);
		document.setFileFullPath(filePath);
		document.setCategory(1); // 文件类别0：主要图片,1：次要图片
		document.setBusinessUniqueId(businessUniqueId);
		document.setUuid(uuid);
		
		int documentUniqueId = ui.documentService.create(document);
		document.setDocumentUniqueId(documentUniqueId);
		
		if (documentUniqueId > 0) {
			ClosableUploadGridCell cell = new ClosableUploadGridCell(document, site);
			Callback deleteCallback = new Callback() {
				@Override
				public void onSuccessful() {
					hLayout.removeComponent(cell);
				}
			};
			cell.setDeleteCallback(deleteCallback);
			hLayout.addComponents(cell);
		}
		
	}
	
	public void reset() {
		hLayout.removeAllComponents();
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setBusinessUniqueId(int businessUniqueId) {
		this.businessUniqueId = businessUniqueId;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * 
	 * @param site
	 * @param documents
	 */
	public void addUploadCells(Site site, Document... documents) {
		hLayout.removeAllComponents();
		int i = 1;
		for (Document doc : documents) {
			ClosableUploadGridCell cell = new ClosableUploadGridCell(doc,  site);
			Callback deleteCallback = new Callback() {
				@Override
				public void onSuccessful() {
					hLayout.removeComponent(cell);
				}
			};
			cell.setDeleteCallback(deleteCallback);
			hLayout.addComponents(cell);
			i++;
		}
	}
	
	private MultiFileUpload multi;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Panel panel = new Panel();
	private HorizontalLayout hLayout = new HorizontalLayout();
	private String caption;
	private String uuid;
	private int businessUniqueId;
	private Site site;
}
