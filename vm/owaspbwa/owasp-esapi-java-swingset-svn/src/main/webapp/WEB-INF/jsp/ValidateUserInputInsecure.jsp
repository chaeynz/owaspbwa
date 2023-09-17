<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	String output = "";
	String input = request.getParameter( "input" );
	if(request.getAttribute("output")!=null)output = request.getAttribute("output").toString();
	if ( input == null ) input = "type input here";
%>

<h2 align="center">Exercise</h2>

<form action="main?function=ValidateUserInput&mode=insecure" method="POST">
	<h4>Enter malicious data in the textbox</h4>
	<p class="newsItem">
	<code>
		EXAMPLE: <b>&lt;script&gt;alert(document.cookie)&lt;/script&gt;</b>
	</code>
	</p>
	<textarea style="width:300px; height:100px" name="input"><%=input %></textarea>
	<br /><br /><input type="submit" value="Submit">
</form>
<h4>Unvalidated output: </h4><%=output %>
<%@include file="footer.jsp" %>
