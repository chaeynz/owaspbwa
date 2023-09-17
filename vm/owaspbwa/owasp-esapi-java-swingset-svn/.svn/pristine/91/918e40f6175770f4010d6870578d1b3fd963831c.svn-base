package org.owasp.esapi.swingset.actions;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.errors.EnterpriseSecurityException;

public class GUIDInsecure {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String GUID = UUID.randomUUID().toString();
		request.setAttribute("GUID", GUID);
	}
}
