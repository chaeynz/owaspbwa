<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%@ page import="java.util.*" %>

<%
	boolean attempt = (Boolean)request.getAttribute("attempt");
	String profile = (String)request.getAttribute("profile");
	
	String href="?function=ObjectReference&mode=insecure&user=";
%>

<h2>Exercise: Secure Object Reference</h2>

Imagine that these are the user profiles you are aware of:
<table width="30%" border="1">
<tr><th width="50%">User Profiles</th></tr>

<%
HashMap<String,String> userList = (HashMap<String,String>)session.getAttribute("userList");

for (Map.Entry<String, String> item : userList.entrySet()) {
%>
	<tr><td><a href="<%=href + item.getKey()%>"><%=item.getValue() %></a></td></tr>
<%}%>

</table>
<br />  
First, click on the profile links and note the URL. Then try changing the URL to view other users' profiles.<br />
<% if(attempt){ %><br />User's profile:  <br /><p style="color: red"><%=profile %></p><%} %>

<br />

<%@include file="footer.jsp" %>
