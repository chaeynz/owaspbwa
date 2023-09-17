<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	String encrypted = "";
	String decrypted = "Encrypt me right now";
	if( (request.getAttribute("decrypted") != null) && (request.getAttribute("encrypted") != null) ){
		encrypted = request.getAttribute("encrypted").toString();
		decrypted = request.getAttribute("decrypted").toString();
	}
%>

<hr><br><h2>Exercise: do something</h2>

<hr><br><h2>Encrypt and Decrypt</h2>
<table width="100%" border="1"><tr><th width="50%">Enter something to encrypt</th><th>Enter something to decrypt</th></tr>
<tr><td><form action="main?function=Encryption&mode=insecure" method="POST">
<textarea style="width:400px; height:150px" name="decrypted"><%=decrypted %></textarea><input type="submit" value="encrypt"><br></form></td>
<td><form action="main?function=Encryption&mode=secure" method="POST">
<textarea style="width:400px; height:150px" name="encrypted"><%=encrypted %></textarea><input type="submit" value="decrypt"><br></form></td>			</tr></table>

<%@include file="footer.jsp" %>
