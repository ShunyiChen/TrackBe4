package com.maxtree.automotive.dashboard.view.front;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.HelloServlet;
import com.maxtree.automotive.dashboard.UploadParameters;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.UploadedFileQueue;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.event.UIEvents;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * 业务类型选择器
 * 
 * @author Chen
 *
 */
public class BusinessTypeSelector extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public BusinessTypeSelector(FrontView view) {
		this.view = view;
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		data = ui.userService.findAssignedBusinesses(loginUser.getUserUniqueId());
		selector = new ComboBox<Business>("业务类型:", data);
		// Disallow null selections
		selector.setEmptySelectionAllowed(false);
		selector.setTextInputAllowed(false);
		selector.setPlaceholder("请选择一个业务类型");
		selector.setWidth("100%");
		selector.setHeight("27px");
		this.addComponent(selector);
		
		selector.addValueChangeListener(e->{
			business = e.getValue();
			if (business != null) {
				view.businessCode = business.getCode();
				loadMaterials(business.getCode());
				polling();
			} 
			else {
				view.businessCode = null;
			}
		});
	}
	
	/**
	 * 根据业务类型加载材料
	 * 
	 * @param businessCode
	 */
	private void loadMaterials(String businessCode) {
		// 加载文件上传表格
		view.fileGrid.removeAllRows();
		
		List<DataDictionary> list = ui.businessService.getDataDictionaries(businessCode);
		int i = 0;
		for (DataDictionary dd : list) {
			i++;
			ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName());
			row.setDataDictionary(dd);
			
			view.fileGrid.addRow(row);
			// 选中第一个
			if (i == 1) {
				row.selected();
				try {
					UploadParameters p = new UploadParameters(view.loggedInUser.getUserUniqueId(), view.vin, view.batch+"", view.editableSite.getSiteUniqueId(),view.uuid,view.businessCode,dd.getCode());
					Yaml.updateUploadParameters(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		i++;
		ThumbnailRow row = new ThumbnailRow(i+"."+"其它材料");
		DataDictionary dict = new DataDictionary();
		dict.setCode("$$$$");
		row.setDataDictionary(dict);
		view.fileGrid.addRow(row);
		view.fileGrid.focus();
		// 加载拍照影像
		view.capturePane.displayImage(view.uuid);
		
	}
	
	private void polling() {
		ui.setPollInterval(1 * 1000);
		ui.addPollListener(new UIEvents.PollListener() {
			@Override
			public void poll(UIEvents.PollEvent event) {
				
				List<UploadedFileQueue> list = HelloServlet.MAP.get(view.loggedInUser.getUserUniqueId()+"");
				if (list != null) {
					for (UploadedFileQueue ufq : list) {
						if (ufq.getRemovable() == 0) {
							view.fileGrid.map.get(ufq.getDictionaryCode()).addThumbnail(new Thumbnail(ufq.getUserUniqueId(), ufq.getDocumentUniqueId()));
							ufq.setRemovable(1);
						}
					}
				}
			}
		});
	}

	private FrontView view;
	private List<Business> data;
	private ComboBox<Business> selector;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Business business;
}
