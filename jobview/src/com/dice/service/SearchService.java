package com.dice.service;

import com.dice.model.*;

import java.util.List;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 7:46 AM
 */
public interface SearchService {
	public boolean executeSearch(Search theData);
	public List<DisplaySeeker> getSeekers();
	public ResultPageData getResultPageData();
	public List<DisplayFacet> getFacets();
	public List<DisplayBreadCrumb> getDisplayBreadCrumbList();
}
