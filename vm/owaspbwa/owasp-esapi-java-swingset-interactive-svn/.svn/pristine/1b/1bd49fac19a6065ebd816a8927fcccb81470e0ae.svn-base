<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256" import="org.owasp.esapi.swingset.login.UserBean" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
	<head>  
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">  
		<title> User Logged In </title>  
	</head>  
	<body>  
		<center>  
			<% UserBean currentUser = (UserBean) session.getAttribute("currentSessionUser");%>  
			Welcome <%= currentUser.getUsername()  %>  <br/>
			<!-- CD 2011-04-01 -->
			<br/>
			This is your session id : <%=session.getId() %> 
		</center>  
	</body>  
</html> 