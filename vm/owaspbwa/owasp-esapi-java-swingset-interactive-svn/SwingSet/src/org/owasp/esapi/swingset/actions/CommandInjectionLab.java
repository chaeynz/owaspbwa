package org.owasp.esapi.swingset.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;

/**
 * Handles the whole Command Injection lab.
 * 
 * @author Christopher Popp
 */
public class CommandInjectionLab extends CommandInjectionAction {
	
	/** Custom logger of this class */
	private static final Logger logger = ESAPI.getLogger("CommandInjectionLab");
	
	/**
	 * Standard method which get called by the Controller if the Command Injection lab
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
					// TODO Use the ESAPI.executor()
					ProcessBuilder pb = new ProcessBuilder("cmd", "/C", batchPath, username, email,
							password, password2);
					Process process = pb.start();
					
					// The follow code is necessary because you have to read
					// the cmd.exe output and error stream in parallel
					// otherwise you could run into a deadlock because the
					// program stops while one of its streams is full and waits
					// until someone clears that stream.
					// You do not need this code to solve this lab because
					// the ESAPI.executor() will do this for you.
					// This code is a shortened copy from the ESAPI
					// DefaultExecutor
					final StringBuilder outputBuffer = new StringBuilder();
		            final StringBuilder errorsBuffer = new StringBuilder();
		            try {
		                ReadThread errorReader = new ReadThread(process.getErrorStream(), errorsBuffer);
		                errorReader.start();
		                
		            	readStream( process.getInputStream(), outputBuffer );
		            	
	            		errorReader.join();
	            		
	            		if (errorReader.exception != null) {
	            			throw errorReader.exception;
	            		}
	            		
		            	process.waitFor();
		            } catch (Throwable e) {
		            	process.destroy();
		            	logger.error(Logger.EVENT_FAILURE, "Exception thrown during execution of system command: " + e.getMessage());
		            }
					
		            String output = outputBuffer.toString();
		            String errors = errorsBuffer.toString();
					
		            if (errors != null && errors.length() > 0) {
		            	// Error during command execution
						request.setAttribute("batchResult", "Batch execution failed!");
						logger.error(Logger.EVENT_FAILURE, errors);
					} else {
						request.setAttribute("batchResult", output);
					}
				} catch (IOException ioe) {
					logger.error(Logger.EVENT_FAILURE, ioe.getMessage());
				}
			} else {
				request.setAttribute("error", "You have to fill in the whole form!");
			}
		}
	}
	
	/**
	 * Copied from org.owasp.esapi.reference.DefaultExecutor
	 */
	private static void readStream( InputStream is, StringBuilder sb ) throws IOException {
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
	        sb.append(line).append('\n');
	    }
    }
    
	/**
	 * Copied from org.owasp.esapi.reference.DefaultExecutor
	 */
    private static class ReadThread extends Thread {
    	volatile IOException exception;
    	private final InputStream stream;
    	private final StringBuilder buffer;

    	ReadThread(InputStream stream, StringBuilder buffer) {
    		this.stream = stream;
    		this.buffer = buffer;
    	}

    	@Override
		public void run() {
    		try {
    			readStream(stream, buffer);
    		} catch (IOException e) {
    			exception = e;
    		}
    	}
    }
}
