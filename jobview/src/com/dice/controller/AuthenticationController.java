package com.dice.controller;

import com.dice.model.Credentials;
import com.dice.service.ValidationService;
import com.dice.validator.ValidationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * User: duket
 * Date: 4/10/12
 * Time: 10:50 AM
 */
@Controller
public class AuthenticationController {
	@Autowired
	private ValidationService validationService;

	@InitBinder
	protected void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.setValidator(new ValidationValidator());
	}

	@RequestMapping( value = "/login", method = RequestMethod.POST)
	public String processAuthenticationRequest(	@ModelAttribute("credentials") Credentials credentials,
												BindingResult bindingResult,
												HttpSession session) {
		if (validationService.validateCredentials(credentials)) {
			session.setAttribute("solrprof.authenticated", true);
			return "redirect:search.html";
		} else {
			bindingResult.addError(new ObjectError("u", "Login failed!"));
			return "login";
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage(ModelMap modelMap) {
		Credentials credentials = new Credentials();
		modelMap.addAttribute("credentials", credentials);
		return "login";
	}
}
