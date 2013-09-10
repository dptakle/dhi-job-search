package com.dice.model;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;

public class MLTAnswer {
	private HashSet<String> cdataSet = null;
	private static Gson gson = null;

	private long elapsedTime;
	private long resultCount;
	private List<ResultItem> results;

	public long getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public long getResultCount() {
		return resultCount;
	}
	public void setResultCount(long resultCount) {
		this.resultCount = resultCount;
	}

	public List<ResultItem> getResults() {
		return results;
	}

	public void setResults(List<ResultItem> results) {
		this.results = results;
	}

	public void addResult(ResultItem item) {
		if (results == null) {
			results = new ArrayList<ResultItem>();
		}
		results.add(item);
	}

	@Override
	public String toString() {
		return "elapsedTime=" + elapsedTime + "<br/>\n" +
				"resultCount=" + resultCount + "<br/>\n" +
				"results=" + results;
	}
	public String toHTML() {
		StringBuilder buf = new StringBuilder();
		
		buf.append("<html>\n");
		buf.append("<body>\n");
		buf.append("<pre>\n");
		buf.append(this.toString());
		buf.append("</pre>\n");
		buf.append("</body>\n");
		buf.append("</html>\n");

		return buf.toString();
	}

	public String toJSON() {
		if (gson == null) {
			gson = new Gson();
		}

		return gson.toJson(this);
	}
}
