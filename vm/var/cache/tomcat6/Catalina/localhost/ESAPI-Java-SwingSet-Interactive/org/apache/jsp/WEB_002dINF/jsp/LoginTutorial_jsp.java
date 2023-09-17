package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.esapi.*;
import org.owasp.esapi.errors.*;
import org.owasp.esapi.AccessReferenceMap;
import org.owasp.esapi.codecs.*;

public final class LoginTutorial_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<b><a href=\"main?function=Login\">Tutorial</a></b> | \r\n");
      out.write("<a href=\"main?function=Login&lab\">Lab : Authenticator Functions</a>| \r\n");
      out.write("<a href=\"main?function=Login&solution\">Solution</a> \r\n");
      out.write("</div>\r\n");
      out.write("<div id=\"header\"></div>\r\n");
      out.write("<p>\r\n");
      out.write("<hr>\r\n");
      out.write("\r\n");
      out.write("<h2 align=\"center\">Tutorial</h2>\r\n");
      out.write("<h4>Background</h4>\r\n");
      out.write("<p>Instance of the ESAPI's authenticator Class can be created as:</p>\r\n");
      out.write("\r\n");
      out.write("<p class=\"newsItem\"><code>Authenticator instance = ESAPI.authenticator();</code></p>\r\n");
      out.write("\r\n");
      out.write("<p>If you plan on using the default Access Controller, you may need one or more of the following:</p>\r\n");
      out.write("<li> DataAccessRules.txt</li>\r\n");
      out.write("<li> FileAccessRules.txt</li>\r\n");
      out.write("<li> FunctionAccessRules.txt</li>\r\n");
      out.write("<li> ServiceAccessRules.txt</li>\r\n");
      out.write("<li> URLAccessRules.txt</li>\r\n");
      out.write("\r\n");
      out.write("<p>You do not need users.txt.  ESAPI will create this file when your application requests to create its first user.</p>\r\n");
      out.write("\r\n");
      out.write("<h4>Create Users</h4>\r\n");
      out.write("There are two ways to create users safely in ESAPI:\r\n");
      out.write("<p>Use main() from FileBasedAuthenticator to generate users.txt for the first time.  To do this:</p>\r\n");
      out.write("<p class=\"newsItem\"><code>java -Dorg.owasp.esapi.resources=\"/path/resources\" -classpath esapi.jar org.owasp.esapi.Authenticator username password role</pre></code></p>\r\n");
      out.write("\r\n");
      out.write("To create users from within your application, use:\r\n");
      out.write("<p class=\"newsItem\"><code>ESAPI.authenticator.createUser(username, password, password)</code></p>\r\n");
      out.write("Two copies of the new password are required to encourage user interface designers to include a \"re-type password\" field in their forms.\r\n");
      out.write("\r\n");
      out.write("<br />''Note:Users created with the createUser method are disabled and locked by default.'' <br /><br /> You must call:\r\n");
      out.write("<p class=\"newsItem\">\r\n");
      out.write("<code>\r\n");
      out.write("ESAPI.authenticator().getUser(username).enable();<br>\r\n");
      out.write("ESAPI.authenticator().getUser(username).unlock();\r\n");
      out.write("</code>\r\n");
      out.write("</p>\r\n");
      out.write("\r\n");
      out.write("<h4>Login</h4>\r\n");
      out.write("If you use the default ESAPI authenticator, you will need your login page to use SSL, so be sure to have a keystore file and adjust your server configuration settings to account for this.  If you are using Apache Tomcat, please see the readme included in the latest release of the <a href=\"https://www.owasp.org/index.php/ESAPI_Swingset\">ESAPI Swingset</a> for help setting up SSL.\r\n");
      out.write("\r\n");
      out.write("<br/><br/>\r\n");
      out.write("<a href=\"http://tomcat.apache.org/tomcat-6.0-doc/ssl-howto.html#Configuration\">Set up SSL for tomcat 6.0</a>\r\n");
      out.write("\r\n");
      out.write("<br/><br/>\r\n");
      out.write("\r\n");
      out.write("<p>To authenticate a user, call:</p>\r\n");
      out.write("<p class=\"newsItem\"><code>User  user = ESAPI.authenticator().login(HTTPServletRequest, HTTPServletResponse);</code></p>\r\n");
      out.write("\r\n");
      out.write("Be sure to set the UsernameParameterName and PasswordParameterName variables in ESAPI.properties.  The login method will use those variable names to take the username and password that the user entered from the HTTPRequest.\r\n");
      out.write("\r\n");
      out.write("<h4>Logout</h4>\r\n");
      out.write("To log a User out, simply call:\r\n");
      out.write("<p class=\"newsItem\">\r\n");
      out.write("<code>User  user = ESAPI.authenticator().logout;</code>\r\n");
      out.write("</p>\r\n");
      out.write("\r\n");
      out.write("<h4>ESAPI User Interface: </h4>\r\n");
      out.write("<p>ESAPI's User Interface provides support to store lot of information that an application  must store for each user in order to enforce security properly.</p>\r\n");
      out.write("<p>A user account can be in one of several states. When first created, a User should be disabled, not expired, and unlocked. To start using the account, an administrator should enable the account. The account can be locked for a number of reasons, most commonly because they have failed login for too many times. Finally, the account can expire after the expiration date has been reached. The User must be enabled, not expired, and unlocked in order to pass authentication. </p>\r\n");
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
