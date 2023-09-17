
<%@page import="org.owasp.appsensor.demoapp.Message"%>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>


<%
String title = "AppSensor - Demo Application";
String pageHeader = "AppSensor Demo Application";




%>

<html>
<head>
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="style/style.css" />

</head>
<hr>

<div id="container">
	<div id="holder">
		<div id="logo"><img src="style/images/owasp-logo_130x55.png" width="130" height="55" alt="owasp_logo" title="owasp_logo"></div>
<h2><%=pageHeader %></h2>
	<div id="navigation">
	<a href="Login.jsp">Login</a> |
	<a href="index.jsp">Home</a> |
	<a href="UpdateProfile.jsp">UpdateProfile</a> |
	<a href="Search.jsp">Search</a> |
	<a href="Logout.jsp">Logout</a> |
	</div>
	</div>
</div>
<p>
<hr>