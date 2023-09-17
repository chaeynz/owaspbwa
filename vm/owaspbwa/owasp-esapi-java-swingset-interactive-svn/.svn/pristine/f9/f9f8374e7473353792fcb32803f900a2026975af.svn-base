<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Encoding">Tutorial</a> | 
<a href="main?function=Encoding&lab">Lab : Encoding</a> |
<b><a href="main?function=Encoding&solution">Solution</a></b> |
<a href="main?function=XSS&lab">Lab : XSS</a> | 
<a href="main?function=XSS&solution">Solution</a> |
<a href="main?function=Canonicalize&lab">Lab : Canonicalize</a> | 
<a href="main?function=Canonicalize&solution">Solution</a>
</div>
<div id="header"></div>
<p>

<%
	String input = request.getParameter( "input" );
	if ( input == null ) input = "encode 'this' <b>string</b> null \0 byte";
	Codec oracle = new OracleCodec();
	Codec mysqlansi = new MySQLCodec( MySQLCodec.ANSI_MODE);
	Codec mysql = new MySQLCodec( MySQLCodec.MYSQL_MODE);
%>

<h2>HTML Encoding Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\EncodingSolution.jsp</h4>

<p>Enter whatever input you want. Click submit and the table below will be populated using different Encoding methods.</br></p>

<form action="main?function=Encoding&solution" method="POST">	
	<textarea style="width:400px; height:150px" name="input"><%=input %></textarea><br>
	<input type="submit" value="submit">
</form>

<div style="overflow-x:scroll;">
<table border="1">
<tr><th>Input</th><th>Encoded output</th></tr>
<tr><td>Unencoded</td><td><p><pre><%=input %></pre></td></tr>
<tr><td>HTML Body (encodeForHTML)</td><td><p><pre><%=ESAPI.encoder().encodeForHTML(input) %></pre></p></td></tr>
<tr><td>HTML Attribute (encodeForHTMLAttribute)</td><td><p><pre><%=ESAPI.encoder().encodeForHTMLAttribute(input) %></pre></p></td></tr>
<tr><td>Javascript (encodeForJavascript)</td><td><p><pre><%=ESAPI.encoder().encodeForJavaScript(input) %></pre></p></td></tr>
<tr><td>VBScript (encodeForVBScript)</td><td><p><pre><%=ESAPI.encoder().encodeForVBScript(input) %></pre></p></td></tr>
<tr><td>CSS (encodeForCSS)</td><td><p><pre><%=ESAPI.encoder().encodeForCSS(input) %></pre></p></td></tr>
<tr><td>URL (encodeForURL)</td><td><p><pre><%=ESAPI.encoder().encodeForURL(input) %></pre></p></td></tr>
<tr><td>Base 64 (encodeForBase64)</td><td><p><pre><%=ESAPI.encoder().encodeForBase64(input.getBytes(), false) %></pre></p></td></tr>
<tr><td>LDAP (encodeForLDAP)</td><td><p><pre><%=ESAPI.encoder().encodeForLDAP(input) %></pre></p></td></tr>
<tr><td>Oracle (encodeForSQL) - discouraged use PreparedStatement</td><td><p><pre><%=ESAPI.encoder().encodeForSQL(oracle, input) %></pre></p></td></tr>
<tr><td>MySQL (encodeForSQL) - discouraged use PreparedStatement</td><td><p><pre><%=ESAPI.encoder().encodeForSQL(mysql, input) %></pre></p></td></tr>
<tr><td>MySQLAnsi (encodeForSQL) - discouraged use PreparedStatement</td><td><p><pre><%=ESAPI.encoder().encodeForSQL(mysqlansi, input) %></pre></p></td></tr>
<tr><td>XML (encodeForXML)</td><td><p><pre><%=ESAPI.encoder().encodeForXML(input) %></pre></p></td></tr>
<tr><td>XML Attribute (encodeForXMLAttribute)</td><td><p><pre><%=ESAPI.encoder().encodeForXMLAttribute(input) %></pre></p></td></tr>
<tr><td>LDAP Distinguished Name (encodeForDN)</td><td><p><pre><%=ESAPI.encoder().encodeForDN(input) %></pre></p></td></tr>
<tr><td>XPath Query (encodeForXPath)</td><td><p><pre><%=ESAPI.encoder().encodeForXPath(input) %></pre></p></td></tr>
</table>
</div>

<h2>Quick Reference</h2>

<p>Important: The characters below are what is produced by the ESAPI codecs. These
represent the most standard ways of encoding for the listed interpreters. However,
there are many other <i>legal</i> encoding formats. For example, the ESAPI default
is to use decimal HTML entities if there is not a named entity, but hexidecimal entities
(e.g. &amp;#x26;) are completely legal. ESAPI follows the principle of being liberal
in what it accepts (for canonicalization) and strict in what it emits.

<div style="overflow-x:scroll;"> 
<table width="100%">
<tr align="center" bgcolor="yellow">
<th width="10%">int</th>
<th width="10%">char</th>
<th width="10%">html body</th>
<th width="10%">html attr</th>
<th width="10%">javascript</th>
<th width="10%">vbscript</th>
<th width="10%">css</th>
<th width="10%">url</th>
<th width="10%">oracle</th>
<th width="10%">mysql</th>
<th width="10%">mysqlansi</th>
<th width="10%">xml</th>
<th width="10%">xml attr</th>
<th width="10%">ldap</th>
<th width="10%">ldap dn</th>
<th width="10%">xpath</th>
</tr>
</div>

<%
for( int i = 0; i < 1024; i++ ) {
String c = "" + (char)i;
try {
%>
<tr bgcolor="#e0e0e0" align="center">
	<td bgcolor="yellow"><%=i %></td>
	<td bgcolor="yellow"><%=c %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForHTML(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForHTMLAttribute(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForJavaScript(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForVBScript(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForCSS(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForURL(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForSQL(oracle, c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForSQL(mysql, c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForSQL(mysqlansi, c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForXML(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForXMLAttribute(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForLDAP(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForDN(c)) %></td>
	<td><%=ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForXPath(c)) %></td>
</tr>
<%
} catch (Exception e) {
	e.printStackTrace();
}
}
%>
</table>

<%@include file="footer.jsp" %>
