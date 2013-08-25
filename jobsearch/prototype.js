var apiServer = "http://dev.api.dice.com";
var lastReportTime = 0;
var currentSearchUrl;

window.onload = init;

function init() {
	search("/job-search/query.js?page=1&sort=1&sd=d&callback=displayResults");
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
}

function displayCurrentUrl() {
	alert(currentSearchUrl);
}

function searchWithText() {
	alert("keyword: " + document.getElementById('keyword').value);
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
		div.innerHTML = "<a href=\"" + result.detailUrl + "\" target=\"dicedetail\">" + result.jobTitle + "</a> " + result.company + " " + result.location;
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

	displayFacets(answer.facets);
}

function displayFacets(facets) {
	var navbarDiv = document.createElement("div");
	navbarDiv.id = "navbar";
	var container = document.getElementById("container");
	container.replaceChild(navbarDiv, document.getElementById("navbar"));
	navbarDiv = document.getElementById("navbar");

	for (var i = 0; i < facets.length; i++) {
		var previousFacet;
		var facet = facets[i];
		var div = document.createElement("div");
		div.setAttribute("class", "facet");
		div.innerHTML = "<strong>" + facet.name + "</strong>";
		displayFacet(facets[i], div);
		if (navbarDiv.childElementCount == 0) {
			navbarDiv.appendChild(div);
		} else {
			insertAfter(div, previousFacet);
		}
		previousFacet = div;
	}
}

function displayFacet(facet, facetDiv) {
	var facetItem;
	var li;
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
	facetDiv.appendChild(ul);
}

//create function, it expects 2 values.
function insertAfter(newElement,targetElement) {
    //target is what you want it to go after. Look for this elements parent.
    var parent = targetElement.parentNode;

    //if the parents lastchild is the targetElement...
    if(parent.lastchild == targetElement) {
        //add the newElement after the target element.
        parent.appendChild(newElement);
        } else {
        // else the target has siblings, insert the new element between the target and it's next sibling.
        parent.insertBefore(newElement, targetElement.nextSibling);
        }
}
