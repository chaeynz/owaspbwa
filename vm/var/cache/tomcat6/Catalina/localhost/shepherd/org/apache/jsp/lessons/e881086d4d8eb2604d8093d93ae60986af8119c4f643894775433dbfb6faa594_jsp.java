package org.apache.jsp.lessons;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import utils.*;

public final class e881086d4d8eb2604d8093d93ae60986af8119c4f643894775433dbfb6faa594_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\r');
      out.write('\n');

String levelName =  "SQL Injection Lesson";

/**
 * <br/><br/>
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

 ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName + " Accessed");
 if (request.getSession() != null)
 {
 	HttpSession ses = request.getSession();
 	//Getting CSRF Token from client
 	Cookie tokenCookie = null;
 	try
 	{
 		tokenCookie = Validate.getToken(request.getCookies());
 	}
 	catch(Exception htmlE)
 	{
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName +".jsp: tokenCookie Error:" + htmlE.toString());
 	}
 	// validateSession ensures a valid session, and valid role credentials
 	// If tokenCookie == null, then the page is not going to continue loading
 	if (Validate.validateSession(ses) && tokenCookie != null)
 	{
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName + " has been accessed by " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));

 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("\t<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("\t<title>Security Shepherd - SQL Injection Lesson</title>\r\n");
      out.write("\t<link href=\"../css/lessonCss/theCss.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<script type=\"text/javascript\" src=\"../js/jquery.js\"></script>\r\n");
      out.write("\t\t<div id=\"contentDiv\">\r\n");
      out.write("\t\t\t<h2 class=\"title\">What is SQL Injection?</h2>\r\n");
      out.write("\t\t\t<p> \r\n");
      out.write("\t\t\t\t<div id=\"lessonIntro\">\r\n");
      out.write("\t\t\t\t\tInjection flaws, such as <a>SQL injection</a>, occur when hostile data is sent to an interpreter as part of a command or query. The hostile data can trick the interpreter into executing unintended commands or accessing unauthorized data. Injections attacks are of a high severity. Injection flaws can be exploited to remove a system's confidentiality by accessing any information held on the system. These security risks can then be extended to execute updates to existing data affecting the systems integrity and availability. These attacks are easily exploitable as they can be initiated by anyone who can interact with the system through any data they pass to the application.\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\tThe following form's parameters are concatenated to a string that will be passed to a SQL server. This means that the data can be interpreted as part of the code. \r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\tThe objective here is to modify the result of the query with <a>SQL Injection</a> so that all of the table's rows are returned. This means you want to change the <a>boolean</a> result of the query's <a>WHERE</a> clause to return true for every row in the table. The easiest way to ensure the <a>boolean</a> result is always true is to inject a <a>boolean 'OR'</a> operator followed by a true statement like <a>1 = 1</a>.\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\tIf the parameter is been interpreted as a string, you can escape the string with an apostrophe. That means that everything after the apostrophe will be interpreted as SQL code.\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<input type=\"button\" value=\"Hide Lesson Introduction\" id=\"hideLesson\"/>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<input type=\"button\" value=\"Show Lesson Introduction\" id=\"showLesson\"  style=\"display: none;\"/>\r\n");
      out.write("\t\t\t\t<br/>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\tExploit the <a>SQL Injection</a> flaw in the following example to retrieve all of the rows in the table. The lesson's solution key will be found in one of these rows! The results will be posted beneath the search form.\r\n");
      out.write("\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t<div id=\"hint\" style=\"display: none;\">\r\n");
      out.write("\t\t\t\t\t<h2 class=\"title\">Lesson Hint</h2>\r\n");
      out.write("\t\t\t\t\tThis is the query that you are adding data to. See if you can input something that will cause the <a>WHERE</a> clause to return <a>true</a> for every row in the table. Remember, you can escape a string using an apostrophe.\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<div>SELECT * FROM tb_users WHERE username ='<a id=\"userContent\"></a>';</div>\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t<form id=\"leForm\" action=\"javascript:;\">\r\n");
      out.write("\t\t\t\t\t<table>\r\n");
      out.write("\t\t\t\t\t<tr><td>\r\n");
      out.write("\t\t\t\t\t\tPlease enter the <a>user name</a> of the user that you want to look up\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td>\r\n");
      out.write("\t\t\t\t\t\t<input style=\"width: 400px;\" id=\"aUserName\" type=\"text\"/>\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td>\r\n");
      out.write("\t\t\t\t\t\t<div id=\"submitButton\"><input type=\"submit\" value=\"Get this user\"/></div>\r\n");
      out.write("\t\t\t\t\t\t<p style=\"display: none;\" id=\"loadingSign\">Loading...</p>\r\n");
      out.write("\t\t\t\t\t\t<div style=\"display: none;\" id=\"hintButton\"><input type=\"button\" value=\"Would you like a hint?\" id=\"theHintButton\"/></div>\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t<div id=\"resultsDiv\"></div>\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<script>\r\n");
      out.write("\t\t\tvar counter = 0;\r\n");
      out.write("\t\t\t$(\"#leForm\").submit(function(){\r\n");
      out.write("\t\t\t\tcounter = counter + 1;\r\n");
      out.write("\t\t\t\t$(\"#submitButton\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#loadingSign\").show(\"slow\");\r\n");
      out.write("\t\t\t\t$(\"#userContent\").text($(\"#aUserName\").val());\r\n");
      out.write("\t\t\t\tvar theName = $(\"#aUserName\").val();\r\n");
      out.write("\t\t\t\t$(\"#resultsDiv\").hide(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\t\turl: \"e881086d4d8eb2604d8093d93ae60986af8119c4f643894775433dbfb6faa594\",\r\n");
      out.write("\t\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\t\taUserName: theName\r\n");
      out.write("\t\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\t\tasync: false\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\tif(ajaxCall.status == 200)\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t$(\"#resultsDiv\").html(ajaxCall.responseText);\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\telse\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t$(\"#resultsDiv\").html(\"<p> An Error Occurred: \" + ajaxCall.status + \" \" + ajaxCall.statusText + \"</p>\");\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t$(\"#resultsDiv\").show(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t\t$(\"#loadingSign\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#submitButton\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\tif (counter == 3)\r\n");
      out.write("\t\t\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#hintButton\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#aUserName\").keyup(function () {\r\n");
      out.write("\t\t\t\t$(\"#userContent\").text($(this).val());\r\n");
      out.write("\t\t\t}).keyup();\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#theHintButton\").click(function() {\r\n");
      out.write("\t\t\t\t$(\"#hintButton\").hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#hint\").show(\"fast\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});;\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$('#hideLesson').click(function(){\r\n");
      out.write("\t\t\t\t$(\"#lessonIntro\").hide(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#showLesson\").show(\"fast\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#showLesson\").click(function(){\r\n");
      out.write("\t\t\t\t$('#showLesson').hide(\"fast\", function(){\r\n");
      out.write("\t\t\t\t\t$(\"#lessonIntro\").show(\"slow\");\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t</script>\r\n");
      out.write("\t\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
 
	}
	else
	{
		response.sendRedirect("../loggedOutSheep.html");
	}
}
else
{
	response.sendRedirect("../loggedOutSheep.html");
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
