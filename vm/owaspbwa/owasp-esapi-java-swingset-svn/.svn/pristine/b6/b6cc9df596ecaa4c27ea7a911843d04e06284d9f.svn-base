<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@include file="header.jsp" %>

<h2>Exercise: Enter Malicious Input</h2>

<%
	String name = request.getParameter("name");
	if ( name == null || name.equals("") ) name = "anonymous";
%>

<ul>
<% 
ArrayList<String> examples = (ArrayList<String>)request.getAttribute("OutputUserExamples");
for (String example: examples) {
%>
<li><a href="" onclick="document.getElementById('OutputUserForm').value='<%=ESAPI.encoder().encodeForJavaScript(example)%>'; return false;"><%=ESAPI.encoder().encodeForHTML(example) %></a></li>
<% } %>
</ul>

<form action="main?function=OutputUserInput&mode=insecure" method="POST">
    <p>Enter your name:</p>
    <input name='name' id="OutputUserForm" value='<%=name %>'>
    <input type='submit' value='submit'>
</form>

<p>Hello, <%=name %></p>

<%@include file="footer.jsp" %>
