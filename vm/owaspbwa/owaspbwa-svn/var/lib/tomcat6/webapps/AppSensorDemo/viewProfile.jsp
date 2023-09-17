<%@include file="header.jsp" %>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>
<%@page import="org.owasp.appsensor.demoapp.UserManager.*"%>
<%@page import="org.owasp.appsensor.demoapp.*"%>
<%@page import="org.owasp.appsensor.intrusiondetection.*"%>

<b>View Friend</b><br><br>


<table border="1" width="60%">

<%
int profileID=0;
String temp=Utility.safeGetParam("profileID", request);
if (!temp.equals("")){
	try{
		profileID=Integer.parseInt(temp);
	}catch(NumberFormatException nfe){
		//TODO: Add direct object tamper attack detection here
		//@AppSensor Attack Detection - ACE1
		//Attacker is tampering with userID within the URL		
		User user=org.owasp.appsensor.demoapp.UserManager.getLoggedInUserObject(request);
		new AppSensorIntrusionException(request.getContextPath(),"ACE1",user,"User Message",
					"Direct object tampering with userID within URL to submit nonInteger values");
		profileID=0;
	}
}
if (profileID>0){
	org.owasp.appsensor.demoapp.User friend = org.owasp.appsensor.demoapp.UserManager.getUserObjectfromID(profileID);
	if (friend!=null){
%>
	<tr>
		<td rowspan=4><img src="/AppSensorDemo/face1.jpg" alt="friend's picture" title="your friend"></td>
	</tr>	
	<tr>
		
		<td><b>UserName</b><br> <%=Utility.safeOut(friend.getUsername())  %></td>
	</tr>
	<tr>
		<td><b>Status</b><br><%=Utility.safeOut(friend.getStatus()) %></td>
	</tr>
	<tr>
		<td><b>Profile</b><br>	<%=Utility.safeOut(friend.getProfile()) %>	</td>
	</tr>
<%
	}
}
%>
</table>

<%@include file="footer.jsp" %>
