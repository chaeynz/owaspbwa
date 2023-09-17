<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
String input = "<p>test <b>this</b> <script>alert(document.cookie)</script><i>right</i> now</p>";
String markup = "testing";
if( request.getParameter("input") != null )
	input = request.getParameter( "input" );
try{
Validator instance = ESAPI.validator();
markup = instance.getValidSafeHTML("input", input, 2500, false);
}catch(Exception e){
System.out.println(e);
}
%>

<h2>Exercise: Enter Rich HTML Markup</h2>
<form action="?function=RichContent&mode=secure" method="POST">
	<table width="100%" border="1">
	<tr><th width="50%">Enter whatever markup you want</th><th>Safe HTML rendered</th><th>HTML encoded</th></tr>
	<tr><td><textarea style="width:400px; height:150px" name="input"><%=input %></textarea><input type="submit" value="render"><br></td><td><%=markup %></td><td><%=ESAPI.encoder().encodeForHTML(markup) %></td></tr>
	</table>
</form>

<%@include file="footer.jsp" %>
