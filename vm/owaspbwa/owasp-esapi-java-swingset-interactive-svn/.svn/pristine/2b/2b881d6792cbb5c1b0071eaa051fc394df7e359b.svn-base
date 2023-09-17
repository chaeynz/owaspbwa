<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<%@page import="org.owasp.esapi.ESAPI"%>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?<%=querystring%>">Tutorial</a> | 
<b><a href="main?<%=querystring%>&lab">Lab : Browser Caching</a></b>| 
<a href="main?<%=querystring%>&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<%	
	response.addHeader("Cache-Control", "public");
	response.setDateHeader("Expires", System.currentTimeMillis() + 5000 );	
	// TODO : Set the page not to cache
%>

<h2>Browser Caching Lab</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\DataProtectionLab.jsp</h4>

<p>Page is set to cache for 5 seconds, so clicking on the 'Refresh Page' link below should 
show the same value until the cache timeout.</p>

<p>Your goal is to set the page not to cache, so clicking on the 'Refresh Page' link below should show a new set of account balances each time</p>

<p><b>Note: </b>  Some browsers, e.g. Google Chrome, will always request the page from the server without using the cache. For best results, use Firefox or IE and use the "Refresh Page link below, not F5 or the browser's refresh button.</p>
 
<p>Social Security Number: 123-12-1234</p>
<p>Credit Card Number: 1234-1234-1234-1234</p>
<table border="1">
<tr><th>Account Number</th><th>Balance</th></tr>
<tr><td>95812344</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
<tr><td>21231235</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
<tr><td>10823580</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
</table>

<a href="/SwingSet/main?function=DataProtection&lab=">Refresh Page</a>
<br/><br/>


<%@include file="footer.jsp" %>
