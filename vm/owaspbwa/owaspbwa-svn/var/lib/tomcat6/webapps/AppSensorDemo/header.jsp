
<%@page import="org.owasp.appsensor.demoapp.Message"%>
<%@page import="org.owasp.appsensor.AppSensorLogger.*" %>
<%@page import="org.owasp.appsensor.demoapp.Utility.*"%>
<%@page import="org.owasp.appsensor.demoapp.UserManager.*"%>
<%@page import="org.owasp.appsensor.demoapp.User.*"%>
<%@page import="org.owasp.appsensor.demoapp.*"%>
<%@page import="org.owasp.appsensor.intrusiondetection.*"%>



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
	<div align="right" id="appsensorlogo"><img border=1 src="<%=request.getContextPath()%>/appsensor.jpg" width="155" height="138" alt="owasp_logo" title="owasp_logo" style="float:right;"></div>
	<b><%=pageHeader %></b><br>
	<br>
	<div id="holder" style="background-color:#A9BFEA; border-width:thin; border-style:solid;">	
			
	<div id="navigation">
	<% org.owasp.appsensor.AppSensorLogger.testlog();%>
	<% 
	
	if (org.owasp.appsensor.demoapp.UserManager.isSessionValid(request)){
		User user = org.owasp.appsensor.demoapp.UserManager.getLoggedInUserObject(request);
		String username="";
		if (user.getUsername()!=null&&user.getUsername()!=""){
			username=user.getUsername();
		}
		%>User <%=Utility.safeOut(username)%>
		<%	
	}else{
		%><b></b>	<%
	}
	
	
	%>
	<br>
	&nbsp;
	<a href="login.jsp">Login</a> |
	<a href="home.jsp">Home</a> |
	<a href="updateProfile.jsp">UpdateProfile</a> |
	<a href="friends.jsp">Friends</a> |
	<a href="search.jsp">Search</a> |
	<a href="logout.jsp">Logout</a> |
	<a href="admin/observedAttacks.jsp" target="BLANK">ADMIN:Observed Attacks</a>
	
	</div>
	<br>
	</div>
</div>
<p>
