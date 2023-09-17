<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	String name = request.getParameter("name");
	if ( name == null || name.equals("") ) name = "anonymous";
%>

<h2>Exercise: Enter Malicious Input</h2>

<form action="main?function=OutputUserInput&solution" method="POST">
    <p>Enter your name:</p>
    <input name='name' value='<%=ESAPI.encoder().encodeForHTMLAttribute(name) %>'>
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
