<%@include file="header.jsp" %>
<%@page import="org.owasp.appsensor.demoapp.UserManager.*"%>
<%@page import="org.owasp.appsensor.demoapp.User.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>

<b>Add a Friend</b><br>
 
<%
int profileID=0;
String tempProfileID=Utility.safeGetParam("profileID", request);
if (tempProfileID!=null){
	try{
	profileID=Integer.parseInt(tempProfileID);
	}catch(NumberFormatException nfe){
		//TODO: Add direct object tamper attack detection here
		profileID=0;
	}
}
if (profileID>0){
	org.owasp.appsensor.demoapp.User friend = org.owasp.appsensor.demoapp.UserManager.getUserObjectfromID(profileID);
	if (friend!=null){
		int currentUserID=org.owasp.appsensor.demoapp.UserManager.getLoggedInUserID(request);
		org.owasp.appsensor.demoapp.UserManager.addFriendRequest(currentUserID,friend.getID());
		%>
		A friend request has been sent to: <%=Utility.safeOut(friend.getUsername())%><br><br>
		<%
	}
}
%>


 
<%
ArrayList<Integer> users=org.owasp.appsensor.demoapp.UserManager.getAllUserIDs();
%>

<table border="0">
<tr>
<td align="center">Potential Friend</td>
<td width="100"></td>
<td align="center">Status</td>
</tr>

<%
int id=org.owasp.appsensor.demoapp.UserManager.getLoggedInUserID(request);
org.owasp.appsensor.demoapp.User u=org.owasp.appsensor.demoapp.UserManager.getUserObjectfromID(id);

for (int i=0;i<users.size();i++){
	org.owasp.appsensor.demoapp.User potentialFriend = org.owasp.appsensor.demoapp.UserManager.getUserObjectfromID(users.get(i));
	if (!(u.containsFriend(potentialFriend.getID()))){
		%>
		<tr>
		<td>User: <b> <a href="addFriend.jsp?profileID=<%=Utility.safeOut(String.valueOf(potentialFriend.getID()))%>"> <%=Utility.safeOut(potentialFriend.getUsername())%></a></b></td>  
		<td></td>
		<td> <%=Utility.safeOut(potentialFriend.getStatus())%></td>
		</tr>
		<%
	}
}
%>
</table>

 
<%@include file="footer.jsp" %>
