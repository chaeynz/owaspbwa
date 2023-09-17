package org.apache.jsp;

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

public final class getStarted_jsp extends org.apache.jasper.runtime.HttpJspBase
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
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(getStarted.jsp): tokenCookie Error:" + htmlE.toString());
}
// validateSession ensures a valid session, and valid role credentials
// Also, if tokenCookie != null, then the page is good to continue loading
if (Validate.validateSession(ses) && tokenCookie != null)
{
	//Logging Username
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Accessed by: " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
// Getting Session Variables
//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
Encoder encoder = ESAPI.encoder();
String csrfToken = encoder.encodeForHTMLAttribute(tokenCookie.getValue());
String userName = encoder.encodeForHTML(ses.getAttribute("userName").toString());
String userRole = encoder.encodeForHTML(ses.getAttribute("userRole").toString());
String userId = encoder.encodeForHTML(ses.getAttribute("userStamp").toString());
String threadId = (String) ses.getAttribute("ThreadSequenceId");
String ApplicationRoot = getServletContext().getRealPath("");
boolean isAdmin = userRole.equalsIgnoreCase("admin");
boolean changePassword = false;
if(ses.getAttribute("ChangePassword") != null)
{
	String tempPass = ses.getAttribute("ChangePassword").toString();
	changePassword = tempPass.equalsIgnoreCase("true");
}

int i = 0;

//IF Change Password is True, Stick up a form
if(!changePassword)
{

      out.write("\r\n");
      out.write("\t<div id=\"getStarted\" style=\"display:none;\">\r\n");
      out.write("\t<div class=\"post\">\r\n");
      out.write("\t\t<h1 class=\"title\">Lets Get Started</h1>\r\n");
      out.write("\t\t<div class=\"entry\">\r\n");
      out.write("\t\t\t");
 if(ModulePlan.openFloor) { 
      out.write("\r\n");
      out.write("\t\t\tNow that you've signed in, lets get started with some Security Shepherd modules! To start a module, click on the list headers, <span><a>Lessons</a></span> or <span><a>Modules</a></span>, in the side menu to see what modules are currently available!\r\n");
      out.write("\t\t\t");
 } else if (ModulePlan.incrementalFloor) { 
      out.write("\r\n");
      out.write("\t\t\tNow that you've signed in, lets get started with some Security Shepherd challenges! To start one, click the &quot;Get Next Challenge&quot; button on the left!\r\n");
      out.write("\t\t\t");
 } else {
      out.write("\r\n");
      out.write("\t\t\tNow that you've signed in, lets get started with some Security Shepherd modules! To start a module, click on the list headers, such as <span><a>Field Training</a></span> or <span><a>Corporal</a></span>, in the side menu to see what modules are currently available! The lower down in the side menu that the module is listed, the more points it is worth! \r\n");
      out.write("\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t");
 if(isAdmin) {
      out.write("\r\n");
      out.write("\t\t\t<h2 class=\"title\">Configure Shepherd</h2>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\tAs you are an Administrator you can define the scope of Security Shepherd. You can quickly change the game layout to Web or Mobile Application focused with the following buttons, or you can open the Admin Module Management menu, at the top left of the page, to open or close specific topics.\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t<div id=\"scopeResultsDiv\" class=\"informationBox\" style=\"display: none;\"></div>\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t<div id=\"setScopeDiv\">\r\n");
      out.write("\t\t\t\t<a href=\"javascript:;\" style=\"text-decoration: none;\" id=\"allApplication\"><div class=\"menuButton\">Open All Levels</div></a>\r\n");
      out.write("\t\t\t\t<a href=\"javascript:;\" style=\"text-decoration: none;\" id=\"onlyWebApplication\"><div class=\"menuButton\">Open Web App Levels Only</div></a>\r\n");
      out.write("\t\t\t\t<a href=\"javascript:;\" style=\"text-decoration: none;\" id=\"onlyMobileApplication\"><div class=\"menuButton\">Open Mobile Levels Only</div></a>\r\n");
      out.write("\t\t\t\t<a href=\"javascript:;\" style=\"text-decoration: none;\" id=\"noApplication\"><div class=\"menuButton\">Close All Levels</div></a>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div id=\"scopeLoadingDiv\" style=\"display: none;\">Loading...</div>\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t<br/><br/>\r\n");
      out.write("\t\t\tIf you cannot see the message below this paragraph, please ensure that the Security Shepherd instance is correctly configured.</a>.\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<br/>\r\n");
      out.write("\t<div id=\"cantSee\">\r\n");
      out.write("\t\t\r\n");
      out.write("\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<script>\r\n");
      out.write("\t$('#getStarted').slideDown(\"slow\");\r\n");
      out.write("\t$('#cantSee').html(\"<iframe style='width: 685px; height: 600px;' frameborder='no' id='theStart' src='readyToPlay.jsp?ThreadSequenceId=");
      out.print(encoder.encodeForHTMLAttribute(encoder.encodeForURL(threadId)));
      out.write("'></iframe>\");\r\n");
      out.write("\t$('#cantSee').html(function(){\r\n");
      out.write("\t\t$(\"#theStart\").load(function(){\r\n");
      out.write("\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\t");
 if (isAdmin) { 
      out.write("\r\n");
      out.write("\t$(\"#allApplication\").click(function(){\r\n");
      out.write("\t\t$(\"#scopeResultsDiv\").slideUp(\"slow\");\r\n");
      out.write("\t\t$(\"#scopeLoadingDiv\").show(\"slow\");\r\n");
      out.write("\t\t$(\"#setScopeDiv\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\turl: \"openEveryModules\",\r\n");
      out.write("\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t},\r\n");
      out.write("\t\t\t\tasync: false\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\telse\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(\"<br/><p> Config Failed!: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p><br/>\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\t$(\"#scopeLoadingDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t$(\"#setScopeDiv\").slideDown(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#scopeResultsDiv\").show (\"fast\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t$(\"html, body\").animate({ scrollTop: 0 }, \"fast\");\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#onlyWebApplication\").click(function(){\r\n");
      out.write("\t\t$(\"#scopeResultsDiv\").slideUp(\"slow\");\r\n");
      out.write("\t\t$(\"#scopeLoadingDiv\").show(\"slow\");\r\n");
      out.write("\t\t$(\"#setScopeDiv\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\turl: \"openWebModules\",\r\n");
      out.write("\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t},\r\n");
      out.write("\t\t\t\tasync: false\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\telse\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(\"<br/><p> Config Failed!: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p><br/>\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\t$(\"#scopeLoadingDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t$(\"#setScopeDiv\").slideDown(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#scopeResultsDiv\").show (\"fast\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t$(\"html, body\").animate({ scrollTop: 0 }, \"fast\");\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#onlyMobileApplication\").click(function(){\r\n");
      out.write("\t\t$(\"#scopeResultsDiv\").slideUp(\"slow\");\r\n");
      out.write("\t\t$(\"#scopeLoadingDiv\").show(\"slow\");\r\n");
      out.write("\t\t$(\"#setScopeDiv\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\turl: \"openMobileModules\",\r\n");
      out.write("\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t},\r\n");
      out.write("\t\t\t\tasync: false\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\telse\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(\"<br/><p> Config Failed!: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p><br/>\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\t$(\"#scopeLoadingDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t$(\"#setScopeDiv\").slideDown(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#scopeResultsDiv\").show (\"fast\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t$(\"html, body\").animate({ scrollTop: 0 }, \"fast\");\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#noApplication\").click(function(){\r\n");
      out.write("\t\t$(\"#scopeResultsDiv\").slideUp(\"slow\");\r\n");
      out.write("\t\t$(\"#scopeLoadingDiv\").show(\"slow\");\r\n");
      out.write("\t\t$(\"#setScopeDiv\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\turl: \"closeEveryModules\",\r\n");
      out.write("\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t},\r\n");
      out.write("\t\t\t\tasync: false\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\telse\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$('#scopeResultsDiv').html(\"<br/><p> Config Failed!: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p><br/>\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\t$(\"#scopeLoadingDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t$(\"#setScopeDiv\").slideDown(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#scopeResultsDiv\").show (\"fast\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t$(\"html, body\").animate({ scrollTop: 0 }, \"fast\");\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\t");
 } // End of Admin Only Script
      out.write("\r\n");
      out.write("\t</script>\r\n");
      out.write("\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write('\r');
      out.write('\n');
      out.write('	');

}
else	//IF the  user doesnt need to change their pass, just post up the get Started message
{
	
      out.write("\r\n");
      out.write("\t<div class=\"errorWrapper\">\r\n");
      out.write("\t\tYour password is a temporary password. This means that somebody else knows it! Lets keep things secure and change your password now!\r\n");
      out.write("\t\t<br /><br />\r\n");
      out.write("\t\t<div class=\"errorMessage\">\r\n");
      out.write("\t\t\t<form id=\"changePassword\" method=\"POST\" action=\"passwordChange\">\r\n");
      out.write("\t\t\t<table align=\"center\">\r\n");
      out.write("\t\t\t\t<tr><td>Current Password:</td><td><input type=\"password\" name=\"currentPassword\" /></td></tr>\r\n");
      out.write("\t\t\t\t<tr><td>New Password:</td><td><input type=\"password\" name=\"newPassword\" /></td></tr>\r\n");
      out.write("\t\t\t\t<tr><td>Password Confirmation:</td><td><input type=\"password\" name=\"passwordConfirmation\" /></td></tr>\r\n");
      out.write("\t\t\t\t<tr><td colspan=\"2\"><center><input type=\"submit\" id=\"changePasswordSubmit\" value = \"Change Password\"/></center></td></tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t<input type=\"hidden\" name=\"csrfToken\" value=\"");
      out.print(csrfToken);
      out.write("\" />\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t");

}
}
else
{
response.sendRedirect("login.jsp");
}
}
else
{
response.sendRedirect("login.jsp");
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
