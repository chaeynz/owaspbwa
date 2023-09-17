package org.owasp.esapi.swingset;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.EnterpriseSecurityException;

/**
 * 
 * @author Pawan Singh
 *
 */
public class AjaxEngine extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = ESAPI.getLogger( "AjaxEngine" );

	public void init() {
	}

	public void destroy() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//forward through doPost
		logger.info( Logger.EVENT_SUCCESS, "AjaxEngine doGet method called");
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info( Logger.EVENT_SUCCESS, "AjaxEngine doPost method called");
		// Initialize ESAPI request and response
		ESAPI.httpUtilities().setCurrentHTTP(request, response);
		
		try {
			hack();
			String function = request.getParameter("function");
			if ( function != null ) {
				String prefix = "org.owasp.esapi.swingset.actions."; 
				String shortName = (""+function.charAt(0)).toUpperCase() + function.substring(1);
				
				// figure out which page was requested
				String pname = shortName + "Tutorial";
				if ( request.getParameter("solution") != null ) {
					pname = shortName + "Solution";
				} else if ( request.getParameter( "lab" ) != null ) {
					pname = shortName + "Lab";
				}
				//set default method as invoke
				String method = "invoke";
				//figure out which method will be called
				if(request.getParameter("method")!=null) {
					method = request.getParameter("method");
				}
				
				Class[] sig = {HttpServletRequest.class, HttpServletResponse.class};
				Object[] params = { request, response };
				try {
					Class.forName( prefix + pname ).getMethod(method, sig).invoke(this, params);
				} catch( Exception e ) {
					logger.warning(Logger.SECURITY_FAILURE, "Failed to execute " + prefix + pname + ".java", e );
					e.printStackTrace();
				}
				return;
			}
		}catch ( Exception e ) {
			logger.error( Logger.SECURITY_FAILURE, e.getMessage(), e );
		}
	}
		
	private void hack() throws EnterpriseSecurityException {
		// trick compiler
	}
}
