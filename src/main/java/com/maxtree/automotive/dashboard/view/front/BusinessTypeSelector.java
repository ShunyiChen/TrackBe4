package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * 业务类型选择器
 * 
 * @author Chen
 *
 */
public class BusinessTypeSelector extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public BusinessTypeSelector(FrontView view) {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidthUndefined();
		this.setHeightUndefined();
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		Label businessTypeText = new Label("业务类型:");
		data = ui.userService.findAssignedBusinesses(loginUser.getUserUniqueId());
		selector = new ComboBox<Business>(null, data);
		// Disallow null selections
		selector.setEmptySelectionAllowed(true);
		selector.setTextInputAllowed(false);
		selector.setPlaceholder("业务类型");
		selector.setWidth("430px");
		selector.setHeight("27px");
		
		this.addComponents(businessTypeText, Box.createHorizontalBox(3), selector);
		this.setComponentAlignment(businessTypeText, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(selector, Alignment.MIDDLE_LEFT);
		
		selector.addValueChangeListener(e->{
			Business business = e.getValue();
			if (business != null) {
				List<DataDictionary> list = ui.businessService.getDataDictionaries(business.getCode());
				int i = 0;
				for (DataDictionary dd : list) {
					i++;
					ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName());
					view.topGrid.addRow(row);
				}
			}
			
		});
	}

	public ComboBox<Business> getSelector() {
		return selector;
	}
	
	/**
	 * 
	 * @param bool
	 */
	public void setSelectorEnabled(boolean bool) {
		selector.setEnabled(bool);
	}

	public void reset() {
		selector.setEnabled(true);
		selector.setSelectedItem(null);
	}
	
	public void setSelected(int businessUniqueId) {
		for (Business item : data) {
			if (item.getBusinessUniqueId() == businessUniqueId) {
				selector.setSelectedItem(item);
				break;
			}
		}
	}
	
	private List<Business> data;
	private ComboBox<Business> selector;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
