package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import utils.ShepherdLogManager;
import utils.*;

public final class readyToPlay_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 * This file assigns the tracking cookie for the exposed server
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
 String levelName = new String("ReadyToPlay.jsp");
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
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName +": tokenCookie Error:" + htmlE.toString());
 	}
 	// validateSession ensures a valid session, and valid role credentials
 	// If tokenCookie == null, then the page is not going to continue loading
 	if (Validate.validateSession(ses) && tokenCookie != null)
 	{
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName + " has been accessed by " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
		Encoder encoder = ESAPI.encoder();
		String parameter = (String)request.getParameter("ThreadSequenceId");
		try
		{
			ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Thread Parameter = " + parameter, ses.getAttribute("userName"));
			Cookie cookie = new Cookie("JSESSIONID3", parameter);
			if(request.getRequestURL().toString().startsWith("https"))//If Requested over HTTPs
			cookie.setSecure(true);
		    ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Thread Cookie Value = " + cookie.getValue(), ses.getAttribute("userName"));
		    response.addCookie(cookie);
		}
		catch(Exception e)
		{
			ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Thread Parameter caused Failure: " + parameter);
			parameter = "";
		}

      out.write("\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("\t<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("\t<title>Security Shepherd - Ready to Go?</title>\r\n");
      out.write("\t<link href=\"../css/lessonCss/theCss.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<script type=\"text/javascript\" src=\"../js/jquery.js\"></script>\r\n");
      out.write("\t\t<div id=\"contentDiv\">\t\t\r\n");
      out.write("\t\t\t");
 if(parameter.isEmpty()) { 
      out.write("\r\n");
      out.write("\t\t\t<h2 class=\"title\">You are not ready!</h2>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t\tRefresh the home page! If this error persists; Log out and back in! If this error continues to persist, please contact an Administrator!\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t<h2 class=\"title\">You have entered the game!</h2>\r\n");
      out.write("\t\t\t<p> \r\n");
      out.write("\t\t\t\tNow that you can see this, you're good to go! Get cracking on lessons and challenges! \r\n");
      out.write("\t\t\t\t<br/><br/>\r\n");
      out.write("\t\t\t\tRemember, the levels you are playing are sub applications. Keep the game play in these applications! \r\n");
      out.write("\t\t\t\tStay away from your session ID's! You'll just log yourself out of you change them!\r\n");
      out.write("\t\t\t\t<br/><br/>\r\n");
      out.write("\t\t\t\tIf you havn't already configured a web proxy, you better! It makes things much easier!\r\n");
      out.write("\t\t\t</p>\r\n");
      out.write("\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t<h2 class=\"title\">Project Sponsors</h2>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\tThe OWASP Security Shepherd project would like to acknowledge and thank the generous support of our sponsors. \r\n");
      out.write("\t\t\tPlease be certain to visit their <a href=\"http://bit.ly/BccRiskAdvisorySite\">site</a> \r\n");
      out.write("\t\t\tas well as follow them on <a href=\"http://bit.ly/bccRiskAdvisory\">twitter</a>.\r\n");
      out.write("\t\t\t<br/><br/>\r\n");
      out.write("\t\t\t<a href=\"http://bit.ly/BccRiskAdvisorySite\"><img src=\"css/images/bccRiskAdvisorySmallLogo.jpg\" alt=\"BCC Risk Advisory\"/></a>\r\n");
      out.write("\t\t\t<a href=\"http://bit.ly/EdgeScan\"><img src=\"css/images/edgescanSmallLogo.jpg\" alt=\"EdgeScan\" /></a>\r\n");
      out.write("\t\t\t<br/><br/>\r\n");
      out.write("\t\t\tThe OWASP Security Shepherd Project would also like to thank Dr. Anthony Keane and the ITB Security Research Lab for hosting the public http://owasp.securityShepherd.eu!  \r\n");
      out.write("\t\t\t<br/><a href=\"http://securityresearch.ie/\"><img src=\"https://www.owasp.org/images/thumb/2/24/Fontlogo.png/300px-Fontlogo.png\"/></a></p>\r\n");
      out.write("\t\t\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write("\r\n");
      out.write("\t\t</div>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
 
	}
	else
	{
		response.sendRedirect("loggedOutSheep.html");
	}
}
else
{
	response.sendRedirect("loggedOutSheep.html");
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
