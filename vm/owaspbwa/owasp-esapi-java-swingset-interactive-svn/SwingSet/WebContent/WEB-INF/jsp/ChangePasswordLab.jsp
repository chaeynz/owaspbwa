<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2>Exercise: Enter Malicious Input</h2>
<%
	String oldPassword = request.getParameter( "oldPassword" );
	String newPassword1 = request.getParameter( "newPassword1" );
	String newPassword2 = request.getParameter( "newPassword2" );
	if ( oldPassword == null ) oldPassword = "type input here";
	if ( newPassword1 == null ) newPassword1 = "type input here";
	if ( newPassword2 == null ) newPassword2 = "type input here";
%>

		<form action="main?function=ChangePasswordInsecure" method="POST">
		<table><tr><td>Old Password:</td><td><input type="password" name="oldPassword"></td></tr>
		<tr><td>New Password:</td><td><input type="password" name="newPassword1" autocomplete="off"></td></tr>
		<tr><td>Re-type new Password:</td><td><input type="password" name="newPassword2" autocomplete="off"></td></tr></table>
		<input type="submit" value="Save"><br>
		</form>

<%@include file="footer.jsp" %>
