package org.owasp.esapi.swingset.actions;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.errors.EnterpriseSecurityException;

public class ObjectReferenceInsecure {
	
	private static boolean initialized = false;
	private static HashMap<String,String> map;
	private static HashMap<String, String> userList;
	
	public static void invoke( HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException, IOException {
		if( !initialized) {
			map = new HashMap<String,String>(11);
			map.put("user1", "My password is abc123.  It's really secure, but it controls EVERYTHING, so don't tell anyone - please.");
			map.put("user2", "So long, and thanks for all the fish<br />So sad that it should come to this<br />We tried to warn you all, but, oh, dear<br />You may not share out intellect<br />Which might explain your disrespect<br />For all the natural wonders that grow around you<br />So long, so long, and thanks for all the fish! The world's about to be destroyed<br />There's no point getting all annoyed<br />Lie back and let the planet dissolve around you<br />Despite those nets of tuna fleets<br />We thought that most of you were sweet<br />Especially tiny tots and your pregnant women<br />So long, so long, so long, so long, so long! So long, so long, so long, so long, so long! So long, so long, and thanks for all the fish!<br /> If I had just one last wish<br />I would like a tasty fish!<br />If we could just change one thing<br />We would all have learnt to sing!<br />Come one and all<br />Man and mammal<br />Side by side<br />In life's great gene pool!<br />So long, so long, so long, so long, so long<br />So long, so long, so long, so long<br />So long, so long and thanks for all the fish! ");
			map.put("user3", "Take the blue pill... trust me!");
			map.put("user4", "The Matrix has you.");
			map.put("user5", "We're two wild and crazy guys!");
			map.put("user6", "Tron, is that you?");
			map.put("user7", "Oh man...when you're on the other side of the screen...it all looks so easy... ");
			map.put("user8", "I should never have written all of those tank programs!");
			map.put("user9", "Do you want to know what <i>it</i> is?");
			map.put("user10", "The Matrix is everywhere. It is all around us. Even now, in this very room. You can see it when you look out your window or when you turn on your television. You can feel it when you go to work... when you go to church... when you pay your taxes. It is the world that has been pulled over your eyes to blind you from the truth.");
			map.put("user11", "The Matrix is a system, Neo. That system is our enemy. But when you're inside, you look around, what do you see? Businessmen, teachers, lawyers, carpenters. The very minds of the people we are trying to save. But until we do, these people are still a part of that system and that makes them our enemy. You have to understand, most of these people are not ready to be unplugged. And many of them are so inert, so hopelessly dependent on the system, that they will fight to protect it.");
			
			// Direct reference -> user
			userList = new HashMap<String, String>();
			userList.put("user1", "user1");
			userList.put("user2", "user2");
			userList.put("user3", "user3");
			userList.put("user4", "user4");
			userList.put("user5", "user5");
			
			initialized = true;
		}
		
		final HttpSession session = request.getSession();
		session.setAttribute("userList", userList);
		session.setAttribute("map", map);
		
		if( request.getParameter("user") != null ) {
			request.setAttribute("attempt", true);
			
			String profile = map.get(request.getParameter("user"));
			if( profile == null ) {
				profile = "That user does not exist!";
			}
			
			request.setAttribute("profile", profile);
		} else {
			request.setAttribute("attempt", false);
		}
	}
}
