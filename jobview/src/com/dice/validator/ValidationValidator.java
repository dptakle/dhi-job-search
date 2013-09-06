package com.dice.validator;

import com.dice.model.Credentials;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * User: duket
 * Date: 4/10/12
 * Time: 6:54 PM
 */
public class ValidationValidator implements Validator {
	public boolean supports(Class<?> aClass) {
		return Credentials.class.isAssignableFrom(aClass);
	}

	public void validate(Object o, Errors errors) {
	}
}
