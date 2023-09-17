package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.esapi.*;
import org.owasp.esapi.errors.*;
import org.owasp.esapi.AccessReferenceMap;
import org.owasp.esapi.codecs.*;

public final class CSRFLab_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<a href=\"main?function=SessionFixation&lab\">Lab : Session Fixation</a>| \r\n");
      out.write("<a href=\"main?function=SessionFixation&solution\">Solution</a> |\r\n");
      out.write("<b><a href=\"main?function=CSRF&lab\">Lab : Cross Site Request Forgery</a></b> | \r\n");
      out.write("<a href=\"main?function=CSRF&solution\">Solution</a>\r\n");
      out.write("</div>\r\n");
      out.write("<div id=\"header\"></div>\r\n");
      out.write("<p>\r\n");
      out.write("<hr>\r\n");
      out.write("\r\n");
      out.write("<h2>Cross Site Request Forgery Lab</h2>\r\n");
      out.write("\r\n");
      out.write("<h4>JSP Location: WebContent\\WEB-INF\\Jsp\\CSRFLab.jsp</h4>\r\n");
      out.write("\r\n");
      out.write("<p>CSRF is an attack which forces an end user to execute unwanted actions on a web application in which he/she is currently authenticated. With a little help of social engineering (like sending a link via email/chat), an attacker may force the users of a web application to execute actions of the attacker's choosing. A successful CSRF exploit can compromise end user data and operation in case of normal user. If the targeted end user is the administrator account, this can compromise the entire web application. </p> \r\n");
      out.write("<p>After logging in, click on the link at the bottom to go to a dummy funds transfer page. <br/>\r\n");
      out.write("Your goal is to protect the funds transfer page against a CSRF Attack. \r\n");
      out.write("Add a CSRF Token to the link provided below and Validate the CSRF Token on the Funds Transfer Page. \t\t\t\t\t\t\t\t\r\n");
      out.write("</p>\r\n");
      out.write("\r\n");

			
	User user = null;
		
	try {
			user = ESAPI.authenticator().login(request, response);

			String transferFundsHref = "main?function=TransferFunds&lab";
			
		
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<!-- TODO : Add a CSRF Token to this URL -->\r\n");
      out.write("\t\t\t<a href='");
      out.print(transferFundsHref);
      out.write("' target=\"_blank\">Transfer Funds</a>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\r\n");

					
	} catch( AuthenticationException e ) {
		request.setAttribute("userMessage", e.getUserMessage() );
		request.setAttribute("logMessage", e.getLogMessage() );		
		e.printStackTrace();
	} catch( Exception e){
		request.setAttribute("userMessage", e.getMessage());
		e.printStackTrace();
	}
	
	if ( user == null || user.isAnonymous() ) {

      out.write("\t\t\t\r\n");
      out.write("\t\t<H4>Please login</H4>\r\n");
      out.write("\t\t<p>If you do not have a user account created, you can do so from <a href=\"main?function=Login&solution\" target=\"_blank\">Authentication Chapter Solution</a></p>\r\n");
      out.write("\t\t<form action=\"main?function=CSRF&lab\" method=\"POST\">\r\n");
      out.write("\t\t\t<table>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Username:</td><td><input name=\"username\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Password:</td><td><input type=\"password\" name=\"password\" autocomplete=\"off\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Remember me on this computer:</td>\r\n");
      out.write("\t\t\t\t\t<td><input type=\"checkbox\" name=\"remember\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t<input type=\"submit\" value=\"login\"><br>\r\n");
      out.write("\t\t</form>\r\n");

	} 

      out.write("\r\n");
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
