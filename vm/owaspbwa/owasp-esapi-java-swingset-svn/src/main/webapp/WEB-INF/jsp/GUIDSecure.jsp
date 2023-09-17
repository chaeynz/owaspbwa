<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<form action="main?function=GUID&mode=secure" method="POST">
<h2 align="center">Exercise</h2>	
<p>Generate GUID using <b>ESAPI - String getRandomGUID()</b></p>
<table align="center" border="1" width="80%">
	<tr><th>GUID</th></tr>
	<tr><td align="center"><%=request.getAttribute("GUID") %></td></tr>
	<tr><td align="center"><input type="submit" value="New GUID"></td></tr>
</table>
</form>

<%@include file="footer.jsp" %>
