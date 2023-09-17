package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.net.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class editUser_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 *	$RCSfile: editUser.jsp,v $
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
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	
	// redirect to error page if we're not a user admin or a system admin
	if( !isSystemAdmin ) {
		throw new UnauthorizedException("Sorry, you don't have permission to edit a user");
	}

      out.write("\r\n");
      out.write("\r\n");
	///////////////////////
	// get parameters
	
	boolean saveChanges = ParamUtils.getBooleanParameter(request,"saveChanges");
	String username = ParamUtils.getParameter(request,"user");
	String name = ParamUtils.getParameter(request,"name",true);
	String email = ParamUtils.getParameter(request,"email");
	boolean nameVisible = ParamUtils.getBooleanParameter(request,"nameVisible");
	boolean emailVisible = ParamUtils.getBooleanParameter(request,"emailVisible");

      out.write("\r\n");
      out.write("\r\n");
	////////////////
	// create a profile manager
	
	ProfileManager manager = forumFactory.getProfileManager();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////
	// check for errors
	
	boolean errorEmail = (email==null);
	boolean errors = (errorEmail);

      out.write("\r\n");
      out.write("\r\n");
	//////////////////
	// save user changes if necessary
	
	if( !errors && saveChanges ) {
		User user = manager.getUser(username);
		if( name != null ) {
			user.setName(name);
		}
		if( email != null ) {
			user.setEmail(email);
		}
		user.setEmailVisible( emailVisible );
		user.setNameVisible( nameVisible );
		
		// redirect to user main page
		response.sendRedirect(
			response.encodeRedirectURL("users.jsp?msg=Changes saved.")
		);
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////
	// user properties
	
	User user = manager.getUser(username);
	int userID = user.getID();
	name = user.getName();
	email = user.getEmail();
	boolean isEmailVisible = user.isEmailVisible();
	boolean isNameVisible = user.isNameVisible();
	Enumeration userProperties = user.propertyNames();

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
	String[] pageTitleInfo = { "Users", "Edit User Properties" };

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
      out.write("<form action=\"editUser.jsp\">\r\n");
      out.write("<input type=\"hidden\" name=\"saveChanges\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"user\" value=\"");
      out.print( username );
      out.write("\">\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("Properties for: <b>");
      out.print( username );
      out.write("</b>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"80%\" align=\"center\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>User ID:</td>\r\n");
      out.write("\t<td colspan=\"2\">");
      out.print( userID );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Username:</td>\r\n");
      out.write("\t<td colspan=\"2\">");
      out.print( username );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Name:</td>\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\t<input type=\"text\" name=\"name\" value=\"");
      out.print( (name!=null)?name:"" );
      out.write("\">\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Name is visible in forums</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<input type=\"radio\" name=\"nameVisible\" value=\"true\" id=\"rb01\"");
      out.print( isNameVisible?" checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb01\">Yes</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<input type=\"radio\" name=\"nameVisible\" value=\"false\" id=\"rb02\"");
      out.print( !isNameVisible?" checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb02\">No</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Email:</td>\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\t<input type=\"text\" name=\"email\" value=\"");
      out.print( (email!=null)?email:"" );
      out.write("\">\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Email is visible in forums</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<input type=\"radio\" name=\"emailVisible\" value=\"true\" id=\"rb03\"");
      out.print( isEmailVisible?" checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb03\">Yes</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<input type=\"radio\" name=\"emailVisible\" value=\"false\" id=\"rb04\"");
      out.print( !isEmailVisible?" checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb04\">No</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("Extended Properties\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"80%\" align=\"center\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
	if( !userProperties.hasMoreElements() ) { 
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td align=\"center\"><i>No extended properties set</i></td>\r\n");
      out.write("</tr>\r\n");
	} 
      out.write('\r');
      out.write('\n');
	while( userProperties.hasMoreElements() ) {
		String propName = (String)userProperties.nextElement();
		String propValue = user.getProperty(propName);

      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>");
      out.print( propName );
      out.write("</td>\r\n");
      out.write("\t<td><input type=\"text\" size=\"30\" maxlength=\"100\" value=\"");
      out.print( propValue );
      out.write("\"></td>\r\n");
      out.write("</tr>\r\n");
	} 
      out.write("\r\n");
      out.write("</table></td></table>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<center>\r\n");
      out.write("\t<input type=\"submit\" value=\"Save Changes\">\r\n");
      out.write("</center>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
