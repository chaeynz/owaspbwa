package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import utils.*;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html; charset=iso-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");

ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Sombody Conntected to login.jsp ...");

/**
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 * @author Mark Denihan
 */
 
HttpSession ses = request.getSession();
if(request.getSession() != null)
{
	if(ses.getAttribute("loginFailed") == null 
	&& ses.getAttribute("registrationSuccess") == null
	&& ses.getAttribute("registerError") == null)
	{
		ses.invalidate();
		ses = request.getSession(true);
	}
}
String url = (request.getRequestURL()).toString();
if(url.contains("login.jsp"))
{
	url = url.substring(0, url.lastIndexOf("/") + 1);
}
else
{
	response.sendRedirect("login.jsp");
}

String registrationSuccess = new String();
String loginFailed = new String();
String registerError = new String();
Encoder encoder = ESAPI.encoder();

if(ses.getAttribute("loginFailed") != null)
{
	loginFailed = ses.getAttribute("loginFailed").toString();
	ses.removeAttribute("loginFailed");
}

      out.write("\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("<title>OWASP Security Shepherd - Login</title>\r\n");
      out.write("\r\n");
      out.write("<link href=\"css/theCss.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<script type=\"text/javascript\" src=\"js/jquery.js\"></script>\r\n");
      out.write("<div id=\"wrapper\">\r\n");
      out.write("<!-- start header -->\r\n");
      out.write("<div id=\"header\">\r\n");
      out.write("\t<h1>Security Shepherd</h1>\r\n");
      out.write("</div>\r\n");
      out.write("<!-- end header -->\r\n");
      out.write("<!-- start page -->\r\n");
      out.write("<div id=\"page\">\r\n");
      out.write("\t<!-- start content -->\r\n");
      out.write("\t<div id=\"content\" style=\"margin-left: auto; margin-right: auto; width:320px\">\r\n");
      out.write("\t\t<div class=\"post\">\r\n");
      out.write("\t\t\t<h1 class=\"title\">Login</h1>\r\n");
      out.write("\t\t\t<p>Use your <a><span>Security Shepherd Credentials</span></a> to Login.</p>\r\n");
      out.write("\t\t\t");
 if(OpenRegistration.isEnabled()) { 
      out.write("\r\n");
      out.write("\t\t\t\t<p>Register a <a href=\"register.jsp\"><span>Security Shepherd Account</span></a> here!</p>\r\n");
      out.write("\t\t\t");
 } if(!loginFailed.isEmpty()) {
      out.write("\r\n");
      out.write("\t\t\t\t<p><strong><font color=\"red\">");
      out.print( loginFailed );
      out.write("</font></strong></p>\r\n");
      out.write("\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t<form name=\"loginForm\" method=\"POST\" action=\"login\">\r\n");
      out.write("\t\t\t\t<table>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Username:</td><td><input type=\"text\" name=\"login\" value=\"\" autocomplete=\"OFF\" autofocus/></p></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Password:&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t</td><td><input type=\"password\" name=\"pwd\" autocomplete=\"OFF\"/><br /></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td colspan=\"2\" align=\"center\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"submit\" name=\"submit\" value=\"Login\" />\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t<div align=\"center\">\r\n");
      out.write("\t\t\t\t<a id=\"tools\" href=\"javascript:;\">Do you need a Proxy?</a>\r\n");
      out.write("\t\t\t\t<div id=\"toolsTable\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t<p>Download a HTTP Proxy here;</p>\r\n");
      out.write("\t\t\t\t<table>\r\n");
      out.write("\t\t\t\t\t<tr><td align=\"center\"><a href=\"http://bit.ly/zapWindows\">ZAP for Windows</a></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td align=\"center\"><a href=\"http://bit.ly/zapLinux\">ZAP for Linux</a></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td align=\"center\"><a href=\"http://bit.ly/zapForMac\">ZAP for Mac</a></td></tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<br><a id=\"showAbout\" href=\"javascript:;\">About Security Shepherd</a>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<div align=\"justify\">\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<div id=\"aboutDiv\" style=\"display: none;\">\r\n");
      out.write("\t\t<h2 class=\"title\">About Security Shepherd</h2>\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\t\tThe OWASP Security Shepherd project has been designed and implemented with the aim of fostering and improving application security\r\n");
      out.write("\t\t\t awareness among a varied skill-set demographic.This project enables users to learn or to \r\n");
      out.write("\t\t\t improve upon existing manual penetration testing skills. This is accomplished through \r\n");
      out.write("\t\t\t lesson and challenge techniques. A lesson provides a user with a lot of help in completing \r\n");
      out.write("\t\t\t that module, where a challenge puts what the user learned in the lesson to use.\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\tThe OWASP Security Shepherd project covers the OWASP Top Ten web app risks and also covers the OWASP Top Ten Mobile risks as well. \r\n");
      out.write("\t\t\tUsing these risks as a challenge test bed, common security vulnerabilities can be explored and their \r\n");
      out.write("\t\t\timpact on a system understood. Many of these levels include insufficient mitigations and protections to these risks, \r\n");
      out.write("\t\t\tsuch as blacklist filters, atrocious encoding schemes, barbaric security mechanisms and \r\n");
      out.write("\t\t\tpoor security configuration. The modules have been crafted to provide not only a challenge for a \r\n");
      out.write("\t\t\tsecurity novice, but security professionals as well.\r\n");
      out.write("\t\t</p>\r\n");
      out.write("\t\t<h2 class=\"title\">Project Sponsors</h2>\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\t\tThe OWASP Security Shepherd project would like to acknowledge and thank the generous support of our sponsors. \r\n");
      out.write("\t\t\tPlease be certain to visit their stall at the <a href=\"http://bit.ly/AppSecEu2014\">OWASP AppSec EU 2014</a> \r\n");
      out.write("\t\t\tconference as well as follow them on <a href=\"http://bit.ly/bccRiskAdvisory\">twitter</a>.\r\n");
      out.write("\t\t\t<br/><br/>\r\n");
      out.write("\t\t\t<a href=\"http://bit.ly/BccRiskAdvisorySite\"><img src=\"css/images/bccRiskAdvisorySmallLogo.jpg\" alt=\"BCC Risk Advisory\"/></a>\r\n");
      out.write("\t\t\t<a href=\"http://bit.ly/EdgeScan\"><img src=\"css/images/edgescanSmallLogo.jpg\" alt=\"EdgeScan\" /></a>\r\n");
      out.write("\t\t\t<br/><br/>\r\n");
      out.write("\t\t\tThe OWASP Security Shepherd Project would also like to thank Dr. Anthony Keane and the ITB Research Lab for hosting http://owasp.securityShepherd.eu!<br>\r\n");
      out.write("\t\t\t<a href=\"http://securityresearch.ie/\"><img src=\"https://www.owasp.org/images/thumb/2/24/Fontlogo.png/300px-Fontlogo.png\"/></a>  \r\n");
      out.write("\t\t</p>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<!-- end content -->\r\n");
      out.write("\t<!-- start sidebar -->\r\n");
      out.write("\t<!-- end sidebar -->\r\n");
      out.write("</div>\r\n");
      out.write("</div>\r\n");
      out.write("<!-- end page -->\r\n");
      out.write("<script>\r\n");
      out.write("\tjQuery.fn.center = function () \r\n");
      out.write("\t{\r\n");
      out.write("\t\tthis.css(\"position\",\"absolute\");\r\n");
      out.write("\t\tthis.css(\"left\", (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + \"px\");\r\n");
      out.write("\t\treturn this;\r\n");
      out.write("\t}\t\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#tools\").click(function(){\r\n");
      out.write("\t\t$(\"#toolsTable\").show(\"slow\");\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#showAbout\").click(function(){\r\n");
      out.write("\t\t$(\"#aboutDiv\").show(\"slow\");\r\n");
      out.write("\t});\r\n");
      out.write("</script>\r\n");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
