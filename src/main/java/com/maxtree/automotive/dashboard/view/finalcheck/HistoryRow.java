package com.maxtree.automotive.dashboard.view.finalcheck;

import java.text.SimpleDateFormat;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.domain.DocumentHistory;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author chens
 *
 */
public class HistoryRow extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param history
	 */
	public HistoryRow(DocumentHistory history) {
		this.setWidth("100%");
		this.setHeight("30px");
		this.setSpacing(false);
		this.setMargin(false);
		Label time = new Label();
		Label userName = new Label();
		time.setValue(format.format(history.getDateCreated()));
		userName.setValue(history.getUserName());
		this.addComponents(time,userName);
		this.setComponentAlignment(time, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(userName, Alignment.MIDDLE_LEFT);
		this.addStyleName("HistoryRow");
		this.addLayoutClickListener(e->{
			if(e.getMouseEventDetails().isDoubleClick()) {
				Callback onOK = new Callback() {

					@Override
					public void onSuccessful() {
					}
				};
				MessageBox.showMessage("提示", "请确认是否还原此项。", MessageBox.WARNING, onOK, "确定");
			}
		});
	}
	
	private SimpleDateFormat format = new SimpleDateFormat();
}
