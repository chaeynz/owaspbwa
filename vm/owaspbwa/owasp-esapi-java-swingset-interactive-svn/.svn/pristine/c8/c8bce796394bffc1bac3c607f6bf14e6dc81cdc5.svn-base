package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.Randomizer;
import org.owasp.esapi.errors.EnterpriseSecurityException;
import org.owasp.esapi.reference.DefaultRandomizer;

public class GUIDSolution {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		Randomizer random = new DefaultRandomizer();
		
		String randomGUID = random.getRandomGUID();
		request.setAttribute("randomGUID", randomGUID);	
	}
}
