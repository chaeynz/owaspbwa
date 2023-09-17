package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class LoginSecure {

	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		try {
			if ( request.getParameter( "logout" ) != null && request.getParameter( "logout" ).equals("true")) 
				//if URL contains logout, logout
				ESAPI.authenticator().logout();
			
			Authenticator instance = ESAPI.authenticator();
			//Create user for demo if user does not exist
			if(!instance.exists("swingset")) 
				instance.createUser("swingset", "lookatme01@", "lookatme01@");
			
			//Enable user if disabled (disabled after creation by default)
			if(!instance.getUser("swingset").isEnabled()) 
				instance.getUser("swingset").enable();

			// Set current user
		 	// This should be done first for accountability
			// This is typically done once a framework or filter
			// You can access user anywhere using ESAPI.authenticator().getCurrentUser()
			
			/*
			String accountName = user.getAccountName();
			Date lastLoginTime = user.getLastLoginTime();
			Date lastFailedLogin = user.getLastFailedLoginTime();
			int failedLoginCount = user.getFailedLoginCount();
			roles = user.getRoles();
			String lastHost = user.getLastHostAddress();
			
			request.setAttribute("accountName", accountName);
			request.setAttribute("lastLoginTime", lastLoginTime);
			request.setAttribute("lastFailedLogin", lastFailedLogin);
			request.setAttribute("failedLoginCount", failedLoginCount);
			request.setAttribute("roles", roles);
			request.setAttribute("lastHost", lastHost);
			*/
			//request.setAttribute("user", user);
			
			// set a remember cookie
			if ( request.getParameter( "remember" ) != null && request.getParameter( "remember" ).equalsIgnoreCase("on")) {				
				// password must be right at this point since we're logged in.
				String password = request.getParameter( "password" );
				int maxAge = ( 60 * 60 * 24 * 14 );
				String token = ESAPI.httpUtilities().setRememberToken( ESAPI.httpUtilities().getCurrentRequest(), ESAPI.httpUtilities().getCurrentResponse(), password, maxAge, null, null );
				request.setAttribute("message", "New remember token:" + token );
			}
		} catch( EnterpriseSecurityException e ) {
			// Any unhandled security exceptions result in an immediate logout and login screen
			ESAPI.authenticator().logout();
			
		}
	}

}
