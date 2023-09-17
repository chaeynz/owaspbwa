<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2 align="center">Tutorial</h2>
<h4>Change Password: </h4>
<p>Following method in the ESAPI's User interface helps in securely changing the user password</p>
<b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/org/owasp/esapi/User.html#changePassword(java.lang.String, java.lang.String, java.lang.String)">changePassword(String oldPassword, String newPassword1, String newPassword2)</a></b> 
<p><br />Sets the user's password, performing a verification of the user's old password, the equality of the two new passwords, and the strength of the new password.</p>
<p>Proper logger messages are displayed on every successful/unsuccessful password change.</p>
<p>Throws AuthenticationCredentialsException if newPassword1 does not match newPassword2, if oldPassword does not match the stored old password, or if the new password does not meet complexity requirements 
</p> 
<%@include file="footer.jsp" %>
