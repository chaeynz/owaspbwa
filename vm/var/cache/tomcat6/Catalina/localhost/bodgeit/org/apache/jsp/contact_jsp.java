package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.sql.*;

public final class contact_jsp extends org.apache.jasper.runtime.HttpJspBase
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
String anticsrf = null;

String comments = (String) request.getParameter("comments");

if (request.getMethod().equals("POST") && comments != null) {

	anticsrf = request.getParameter("anticsrf");
	if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {

		// Strip script tags, because that will make everything alright...
		comments = comments.replace("<script>", "");
		comments = comments.replace("</script>", "");
		// And double quotes, just to make sure
		comments = comments.replace("\"", "");

		PreparedStatement stmt = conn.prepareStatement("INSERT INTO Comments (name, comment) VALUES (?, ?)");
		ResultSet rs = null;
		try {
			stmt.setString(1, username);
			stmt.setString(2, comments);
			stmt.execute();

			if (username == null) {
				username = "Guest user";
			}

			out.println("<br/><p style=\"color:green\">Thank you for your feedback:</p><br/>");
			out.println("<br/><center><table border=\"1\" width=\"80%\" class=\"border\">");
			out.println("<tr><td>" + comments + "</td></tr>");
			out.println("</table></center><br/>");

			return;

		} catch (SQLException e) {
			out.println("System error.<br/><br/>" + e);
		} catch (Exception e) {
			out.println("System error.<br/><br/>" + e);
		} finally {
			stmt.close();
		}
	} else {
		out.println("<br/><p style=\"color:red\">There was a problem with your feedback, please try again.</p><br/>");
	}
}
// Generate and store a new token
anticsrf = "" + Math.random();
request.getSession().setAttribute("anticsrf", anticsrf);


if (usertype != null && usertype.endsWith("ADMIN")) {
	// Display all of the messages
	ResultSet rs = null;
	PreparedStatement  stmt = conn.prepareStatement("SELECT * FROM Comments");
	try {
		rs = stmt.executeQuery();
		out.println("<br/><center><table border=\"1\" width=\"80%\" class=\"border\">");
		out.println("<tr><th>User</th><th>Comment</th></tr>");
		while (rs.next()) {
			out.println("<tr>");
			out.println("<td>" + rs.getString("name") + "</td><td>" + rs.getString("comment") + "</td>");
			out.println("</tr>");
		}
		out.println("</table></center><br/>");
	} catch (Exception e) {
		if ("true".equals(request.getParameter("debug"))) {
			conn.createStatement().execute("UPDATE Score SET status = 1 WHERE task = 'HIDDEN_DEBUG'");
			out.println("DEBUG System error: " + e + "<br/><br/>");
		} else {
			out.println("System error.");
		}
	} finally {
		stmt.close();
	}

} else {
	// Display the message form

      out.write("\n");
      out.write("<h3>Contact Us</h3>\n");
      out.write("Please send us your feedback: <br/><br/>\n");
      out.write("<form method=\"POST\">\n");
      out.write("\t<input type=\"hidden\" id=\"user\" name=\"");
      out.print(username);
      out.write("\" value=\"\"/>\n");
      out.write("\t<input type=\"hidden\" id=\"anticsrf\" name=\"anticsrf\" value=\"");
      out.print(anticsrf);
      out.write("\"></input>\n");
      out.write("\t<center>\n");
      out.write("\t<table>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td><textarea id=\"comments\" name=\"comments\" cols=80 rows=8></textarea></td>\n");
      out.write("\t</tr>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td><input id=\"submit\" type=\"submit\" value=\"Submit\"></input></td>\n");
      out.write("\t</tr>\n");
      out.write("\t</table>\n");
      out.write("\t</center>\n");
      out.write("</form>\n");
      out.write("\n");

}


      out.write('\n');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/footer.jsp", out, false);
      out.write('\n');
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
