package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.swingset.models.*;

import org.owasp.esapi.errors.EnterpriseSecurityException;

public class OutputUserInputSecure {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		request.setAttribute("OutputUserExamples", OutputUserInputData.examples);
	}
}
