<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<%@ page import="org.owasp.esapi.reference.RandomAccessReferenceMap"%>

<div id="navigation">
	<a href="main">Home</a> | <a href="main?function=AccessControl">Tutorial</a>
	| <a href="main?function=AccessControl&lab">Lab : Forced Browsing</a>|
	<a href="main?function=AccessControl&solution">Solution</a> | <b><a
		href="main?function=ObjectReference&lab">Lab : Direct Object
			Reference</a>
	</b> | <a href="main?function=ObjectReference&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<%
	boolean found = false;
	String quote = "Change the URL to access other files...";
	if (request.getAttribute("user") != null) {
		found = true;
		quote = request.getAttribute("user").toString();
	}
	String href = "?function=ObjectReference&lab&user=";
%>

<h2>Lab: Insecure Object Reference</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\ObjectReferenceLab.jsp</h4>
<p>
	Below is a list of users which have been put in session attributes in
	the following Action Class:<br /> <b><i>Java
			Resources:src/org.owasp.esapi.swingset.actions.ObjectReferenceLab.java</i>
	</b>
</p>

<p>
	Changing the user parameter in the url to any of the users name will
	retrieve the users message. <br /> E.g. in the url <i>https://localhost:8443/SwingSet/main?function=ObjectReference&lab&user=admin</i><br />
	Change 'admin' to 'matrix'.
</p>

<p>Your goal is to use
	org.owasp.esapi.reference.RandomAccessReferenceMap to change the
	references from Direct to Indirect references.</p>

<table width="30%" border="1">
	<tr>
		<th width="50%">List of Users</th>
	</tr>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do0") %>"><%=session.getAttribute("do0") %></a></td></tr> --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do1") %>"><%=session.getAttribute("do1") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do2") %>"><%=session.getAttribute("do2") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do3") %>"><%=session.getAttribute("do3") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do4") %>"><%=session.getAttribute("do4") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do5") %>"><%=session.getAttribute("do5") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do6") %>"><%=session.getAttribute("do6") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do7") %>"><%=session.getAttribute("do7") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do8") %>"><%=session.getAttribute("do8") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do9") %>"><%=session.getAttribute("do9") %></a></td></tr>  --%>
	<%-- <tr><td><a href="<%=href + session.getAttribute("do10") %>"><%=session.getAttribute("do10") %></a></td></tr> --%>

	<tr><td><a href="<%=href + session.getAttribute("do0") %>"><%=session.getAttribute("do0") %></a></td></tr>
	<tr><td><a href="<%=href + session.getAttribute("do1") %>"><%=session.getAttribute("do1") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do2") %>"><%=session.getAttribute("do2") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do3") %>"><%=session.getAttribute("do3") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do4") %>"><%=session.getAttribute("do4") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do5") %>"><%=session.getAttribute("do5") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do6") %>"><%=session.getAttribute("do6") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do7") %>"><%=session.getAttribute("do7") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do8") %>"><%=session.getAttribute("do8") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do9") %>"><%=session.getAttribute("do9") %></a></td></tr> 
	<tr><td><a href="<%=href + session.getAttribute("do10") %>"><%=session.getAttribute("do10") %></a></td></tr>
</table>
<br />
<%
	if (found) {
%>User's message:
<br />
<p style="color: red">
	<%
		}
	%><%=quote%></p>
<br />

<%@include file="footer.jsp"%>
