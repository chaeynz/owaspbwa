package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/WEB-INF/jsp/footer.jsp");
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

      out.write('\r');
      out.write('\n');

String title = "ESAPI SwingSet Demonstration Application beta";

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>");
      out.print(title);
      out.write("</title>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"style/style.css\" />\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("<div id=\"container\">\r\n");
      out.write("\t<div id=\"holder\">\r\n");
      out.write("\t\t<div id=\"logo\"><img src=\"style/images/owasp-logo_130x55.png\" width=\"130\" height=\"55\" alt=\"owasp_logo\" title=\"owasp_logo\"></div>\r\n");
      out.write("<h1>");
      out.print(title );
      out.write("</h1>\r\n");
      out.write("<div id=\"header\"></div>\r\n");
      out.write("<hr> \r\n");
      out.write("\r\n");
      out.write("<h2>Input Validation, Encoding, and Injection</h2>\r\n");
      out.write("<ul>\r\n");
      out.write("<li><a href=\"main?function=OutputUserInput\">Output User Input</a></li>\r\n");
      out.write("<li><a href=\"main?function=RichContent\">Accept Rich Content</a></li>\r\n");
      out.write("<li><a href=\"main?function=ValidateUserInput\">Validate User Input</a></li>\r\n");
      out.write("<li><a href=\"main?function=Encoding\">Encode Output</a></li>\r\n");
      out.write("<li><a href=\"main?function=Canonicalize\">Canonicalize Input</a></li>\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<h2>Cross Site Scripting</h2>\r\n");
      out.write("<ul>\r\n");
      out.write("<li><a href=\"main?function=XSS\">Cross Site Scripting</a></li>\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<h2>Authentication and Session Management</h2>\r\n");
      out.write("<ul>\r\n");
      out.write("<li><a href=\"main?function=Login\">Login</a></li>\r\n");
      out.write("<!-- <li><a href=\"main?function=Logout\">Logout</a></li> (no implementation)-->\r\n");
      out.write("<li><a href=\"main?function=ChangePassword\">Change Password</a></li>\r\n");
      out.write("<li><a href=\"main?function=ChangeSessionIdentifier\">Change Session Identifier</a></li>\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<h2>Access Control and Referencing Objects</h2>\r\n");
      out.write("<ul>\r\n");
      out.write("<li><a href=\"main?function=ObjectReference\">Reference a Server-Side Object</a></li>\r\n");
      out.write("<li><a href=\"main?function=AccessControl\">Access Control</a></li>\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<h2>Encryption, Randomness, and Integrity</h2>\r\n");
      out.write("<ul>\r\n");
      out.write("<li><a href=\"main?function=Encryption\">Encryption</a></li>\r\n");
      out.write("<li><a href=\"main?function=Randomizer\">Randomizer</a></li>\r\n");
      out.write("<li><a href=\"main?function=Integrity\">Integrity Seals</a></li>\r\n");
      out.write("<li><a href=\"main?function=GUID\">Globally Unique IDs</a></li>\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<h2>Caching</h2>\r\n");
      out.write("<ul>\r\n");
      out.write("<li><a href=\"main?function=BrowserCaching\">Browser Caching</a></li>\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<hr>\r\n");

	String message = (String)request.getAttribute( "message" );
	if ( request.getAttribute("userMessage") != null || request.getAttribute("logMessage") != null) {

      out.write("\r\n");
      out.write("\t<p>User Message: <font color=\"red\">");
      out.print(org.owasp.esapi.ESAPI.encoder().encodeForHTML(request.getAttribute("userMessage").toString()) );
      out.write("</font></p>\r\n");
      out.write("\t<p>Log Message: <font color=\"red\">");
      out.print(org.owasp.esapi.ESAPI.encoder().encodeForHTML(request.getAttribute("logMessage").toString()) );
      out.write("</font></p><hr>\r\n");

	}

      out.write("\r\n");
      out.write("<p><center><a href=\"http://www.owasp.org/index.php/ESAPI\">OWASP Enterprise Security API Project</a> (c) 2008</center></p>\r\n");
      out.write("<!--  <span id=\"copyright\">Design by <a href=\"http://www.sitecreative.net\" target=\"_blank\" title=\"Opens link to SiteCreative.net in a New Window\">SiteCreative</a></span> -->\r\n");
      out.write("\t</div> <!-- end holder div -->\r\n");
      out.write("</div> <!-- end container div -->\r\n");
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
