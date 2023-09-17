<%@page import="java.util.ArrayList"%>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>
<%@page import="org.owasp.appsensor.demoapp.*"%>
<%@page import="org.owasp.appsensor.intrusiondetection.*"%>

<b>Pending Friend Requests</b>
<br>
<%
	int id = org.owasp.appsensor.demoapp.UserManager.getLoggedInUserID(request);
	org.owasp.appsensor.demoapp.User u = org.owasp.appsensor.demoapp.UserManager.getUserObjectfromID(id);
	ArrayList<Integer> friendRequests = u.getFriendRequestList();
%>

<table>	
	<%
		//Display remainding friend requests if any
		for (int i = 0; i < friendRequests.size(); i++) {
			org.owasp.appsensor.demoapp.User potentialFriend = org.owasp.appsensor.demoapp.UserManager
					.getUserObjectfromID(friendRequests.get(i));
	%>
	<tr>
		<td><b> <a
			href="viewProfile.jsp?profileID=<%=potentialFriend.getID()%>">
		<%=potentialFriend.getUsername()%></a> wants to be your friend! <a
			href="friendRequests.jsp?profileID=<%=potentialFriend.getID()%>&accept=1">[Accept?]</a>
		<a
			href="friendRequests.jsp?profileID=<%=potentialFriend.getID()%>&accept=0">[Reject!]</a>

		<br>
		</b></td>
		<td></td>
		<td><%=Utility.safeOut(potentialFriend.getStatus())%></td>
	</tr>
	<%
		}
	%>
</table>

<br>

<b>Sent Requests awaiting response</b>
<br>
<table>
	<%
		ArrayList<Integer> PendingFriendRequests = u
				.getPendingFriendRequestList();
		for (int i = 0; i < PendingFriendRequests.size(); i++) {
			org.owasp.appsensor.demoapp.User pendingFriend = org.owasp.appsensor.demoapp.UserManager
					.getUserObjectfromID(PendingFriendRequests.get(i));
	%>
	<tr>
		<td>Awaiting response from: <a
			href="viewProfile.jsp?profileID=<%=pendingFriend.getID()%>">
		<%=pendingFriend.getUsername()%></a></td>
	</tr>
	<%
		}
	%>
</table>