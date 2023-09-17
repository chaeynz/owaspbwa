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

String title = "ESAPI SwingSet Interactive Application";

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
      out.write("<!-- <h2><a href=\"main?function=InitialSetup\">1. ESAPI SwingSet Initial Setup</a></h2> -->\r\n");
      out.write("<!-- <p>This tutorial will guide you on how to install, configure and run Swingset for the first time.</p> -->\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=Introduction\">1. ESAPI SwingSet Interactive - Introduction</a></h2>\r\n");
      out.write("<p>A few words about this application.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=Login\">2. Authentication</a></h2>\r\n");
      out.write("<p>Authentication is the process of determining whether someone or something is, in fact, who or what it is declared to be. The ESAPI Authenticator interface defines a set of methods for generating and handling account credentials and session identifiers. The goal of this interface is to encourage developers to protect credentials from disclosure to the maximum extent possible.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=SessionManagement\">3. Session Management</a></h2>\r\n");
      out.write("<p>Session management is the process of keeping track of a user's activity across sessions of interaction with the computer system. The ESAPI HTTPUtilities interface is a collection of methods that provide additional security related to HTTP requests, responses, sessions, cookies, headers, and logging.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=AccessControl\">4. Access Control</a></h2>\r\n");
      out.write("<p>Access Control is a process that defines each user's privileges on a system.\r\n");
      out.write("The ESAPI AccessController interface defines a set of methods that can be used in a wide variety of applications to enforce access control.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=ValidateUserInput\">5. Input Validation </a></h2>\r\n");
      out.write("<p>Input Validation is the process of ensuring that a program operates on clean, correct and useful data. The ESAPI Validator interface defines a set of methods for canonicalizing and validating untrusted input.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=Encoding\">6. Output Encoding/Escaping</a></h2>\r\n");
      out.write("<p>Encoding is the process of transforming information from one format into another. The ESAPI Encoder interface contains a number of methods for decoding input and encoding output so that it will be safe for a variety of interpreters.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=Encryption\">7. Cryptography</a></h2>\r\n");
      out.write("<p>Encryption is the process of transforming information (referred to as plaintext) using an algorithm (called cipher) to make it unreadable to anyone except those possessing special knowledge, usually referred to as a key. The ESAPI Encryptor interface provides a set of methods for performing common encryption, random number, and hashing operations.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=Logging\">8. Error Handling and Logging</a></h2>\r\n");
      out.write("<p>Error handling refers to the anticipation, detection, and resolution of programming, application, and communications errors. Data logging is the process of recording events, with an automated computer program, in a certain scope in order to provide an audit trail that can be used to understand the activity of the system and to diagnose problems. The ESAPI Logger interface defines a set of methods that can be used to log security events.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=DataProtection\">9. Data Protection</a></h2>\r\n");
      out.write("<p>Data Protection is the process of ensuring the prevention of misuse of computer data. The ESAPI HTTPUtilities interface is a collection of methods that provide additional security related to HTTP requests, responses, sessions, cookies, headers, and logging.</p>\r\n");
      out.write("\r\n");
      out.write("<h2><a href=\"main?function=HttpSecurity\">10. Http Security</a></h2>\r\n");
      out.write("<p>HTTP Security refers to the protection of HTTP requests, responses, sessions, cookies, headers and logging. The ESAPI HTTPUtilities interface is a collection of methods that provide additional security for all these.</p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
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
      out.write("<p><center><a href=\"http://www.owasp.org/index.php/ESAPI\">OWASP Enterprise Security API Project</a> <!--  (c) 2008 --></center></p>\r\n");
      out.write("<!-- <p><center><a href=\"main?function=About\">About SwingSet</a></center></p> -->\r\n");
      out.write("<!--  <span id=\"copyright\">Design by <a href=\"http://www.sitecreative.net\" target=\"_blank\" title=\"Opens link to SiteCreative.net in a New Window\">SiteCreative</a></span> -->\r\n");
      out.write("\t</div> <!-- end holder div -->\r\n");
      out.write("</div> <!-- end container div -->\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
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
