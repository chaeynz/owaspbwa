<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?<%=querystring%>">Tutorial</a> | 
<a href="main?<%=querystring%>&lab">Lab : Browser Caching</a>| 
<b><a href="main?<%=querystring%>&solution">Solution</a></b>
</div>
<div id="header"></div>
<p>
<hr>

<%
	ESAPI.httpUtilities().setNoCacheHeaders(ESAPI.httpUtilities().getCurrentResponse());
%>

<h2>Browser Caching Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\DataProtectionLab.jsp</h4>

<p>Page is not set to cache using <i>ESAPI.httpUtilities().setNoCacheHeaders()</i>, so pressing refresh button should 
show a new set of account balances each time</p>

<p>Social Security Number: 123-12-1234</p>
<p>Credit Card Number: 1234-1234-1234-1234</p>
<table border="1">
<tr><th>Account Number</th><th>Balance</th></tr>
<tr><td>95812344</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
<tr><td>21231235</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
<tr><td>10823580</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
</table>

<br/><br/>

<a href="/SwingSet/main?function=DataProtection&solution=">Refresh Page</a>


<%@include file="footer.jsp" %>
