<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<b><a href="main?function=AccessControl">Tutorial</a></b> | 
<a href="main?function=AccessControl&lab">Lab : Forced Browsing</a>| 
<a href="main?function=AccessControl&solution">Solution</a> |
<a href="main?function=ObjectReference&lab">Lab : Direct Object Reference</a> | 
<a href="main?function=ObjectReference&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2 align="center">Tutorial</h2>
<p>The AccessController interface defines a set of methods that can be used in a wide variety of applications to enforce access control. In most applications, access control must be performed in multiple different locations across the various application layers. This class provides access control for URLs, business functions, data, services, and files. </p>


<p>For Forced Browsing lab we need first to set the following url access rules in the .esapi\fbac-policies\URLAccessRules.txt file.</p>  
<p class="newsItem">
<code>
# URL Access Rules
#
<br />
/SwingSet/admin_solution.jsp    | any    | allow  |<br />
/SwingSet/admin_solution.jsp    | admin    | allow  |<br />
</code>
</p>

<p>In the Forced Browsing lab, the following ESAPI function is used:</p>

<p class="newsItem">
<code>
boolean isAuthorizedForURL(String url) 
<br />Checks if an account is authorized to access the referenced URL. 
<br />Returns true, if is authorized for URL
</code>
<p>Once you click on the test url. The requested jsp calls the ESAPI's isAuthorizedForURL function. It displays the success and failure messages depending upon the boolean value returned by the function.</p>
<p>The jsp also displays the boolean value returned by calling ESAPI.accessController().isAuthorizedForURL(request.getRequestURI());
and the log message in case of authorization failure.</p>
<h4>ESAPI's AccessController Interface includes:</h4>
<ul>
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#assertAuthorizedForData(java.lang.String)">assertAuthorizedForData(java.lang.String key)</a></b> Checks if the current user is authorized to access the referenced data. This method simply returns if access is authorized.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#assertAuthorizedForData(java.lang.String, java.lang.Object)">assertAuthorizedForData(java.lang.String action, java.lang.Object data) </a></b> Checks if the current user is authorized to access the referenced data.</li><br />
    <li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#assertAuthorizedForFile(java.lang.String)">assertAuthorizedForFile(java.lang.String filepath)</a></b>Checks if an account is authorized to access the referenced file.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#assertAuthorizedForFunction(java.lang.String)">assertAuthorizedForFunction(java.lang.String functionName)</a></b>Checks if an account is authorized to access the referenced function.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#assertAuthorizedForService(java.lang.String)">assertAuthorizedForService(java.lang.String serviceName) </a></b>   Checks if an account is authorized to access the referenced service.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#assertAuthorizedForURL(java.lang.String)">assertAuthorizedForURL(java.lang.String url) </a></b>Checks if an account is authorized to access the referenced URL.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#isAuthorizedForData(java.lang.String)">boolean isAuthorizedForData(java.lang.String key) </a></b>Checks if an account is authorized to access the referenced data, represented as a String.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#isAuthorizedForData(java.lang.String, java.lang.Object)">boolean isAuthorizedForData(java.lang.String action, java.lang.Object data) </a></b>Checks if an account is authorized to access the referenced data, represented as an Object.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#isAuthorizedForFile(java.lang.String)">boolean isAuthorizedForFile(java.lang.String filepath) </a></b>Checks if an account is authorized to access the referenced file.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#isAuthorizedForFunction(java.lang.String)">boolean isAuthorizedForFunction(java.lang.String functionName) </a></b>Checks if an account is authorized to access the referenced function.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#isAuthorizedForService(java.lang.String)">boolean isAuthorizedForService(java.lang.String serviceName) </a></b>Checks if an account is authorized to access the referenced service.</li><br />
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/AccessController.html#isAuthorizedForURL(java.lang.String)">boolean isAuthorizedForURL(java.lang.String url)</a></b>Checks if an account is authorized to access the referenced URL.</li><br />
</ul>
<%@include file="footer.jsp" %>