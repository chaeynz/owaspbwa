<%@page import="org.apache.log4j.SimpleLayout"%>
<%@page import="org.apache.log4j.HTMLLayout"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<b><a href="main?function=Logging">Logging Tutorial</a></b> | 
<a href="main?function=Logging&lab">Lab: Logging</a> | 
<a href="main?function=Logging&solution">Solution</a> | 
<a href="main?function=ErrorHandling">Error Handling Tutorial</a> | 
<a href="main?function=ErrorHandling&lab">Lab: Error Handling</a> | 
<a href="main?function=ErrorHandling&solution">Lab: Error Handling</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2 align="center">Tutorial</h2>

<p>The ESAPI Logger should promote secure logging functionality while allowing organizations 
to choose their own logging framework. The primary benefit of the ESAPI Logger is the addition of 
relevant security information to the log message and the use of specific tags that allow log messages 
to be identified as SECURITY related (as opposed to FUNCTIONAL, PERFORMANCE, etc). 
</p>

<p>The Logger interface defines a set of methods that can be used to log security events. It supports a hierarchy of logging levels which can be configured at runtime to determine the severity of events that are logged, and those below the current threshold that are discarded. Implementors should use a well established logging library as it is quite difficult to create a high-performance logger.</p> 

 

<p>The logging levels defined by this interface (in descending order) are: 

<ul>
<li>fatal (highest value)</li> 
<li>error </li>
<li>warning </li>
<li>info </li>
<li>debug </li>
<li>trace (lowest value)</li>
</ul> 
</p>
<p>
ESAPI also allows for the definition of the type of log event that is being generated. The Logger interface predefines 4 types of Log events: SECURITY_SUCCESS, SECURITY_FAILURE, EVENT_SUCCESS, EVENT_FAILURE. Your implementation can extend or change this list if desired. This Logger allows callers to determine which logging levels are enabled, and to submit events at different severity levels.
</p>
<p>
Implementors of this interface should:<br/> 
<ul>
<li>provide a mechanism for setting the logging level threshold that is currently enabled. This usually works by logging all events at and above that severity level, and discarding all events below that level. This is usually done via configuration, but can also be made accessible programmatically.</li> 
<li>ensure that dangerous HTML characters are encoded before they are logged to defend against malicious injection into logs that might be viewed in an HTML based log viewer.</li> 
<li>encode any CRLF characters included in log data in order to prevent log injection attacks.</li> 
<li>avoid logging the user's session ID. Rather, they should log something equivalent like a generated logging session ID, or a hashed value of the session ID so they can track session specific events without risking the exposure of a live session's ID.</li> 
<li>record the following information with each event:
<ul> 
<li>identity of the user that caused the event,</li> 
<li>a description of the event (supplied by the caller),</li> 
<li>whether the event succeeded or failed (indicated by the caller),</li> 
<li>severity level of the event (indicated by the caller),</li> 
<li>that this is a security relevant event (indicated by the caller),</li> 
<li>hostname or IP where the event occurred (and ideally the user's source IP as well),a time stamp</li>
</ul>
 </li>
<li>Custom logger implementations might also filter out any sensitive data specific to the current application or organization, such as credit cards, social security numbers, etc. 
There are both Log4j and native Java Logging default implementations. JavaLogger uses the java.util.logging package as the basis for its logging implementation. Both default implementations implements requirements #1 thru #5 above.</li>
</ul>
</p>
<h3>Customization</h3>
<p> It is expected that most organizations will implement their own custom Logger class in order to integrate ESAPI logging with their logging infrastructure. The ESAPI Reference Implementation is intended to provide a simple functional example of an implementation.</p>

<h3>Configuration</h3> 
<p>
There are various steps required to configure ESAPI for logging
<ul>
<li>Define the Log factory in the ESAPI.properties file (in your resources directory)</li>
<li>Define the Logger.* properties in ESAPI.properties</li>
<li>The custom org.owasp.esapi.reference.Log4JLogFactory2 implementation used in this SwingSet demonstrates a possible implementation using Logger.LogFileName and Logger.MaxLogFileSize to write all log messages to a log file. However, future versions of ESAPI will remove Logger.LogFileName and Logger.MaxLogFileSize from ESAPI.properties. You should use the native configuration method of the logger you use in stead (e.g. log4j.xml or log4j.properties if you use log4j).</li>
</ul>
</p>
<h3>Use</h3> 
<p>The Log4JLogFactory reference implementation can be used in the following way:</p>
<p class="newsItem">
<code>//sample usage of ESAPI's Logger<br />
Logger logger = ESAPI.getLogger("some Class or String");<br />
<br />
logger.fatal(Logger.SECURITY_FAILURE, "some log message");<br />
logger.debug(Logger.EVENT_FAILURE, "another log message");<br />
</code>
</p>

<%@include file="footer.jsp" %>
