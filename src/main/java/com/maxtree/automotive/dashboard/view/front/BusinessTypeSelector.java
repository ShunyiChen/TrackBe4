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
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.servlet.UploadFileServlet;
import com.maxtree.automotive.dashboard.servlet.UploadInDTO;
import com.maxtree.automotive.dashboard.servlet.UploadOutDTO;
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
				loadMaterials(business.getCode());
				polling();
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
				
				UploadInDTO inDto = new UploadInDTO(view.loggedInUser.getUserUniqueId(), view.vin, view.batch+"", view.editableSite.getSiteUniqueId(),view.uuid,dd.getCode());
				UploadFileServlet.IN_DTOs.put(view.loggedInUser.getUserUniqueId(), inDto);
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
	
	/**
	 * 
	 */
	private void polling() {
		ui.setPollInterval(1 * 1000);
		ui.addPollListener(new UIEvents.PollListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void poll(UIEvents.PollEvent event) {
				
				List<UploadOutDTO> list = UploadFileServlet.OUT_DTOs.get(view.loggedInUser.getUserUniqueId());
				if (list != null) {
 
					Iterator<UploadOutDTO> iter = list.iterator();
					while(iter.hasNext()) {
						UploadOutDTO ufq = iter.next();
						if (ufq.getRemovable() == 0) {
							view.fileGrid.map.get(ufq.getDictionaryCode()).addThumbnail(new Thumbnail(ufq.thumbnail));
							ufq.setRemovable(1);
						}
						else {
							// 已经回显过的从缓存清空
							iter.remove();
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
