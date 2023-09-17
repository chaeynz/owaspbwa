<%@include file="header.jsp" %>





<br>
<table align="center" border="1" width="60%">
<tr>
	<td><b>UserName</b><br> <%=Utility.safeOut(Message.getUserNameFromSessionObject(request)) %></td>
</tr>
<tr>
	<td><b>Status</b><br>
	<%=Utility.safeOut(org.owasp.appsensor.demoapp.UserManager.getStatus(request)) %>
	</td>
</tr>
<tr>
	<td><b>Profile</b><br>	
	<%=Utility.safeOut(org.owasp.appsensor.demoapp.UserManager.getProfile(request)) %>	
	</td>
</tr>
</table>

<br><hr><br>
<b>Update Your Info</b>
<form name="input" action="UpdateProfile" method="POST">
<table align="center" border="0" width="60%">
<tr>
<td>Status:</td>
<td><input type="text" name="status"></td>
</tr>
<tr>
<td>Profile:</td>
<td><textarea name="profile" cols="40" rows="5">
</textarea></td>
</tr>
<tr>
<td><input type="submit" value="Submit"></td>
</tr>
</table>
</form>



 
<%@include file="footer.jsp" %>
