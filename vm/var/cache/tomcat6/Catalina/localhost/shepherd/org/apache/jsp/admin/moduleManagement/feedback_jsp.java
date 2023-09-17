package org.apache.jsp.admin.moduleManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import dbProcs.*;
import utils.*;

public final class feedback_jsp extends org.apache.jasper.runtime.HttpJspBase
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

	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG: feedback.jsp *************************");

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
 
if (request.getSession() != null)
{
HttpSession ses = request.getSession();
Getter get = new Getter();
//Getting CSRF Token from client
Cookie tokenCookie = null;
try
{
	tokenCookie = Validate.getToken(request.getCookies());
}
catch(Exception htmlE)
{
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(feedback.jsp): tokenCookie Error:" + htmlE.toString());
}
// validateAdminSession ensures a valid session, and valid administrator credentials
// Also, if tokenCookie != null, then the page is good to continue loading
// Token is now validated when accessing admin pages to stop attackers causing other users to tigger logs of access attempts
Object tokenParmeter = request.getParameter("csrfToken");
if(Validate.validateAdminSession(ses, tokenCookie, tokenParmeter))
{
	//Logging Username
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Accessed by: " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
// Getting Session Variables
//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
Encoder encoder = ESAPI.encoder();
String csrfToken = encoder.encodeForHTMLAttribute(tokenCookie.getValue());
String ApplicationRoot = getServletContext().getRealPath("");

      out.write("\r\n");
      out.write("\t<div id=\"formDiv\" class=\"post\">\r\n");
      out.write("\t\t<h1 class=\"title\">Get Module Feedback</h1>\r\n");
      out.write("\t\t<div class=\"entry\">\r\n");
      out.write("\t\t\t<div id=\"badData\"></div>\r\n");
      out.write("\t\t\t<form id=\"theForm\" action=\"javascript:;\">\r\n");
      out.write("\t\t\t\t\t<p>Select the module you would like to see the feedback from</p>\r\n");
      out.write("\t\t\t\t\t<div id=\"badData\"></div>\r\n");
      out.write("\t\t\t\t\t<input type=\"hidden\" id=\"csrfToken\" value=\"");
      out.print( csrfToken );
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t<table align=\"center\">\r\n");
      out.write("\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<select id=\"theModule\" style=\"width: 300px\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<option></option>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t");
      out.print( Getter.getModulesInOptionTags(ApplicationRoot) );
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\t<tr><td align=\"center\">\r\n");
      out.write("\t\t\t\t\t\t\t<div id=\"submitButton\"><input type=\"submit\" value=\"Get Feedback\"/></div>\r\n");
      out.write("\t\t\t\t\t\t\t<div id=\"loadingSign\" style=\"display: none;\"><p>Loading...</p></div> \r\n");
      out.write("\t\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\t<div id=\"resultDiv\"></div>\r\n");
      out.write("\t\t\t\t\t<script>\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t$(\"#theForm\").submit(function(){\r\n");
      out.write("\t\t\t\t\t\tvar theCsrfToken = $('#csrfToken').val();\r\n");
      out.write("\t\t\t\t\t\tvar theModule = $(\"#theModule\").val();\r\n");
      out.write("\t\t\t\t\t\t//The Ajax Operation\r\n");
      out.write("\t\t\t\t\t\t$(\"#badData\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#submitButton\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#loadingSign\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#resultDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\t\t\t\turl: \"getFeedback\",\r\n");
      out.write("\t\t\t\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\t\t\t\tmoduleId: theModule,\r\n");
      out.write("\t\t\t\t\t\t\t\t\tcsrfToken: theCsrfToken\r\n");
      out.write("\t\t\t\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#resultDiv\").html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#resultDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t\telse\r\n");
      out.write("\t\t\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#badData\").html(\"<div id='errorAlert'><p> Sorry but there was an error: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p></div>\");\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#badData\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t$(\"#loadingSign\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#submitButton\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t</script>\r\n");
      out.write("\t\t\t\t\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write("\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t");

}
else
{
response.sendRedirect("../../loggedOutSheep.html");
}
}
else
{
response.sendRedirect("../../loggedOutSheep.html");
}

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
