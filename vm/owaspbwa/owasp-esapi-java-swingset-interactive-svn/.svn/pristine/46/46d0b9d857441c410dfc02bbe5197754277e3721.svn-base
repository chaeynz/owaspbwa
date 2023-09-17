<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%@page import="org.apache.log4j.WriterAppender"%>
<%@page import="org.apache.log4j.SimpleLayout"%>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Logging">Logging Tutorial</a> | 
<a href="main?function=Logging&lab">Lab: Logging</a> | 
<b><a href="main?function=Logging&solution">Solution</a></b> | 
<a href="main?function=ErrorHandling">Error Handling Tutorial</a> | 
<a href="main?function=ErrorHandling&lab">Lab: Error Handling</a> | 
<a href="main?function=ErrorHandling&solution">Lab: Error Handling</a></div>
<div id="header"></div>
<p>
<hr>


<h2>Logging Solution</h2>

<p>The solution below consists of</p>
<ol>
<li>Creating a Logger for this page and then</li>
<li>Writing a log message while defining</li>
<ul>The logging level (e.g. FATAL, DEBUG)</ul>
<ul>The event type (e.g. SECURITY_FAILURE, EVENT_SUCCESS)</ul>
<ul>The log message</ul>
</ol>
<p>Log4j is set up to print to the console by default. In this application, it was also set up to print to the log file as defined in ESAPI.properties.</p>
<p>Notice how the user gets a unique but random identifier in stead of the sessionID and unsafe HTML is automatically encoded (if set in ESAPI.properties).</p>

<h3>Log messages:</h3>

<% 				
	WriterAppender appender = new WriterAppender(new SimpleLayout(), out);
	org.apache.log4j.Logger.getRootLogger().addAppender(appender);
	// all logs created below this line will be written to JspWriter "out"
	// this will only work if log4j (Log4JLogFactory) is used in ESAPI.properties
	
	out.println("<textarea style='width:700px' rows='10'>");
	Logger logger = ESAPI.getLogger("LoggingTutorialModule");
	logger.fatal(Logger.SECURITY_FAILURE, "A log message containing unsafe html: <script>alert('123')</script>");
	logger.debug(Logger.EVENT_SUCCESS, "A Debug Log message: <scr<script>ipt>alert('abc')</script> ");
	out.println("</textarea>");
	
	// end of logging to "out"
	org.apache.log4j.Logger.getRootLogger().removeAppender(appender);
%>


<%@include file="footer.jsp" %>
