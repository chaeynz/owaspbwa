<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=HttpSecurity">Tutorial</a> | 
<b><a href="main?function=HttpSecurity&lab">Lab : Unvalidated Redirect/Forward</a></b>| 
<a href="main?function=HttpSecurity&solution">Solution</a> 
</div>
<div id="header"></div>
<p>
<hr>

<%
	// TODO: Validate this redirect
	String redirect = request.getParameter("redirect");
	if (redirect != null){
 		response.sendRedirect(redirect); 
	}
%>

<h2>Unvalidated Redirect/Forward Lab</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\HttpSecurityLab.jsp</h4>


<p>The form below redirects a user to the Http Security Solution or Tutorial.<br/>
	The jsp checks if a request parameter 'redirect' exists. If not it displays this lab. 
	If it is present it redirects it to the parameter. There is no validation on this 
	parameter. So if you hit the following url, you will be redirected to www.google.com.<br/><br/>
	<a href="/SwingSet/main?function=HttpSecurity&lab&redirect=http://www.google.com">/SwingSet/main?function=HttpSecurity&lab&redirect=http://www.google.com</a> 
</p>

<p>Your goal is to use ESAPI.httpUtilities().sendRedirect to validate the redirect and protect this page against this vulnerability.</p>


<form action="main?function=HttpSecurity&lab" method="POST">
		<select name="redirect">
			<option value="main?function=HttpSecurity&solution">Redirect to Solution</option>
			<option value="main?function=HttpSecurity">Redirect to Tutorial</option>
		</select>
		
		<input type="submit" value="redirect"><br>
</form>


<%@include file="footer.jsp" %>