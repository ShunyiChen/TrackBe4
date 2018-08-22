package com.maxtree.automotive.dashboard.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class Hr extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Hr() {
		// ClickLabel all = new ClickLabel("<span style='cursor:pointer;font-size:13px;color: #2499DD;'>全选</span>");
        super("<hr style='height:1px;border:none;border-top:1px solid #F0F0F0;line-height:1px;padding:0px;margin-top:1px;margin-bottom:1px;'/>", ContentMode.HTML);
        setWidth("100%");
        setHeightUndefined();
    }
}
