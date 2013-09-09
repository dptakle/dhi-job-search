package com.dice.service;

import com.dice.model.*;
import com.dice.reference.RefPostalCode;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

/**
 * User: duket
 * Date: 4/6/12
 * Time: 7:47 AM
 */
@Service
public class SearchServiceImpl implements SearchService {
	private static SolrServer solrServer = null;
	private static final int ROW_COUNT = 10;
	private static final String BASE_URL = "/solrprof/search.html?";
	private static final String solrURL = "http://dirac.dev.dice.com:8080/solr";

	private SolrQuery query;
	private QueryResponse response;
	private SolrDocumentList results;
	//private Collection<String> fieldNames;
	//private Iterator<String> nameIterator;
	private int offset = 0;
	private String currentQueryString;
	private Search theData;

	// For the model
	private List<DisplaySeeker> seekers;
	private ResultPageData resultPageData;
	private List<DisplayFacet> facets;
	private List<DisplayBreadCrumb> displayBreadCrumbList;

	private void initSolrServer() {
		try {
			solrServer = new CommonsHttpSolrServer(solrURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public boolean executeSearch(Search theData) {
		System.out.println("SearchServiceImpl.executeSearch() called");
		Iterator<SolrDocument> it;
		String searchText;
		DisplaySeeker currentSeeker;
		Iterator skillIterator;
		StringBuilder skillBuffer;
		this.theData = theData;

		try {
			if (solrServer == null) {
				initSolrServer();
			}

			currentQueryString = theData.toString();
			searchText = theData.getT();
			try {
				offset = theData.getO();
			} catch (Exception e) { /****/}
			query = new SolrQuery();
			query.setFacet(true);
			query.setFacetLimit(10);
			query.setFacetMinCount(1);
			for (int idx = 0; idx < DISPLAY_FACETS.length; idx++) {
				query.addFacetField(DISPLAY_FACETS[idx]);
			}
			query.setStart(offset);
			query.setRows(ROW_COUNT);

			addFilter();
			addGeoSearch();
			if (searchText == null || searchText.trim().length() == 0) {
				query.setQuery("*:*");
			} else {
				query.setQuery(searchText.trim());
			}
			query.setIncludeScore(true);
			System.out.println("QQQQQQQQQUUUUEEEEERRRRRYYYYYY: " + query.toString());
			response = solrServer.query(query);
			results = response.getResults();
			it = results.iterator();
			fillResultData();
			seekers = new ArrayList<DisplaySeeker>();
			while (it.hasNext()) {
				SolrDocument result = it.next();
				currentSeeker = new DisplaySeeker();
				currentSeeker.setName(result.get("seekerName").toString().trim());
				currentSeeker.setEmail(result.get("seekerEmail").toString().trim());
				currentSeeker.setDetailLink("#");
				currentSeeker.setDesiredPosition(result.get("desiredPosition").toString().trim());
				if (result.containsKey("City")) currentSeeker.setLocation(result.get("City").toString().trim());

				skillBuffer = new StringBuilder();
				try {
					skillIterator = result.getFieldValues("skill").iterator();
					skillBuffer = new StringBuilder();
					while (skillIterator.hasNext()) {
						if (skillBuffer.length() > 0) {
							skillBuffer.append(" | ");
						}
						skillBuffer.append(skillIterator.next().toString().trim());
					}
				} catch (Exception fuck) {
					System.out.print("ERROR: " + result.get("dockey"));
					System.out.print('=');
					System.out.println(result.get("score"));
					fuck.printStackTrace();
				}
				currentSeeker.setSkills(skillBuffer.toString().trim());
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX: " + currentSeeker.getName());
				seekers.add(currentSeeker);
			}

			fillFacetsBucket();
			fillBreadCrumbThingy();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean fillResultData() {
		String truncatedQueryString = stripOffsetFromQueryString();
		StringBuilder buf;
		try {
			resultPageData = new ResultPageData();
			resultPageData.setNumFound(results.getNumFound());
			resultPageData.setStartDocument(offset + 1);
			resultPageData.setEndDocument(offset + results.size());

			if (offset > 0) {
				buf = new StringBuilder(BASE_URL);
				buf.append(truncatedQueryString);
				buf.append("&o=");
				buf.append(offset - ROW_COUNT);
				resultPageData.setPrevURL(buf.toString());
			} else {
				resultPageData.setPrevURL(null);
			}

			if ((offset + ROW_COUNT) < results.getNumFound()) {
				buf = new StringBuilder(BASE_URL);
				buf.append(truncatedQueryString);
				buf.append("&o=");
				buf.append(offset + ROW_COUNT);
				resultPageData.setNextURL(buf.toString());
			} else {
				resultPageData.setNextURL(null);
			}

			return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}
}
