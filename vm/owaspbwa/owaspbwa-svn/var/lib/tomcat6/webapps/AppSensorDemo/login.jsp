<%@include file="header_unauthenticated.jsp" %>



<form name="input" action="Login" method="POST">
<table>

<tr>
<td>Login</td>
<td><input type="text" name="username" AUTOCOMPLETE="off"></td>
</tr>

<tr>
<td>Password</td>
<td><input type="password" name="password"></td>
</tr>
<tr>
<td><input type="submit" value="Submit"></td>
</tr>


</table>
</form>
<br>
You can use any of the following accounts for this test system.<br>

foo : foo<br>
sue : sue<br>
bob : bob<br>
<br>
<%@include file="footer.jsp" %>
