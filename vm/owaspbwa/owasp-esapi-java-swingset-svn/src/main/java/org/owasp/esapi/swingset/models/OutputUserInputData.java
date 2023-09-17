package org.owasp.esapi.swingset.models;

import java.util.ArrayList;

import org.owasp.esapi.ESAPI;

/**
 * @author Craig Younkins
 *
 */

public class OutputUserInputData {
	public static ArrayList<String> examples = new ArrayList<String>();
	
	static {
		examples.add("<script>alert(document.cookie)</script>");
		examples.add("' onblur='alert(document.cookie)");
	}
}
