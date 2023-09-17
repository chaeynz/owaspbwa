package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.esapi.*;
import org.owasp.esapi.errors.*;
import org.owasp.esapi.AccessReferenceMap;
import org.owasp.esapi.codecs.*;

public final class SessionFixationLab_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/WEB-INF/jsp/header.jsp");
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
      response.setContentType("text/html; charset=UTF-8");
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

String function = (String)request.getAttribute("function");
String title = "ESAPI SwingSet Interactive - " + function;
String querystring = request.getQueryString();
String pageHeader = "ESAPI Swingset Interactive - " + function;

int i1 = querystring.indexOf("&solution");
boolean secure = ( i1 != -1 );
if ( secure ) {
	querystring = querystring.substring( 0, i1 );
	title += ": Solution with ESAPI";
	pageHeader += ": Solution with ESAPI";
}

int i2 = querystring.indexOf("&lab");
boolean insecure = ( i2 != -1 );
if ( insecure ) {
	querystring = querystring.substring( 0, i2 );
	title += ": Lab";
	pageHeader += ": Lab";
}


      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>");
      out.print(title);
      out.write("</title>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"style/style.css\" />\r\n");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
 if ( !insecure && !secure ) { 
      out.write(" <body> ");
 } 
      out.write('\r');
      out.write('\n');
 if ( insecure ) { 
      out.write(" <body bgcolor=\"#EECCCC\"> ");
 } 
      out.write('\r');
      out.write('\n');
 if ( secure ) { 
      out.write(" <body bgcolor=\"#BBDDBB\"> ");
 } 
      out.write("\r\n");
      out.write("<div id=\"container\">\r\n");
      out.write("\t<div id=\"holder\">\r\n");
      out.write("\t\t<div id=\"logo\"><img src=\"style/images/owasp-logo_130x55.png\" width=\"130\" height=\"55\" alt=\"owasp_logo\" title=\"owasp_logo\"></div>\r\n");
      out.write("<h2>");
      out.print(pageHeader );
      out.write("</h2>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<div id=\"navigation\">\r\n");
      out.write("<a href=\"main\">Home</a> | \r\n");
      out.write("<a href=\"main?function=SessionManagement\">Tutorial</a> | \r\n");
      out.write("<b><a href=\"main?function=SessionFixation&lab\">Lab : Session Fixation</a></b>| \r\n");
      out.write("<a href=\"main?function=SessionFixation&solution\">Solution</a> |\r\n");
      out.write("<a href=\"main?function=CSRF&lab\">Lab : Cross Site Request Forgery</a> | \r\n");
      out.write("<a href=\"main?function=CSRF&solution\">Solution</a>\r\n");
      out.write("</div>\r\n");
      out.write("<div id=\"header\"></div>\r\n");
      out.write("<p>\r\n");
      out.write("<hr>\r\n");
      out.write("\r\n");
      out.write("<h2>Exercise: Session Fixation</h2>\r\n");
      out.write("\r\n");
      out.write("<h4>JSP Location: WebContent\\WEB-INF\\JSP\\SessionFixationLab.jsp<br/>\r\n");
      out.write("Java Resources: org.owasp.esapi.swingset.login.LoginServletLab.java</h4>\r\n");
      out.write("\r\n");
      out.write("<h3>Step 1</h3>\r\n");
      out.write("<p>Open this <a href=\"main?function=InsecureLogin&lab\" target=\"_blank\">link</a> in a new browser window and login using the following user details :</p>\r\n");
      out.write("<p>username : test <br/>password : test<br/><br/>Make a note of the session id displayed.</p>\r\n");
      out.write("\r\n");
      out.write("<h3>Step 2</h3>\r\n");
      out.write("<p>Open a new browser window and browse to the following URL: .../LoginServletLab. <br> You should be told that you are not logged in.  \r\n");
      out.write("<h3>Step 3</h3>\r\n");
      out.write("<p>\r\n");
      out.write("You have a session id for a logged in user from Step 1. Change the session id for example by using the \r\n");
      out.write("Tamper Data Firefox plugin.</p>\r\n");
      out.write("\r\n");
      out.write("<h3>Step 4</h3>\r\n");
      out.write("<p>\r\n");
      out.write("Now that you have seen how session fixation works. Try to secure the login servlet against this vulnerability.</p>\r\n");
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
      out.write("<p><center><a href=\"http://www.owasp.org/index.php/ESAPI\">OWASP Enterprise Security API Project</a> <!--  (c) 2008 --></center></p>\r\n");
      out.write("<!-- <p><center><a href=\"main?function=About\">About SwingSet</a></center></p> -->\r\n");
      out.write("<!--  <span id=\"copyright\">Design by <a href=\"http://www.sitecreative.net\" target=\"_blank\" title=\"Opens link to SiteCreative.net in a New Window\">SiteCreative</a></span> -->\r\n");
      out.write("\t</div> <!-- end holder div -->\r\n");
      out.write("</div> <!-- end container div -->\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
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
