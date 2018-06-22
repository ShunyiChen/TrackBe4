package com.maxtree.automotive.dashboard.component;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class TextFieldWithOption extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param caption
	 */
	public TextFieldWithOption(String caption) {
		this.setCaption(caption);
		this.setSpacing(false);
		this.setMargin(false);
		
		List<String> data = new ArrayList<String>();
		data.add("正确");
		data.add("错误");
		ComboBox<String> option = new ComboBox<>(null, data);
		option.setEmptySelectionAllowed(false);
		option.setTextInputAllowed(false);
		option.setSelectedItem("正确");
		txtField.setWidth("100%");
		txtField.setHeight("26px");
		option.setWidth("90px");
		option.setHeight("26px");
		this.addComponents(txtField, option);
		this.setComponentAlignment(txtField, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(option, Alignment.MIDDLE_RIGHT);
		this.setExpandRatio(txtField, 0.94f);
		this.setExpandRatio(txtField, 0.06f);
	}
	
	public void focus() {
		txtField.focus();
	}
	
	private TextField txtField = new TextField();
}
