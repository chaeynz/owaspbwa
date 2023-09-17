package org.apache.jsp.active.xss;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.http.Cookie;
import java.util.*;

public final class xss_002dcookie_002dbasic_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\n");
      out.write("<!--\n");
      out.write("    This file is part of the OWASP Zed Attack Proxy (ZAP) project (http://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project)\n");
      out.write("    ZAP is an HTTP/HTTPS proxy for assessing web application security.\n");
      out.write("    \n");
      out.write("    Author: psiinon@gmail.com\n");
      out.write("    \n");
      out.write("    Licensed under the Apache License, Version 2.0 (the \"License\"); \n");
      out.write("    you may not use this file except in compliance with the License. \n");
      out.write("    You may obtain a copy of the License at \n");
      out.write("    \n");
      out.write("      http://www.apache.org/licenses/LICENSE-2.0 \n");
      out.write("      \n");
      out.write("    Unless required by applicable law or agreed to in writing, software \n");
      out.write("    distributed under the License is distributed on an \"AS IS\" BASIS, \n");
      out.write("    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. \n");
      out.write("    See the License for the specific language governing permissions and \n");
      out.write("    limitations under the License. \n");
      out.write("-->\n");
      out.write("\n");
      out.write("\n");
      out.write("<head>\n");
      out.write("<title>OWASP ZAP WAVE - XSS Form basic</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<H2>OWASP ZAP WAVE - Simple XSS in a Form parameter</H2>\n");
      out.write("<H3>Description</H3>\n");
      out.write("The 'zap-xss-cookie-basic' cookie is written to a the page without being sanitised and so is vulnerable to a simple script injection attack.<br>\n");
      out.write("The cookie can be set via the 'name' parameter with a POST, view the page again to see its value.\n");
      out.write("<H3>Example</H3>\n");

	// Standard bit of code to ensure any session ID is protected using HTTPOnly
	String sessionid = request.getSession().getId();
	if (sessionid != null && sessionid.length() > 0) {
		response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; HttpOnly");
	}

	// Set the 'zap-xss-cookie-basic' to the name parameter (if suppplied)
	String cookieName = "zap-xss-cookie-basic";
	String name = request.getParameter("name");
	if (request.getMethod().equals("POST") && name != null) {
	/*
		Date now = new Date();
		String timestamp = now.toString();
		Cookie cookie = new Cookie (cookieName,name);
		cookie.setMaxAge(365 * 24 * 60 * 60);
		cookie
		response.addCookie(cookie);
		*/
		response.setHeader("Set-Cookie", cookieName + "=" + name + "; HTTPOnly");
	}

	// Display the cookie, if supplied
	Cookie cookies [] = request.getCookies ();
	Cookie myCookie = null;
	if (cookies != null) {
		for (int i = 0; i < cookies.length; i++) {
			if (cookies [i].getName().equals (cookieName)) {
				myCookie = cookies[i];
				out.println("The cookie '" + cookieName + "' is " + myCookie.getValue() + "<br/><br/>");
				break;
			}
		}
	}

      out.write("\n");
      out.write("\n");
      out.write("<form method=\"POST\">\n");
      out.write("\t<table>\n");
      out.write("\t<tr>\n");
      out.write("\t<td>Name:</td>\n");
      out.write("\t<td><input id=\"name\" name=\"name\"></input></td>\n");
      out.write("\t</tr>\n");
      out.write("\t<td></td><td><input id=\"submit\" type=\"submit\" value=\"Submit\"></input></td>\n");
      out.write("\t</tr>\n");
      out.write("\t</table>\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("\n");
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
