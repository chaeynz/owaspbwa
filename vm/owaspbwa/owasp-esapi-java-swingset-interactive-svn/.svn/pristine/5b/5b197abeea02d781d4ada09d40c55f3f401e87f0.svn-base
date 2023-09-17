package org.owasp.esapi.swingset;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.EnterpriseSecurityException;

/**
 * @author jwilliams
 */
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = ESAPI.getLogger( "SwingSet" );

	
	
    public void init(ServletConfig config) throws ServletException {
    	// no parameters in web.xml    	
    	super.init(config);    	    
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Initialize ESAPI request and response
		ESAPI.httpUtilities().setCurrentHTTP(request, response);		
		
		try {
			hack();
			String function = request.getParameter("function");			
			if ( function != null ) {
				String prefix = "org.owasp.esapi.swingset.actions."; 
				String shortName = (""+function.charAt(0)).toUpperCase() + function.substring(1);
				
				// Figure out which page was requested
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
				request.setAttribute( "function", shortName );
				//System.out.println("---------------------------------------------" + '\n' + "START DEBUG" + '\n' + "---------------------------------------------" + '\n' + "prefix: " + prefix + '\n' + "shortName: " + shortName + '\n' + "pname: " + pname + '\n' + "---------------------------------------------" + '\n' + "END DEBUG" + '\n' + "---------------------------------------------");
				logger.info( Logger.SECURITY_SUCCESS, "Invoked " + pname + ".java -> " + pname + ".jsp" );
				RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/"+pname+".jsp");
				dispatcher.forward(request, response);
				return;
			}
		}catch (EnterpriseSecurityException e){
			request.setAttribute("userMessage", e.getUserMessage());
			request.setAttribute("logMessage", e.getLogMessage());
		}catch ( Exception e ) {
			logger.error( Logger.SECURITY_FAILURE, e.getMessage(), e );
			request.setAttribute("message", "EXCEPTION: " + e.getMessage() );
	
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/index.jsp");
		dispatcher.forward(request, response);
	}

	private void hack() throws EnterpriseSecurityException {
		// trick compiler
	}
}
