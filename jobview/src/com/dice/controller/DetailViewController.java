package com.dice.controller;

import com.dice.model.Search;
import com.dice.reference.RefDistance;
import com.dice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HttpSessionMutexListener;

import javax.servlet.http.HttpSession;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 7:50 AM
 */
@Controller
@SessionAttributes("search")
public class DetailViewController {
	@Autowired
	private DetailViewService detailViewService;

	@RequestMapping(value="/view")
	public String doSearch(@ModelAttribute("search") Search theData, ModelMap modelMap) {
		detailViewService.getJobDetails(theData);
		modelMap.addAttribute("seekers", searchService.getSeekers());
		modelMap.addAttribute("resultPageData", searchService.getResultPageData());
		modelMap.addAttribute("facets", searchService.getFacets());
		modelMap.addAttribute("displayBreadCrumbList", searchService.getDisplayBreadCrumbList());

		return "detail";
	}
}
