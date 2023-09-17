package org.apache.jsp.lessons;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import utils.*;

public final class _911fa7f4232e096d6a74a0623842c4157e29b9bcc44e8a827be3bb7e58c9a212_jsp extends org.apache.jasper.runtime.HttpJspBase
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
	 * @author Sean Duggan
	 */
	 
	//No Quotes In level Name
	String levelName = "What is Mobile Broken Crypto?";
	//Alphanumeric Only
	String levelHash = "911fa7f4232e096d6a74a0623842c4157e29b9bcc44e8a827be3bb7e58c9a212.jsp";
	//Level blurb can be written here in HTML OR go into the HTML body and write it there. Nobody will update this but you
	String levelBlurb = "";

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
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("<title>Security Shepherd - ");
      out.print(levelName);
      out.write("</title>\r\n");
      out.write("<link href=\"../css/lessonCss/theCss.css\" rel=\"stylesheet\" type=\"text/css\"\r\n");
      out.write("\tmedia=\"screen\" />\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<script type=\"text/javascript\" src=\"../js/jquery.js\"></script>\r\n");
      out.write("\t<div id=\"contentDiv\">\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\t<h2 class=\"title\">");
      out.print(levelName );
      out.write("</h2>\r\n");
      out.write("\t\t\t<p> \r\n");
      out.write("\t\t\t\t<div id=\"lessonIntro\">\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\tCryptography is difficult to get right, as a result many Apps use cryptography poorly and become vulnerable to attack. An App can transfer encryption keys insecurely, use a known broken or deprecated cryptographic algorithm or developers can create their own Crypto algorithms.\t\t\t\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\tPoor key management can be due to hard coded keys, keys stored in directories, transferring the key in an insecure way or using the same key all the time. If the developers of an App make use a of custom, unproven, untested encryption algorithm then it is highly likely that the encrypted data is vulnerable.\r\n");
      out.write("\t\t\tFinally\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<input type=\"button\" value=\"Hide Lesson Introduction\" id=\"hideLesson\"/>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<input type=\"button\" value=\"Show Lesson Introduction\" id=\"showLesson\"  style=\"display: none;\"/>\r\n");
      out.write("\t\t\t\t<br/>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t <br/> The developers of this App are holding a competition, whoever can crack their secure chat wins. Unfortunately, the developers have misunderstood the definition of Cryptography. Reduce the intercepted messages exchanged to plain text to reveal the key. <br />\r\n");
      out.write("\t\t\t\t<br/>\r\n");
      out.write("\t\t\t  ");
      out.print( Analytics.getMobileLevelBlurb("BrokenCrypto.apk") );
      out.write("\r\n");
      out.write("\n");
      out.write("\t\t\t\t<script>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t$('#hideLesson').click(function(){\r\n");
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
      out.write("\t\t\t\r\n");
      out.write("\t\t</p>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t");
 if (Analytics.googleAnalyticsOn) { 
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
