package org.owasp.esapi.swingset.login;

import java.io.IOException; 

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession; 
import org.owasp.esapi.*;

import org.owasp.esapi.swingset.login.UserBean;

/** * Servlet implementation class LoginServlet */ 
public class LoginServletSolution extends HttpServlet { 
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
		