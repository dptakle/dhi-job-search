var apiServer = "http://dev.api.dice.com";
var lastReportTime = 0;

window.onload = init;

function init() {
	search("/job-search/query.js?page=1&sort=1&sd=d&callback=displayResults");
}

function search(queryString) {
	console.log("search() called");
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
}

function displayResults(answer) {
	var answerBuffer = "";
	var pagingBuffer = "";
	var answerDiv = document.getElementById("answer");

	var resultsDiv = document.createElement("div");
	resultsDiv.id = "results";
	//var container = document.getElementsByTagName("body")[0].getElementById("container");
	var container = document.getElementById("container");
	container.replaceChild(resultsDiv, document.getElementById("results"));
	resultsDiv = document.getElementById("results");

	for (var i = 0; i < answer.results.length; i++) {
		var result = answer.results[i];
		var div = document.createElement("div");
		div.setAttribute("class", "resultItem");
		div.innerHTML = "<a href=\"" + result.detailUrl + "\" target=\"dicedetail\">" + result.jobTitle + "</a> " + result.company + " " + result.location;
		
		if (resultsDiv.childElementCount == 0) {
			resultsDiv.appendChild(div);
		} else {
			resultsDiv.insertBefore(div, resultsDiv.firstChild);
		}
	}
	answerBuffer += "<p>" + answer.firstDocument + " - " + answer.lastDocument + " of " +  answer.resultCount + " documents found</p>";

	if (typeof(answer.previousUrl) != "undefined") {
		pagingBuffer += " <div href=\"#\" onclick=\"search('" + answer.previousUrl + "');\">&laquo; prev</div>";
	}

	if (typeof(answer.nextUrl) != "undefined") {
		pagingBuffer += " <div href=\"#\" onclick=\"search('" + answer.nextUrl + "');\">next &raquo;</div>";
	}

	if (pagingBuffer.length > 0) {
		answerBuffer += "<p>" + pagingBuffer + "</p>";
	}

	console.log(answerBuffer);
	answerDiv.innerHTML = answerBuffer;
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
