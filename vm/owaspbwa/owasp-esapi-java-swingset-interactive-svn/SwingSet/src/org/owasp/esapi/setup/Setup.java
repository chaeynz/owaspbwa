package org.owasp.esapi.setup;

import org.owasp.esapi.*;
import org.owasp.esapi.errors.AuthenticationException;


public class Setup {
	public static void main(String args[]){
		try{
			ESAPI.authenticator().createUser("test", "test0000", "test0000");
		}catch (AuthenticationException e){
			ESAPI.log().error(Logger.EVENT_FAILURE, e.getMessage());		
		}
	}
}
