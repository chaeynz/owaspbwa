<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	String input = request.getParameter( "input" );
	if ( input == null ) input = "encode this string";
%>

<hr><br><h2>Exercise: Enter Rich HTML Markup</h2>

<form action="main?function=Encoding&mode=insecure" method="POST">
	Enter whatever input you want
	<textarea style="width:400px; height:150px" name="input"><%=input %></textarea>
	<input type="submit" value="submit"><br></td>
</form>

<table>
<tr><th>Encoding method</th><th>Encoded output</th></tr>
<tr><td></td><td></td></tr>
</table>

<%@include file="footer.jsp" %>
