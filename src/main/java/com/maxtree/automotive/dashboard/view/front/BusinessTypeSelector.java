package com.maxtree.automotive.dashboard.view.front;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
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
			Business business = e.getValue();
			if (business != null) {
				
				if (view.fileGrid.uuid != null) {
					Callback event = new Callback() {

						@Override
						public void onSuccessful() {
							
							loadMaterials(business.getCode());
						}
					};
					MessageBox.showMessage("变更提示", "您确定要放弃上次上传的文件？", MessageBox.WARNING, event, "确定");
				}
				else {
					
					loadMaterials(business.getCode());
				}
				
				
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
			view.fileGrid.addRow(row);
			if (i == 1) {
				row.selected();
			}
		}
		i++;
		ThumbnailRow row = new ThumbnailRow(i+"."+"其它材料");
		view.fileGrid.addRow(row);
		view.fileGrid.generateNewUUID();
		view.fileGrid.focus();
		// 加载拍照影像
		view.capturePane.displayImage(view.fileGrid.uuid);
		
	}
	
	private FrontView view;
	private List<Business> data;
	private ComboBox<Business> selector;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
