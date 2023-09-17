package org.apache.jsp.lessons;

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

public final class zf8ed52591579339e590e0726c7b24009f3ac54cdff1b81a65db1688d86efb3a_jsp extends org.apache.jasper.runtime.HttpJspBase
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


String levelName = new String("Cross Site Scripting Lesson");

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
		// Getting Session Variables
		//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
		Encoder encoder = ESAPI.encoder();
		String csrfToken = encoder.encodeForHTML(tokenCookie.getValue());

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("\t<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("\t<title>Security Shepherd - Cross Site Scripting Lesson</title>\r\n");
      out.write("\t<link href=\"../css/lessonCss/theCss.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<script type=\"text/javascript\" src=\"../js/jquery.js\"></script>\r\n");
      out.write("\t\t<div id=\"contentDiv\">\r\n");
      out.write("\t\t\t<h2 class=\"title\">What is Cross Site Scripting?</h2>\r\n");
      out.write("\t\t\t<p> \r\n");
      out.write("\t\t\t\t<div id=\"lessonIntro\">\r\n");
      out.write("\t\t\t\t\tCross-Site Scripting, or <a>XSS</a>, issues occur when an application uses <a>untrusted data</a> in a\r\n");
      out.write("\t\t\t\t\tweb browser without sufficient <a>validation</a> or <a>escaping</a>. If untrusted data contains\r\n");
      out.write("\t\t\t\t\ta client side script, the browser will execute the script while it is interpreting the page.\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\tAttackers can use XSS attacks to execute scripts in a victim's browser which can hijack user sessions, \r\n");
      out.write("\t\t\t\t\tdeface web sites, or redirect the user to malicious sites. Anyone that can send data to the system, \r\n");
      out.write("\t\t\t\t\tincluding administrators, are possible candidates for performing XSS attacks in an application. \r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\tAccording to OWASP, XSS is the most widespread vulnerability found in web applications today. \r\n");
      out.write("\t\t\t\t\tThis is partially due to the variety of <a>attack vectors</a> that are available. The easiest way \r\n");
      out.write("\t\t\t\t\tof showing an XSS attack executing is using a simple <a>alert box</a> as a client side script pay load. \r\n");
      out.write("\t\t\t\t\tTo execute a XSS payload, a variety of an attack vectors may be necessary to overcome insufficient escaping \r\n");
      out.write("\t\t\t\t\tor validation. The following are examples of some known attack vectors, that all create the same\r\n");
      out.write("\t\t\t\t\t <a>alert</a> pop up that reads \"XSS\".\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t&lt;SCRIPT&gt;<a>alert(&#39;XSS&#39;)</a>&lt;/SCRIPT&gt;<br />\r\n");
      out.write("\t\t\t\t\t&lt;IMG SRC=&quot;&#x23;&quot; ONERROR=&quot;<a>alert(&#39;XSS&#39;)</a>&quot;/&gt;<br />\r\n");
      out.write("\t\t\t\t\t&lt;INPUT TYPE=&quot;BUTTON&quot; ONCLICK=&quot;<a>alert(&#39;XSS&#39;)</a>&quot;/&gt;<br />\r\n");
      out.write("\t\t\t\t\t&lt;IFRAME SRC=&quot;javascript:<a>alert(&#39;XSS&#39;)</a>;&quot;&gt;&lt;/IFRAME&gt;<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t\t<input type=\"button\" value=\"Hide Lesson Introduction\" id=\"hideLesson\"/>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<input type=\"button\" value=\"Show Lesson Introduction\" id=\"showLesson\"  style=\"display: none;\"/>\r\n");
      out.write("\t\t\t\t<br/>\r\n");
      out.write("\t\t\t\tThe following search box outputs untrusted data without any validation or escaping. \r\n");
      out.write("\t\t\t\tGet an alert box to execute through this function to show that there is an XSS vulnerability present.\r\n");
      out.write("\t\t\t\t<br />\r\n");
      out.write("\t\t\t\t<br />\t\t\t\t\r\n");
      out.write("\t\t\t\t<form id=\"leForm\" action=\"javascript:;\">\r\n");
      out.write("\t\t\t\t\t<table>\r\n");
      out.write("\t\t\t\t\t<tr><td>\r\n");
      out.write("\t\t\t\t\t\tPlease enter the <a>Search Term</a> that you want to look up\r\n");
      out.write("\t\t\t\t\t</td></tr>\r\n");
      out.write("\t\t\t\t\t<tr><td>\r\n");
      out.write("\t\t\t\t\t\t<input style=\"width: 400px;\" id=\"searchTerm\" type=\"text\"/>\r\n");
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
      out.write("\t\t\t$(\"#leForm\").submit(function(){\r\n");
      out.write("\t\t\t\t$(\"#submitButton\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t$(\"#loadingSign\").show(\"slow\");\r\n");
      out.write("\t\t\t\tvar theSearchTerm = $(\"#searchTerm\").val();\r\n");
      out.write("\t\t\t\t$(\"#resultsDiv\").hide(\"slow\", function(){\r\n");
      out.write("\t\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\t\turl: \"zf8ed52591579339e590e0726c7b24009f3ac54cdff1b81a65db1688d86efb3a\",\r\n");
      out.write("\t\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\t\tsearchTerm: theSearchTerm,\r\n");
      out.write("\t\t\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
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
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
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
		response.sendRedirect("../ajaxLogin.jsp");
	}
}
else
{
	response.sendRedirect("../ajaxLogin.jsp");
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
