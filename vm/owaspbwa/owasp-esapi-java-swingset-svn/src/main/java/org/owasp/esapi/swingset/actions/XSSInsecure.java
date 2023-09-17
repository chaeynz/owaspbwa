/**
 * 
 */
package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EnterpriseSecurityException;
import org.owasp.esapi.swingset.models.*;

/**
 * @author Craig Younkins
 *
 */
public class XSSInsecure {

	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		
		ArrayList<XSSRule> rules = XSSData.getRulesForInsecure();
		for (XSSRule rule: rules) {
			for (XSSContext context: rule.getContexts()) {
				context.setInput(request.getParameter("input" + context.getContextCode()));
			}		
		}
		request.setAttribute("XSSRules", rules);
	}
}
