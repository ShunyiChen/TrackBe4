package com.maxtree.automotive.dashboard.component;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class EmptyValidator implements Validator<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param errMsg
	 */
	public EmptyValidator(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public ValidationResult apply(Object value, ValueContext context) {
		// if(value.length() == 6) {
		// return ValidationResult.ok();
		// } else {
		// return ValidationResult.error(
		// "Must be exactly six characters long");
		// }

		if (value == null) {
			return ValidationResult.error(errMsg);
		} else {
			return ValidationResult.ok();
		}
	}

	private String errMsg;
}
