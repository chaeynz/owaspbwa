<%@page import="java.util.ArrayList"%>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>
<%@page import="org.owasp.appsensor.demoapp.*"%>
<%@page import="org.owasp.appsensor.intrusiondetection.*"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.TimeZone"%>
<%@page import="org.owasp.esapi.*"%>

<html>
<body>

<h1>Attacks Observed For Current User:</h1><br>

<table border="1"><tr>
<td>TimeStamp</td>
<td>EventCode</td>
<td>Location</td>
<td>LogMessage</td>
<td>User</td>

</tr>
<%

try {
	org.owasp.appsensor.demoapp.User u = org.owasp.appsensor.demoapp.UserManager.getLoggedInUserObject(request);
	AppSensorIntrusionRecord air=u.getAir();
	ArrayList<AppSensorIntrusionException> aieCopy=air.getCopyIntrusionsObserved();
	Iterator<AppSensorIntrusionException> iterator=aieCopy.iterator();
	while (iterator.hasNext()){
		AppSensorIntrusionException aie=iterator.next();				
		long timestamp = aie.getTimeStamp() * 1000;  // msec  
		java.util.Date d = new java.util.Date(timestamp);  
		
		
		out.println("<tr>");
		out.println("<td>"+d.toString()+"</td>");
		out.println("<td>"+ESAPI.encoder().encodeForHTML(aie.getEventCode())+"</td>");
		out.println("<td>"+ESAPI.encoder().encodeForHTML(aie.getLocation())+"</td>");
		out.println("<td>"+ESAPI.encoder().encodeForHTML(aie.getLogMessage())+"</td>");		
		out.println("<td>"+ESAPI.encoder().encodeForHTML(aie.getUser().getUsername())+"</td>");
		out.println("</tr>");		
		
	}	
}catch(NullPointerException e){
	out.print("<b>No Attacks Recorded</b><br><br>");
}
out.print("</table>");
%>




<%@include file="../footer.jsp" %>
