package com.dice.controller;

import com.dice.model.JobDetails;
import com.dice.model.Search;
import com.dice.service.DetailViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 7:50 AM
 */
@Controller
public class DetailViewController {
	@Autowired
	private DetailViewService detailViewService;

	@RequestMapping(value="/detail.html", method = RequestMethod.GET)
	public String doSearch(@ModelAttribute("search") Search theData, ModelMap modelMap) {
		System.out.println("doSearch() called");
		JobDetails details = new JobDetails();
		detailViewService.getDetails(theData, details);
		modelMap.addAttribute("details", details);

		
		return "detail";
	}
}
