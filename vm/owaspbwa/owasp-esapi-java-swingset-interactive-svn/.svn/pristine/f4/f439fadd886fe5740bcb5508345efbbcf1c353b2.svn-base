package org.owasp.esapi.swingset.login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AuthenticationException;

/** * Servlet implementation class LoginServlet */ 
public class LoginServletLab extends HttpServlet { 
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, java.io.IOException { 
			try { 
				UserBean user;
				HttpSession session = request.getSession(true);
				
				user = (UserBean)session.getAttribute("currentSessionUser");
				if (user == null){
					user = new UserBean();
					user.setUserName(request.getParameter("un")); 
					user.setPassword(request.getParameter("pw"));
				}
								
				RequestDispatcher dispatcher;
								
				// login user and check if valid
				if ("test".equals(user.getUsername()) && "test".equals(user.getPassword())) {					 					 					
					// TODO: Change Session Identifier on successful logon
					session.setAttribute("currentSessionUser", user);
					dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login/userLoggedLab.jsp");
					dispatcher.forward(request, response);
				} else {
					dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login/invalidLoggedLab.jsp");
					dispatcher.forward(request, response);
				}
			} catch (Throwable theException) { 
				System.out.println(theException); 
			} 
	} 
} 
		