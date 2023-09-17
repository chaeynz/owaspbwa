<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=AccessControl">Tutorial</a> | 
<b><a href="main?function=AccessControl&lab">Lab : Forced Browsing</a></b>| 
<a href="main?function=AccessControl&solution">Solution</a> |
<a href="main?function=ObjectReference&lab">Lab : Direct Object Reference</a> | 
<a href="main?function=ObjectReference&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2 align="center">Access Control Lab</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\AccessControlLab.jsp</h4>

<h4>Background:</h4>
<p>The url below is a hidden administration page that has no access control. </p>
<p>The administration jsp will display corresponding text depending upon the boolean value returned by the ESAPI's isAuthorizedForURL() method.</p>
<p>The requested jsp will also display respective log messages and the boolean value returned by the ESAPI's isAuthorizedForURL() method.</p>
<p>Your goal is to secure the admin page by changing the URLAccessRules.txt file. The URLAccessRules.txt file is located in <i>.esapi\fbac-policies</i></p>

<h4>Admin URL</h4>
<p>The access privilege for this url is assigned as "any" which means any user can access this url.</p>
<p>Click on the url below. The requested jsp would say Uh oh... you have the access to this page!</p>
<a href="/SwingSet/admin_lab.jsp" target="_blank">http://localhost/SwingSet/admin_lab.jsp</a><br /><br />
<p>Try changing the URLAccessRules.txt file and change the rule of /SwingSet/admin_lab.jsp from<br /> | any      | allow  | to | any      | deny  |.</p>
<p>Restart Tomcat and hit the URL again. This time the requested jsp would say "Sorry you do not have the access to this page!"</p>

<%@include file="footer.jsp" %>