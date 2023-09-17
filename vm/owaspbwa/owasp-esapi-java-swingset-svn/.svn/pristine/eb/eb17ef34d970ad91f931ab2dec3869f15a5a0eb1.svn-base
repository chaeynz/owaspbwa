<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="org.owasp.esapi.swingset.models.*" %>

<%@include file="header.jsp" %>

<h2 align="center">Exercise</h2>

The exercises on this page are rule-by-rule examples from the 
<a href="http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet">XSS (Cross Site Scripting) Prevention Cheat Sheet</a>.
The forms on this page have no validation or escaping. This exemplifies what can happen when proper security controls are not in place.
<br><br>
PLEASE NOTE: Not all of these injections will work in all browsers. For example, Firefox does not support xss:expression javascript in style tags.
<br><br>
<span style="color:red;"><b>WARNING:</b></span> Some of these examples may cause seemingly infinite alert() messages in your browser!
Save all work and bookmark any open tabs you want to keep in the event you must force kill your browser.
In particular, the xss:expression examples will cause infinite alert() messages on Internet Explorer 8.

<form name="form" action="main?function=XSS&mode=insecure" method="POST">

<% 
ArrayList<XSSRule> rules = (ArrayList<XSSRule>)request.getAttribute("XSSRules");
for (XSSRule rule: rules) {
%>
	
<hr><h4><a href="<%= rule.getRuleURL() %>"><%= rule.getRuleTitle() %></a></h4>

	<%
	ArrayList<XSSContext> contexts = rule.getContexts();
	for (XSSContext context: contexts) {
	%>
	<hr>
	<div><%= context.getComment() %>
		<ul>
		<%
		ArrayList<XSSAttack> attacks = context.getAttacks();
		for (XSSAttack attack: attacks) {
		%>
			<li><%= attack.getComment() %></li>
			<ul>
				<%
				ArrayList<String> examples = attack.getExamples();
				for (String example: examples) {
				%>
				<li><a href="" onclick="document.form.input<%= context.getContextCode() %>.focus(); document.form.input<%= context.getContextCode() %>.value='<%= ESAPI.encoder().encodeForJavaScript(example) %>'; return false;"><%= ESAPI.encoder().encodeForHTML(example) %></a></li>
				<%
				} // End for each example loop
				%>
			</ul>
		<%
		} //End for each attack loop
		%>
		<li><a href="" onclick="document.form.input<%= context.getContextCode() %>.focus(); document.form.input<%= context.getContextCode() %>.value='null'; return false;">Clear form</a></li>
		</ul>
	</div>
	<table align="center" width="80%" border="1">
		<tr><td>
			<div><%= ESAPI.encoder().encodeForHTML(context.getFormPart(0)) %><input name="input<%= context.getContextCode() %>" class="text" type="text" value="<%= context.getInputEncodedForHTMLAttribute() %>" /><%= ESAPI.encoder().encodeForHTML(context.getFormPart(1)) %></div>
		</td></tr>
		<% String filledForm = context.getFormPart(0) + context.getInput() + context.getFormPart(1); %>
		<tr><td>
			Source HTML: <span class="sourceHTML"><%= ESAPI.encoder().encodeForHTML(filledForm) %></span>
		<tr><td>
			Rendered HTML: <span class="renderedHTML"><%= filledForm %></span>
		</td></tr>
	</table>
	
	<%
	} //End for each context loop
	%>
	
<%
} //End for each rule loop
%>

<br /><br />

<input type="submit" value="Submit">

</form>

<%@include file="footer.jsp" %>
