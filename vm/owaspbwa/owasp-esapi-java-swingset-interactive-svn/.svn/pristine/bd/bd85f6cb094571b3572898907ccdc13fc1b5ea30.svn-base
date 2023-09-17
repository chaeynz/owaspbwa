<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<div id="navigation">
<a href="main">Home</a> | 
<a href="main?<%=querystring%>">Tutorial</a> | 
<a href="main?function=ValidateUserInput&lab">Lab : Validate User Input</a> | 
<a href="main?function=ValidateUserInput&solution">Solution</a> |
<a href="main?function=RichContent&lab">Lab : Rich Content</a> | 
<b><a href="main?function=RichContent&solution">Solution</a></b>
</div>
<div id="header"></div>
<p>
<hr>

<%
String input = "<p>test <b>this</b> <script>alert(document.cookie)</script><i>right</i> now</p>";
String markup = "testing";
if( request.getParameter("input") != null )
	input = request.getParameter( "input" );
try{
	markup = ESAPI.validator().getValidSafeHTML("input", input, 2500, false);
}
catch(Exception e){
	ESAPI.log().error(Logger.EVENT_FAILURE, e.getMessage());
}
%>

<h2>Rich Content Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\RichContentSolution.jsp</h4>

<p>The input is validated using ESAPI.validator().getValidSafeHTML()</p>

<form action="?function=RichContent&solution" method="POST">
	<table width="100%" border="1">
	<tr><th width="50%">Enter whatever markup you want</th><th>Safe HTML rendered</th><th>HTML encoded</th></tr>
	<tr><td><textarea style="width:400px; height:150px" name="input"><%=input %></textarea><input type="submit" value="render"><br></td><td><%=markup %></td><td><%=ESAPI.encoder().encodeForHTML(markup) %></td></tr>
	</table>
</form>

<%@include file="footer.jsp" %>
