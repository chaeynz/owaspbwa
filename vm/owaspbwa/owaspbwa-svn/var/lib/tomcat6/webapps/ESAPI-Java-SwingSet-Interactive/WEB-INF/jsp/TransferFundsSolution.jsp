<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<div id="navigation">
	<a href="main">Home</a> | <a href="main?function=SessionManagement">Tutorial</a>
	| <a href="main?function=SessionFixation&lab">Lab : Session
		Fixation</a>| <a href="main?function=SessionFixation&solution">Solution</a>
	| <a href="main?function=CSRF&lab">Lab : Cross Site Request Forgery</a>
	| <b><a href="main?function=CSRF&solution">Solution</a>
	</b>
</div>
<div id="header"></div>
<p>
<hr>

<h1>Transfer Funds</h1>

<p>
	We check the CSRF Token by comparing the user CSRF Token -
	user.getCSRFToken();<br /> against the CSRF Token in the request -
	request.getParameter("ctoken")
</p>


<%	User user = ESAPI.authenticator().login();
	
	  if (user == null){
	%>
		Not Logged IN
	<%
	  }
	  // CD 2011-04-12
	  else{
		  try {
			ESAPI.httpUtilities().verifyCSRFToken();
			out.print("CSRF Token Validated - Funds transferred");
		} catch (IntrusionException e) {
			e.printStackTrace();
			out.println("CSRF Token Not Validated - Funds can not be transferred<br/>");
			out.println(e.getUserMessage());
		}
	  }
	  %>
<!-- else if (user.getCSRFToken().equals(request.getParameter("ctoken"))){ -->
<!-- %> -->
<!-- 		CSRF Token Validated - Funds Transferred -->

<%-- 	<%} else{  --%>
<!-- 		ESAPI.log().error(Logger.SECURITY_FAILURE, "CSRF token not detected"); -->
<!-- 	%> -->
<!-- 		CSRF Token Not Validated - Funds can not be transferred -->
<!-- } -->

<%@include file="footer.jsp"%>
