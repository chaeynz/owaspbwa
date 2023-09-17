package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class password_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/admin/include/pageTitle.jsp");
  }

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
      			"error.jsp", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: password.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/18 21:37:08 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      com.Yasna.forum.util.admin.AdminBean adminBean = null;
      synchronized (session) {
        adminBean = (com.Yasna.forum.util.admin.AdminBean) _jspx_page_context.getAttribute("adminBean", PageContext.SESSION_SCOPE);
        if (adminBean == null){
          adminBean = new com.Yasna.forum.util.admin.AdminBean();
          _jspx_page_context.setAttribute("adminBean", adminBean, PageContext.SESSION_SCOPE);
        }
      }
      out.write("\r\n");
      out.write(" \r\n");
	////////////////////////////////
	// Yazd authorization check
	
	// check the bean for the existence of an authorization token.
	// Its existence proves the user is valid. If it's not found, redirect
	// to the login page
	Authorization authToken = adminBean.getAuthToken();
	if( authToken == null ) {
		response.sendRedirect( "login.jsp" );
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// Security check
	
	// make sure the user is authorized to create forums::
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isSystemAdmin ) {
		throw new UnauthorizedException("Sorry, you don't have permission to change passwords");
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// get parameters
	
	boolean doChange = ParamUtils.getBooleanParameter(request,"doChange");
	String adminPassword = ParamUtils.getParameter(request,"adminPassword");
	String username = ParamUtils.getParameter(request,"username");
	String newPassword = ParamUtils.getParameter(request,"newPassword");
	String confirmNew = ParamUtils.getParameter(request,"confirmNew");

      out.write("\r\n");
      out.write("\r\n");
	//////////////////
	// error variables
	
	boolean errorAdminPassword = (adminPassword==null);
	boolean errorUsername = (username==null);
	boolean errorNewPassword = (newPassword==null);
	boolean errorConfirmNew = (confirmNew==null);
	boolean errorNewPasswordsNotEqual = true;
	if( !errorNewPassword && !errorConfirmNew ) {
		if( newPassword.equals(confirmNew) ) {
			errorNewPasswordsNotEqual = false;
		}
	}
	boolean errors = ( errorAdminPassword
					   || errorUsername
					   || errorNewPassword
					   || errorConfirmNew
					   || errorNewPasswordsNotEqual
					 );

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// set the password if there are no errors
	
	ProfileManager manager = forumFactory.getProfileManager();
	boolean errorUserNotExist = false;
	if( !errors && doChange ) {
		
		// reauthenticate the admin:
		try {
			User adminUser = manager.getUser(authToken.getUserID());
			//AuthorizationFactory authFactory = AuthorizationFactory.getInstance();
			authToken = AuthorizationFactory.getAuthorization(adminUser.getUsername(),adminPassword);
			if( authToken == null ) {
				errorAdminPassword = true;
			}
		}
		catch( Exception e ) {
			errorAdminPassword = true;
		}
		
		// try to load specified user:
		if( !errorAdminPassword ) {
			try {
				User user = manager.getUser(username);
				user.setPassword(newPassword);
				response.sendRedirect(
					response.encodeRedirectURL("users.jsp?msg=Password changed successfully")
				);
			}
			catch( UserNotFoundException unfe ) {
				errorUserNotExist = true;
			}
		}
	}

      out.write("\r\n");
      out.write("\r\n");
	//////////////////
	// recheck for errors
	
	errors = (errors || errorUserNotExist || errorAdminPassword);

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title></title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/shadowBack.gif\" bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "System Settings", "Change Passwords" };

      out.write('\r');
      out.write('\n');
	///////////////////
	// pageTitle include

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
	if( pageTitleInfo != null ) { 
      out.write("\r\n");
      out.write("\t<table class=\"pageHeaderBg\" cellpadding=\"1\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t<td><table class=\"pageHeaderFg\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t<span class=\"pageTitle\">\r\n");
      out.write("\t");
	for( int i=0; i<pageTitleInfo.length; i++ ){ 
      out.write("\r\n");
      out.write("\t\t");
      out.print( pageTitleInfo[i] );
      out.write("\r\n");
      out.write("\t\t");
	if( (i+1)<pageTitleInfo.length ) { 
      out.write("\r\n");
      out.write("\t\t\t&nbsp;:&nbsp;\r\n");
      out.write("\t\t");
	} 
      out.write('\r');
      out.write('\n');
      out.write('	');
	} 
      out.write("\r\n");
      out.write("\t</span>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table></td>\r\n");
      out.write("\t</table>\r\n");
	} 
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("Change Passwords\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<i>(All fields are required)</i>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form action=\"password.jsp\" method=\"post\">\r\n");
      out.write("<input type=\"hidden\" name=\"doChange\" value=\"true\">\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"80%\" align=\"center\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( (doChange&&errorAdminPassword)?"<font color=\"#ff0000\">(error)</font><br>":"" );
      out.write("\r\n");
      out.write("\t\tPlease re-enter your<br>admin password</td>\r\n");
      out.write("\t<td><input type=\"password\" name=\"adminPassword\" size=\"30\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\"><td colspan=\"2\">&nbsp;</td></tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( (doChange&&(errorUsername||errorUserNotExist))?"<font color=\"#ff0000\">(error)</font><br>":"" );
      out.write("\r\n");
      out.write("\t\tUsername of person to change:\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td><input type=\"text\" name=\"username\" size=\"30\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( (doChange&&(errorNewPassword||errorNewPasswordsNotEqual))?"<font color=\"#ff0000\">(Error: no password entered or passwords not equal)</font><br>":"" );
      out.write("\r\n");
      out.write("\t\tNew password\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td><input type=\"password\" name=\"newPassword\" size=\"30\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( (doChange&&(errorConfirmNew||errorNewPasswordsNotEqual))?"<font color=\"#ff0000\">(Error: no password entered or passwords not equal)</font><br>":"" );
      out.write("\r\n");
      out.write("\t\tConfirm new password\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td><input type=\"password\" name=\"confirmNew\" size=\"30\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<center>\r\n");
      out.write("\t<input type=\"submit\" value=\"Change Password\">\r\n");
      out.write("</center>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
