package com.dice.model;

import java.util.StringTokenizer;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 2:10 PM
 */
public class BreadCrumbItem {
	private String name;
	private String undoUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUndoUrl() {
		return undoUrl;
	}

	public void setUndoUrl(String undoUrl) {
		this.undoUrl = undoUrl;
	}

	public BreadCrumbItem(String name, String strippedBaseUrl, String currentF, String crumbString) {
		this.name = name;
		StringTokenizer toker;
		String wrk;
		StringBuilder undoF = new StringBuilder();

		try {
			toker = new StringTokenizer(currentF, ";");
			while (toker.hasMoreTokens()) {
				wrk = toker.nextToken();
				if (!wrk.equals(crumbString)) {
					if (undoF.length() > 0) {
						undoF.append(';');
					}
					undoF.append(wrk);
				}
			}

			this.undoUrl = strippedBaseUrl + "&o=0&f=" + undoF.toString();
		} catch (Exception x) {
			this.undoUrl = "#";
		}
	}
}
