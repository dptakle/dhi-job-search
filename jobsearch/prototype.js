var jobViewStub = "http://localhost:8080/jobview/detail.html?k=";
var apiServer = "http://dev.api.dice.com";
var lastReportTime = 0;
var currentSearchUrl;
var expandedFacet = '';
var expandedBreadCrumb = '';
var jobBoard = 'dice';

window.onload = init;

function init() {
	search("/job-search/" + jobBoard + "/query.js?page=1&pgcnt=30&callback=displayResults");
}

function search(queryString) {
	var url = apiServer + queryString;
	if (url.indexOf("?") > -1){
		url = url + "&";
	} else {
		url = url + "?";
	}
	url = url + "random=" + (new Date()).getTime();

	var newScriptElement = document.createElement("script");
	newScriptElement.setAttribute("src", url);
	newScriptElement.setAttribute("id", "jsonp");
	newScriptElement.setAttribute("type", "text/javascript");
	var oldScriptElement = document.getElementById("jsonp");
	var head = document.getElementsByTagName("head")[0];
	if (oldScriptElement == null) {
		head.appendChild(newScriptElement);
	} else {
		head.replaceChild(newScriptElement, oldScriptElement);
	}

	currentSearchUrl = queryString;
	displayCurrentUrl();
}

function displayCurrentUrl() {
	console.log(currentSearchUrl);
}

function setJobBoard(newJobBoard) {
	document.getElementById('keyword').value = '';
	jobBoard = newJobBoard;
	init();
}

function searchWithText() {
	var wrk = currentSearchUrl.split("?");
	var query = ""
	if (wrk.length == 2) {
		var params = wrk[1].split("&");
		for (var i = 0; i < params.length; i++) {
			if (params[i].indexOf("random=") == -1 && params[i].indexOf("page=") == -1 && params[i].indexOf("text=") == -1) {
				if (query.length == 0) {
					query += "?";
				} else {
					query += "&";
				}
				query += params[i];
			}
		}

		if (query.length == 0) {
			query += "?";
		} else {
			query += "&";
		}
		query += "text=";
		query += encodeURIComponent(document.getElementById('keyword').value);
		query = "/job-search/" + jobBoard + "/query.js" + query;
		search(query);
	} else {
		console.log("wrk length is " + wrk.length);
	}
}

function displayResults(answer) {
	var answerBuffer = "";
	var pagingBuffer = "";
	var answerDiv = document.getElementById("answer");

	var resultsDiv = document.createElement("div");
	resultsDiv.id = "results";
	var container = document.getElementById("container");
	container.replaceChild(resultsDiv, document.getElementById("results"));
	resultsDiv = document.getElementById("results");

	for (var i = 0; i < answer.results.length; i++) {
		var previousResult;
		var result = answer.results[i];
		var div = document.createElement("div");
		div.setAttribute("class", "resultItem");
		div.innerHTML = "<a href=\"" + jobViewStub + result.id + "\" target=\"dicedetail\">" + result.jobTitle + "</a> " + result.company + " " + result.location;
		if (resultsDiv.childElementCount == 0) {
			resultsDiv.appendChild(div);
		} else {
			insertAfter(div, previousResult);
		}
		previousResult = div;
	}
	answerBuffer += "<p>" + answer.firstDocument + " - " + answer.lastDocument + " of " +  answer.resultCount + " documents found</p>";

	if (typeof(answer.previousUrl) != "undefined") {
		pagingBuffer += " <div style=\"float: left; cursor: pointer;\" href=\"#\" onclick=\"search('" + answer.previousUrl + "');\">&laquo; prev &nbsp;</div>";
	}

	if (typeof(answer.nextUrl) != "undefined") {
		pagingBuffer += " <div style=\"cursor: pointer;\" href=\"#\" onclick=\"search('" + answer.nextUrl + "');\">&nbsp; next &raquo;</div>";
	}

	if (pagingBuffer.length > 0) {
		answerBuffer += "<p style=\"width: 200px;\">" + pagingBuffer + "<div style=\"clear: both;\" /></p>";
	}

	answerDiv.innerHTML = answerBuffer;

	if (typeof(answer.breadCrumb) != "undefined") {
		displayBreadCrumbs(answer.breadCrumb);
	}

	if (typeof(answer.facets) != "undefined") {
		displayFacets(answer.facets);
	}
}

function displayBreadCrumbs(breadcrumbs) {
	var labelDiv;
	var breadcrumbDiv = document.createElement("div");
	breadcrumbDiv.id = "breadcrumb";
	var navbar = document.getElementById("navbar");
	navbar.replaceChild(breadcrumbDiv, document.getElementById("breadcrumb"));
	breadcrumbDiv = document.getElementById("breadcrumb");

	for (var i = 0; i < breadcrumbs.length; i++) {
		var previousBreadcrumb;
		var breadcrumb = breadcrumbs[i];
		var div = document.createElement("div");
		div.setAttribute("class", "breadcrumb");
		div.innerHTML = "<strong>" + breadcrumb.name + "</strong>";
		displayBreadCrumb(breadcrumbs[i], div);
		if (breadcrumbDiv.childElementCount == 0) {
			labelDiv = document.createElement("h3");
			labelDiv.innerHTML = "Current search";
			breadcrumbDiv.appendChild(labelDiv);
			insertAfter(div, labelDiv);
		} else {
			insertAfter(div, previousBreadcrumb);
		}
		previousBreadcrumb = div;
	}
}

