<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Encoding">Tutorial</a> | 
<a href="main?function=Encoding&lab">Lab : Encoding</a> |
<a href="main?function=Encoding&solution">Solution</a> |
<a href="main?function=XSS&lab">Lab : XSS</a>| 
<a href="main?function=XSS&solution">Solution</a> |
<a href="main?function=Canonicalize&lab">Lab : Canonicalize</a> | 
<b><a href="main?function=Canonicalize&solution">Solution</a></b>
</div>
<div id="header"></div>
<p>
<hr>


<%

	String input = request.getParameter( "input" );
	if ( input == null ) input = "%2&#x35;2%3525&#x32;\\u0036lt;\r\n\r\n%&#x%%%3333\\u0033;&%23101;";

	String canonical = "";
	// do it in strict mode just to get the warnings
	String userMessage = null;
	String logMessage = null;
	try{
		canonical = ESAPI.encoder().canonicalize(input, true);
	} catch( IntrusionException e ) {
		userMessage = e.getUserMessage();
		logMessage = e.getLogMessage();
	}	

	// now redo it in non-strict mode to get the real answer
	canonical = ESAPI.encoder().canonicalize(input, false);
%>

<h2 align="center">Canonicalize Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\CanonicalizeSolution.jsp</h4>

<form action="main?function=Canonicalize&solution" method="POST">
	<table>
	<tr><td>Original</td><td>Decoded</td></tr>
	<tr><td>
		<textarea style="width:300px; height:150px" name="input"><%=ESAPI.encoder().encodeForHTML(input)%></textarea>
	</td><td>
		<textarea style="width:300px; height:150px"><%=ESAPI.encoder().encodeForHTML(canonical)%></textarea>
	</td></tr></table>
	<input type="submit" value="submit">
</form>
<p>User Message: <font color="red"><%=ESAPI.encoder().encodeForHTML(userMessage) %></font></p>
<p>Log Message: <font color="red"><%=ESAPI.encoder().encodeForHTML(logMessage) %></font></p><hr>
<p>
<h2 align="center">Quick Reference</h2>

<table border=0 width="100%">
<tr align="center">
<td bgcolor="yellow">int</td><td>hex</td><td>char</td><td bgcolor="black">&nbsp;</td>
<td bgcolor="yellow">int</td><td>hex</td><td>char</td><td bgcolor="black">&nbsp;</td>
<td bgcolor="yellow">int</td><td>hex</td><td>char</td><td bgcolor="black">&nbsp;</td>
<td bgcolor="yellow">int</td><td>hex</td><td>char</td>
<%
PercentCodec pc = new PercentCodec();
for( int i=1; i<65; i++ ) {
int value = i;
%>
<tr align="center">
<td bgcolor="yellow"><%=value%></td>
<td><%=pc.toHex( (char)value )%></td>
<td><%=(char)value %></td>
<td bgcolor="black">&nbsp;</td>

<%
value+=64;
%>
<td bgcolor="yellow"><%=value%></td>
<td><%=pc.toHex( (char)value )%></td>
<td><%=(char)value %></td>
<td bgcolor="black">&nbsp;</td>

<%
value+=64;
%>
<td bgcolor="yellow"><%=value%></td>
<td><%=pc.toHex( (char)value )%></td>
<td><%=(char)value %></td>
<td bgcolor="black">&nbsp;</td>

<%
value+=64;
%>
<td bgcolor="yellow"><%=value%></td>
<td><%=pc.toHex( (char)value )%></td>
<td><%=(char)value %>
</tr>
<%
}
%>
</table>


<%@include file="footer.jsp" %>
