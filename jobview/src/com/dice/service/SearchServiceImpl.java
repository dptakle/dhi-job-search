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

	private final String[] DISPLAY_FACETS = {
			"skill",
			"City",
			"seekerContactState",
			"seekerContactCountry",
			"workAuthorization",
			"techExperienceRange",
			"employmentType",
			"jobClassification",
			"highestDegree",
			"securityClearance",
			"thirdParty",
			"diceCommunication",
			"thirdPartyCommunication",
			"diceAdvisorCommunication",
			"resumeStatusCommunication",
			"willingtoRelocate",
			"searchableStatus"
	};

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

	public List<DisplaySeeker> getSeekers() {
		return seekers;
	}

	public ResultPageData getResultPageData() {
		return resultPageData;
	}

	public List<DisplayFacet> getFacets() {
		return facets;
	}

	public List<DisplayBreadCrumb> getDisplayBreadCrumbList() {
		return displayBreadCrumbList;
	}

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

	private boolean addFilter() {
		String f = theData.getF();
		StringTokenizer semicolonToker;
		StringTokenizer colonToker;
		String wrk;

		try {
			if (f != null && f.trim().length() > 0) {
				semicolonToker = new StringTokenizer(f, ";");
				while (semicolonToker.hasMoreTokens()) {
					wrk = semicolonToker.nextToken();
					colonToker = new StringTokenizer(wrk, ":");
					query.addFilterQuery(colonToker.nextToken() + ":\"" + colonToker.nextToken() + "\"");
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean addGeoSearch() {
		String postalCodeKey = theData.getZ();
		String distance = Integer.toString(theData.getD());
		String wrk;

		try {
			if (RefPostalCode.getPostalCodeMap().containsKey(postalCodeKey)) {
				query.addFilterQuery("{!bbox}");
				query.setParam("sfield", "contactGeoCode");
				query.setParam("pt", RefPostalCode.getPostalCodeMap().get(postalCodeKey));
				query.setParam("d", distance);
				//System.out.println("query: " + query.toString());
			}
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

	private boolean fillFacetsBucket() {
		List<FacetField.Count> facetAnswers;
		Iterator<FacetField.Count> countIterator;
		FacetField.Count currentCount;
		DisplayFacet currentFacet;
		DisplayFacetItem currentItem;
		List<DisplayFacetItem> currentItemList;
		int itemCount = 0;

		try {
			facets = new ArrayList<DisplayFacet>();
			for (int idx = 0; idx < DISPLAY_FACETS.length; idx++) {
				facetAnswers = response.getFacetField(DISPLAY_FACETS[idx]).getValues();
				countIterator = facetAnswers.iterator();
				currentFacet = new DisplayFacet();
				currentFacet.setName(DISPLAY_FACETS[idx]);
				currentItemList = new ArrayList<DisplayFacetItem>();
				itemCount = 0;
				while (itemCount++ < 20 && countIterator.hasNext()) {
					currentCount = countIterator.next();
					currentItem = new DisplayFacetItem(stripOffsetFromQueryString(), theData.getF());
					currentItem.setName(currentCount.getName().trim());
					currentItem.setCount(currentCount.getCount());
					currentItem.addFacet(DISPLAY_FACETS[idx] + ":" + currentCount.getName().trim());
					currentItemList.add(currentItem);
				}
				currentFacet.setFacetItems(currentItemList);
				facets.add(currentFacet);
			}

			return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}

	private boolean fillBreadCrumbThingy() {
		StringTokenizer semicolonToker;
		StringTokenizer colonToker;
		String currentF = theData.getF();
		String key, val, wrk;
		HashMap<String, List<BreadCrumbItem>> breadCrumbMap;
		List<BreadCrumbItem> crumbList;
		BreadCrumbItem currentCrumb;
		String strippedBaseUrl;
		DisplayBreadCrumb displayBreadCrumb;

		try {
			if (currentF == null) {
				return true;
			}
			strippedBaseUrl = BASE_URL + stripParametersFromQueryString(new String[] {"o", "f"});
			semicolonToker = new StringTokenizer(currentF, ";");
			breadCrumbMap = new HashMap<String, List<BreadCrumbItem>>();

			while (semicolonToker.hasMoreElements()) {
				wrk = semicolonToker.nextToken(); // e.g. skill:java
				System.out.println("wrk: " + wrk);
				colonToker = new StringTokenizer(wrk, ":");
				key = colonToker.nextToken();
				val = colonToker.nextToken();
				currentCrumb = new BreadCrumbItem(val, strippedBaseUrl, currentF, wrk);
				if (breadCrumbMap.containsKey(key)) {
					crumbList = breadCrumbMap.get(key);
				} else {
					crumbList = new ArrayList<BreadCrumbItem>();
				}
				crumbList.add(currentCrumb);
				breadCrumbMap.put(key, crumbList);
			}

			displayBreadCrumbList = new ArrayList<DisplayBreadCrumb>();
			for (int idx = 0; idx < DISPLAY_FACETS.length; idx++) {
				if (breadCrumbMap.containsKey(DISPLAY_FACETS[idx])) {
					displayBreadCrumbList.add(new DisplayBreadCrumb(DISPLAY_FACETS[idx], breadCrumbMap.get(DISPLAY_FACETS[idx])));
				}
			}

			return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}

	private String stripOffsetFromQueryString() {
		StringTokenizer toker;
		StringBuilder buf = new StringBuilder();
		String wrk;

		try {
			toker = new StringTokenizer(currentQueryString, "&");
			while (toker.hasMoreTokens()) {
				wrk = toker.nextToken();
				if (!wrk.startsWith("o=")) {
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

	private String stripParametersFromQueryString(String[] whack) {
		StringBuilder buf = new StringBuilder();
		HashSet<String> whackSet = new HashSet<String>();

		try {
			for (int idx = 0; idx < whack.length; idx++) {
				whackSet.add(whack[idx]);
			}

			if (whackSet.contains("t")) {
				buf.append("t=");
			} else {
				buf.append("t=");
				buf.append(theData.getT());
			}

			buf.append('&');
			if (whackSet.contains("z")) {
				buf.append("z=");
			} else {
				buf.append("z=");
				buf.append(theData.getZ());
			}

			buf.append('&');
			if (!whackSet.contains("f")) {
				buf.append("f=");
				buf.append(theData.getF());
			}

			buf.append('&');
			if (whackSet.contains("d")) {
				buf.append("d=");
			} else {
				buf.append("d=");
				buf.append(theData.getD());
			}

			buf.append('&');
			if (!whackSet.contains("o")) {
				buf.append("o=");
				buf.append(theData.getO());
			}
			return buf.toString();
		} catch (Exception x) {
			x.printStackTrace();
			return "";
		}


	}
}
