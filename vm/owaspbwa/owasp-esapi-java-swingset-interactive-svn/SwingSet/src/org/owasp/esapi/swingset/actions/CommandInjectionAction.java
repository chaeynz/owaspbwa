package org.owasp.esapi.swingset.actions;

/**
 * Serves general task for the Command Injection lab and solution.
 * Actually only defines the path to the batch file.
 * 
 * @author Christopher Popp
 */
public abstract class CommandInjectionAction {
	
	/** Path to the batch file for the Command Injection lab/solution */
	protected static final String batchPath = System.getProperty("user.home", "")
			+ "\\.esapi\\swingset\\CommandInjection\\newUser.bat";
	
}
