package com.maxtree.automotive.dashboard.view.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.maxtree.automotive.dashboard.view.quality.ImageStage;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class ImageViewerWindow extends Window implements ImageViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ImageViewerWindow(InputViewIF view, int selectDocumentId) {
		this.view = view;
		this.selectDocumentId = selectDocumentId;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("原文");
		this.setModal(true);
		this.setClosable(true);
		this.setResizable(true);
		this.setWidth("800px");
		this.setHeight("700px");
		this.addCloseListener(e->{
			SystemConfiguration config = Yaml.readSystemConfiguration();
			ui.setPollInterval(config.getInterval());
		});
		List<Document> list1 = ui.documentService.findAllDocument1(view.vin(), view.uuid());
		List<Document> list2 = ui.documentService.findAllDocument2(view.vin(), view.uuid());
		allDocuments.addAll(list1);
		allDocuments.addAll(list2);
		for(Document doc : allDocuments) {
			if (doc.getDocumentUniqueId()==selectDocumentId) {
				
				String alias = StringUtils.isEmpty(doc.getAlias())?"其它材料":doc.getAlias();
		 		this.setCaption("原文-"+alias);
				imgStage.display(view.editableSite(), doc);
				break;
			}
			index++;
		}
		
		this.setContent(imgStage);
	}
	
	/**
	 * 
	 * @param view
	 * @param selectDocumentId
	 */
	public static void open(InputViewIF view, int selectDocumentId) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
		ui.setPollInterval(-1);
        ImageViewerWindow w = new ImageViewerWindow(view, selectDocumentId);
        UI.getCurrent().addWindow(w);
        w.center();
        w.focus();
    }
	
	@Override
	public void previous() {
		// TODO Auto-generated method stub
		if (index < allDocuments.size() - 1) {
			index++;
		} else {
			index = 0;
		}
		Document doc = allDocuments.get(index);
		imgStage.display(view.editableSite(), doc);
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		if (index < allDocuments.size() - 1) {
			index++;
		} else {
			index = 0;
		}
		Document doc = allDocuments.get(index);
		imgStage.display(view.editableSite(), doc);
	}
	
	private InputViewIF view;
	private List<Document> allDocuments = new ArrayList<Document>();
	private int selectDocumentId;
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private int index = 0;
	private ImageStage imgStage = new ImageStage(this, false);
}
