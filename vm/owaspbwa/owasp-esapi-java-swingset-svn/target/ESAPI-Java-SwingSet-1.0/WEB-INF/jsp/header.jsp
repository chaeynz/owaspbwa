
<%@page import="org.owasp.esapi.*"%>
<%@page import="org.owasp.esapi.errors.*"%>
<%@page import="org.owasp.esapi.AccessReferenceMap"%>
<%@page import="org.owasp.esapi.codecs.*" %>

<%
String function = (String)request.getAttribute("function");
String title = "ESAPI SwingSet - " + function;
String pageHeader = "ESAPI Swingset - " + function;

// Find the mode
String mode;
if (request.getParameter("mode") == null || request.getParameter("mode").equalsIgnoreCase("tutorial")) {
	mode = "Tutorial";
} else if (request.getParameter("mode").equalsIgnoreCase("secure")) {
	mode = "Secure";
} else if (request.getParameter("mode").equalsIgnoreCase("insecure")) {
	mode = "Insecure";
} else { // Default to tutorial mode
	mode = "Tutorial";
}

if (mode.equals("Secure")) {
	title += ": Secured by ESAPI";
	pageHeader += ": Secured by ESAPI";
} else if (mode.equals("Insecure")) {
	title += ": Insecure";
	pageHeader += ": Insecure";
}

%>

<html>
<head>
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="style/style.css" />

</head>

<% if ( mode.equals("Tutorial") ) { %> <body> <% } %>
<% if ( mode.equals("Insecure") ) { %> <body bgcolor="#EECCCC"> <% } %>
<% if ( mode.equals("Secure") ) { %> <body bgcolor="#BBDDBB"> <% } %>
<div id="container">
	<div id="holder">
		<div id="logo"><img src="style/images/owasp-logo_130x55.png" width="130" height="55" alt="owasp_logo" title="owasp_logo"></div>
<h2><%=pageHeader %></h2>

<div id="navigation">
<a href="main">Home</a> | 
<% if ( mode.equals("Tutorial") ) { %><b><%}%><a href="main?function=<%=function%>&mode=tutorial">Tutorial</a><% if ( mode.equals("Tutorial") ) { %></b><%} %> | 
<% if ( mode.equals("Insecure") ) { %><b><%}%><a href="main?function=<%=function%>&mode=insecure">Insecure Demo</a><% if ( mode.equals("Insecure") ) { %></b><%} %> | 
<% if ( mode.equals("Secure") ) { %><b><%}%><a href="main?function=<%=function%>&mode=secure">Secure Demo</a><% if ( mode.equals("Secure") ) { %></b><%} %>
</div>
<div id="header"></div>
<p>
<hr>

