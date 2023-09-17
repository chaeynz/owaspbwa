package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class EncryptionSecure {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String encrypted = request.getParameter( "encrypted" );
		String decrypted = request.getParameter( "decrypted" );
		
		if ( encrypted != null ) try {
			decrypted = ESAPI.encryptor().decrypt( encrypted );
			encrypted = "";
		} catch( EnterpriseSecurityException e ) {
			decrypted = e.getLogMessage();
		} else if ( decrypted != null ) try {
			encrypted = ESAPI.encryptor().encrypt( decrypted );
			decrypted = "";
		} catch( EnterpriseSecurityException e ) {
			encrypted = e.getLogMessage();
		} else {
			decrypted = "Encrypt me right now";
			encrypted = null;
		}
		request.setAttribute("encrypted", encrypted);
		request.setAttribute("decrypted", decrypted);
	}
}
