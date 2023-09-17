<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<h2>Enter Malicious Data to Seal</h2>

<%
String unsealed = "";
String sealed = "";
String verified = "";
String sealToVerify = "";
String timer = "15";
if(request.getAttribute("timer") != null) timer = request.getAttribute("timer").toString();
if(request.getAttribute("verified") != null) verified = request.getAttribute("verified").toString();
if(request.getAttribute("sealed") != null) sealed = request.getAttribute("sealed").toString();
if(request.getAttribute("sealToVerify") != null) sealToVerify = request.getAttribute("sealToVerify").toString();
if(request.getAttribute("unsealed") != null) unsealed = request.getAttribute("unsealed").toString();

%>

<table width="100%" border="1">
<form action="main?function=Integrity&mode=secure" method="POST">
<tr>
	<th width="50%">Enter something to seal</th>
</tr>
<tr>
	<td><textarea style="width:750px; height:50px" name="unsealed"><%=unsealed %></textarea><br />Seal will be valid for: <input type="text" size="5" maxlength="7" name="timer" value="<%=timer %>"> seconds.
	<input type="submit" value="seal"></td>
</tr>
</form>
<form action="main?function=Integrity&mode=secure&unseal" method="POST">
<th>This is the sealed text</th>
<tr>
	<td><textarea style="width:750px; height:50px" name="sealed"><%=sealed %></textarea><br />
	<input type="submit" value="unseal"></td>
</tr>
</form>
<form action="main?function=Integrity&mode=secure" method="POST">
<tr>
	<th>Seal to verify</th>
</tr>
<tr>
	<td><textarea style="width:750px; height:50px" name="sealToVerify"><%=sealToVerify %></textarea><br />
	<input type="submit" value="verify"></td>
</tr>
<th>Result of Seal Verification</th>
<tr>
	<td>Verification: <%=verified %> </td>
</tr>
<br>
</form>
</table>


<%@include file="footer.jsp" %>
