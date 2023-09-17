package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.esapi.reference.FileBasedAuthenticator2;
import org.owasp.esapi.*;
import org.owasp.esapi.errors.*;
import org.owasp.esapi.AccessReferenceMap;
import org.owasp.esapi.codecs.*;

public final class LoginSolution_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n");
      out.write("\r\n");
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
      out.write("<a href=\"main?function=Login\">Tutorial</a> | \r\n");
      out.write("<a href=\"main?function=Login&lab\">Lab : Authenticator Functions</a>| \r\n");
      out.write("<b><a href=\"main?function=Login&solution\">Solution</a></b>\r\n");
      out.write("</div>\r\n");
      out.write("<div id=\"header\"></div>\r\n");
      out.write("<p>\r\n");
      out.write("<hr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<h2>Authenticator Methods Solution</h2>\r\n");
      out.write("\r\n");
      out.write("<h4>JSP Location: \\WebContent\\WEB-INF\\jsp\\LoginSolution.jsp</h4>\r\n");
      out.write("\r\n");
      out.write("<p>The following JSP creates a user account with - ESAPI.authenticator().createUser()</p>\r\n");
      out.write("<p>And logs the user in with ESAPI.authenticator().login()</p>\r\n");
      out.write("<p>After creating a user you should see a users.txt file in your .esapi folder.</p>\r\n");
      out.write("<p>You need to login once to enable and unlock a newly created account.</p>\r\n");
      out.write("<p>Please see the source of the JSP for full details.</p>\r\n");

			
	User user = null;
		
	try {
		if (request.getParameter("create_username") != null){			
			ESAPI.authenticator().createUser(request.getParameter("create_username"), request.getParameter("create_password1"), request.getParameter("create_password2"));
			ESAPI.authenticator().getUser(request.getParameter("create_username")).enable();
			ESAPI.authenticator().getUser(request.getParameter("create_username")).unlock();
			// The account is now unlocked and enabled but only in the runtime and not in the users.txt
			// You need to call ESAPI.authenticator().saveUsers() or login which implicit calls saveUsers()
			
			request.setAttribute("userMessage", "User " + request.getParameter("create_username") + " Created");
			request.setAttribute("logMessage", "User Created");
		
      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tUser Created : ");
      out.print(request.getParameter("create_username") );
      out.write("\t\r\n");
      out.write("\t\t\r\n");
 		
		}else if (request.getParameter("username") != null) {
			user = ESAPI.authenticator().login(request, response);

      out.write("\t\t\r\n");
      out.write("\t\t\tCurrent User: ");
      out.print(user.getAccountName() );
      out.write("<br>\r\n");
      out.write("\t\t\tLast Successful Login: ");
      out.print(user.getLastLoginTime() );
      out.write("<br>\r\n");
      out.write("\t\t\tLast Failed Login: ");
      out.print(user.getLastFailedLoginTime() );
      out.write("<br>\r\n");
      out.write("\t\t\tFailed Login Count: ");
      out.print(user.getFailedLoginCount() );
      out.write("<br>\r\n");
      out.write("\t\t\tCurrent Roles: ");
      out.print(user.getRoles() );
      out.write("<br>\r\n");
      out.write("\t\t\tLast Host Name: ");
      out.print(user.getLastHostAddress() );
      out.write("<br>\r\n");
      out.write("\t\t\tCurrent Cookies: <br />");
 Cookie[] cookies=ESAPI.httpUtilities().getCurrentRequest().getCookies(); for (int i=0; i<cookies.length; i++) out.print( "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp " + cookies[i].getName() + "=" + cookies[i].getValue() + "; <br />" ); 
      out.write("\r\n");
      out.write("\t\t\tBrowser Cookies: <script>document.write(document.cookie);</script><br><br>\r\n");
      out.write("\t\t\t<a href=\"main?function=Login&logout&solution\">logout</a>\r\n");

		}
				
	} catch( AuthenticationException e ) {
		request.setAttribute("userMessage", e.getUserMessage() );
		request.setAttribute("logMessage", e.getLogMessage() );		
		e.printStackTrace();
	} catch( Exception e){
		request.setAttribute("userMessage", e.getMessage());
		e.printStackTrace();
	}
	
	if ( user == null || user.isAnonymous() ) {

      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<H2>Create User</H2>\r\n");
      out.write("\t\t<form action=\"main?function=Login&solution\" method=\"POST\">\r\n");
      out.write("\t\t\t<table>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Username:</td><td><input name=\"create_username\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Password:</td><td><input type=\"password\" name=\"create_password1\" autocomplete=\"off\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Confirm Password:</td><td><input type=\"password\" name=\"create_password2\" autocomplete=\"off\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\t\t\t\r\n");
      out.write("\t\t\t<input type=\"submit\" value=\"Create User\"><br>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<H2>Please login</H2>\r\n");
      out.write("\t\t<form action=\"main?function=Login&solution\" method=\"POST\">\r\n");
      out.write("\t\t\t<table>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Username:</td><td><input name=\"username\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>Password:</td><td><input type=\"password\" name=\"password\" autocomplete=\"off\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<!--<tr>\r\n");
      out.write("\t\t\t\t\t<td>Remember me on this computer:</td>\r\n");
      out.write("\t\t\t\t\t<td><input type=\"checkbox\" name=\"remember\"></td>\r\n");
      out.write("\t\t\t\t</tr>-->\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t<input type=\"submit\" value=\"login\"><br>\r\n");
      out.write("\t\t</form>\r\n");

	} 

      out.write("\r\n");
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
