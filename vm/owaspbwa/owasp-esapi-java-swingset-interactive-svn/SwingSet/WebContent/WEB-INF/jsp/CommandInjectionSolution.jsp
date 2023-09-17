<%@page import="java.util.Scanner"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 9)%>
<div id="header"></div>
<p>

<h2>Command Injection Solution</h2>
<h4>Java Location: Java Resources:src/org.owasp.esapi.swingset.actions.CommandInjectionSolution.java</h4>

<% String batchResult = (String)request.getAttribute("batchResult");
   if (batchResult == null) { %>
	<p>The batch call is now secured through the ESAPI.executer().executeSystemCommand(). 
	Once again you can try to execute an additional command but this time ESAPI will encode 
	the parameters which will just lead to a curious password instead of an injection.</p>
	
	<p class="newsItem">
	<code>
	// Confim Password:<br />
	a" &amp; dir ."<br />
	</code>
	</p>
	
	<H4>Registration</H4>
	<font color="red"><%=request.getAttribute("error") != null ? request.getAttribute("error") : "" %></font>
	<form action="main?function=CommandInjection&solution" method="POST">
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
	<a href="main?function=CommandInjection&solution">Back</a>
<%} %>

<%@include file="footer.jsp" %>