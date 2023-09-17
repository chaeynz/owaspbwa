<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2>Exercise: Enter Malicious Input</h2>

<%
	String name = request.getParameter("name");
	if ( name == null || name.equals("") ) name = "anonymous";
%>

<form action="main?function=OutputUserInput&lab" method="POST">
    <p>Enter your name:</p>
    <input name='name' value='<%=name %>'>
    <input type='submit' value='submit'>
</form>

<p>Hello, <%=name%></p>

<%@include file="footer.jsp" %>
