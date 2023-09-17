<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<div id="navigation">
	<a href="main">Home</a> | <a href="main?<%=querystring%>">Tutorial</a>
	| <a href="main?function=ValidateUserInput&lab">Lab : Validate User
		Input</a> | <b><a href="main?function=ValidateUserInput&solution">Solution</a>
	</b> | <a href="main?function=RichContent&lab">Lab : Rich Content</a> | <a
		href="main?function=RichContent&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<%
	String type = request.getParameter("type");
	if (type == null)
		type = "SafeString";
	String input = request.getParameter("input");
	if (input == null)
		input = "type input here";

	System.out.println(" >>>>>");
	byte[] inputBytes = input.getBytes("UTF-8");
	for (int i = 0; i < inputBytes.length; i++)
		System.out.print(" " + inputBytes[i]);
	System.out.println();

	String canonical = "";
	try {
		canonical = ESAPI.encoder().canonicalize(input, false);
		ESAPI.validator().getValidInput(
				"Swingset Validation Secure Exercise", input, type,
				200, false);
	} catch (ValidationException e) {
		input = "Validation attack detected";
		request.setAttribute("userMessage", e.getUserMessage());
		request.setAttribute("logMessage", e.getLogMessage());
	} catch (IntrusionException ie) {
		input = "double encoding attack detected";
		request.setAttribute("userMessage", ie.getUserMessage());
		request.setAttribute("logMessage", ie.getLogMessage());
	} catch (Exception e) {
		input = "exception thrown";
		request.setAttribute("logMessage", e.getMessage());
	}
%>

<h2 align="center">Validate User Input Solution</h2>
<h4>JSP
	Location:WebContent\WEB-INF\jsp\ValidateUserInputSolution.jsp</h4>

<!-- <p>1. The input is canonicalized using the ESAPI.encoder().canonicalize() to remove any encoded characters.</p> -->
<p>1. The input is canonicalized and validated using
	ESAPI.validator().getValidInput() which compares the string against a
	regular expression in validation.properties</p>
<p>2. The output is encoded for html using
	ESAPI.encoder().encodeForHTML() to prevent XSS.</p>
<p class="newsItem">
<code>
	EXAMPLE: <br/>
	<b>%252%35252\u0036lt;<br/>
	%&#x%%%3333\u0033;&%23101;</b>
</code>
</p>
<p>Note: The "Type/Regex" field accepts any of the values defined in
	the .esapi/validation.properties file be one of the following Regular
	Expressions.</p>
<p>
	SafeString : [A-Za-z0-9]{0,1024}$<br /> Email :
	^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$<br /> IPAddress :
	^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$<br />
	URL :
	^(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\:\\'\\/\\\\\\+=&amp;%\\$#_]*)?$<br />
	CreditCard : ^(\\d{4}[- ]?){3}\\d{4}$<br /> SSN :
	^(?!000)([0-6]\\d{2}|7([0-6]\\d|7[012]))([
	-]?)(?!00)\\d\\d\\3(?!0000)\\d{4}$<br />
</p>
<form action="main?function=ValidateUserInput&solution" method="POST">
	Type/Regex: <input name="type"
		value="<%=ESAPI.encoder().encodeForHTMLAttribute(type)%>"><br>
	<textarea style="width: 400px; height: 150px" name="input"><%=ESAPI.encoder().encodeForHTML(input)%></textarea>
	<br> <input type="submit" value="submit">
</form>

<p>
	Canonical output:
	<%=ESAPI.encoder().encodeForHTML(canonical)%></p>


<%@include file="footer.jsp"%>
