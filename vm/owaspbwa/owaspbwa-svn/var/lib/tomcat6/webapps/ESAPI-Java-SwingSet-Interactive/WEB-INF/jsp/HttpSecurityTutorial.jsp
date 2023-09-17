<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<b><a href="main?function=HttpSecurity">Tutorial</a></b> | 
<a href="main?function=HttpSecurity&lab">Lab : Unvalidated Redirect/Forward</a>| 
<a href="main?function=HttpSecurity&solution">Solution</a> 
</div>
<div id="header"></div>
<p>
<hr>
<h2>Unvalidated Redirects/Forwards</h2>

<p>Web Application Redirects and Forwards are very common and frequently include user
supplied parameters in the destination URL. If they aren't validated, an attacker can 
potentially send victims to a site of their choice (say a phishing or malware site). 
Internal Redirects can also cause problems if not validated. An attacker may be able to 
bypass authentication or authorization checks. 
</p>

<%@include file="footer.jsp" %>