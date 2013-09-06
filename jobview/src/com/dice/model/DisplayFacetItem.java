package com.dice.model;

import java.util.StringTokenizer;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 1:57 PM
 */
public class DisplayFacetItem {
	private static final String BASE_URL = "/solrprof/search.html?";
	private String name;
	private String url;
	private StringBuilder currentF;
	private long count;

	public DisplayFacetItem(String url, String currentF) {
		this.url = url;
		if (currentF == null) {
			this.currentF = new StringBuilder("");
		} else {
			this.currentF = new StringBuilder(currentF);
		}
		this.url = stripCurrentF();
	}

	public void addFacet(String facet) {
		if (currentF.length() > 0) {
			currentF.append(';');
		}
		currentF.append(facet);
	}

	public String getUrl() {
		return BASE_URL + url + "&o=0&f=" + currentF.toString();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	private String stripCurrentF() {
		String currentQueryString = url;
		StringTokenizer toker;
		StringBuilder buf = new StringBuilder();
		String wrk;

		try {
			toker = new StringTokenizer(currentQueryString, "&");
			while (toker.hasMoreTokens()) {
				wrk = toker.nextToken();
				if (!wrk.startsWith("f=")) {
					if (buf.length() > 0) {
						buf.append('&');
					}
					buf.append(wrk);
				}
			}
			return buf.toString();
		} catch (Exception x) {
			x.printStackTrace();
			return currentQueryString;
		}
	}

}
