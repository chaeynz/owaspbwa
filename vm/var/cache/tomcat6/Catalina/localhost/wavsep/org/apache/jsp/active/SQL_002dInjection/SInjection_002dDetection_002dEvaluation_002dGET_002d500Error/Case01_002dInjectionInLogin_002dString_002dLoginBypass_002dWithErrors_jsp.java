package org.apache.jsp.active.SQL_002dInjection.SInjection_002dDetection_002dEvaluation_002dGET_002d500Error;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import com.sectooladdict.database.ConnectionPoolManager;

public final class Case01_002dInjectionInLogin_002dString_002dLoginBypass_002dWithErrors_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

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
      response.setContentType("text/html; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n");
      out.write("<title>Case 1 - Injection into string values in a login page with erroneous responses</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");

if (request.getParameter("username") == null
	&& request.getParameter("password") == null	) {

      out.write("\n");
      out.write("\tLogin Page:<br><br>\n");
      out.write("\t<form name=\"frmInput\" id=\"frmInput\" action=\"Case01-InjectionInLogin-String-LoginBypass-WithErrors.jsp\" method=\"POST\">\n");
      out.write("\t\t<input type=\"text\" name=\"username\" id=\"username\"><br>\n");
      out.write("\t\t<input type=\"password\" name=\"password\" id=\"password\"><br>\n");
      out.write("\t\t<input type=submit value=\"submit\">\n");
      out.write("\t</form>\n");

} 
else {
	Connection conn = null;
    try {
  	    String username = request.getParameter("username");
  	    String password = request.getParameter("password");

  	    conn = ConnectionPoolManager.getConnection();
     
        System.out.print("Connection Opened Successfully\n");

 	    String SqlString = 
            "SELECT username, password " +
 	        "FROM users " +
 	        "WHERE username='" + username + "'" +
 	        " AND password='" + password + "'";
 		Statement stmt = conn.createStatement();
 		ResultSet rs = stmt.executeQuery(SqlString);
 		 
 		if(rs.next()) {
 			out.println("hello " + rs.getString(1));
 	    } else {
 	 		out.println("login failed");
 	 	}
 	 	
	  	out.flush();
	  	
	  	if(conn != null) {
        	ConnectionPoolManager.closeConnection(conn);
        }
	  	
    } catch (Exception e) {
        response.sendError(500,"Exception details: " + e);
        
        if(!(e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException)) {
  	        System.out.println("Exception details: " + e);
        } 

		if(conn != null) {
        	ConnectionPoolManager.closeConnection(conn);
        }
    }
} //end of if/else block

      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>");
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
