
<%@page import="org.owasp.esapi.*"%>
<%@page import="org.owasp.esapi.errors.*"%>
<%@page import="org.owasp.esapi.AccessReferenceMap"%>
<%@page import="org.owasp.esapi.codecs.*" %>

<%
String function = (String)request.getAttribute("function");
String title = "ESAPI SwingSet Interactive - " + function;
String querystring = request.getQueryString();
String pageHeader = "ESAPI Swingset Interactive - " + function;

int i1 = querystring.indexOf("&solution");
boolean secure = ( i1 != -1 );
if ( secure ) {
	querystring = querystring.substring( 0, i1 );
	title += ": Solution with ESAPI";
	pageHeader += ": Solution with ESAPI";
}

int i2 = querystring.indexOf("&lab");
boolean insecure = ( i2 != -1 );
if ( insecure ) {
	querystring = querystring.substring( 0, i2 );
	title += ": Lab";
	pageHeader += ": Lab";
}

%>

<html>
<head>
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="style/style.css" />

</head>

<% if ( !insecure && !secure ) { %> <body> <% } %>
<% if ( insecure ) { %> <body bgcolor="#EECCCC"> <% } %>
<% if ( secure ) { %> <body bgcolor="#BBDDBB"> <% } %>
<div id="container">
	<div id="holder">
		<div id="logo"><img src="style/images/owasp-logo_130x55.png" width="130" height="55" alt="owasp_logo" title="owasp_logo"></div>
<h2><%=pageHeader %></h2>

