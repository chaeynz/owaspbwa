<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<div id="navigation">
	<a href="main">Home</a> | <a href="main?function=SessionManagement">Tutorial</a>
	| <a href="main?function=SessionFixation&lab">Lab : Session
		Fixation</a>| <a href="main?function=SessionFixation&solution">Solution</a>
	| <b><a href="main?function=CSRF&lab">Lab : Cross Site Request
			Forgery</a> </b> | <a href="main?function=CSRF&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h1>Transfer Funds</h1>
<h4>JSP Location: WebContent/WEB-INF/jsp/TransferFundsLab.jsp</h4>

<p>Your goal here is to get the logged in users CSRF Token and
	validate it against the CSRF Token in the request.</p>

<%
	User user = ESAPI.authenticator().getCurrentUser();

	if (user == null) {
%>
Not Logged IN
<%
	// TODO: Verify that the User's CSRF Token matches the CSRF Token in the Request

	}

	else {
		
	%>
		
		Funds Transferred
		
	<%
	
	}
%>

<%@include file="footer.jsp"%>
