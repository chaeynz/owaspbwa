<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 1)%>
<div id="header"></div>
<p>

<%
	String input = request.getParameter( "input" );
	if ( input == null ) input = "encode this string";
%>


<h2>HTML Encoding Exercise</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\EncodingLab.jsp</h4>
<p>Enter some javascript in the following text box. E.g.</p>
<code>
		<b>&lt;script&gt;alert(document.cookie)&lt;/script&gt;</b>
</code>
<p>Your goal is to html encode the input so the script is not executed.</p>
<form action="main?function=Encoding&lab" method="POST">	
	<textarea style="width:400px; height:150px" name="input"><%=input %></textarea>
	<input type="submit" value="submit"><br></td>
</form>

<!-- TODO HTML Encode the Output -->
<p>Output : <%=input %></p>

<%@include file="footer.jsp" %>
