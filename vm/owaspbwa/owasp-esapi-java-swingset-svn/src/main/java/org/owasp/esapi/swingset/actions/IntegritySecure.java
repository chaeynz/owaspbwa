package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encryptor;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class IntegritySecure {
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		String timer = "15"; //how long seal will be good for in seconds
		try{
			Encryptor instance = ESAPI.encryptor(); 
			String plaintext = ""; //plaintext before sealing or after unsealing
			String toVerify = ""; //possible seal to verify
			String seal = ""; //sealed text
			
			//Get the length of time in seconds before seal expires
			if( request.getParameter("timer") != null )
				timer = request.getParameter("timer");
			
			//if we want to unseal something
			if( request.getParameter("unseal") != null && request.getParameter("sealed") != null){
				plaintext = instance.unseal(request.getParameter("sealed")); 
				toVerify = request.getParameter("sealed"); 
				seal = request.getParameter("sealed");
			}
			
			//check that we're not sealing null or "" ("" can be sealed, we just do not do it here)
			if(request.getParameter("unsealed") != null && !request.getParameter("unsealed").equals(""))
				plaintext = request.getParameter("unsealed");
        
			//check that we're sealing, not unsealing, and what we're sealing is not ""(again, we could seal "" - we just don't here)
			if( !plaintext.equals("") && request.getParameter("unseal") == null){
				seal = instance.seal( plaintext, instance.getTimeStamp() + 1000 * Integer.parseInt(timer) );
				toVerify = seal;
			}
			
			//check if we are going to verify a seal
			if( request.getParameter("sealToVerify")  != null)
				toVerify = request.getParameter("sealToVerify");
			boolean verified = instance.verifySeal( toVerify );
			
			//send back data
			request.setAttribute("sealed", seal);
			request.setAttribute("verified", verified);
			request.setAttribute("unsealed", plaintext);
			request.setAttribute("sealToVerify", toVerify);
		
		}catch(Exception e){
			System.out.println(e);
			timer = "15";
		}
		request.setAttribute("timer", timer);
	}
}
