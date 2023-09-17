<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=AccessControl">Tutorial</a> | 
<a href="main?function=AccessControl&lab">Lab : Forced Browsing</a>| 
<b><a href="main?function=AccessControl&solution">Solution</a></b> |
<a href="main?function=ObjectReference&lab">Lab : Direct Object Reference</a> | 
<a href="main?function=ObjectReference&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>


<h2 align="center">Access Control Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\AccessControlSolution.jsp</h4>

<p>Add the following line to the URLAccessControl.txt file located in <i>.esapi/fbac-policies</i><br/><br />
<br />
/SwingSet/admin_solution.jsp    | admin    | allow  |</p>
</p>

<p>Adding this ensures that only the admin user will have access to admin_solution.jsp. </p>

<p>After adding to URLAccessControl.txt. Click on the url below, The requested jsp will display corresponding text depending upon the boolean value returned by the ESAPI's isAuthorizedForURL() method.</p>
<p>The requested jsp will also display respective log messages and the boolean value returned by the ESAPI's isAuthorizedForURL() method.</p>



<h4>Secured Admin Page</h4>
The requested jsp should say "Sorry you do not have the access to this page!"</p>
<a href="/SwingSet/admin_solution.jsp" target="_blank">http://localhost/SwingSet/admin_solution.jsp</a><br /><br />

<%@include file="footer.jsp" %>