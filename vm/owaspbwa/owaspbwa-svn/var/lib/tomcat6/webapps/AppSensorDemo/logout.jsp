<%@include file="header_unauthenticated.jsp" %>



<b>Logged Out</b>
<% 
	org.owasp.appsensor.demoapp.UserManager.logout(request,response);
%>
 
 
 

<%@include file="footer.jsp" %>
