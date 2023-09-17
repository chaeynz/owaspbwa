<%@page import="java.nio.charset.Charset"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.catalina.tribes.group.interceptors.TwoPhaseCommitInterceptor.MapEntry"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.owasp.esapi.crypto.CipherText"%>
<%@page import="org.owasp.esapi.crypto.PlainText"%>
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
	byte[] crypt = null;
	CipherText cipherText = null;
	if (request.getParameter("decrypted") != null) {
		decrypted = request.getParameter("decrypted");
		crypt = ESAPI.encryptor().encrypt(new PlainText(decrypted)).asPortableSerializedByteArray();
	}
	if (request.getParameter("encrypted") != null) {
		encrypted = request.getParameter("encrypted");
		cipherText = CipherText.fromPortableSerializedBytes(ESAPI.encoder().decodeFromBase64(encrypted));
	}
%>

<h2>Encryption Solution</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\EncryptionSolution.jsp</h4>

<p>The values below are encrypted and decrypted with : </p>
<code>
CipherText ESAPI.encryptor().encrypt(PlainText plaintext)<br/>
PlainText ESAPI.encryptor().decrypt(CipherText ciphertext)<br/>
</code>
<br/><br/>
<table width="100%" border="1">
	<tr>
		<th width="50%">Enter something to encrypt</th>
		<th>Enter something to decrypt (Base64 encoded)</th>
	</tr>
	<tr>
		<td>
			<form action="main?function=Encryption&solution" method="POST">
				<textarea style="width:300px; height:150px" name="decrypted"><%=
					(cipherText == null)? "" : ESAPI.encryptor().decrypt(cipherText).toString()
				%></textarea>
				<input type="submit" value="encrypt"><br>
			</form>
		</td>
		<td>
			<form action="main?function=Encryption&solution" method="POST">
				<textarea style="width:300px; height:150px" name="encrypted"><%=
					(crypt == null) ? "" : ESAPI.encoder().encodeForBase64(crypt, true)
				%></textarea>
				<input type="submit" value="decrypt"><br>
			</form>
		</td>
	</tr>
</table>

<%@include file="footer.jsp" %>
