<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Encoding">Tutorial</a> | 
<a href="main?function=Encoding&lab">Lab : Encoding</a> |
<a href="main?function=Encoding&solution">Solution</a> |
<a href="main?function=XSS&lab">Lab : XSS</a>| 
<a href="main?function=XSS&solution">Solution</a> |
<b><a href="main?function=Canonicalize&lab">Lab : Canonicalize</a></b> | 
<a href="main?function=Canonicalize&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>


<%
	String input = request.getParameter( "input" );
	if ( input == null ) input = "%2&#x35;2%3525&#x32;\\u0036lt;\r\n\r\n%&#x%%%3333\\u0033;&%23101;";

	String canonical = "";
	
	String userMessage = null;
	String logMessage = null;
	
	// TODO : Canonicalize the input

%>

<h2 align="center">Canonicalize Exercise</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\CanonicalizeLab.jsp</h4>
<p>Canonicalization deals with the way in which systems convert data from one form to another. Canonical means the simplest or most standard form of something. Canonicalization is the process of converting something from one representation to the simplest form</p>
<h4>Enter encoded data</h4>
<p>Your goal is to canonicalize the input to see the string decoded, and to display the IntrusionException's user and log messages if necessary. <br />

(For more information on the user and log messages of ESAPI's Exception classes, c.f. the <a href="https://localhost:8443/SwingSet/main?function=ErrorHandling">ErrorHandling Tutorial</a>)</p>

<form action="main?function=Canonicalize&lab" method="POST">
	<table>
	<tr><td>Original</td><td>Decoded</td></tr>
	<tr><td>
		<textarea style="width:300px; height:150px" name="input"><%=ESAPI.encoder().encodeForHTML(input)%></textarea>
	</td><td>
		<textarea style="width:300px; height:150px"><%=ESAPI.encoder().encodeForHTML(canonical)%></textarea>
	</td></tr></table>
	<input type="submit" value="submit">
</form>
<!-- <p>The User Message and Log Message can be obtained by the IntrusionException, thrown by ESAPI.encoder().canonicalize()</p> -->
<p>User Message: <font color="red"><%=ESAPI.encoder().encodeForHTML(userMessage) %></font></p>
<p>Log Message: <font color="red"><%=ESAPI.encoder().encodeForHTML(logMessage) %></font></p><hr>

<%@include file="footer.jsp" %>
