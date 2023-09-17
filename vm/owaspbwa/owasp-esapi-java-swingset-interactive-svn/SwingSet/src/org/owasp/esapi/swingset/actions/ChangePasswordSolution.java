package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.EnterpriseSecurityException;
//import org.owasp.esapi.reference.FileBasedAuthenticator;

public class ChangePasswordSolution {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String oldPassword, newPassword1, newPassword2 = "";
		String result = "";
		String default_username = "admin";
		String default_password = "lookatme01@";
		boolean invalid = false;

		if(request.getParameter("oldPassword") != null){
			oldPassword = request.getParameter("oldPassword");
			newPassword1 = request.getParameter("newPassword1");
			newPassword2 = request.getParameter("newPassword2");
								
			try{
				Authenticator instance = ESAPI.authenticator();
				//FileBasedAuthenticator instance2 = (FileBasedAuthenticator)ESAPI.authenticator();
				if(!instance.exists(default_username)){
					User user1 = instance.createUser(default_username, default_password, default_password);
					user1.enable();
					//user1.loginWithPassword("l00katmeg0!@34");
				}
				User user1 = instance.getUser("admin");
				request.setAttribute("oldPassword", request.getParameter("oldPassword"));
				request.setAttribute("newPassword1", request.getParameter("newPassword1"));
				request.setAttribute("newPassword2", request.getParameter("newPassword2"));
				request.setAttribute("currentUser", instance.getCurrentUser().toString());
				request.setAttribute("testCase", "hi there!");
				request.setAttribute("accountName", instance.getUser("admin").getAccountName());
				request.setAttribute("isLocked", user1.isLocked());
				request.setAttribute("pwChanged", user1.getLastPasswordChangeTime());
				request.setAttribute("account", user1.getAccountName());
						
				invalid = true;						
				user1.changePassword(oldPassword, newPassword1, newPassword2);
				//instance2.saveUsers();
				result = "Success! Your password has been changed!";
			}
			/*
			 * catch (AuthenticationCredentialsException e){
				System.out.println("Error: " + e);
				if(invalid)
					result = "Password change failed.<br />Old password entered does not match stored old password.";
			}
			*/
			catch(Exception e){
				//System.out.println("Error: " + e);
				if(invalid)
					result = "Password change failed.<br /><ul><u>Possible causes:</u><li>The old password you entered does not match stored old password</li><li>Your new password does not meet password complexity requirements</li><li>The new password you entered is a recently used password</li><li>The new password you entered is part of a recently used password</li></ul>";
			}
		}
		request.setAttribute("result", result);	
	}
}
