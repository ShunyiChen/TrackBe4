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
	 * @param defaultValue
	 */
	public DoubleField(double defaultValue) {
		this.setValue(defaultValue+"");
		setValueChangeMode(ValueChangeMode.EAGER);
		addValueChangeListener(this);
	}
	
	/**
	 * 
	 */
	public DoubleField() {
		setValueChangeMode(ValueChangeMode.EAGER);
		addValueChangeListener(this);
	}

	@Override
	public void valueChange(ValueChangeEvent<String> event) {
		String text = (String) event.getValue();
	    try {
	        new Double(text);
	    } catch (NumberFormatException e) {
//	        setValue(lastValue);
	    }
	}
	
}
