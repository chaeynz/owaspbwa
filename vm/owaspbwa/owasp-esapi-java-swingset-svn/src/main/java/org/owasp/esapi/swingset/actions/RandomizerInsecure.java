package org.owasp.esapi.swingset.actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.errors.EnterpriseSecurityException;

/**
 * 
 * @author Pawan Singh
 *
 */
public class RandomizerInsecure {
	
	static Random random = new Random();

	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {

	}
	
	public static void getRandomBoolean( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		
		
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			boolean randomBoolean = random.nextBoolean();
			out.print(randomBoolean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getRandomInteger ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String randomInteger = "";
				
		randomInteger = Integer.toString(random.nextInt());
		try {
			response.setContentType("text/html");
			PrintWriter	out = response.getWriter();
			out.print(randomInteger);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void getRandomLong ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		Long randomLong = random.nextLong();
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(randomLong);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void getRandomReal ( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String randomReal = "";
		
		randomReal = Float.toString(random.nextFloat());	
		
		try {
			response.setContentType("text/html");
			PrintWriter	out = response.getWriter();
			out.print(randomReal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}