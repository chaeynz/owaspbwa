<%@page import="org.owasp.esapi.*"%>
<html>
<head><title>Hello ESAPI!</title></head>

<body>
<%
	ESAPI.httpUtilities().sendRedirect( "main");
%>
</body>
</html>
