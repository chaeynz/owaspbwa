<%@page import="org.owasp.esapi.reference.FileBasedAuthenticator2"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<div id="navigation">
	<a href="main">Home</a> | <a href="main?function=Login">Tutorial</a> |
	<b><a href="main?function=Login&lab">Lab : Authenticator
			Functions</a>
	</b>| <a href="main?function=Login&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2>Authenticator Methods Lab</h2>

<h4>JSP Location : WebContent\WEB-INF\jsp\LoginLab.jsp</h4>

<p>The goal of this exercise is to create an ESAPI user account and then log in using that user.</p>
<p> Additionally, generate a Logout link for users who are logged in.</p>

<p>Use the form below and the functions detailed in the tutorial to
	implement this.</p>

<p>After creating a user you should see a users.txt file in your
	.esapi folder.</p>

<H2>Create User</H2>
<form action="main?function=Login&lab" method="POST">
	<table>
		<tr>
			<td>Username:</td>
			<td><input name="create_username">
			</td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type="password" name="create_password1"
				autocomplete="off">
			</td>
		</tr>
		<tr>
			<td>Confirm Password:</td>
			<td><input type="password" name="create_password2"
				autocomplete="off">
			</td>
		</tr>
	</table>
	<input type="submit" value="Create User"><br>
</form>

<H2>Login</H2>
<form action="main?function=Login&lab" method="POST">
	<table>
		<tr>
			<td>Username:</td>
			<td><input name="username">
			</td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type="password" name="password" autocomplete="off">
			</td>
		</tr>
		<!-- <tr>
			<td>Remember me on this computer:</td>
			<td><input type="checkbox" name="remember">
			</td>
		</tr> -->
	</table>
	<input type="submit" value="login"><br>
</form>

<%@include file="footer.jsp"%>

