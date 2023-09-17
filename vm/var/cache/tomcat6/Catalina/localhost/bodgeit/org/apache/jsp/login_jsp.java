package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.sql.*;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


    private Connection conn = null;

    public void jspInit() {
        try {
            // Get hold of the JDBC driver
            Class.forName("org.hsqldb.jdbcDriver" );
            // Establish a connection to an in memory db
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:SQL", "sa", "");
        } catch (SQLException e) {
            getServletContext().log("Db error: " + e);
        } catch (Exception e) {
            getServletContext().log("System error: " + e);
        }
    }

    public void jspDestroy() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            getServletContext().log("Db error: " + e);
        } catch (Exception e) {
            getServletContext().log("System error: " + e);
        }
    }

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/dbconnection.jspf");
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");

boolean loggedIn = false;
String username = (String) request.getParameter("username");
String password = (String) request.getParameter("password");
String debug = "Clear";

if (request.getMethod().equals("POST") && username != null) {
	Statement stmt = conn.createStatement();
	ResultSet rs = null;
	try {
		rs = stmt.executeQuery("SELECT * FROM Users WHERE (name = '" + username + "' AND password = '" + password + "')");
		if (rs.next()) {
			loggedIn = true;
			debug="Logged in";
			// We must have been given the right credentials, right? ;)
			// Put credentials in the session
			String userid = "" + rs.getInt("userid");
			session.setAttribute("username", rs.getString("name"));
			session.setAttribute("userid", userid);
			session.setAttribute("usertype", rs.getString("type"));

			// Update the scores
			if (userid.equals("3")) {
				stmt.execute("UPDATE Score SET status = 1 WHERE task = 'LOGIN_TEST'");
			} else if (userid.equals("1")) {
				stmt.execute("UPDATE Score SET status = 1 WHERE task = 'LOGIN_USER1'");
			} else if (userid.equals("2")) {
				stmt.execute("UPDATE Score SET status = 1 WHERE task = 'LOGIN_ADMIN'");
			}

			Cookie[] cookies = request.getCookies();
			String basketId = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("b_id") && cookie.getValue().length() > 0) {
						basketId = cookie.getValue();
						break;
					}
				}
			}
			if (basketId != null) {
				debug += " basketid = " + basketId;
				int cBasketId = rs.getInt("currentbasketid");
				if (cBasketId > 0) {
					// Merge baskets
					debug += " currentbasketid = " + cBasketId;
					stmt.execute("UPDATE BasketContents SET basketid = " + cBasketId + " WHERE basketid = " + basketId);

				} else {
					stmt.execute("UPDATE Users SET currentbasketid = " + basketId + " WHERE userid = " + userid);
				}
				response.addCookie(new Cookie("b_id", ""));
			}

		}
	} catch (Exception e) {
		if ("true".equals(request.getParameter("debug"))) {
			stmt.execute("UPDATE Score SET status = 1 WHERE task = 'HIDDEN_DEBUG'");
			out.println("DEBUG System error: " + e + "<br/><br/>");
		} else {
			out.println("System error.");
		}
	} finally {
		if (rs != null) {
			rs.close();
		}
		stmt.close();
	}
}

      out.write('\r');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/header.jsp", out, false);
      out.write('\r');
      out.write('\n');

if ("true".equals(request.getParameter("debug"))) {
	out.println("DEBUG: " + debug + "<br/><br/>");
}
// Display the form
if (request.getMethod().equals("POST") && username != null) {
	if (loggedIn) {
		if (username.replaceAll("\\s", "").toLowerCase().indexOf("<script>alert(\"xss\")</script>") >= 0) {
			Statement stmt = conn.createStatement();
			try {
				stmt.execute("UPDATE Score SET status = 1 WHERE task = 'XSS_LOGIN'");
			} finally {
				stmt.close();
			}
		}
		out.println("<br/>You have logged in successfully: " + username);
		return;

	} else {
		out.println("<p style=\"color:red\">You supplied an invalid name or password.</p>");

	}
}

      out.write("\r\n");
      out.write("<h3>Login</h3>\r\n");
      out.write("Please enter your credentials: <br/><br/>\r\n");
      out.write("<form method=\"POST\">\r\n");
      out.write("\t<center>\r\n");
      out.write("\t<table>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>Username:</td>\r\n");
      out.write("\t\t<td><input id=\"username\" name=\"username\"/></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>Password:</td>\r\n");
      out.write("\t\t<td><input id=\"password\" name=\"password\" type=\"password\"/></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td></td>\r\n");
      out.write("\t\t<td><input id=\"submit\" type=\"submit\" value=\"Login\"/></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</center>\r\n");
      out.write("</form>\r\n");
      out.write("If you dont have an account with us then please <a href=\"register.jsp\">Register</a> now for a free account.\r\n");
      out.write("<br/><br/>\r\n");
      out.write("\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/footer.jsp", out, false);
      out.write("\r\n");
      out.write("\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
