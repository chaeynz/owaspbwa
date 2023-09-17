package org.owasp.esapi.swingset.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.errors.EncryptionException;

/**
 * Handles the whole SQL Injection solution.
 * 
 * @author Christopher Popp
 */
public class SqlInjectionSolution extends SqlInjectionAction {
	
	/** Custom logger of this class */
	private static final Logger logger = ESAPI.getLogger("SqlInjectionSolution");
	
	/**
	 * Standard method which get called by the Controller if the SQL Injection solution
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
			if (request.getParameter("resetDb") != null) {
				try {
					resetDb();
					request.setAttribute("error", "Database has been resetted");
				} catch (Exception e) {
					logger.error(Logger.EVENT_FAILURE, "Database reset failed!", e);
					request.setAttribute("error", "Database reset failed!");
				}
			} else {
				if (user == null) { // Login
					String username = request.getParameter("username");
					String password = request.getParameter("password");
					if (username != null || password != null) {
						con = getConnection();
						PreparedStatement stmt = con.prepareStatement("SELECT u_id FROM users " +
								"WHERE name = ? AND password = ?");
						stmt.setString(1, username);
						stmt.setString(2, ESAPI.encryptor().hash(password, salt));
						ResultSet rs = stmt.executeQuery();
						if (rs.next()) { // Login correct
							session.setAttribute("solUser", username);
							session.setAttribute("solUserId", rs.getInt("u_id"));
						} else { // Login failed
							request.setAttribute("error", "Invalid username or password!");
						}
					}
				} if (request.getParameter("logout") != null) { // Logout
					session.removeAttribute("solUser");
					session.removeAttribute("solUserId");
				} else { // Display messages
					String filter = request.getParameter("filter");
					if (filter == null) {
						filter = "";
					}
					// Just a demo of the ESAPI encodeForSQL() method.
					// ALWAYS try to use prepared statements like above
					filter = ESAPI.encoder().encodeForSQL(new MySQLCodec(MySQLCodec.Mode.ANSI), filter);
					con = getConnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT sender, text FROM messages " +
							"WHERE u_id = " + session.getAttribute("solUserId") + " AND sender LIKE '%" + filter + "%'");
					Map<String, String> messages = new TreeMap<String,String>();
					while (rs.next()) {
						messages.put(rs.getString("sender"), rs.getString("text"));
					}
					request.setAttribute("messages", messages);
				}
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
