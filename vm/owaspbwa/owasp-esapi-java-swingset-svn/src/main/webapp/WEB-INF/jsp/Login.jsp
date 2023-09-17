<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2>Background</h2>

<p></p>
<p></p>

<hr><br><h2>Exercise: Login to View Account Information</h2>

<p>Current Cookie: <script>document.write(document.cookie)</script></p>

<%
	ESAPI.httpUtilities().setNoCacheHeaders(response);
	User user = null;
	
	try {
		user = ESAPI.authenticator().login(request, response);
		if ( request.getParameter( "logout" ) != null ) {
			user.logout();
		} else {
%>
			Current User: <%=user.getAccountName() %><br>
			Last Successful Login: <%=user.getLastLoginTime() %><br>
			Last Failed Login: <%=user.getLastFailedLoginTime() %><br>
			Failed Login Count: <%=user.getFailedLoginCount() %><br>
			Current Roles: <%=user.getRoles() %><br>
			Last Host Name: <%=user.getLastHostAddress() %><br>
			<a href="main?function=Login&logout">logout</a><br><br>
<%
		}
	} catch( AuthenticationException e ) {
		request.setAttribute("message", e.getUserMessage() );
	}
	if ( user==null || !user.isLoggedIn() ) {
%>
		<form action="main?function=Login" method="POST">
		<table><tr><td>Username:</td><td><input name="username"></td></tr>
		<tr><td>Password:</td><td><input type="password" name="password" autocomplete="off"></td></tr>
		<tr><td>Remember:</td><td><input type="checkbox" name="remember"></td></tr></table>
		<input type="submit" value="login"><br>
		</form>
<%
	}
%>

<%@include file="footer.jsp" %>
