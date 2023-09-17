package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.appsensor.demoapp.Message;
import org.owasp.appsensor.AppSensorLogger.*;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/header_unauthenticated.jsp");
    _jspx_dependants.add("/footer.jsp");
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

String title = "AppSensor - Demo Application";
String pageHeader = "AppSensor Demo Application";





      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>");
      out.print(title);
      out.write("</title>\r\n");
      out.write("<!-- <link rel=\"stylesheet\" type=\"text/css\" href=\"style/style.css\" />-->\r\n");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<div id=\"container\">\r\n");
      out.write("\t<div align=\"right\" id=\"appsensorlogo\"><img border=1 src=\"");
      out.print( request.getContextPath() );
      out.write("/appsensor.jpg\" width=\"155\" height=\"138\" alt=\"owasp_logo\" title=\"owasp_logo\" style=\"float:right;\"></div>\r\n");
      out.write("\t<b>");
      out.print(pageHeader );
      out.write("</b><br>\r\n");
      out.write("\t<br>\r\n");
      out.write("\t<div id=\"holder\" style=\"background-color:#A9BFEA; border-width:thin; border-style:solid;\">\t\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t<div id=\"navigation\">\r\n");
      out.write("\t<b>");
 org.owasp.appsensor.AppSensorLogger.testlog();
      out.write("</b>\r\n");
      out.write("\t<br>\r\n");
      out.write("\t&nbsp;\r\n");
      out.write("\t<a href=\"login.jsp\">Login</a> |\r\n");
      out.write("\t<a href=\"home.jsp\">Home</a> |\r\n");
      out.write("\t<a href=\"updateProfile.jsp\">UpdateProfile</a> |\r\n");
      out.write("\t<a href=\"friends.jsp\">Friends</a> |\r\n");
      out.write("\t<a href=\"search.jsp\">Search</a> \t\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<br>\r\n");
      out.write("\t</div>\r\n");
      out.write("</div>\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form name=\"input\" action=\"Login\" method=\"POST\">\r\n");
      out.write("<table>\r\n");
      out.write("\r\n");
      out.write("<tr>\r\n");
      out.write("<td>Login</td>\r\n");
      out.write("<td><input type=\"text\" name=\"username\" AUTOCOMPLETE=\"off\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("<tr>\r\n");
      out.write("<td>Password</td>\r\n");
      out.write("<td><input type=\"password\" name=\"password\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("<td><input type=\"submit\" value=\"Submit\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</form>\r\n");
      out.write("<br>\r\n");
      out.write("You can use any of the following accounts for this test system.<br>\r\n");
      out.write("\r\n");
      out.write("foo : foo<br>\r\n");
      out.write("sue : sue<br>\r\n");
      out.write("bob : bob<br>\r\n");
      out.write("<br>\r\n");
      out.write("<br>\r\n");
      out.write("<hr><br>\r\n");
      out.write("<font size=\"1\">last code update: 2009-12-23</font><br>\r\n");
      out.write("Please send any comments/questions to michael.coates@owasp.org<br>\r\n");
      out.write("Or provide your comments to the AppSensor <a href=\"https://lists.owasp.org/mailman/listinfo/owasp-appsensor-project\"> mailing list</a>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
      out.write('\r');
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
