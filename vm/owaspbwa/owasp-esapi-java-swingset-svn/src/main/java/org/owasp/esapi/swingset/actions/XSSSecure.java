/**
 * 
 */
package org.owasp.esapi.swingset.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.errors.EnterpriseSecurityException;
import org.owasp.esapi.swingset.models.XSSContext;
import org.owasp.esapi.swingset.models.XSSData;
import org.owasp.esapi.swingset.models.XSSRule;

/**
 * @author Pawan Singh
 *
 */
public class XSSSecure {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		
		
		ArrayList<XSSRule> rules = XSSData.getRulesForSecure();
		for (XSSRule rule: rules) {
			for (XSSContext context: rule.getContexts()) {
				context.setInput(request.getParameter("input" + context.getContextCode()));
			}		
		}
		request.setAttribute("XSSRules", rules);
		
	}

}
