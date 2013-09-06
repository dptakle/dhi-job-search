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
public class SearchController {
	@Autowired
	private SearchService searchService;

	@RequestMapping(value="/search")
	public String showSearch(ModelMap modelMap, HttpSession session) {
		System.out.println("showSearch() called");
		modelMap.addAttribute("distances", RefDistance.getDistances());
		Search search;
		if (session.getAttribute("search") == null) {
			search = new Search();
		} else {
			search = (Search)session.getAttribute("search");
			search.setO(0);
			search.setF("");
		}
		modelMap.addAttribute("search", search);
		return "search";
	}

	@RequestMapping(value="/search", params = "o")
	public String doSearch(@ModelAttribute("search") Search theData, ModelMap modelMap) {
		System.out.println("showSearch() called");
		searchService.executeSearch(theData);
		modelMap.addAttribute("seekers", searchService.getSeekers());
		modelMap.addAttribute("resultPageData", searchService.getResultPageData());
		modelMap.addAttribute("facets", searchService.getFacets());
		modelMap.addAttribute("displayBreadCrumbList", searchService.getDisplayBreadCrumbList());

		return "results";
	}
}
