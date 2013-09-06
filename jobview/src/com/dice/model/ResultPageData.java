package com.dice.model;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 4:04 PM
 */
public class ResultPageData {
	String nextURL;
	String prevURL;
	long startDocument;
	long endDocument;
	long numFound;

	public String getNextURL() {
		return nextURL;
	}

	public void setNextURL(String nextURL) {
		this.nextURL = nextURL;
	}

	public String getPrevURL() {
		return prevURL;
	}

	public void setPrevURL(String prevURL) {
		this.prevURL = prevURL;
	}

	public long getStartDocument() {
		return startDocument;
	}

	public void setStartDocument(long startDocument) {
		this.startDocument = startDocument;
	}

	public long getEndDocument() {
		return endDocument;
	}

	public void setEndDocument(long endDocument) {
		this.endDocument = endDocument;
	}

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}
}
