<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=SessionManagement">Tutorial</a> | 
<b><a href="main?function=SessionFixation&lab">Lab : Session Fixation</a></b>| 
<a href="main?function=SessionFixation&solution">Solution</a> |
<a href="main?function=CSRF&lab">Lab : Cross Site Request Forgery</a> | 
<a href="main?function=CSRF&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2>Exercise: Session Fixation</h2>

<h4>JSP Location: WebContent\WEB-INF\JSP\SessionFixationLab.jsp</h4>

<h3>Step 1</h3>
<p>Open this <a href="/SwingSet/main?function=InsecureLogin&lab" target="_blank">link</a> in a new browser window and login using the following user details :</p>
<p>username : test <br/>password : test<br/><br/>Make a note of the session id displayed.</p>

<h3>Step 2</h3>
<p>Open a new browser window and browse to the following URL: https://localhost:8443/SwingSet/LoginServletLab. <br> You should be told that you are not logged in.  
<h3>Step 3</h3>
<p>
You have a session id for a logged in user from Step 1. Change the session id by using the Tamper Data Firefox plugin or by typing the following script in the browser url bar:<br/><br/>
javascript:document.cookie='JSESSIONID=INSERT_SESSION_ID_HERE';<br/><br/>Execute the script and reload the url.</p>

<h3>Step 4</h3>
<p>
Now that you have seen how session fixation works. Try to secure the login servlet against this vulnerability. The source code for the login servlet is located in : <br/>
<b>Java Resources/org.owasp.esapi.swingset.login.LoginServletLab.java</b> 
</p>

<%@include file="footer.jsp" %>
