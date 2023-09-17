<%@page import="java.util.Scanner"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 8)%>
<div id="header"></div>
<p>

<h2>Command Injection Exercise</h2>
<h4>Java Location: Java Resources:src/org.owasp.esapi.swingset.actions.CommandInjectionLab.java</h4>

<% String batchResult = (String)request.getAttribute("batchResult");
   if (batchResult == null) { %>
	<p>The following registration page uses a batch script to create a new user. 
	This includes creating a home directory with a specific subfolder structure, copying 
	some default settings and creating an entry in the database.<br />
	The inputs which are passed to the batch are not validated and attackers could append 
	some additional commands within the "Confirm Password" field. For example you can display 
	all files in the current directory:</p>
	
	<p class="newsItem">
	<code>
	// Confim Password:<br />
	a" &amp; dir ."<br />
	<br />
	// Creates for example<br />
	newUser.bat "simon" "simon@abc.com" "a" "a" &amp; dir .""
	</code>
	</p>
	
	<p>Now your goal is to secure this batch call.</p>
	
	<H4>Registration</H4>
	<font color="red"><%=request.getAttribute("error") != null ? request.getAttribute("error") : "" %></font>
	<form action="main?function=CommandInjection&lab" method="POST">
		<table>
			<tr>
				<td>Username:</td><td><input name="username"></td>
			</tr>
			<tr>
				<td>E-Mail:</td><td><input name="email"></td>
			</tr>
			<tr>
				<td>Password:</td><td><input type="password" name="password" autocomplete="off"></td>
			</tr>
			<tr>
				<td>Confirm Password:</td><td><input type="password" name="password2" autocomplete="off"></td>
			</tr>
		</table>
		<input type="submit" value="Register"><br>
	</form>
<%} else { %>
	<% Scanner sc = new Scanner(batchResult);
	   while (sc.hasNextLine()) {%>
	   	<%=ESAPI.encoder().encodeForHTML(sc.nextLine())%><br />
	<% }%>
	<br />
	<a href="main?function=CommandInjection&lab">Back</a>
<%} %>

<%@include file="footer.jsp" %>
