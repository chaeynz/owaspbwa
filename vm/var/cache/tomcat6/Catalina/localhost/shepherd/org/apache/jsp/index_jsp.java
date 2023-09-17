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

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
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

	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG: index.jsp *************************");

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
		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(index.jsp): tokenCookie Error:" + htmlE.toString());
	}
	// validateSession ensures a valid session, and valid role credentials
	// Also, if tokenCookie != null, then the page is good to continue loading
	if (Validate.validateSession(ses) && tokenCookie != null)
	{
		//Log User Name
		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Accessed by: " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
		// Getting Session Variables
		//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
		Encoder encoder = ESAPI.encoder();
		String csrfToken = encoder.encodeForHTML(tokenCookie.getValue());
		String userName = encoder.encodeForHTML(ses.getAttribute("userName").toString());
		String userRole = encoder.encodeForHTML(ses.getAttribute("userRole").toString());
		String userId = encoder.encodeForJavaScript(ses.getAttribute("userStamp").toString());
		boolean canSeeScoreboard = ScoreboardStatus.canSeeScoreboard((String)ses.getAttribute("userRole"));
		String csrfJsToken = encoder.encodeForJavaScript(tokenCookie.getValue());
		String ApplicationRoot = getServletContext().getRealPath("");
		boolean showCheatSheet = CheatSheetStatus.showCheat(userRole);
		int i = 0;

      out.write("\r\n");
      out.write("\t\t<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("\t\t<head>\r\n");
      out.write("\t\t<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("\t\t<title>OWASP Security Shepherd</title>\r\n");
      out.write("\r\n");
      out.write("\t\t<!-- You are currently looking at the core server. \r\n");
      out.write("\t\t\tNothing related to the levels in Security Shepherd will be found in here. \r\n");
      out.write("\t\t\tYou might be looking for the iframe embeded in the page.\r\n");
      out.write("\t\t\tTry a tool like Firebug to make this stuff easier.\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\tSecurity Shepherd Version: 2.4 -->\r\n");
      out.write("\r\n");
      out.write("\t\t<link href=\"css/theCss.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("\t\t<link rel=\"shortcut icon\" href=\"css/images/flavicon.jpg\" type=\"image/jpeg\" />\r\n");
      out.write("\r\n");
      out.write("\t\t</head>\r\n");
      out.write("\t\t<body>\r\n");
      out.write("\t\t<script type=\"text/javascript\" src=\"js/jquery.js\"></script>\r\n");
      out.write("\t\t<div id=\"wrapper\">\r\n");
      out.write("\t\t<div id=\"header\">\r\n");
      out.write("\t\t\t<h1>Security Shepherd</h1>\r\n");
      out.write("\t\t\t<div style=\"position: absolute;right: 100px;\">\r\n");
      out.write("\t\t\t\t<p>\r\n");
      out.write("\t\t\t\t\t<strong>Welcome ");
      out.print( userName );
      out.write("</strong><br />\r\n");
      out.write("\t\t\t\t\t<strong><a href=\"logout?csrfToken=");
      out.print( csrfToken );
      out.write("\">Logout</a></strong>\r\n");
      out.write("\t\t\t\t</p>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<div id=\"page\">\r\n");
      out.write("\t\t\t<div id=\"submitResult\" style=\"display: none; float: right;, width: 800px;\">\r\n");
      out.write("\t\t\t\t<form action=\"javascript:;\" id=\"resultForm\" >\r\n");
      out.write("\t\t\t\t<input type=\"hidden\" id=\"currentModule\" value=\"\">\r\n");
      out.write("\t\t\t\t\t<table>\r\n");
      out.write("\t\t\t\t\t<tr><td>\r\n");
      out.write("\t\t\t\t\t\tSubmit Result Key:</td><td> <input style=\"width: 470px;\" type=\"text\" id=\"moduleResult\" value=\"\" autocomplete=\"OFF\"/></td><td><div id=\"submitForm\"><input type=\"submit\" value=\"Submit\"/></div>\r\n");
      out.write("\t\t\t\t\t\t<div id=\"submitLoading\" style=\"display: none;\">Loading... </div>\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\t<div id=\"resultResponse\"></div>\r\n");
      out.write("\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t");
 if(showCheatSheet) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<div id=\"solutionDiv\" style=\"display:none;\"></div>\r\n");
      out.write("\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<!-- You are currently looking at the core server. \r\n");
      out.write("\t\t\tNothing related to the levels in Security Shepherd will be found in here. \r\n");
      out.write("\t\t\tYou might be looking for the iframe embeded in the page.\r\n");
      out.write("\t\t\tTry a tool like Firebug to make this stuff easier. -->\r\n");
      out.write("\t\t\t<div id=\"contentDiv\">\r\n");
      out.write("\t\t\t\t<!-- Ajax Div -->\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div id=\"sidebar\">\r\n");
      out.write("\t\t\t\t<ul>\r\n");
      out.write("\t\t\t\t\t");
 if (userRole.compareTo("admin") == 0){ 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t <a id=\"adminList\" href=\"javascript:;\"><div class='menuButton'>Admin</div></a>\r\n");
      out.write("\t\t\t\t\t\t\t<ul id=\"theAdminList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<a id=\"cheatSheetManagementList\" href=\"javascript:;\">Cheat Sheet Management</a>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<ul id=\"theCheatSheetManagementList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"createCheatsLink\" href=\"javascript:;\">Create New Cheat Sheet</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"disableCheatsLink\" href=\"javascript:;\">Disable Cheat Sheets</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"enableCheatsLink\" href=\"javascript:;\">Enable Cheat Sheets</a></li>\t\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<a id=\"configurationList\" href=\"javascript:;\">Configuration</a>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<ul id=\"theConfigurationList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"aboutShepherdLink\" href=\"javascript:;\">About Security Shepherd</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"levelLayoutLink\" href=\"javascript:;\">Change Module Layout</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"configureFeedbackLink\" href=\"javascript:;\">Feedback Configuration</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"registrationLink\" href=\"javascript:;\">Open/Close Registration</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"scoreboardLink\" href=\"javascript:;\">Scoreboard Configuration</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"setCoreDatabaseLink\" href=\"javascript:;\">Set Core Database</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<a id=\"moduleManagementList\" href=\"javascript:;\">Module Management</a>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<ul id=\"theModuleManagementList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"moduleBlockLink\" href=\"javascript:;\">Module Block Setup</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"setModuleStatusLink\" href=\"javascript:;\">Open and Close Modules</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"openCloseByCategory\" href=\"javascript:;\">Open or Close by Category</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"feedbackLink\" href=\"javascript:;\">View Feedback</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"progressLink\" href=\"javascript:;\">View Progress</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<a id=\"userManagementList\" href=\"javascript:;\">User Management</a>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<ul id=\"theUserManagementList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"addPlayersLink\" href=\"javascript:;\">Add Players</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"updatePlayerScoreLink\" href=\"javascript:;\">Add / Deduct Player Points</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"assignPlayersLink\" href=\"javascript:;\">Assign Players to Class</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"createNewClassLink\" href=\"javascript:;\">Create Class</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"createNewAdminLink\" href=\"javascript:;\">Create New Admin</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"changePlayerPasswordLink\" href=\"javascript:;\">Reset Password</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"setDefaultClassForRegistrationLink\" href=\"javascript:;\">Set Default Player Class</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"suspendPlayerLink\" href=\"javascript:;\">Suspend Player</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"unSuspendPlayerLink\" href=\"javascript:;\">Unsuspend Player</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<li><a id=\"upgradePlayersLink\" href=\"javascript:;\">Upgrade Player to Admin</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
 if(canSeeScoreboard) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<div id=\"scoreboardButton\">\r\n");
      out.write("\t\t\t\t\t\t<a id=\"showScoreboard\" href=\"scoreboard.jsp\" target=\"bar\"><div class=\"menuButton\">Scoreboard</div></a>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
 if(showCheatSheet) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<div id=\"cheatSheetButton\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t<a id=\"showSolution\" href=\"javascript:;\"><div class=\"menuButton\">Cheat</div></a>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
 if(ModulePlan.isOpenFloor()) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t<a id=\"lessonList\" href=\"javascript:;\"><div class=\"menuButton\">Lessons</div></a>\r\n");
      out.write("\t\t\t\t\t\t\t<ul id=\"theLessonList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t");
      out.print( Getter.getLessons(ApplicationRoot, (String)ses.getAttribute("userStamp")) );
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t<a id=\"challengeList\" href=\"javascript:;\"><div class=\"menuButton\">Challenges</div></a>\r\n");
      out.write("\t\t\t\t\t\t\t<ul id=\"theChallengeList\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t\t\t\t");
      out.print( Getter.getChallenges(ApplicationRoot, (String)ses.getAttribute("userStamp")) );
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t");
 } else {
						if(ModulePlan.isIncrementalFloor()){ 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t<div id=\"sideMenuWrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t\t");
      out.print( Getter.getIncrementalModules(ApplicationRoot, (String)ses.getAttribute("userStamp"), csrfToken) );
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t");
 } else {
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<li>\r\n");
      out.write("\t\t\t\t\t\t\t");
      out.print( Getter.getTournamentModules(ApplicationRoot, (String)ses.getAttribute("userStamp")) );
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</li>\r\n");
      out.write("\t\t\t\t\t\t");
 }
					} //End of Module List Output 
      out.write("\r\n");
      out.write("\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t<!-- You are currently looking at the core server. \r\n");
      out.write("\t\t\t\tNothing related to the levels in Security Shepherd will be found in here. \r\n");
      out.write("\t\t\t\tYou might be looking for the iframe embeded in the page.\r\n");
      out.write("\t\t\t\tTry a tool like Firebug to make this stuff easier. -->\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<script src=\"js/toggle.js\"></script>\r\n");
      out.write("\t\t<script src=\"js/ajaxCalls.js\"></script>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t");
 //Hide UI Scripts from Users (Blocked at session level anyway, just stops spiders finding the links)
		if (userRole.compareTo("admin") == 0){ 
      out.write("\r\n");
      out.write("\t\t\t<script src=\"js/adminToggle.js\"></script>\r\n");
      out.write("\t\t\t<script>\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#updatePlayerScoreLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/givePoints.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#suspendPlayerLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/suspendUser.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#unSuspendPlayerLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/unSuspendUser.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#changePlayerPasswordLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/changeUserPassword.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#createNewAdminLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/createNewAdmin.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#createNewClassLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/createNewClass.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#addPlayersLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/addPlayers.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#assignPlayersLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/assignPlayers.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#setDefaultClassForRegistrationLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/setDefaultClassForRegistration.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#upgradePlayersLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/userManagement/upgradePlayers.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#enableCheatsLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/cheatManagement/enableCheats.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#disableCheatsLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/cheatManagement/disableCheats.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#createCheatsLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/cheatManagement/createCheat.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#feedbackLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/moduleManagement/feedback.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#setModuleStatusLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/moduleManagement/setStatus.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#openCloseByCategory\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/moduleManagement/openCloseByCategory.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#registrationLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/config/updateRegistration.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#progressLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/moduleManagement/classProgress.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#scoreboardLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/config/scoreboard.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#moduleBlockLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/moduleManagement/moduleBlock.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#setCoreDatabaseLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/config/setCoreDatabase.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t$(\"#configureFeedbackLink\").click(function(){\r\n");
      out.write("\t\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/config/configFeedback.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#aboutShepherdLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/config/aboutShepherd.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#levelLayoutLink\").click(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitResult\").slideUp(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#contentDiv\").load(\"admin/config/changeLevelLayout.jsp?csrfToken=");
      out.print( csrfJsToken );
      out.write("\", function(response, status, xhr) {\r\n");
      out.write("\t\t\t\t\t\t  if (status == \"error\") {\r\n");
      out.write("\t\t\t\t\t\t\tvar msg = \"Sorry but there was an error: \";\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").html(\"<p>\" + msg + xhr.status + \" \" + xhr.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t  $(\"#contentDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t</script>\r\n");
      out.write("\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<script>\r\n");
      out.write("\t\t$(\".challenge\").click(function(){\r\n");
      out.write("\t\t\tvar whatFile = $(this).attr('id');\r\n");
      out.write("\t\t\t$(\"#currentModule\").val(whatFile);\r\n");
      out.write("\t\t\tvar theActualFile = \"\";\r\n");
      out.write("\t\t\t$(\"#solutionDiv\").hide(\"fast\");\r\n");
      out.write("\t\t\t$(\"#contentDiv\").slideUp(\"slow\", function(){\r\n");
      out.write("\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\turl: \"getModule\",\r\n");
      out.write("\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\tmoduleId: whatFile,\r\n");
      out.write("\t\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\ttheActualFile = ajaxCall.responseText;\r\n");
      out.write("\t\t\t\t\t$('#contentDiv').html(\"<iframe frameborder='no' style='word-wrap: break-word; width: 685px; height: 2056px;' id='theChallenge' src='\" + theActualFile + \"'></iframe>\");\r\n");
      out.write("\t\t\t\t\t$(\"#theChallenge\").load(function(){\r\n");
      out.write("\t\t\t\t\t\t");
 if(showCheatSheet) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#submitResult\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#cheatSheetButton\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#submitResult\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t}).appendTo('#contentDiv');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\telse\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t$('#contentDiv').html(\"<p> Sorry but there was a challenge error: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t});\t\r\n");
      out.write("\r\n");
      out.write("\t\t$(\".lesson\").click(function(){\r\n");
      out.write("\t\t\tvar whatFile = $(this).attr('id');\r\n");
      out.write("\t\t\t$(\"#currentModule\").val(whatFile);\r\n");
      out.write("\t\t\tvar theActualFile = \"\";\r\n");
      out.write("\t\t\t$(\"#solutionDiv\").hide(\"fast\");\r\n");
      out.write("\t\t\t$(\"#contentDiv\").slideUp(\"slow\", function(){\r\n");
      out.write("\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\turl: \"getModule\",\r\n");
      out.write("\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\tmoduleId: whatFile,\r\n");
      out.write("\t\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\ttheActualFile = ajaxCall.responseText;\r\n");
      out.write("\t\t\t\t\t$('#contentDiv').html(\"<iframe frameborder='no' style='word-wrap: break-word; width: 685px; height: 2056px;' id='theLesson' src='\" + theActualFile + \"'></iframe>\");\r\n");
      out.write("\t\t\t\t\t$(\"#theLesson\").load(function(){\r\n");
      out.write("\t\t\t\t\t");
 if(showCheatSheet) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t$(\"#submitResult\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#cheatSheetButton\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t$(\"#submitResult\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t}).appendTo('#contentDiv');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\telse\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t$('#contentDiv').html(\"<p> Sorry but there was a lesson error: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").slideDown(\"slow\");\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t$(\"#resultForm\").submit(function(){\r\n");
      out.write("\t\t\tvar theKey = $(\"#moduleResult\").val();\r\n");
      out.write("\t\t\tvar theModuleId = $(\"#currentModule\").val();\r\n");
      out.write("\t\t\tif(theKey != null || theKey.length > 5)\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\t$(\"#solutionDiv\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#resultResponse\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#submitForm\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#submitLoading\").show(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#contentDiv\").slideUp(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#moduleResult\").val(\"\");\r\n");
      out.write("\t\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\t\turl: \"solutionSubmit\",\r\n");
      out.write("\t\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\t\tmoduleId: theModuleId,\r\n");
      out.write("\t\t\t\t\t\t\tsolutionKey: theKey,\r\n");
      out.write("\t\t\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t$('#contentDiv').html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\telse\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t$('#resultResponse').html(\"<br/><p> Sorry but there was a result form error: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p><br/>\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#resultResponse\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t$(\"#contentDiv\").slideDown(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#submitLoading\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#submitForm\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\telse\r\n");
      out.write("\t\t\t{\r\n");
      out.write("\t\t\t\talert('Invalid Key');\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t");
 if(showCheatSheet) { 
      out.write("\r\n");
      out.write("\t\t$(\"#showSolution\").click(function(){\r\n");
      out.write("\t\t\t$(\"#solutionDiv\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\tvar theModuleId = $(\"#currentModule\").val();\r\n");
      out.write("\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\turl: \"getCheat\",\r\n");
      out.write("\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\tmoduleId: theModuleId,\r\n");
      out.write("\t\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t$('#solutionDiv').html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\telse\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t$('#solutionDiv').html(\"<p> Sorry but there was a show solution error: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t$(\"#solutionDiv\").show(\"fast\");\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t");
 if(ModulePlan.tornyFloor){
      out.write("\r\n");
      out.write("\t\t\t$(\"#fieldTrainingList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t});  \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#privateList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t}); \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#corporalList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t});  \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#sergeantList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t});  \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#lieutenantList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").hide(\"fast\");\r\n");
      out.write("\t\t\t}); \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#majorList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t}); \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#admiralList\").click(function () {\r\n");
      out.write("\t\t\t\t$(\"#theAdmiralList\").toggle(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#theFieldTrainingList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theCorporalList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theSergeantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theMajorList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#thePrivateList\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#theLieutenantList\").hide(\"fast\");\r\n");
      out.write("\t\t\t}); \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t</script>\r\n");
      out.write("\t\t<!-- You are currently looking at the core server. \r\n");
      out.write("\t\tNothing related to the levels in Security Shepherd will be found in here. \r\n");
      out.write("\t\tYou might be looking for the iframe embeded in the page.\r\n");
      out.write("\t\tTry a tool like Firebug to make this stuff easier. -->\r\n");
      out.write("\t\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write("\r\n");
      out.write("\t\t</body>\r\n");
      out.write("\t\t</html>\r\n");
      out.write("\t\t");

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
