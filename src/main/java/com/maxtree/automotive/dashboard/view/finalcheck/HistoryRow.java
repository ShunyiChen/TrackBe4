package com.maxtree.automotive.dashboard.view.finalcheck;

import java.text.SimpleDateFormat;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.EncryptionUtils;
import com.maxtree.automotive.dashboard.component.MessageBox;
//import com.maxtree.automotive.dashboard.domain.Document;
//import com.maxtree.automotive.dashboard.domain.DocumentHistory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class HistoryRow extends HorizontalLayout {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param window
//	 * @param history
//	 * @param callback
//	 */
//	public HistoryRow(PopupHistory window, DocumentHistory history, Callback callback) {
//		this.setWidth("100%");
//		this.setHeight("30px");
//		this.setSpacing(false);
//		this.setMargin(false);
//		Label time = new Label();
//		Label userName = new Label();
//		time.setValue(format.format(history.getDateCreated()));
//		userName.setValue(history.getUserName());
//		this.addComponents(time,userName);
//		this.setComponentAlignment(time, Alignment.MIDDLE_LEFT);
//		this.setComponentAlignment(userName, Alignment.MIDDLE_LEFT);
//		this.addStyleName("HistoryRow");
//		this.addLayoutClickListener(e->{
//			if(e.getMouseEventDetails().isDoubleClick()) {
//				Callback onOK = new Callback() {
//
//					@Override
//					public void onSuccessful() {
//						Document doc = ui.documentService.findById(history.getDocumentUniqueId(), history.getTableId());
//
//						doc.setThumbnail(history.getThumbnail());
//						doc.setFileFullPath(EncryptionUtils.decryptString(history.getFileFullPath()));
//
////						System.out.println(history+","+doc.getDocumentUniqueId());
//
//						ui.documentService.update(doc, history.getTableId());
//
//						window.close();
//
//						callback.onSuccessful();
//					}
//				};
//				MessageBox.showMessage("提示", "请确认是否还原此项。", MessageBox.WARNING, onOK, "确定");
//			}
//		});
//	}
//
//	private String pattern = "yyyy年MM月dd日 HH:mm:ss";
//	private SimpleDateFormat format = new SimpleDateFormat(pattern);
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
