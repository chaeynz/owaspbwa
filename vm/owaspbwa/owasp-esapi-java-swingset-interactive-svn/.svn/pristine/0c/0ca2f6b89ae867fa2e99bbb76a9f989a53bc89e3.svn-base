<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<div id="navigation">
	<a href="main">Home</a> | <a href="main?function=Encryption">Tutorial</a>
	| <b><a href="main?function=Encryption&lab">Lab: Cryptography</a> </b>
	| <a href="main?function=Encryption&solution">Solution</a> | <a
		href="main?function=Randomizer&lab">Lab: Randomizer</a> | <a
		href="main?function=Randomizer&solution">Solution</a> | <a
		href="main?function=Integrity&lab">Lab: Integrity Seals</a> | <a
		href="main?function=Integrity&solution">Solution</a>
</div>
<div id="header"></div>
<p>

	<%
		String encrypted = "";
		String decrypted = "Encrypt me right now";
		String encryptedParam = request.getParameter("encrypted");
		String decryptedParam = request.getParameter("decrypted");	
		
		//TODO encrypt/decrypt the received parameters and re-display them in the appropriate form input fields
	%>

<h2>Encryption Lab</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\EncryptionLab.jsp</h4>
<p>
	Use the ESAPI encryption methods to encrypt and decrypt the text values
	below. <br /> 
	This lab requires the presence of the encryption keys in the ESAPI.properties file. 
<!-- 	<a href="/SwingSet/main?function=InitialSetup">See Step 4</a> -->
</p>
<table width="100%" border="1">
	<tr>
		<th width="50%">Enter something to encrypt</th>
		<th>Enter something to decrypt</th>
	</tr>
	<tr>
		<td>
			<form action="main?function=Encryption&lab" method="POST">
				<textarea style="width: 300px; height: 150px" name="decrypted"><%=decrypted%><%//TODO : Decrypt the POSTed value %></textarea>
				<input type="submit" value="encrypt"><br>
			</form>
		</td>
		<td>
			<form action="main?function=Encryption&lab" method="POST">
				<textarea style="width: 300px; height: 150px" name="encrypted"><%=encrypted%><%//TODO : Encrypt the POSTed value %></textarea>
				<input type="submit" value="decrypt"><br>
			</form>
		</td>
	</tr>
</table>
<p></p>
<p>
Note: The string-based encrypt() and decrypt() methods have been deprecated in favor of the new CipherText-based methods:
</p>
<p class="newsItem">
<code>
CipherText ESAPI.encryptor().encrypt(PlainText plainText)
PlainText ESAPI.encryptor.decrypt(CipherText ciphertext)<br />
</code>
</p>
<p>
This Swingset Application uses ESAPI 2.0 rc4 which doesn't offer easy serialization methods for CipherText objects. <br />
In more recent releases of ESAPI 2.0 (e.g. rc11), the CipherText interface and reference implementation offer the following simple serialization and de-serialization methods which are portable across other ESAPI programming language implementations as well:
</p>
<p class="newsItem">
<code>
byte[] asPortableSerializedByteArray()<br />
static CipherText fromPortableSerializedBytes(byte[] bytes)<br />
</code>
</p>
<p>
</p>
<%@include file="footer.jsp"%>
