package org.owasp.esapi.swingset.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.EncryptionException;

/**
 * Serves general task for the SQL Injection lab and solution like
 * providing a connection and reseting the database. Hides the 
 * database specific information.
 * 
 * @author Christopher Popp
 */
public abstract class SqlInjectionAction {

	/** Path to the HSQL database file */
	private static final String dbPath = setDbPath();
	
	protected static final String salt = "SqlInjectionAction";
		
	/** Load the database driver when this class gets loaded */
	static {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriveSr");
		} catch (ClassNotFoundException e) {
			ESAPI.getLogger("SqlInjectionAction").error(Logger.EVENT_FAILURE, e.getMessage());
		}
	}
	
	/**
	 * Resets the database to the default required by the SQL Injection lab and solution.
	 * 
	 * @throws SQLException Could not open/close the connection or could not execute
	 * 						a specific command
	 * @throws EncryptionException 
	 */
	protected static void resetDb() throws SQLException, EncryptionException {
		Connection con = null;
		try {
			con = getConnection();
			// Create tables if they do not exist and clear all entries
			con.prepareStatement("CREATE TABLE IF NOT EXISTS users " +
					"(u_id int, " +
					"name varchar(20), " +
					"password varchar(200), " +
					"PRIMARY KEY (u_id))").execute();
			con.prepareStatement("CREATE TABLE IF NOT EXISTS messages " +
					"(m_id int, " +
					"u_id int, " +
					"sender varchar(20), " +
					"text varchar(200), " +
					"PRIMARY KEY (m_id), " +
					"CONSTRAINT fk_user_message FOREIGN KEY (u_id) REFERENCES users (u_id))").execute();
			con.prepareStatement("DELETE FROM messages").execute();
			con.prepareStatement("DELETE FROM users").execute();
						
			
			// Create Users
			PreparedStatement pst = con.prepareStatement("insert into users values(?,?,?)");
			
			pst.setInt(1, 1);
			pst.setString(2, "James");
		    pst.setString(3, ESAPI.encryptor().hash("1234", salt));
		    pst.addBatch();
		    
		    pst.setInt(1, 2);
		    pst.setString(2, "Jim");
		    pst.setString(3, ESAPI.encryptor().hash("Qwerty123!", salt));
		    pst.addBatch();
		    
		    pst.setInt(1, 3);
		    pst.setString(2, "John");
		    pst.setString(3, ESAPI.encryptor().hash("Password1", salt));
		    pst.addBatch();
		    
		    pst.executeBatch();
			
		    
		    // Create Messages
		    pst = con.prepareStatement("insert into messages values(?,?,?,?)");
		    
		    pst.setInt(1, 1);
		    pst.setInt(2, 1);
			pst.setString(3, "Tim");
		    pst.setString(4, "Hello James how are you?");
		    pst.addBatch();
		    
		    pst.setInt(1, 2);
		    pst.setInt(2, 2);
			pst.setString(3, "Sandy");
		    pst.setString(4, "Hi Jim please don't tell anybody... ");
		    pst.addBatch();
		    
		    pst.setInt(1, 3);
		    pst.setInt(2, 3);
			pst.setString(3, "Tony");
		    pst.setString(4, "What's up John? Are you going to be there tomorrow?");
		    pst.addBatch();
		    
		    pst.executeBatch();
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}
	
	/**
	 * Opens a connection to the database. The caller have to close the
	 * connection on his own!
	 * 
	 * @return Connection to the database
	 * @throws SQLException - could not open connection
	 */
	protected static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:"+dbPath,"SA","");
	}
		
	/**
	 * Uses \.esapi\db\webmail in the users home directory to store/load
	 * the database file. Normally SwingSets ESAPI configurations are in that
	 * directory too. ESAPI do not provide a method to get the ESAPI configuration
	 * directory.
	 * 
	 * @return Path to the database file
	 */
	private static String setDbPath() {
		String path = System.getProperty("user.home", "");
		if (path != null) {
			path += "\\";
		}
		path += ".esapi\\swingset\\SqlInjection\\webmail";
		return  path;
	}
}
