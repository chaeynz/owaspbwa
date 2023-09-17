package org.owasp.esapi.swingset.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.ExecuteResult;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.ExecutorException;

/**
 * Handles the whole Command Injection solution.
 * 
 * @author Christopher Popp
 */
public class CommandInjectionSolution extends CommandInjectionAction {
	
	/** Custom logger of this class */
	private static final Logger logger = ESAPI.getLogger("CommandInjectionSolution");
	
	/**
	 * Standard method which get called by the Controller if the Command Injection solution
	 * got requested.
	 * 
	 * @param request - an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response - an HttpServletResponse object that contains the response the servlet sends to the client 
	 */
	public static void invoke( HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
				
		if (username != null) { // form has been sent
			if (!username.equals("") && !email.equals("") && !password.equals("") && !password2.equals("")) {
				try {
					List<String> args = new ArrayList<String>();
					args.add("/C");
					args.add(batchPath);
					args.add(username);
					args.add(email);
					args.add(password);
					args.add(password2);
					
					ExecuteResult er = ESAPI.executor().executeSystemCommand(
							new File("C:\\Windows\\System32\\cmd.exe"), args);
					
					String output = er.getOutput();
		            String errors = er.getErrors();
		            
					if (errors != null && errors.length() > 0) {
						// Error during command execution
						request.setAttribute("batchResult", "Batch execution failed!");
						logger.error(Logger.EVENT_FAILURE, errors);
					} else {
						request.setAttribute("batchResult", output);
					}
				} catch (ExecutorException e) {
					logger.error(Logger.EVENT_FAILURE, e.getMessage());
				}
			} else {
				request.setAttribute("error", "You have to fill in the whole form!");
			}
		}
	}
}
