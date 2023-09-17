<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=HttpSecurity">Tutorial</a> | 
<a href="main?function=HttpSecurity&lab">Lab : Unvalidated Redirect/Forward</a>| 
<b><a href="main?function=HttpSecurity&solution">Solution</a></b> 
</div>
<div id="header"></div>
<p>
<hr>
<%
	String redirect = request.getParameter("redirect");
	if (redirect != null)
		ESAPI.httpUtilities().sendRedirect(redirect); 
%>


<h2>Unvalidated Redirect/Forward Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\HttpSecuritySolution.jsp</h4>

<p>The form below redirects users to the Http Security Lab or Tutorial. 
	The jsp checks if a request parameter 'redirect' is present. 
	If it is present it performs the redirect. 
</p>

<p>The redirect is performed using ESAPI.httpUtilities().sendRedirect 
which only allows redirects to resources in the WEB-INF directory of 
the application. 
</p>
<p>The validation is controlled by setting <i>'Validator.Redirect'</i> in ESAPI.properties. An example of a regular expression that could be used in this case is <br/><br/>

^/SwingSet/.+
</p>
<p>Try redirecting the user to a random site <br/><br/>
<a href="/SwingSet/main?function=HttpSecurity&solution&redirect=http://www.google.com" target="_blank">/SwingSet/main?function=HttpSecurity&solution&redirect=http://www.google.com</a>
</p>

<form action="main?function=HttpSecurity&solution" method="POST">
		<select name="redirect">
			<option value="/SwingSet/main?function=HttpSecurity">Redirect to Tutorial</option>
			<option value="/SwingSet/main?function=HttpSecurity&lab">Redirect to Lab</option>
		</select>
		
		<input type="submit" value="redirect"><br>
</form>
<%@include file="footer.jsp" %>