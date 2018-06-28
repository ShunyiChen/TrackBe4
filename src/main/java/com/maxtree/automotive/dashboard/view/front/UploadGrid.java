package com.maxtree.automotive.dashboard.view.front;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class UploadGrid extends Panel {

	private static final Logger log = LoggerFactory.getLogger(UploadGrid.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param caption
	 * @param isPrimary
	 */
	public UploadGrid(String caption) {
		this.setCaption(caption);
		initComponents();
	}
	
	private void initComponents() {
		this.addStyleName("picture-pane");
		this.setWidth("100%");
		
    	hLayout.setMargin(false);
    	hLayout.setSpacing(true);
    	hLayout.setWidthUndefined();
    	hLayout.setHeight("100%");
    	
    	setContent(hLayout);

    		
    		
//    		UploadStateWindow uploadStateWindow = new UploadStateWindow();
//	   	    UploadFinishedHandler uploadFinishedHandler = new UploadFinishedHandler() {
//	
//	   			@Override
//	   			public void handleFile(InputStream stream, String fileName, String mimeType, long length,
//	   					int filesLeftInQueue) {
//	   		
//	   				String filePath = transactionUniqueId+"/secondary/"+fileName;
//	   				try {
//						OutputStream out = new MyFileSystem().receiveUpload(site, filePath);
//						IOUtils.copy(stream,out);
//						
//						out.close();
//						stream.close();
//						
//					} catch (FileException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//	   				
//	   				Material material = new Material();
//	   				material.setName("附件_"+index);
//	   				
//	   				UploadGridCell cell = new UploadGridCell(material, site, transactionUniqueId, false, UploadGrid.this);
//	   				cell.onSuccessful(material.getName(), filePath);
//	   				hLayout.addComponents(Box.createHorizontalBox(5), cell);
//	   				index++;
////	   				Notification.show(fileName + " uploaded (" + length + " bytes). " + filesLeftInQueue + " files left.");
//	   			}
//	   	      };
//	   		
//	   		MultiFileUpload multi = new MultiFileUpload(uploadFinishedHandler, uploadStateWindow);
//	   		multi.setUploadButtonCaptions("单选", "多选");
//	   		multi.setUploadButtonIcon(VaadinIcons.PRINT);
//	   		hLayout.addComponent(multi);
	   		
//    	}
    	
	}
	
//	public void enableRightClickMenu() {
//		com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(this, true);
//		menu.addItem("选择文件", new com.vaadin.contextmenu.Menu.Command() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
//			}
//		});
//	}
	
	public void addUploadCells(String vin, Site site, Document... documents) {
		hLayout.removeAllComponents();
		for (Document doc : documents) {
			UploadGridCell cell = new UploadGridCell(doc, site, vin);
			hLayout.addComponents(cell);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean checkUploads() {
		boolean bool = true;
		Iterator<Component> iter = hLayout.iterator();
		while(iter.hasNext()) {
			Component c = iter.next();
			UploadGridCell cell = (UploadGridCell) c;
			if (cell.hasUploadFailed()) {
				bool = false;
				break;
			}
		}
		return bool;
	}
	
	public void reset() {
		hLayout.removeAllComponents();
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private HorizontalLayout hLayout = new HorizontalLayout();
}
