package com.maxtree.automotive.dashboard.view.front;

import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

/**
 * 
 * @author chens
 *
 */
public class BusinessTypePane extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public BusinessTypePane(InputViewIF view) {
		this.view = view;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("业务类型");
		this.addStyleName("picture-pane");
		this.setWidth("100%");
		this.setHeightUndefined();
		// 业务类型选择
		selector = new BusinessTypeSelector(view);
		HorizontalLayout main = new HorizontalLayout();
		main.setSpacing(false);
		main.setMargin(new MarginInfo(false, false, true, true));
		main.setWidth("100%");
		main.setHeightUndefined();
		main.addComponent(selector);
		
		this.setContent(main);
	}
	
	/**
	 * Get value
	 * 
	 * @return
	 */
	public Business getSelected() {
		return selector.getValue();
	}
	
	/**
	 * Clear field's value
	 */
	public void fieldClear() {
		selector.setEmpty();
	}
	
	/**
	 * 业务代码
	 * @param code
	 */
	public void populate(String code) {
		selector.populate(code);
	}
	
	private InputViewIF view = null;
	private BusinessTypeSelector selector;
}
