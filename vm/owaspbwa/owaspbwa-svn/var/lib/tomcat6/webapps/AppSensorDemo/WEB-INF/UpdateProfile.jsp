<%@include file="header.jsp" %>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>



<%=Utility.safeOut(Message.getMessage())%>
<br>
<table align="center" border="1" width="60%">
<tr>
	<td><b>UserName</b><br> <%=Utility.safeOut(Message.getUserNameFromSessionObject(request)) %></td>
</tr>
<tr>
	<td><b>Profile</b><br>	<%=Utility.safeOut(Message.getUserProfile(request)) %>	</td>
</tr>
</table>



 
<%@include file="footer.jsp" %>
