<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 12)%>
<div id="header"></div>
<p>

<h2>SQL Injection Solution</h2>
<h4>Java Location: Java Resources:src/org.owasp.esapi.swingset.actions.CommandInjectionSolution.java</h4>


<% if (session.getAttribute("solUser") == null) { %>
	<p>The "WebMail" demo application is now secured against SQL Injections and uses a PreparedStatement
	for the login query. Once again try to bypass the login otherwise you can login as 'James' with the 
	password '1234'. </p>
	
	<p class="newsItem">
	<code>
	' OR 1 = 1 --
	</code>
	</p>
	
	<H4>Please login</H4>
	<font color="red"><%=request.getAttribute("error") != null ? request.getAttribute("error") : "" %></font>
	<form action="main?function=SqlInjection&solution" method="POST">
		<table>
			<tr>
				<td>Username:</td><td><input name="username"></td>
			</tr>
			<tr>
				<td>Password:</td><td><input type="password" name="password" autocomplete="off"></td>
			</tr>
		</table>
		<input type="submit" value="login"><br>
	</form>
	<br />
	<a href="main?function=SqlInjection&solution&resetDb">Reset database</a>
<%} else { %>
	<p>This time the filter string gets encoded by ESAPI.encoder() which makes sure that 
	the entered string just gets passed to the LIKE clause and there is surely no sender 
	with a name like "%' UNION SELECT ...".<br />
	But never forget that this is only an alternative if you do not have the possibility 
	to use prepared statements. Otherwise ALWAYS use prepared statements.</p>
	
	<p class="newsItem">
	<code>
	// Displays all users and there passwords<br />
	%' UNION SELECT name, password FROM users --<br /><br />
	// Displays all messages from all users<br />
	%' UNION SELECT sender, text FROM messages --
	</code>
	</p>
		
	<H4>Welcome <%=session.getAttribute("solUser") %></H4>
	<form action="main?function=SqlInjection&solution" method="POST">
		Filter: <input name="filter">
		<input type="submit" value="filter"><br>
	</form>
	<table border="1">
		<tr>
			<th>Sender</th>
			<th>Text</th>
		</tr>
		<% 	@SuppressWarnings("unchecked")
			Map<String, String> messages = (Map<String, String>)request.getAttribute("messages");
			for (Entry<String, String> e : messages.entrySet()) { %>
				<tr>
					<td><%=e.getKey()%></td>
					<td><%=e.getValue()%></td>
				</tr>
		<% 	} %>
	</table>
	<br />
	<a href="main?function=SqlInjection&solution&logout">Logout</a>
<%} %>

<%@include file="footer.jsp" %>