<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=SessionManagement">Tutorial</a> | 
<a href="main?function=SessionFixation&lab">Lab : Session Fixation</a>| 
<a href="main?function=SessionFixation&solution">Solution</a> |
<b><a href="main?function=CSRF&lab">Lab : Cross Site Request Forgery</a></b> | 
<a href="main?function=CSRF&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2>Cross Site Request Forgery Lab</h2>

<h4>JSP Location: WebContent\WEB-INF\Jsp\CSRFLab.jsp</h4>

<p>CSRF is an attack which forces an end user to execute unwanted actions on a web application in which he/she is currently authenticated. With a little help of social engineering (like sending a link via email/chat), an attacker may force the users of a web application to execute actions of the attacker's choosing. A successful CSRF exploit can compromise end user data and operation in case of normal user. If the targeted end user is the administrator account, this can compromise the entire web application. </p> 
<p>After logging in, click on the link at the bottom to go to a dummy funds transfer page. <br/>
Your goal is to protect the funds transfer page against a CSRF Attack. 
Add a CSRF Token to the link provided below and Validate the CSRF Token on the Funds Transfer Page. 								
</p>

<%
			
	User user = null;
		
	try {
			user = ESAPI.authenticator().login(request, response);

			String transferFundsHref = "/SwingSet/main?function=TransferFunds&lab";
			
		%>
			
			<!-- TODO : Add a CSRF Token to this URL -->
			<a href='<%=transferFundsHref%>' target="_blank">Transfer Funds</a>

			

<%
					
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
		<H4>Please login</H4>
		<p>If you do not have a user account created, you can do so from <a href="/SwingSet/main?function=Login&solution" target="_blank">Authentication Chapter Solution</a></p>
		<form action="main?function=CSRF&lab" method="POST">
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