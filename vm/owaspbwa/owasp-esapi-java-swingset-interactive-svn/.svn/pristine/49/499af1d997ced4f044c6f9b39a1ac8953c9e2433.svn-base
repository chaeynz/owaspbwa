package org.owasp.esapi.swingset.actions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.EncryptionException;

/**
 * Handles the whole SQL Injection lab.
 * 
 * @author Christopher Popp
 */
public class SqlInjectionLab extends SqlInjectionAction {
	
	/** Custom logger of this class */
	private static final Logger logger = ESAPI.getLogger("SqlInjectionLab");
	
	
	/**
	 * Standard method which get called by the Controller if the SQL Injection lab
	 * got requested.
	 * 
	 * @param request - an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response - an HttpServletResponse object that contains the response the servlet sends to the client 
	 */
	public static void invoke( HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String user = (String)session.getAttribute("user"); 
		Connection con = null;
		
		try {
			if (user == null) { // Login
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				if (username != null || password != null) {
					con = getConnection();
					// TODO Secure this statement
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT u_id FROM users WHERE name = '"
							+ username + "' AND password = '" + ESAPI.encryptor().hash(password, salt) +"'");
					if (rs.next()) { // Login correct
						session.setAttribute("labUser", username);
						session.setAttribute("labUserId", rs.getInt("u_id"));
					} else { // Login failed
						request.setAttribute("error", "Invalid username or password!");
					}
				}
			} if (request.getParameter("logout") != null) { // Logout
				session.removeAttribute("labUser");
				session.removeAttribute("labUserId");
			} else { // Display messages
				String filter = request.getParameter("filter");
				if (filter == null) {
					filter = "";
				}
				con = getConnection();
				// TODO Secure this statement
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT sender, text FROM messages " +
						"WHERE u_id = " + session.getAttribute("labUserId") + " AND sender LIKE '%" + filter + "%'");
				Map<String, String> messages = new TreeMap<String,String>();
				while (rs.next()) {
					messages.put(rs.getString("sender"), rs.getString("text"));
				}
				request.setAttribute("messages", messages);
			}
		} catch (SQLException e) {
			logger.error(Logger.EVENT_FAILURE, e.getMessage());
		} catch (EncryptionException e) {
			// Failed to has password
			logger.error(Logger.EVENT_FAILURE, "Failed to hash password!", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error(Logger.EVENT_FAILURE, e.getMessage());
			}
		} 
	}
}
