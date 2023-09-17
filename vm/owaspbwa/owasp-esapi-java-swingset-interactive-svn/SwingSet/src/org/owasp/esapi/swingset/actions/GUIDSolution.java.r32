package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Randomizer;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class GUIDSolution {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		// CP_2012-05-19 Use factory method instead of creating a new instance 
		// of DefaultRandomizer() on its own
		Randomizer random = ESAPI.randomizer();
		
		String randomGUID = random.getRandomGUID();
		request.setAttribute("randomGUID", randomGUID);	
	}
}
