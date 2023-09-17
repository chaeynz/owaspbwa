<%@include file="header.jsp" %>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.TimeZone"%>




<h2>Disabled by AppSensor</h2>
<%
long reActivateTime=0;
try{
 reActivateTime=Long.parseLong(request.getParameter("time"));
}catch(Exception e){}

Long current_time = Long.parseLong((new SimpleDateFormat ("yyyyMMddHHmmss").format (Calendar.getInstance (TimeZone.getDefault ()).getTime ())));
Long waitTime=reActivateTime-current_time;

/*
String targetPage=request.getParameter("target"); 
String duration=request.getParameter("duration");
String timeScale=request.getParameter("timeScale");
*/
%>

The page you've requested has been temporarily disabled by AppSensor.<br>
<br>
Service will return in <b><%=waitTime%></b> seconds<br>
Current Time: <%=current_time %><br>
ReActivate Time: <%=reActivateTime %>
	

 
<%@include file="footer.jsp" %>