function displayBreadCrumb(breadcrumb, breadcrumbDiv) {
	var breadCrumbItem;
	var li;
	var id;
	var previousItem;
	var ul = document.createElement("ul");
	for (var i = 0; i < breadcrumb.breadCrumbItems.length; i++) {
		breadCrumbItem = breadcrumb.breadCrumbItems[i];
		li = document.createElement("li");
		li.setAttribute("onclick", "search('" + breadCrumbItem.undoUrl + "');");
		li.setAttribute("class", "breadCrumbItem");
		li.innerHTML = "[X] " + breadCrumbItem.name;
		if (typeof(previousItem) == "undefined") {
			ul.appendChild(li);
		} else {
			insertAfter(li, previousItem);
		}
		previousItem = li;
	}
	id = "b" + Math.floor((Math.random()*100000)+1);
	ul.style.display = 'block';
	ul.setAttribute("id", id);
	breadcrumbDiv.setAttribute("onclick", "toggleBreadCrumbState('" + id + "')");
	breadcrumbDiv.style.cursor = "pointer";
	breadcrumbDiv.appendChild(ul);
}

function displayFacets(facets) {
	var labelDiv;
	var refinementDiv = document.createElement("div");
	refinementDiv.id = "refinement";
	var navbar = document.getElementById("navbar");
	navbar.replaceChild(refinementDiv, document.getElementById("refinement"));
	refinementDiv = document.getElementById("refinement");

	for (var i = 0; i < facets.length; i++) {
		var previousFacet;
		var facet = facets[i];
		var div = document.createElement("div");
		div.setAttribute("class", "facet");
		div.innerHTML = "<strong>" + facet.name + "</strong>";
		displayFacet(facets[i], div);
		if (refinementDiv.childElementCount == 0) {
			labelDiv = document.createElement("h3");
			labelDiv.innerHTML = "Refinements";
			refinementDiv.appendChild(labelDiv);
			insertAfter(div, labelDiv);
		} else {
			insertAfter(div, previousFacet);
		}
		previousFacet = div;
	}
}

function displayFacet(facet, facetDiv) {
	var facetItem;
	var li;
	var id;
	var previousItem;
	var ul = document.createElement("ul");
	for (var i = 0; i < facet.facetItems.length; i++) {
		facetItem = facet.facetItems[i];
		li = document.createElement("li");
		li.setAttribute("onclick", "search('" + facetItem.url + "');");
		li.setAttribute("class", "facetItem");
		li.innerHTML = facetItem.name + "(" + facetItem.count + ")";
		if (typeof(previousItem) == "undefined") {
			ul.appendChild(li);
		} else {
			insertAfter(li, previousItem);
		}
		previousItem = li;
	}
	id = "f" + Math.floor((Math.random()*100000)+1);
	ul.style.display = 'none';
	ul.setAttribute("id", id);
	facetDiv.setAttribute("onclick", "toggleFacetState('" + id + "')");
	facetDiv.style.cursor = "pointer";
	facetDiv.appendChild(ul);
}

function insertAfter(newElement,targetElement) {
	var parent = targetElement.parentNode;

	if (parent.lastchild == targetElement) {
		parent.appendChild(newElement);
	} else {
		parent.insertBefore(newElement, targetElement.nextSibling);
	}
}

function expandFacet(id) {
	document.getElementById(id).style.display = 'block';
	expandedFacet = id;
	return true;
}

function collapseFacet(id) {
	document.getElementById(id).style.display = 'none';
	expandedFacet = '';
	return true;
}

function toggleFacetState(id){
	if (expandedFacet == '') {
		expandFacet(id);
	} else if (expandedFacet == id) {
		collapseFacet(id);
	} else {
		collapseFacet(expandedFacet);
		expandFacet(id);
	}
	return true;
}

function expandBreadCrumb(id) {
	document.getElementById(id).style.display = 'block';
	expandedBreadCrumb = id;
	return true;
}

function collapseBreadCrumb(id) {
	document.getElementById(id).style.display = 'none';
	expandedBreadCrumb = '';
	return true;
}

function toggleBreadCrumbState(id){
	if (expandedBreadCrumb == '') {
		expandBreadCrumb(id);
	} else if (expandedBreadCrumb == id) {
		collapseBreadCrumb(id);
	} else {
		collapseBreadCrumb(expandedBreadCrumb);
		expandBreadCrumb(id);
	}
	return true;
}
