package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class ChangePasswordInsecure {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		Authenticator instance = ESAPI.authenticator();
		if(!instance.exists("admin")){
			User user1 = instance.createUser("admin", "l00katmeg0!@34", "l00katmeg0!@34");
			user1.enable();
			user1.loginWithPassword("l00katmeg0!@34");
		}
		
	}
}
