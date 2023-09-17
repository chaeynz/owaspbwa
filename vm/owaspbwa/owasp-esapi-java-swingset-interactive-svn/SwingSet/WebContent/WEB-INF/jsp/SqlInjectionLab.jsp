<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 11)%>
<div id="header"></div>
<p>

<h2>SQL Injection Lab</h2>
<h4>Java Location: Java Resources:src/org.owasp.esapi.swingset.actions.CommandInjectionLab.java</h4>

<% if (session.getAttribute("labUser") == null) { %>
	<p>The following "WebMail" demo application uses a HSQL database to manage users and there messages.
	Before the users can view there messages they need to login first. The used SQL statements are not 
	parameterized so you can simply bypass the login with a user name like  </p>
	
	<p class="newsItem">
	<code>
	' OR 1 = 1 --
	</code>
	</p>
	
	<H4>Please login</H4>
	<font color="red"><%=request.getAttribute("error") != null ? request.getAttribute("error") : "" %></font>
	<form action="main?function=SqlInjection&lab" method="POST">
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
<%} else { %>
	<p>Now that you are logged in the users messages are displayed. You can see the messages of the first user 
	because the system takes the userId of the first entry returned by the login query (normally there is just 
	one entry because there is only one username). <br />The site has a filter option so that users can decide to only 
	display messages from senders whose name matches a certain substring. You can misuse this filter to display 
	for example all users or all messages. </p>
	
	<p class="newsItem">
	<code>
	// Displays all users and there passwords<br />
	%' UNION SELECT name, password FROM users --<br /><br />
	// Displays all messages from all users<br />
	%' UNION SELECT sender, text FROM messages --
	</code>
	</p>
	
	<p>Now your goal is to secure those SQL statements.</p>
	
	<H4>Welcome <%=session.getAttribute("labUser") %></H4>
	<form action="main?function=SqlInjection&lab" method="POST">
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
	<a href="main?function=SqlInjection&lab&logout">Logout</a>
<%} %>

<%@include file="footer.jsp" %>
