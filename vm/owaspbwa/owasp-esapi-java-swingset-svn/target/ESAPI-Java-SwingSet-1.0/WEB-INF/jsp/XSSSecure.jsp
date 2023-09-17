<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="org.owasp.esapi.swingset.models.*" %>

<h2 align="center">Secure</h2>

The exercises on this page are rule-by-rule examples from the 
<a href="http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet">XSS (Cross Site Scripting) Prevention Cheat Sheet</a>.
The forms on this page <b>have</b> the proper escaping and encoding, as recommended in the Cheat Sheet.
<br><br>
Viewing this page in your browser will show that the attacks are no longer effective, and you can look at the source to see the escaping and encoding.

<form name="form" action="main?function=XSS&mode=secure" method="POST">

<% 
ArrayList<XSSRule> rules = (ArrayList<XSSRule>)request.getAttribute("XSSRules");
for (XSSRule rule: rules) {
%>
	
<hr><h4><a href="<%= rule.getRuleURL() %>"><%= rule.getRuleTitle() %></a></h4>
	Active encoding: <b><%= rule.getEncoderText() %></b>
	<br>
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
		<% 
			String encodedInput = rule.encodeForRule(context.getInput());
			String filledForm = context.getFormPart(0) + encodedInput + context.getFormPart(1); 
		%>
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
