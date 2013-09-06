package com.dice.model;

import com.dice.reference.FacetDisplayNameTool;

import java.util.List;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 2:08 PM
 */
public class DisplayFacet {
	private String name;
	private List<DisplayFacetItem> facetItems;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DisplayFacetItem> getFacetItems() {
		return facetItems;
	}

	public void setFacetItems(List<DisplayFacetItem> facetItems) {
		this.facetItems = facetItems;
	}

	public String getDisplayName() {
		return FacetDisplayNameTool.lookup(name);
	}
}
