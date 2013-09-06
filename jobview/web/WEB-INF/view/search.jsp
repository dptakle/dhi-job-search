<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=us-ascii">
<style type="text/css">
<!--
#doc4
	{border-bottom:4px solid #555}
#solr1
	{display:block;
	margin:0 3em;
	padding:5em 0}
h2
	{display:block;
	margin:0 0 1em 0;
	padding:.5em 0;
	font-size:24px;
	border-bottom:4px solid #999}
label
	{float:left;
	padding:0 5px 0 0;
	line-height:1.85;
	font-weight:bold;
	color:#300}
.text
	{float:left;
	padding:0 8px 0 0}
.textSearch
	{display:block;
	width:200px;
	height:1.85em;
	margin:0 0 15px 0;
	border:0px solid #666;
	line-height:1.85}
.zipCode
	{display:block;
	float:left;
	width:50px;
	height:1.85em;
	margin:0 20px 15px 0;
	border:1px solid #666;
	line-height:1.85}
select
	{width:65px;
	border:1px solid #666;
	height:1.85em;
	line-height:1.85}
.submitBTN
	{display:block;
	height:26px;
	width:80px;
	margin:0 0 10px 0;
	padding:0;
	border:0px solid #FFF}
-->
</style>
</head>
<body class="yui-skin-sam">
<div id="doc4" class="yui-t7">
	<div id="solr1">
		<h2>SOLR List Pull Utility - Search</h2>
		<form:form method="get" action="search.html" commandName="search">
			<div class="text">
				<form:label path="t">Search text:</form:label> <form:input  path="t" />
			</div>
			<div class="clear"></div>
			<div class="text">
				<form:label path="z">Postal code:</form:label> <form:input path="z" maxlength="5" size="5"/>
			</div>
			<div class="clear"></div>
			<div class="text">
				<form:label path="d">Distance:</form:label>
				<form:select path="d" items="${distances}" itemLabel="description" itemValue="distance" />
			</div>
			<div class="clear"></div>
			<div class="location">
				<form:hidden path="o"/>
				<form:hidden path="f"/>
			<input class="submitBTN" type="submit" name="GO" value="Search" />
			</div>
		</form:form>
	</div>
</div>
</body>
</html>
