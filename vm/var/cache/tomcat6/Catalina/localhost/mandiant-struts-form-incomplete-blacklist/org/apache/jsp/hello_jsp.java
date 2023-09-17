package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class hello_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<title>Struts Form (Incomplete Black List)</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("<center><table border=0 width=100%><tr>\n");
      out.write("<td>\n");
      out.write("<center><h1>Struts Form (Incomplete Black List)</h1></center>\n");
      out.write("</td>\n");
      out.write("<td align=right>\n");
      out.write("Contributed by:<br>\n");
      out.write("<a href=\"http://www.mandiant.com/\"><img src=\"mandiant_logo.png\" border=0 align=right></a>\n");
      out.write("</td>\n");
      out.write("</tr></table></center>\n");
      out.write("\n");
      out.write("<h1>Welcome</h1>\n");
      out.write("\n");
      com.mandiant.NameBean nameBean = null;
      synchronized (request) {
        nameBean = (com.mandiant.NameBean) _jspx_page_context.getAttribute("nameBean", PageContext.REQUEST_SCOPE);
        if (nameBean == null){
          nameBean = new com.mandiant.NameBean();
          _jspx_page_context.setAttribute("nameBean", nameBean, PageContext.REQUEST_SCOPE);
        }
      }
      out.write("\n");
      out.write("<p>Welcome, ");
      out.print(nameBean.getName());
      out.write("!</p>\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
