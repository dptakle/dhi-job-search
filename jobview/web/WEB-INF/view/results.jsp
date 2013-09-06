<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=us-ascii">
<style type="text/css" media="screen, print, projection">
<!--
body, html
	{margin:0;
	padding:0;
	color:#444;
	background:#999;
	font-family:"Helvetica Neue",Helvetica,Arial,sans-serif;
	font-size:13px}
a, a:visited
	{color:#069;
	text-decoration:none;
	font-weight:bold;
	outline:none}
a:hover, a:active
	{color:#6CF;
	text-decoration:underline}
ul, li
	{list-style-type:none;
	padding-left:5px}
#wrap
	{width:970px;
	margin:0 auto;
	padding:20px 10px 0 10px;
	background:#fff}
#header
	{padding:10px;
	background:#fff}
h1, h2, h3
	{color:#555;
	margin:0;
	padding:0;
	font-family:"Trebuchet MS",Tahoma,Arial,sans-serif}
#nav
	{display:block;
	width:950px;
	margin:0 10px 20px 10px;
	padding:10px 0;
	border-bottom:3px solid #999}
#nav h1
	{display:block;
	width:49%;
	float:left;
	margin:0;
	padding:0}
#nav .back
	{display:block;
	width:49%;
	float:right;
	margin:0;
	padding:10px 0 0 0;
	text-align:right}
#nav .back a
	{margin:0;
	padding:0 0 0 19px}
#main
	{float:left;
	width:680px;
	padding:10px;
	background:#fff}
#sidebar
	{float:left;
	width:230px;
	padding:10px;
	background:#fff}
#footer
	{clear:both;
	padding:5px 10px;
	background:#fff}
#footer p
	{margin:0}
* html #footer
	{height:1px}
.clear
	{clear:both;
	height:0}
.sumRes
	{display:block;
	width:100%;
	margin:0;
	padding:0}
.sumRes h2
	{display:block;
	width:49%;
	float:left;
	margin:0;
	padding:0}
.sumRes .next
	{display:block;
	width:49%;
	float:right;
	margin:0;
	padding:0;
	text-align:right;
	font-size:14px}
.sumRes .previous
	{display:block;
	width:49%;
	float:right;
	margin:0;
	padding:0;
	text-align:left;
	font-size:14px}
-->
</style>
</head>
<body>
<div id="wrap">
<div id="header"><img src="http://employer.dice.com/assets/images/providerlogo_header.gif"> </div>
<div id="nav">
<h1>Current Search</h1>
<div class="back"><a href="/solrprof/search.html">Back to search</a>
</div>
<div class="clear"></div>
</div>
<div id="sidebar">
	<h2>Current search</h2>
	<c:forEach items="${displayBreadCrumbList}" var="currentBreadCrumb">
		<h3><c:out value="${currentBreadCrumb.displayName}" /></h3>
		<ul>
			<c:forEach items="${currentBreadCrumb.breadCrumbItems}" var="currentItem">
				<li>
					<a href="${currentItem.undoUrl}" style="color: #700">(X)</a> ${currentItem.name}
				</li>
			</c:forEach>
		</ul>
	</c:forEach>
<h2>Refine search</h2>
<p>Don't just sit there, click something!</p>
	<c:forEach items="${facets}" var="currentFacet">
		<h3><c:out value="${currentFacet.displayName}" /></h3>
		<ul>
			<c:forEach items="${currentFacet.facetItems}" var="currentItem">
				<li>
					<c:choose>
						<c:when test="${currentItem.name == '0'}">
							<a href="${currentItem.url}">No (${currentItem.count})</a>
						</c:when>
						<c:when test="${currentItem.name == '1'}">
							<a href="${currentItem.url}">Yes (${currentItem.count})</a>
						</c:when>
						<c:otherwise>
							<a href="${currentItem.url}">${currentItem.name} (${currentItem.count})</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:forEach>
		</ul>
	</c:forEach>
</div>
<div id="main">
<div class="sumRes">
<h2>Results ${resultPageData.startDocument} - ${resultPageData.endDocument} of ${resultPageData.numFound}</h2>
	<div class="next">
<c:if test="${resultPageData.prevURL != null}">
	<a href="${resultPageData.prevURL}" style="text-decoration:none">&laquo; Previous</a>
</c:if>
<c:if test="${resultPageData.nextURL != null}">
	<a href="${resultPageData.nextURL}" style="text-decoration:none">Next &raquo;</a>
</c:if>
</div>
<div class="clear"></div>
</div>
	<c:forEach items="${seekers}" var="currentSeeker">
		<p><b>Name:</b>
			<a href="<c:out value="${currentSeeker.detailLink}" />"><c:out value="${currentSeeker.name}" /></a><br />
			<b>E-mail:</b> <c:out value="${currentSeeker.email}" /><br />
			<b>Desired position:</b> <c:out value="${currentSeeker.desiredPosition}" /><br />
			<b>Location:</b> <c:out value="${currentSeeker.location}" /><br />
			<b>Skills:</b> <c:out value="${currentSeeker.skills}" />
		</p>
	</c:forEach>

</div>
<div id="footer">
<p>&nbsp;</p>
</div>
</div>
</body>
</html>
