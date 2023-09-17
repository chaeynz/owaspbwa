<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2>Background</h2>
One method many people use to safeguard their application from a XSS attack is to filter out the &lt;script&gt; tag.  While this may seem like it would prevent an attack involving javascript, it does contain some flaws.
One way to bypass this filtering is to input &lt;scr&lt;script&gt;ipt&gt;.  When &lt;script&gt; is removed, the two halves of the other &lt;script&gt; tag will come together, forming an attack.  Try this below and see what happens.<br /><br />
<%
	String input = request.getParameter( "input" );
	if ( input == null ) input = "<p>test <b>this</b> <script>alert(document.cookie)</script><i>right</i> now</p>";
	String markup = input.replaceAll("<script>", "");
%>

<hr><br><h2>Exercise: Enter Rich HTML Markup</h2>
 
<form action="main?function=RichContent&mode=insecure" method="POST">
	<table width="100%" border="1"><tr><th width="50%">Enter whatever markup you want</th><th>Safe HTML rendered</th></tr>
	<tr><td><textarea style="width:400px; height:150px" name="input"><%=input %></textarea>
	<input type="submit" value="render"><br></td>
	<td><%=markup %></td></tr></table>
</form>

<%@include file="footer.jsp" %>
