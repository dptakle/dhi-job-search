package com.dice.model;

import com.dice.reference.FacetDisplayNameTool;

import java.util.List;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 2:09 PM
 */
public class DisplayBreadCrumb {
	private String name;
	private List<BreadCrumbItem> breadCrumbItems;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BreadCrumbItem> getBreadCrumbItems() {
		return breadCrumbItems;
	}

	public void setBreadCrumbItems(List<BreadCrumbItem> breadCrumbItems) {
		this.breadCrumbItems = breadCrumbItems;
	}

	public void addBreadCrumbItem(BreadCrumbItem crumb) {

	}
	public DisplayBreadCrumb(String name, List<BreadCrumbItem> breadCrumbItems) {
		this.name = name;
		this.breadCrumbItems = breadCrumbItems;
	}

	public String getDisplayName() {
		return FacetDisplayNameTool.lookup(name);
	}
}
