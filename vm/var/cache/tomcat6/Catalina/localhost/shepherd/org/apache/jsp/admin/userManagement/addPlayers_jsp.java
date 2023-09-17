package org.apache.jsp.admin.userManagement;

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

public final class addPlayers_jsp extends org.apache.jasper.runtime.HttpJspBase
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

	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG: addPlayers.jsp *************************");

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
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(addPlayers.jsp): tokenCookie Error:" + htmlE.toString());
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
String userName = encoder.encodeForHTML(ses.getAttribute("userName").toString());
String userRole = encoder.encodeForHTML(ses.getAttribute("userRole").toString());
String userId = encoder.encodeForHTML(ses.getAttribute("userStamp").toString());
String ApplicationRoot = getServletContext().getRealPath("");
boolean showClasses = false;

ResultSet classList = Getter.getClassInfo(ApplicationRoot);
try
{
	showClasses = classList.next();
}
catch(SQLException e)
{
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Could not open classList: " + e.toString());
	showClasses = false;
}

      out.write("\r\n");
      out.write("\t<div id=\"formDiv\" class=\"post\">\r\n");
      out.write("\t\t<h1 class=\"title\">Add Players</h1>\r\n");
      out.write("\t\t<div id=\"createUserDiv\" class=\"entry\">\r\n");
      out.write("\t\t\t<form id=\"theForm\" action=\"javascript:;\">\r\n");
      out.write("\t\t\t<p>Please select the class you would like to add a player to and input the player information for the player you wish to create.</p>\r\n");
      out.write("\t\t\t<input type=\"hidden\" id=\"csrfToken\" value=\"");
      out.print(csrfToken);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t<table align=\"center\">\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t\t\t<p>Class<font color=\"red\"><small>* </small></font> :</p>\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t\t\t<select id=\"classId\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<option value=\"\"></option>\r\n");
      out.write("\t\t\t\t\t\t\t\t");

									if(showClasses)
									{
										try
										{
											do
											{
												String classId = encoder.encodeForHTMLAttribute(classList.getString(1));
												String classYearName = encoder.encodeForHTML(classList.getString(3)) + " " + encoder.encodeForHTML(classList.getString(2));
												
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value=\"");
      out.print(classId);
      out.write('"');
      out.write('>');
      out.print(classYearName);
      out.write("</option>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t");

											}
											while(classList.next());
										}
										catch(SQLException e)
										{
											ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Error occured when manipulating classList: " + e.toString());
											showClasses = false;
										}
									}
								
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Username<font color=\"red\"><small>* </small></font> :</p></td><td><input type=\"text\" id=\"userName\" value=\"\"/></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Password<font color=\"red\"><small>* </small></font> :</p></td><td><input type=\"password\" id=\"passWord\" /></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Confirm Password<font color=\"red\"><small>* </small></font> :</p></td><td><input type=\"password\" id=\"passWordConfirm\" /></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Email Address:</p></td><td><input type=\"text\" id=\"userAddress\" value=\"\"/></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td><p>Confirm Address:</p></td><td><input type=\"text\" id=\"userAddressCnf\" /></td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td colspan=\"2\" align=\"center\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"submit\" id=\"submitButton\" value=\"Create New Player\"/>\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<div id=\"loadingDiv\" style=\"display:none;\" class=\"menuButton\">Loading...</div>\r\n");
      out.write("\t\t<div id=\"resultDiv\" style=\"display:none;\" class=\"informationBox\"></div>\r\n");
      out.write("\t\t<div id=\"badData\"></div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<script>\r\n");
      out.write("\t$(\"#theForm\").submit(function(){\r\n");
      out.write("\t\t//Get Data\r\n");
      out.write("\t\tvar theClass = $(\"#classId\").val();\r\n");
      out.write("\t\tvar theUserName = $(\"#userName\").attr('value');\r\n");
      out.write("\t\tvar thePassWord = $('#passWord').attr('value');\r\n");
      out.write("\t\tvar thePassWordConfirm = $('#passWordConfirm').attr('value');\r\n");
      out.write("\t\tvar theUserAddress = $('#userAddress').attr('value');\r\n");
      out.write("\t\tvar theUserAddressCnf = $('#userAddressCnf').attr('value');\r\n");
      out.write("\t\tvar theCsrfToken = $('#csrfToken').attr('value');\r\n");
      out.write("\t\t//Validation\r\n");
      out.write("\t\tif (theUserName.length == 0 ||\r\n");
      out.write("\t\t\tthePassWord.length == 0 ||\r\n");
      out.write("\t\t\tthePassWordConfirm.length == 0)\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\t$('#badData').html(\"<p><strong><font color='red'>All required fields must be populated </font></strong></p>\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\telse if(theUserName.length < 5 || theUserName.lenght > 32)\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\t$('#userName').val(\"\");\r\n");
      out.write("\t\t\t$('#userName').css(\"background\", \"#E42217\");\r\n");
      out.write("\t\t\t$('#badData').html(\"<p><strong><font color='red'>Invalid Username. Please try Again.</font></strong></p>\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\telse if(thePassWord.lenght < 5 || thePassWord.lenght > 512 )\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\t$('#passWord').val(\"\");\r\n");
      out.write("\t\t\t$('#passWordConfirm').val(\"\");\r\n");
      out.write("\t\t\t$('#passWord').css(\"background\", \"#E42217\");\r\n");
      out.write("\t\t\t$('#badData').html(\"<p><strong><font color='red'>Invalid password. Please try Again.</font></strong></p>\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\telse if(thePassWord != thePassWordConfirm)\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\t$('#passWord').val(\"\");\r\n");
      out.write("\t\t\t$('#passWordConfirm').val(\"\");\r\n");
      out.write("\t\t\t$('#passWord').css(\"background\", \"#E42217\");\r\n");
      out.write("\t\t\t$('#passWordConfirm').css(\"background\", \"#E42217\");\r\n");
      out.write("\t\t\t$('#badData').html(\"<p><strong><font color='red'>Password fields did not match. Please try Again.</font></strong></p>\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\telse\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\t//Hide&Show Stuff\r\n");
      out.write("\t\t\t$(\"#loadingDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t$(\"#badData\").hide(\"fast\");\r\n");
      out.write("\t\t\t$(\"#resultDiv\").hide(\"fast\");\r\n");
      out.write("\t\t\t$(\"#createUserDiv\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t//The Ajax Operation\r\n");
      out.write("\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\turl: \"addPlayer\",\r\n");
      out.write("\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\tclassId: theClass,\r\n");
      out.write("\t\t\t\t\t\tuserName: theUserName, \r\n");
      out.write("\t\t\t\t\t\tpassWord: thePassWord, \r\n");
      out.write("\t\t\t\t\t\tpassWordConfirm: thePassWordConfirm,\r\n");
      out.write("\t\t\t\t\t\tuserAddress: theUserAddress,\r\n");
      out.write("\t\t\t\t\t\tuserAddressCnf: theUserAddressCnf,\r\n");
      out.write("\t\t\t\t\t\tcsrfToken: theCsrfToken\r\n");
      out.write("\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\t$(\"#loadingDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t//Reset Form\r\n");
      out.write("\t\t\t\t\t\t$(\"#userName\").val('');\r\n");
      out.write("\t\t\t\t\t\t$('#passWord').val('');\r\n");
      out.write("\t\t\t\t\t\t$('#passWordConfirm').val('');\r\n");
      out.write("\t\t\t\t\t\t$('#userAddress').val('');\r\n");
      out.write("\t\t\t\t\t\t$('#userAddressCnf').val('');\r\n");
      out.write("\t\t\t\t\t\t//Now output Result Div and Show\r\n");
      out.write("\t\t\t\t\t\t$(\"#resultDiv\").html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t\t\t\t$(\"#resultDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\telse\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t$(\"#badData\").html(\"<div id='errorAlert'><p> Sorry but there was an error: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p></div>\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#badData\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t$(\"#createUserDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t}\r\n");
      out.write("\t});\r\n");
      out.write("\t</script>\r\n");
      out.write("\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write('\r');
      out.write('\n');
      out.write('	');

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
