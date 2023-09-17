<%@include file="header.jsp" %>
<%@page import="org.owasp.appsensor.demoapp.Message"%>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>


<%=Utility.safeOut(Message.getMessage(request))%>
 
<%@include file="footer.jsp" %>
