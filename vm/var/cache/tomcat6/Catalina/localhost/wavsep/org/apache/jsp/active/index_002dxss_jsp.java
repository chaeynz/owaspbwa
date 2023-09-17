package org.apache.jsp.active;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_002dxss_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("\n");
      out.write("<center><H1>Reflected Cross Site Scripting - Test Cases</H1></center>\n");
      out.write("\n");
      out.write("<br><b>ReflectedXSS GET Input Vector</b><br>\n");
      out.write("<a href=\"Reflected-XSS/RXSS-Detection-Evaluation-GET/index.jsp\">Evaluation of RXSS Detection Accuracy - GET</a><br><br>\n");
      out.write("\n");
      out.write("<br><b>ReflectedXSS POST Input Vector</b><br>\n");
      out.write("<a href=\"Reflected-XSS/RXSS-Detection-Evaluation-POST/index.jsp\">Evaluation of RXSS Detection Accuracy - POST</a><br><br>\n");
      out.write("\n");
      out.write("<br><b>ReflectedXSS Cookie Input Vector - Experimental </b><br>\n");
      out.write("<a href=\"Reflected-XSS/RXSS-Detection-Evaluation-COOKIE-Experimental/index.jsp\">Evaluation of RXSS Detection Accuracy - COOKIE</a><br><br>\n");
      out.write("\n");
      out.write("<br><b>ReflectedXSS GET Input Vector - Experimental Test Cases: Tag Stripping / Secret Input Vectors / Scriptless Exploits / Other</b><br>\n");
      out.write("<a href=\"Reflected-XSS/RXSS-Detection-Evaluation-GET-Experimental/index.jsp\">Evaluation of RXSS Detection Accuracy - GET</a><br><br>\n");
      out.write("\n");
      out.write("<br><b>ReflectedXSS POST Input Vector - Experimental Test Cases: Tag Stripping / Secret Input Vectors / Scriptless Exploits / Other</b><br>\n");
      out.write("<a href=\"Reflected-XSS/RXSS-Detection-Evaluation-POST-Experimental/index.jsp\">Evaluation of RXSS Detection Accuracy - POST</a><br><br>\n");
      out.write("\n");
      out.write("<br><b>DomXSS GET Input Vector - Experimental Test Cases</b><br>\n");
      out.write("<a href=\"DOM-XSS/DXSS-Detection-Evaluation-GET-Experimental/index.jsp\">Evaluation of RXSS Detection Accuracy - GET</a><br><br>\n");
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
