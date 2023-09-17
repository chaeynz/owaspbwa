<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%@page import="org.apache.log4j.WriterAppender"%>
<%@page import="org.apache.log4j.SimpleLayout"%>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Logging">Logging Tutorial</a> | 
<b><a href="main?function=Logging&lab">Lab: Logging</a></b> | 
<a href="main?function=Logging&solution">Solution</a> | 
<a href="main?function=ErrorHandling">Error Handling Tutorial</a> | 
<a href="main?function=ErrorHandling&lab">Lab: Error Handling</a> | 
<a href="main?function=ErrorHandling&solution">Lab: Error Handling</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2>Logging Exercise</h2>

<h4>JSP Location: WebContent\WEB-INF\JSP\LoggingLab.jsp</h4>

<h3>Create a simple log message</h3>
<p>Your goal is to create a simple log message (which will appear below).</p>

<h3>Message to log:</h3>
<form method="post">
	<input name="log_message" type="text" value="Write a log message here ..." style="width:400px;"/>
	<input type="submit" value="Submit">
</form>

<h3>Log message output:</h3>

<% 				

	// the message to log
	String log_message = request.getParameter("log_message");

	WriterAppender appender = new WriterAppender(new SimpleLayout(), out);
	org.apache.log4j.Logger.getRootLogger().addAppender(appender);
	
	// --------------------------------------------------------------------------
	// all logs created below this line will be written to JspWriter "out"
	// this will only work if log4j (Log4JLogFactory) is used in ESAPI.properties
	out.println("<textarea style='width:700px' rows='10'>");
	
	//TODO: log the log_message
	
	out.println("</textarea>");
	// end of logging to "out"
	// --------------------------------------------------------------------------
	
	org.apache.log4j.Logger.getRootLogger().removeAppender(appender);
%>

<%@include file="footer.jsp" %>
