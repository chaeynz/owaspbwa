package org.owasp.esapi.swingset.actions;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.reference.RandomAccessReferenceMap;
import org.owasp.esapi.errors.EnterpriseSecurityException;

public class ObjectReferenceSolution {
	@SuppressWarnings("unchecked")
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
		try{
			//if user selected an in indirect reference to display, send back the direct reference
			if(request.getParameter("showItem") == null){
				final HttpSession session = request.getSession();
				
				//create a new ReferenceMap and store all direct and indirect references
				//for display to user
				String directReference0 = "Oh man...when you're on the other side of the screen...it all looks so easy... ";
				String directReference1 = "Tron, is that you?";
				String directReference2 = "The Matrix has you.";
				String directReference3 = "Take the blue pill... trust me!";
				String directReference4 = "The Matrix is everywhere. It is all around us. Even now, in this very room. You can see it when you look out your window or when you turn on your television. You can feel it when you go to work... when you go to church... when you pay your taxes. It is the world that has been pulled over your eyes to blind you from the truth.";
				String directReference5 = "The Matrix is a system, Neo. That system is our enemy. But when you're inside, you look around, what do you see? Businessmen, teachers, lawyers, carpenters. The very minds of the people we are trying to save. But until we do, these people are still a part of that system and that makes them our enemy. You have to understand, most of these people are not ready to be unplugged. And many of them are so inert, so hopelessly dependent on the system, that they will fight to protect it.";
				String directReference6 = "PC Load Letter? What does that mean?";
			
				RandomAccessReferenceMap instance = new RandomAccessReferenceMap();
			
				String ind0 = instance.addDirectReference((Object)directReference0);
				//request.setAttribute("output", ind0);
				String ind1 = instance.addDirectReference((Object)directReference1);
				String ind2 = instance.addDirectReference((Object)directReference2);
				String ind3 = instance.addDirectReference((Object)directReference3);
				String ind4 = instance.addDirectReference((Object)directReference4);
				String ind5 = instance.addDirectReference((Object)directReference5);
				String ind6 = instance.addDirectReference((Object)directReference6);
				
				// CD 2011-04-13 remove old references before creating new ones.
				session.removeAttribute((String)session.getAttribute("ind0"));
				session.removeAttribute((String)session.getAttribute("ind1"));
				session.removeAttribute((String)session.getAttribute("ind2"));
				session.removeAttribute((String)session.getAttribute("ind3"));
				session.removeAttribute((String)session.getAttribute("ind4"));
				session.removeAttribute((String)session.getAttribute("ind5"));
				session.removeAttribute((String)session.getAttribute("ind6"));
				
				session.setAttribute(ind0, directReference0);
				session.setAttribute(ind1, directReference1);
				session.setAttribute(ind2, directReference2);
				session.setAttribute(ind3, directReference3);
				session.setAttribute(ind4, directReference4);
				session.setAttribute(ind5, directReference5);
				session.setAttribute(ind6, directReference6);
				
				session.setAttribute("ind0", ind0);
				session.setAttribute("ind1", ind1);
				session.setAttribute("ind2", ind2);
				session.setAttribute("ind3", ind3);
				session.setAttribute("ind4", ind4);
				session.setAttribute("ind5", ind5);
				session.setAttribute("ind6", ind6);
			}
		
		}catch(Exception e){
			System.out.println(e);
			System.out.println("hi");
			e.printStackTrace();
		}
	}
}
