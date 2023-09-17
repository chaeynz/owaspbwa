package org.owasp.esapi.swingset.login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;

/** * Servlet implementation class LoginServlet */ 
public class LoginServletSolution extends HttpServlet { 
	
	private static final long serialVersionUID = 1L;

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
				if (user == null){ 				
					user = new UserBean();
					// CP_2012-06-23 Allow login only through POST 
					if (request.getMethod().equals("POST")) {
						user.setUserName(request.getParameter("un")); 
						user.setPassword(request.getParameter("pw"));
					}
				}
								
				//user = UserDAO.login(user); 
				//if (user.isValid()) {
				RequestDispatcher dispatcher;
				
								
				// login user and check if valid
				if ("test".equals(user.getUsername()) && "test".equals(user.getPassword())) {					 					 					
					session = ESAPI.httpUtilities().changeSessionIdentifier();
					session.setAttribute("currentSessionUser", user);					
					dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login/userLoggedSolution.jsp");
					dispatcher.forward(request, response);
					//response.sendRedirect("userLogged.jsp"); //logged-in page 
				} else {
					dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login/invalidLoggedSolution.jsp");
					dispatcher.forward(request, response);
					//response.sendRedirect("invalidLogin.jsp"); //error page
				}
			} catch (Throwable theException) { 
				System.out.println(theException); 
			} 
	} 
} 
		