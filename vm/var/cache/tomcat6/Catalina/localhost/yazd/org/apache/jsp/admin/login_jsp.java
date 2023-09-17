package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;

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
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: login.jsp,v $
 *	$Revision: 1.3 $
 *	$Date: 2000/12/18 21:37:08 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      com.Yasna.forum.util.admin.AdminBean adminBean = null;
      synchronized (session) {
        adminBean = (com.Yasna.forum.util.admin.AdminBean) _jspx_page_context.getAttribute("adminBean", PageContext.SESSION_SCOPE);
        if (adminBean == null){
          adminBean = new com.Yasna.forum.util.admin.AdminBean();
          _jspx_page_context.setAttribute("adminBean", adminBean, PageContext.SESSION_SCOPE);
        }
      }
      out.write("\r\n");
      out.write("\t\t\t\r\n");
 try { 
      out.write("\r\n");
      out.write("\t\t\t\t \r\n");
	// get parameters
	String username = ParamUtils.getParameter(request,"username");
	String password = ParamUtils.getParameter(request,"password",true);
	String redirect = ParamUtils.getParameter(request,"redirect");
	boolean doLogin = ParamUtils.getBooleanParameter(request,"doLogin");

      out.write("\r\n");
      out.write("\r\n");
	// check redirect string
	if( redirect == null ) {
		redirect = "/";
	}

      out.write("\r\n");
      out.write("\r\n");
	String errorMessage = "";
	if( doLogin ) {
		//AuthorizationFactory authFactory = AuthorizationFactory.getInstance();
		try {
			Authorization authToken = AuthorizationFactory.getAuthorization( username, password );
			boolean isSystemAdmin = SkinUtils.isSystemAdmin(authToken);
			boolean isForumAdmin = SkinUtils.isForumAdmin(authToken);
			boolean isGroupAdmin = SkinUtils.isGroupAdmin(authToken);
			boolean isModerator = SkinUtils.isForumModerator(authToken);
			// set admin booleans in session:
			session.putValue("yazdAdmin.systemAdmin",new Boolean(isSystemAdmin));
			session.putValue("yazdAdmin.forumAdmin",new Boolean(isForumAdmin));
			session.putValue("yazdAdmin.groupAdmin",new Boolean(isGroupAdmin));
			session.putValue("yazdAdmin.Moderator",new Boolean(isModerator));
			if( isSystemAdmin || isGroupAdmin || isModerator ) {
				adminBean.setAuthToken( authToken );
				response.sendRedirect(redirect);
				return;
			}
		}
		catch( UnauthorizedException ue ) {
			errorMessage = "Login failed: Make sure your username and password "
			+ "are correct and that you are authorized to use this tool.";
		}
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>Yazd Admin Login</title>\r\n");
      out.write("\t<style type=\"text/css\">\r\n");
      out.write("\t.label, .error {\r\n");
      out.write("\t\tfont-family : verdana,arial,helvetica,sans-serif;\r\n");
      out.write("\t\tfont-size : 10pt;\r\n");
      out.write("\t}\r\n");
      out.write("\t.error {\r\n");
      out.write("\t\tcolor : #ff0000;\r\n");
      out.write("\t}\r\n");
      out.write("\t</style>\r\n");
      out.write("\t<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("\t\t<!-- \r\n");
      out.write("\t\t// break out of frames\r\n");
      out.write("\t\tif (self.parent.frames.length != 0) {\r\n");
      out.write("\t\t\tself.parent.location=document.location;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t// go to help page\r\n");
      out.write("\t\tfunction help() {\r\n");
      out.write("\t\t\tlocation.href = 'help.jsp';\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t//-->\r\n");
      out.write("\t</script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form action=\"login.jsp\" name=\"loginForm\" method=\"post\">\r\n");
      out.write("<input type=\"hidden\" name=\"doLogin\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"redirect\" value=\"index.jsp\">\r\n");
      out.write("\r\n");
      out.write("<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("<td width=\"49%\"><br></td>\r\n");
      out.write("<td width=\"2%\">\t\r\n");
      out.write("\t<noscript>\r\n");
      out.write("\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t<td class=\"error\"><b>Error:</b> You don't have JavaScript enabled. This tool uses JavaScript\r\n");
      out.write("\t\tand much of it will not work correctly without it enabled. Please turn\r\n");
      out.write("\t\tJavaScript back on and reload this page.\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<br><br><br><br>\r\n");
      out.write("\t</noscript>\r\n");
      out.write("\t\r\n");
      out.write("\t<span class=\"error\">");
      out.print( errorMessage );
      out.write("</span>\r\n");
      out.write("\t<p>\r\n");
      out.write("\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t<tr><td background=\"images/loginbacktop.gif\" colspan=\"4\" width=\"100%\"><img src=\"images/blank.gif\" width=\"250\" height=\"30\" border=\"0\"></td></tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td rowspan=\"7\" background=\"images/lside.gif\"><img src=\"images/blank.gif\" width=\"10\" height=\"1\" border=\"0\"></td>\r\n");
      out.write("\t\t<td colspan=\"2\"><img src=\"images/blank.gif\" width=\"230\" height=\"10\" border=\"0\"></td>\r\n");
      out.write("\t\t<td rowspan=\"7\" background=\"images/rside.gif\"><img src=\"images/blank.gif\" width=\"10\" height=\"1\" border=\"0\"></td>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td align=\"right\" nowrap class=\"label\">username &nbsp;</td>\r\n");
      out.write("\t\t<td><input type=\"text\" name=\"username\" size=\"15\" maxlength=\"25\"></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr><td colspan=\"2\"><img src=\"images/blank.gif\" width=\"230\" height=\"5\" border=\"0\"></td></tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td align=\"right\" nowrap class=\"label\">password &nbsp;</td>\r\n");
      out.write("\t\t<td><input type=\"password\" name=\"password\" size=\"15\" maxlength=\"20\"></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr><td colspan=\"2\"><img src=\"images/blank.gif\" width=\"1\" height=\"5\" border=\"0\"></td></tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td colspan=\"2\" align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"submit\" value=\"Login\">\r\n");
      out.write("\t\t\t&nbsp;\r\n");
      out.write("\t\t\t<input type=\"submit\" value=\"Help\" onclick=\"help(); return false;\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td colspan=\"2\"><img src=\"images/blank.gif\" width=\"230\" height=\"10\" border=\"0\"></td></tr>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr><td background=\"images/loginbacktop2.gif\" colspan=\"4\" width=\"100%\"><img src=\"images/blank.gif\" width=\"250\" height=\"10\" border=\"0\"></td></tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("</td>\r\n");
      out.write("<td width=\"49%\"><br></td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("<!--\r\n");
      out.write("\tdocument.loginForm.username.focus();\r\n");
      out.write("//-->\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
 } catch( Exception e ) {
		System.err.println(e);
		e.printStackTrace();
	}

      out.write("\r\n");
      out.write("\r\n");
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
