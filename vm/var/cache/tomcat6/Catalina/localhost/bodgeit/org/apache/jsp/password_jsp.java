package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.sql.*;

public final class password_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\n');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/header.jsp", out, false);
      out.write('\n');
      out.write('\n');

String username = (String) session.getAttribute("username");
String usertype = (String) session.getAttribute("usertype");

String password1 = (String) request.getParameter("password1");
String password2 = (String) request.getParameter("password2");
String okresult = null;
String failresult = null;

if (password1 != null && password1.length() > 0) {
	if ( ! password1.equals(password2)) {
		failresult = "The passwords you have supplied are different.";
	}  else if (password1 == null || password1.length() < 5) {
		failresult = "You must supply a password of at least 5 characters.";
	} else {
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		try {
			stmt.executeQuery("UPDATE Users set password= '" + password1 + "' where name = '" + username + "'");
			
			okresult = "Your password has been changed";

			if (request.getMethod().equals("GET")) {
				conn.createStatement().execute("UPDATE Score SET status = 1 WHERE task = 'PASSWD_GET'");
			}

		} catch (Exception e) {
			failresult = "System error.";
		} finally {
			stmt.close();
		}

	}
}


      out.write("\n");
      out.write("<h3>Your profile</h3>\n");

if (failresult != null) {
	out.println("<p style=\"color:red\">" + failresult + "</p><br/>");
}
if (okresult != null) {
	out.println("<p style=\"color:green\">" + okresult + "</p><br/>");
}

      out.write("\n");
      out.write("Change your password: <br/><br/>\n");
      out.write("<form method=\"POST\">\n");
      out.write("\t<center>\n");
      out.write("\t<table>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td>Name</td>\n");
      out.write("\t\t<td>");
      out.print(username);
      out.write("</td>\n");
      out.write("\t</tr>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td>New Password:</td>\n");
      out.write("\t\t<td><input id=\"password1\" name=\"password1\" type=\"password\"/></td>\n");
      out.write("\t</tr>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td>Repeat Password:</td>\n");
      out.write("\t\t<td><input id=\"password2\" name=\"password2\" type=\"password\"/></td>\n");
      out.write("\t</tr>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td></td>\n");
      out.write("\t\t<td><input id=\"submit\" type=\"submit\" value=\"Submit\"/></td>\n");
      out.write("\t</tr>\n");
      out.write("\t</table>\n");
      out.write("\t</center>\n");
      out.write("</form>\n");
      out.write("\n");




      out.write('\n');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/footer.jsp", out, false);
      out.write('\n');
      out.write('\n');
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
