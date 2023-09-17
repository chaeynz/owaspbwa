package org.owasp.esapi.swingset.actions;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.errors.EnterpriseSecurityException;

public class GUIDLab {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String [] GUIDArr = new String[5];
		String GUID = UUID.randomUUID().toString();
		GUIDArr = GUID.split("-");
		request.setAttribute("GUID1", GUIDArr[0]);
		request.setAttribute("GUID2", GUIDArr[1]);
		request.setAttribute("GUID3", GUIDArr[2]);
		request.setAttribute("GUID4", GUIDArr[3]);
		request.setAttribute("GUID5", GUIDArr[4]);
	}
}
