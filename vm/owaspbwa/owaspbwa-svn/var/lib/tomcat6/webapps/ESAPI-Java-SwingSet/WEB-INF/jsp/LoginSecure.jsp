<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2>Exercise: Login</h2>

<%
	//if(request.getRequestURL().charAt(4) != 's')
			//response.sendRedirect("https://localhost:8443/ESAPI_Swingset/main?function=Login&mode=secure"); 
			//ESAPI.httpUtilities().safeSendForward(ESAPI.httpUtilities().getCurrentRequest(), ESAPI.httpUtilities().getCurrentResponse(), "Forward to ssl page", "https://localhost:8043/ESAPI_Swingset/main?function=Login&mode=secure");
			
	User user = null;
	//if(request.getAttribute("user") != null)
		//user = (User)request.getAttribute("user");		
		//user = ESAPI.authenticator().getCurrentUser();
		
	try {
		user = ESAPI.authenticator().login(request, response);
		//if(request.getAttribute("user") != null){
%>		
			Current User: <%=user.getAccountName() %><br>
			Last Successful Login: <%=user.getLastLoginTime() %><br>
			Last Failed Login: <%=user.getLastFailedLoginTime() %><br>
			Failed Login Count: <%=user.getFailedLoginCount() %><br>
			Current Roles: <%=user.getRoles() %><br>
			Last Host Name: <%=user.getLastHostAddress() %><br>
			Current Cookies: <br /><% Cookie[] cookies=ESAPI.httpUtilities().getCurrentRequest().getCookies(); for (int i=0; i<cookies.length; i++) out.print( "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp " + cookies[i].getName() + "=" + cookies[i].getValue() + "; <br />" ); %>
			Browser Cookies: <script>document.write(document.cookie)</script><br><br>
			<a href="main?function=Login&mode=secure&logout=true">logout</a>
<%
		//}		
	} catch( AuthenticationException e ) {
		request.setAttribute("userMessage", e.getUserMessage() );
		request.setAttribute("logMessage", e.getLogMessage() );
			//request.setAttribute("message", e.getUserMessage() );
		//e.printStackTrace();
	}
	
	if ( user == null || user.isAnonymous() ) {
%>
		<p>Testing username: swingset<br />Testing password: lookatme01@<br />Password can be changed from 'Change Password Secure' example.</p>	
		<H2>Please login</H2>
		<form action="main?function=Login&mode=secure" method="POST">
		<table><tr><td>Username:</td><td><input name="username"></td></tr>
		<tr><td>Password:</td><td><input type="password" name="password" autocomplete="off"></td></tr>
		<tr><td>Remember me on this computer:</td><td><input type="checkbox" name="remember"></td></tr></table>
		<input type="submit" value="login"><br>
		</form>
<%
	} 
%>


<%@include file="footer.jsp" %>
