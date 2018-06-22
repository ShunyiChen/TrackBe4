package com.maxtree.automotive.dashboard.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class Box extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param htmlStr
	 */
	public Box(String htmlStr) {
		super(htmlStr, ContentMode.HTML);
	}
	
	/**
	 * 
	 * @param px
	 * @return
	 */
	public static Box createHorizontalBox(int px) {
		Box box = new Box("");
		box.setWidth(px+"px");
		box.setHeight("100%");
		return box;
    }
	
	/**
	 * 
	 * @param px
	 * @return
	 */
	public static Box createVerticalBox(int px) {
		Box box = new Box("");
		box.setWidth("100%");
		box.setHeight(px+"px");
        return box;
    }
}
