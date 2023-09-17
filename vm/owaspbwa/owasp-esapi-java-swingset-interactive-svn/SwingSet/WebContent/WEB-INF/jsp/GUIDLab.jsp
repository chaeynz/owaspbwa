<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<form action="main?function=GUID&lab" method="POST">
<h2 align="center">Excercise</h2>
<p>Generate GUID using <b>java.util.UUID randomUUID()</b></p>
<table align="center" border="1" width="80%">
<tr><th>GUID</th><tr>
<tr><td align="center">
<%=request.getAttribute("GUID1") %>-<%=request.getAttribute("GUID2") %>-<%=request.getAttribute("GUID3") %>-<%=request.getAttribute("GUID4") %>-<br />
<%=request.getAttribute("GUID5") %>
</td></tr>
<tr><td align="center"><input type="submit" value="New GUID"></td></tr>
</table>
</form>


<%@include file="footer.jsp" %>
