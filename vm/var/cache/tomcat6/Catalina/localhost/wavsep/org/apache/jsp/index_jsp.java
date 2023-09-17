package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n");
      out.write("<title>Evaluation of Web Application Scanners Detection Accuracy</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<H3>Welcome to WAVSEP - The Web Application Vulnerability Scanner Evaluation Project</H3>\n");
      out.write("\n");
      out.write("<H4>Version: 1.5</H4><br>\n");
      out.write("\n");
      out.write("The index page of the project intentionally lacks links and forms.<br>\n");
      out.write("Please access the following index pages to perform specific tests:<br>\n");
      out.write("<br>\n");
      out.write("\n");
      out.write("<table align=top>\n");
      out.write("<tr>\n");
      out.write("<td><b><u>index-active.jsp</u></b></td><td><b><u>index-passive.jsp</u></b></td>\n");
      out.write("</tr><tr>\n");
      out.write("<td valign=top>\n");
      out.write("<b>active/index-xss.jsp</b><br>\n");
      out.write("<b>active/index-sql.jsp</b><br>\n");
      out.write("<b>active/index-lfi.jsp</b><br>\n");
      out.write("<b>active/index-rfi.jsp</b><br>\n");
      out.write("<b>active/index-redirect.jsp</b><br>\n");
      out.write("<b>active/index-obsolete.jsp</b><br>\n");
      out.write("<b>active/index-false.jsp</b><br>\n");
      out.write("</td>\n");
      out.write("<td valign=top>\n");
      out.write("<b>passive/index-info.jsp</b><br>\n");
      out.write("<b>passive/index-session.jsp</b><br>\n");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("</table>\n");
      out.write("\n");
      out.write("<br><br>\n");
      out.write("<b><u>Notes</u></b><br>\n");
      out.write("Make sure you install the database using the auto-installer, <br>\n");
      out.write("and according to the instructions provided at the WAVSEP Google Code home page.<br>\n");
      out.write("<b><u>Known Issues</u></b><br>\n");
      out.write("Previous versions of wavsep might require the web server to run with admin/root permissions (for the database installation script),<br>\n");
      out.write("due to the usage of a derby database created in a default location.\n");
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
