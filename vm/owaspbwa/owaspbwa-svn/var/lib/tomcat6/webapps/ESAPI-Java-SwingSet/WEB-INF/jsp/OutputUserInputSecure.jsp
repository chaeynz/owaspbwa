<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@include file="header.jsp" %>

<%
	String name = request.getParameter("name");
	if ( name == null || name.equals("") ) name = "anonymous";
%>

<h2>Exercise: Enter Malicious Input</h2>

<ul>
<% 
ArrayList<String> examples = (ArrayList<String>)request.getAttribute("OutputUserExamples");
for (String example: examples) {
%>
<li><a href="" onclick="document.getElementById('OutputUserForm').value='<%=ESAPI.encoder().encodeForJavaScript(example)%>'; return false;"><%=ESAPI.encoder().encodeForHTML(example) %></a></li>
<% } %>
</ul>

<form action="main?function=OutputUserInput&mode=secure" method="POST">
    <p>Enter your name:</p>
    <input name='name' id='OutputUserForm' value='<%=ESAPI.encoder().encodeForHTMLAttribute(name) %>'>
    <input type='submit' value='submit'>
</form>

<p>Hello, <%=ESAPI.encoder().encodeForHTML(name)%></p>

<code>


<%
	String encodedName = ESAPI.encoder().encodeForHTML(name);
	encodedName = encodedName.replaceAll("&", "&amp;");
%>
Source:
<%=encodedName %>

</code>
<%@include file="footer.jsp" %>
