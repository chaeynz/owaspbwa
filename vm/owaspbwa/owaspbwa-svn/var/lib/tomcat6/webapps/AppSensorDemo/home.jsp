<%@include file="header.jsp" %>



<b>Welcome Home</b><br>
<b>Your Current Status</b><br>

<%=Utility.safeOut(org.owasp.appsensor.demoapp.UserManager.getStatus(request)) %><br>

<!-- Vulnerable version below -->
<!-- <%=org.owasp.appsensor.demoapp.UserManager.getStatus(request) %><br> -->

<br>
<%@ include file="pendingFriends.jsp" %>
 
<!-- include file="friendRequests.jsp" -->
<%@include file="footer.jsp" %>
