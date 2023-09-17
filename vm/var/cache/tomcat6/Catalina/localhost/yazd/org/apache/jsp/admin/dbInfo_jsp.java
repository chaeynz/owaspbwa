package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import com.Yasna.forum.*;
import com.Yasna.forum.database.*;

public final class dbInfo_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: dbInfo.jsp,v $
 *	$Revision: 1.3 $
 *	$Date: 2000/12/18 02:06:21 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      com.Yasna.forum.util.admin.AdminBean adminBean = null;
      synchronized (session) {
        adminBean = (com.Yasna.forum.util.admin.AdminBean) _jspx_page_context.getAttribute("adminBean", PageContext.SESSION_SCOPE);
        if (adminBean == null){
          adminBean = new com.Yasna.forum.util.admin.AdminBean();
          _jspx_page_context.setAttribute("adminBean", adminBean, PageContext.SESSION_SCOPE);
        }
      }
      out.write("\r\n");
      out.write("\r\n");
	////////////////////////////////
	// Yazd authorization check
	
	// check the bean for the existence of an authorization token.
	// Its existence proves the user is valid. If it's not found, redirect
	// to the login page
	Authorization authToken = adminBean.getAuthToken();
	if( authToken == null ) {
		response.sendRedirect( "login.jsp" );
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>DbConnector.jsp</title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/shadowBack.gif\" bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<table class=\"pageHeaderBg\" cellpadding=\"1\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("<td><table class=\"pageHeaderFg\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("<td>\r\n");
      out.write("\t<span class=\"pageHeaderText\">\r\n");
      out.write("\tDatabase Information\r\n");
      out.write("\t</span>\r\n");
      out.write("</td>\r\n");
      out.write("</table></td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
 
	boolean error = false;
	DatabaseMetaData metaData = null;
	Connection con = null;
	if (!error) {
		try {
			con = DbConnectionManager.getConnection();
			metaData = con.getMetaData();
		}
		catch( Exception e ) { 
			e.printStackTrace();
		}
	}

      out.write("\r\n");
      out.write("\r\n");

	if (!error) {
		try {

      out.write("\r\n");
      out.write("\r\n");
      out.write("<b>Database Information</b>\r\n");
      out.write("\r\n");
      out.write("<ul>\r\n");
      out.write("\t\r\n");
      out.write("\t<table cellpadding=\"2\" cellspacing=\"2\" border=\"0\">\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td colspan=\"2\"><font size=\"-1\"><b>Database Properties</b></font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Name:</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( metaData.getDatabaseProductName() );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Version:</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( metaData.getDatabaseProductVersion() );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td colspan=\"2\"><br><b><font size=\"-1\">JDBC Driver Properties</font></b></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Driver:</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( metaData.getDriverName() );
      out.write(", version ");
      out.print( metaData.getDriverVersion() );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Connection URL:</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( metaData.getURL() );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Connection username:</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( metaData.getUserName() );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td colspan=\"2\"><br><b><font size=\"-1\">Database Capabilities</font></b></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Supports transactions?</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( (metaData.supportsTransactions())?"Yes":"No" );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Supports multiple connections <br>open at once?</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( (metaData.supportsMultipleTransactions())?"Yes":"No" );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td bgcolor=\"#eeeeee\"><font size=\"-1\">Is in read-only mode?</font></td>\r\n");
      out.write("\t<td><font size=\"-1\">");
      out.print( (metaData.isReadOnly())?"Yes":"No" );
      out.write("</font></td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
			con.close();
		}
		catch( Exception e ) {}
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
