<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=SessionManagement">Tutorial</a> | 
<a href="main?function=SessionFixation&lab">Lab : Session Fixation</a>| 
<a href="main?function=SessionFixation&solution">Solution</a> |
<a href="main?function=CSRF&lab">Lab : Cross Site Request Forgery</a> | 
<b><a href="main?function=CSRF&solution">Solution</a></b>
</div>
<div id="header"></div>
<p>
<hr>

<h2>Cross Site Request Forgery Solution</h2>

<h4>JSP Location: WebContent\WEB-INF\Jsp\CSRFSolution.jsp</h4>

<p>The CSRF Token is added to the link using ESAPI.httpUtilities().addCSRFToken()<br/><br/>
A CSRF token can be re-generated once per session or once per request using ESAPI.authenticator().getCurrentUser().resetCSRFToken().<br/> <br/>								
Note: A user has to be logged in to use this utility.</p> 								

<%
			
	User user = null;
		
	try {
			user = ESAPI.authenticator().login(request, response);		
		

			String transferFundsHref = "/SwingSet/main?function=TransferFunds&solution";
		%>
			
			
			<a href='<%=ESAPI.httpUtilities().addCSRFToken(transferFundsHref)%>' target="_blank">Transfer Funds</a>
			
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
		<H2>Please login</H2>
		<form action="main?function=CSRF&solution" method="POST">
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