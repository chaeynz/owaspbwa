<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=Encryption">Tutorial</a> | 
<a href="main?function=Encryption&lab">Lab: Cryptography</a> | 
<b><a href="main?function=Encryption&solution">Solution</a></b> |
<a href="main?function=Randomizer&lab">Lab: Randomizer</a> |
<a href="main?function=Randomizer&solution">Solution</a> |
<a href="main?function=Integrity&lab">Lab: Integrity Seals</a> |
<a href="main?function=Integrity&solution">Solution</a>
</div>
<div id="header"></div>
<p>


<%
	String decrypted = "";
	String encrypted = "";
	if (request.getParameter("decrypted") != null)
		decrypted = request.getParameter("decrypted");
	if (request.getParameter("encrypted") != null)
		encrypted = request.getParameter("encrypted");		
%>

<h2>Encryption Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\EncryptionSolution.jsp</h4>

<p>The values below are encrypted and decrypted with : </p>
<code>
ESAPI.encoder().encrypt()<br/>
ESAPI.encoder().decrypt()<br/>
</code>
<br/><br/>
<table width="100%" border="1">
	<tr>
		<th width="50%">Enter something to encrypt</th>
		<th>Enter something to decrypt</th>
	</tr>
	<tr>
		<td>
			<form action="main?function=Encryption&solution" method="POST">
				<textarea style="width:300px; height:150px" name="decrypted"><%=decrypted %></textarea>
				<input type="submit" value="encrypt"><br>
			</form>
		</td>
		<td>
			<form action="main?function=Encryption&solution" method="POST">
				<textarea style="width:300px; height:150px" name="encrypted"><%=encrypted %></textarea>
				<input type="submit" value="decrypt"><br>
			</form>
		</td>			
	</tr>	
</table>
<p>Encrypted Value: <%=(decrypted == null || decrypted.length() == 0) ? "NULL" : ESAPI.encryptor().encrypt(decrypted)%></p>	
<p>Decrypted Value: <%=(encrypted == null || encrypted.length() == 0) ? "NULL" : ESAPI.encryptor().decrypt(encrypted)%></p>

<%@include file="footer.jsp" %>
