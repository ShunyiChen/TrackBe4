package com.maxtree.automotive.dashboard.view.front;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Business;
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
	public BusinessTypeSelector() {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidthUndefined();
		this.setHeightUndefined();
		
		User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		int companyUniqueId = loginUser.getCompanyUniqueId();
		Label businessTypeText = new Label("业务类型:");
		data = ui.businessService.findAllByCompanyUniqueId(companyUniqueId);
		selector = new ComboBox<Business>(null, data);
//		selector.setTextInputAllowed(false);
		// Disallow null selections
		selector.setEmptySelectionAllowed(false);
		selector.setPlaceholder("选择一个业务类型");
		selector.setWidth("430px");
		selector.setHeight("27px");
		
		
		this.addComponents(businessTypeText, Box.createHorizontalBox(3), selector);
		this.setComponentAlignment(businessTypeText, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(selector, Alignment.MIDDLE_LEFT);
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
