<%@page import="org.owasp.esapi.reference.FileBasedAuthenticator2"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Login">Tutorial</a> | 
<a href="main?function=Login&lab">Lab : Authenticator Functions</a>| 
<b><a href="main?function=Login&solution">Solution</a></b>
</div>
<div id="header"></div>
<p>
<hr>


<h2>Authenticator Methods Solution</h2>

<h4>JSP Location: \WebContent\WEB-INF\jsp\LoginSolution.jsp</h4>

<p>The following JSP creates a user account with - ESAPI.authenticator().createUser()</p>
<p>And logs the user in with ESAPI.authenticator().login()</p>
<p>After creating a user you should see a users.txt file in your .esapi folder.</p>
<p>Please see the source of the JSP for full details.</p>
<%
			
	User user = null;
		
	try {
		if (request.getParameter("create_username")!=null){			
			ESAPI.authenticator().createUser(request.getParameter("create_username"), request.getParameter("create_password1"), request.getParameter("create_password2"));
			ESAPI.authenticator().getUser(request.getParameter("create_username")).enable();
			ESAPI.authenticator().getUser(request.getParameter("create_username")).unlock();
			request.setAttribute("userMessage", "User " + request.getParameter("create_username") + " Created");
			request.setAttribute("logMessage", "User Created");
		%>
		
		User Created : <%=request.getParameter("create_username") %>	
		
<% 		
		}else{
			user = ESAPI.authenticator().login(request, response);
%>		
			Current User: <%=user.getAccountName() %><br>
			Last Successful Login: <%=user.getLastLoginTime() %><br>
			Last Failed Login: <%=user.getLastFailedLoginTime() %><br>
			Failed Login Count: <%=user.getFailedLoginCount() %><br>
			Current Roles: <%=user.getRoles() %><br>
			Last Host Name: <%=user.getLastHostAddress() %><br>
			Current Cookies: <br /><% Cookie[] cookies=ESAPI.httpUtilities().getCurrentRequest().getCookies(); for (int i=0; i<cookies.length; i++) out.print( "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp " + cookies[i].getName() + "=" + cookies[i].getValue() + "; <br />" ); %>
			Browser Cookies: <script>document.write(document.cookie)</script><br><br>
			<a href="main?function=Login&logout&solution">logout</a>
<%
		}
				
	} catch( AuthenticationException e ) {
		request.setAttribute("userMessage", e.getUserMessage() );
		request.setAttribute("logMessage", e.getLogMessage() );		
		e.printStackTrace();
	} catch( Exception e){
		request.setAttribute("userMessage", e.getMessage());
		e.printStackTrace();
	}
	
	if ( user == null || user.isAnonymous() ) {
%>
		
		<H2>Create User</H2>
		<form action="main?function=Login&solution" method="POST">
			<table>
				<tr>
					<td>Username:</td><td><input name="create_username"></td>
				</tr>
				<tr>
					<td>Password:</td><td><input type="password" name="create_password1" autocomplete="off"></td>
				</tr>
				<tr>
					<td>Confirm Password:</td><td><input type="password" name="create_password2" autocomplete="off"></td>
				</tr>
			</table>			
			<input type="submit" value="Create User"><br>
		</form>
		
		<H2>Please login</H2>
		<form action="main?function=Login&solution" method="POST">
			<table>
				<tr>
					<td>Username:</td><td><input name="username"></td>
				</tr>
				<tr>
					<td>Password:</td><td><input type="password" name="password" autocomplete="off"></td>
				</tr>
				<tr>
					<td>Remember me on this computer:</td>
					<td><input type="checkbox" name="remember"></td>
				</tr>
			</table>
			<input type="submit" value="login"><br>
		</form>
<%
	} 
%>


<%@include file="footer.jsp" %>
