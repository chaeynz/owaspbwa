package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class LoginSolution {

	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		try {
			if ( request.getParameter( "logout" ) != null ) //if URL contains logout, logout
				ESAPI.authenticator().logout();
			
			Authenticator instance = ESAPI.authenticator();
			//Create user for demo if user does not exist
			if(!instance.exists("admin")) 
				instance.createUser("admin", "lookatme01@", "lookatme01@");
			
			//Enable user if disabled (disabled after creation by default)
			if(!instance.getUser("admin").isEnabled()) 
				instance.getUser("admin").enable();
			
			// set a remember cookie
			if ( request.getParameter( "remember" ) != null ) {				
				// password must be right at this point since we're logged in.
				String password = request.getParameter( "password" );
				int maxAge = ( 60 * 60 * 24 * 14 );
				String token = ESAPI.httpUtilities().setRememberToken( ESAPI.httpUtilities().getCurrentRequest(), ESAPI.httpUtilities().getCurrentResponse(), password, maxAge, "", null );
				request.setAttribute("message", "New remember token:" + token );
			}
		} catch( EnterpriseSecurityException e ) {
			// Any unhandled security exceptions result in an immediate logout and login screen
			ESAPI.authenticator().logout();
			
		}
	}

}
