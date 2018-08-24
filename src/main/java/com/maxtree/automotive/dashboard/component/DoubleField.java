package com.maxtree.automotive.dashboard.component;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextField;
import com.vaadin.data.HasValue.ValueChangeListener;

/**
 * 
 * @author chens
 *
 */
public class DoubleField extends TextField implements ValueChangeListener<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param caption
	 */
	public DoubleField(String caption) {
		this();
		this.setCaption(caption);
	}
	
	/**
	 * 
	 */
	public DoubleField() {
		setValueChangeMode(ValueChangeMode.EAGER);
		addValueChangeListener(this);
		lastValue = "";
	}

	@Override
	public void valueChange(ValueChangeEvent<String> event) {
		String text = (String) event.getValue();
	    try {
	        new Double(text);
	        lastValue = text;
	    } catch (NumberFormatException e) {
//	        setValue(lastValue);
	    }
	}
	
	public String lastValue;
}
