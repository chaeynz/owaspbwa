package org.owasp.esapi.swingset.login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** * Servlet implementation class LoginServlet */ 
public class LoginServletLab extends HttpServlet {
	
	private static final long serialVersionUID = -8219343162875683959L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		// CP_2012-06-23 Enabled request through GET to be able to call the
		// servlet without user credentials (Step 2 in the lab)
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, java.io.IOException { 
		try { 
			UserBean user;
			HttpSession session = request.getSession(true);
			
			user = (UserBean)session.getAttribute("currentSessionUser");
			if (user == null) {
				user = new UserBean();
				// CP_2012-06-23 Allow login only through POST 
				if (request.getMethod().equals("POST")) {
					user.setUserName(request.getParameter("un")); 
					user.setPassword(request.getParameter("pw"));
				}
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
		