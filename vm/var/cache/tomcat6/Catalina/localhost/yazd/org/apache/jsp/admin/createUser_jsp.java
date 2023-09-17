package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.net.URLEncoder;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class createUser_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 *	$RCSfile: createUser.jsp,v $
 *	$Revision: 1.3 $
 *	$Date: 2000/12/18 02:06:21 $
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
      out.write("\r\n");
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
      out.write(" \r\n");
	////////////////////
	// Security check
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.USER_ADMIN);
	
	// redirect to error page if we're not a user admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("No permission to administer users"));
		return;
	}

      out.write("\r\n");
      out.write(" \r\n");
	//////////////////////////////////
	// error variables for parameters
	
	boolean errorEmail = false;
	boolean errorUsername = false;
	boolean errorNoPassword = false;
	boolean errorNoConfirmPassword = false;
	boolean errorPasswordsNotEqual = false;
	
	// error variables from user creation
	boolean errorUserAlreadyExists = false;
	boolean errorNoPermissionToCreate = false;
	
	// overall error variable
	boolean errors = false;
	
	// creation success variable:
	boolean success = false;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// get parameters
	String name             = ParamUtils.getParameter(request,"name");
	String email            = ParamUtils.getParameter(request,"email");
	String username         = ParamUtils.getParameter(request,"username");
	String password         = ParamUtils.getParameter(request,"password");
	String confirmPassword  = ParamUtils.getParameter(request,"confirmPassword");
	boolean usernameIsEmail = ParamUtils.getCheckboxParameter(request,"usernameIsEmail");
	boolean nameVisible     = !ParamUtils.getCheckboxParameter(request,"hideName");
	boolean emailVisible    = !ParamUtils.getCheckboxParameter(request,"hideEmail");
	boolean doCreate        = ParamUtils.getBooleanParameter(request,"doCreate");

      out.write("\r\n");
      out.write("\r\n");
	///////////////////////////////////////////////////////////////////
	// trim up the passwords so no one can enter a password of spaces
	if( password != null ) {
		password = password.trim();
		if( password.equals("") ) { password = null; }
	}
	if( confirmPassword != null ) {
		confirmPassword = confirmPassword.trim();
		if( confirmPassword.equals("") ) { confirmPassword = null; }
	}

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////
	// check for errors
	if( doCreate ) {
		if( email == null ) {
			errorEmail = true;
		}
		if( username == null ) {
			errorUsername = true;
		}
		if( password == null ) {
			errorNoPassword = true;
		}
		if( confirmPassword == null ) {
			errorNoConfirmPassword = true;
		}
		if( password != null && confirmPassword != null
		    && !password.equals(confirmPassword) )
		{
			errorPasswordsNotEqual = true;
		}
		errors = errorEmail || errorUsername || errorNoPassword
		         || errorNoConfirmPassword || errorPasswordsNotEqual;
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////////////////////////////////////////////////
	// if there are no errors at this point, start the process of
	// adding the user
	
	ProfileManager profileManager = null;
	if( !errors && doCreate ) {
		// get a profile manager to edit user properties
		profileManager = forumFactory.getProfileManager();
		try {
			User newUser = profileManager.createUser(username,password,email);
			newUser.setName( name );
			newUser.setEmailVisible( emailVisible );
			newUser.setNameVisible( nameVisible );
			success = true;
		}
		catch( UserAlreadyExistsException uaee ) {
			errorUserAlreadyExists = true;
			errorUsername = true;
			errors = true;
		}
		catch( UnauthorizedException ue ) {
			errorNoPermissionToCreate = true;
			errors = true;
		}
	}

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////////////////////////////////////////////////////
	// if a user was successfully created, say so and return (to stop the 
	// jsp from executing
	if( success ) {
		response.sendRedirect("users.jsp?msg="
			+ URLEncoder.encode("User was created successfully"));
		return;
	} 

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
	String[] pageTitleInfo = { "Users", "Create User" };

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
	// print error messages
	if( !success && errors ) {

      out.write("\r\n");
      out.write("\t<p><font color=\"#ff0000\">\r\n");
      out.write("\t");
	if( errorUserAlreadyExists ) { 
      out.write("\r\n");
      out.write("\t\tThe username \"");
      out.print( username );
      out.write("\" is already taken. Please try \r\n");
      out.write("\t\tanother one.\r\n");
      out.write("\t");
	} else if( errorNoPermissionToCreate ) { 
      out.write("\r\n");
      out.write("\t\tYou do not have user creation privileges.\r\n");
      out.write("\t");
	} else { 
      out.write("\r\n");
      out.write("\t\tAn error occured. Please check the following \r\n");
      out.write("\t\tfields and try again.\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t</font><p>\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<font size=\"-1\">\r\n");
      out.write("This creates a user with no permissions and default privacy settings.\r\n");
      out.write("Once you create this user, you should edit their properties.\r\n");
      out.write("</font>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form action=\"createUser.jsp\" method=\"post\" name=\"createForm\">\r\n");
      out.write("<input type=\"hidden\" name=\"doCreate\" value=\"true\">\r\n");
      out.write("\r\n");
      out.write("<b>New User Information</b>\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#999999\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"95%\" align=\"right\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#999999\" cellspacing=\"1\" cellpadding=\"3\" border=\"0\" width=\"100%\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\">Name <i>(optional)</i></font></td>\r\n");
      out.write("\t<td><input type=\"text\" name=\"name\" size=\"30\"\r\n");
      out.write("\t\t value=\"");
      out.print( (name!=null)?name:"" );
      out.write("\">\r\n");
      out.write("\t</td>\t\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\"");
      out.print( (errorEmail)?(" color=\"#ff0000\""):"" );
      out.write(">Email</font></td>\r\n");
      out.write("\t<td><input type=\"text\" name=\"email\" size=\"30\"\r\n");
      out.write("\t\t value=\"");
      out.print( (email!=null)?email:"" );
      out.write("\">\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\"");
      out.print( (!usernameIsEmail&&errorUsername)?" color=\"#ff0000\"":"" );
      out.write(">\r\n");
      out.write("\t\tUsername\r\n");
      out.write("\t\t<br>&nbsp;(<input type=\"checkbox\" name=\"usernameIsEmail\" \r\n");
      out.write("\t\t  id=\"cb01\"");
      out.print( (usernameIsEmail)?" checked":"" );
      out.write("\r\n");
      out.write("\t\t  onclick=\"this.form.username.value=this.form.email.value;\"> \r\n");
      out.write("\t\t<label for=\"cb01\">use email</label>)\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td><input type=\"text\" name=\"username\" size=\"30\"\r\n");
      out.write("\t\t");
	if( usernameIsEmail ) { 
      out.write("\r\n");
      out.write("\t\t value=\"");
      out.print( (email!=null)?email:"" );
      out.write("\">\r\n");
      out.write("\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t value=\"");
      out.print( (username!=null)?username:"" );
      out.write("\">\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\"");
      out.print( (errorNoPassword||errorPasswordsNotEqual)?" color=\"#ff0000\"":"" );
      out.write("\r\n");
      out.write("\t\t >Password</font></td>\r\n");
      out.write("\t<td><input type=\"password\" name=\"password\" value=\"\" size=\"20\" maxlength=\"30\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\"");
      out.print( (errorNoConfirmPassword||errorPasswordsNotEqual)?" color=\"#ff0000\"":"" );
      out.write("\r\n");
      out.write("\t\t >Password (again)</font></td>\r\n");
      out.write("\t<td><input type=\"password\" name=\"confirmPassword\" value=\"\" size=\"20\" maxlength=\"30\"></td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<br clear=\"all\"><br>\r\n");
      out.write("\r\n");
      out.write("<input type=\"submit\" value=\"Create User\">\r\n");
      out.write("&nbsp;\r\n");
      out.write("<input type=\"submit\" value=\"Cancel\"\r\n");
      out.write(" onclick=\"location.href='users.jsp';return false;\">\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("<!--\r\n");
      out.write("document.createForm.name.focus();\r\n");
      out.write("//-->\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
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
