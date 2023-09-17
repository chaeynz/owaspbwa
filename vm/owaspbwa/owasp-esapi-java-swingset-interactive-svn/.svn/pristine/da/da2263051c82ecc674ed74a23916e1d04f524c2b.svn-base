<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	String result = "";
	String test="bob";
	
	if(request.getAttribute("testCase") != null){
		test = request.getAttribute("testCase").toString();
	}
	if(request.getAttribute("result") != null){
		result = request.getAttribute("result").toString();
	}
%>

<h2>Exercise: Enter Malicious Input</h2>
Any changes you make to the password will persist until the server is restarted.<br />
This is due to the users.txt file not being written to when passwords are changed. To write to users.txt, use the saveUsers() method from FileBasedAuthenticator.java.<br />
Every time the server is loaded, the original password will be: <b>lookatme01@</b><br /><br />

<div style="border: 1px solid black; padding-left:10px;">
New passwords must be at least 8 characters in length and contain at least two of the following sets: 
<ul>
<li>lower-case letters</li>
<li>upper-case letters</li>
<li>digits</li>
<li>special characters ( valid characters include . - _ ! @ $ ^ * = ~ | + ? )</li>
</ul>
		<form action="main?function=ChangePassword&solution" method="POST">
		<table><tr><td>Old Password:</td><td><input type="password" name="oldPassword"></td></tr>
		<tr><td>New Password:</td><td><input type="password" name="newPassword1" autocomplete="off"></td></tr>
		<tr><td>Re-type new Password:</td><td><input type="password" name="newPassword2" autocomplete="off"></td></tr></table>
		<input type="submit" value="Save"><br>
		</form>

</div>
<!--  
Old Password: <%=request.getAttribute("oldPassword") %><br />
New Passsword1: <%=request.getAttribute("newPassword1") %><br />
New Passsword2: <%=request.getAttribute("newPassword2") %><br />
Current User: <%=request.getAttribute("currentUser") %><br />
Account Name: <%=request.getAttribute("accountName") %><br />
User1 Account name: <%=request.getAttribute("account") %><br />
Locked? <%=request.getAttribute("isLocked") %><br />
Password last changed: <%=request.getAttribute("pwChanged") %><br />
Test Case: <%=test %><br /><br />
-->
<%=result %>
<%@include file="footer.jsp" %>
