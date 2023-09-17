<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2 align="center">Background</h2>

<p>Cross Site Scripting (XSS) is <u>the</u> most common web application security flaw.</p>
<p>One of the reasons that XSS is so difficult to eliminate in any web application is because each user supplied input parameter is a potential vector for XSS attacks and the solution for solving it can be specific for each particular parameter based on the allowed input values of the parameter (e.g., fixed form value like a number vs. text field) and where in the HTML these user supplied values are displayed back to the user by the application. Solutions to this problem also range from validating the input to escaping the output. However, there is no one size fits all solution that you can simply apply to all input or output in order to make your application safe, which is why XSS prevention is so difficult.</p>
<p>
OWASP has studied the XSS problem extensively and we have documented our research and recommended prevention approaches on the OWASP portal at: <a href="http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet">XSS (Cross Site Scripting) Prevention Cheat Sheet</a>. The following recommendations are based exactly on this cheat sheet and ESAPI has been developed to specifically provide the validation and escaping routines necessary to eliminate XSS in your applications.</p>
<h4>Preventing XSS using ESAPI's Escaping routines</h4>

<p>The XSS defense article indicates that there are numerous different contexts into which you might include user supplied input in the HTML you output back to the user. It starts off with a Rule #0 which indicates that you cannot safely include user supplied input in any HTML context except the 5 specific contexts defined in the article.
<div>The 5 allowed contexts are:
<ul><li>HTML Element Content</li>
<li>HTML Common Attributes</li>
<li>HTML Javascript Data Values</li>
<li>HTML Style Property Values</li>
<li>HTML URL Attributes</li></ul></div>
<p>However, you cannot simply include user supplied input in these contexts and be safe. You must escape this output propertly before doing so. The XSS Prevention article specifically defines a defense technique for each of these contexts. The technique description, and the ESAPI method for providing this type of escaping for each context is defined as follows:<ul><li>Rule #1: HTML Element Content</li>
	<ul>
	<li>HTML Escape Before Inserting Untrusted Data into HTML Element Content</li>
	<li><pre>Use ESAPI.encoder.<a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/org/owasp/esapi/Encoder.html#encodeForHTML(java.lang.String)">encodeForHTML(String input)</a></pre></li></ul>
<li>Rule #2: HTML Common Attributes</li>
	<ul>
	<li>Attribute Escape Before Inserting Untrusted Data into HTML Common Attributes</li>
	<li><pre>Use ESAPI.encoder.<a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/org/owasp/esapi/Encoder.html#encodeForHTMLAttribute(java.lang.String)">encodeForHTMLAttribute(String input)</a></pre></li></ul>
<li>Rule #3: HTML Javascript Data Values</li>
	<ul>
	<li>JavaScript Escape Before Inserting Untrusted Data into HTML JavaScript Data Values</li>
	<li><pre>Use ESAPI.encoder.<a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/org/owasp/esapi/Encoder.html#encodeForJavaScript(java.lang.String)">encodeForJavaScript(String input)</a></pre></li>
	</ul>
<li>Rule #4: HTML Style Property Values</li>
	<ul>
	<li>CSS Escape Before Inserting Untrusted Data into HTML Style Property Values</li>
	<li><pre>Use ESAPI.encoder.<a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/org/owasp/esapi/Encoder.html#encodeForCSS(java.lang.String)">encodeForCSS(String input)</a></pre></li></ul>
<li>Rule #5: HTML URL Attributes</li>
	<ul>
	<li>URL Escape Before Inserting Untrusted Data into HTML URL Attributes</li>
	<li><pre>Use ESAPI.encoder.<a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/org/owasp/esapi/Encoder.html#encodeForURL(java.lang.String)">encodeForURL(String input)</a></pre></li></ul></ul>
<p>Please review the XSS Insecure Demo to see how the lack of proper output escaping of user supplied input can introduce an XSS flaw in each of these 5 contexts (and remember that including user supplied input in any other HTML context is automatically unsafe, and no amount of output escaping will make it safe).

Then review the XSS Secure Demo to see how the proper output escaping using the recommended ESAPI escaping methods makes it safe to include properly escaped user supplied input.
</p>

<%@include file="footer.jsp" %>