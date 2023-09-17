package org.owasp.esapi.swingset.actions;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EnterpriseSecurityException;


/**
 * 
 * @author Pawan Singh
 *
 */
public class RandomizerLab {
	
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {

	}
	
	public static void getRandomBoolean( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			boolean randomBoolean = false; // TODO 1: Generate random boolean
			out.print(randomBoolean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getRandomFileName( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String randomFileName = "RandomFileName" + request.getParameter("fileExtension"); // TODO 2: Generate random filename
			out.print(randomFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getRandomInteger ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		int min=0, max=0;
		String randomInteger = "";
				
		
		try {
			min = Integer.parseInt(request.getParameter("min"));
			max = Integer.parseInt(request.getParameter("max"));
			randomInteger = Integer.toString(0); // TODO 3: Generate random int in the range min - max
			
		} catch (Exception e) {
				randomInteger = "Please enter proper min, max seed";
		}
			
		try {
			response.setContentType("text/html");
			PrintWriter	out = response.getWriter();
			out.print(randomInteger);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void getRandomLong ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		Long randomLong = (long)0; // TODO 4: Generate random long

		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(randomLong);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getRandomReal ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		float minFloat = 0, maxFloat = 0; String randomReal = "";

		try {
			minFloat = Float.parseFloat(request.getParameter("minFloat"));
			maxFloat = Float.parseFloat(request.getParameter("maxFloat"));
			randomReal = Float.toString((float)1); // TODO 5: Generate random real in the range minFloat - maxFloat
				
		} catch (Exception e) {
			randomReal = "Please enter valid (float) min, max values";
		}
			
		try {
			response.setContentType("text/html");
			PrintWriter	out = response.getWriter();
			out.print(randomReal);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void getRandomString ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		int length=0;
		String randomString = "";
		
		try {
			char[] charSet = request.getParameter("charSet").toCharArray();
			length = Integer.parseInt(request.getParameter("length"));
			randomString = "random string"; // TODO 6: Generate random String with the specified length and charset
		} catch (Exception e) {
			randomString = "Please enter Length and Char Set values";
		}
		try {
			response.setContentType("text/html");
			PrintWriter	out = response.getWriter();
			out.print(randomString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}