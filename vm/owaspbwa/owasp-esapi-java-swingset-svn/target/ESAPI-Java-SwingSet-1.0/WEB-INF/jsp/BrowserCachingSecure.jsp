<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	ESAPI.httpUtilities().setNoCacheHeaders(ESAPI.httpUtilities().getCurrentResponse());
%>

<h2>Exercise: Experiment with Browser Caching</h2>

<p>Page is not set to cache, so pressing refresh button should 
show a new set of account balances each time</p>

<p>Social Security Number: 123-12-1234</p>
<p>Credit Card Number: 1234-1234-1234-1234</p>
<table border="1">
<tr><th>Account Number</th><th>Balance</th></tr>
<tr><td>95812344</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
<tr><td>21231235</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
<tr><td>10823580</td><td>$<%=ESAPI.randomizer().getRandomInteger(100000,900000) %>.<%=ESAPI.randomizer().getRandomInteger(10,100) %></td></tr>
</table>

<form action="main" method="GET">
<input type="hidden" name="function" value="BrowserCaching">
<input type="hidden" name="mode" value="secure"> 
<input type="submit" value="refresh">
</form>

<%@include file="footer.jsp" %>
