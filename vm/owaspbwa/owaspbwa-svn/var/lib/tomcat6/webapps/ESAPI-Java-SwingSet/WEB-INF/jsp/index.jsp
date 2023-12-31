
<%
String title = "ESAPI SwingSet Demonstration Application beta";
%>

<html>
<head>
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="style/style.css" />
</head>

<body>
<div id="container">
	<div id="holder">
		<div id="logo"><img src="style/images/owasp-logo_130x55.png" width="130" height="55" alt="owasp_logo" title="owasp_logo"></div>
<h1><%=title %></h1>
<div id="header"></div>
<hr> 

<h2>Input Validation, Encoding, and Injection</h2>
<ul>
<li><a href="main?function=OutputUserInput">Output User Input</a></li>
<li><a href="main?function=RichContent">Accept Rich Content</a></li>
<li><a href="main?function=ValidateUserInput">Validate User Input</a></li>
<li><a href="main?function=Encoding">Encode Output</a></li>
<li><a href="main?function=Canonicalize">Canonicalize Input</a></li>
</ul>

<h2>Cross Site Scripting</h2>
<ul>
<li><a href="main?function=XSS">Cross Site Scripting</a></li>
</ul>

<h2>Authentication and Session Management</h2>
<ul>
<li><a href="main?function=Login">Login</a></li>
<!-- <li><a href="main?function=Logout">Logout</a></li> (no implementation)-->
<li><a href="main?function=ChangePassword">Change Password</a></li>
<li><a href="main?function=ChangeSessionIdentifier">Change Session Identifier</a></li>
</ul>

<h2>Access Control and Referencing Objects</h2>
<ul>
<li><a href="main?function=ObjectReference">Reference a Server-Side Object</a></li>
<li><a href="main?function=AccessControl">Access Control</a></li>
</ul>

<h2>Encryption, Randomness, and Integrity</h2>
<ul>
<li><a href="main?function=Encryption">Encryption</a></li>
<li><a href="main?function=Randomizer">Randomizer</a></li>
<li><a href="main?function=Integrity">Integrity Seals</a></li>
<li><a href="main?function=GUID">Globally Unique IDs</a></li>
</ul>

<h2>Caching</h2>
<ul>
<li><a href="main?function=BrowserCaching">Browser Caching</a></li>
</ul>

<%@include file="footer.jsp" %>
