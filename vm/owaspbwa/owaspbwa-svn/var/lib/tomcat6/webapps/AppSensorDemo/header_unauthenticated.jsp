
<%@page import="org.owasp.appsensor.demoapp.Message"%>
<%@page import="org.owasp.appsensor.AppSensorLogger.*" %>


<%
String title = "AppSensor - Demo Application";
String pageHeader = "AppSensor Demo Application";




%>

<html>
<head>
<title><%=title%></title>
<!-- <link rel="stylesheet" type="text/css" href="style/style.css" />-->

</head>


<div id="container">
	<div align="right" id="appsensorlogo"><img border=1 src="<%= request.getContextPath() %>/appsensor.jpg" width="155" height="138" alt="owasp_logo" title="owasp_logo" style="float:right;"></div>
	<b><%=pageHeader %></b><br>
	<br>
	<div id="holder" style="background-color:#A9BFEA; border-width:thin; border-style:solid;">	
			
	<div id="navigation">
	<b><% org.owasp.appsensor.AppSensorLogger.testlog();%></b>
	<br>
	&nbsp;
	<a href="login.jsp">Login</a> |
	<a href="home.jsp">Home</a> |
	<a href="updateProfile.jsp">UpdateProfile</a> |
	<a href="friends.jsp">Friends</a> |
	<a href="search.jsp">Search</a> 	
	</div>
	<br>
	</div>
</div>
<p>
