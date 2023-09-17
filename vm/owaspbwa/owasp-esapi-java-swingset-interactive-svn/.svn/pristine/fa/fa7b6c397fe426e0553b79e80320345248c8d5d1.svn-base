<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Encryption">Tutorial</a> | 
<a href="main?function=Encryption&lab">Lab: Cryptography</a> | 
<a href="main?function=Encryption&solution">Solution</a> |
<a href="main?function=Randomizer&lab">Lab: Randomizer</a> |
<a href="main?function=Randomizer&solution">Solution</a> |
<b><a href="main?function=Integrity&lab">Lab: Integrity Seals</a></b> |
<a href="main?function=Integrity&solution">Solution</a>
</div>
<div id="header"></div>
<p>
<hr>

<h2>Integrity Seals Lab</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\IntegrityLab.jsp</h4>
<p>TODO: Relevant Example?</p>
<p>Data that expires after an elapsed time should be generated with the [Encryptor.seal()] method and verified with the [Encryptor.verifySeal()] method. </p>

<p>ESAPI Integrity Seals are used to create a seal that binds a set of data and includes an expiration timestamp.</p>
<p>The goal of this exercise is to seal, unseal and verify the data entered in the text fields below.</p>

<p><b>Java Location: Java Resources:src/org.owasp.esapi.swingset.actions.IntegrityLab.java</b></p>

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
<form action="main?function=Integrity&lab" method="POST">
<tr>
	<th width="50%">Enter something to seal</th>
</tr>
<tr>
	<td><textarea style="width:750px; height:50px" name="unsealed"><%=unsealed %></textarea><br />Seal will be valid for: <input type="text" size="5" maxlength="7" name="timer" value="<%=timer %>"> seconds.
	<input type="submit" value="seal"></td>
</tr>
</form>
<form action="main?function=Integrity&solution&unseal" method="POST">
<th>This is the sealed text</th>
<tr>
	<td><textarea style="width:750px; height:50px" name="sealed"><%=sealed %></textarea><br />
	<input type="submit" value="unseal"></td>
</tr>
</form>
<form action="main?function=Integrity&solution" method="POST">
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
