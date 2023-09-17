package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class users_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	//////////////////
	// page variables
	private final String YES = "<font color='#006600' size='-1'><b>Yes</b></font>";
	private final String NO  = "<font color='#ff0000' size='-1'><b>No</b></font>";
	private final int DEFAULT_RANGE = 10;
	private final int[] ranges = {10,20,30,50};

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
 *	$RCSfile: users.jsp,v $
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
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.USER_ADMIN);
	
	// redirect to error page if we're not a user admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer users");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	///////////////////
	// get parameters
	
	// parameter for paging through list of users
	int start = ParamUtils.getIntParameter(request,"start",0);
	int range = ParamUtils.getIntParameter(request,"range",DEFAULT_RANGE);
	String msg = ParamUtils.getParameter(request,"msg");

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title></title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("\t<style type=\"text/css\">\r\n");
      out.write("\t.userHeader {\r\n");
      out.write("\t\tfont-size : 8pt;\r\n");
      out.write("\t\ttext-align : center;\r\n");
      out.write("\t}\r\n");
      out.write("\t</style>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/shadowBack.gif\" bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Users" };

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
      out.write("\r\n");
	////////////////////////////////
	// get an Iterator of users
	ProfileManager manager = forumFactory.getProfileManager();
	Iterator userIterator = manager.users();
	int userCount = manager.getUserCount();

      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
	if( msg != null ) { 
      out.write("\r\n");
      out.write("\t<span class=\"messageText\">\r\n");
      out.write("\t");
      out.print( msg );
      out.write("\r\n");
      out.write("\t</span>\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form>\r\n");
      out.write("\t\r\n");
      out.write('\r');
      out.write('\n');
	if( (userCount>0) ) { 
      out.write("\r\n");
      out.write("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t");
	if( (start-range) >= 0 ) { 
      out.write("\r\n");
      out.write("\t\t&laquo; <a href=\"users.jsp?range=");
      out.print(range);
      out.write("&start=");
      out.print((start-range));
      out.write("\">Previous ");
      out.print( range );
      out.write("</a>\r\n");
      out.write("\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t&nbsp;\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"98%\" align=\"center\">\r\n");
      out.write("\t\tshow\r\n");
      out.write("\t\t<select size=\"1\"\r\n");
      out.write("\t\t onchange=\"location.href='users.jsp?start=");
      out.print(start);
      out.write("&range='+this.options[this.selectedIndex].value;\">\r\n");
      out.write("\t\t");
	for( int i=0; i<ranges.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t");
		String selected = ""; 
      out.write("\r\n");
      out.write("\t\t");
		if( range == ranges[i] ) { selected = " selected"; } 
      out.write("\r\n");
      out.write("\t\t\t<option value=\"");
      out.print( ranges[i] );
      out.write('"');
      out.print( selected );
      out.write('>');
      out.print( ranges[i] );
      out.write("\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t</select>\r\n");
      out.write("\t\tusers per page\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t");
	if( (start+range) < userCount ) { 
      out.write("\r\n");
      out.write("\t\t<a href=\"users.jsp?range=");
      out.print(range);
      out.write("&start=");
      out.print((start+range));
      out.write("\">Next ");
      out.print( ((start+range-userCount)<range)?(userCount-range):range );
      out.write("</a> &raquo;\r\n");
      out.write("\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t&nbsp;\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
	} 
      out.write('\r');
      out.write('\n');
	/////////////////////
	// skip results if necessary
	int skip = start;
	if( skip > 0 ) {
		while( userIterator.hasNext() && (skip-- > 0) ) {
			userIterator.next();
		}
	}

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n");
      out.write("<td>\r\n");
      out.write("<table border=\"0\" cellpadding=\"2\" cellspacing=\"1\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td class=\"userHeader\" width=\"2%\" nowrap>&nbsp;ID&nbsp;</td>\r\n");
      out.write("\t<td width=\"25%\"><b>Username</b></td>\r\n");
      out.write("\t<td width=\"36%\"><b>Name</b></td>\r\n");
      out.write("\t<td width=\"25%\"><b>Email</b></td>\r\n");
      out.write("\t<td class=\"userHeader\" width=\"4%\" nowrap>Edit<br>Properties</td>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<td class=\"userHeader\" width=\"4%\" nowrap>Remove</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
	////////////////////////////////////
	// iterate through users, show info
	
	int count = 0;
	while( userIterator.hasNext() && (count++)<range ) {
		User user = (User)userIterator.next();
		int userID = user.getID();
		String username = user.getUsername();
		String name = user.getName();
		String email = user.getEmail();
		boolean isNameVisible = user.isNameVisible();
		boolean isEmailVisible = user.isEmailVisible();

      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td align=\"center\">");
      out.print( userID );
      out.write("</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<b>");
      out.print( username );
      out.write("</b>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( (name!=null&&!name.equals(""))?name:"&nbsp;" );
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( (email!=null&&!email.equals(""))?email:"&nbsp;" );
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"props\" value=\"\"\r\n");
      out.write("\t\t onclick=\"location.href='editUser.jsp?user=");
      out.print( username );
      out.write("';\">\r\n");
      out.write("\t</td>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"props\" value=\"\"\r\n");
      out.write("\t\t onclick=\"location.href='removeUser.jsp?user=");
      out.print( username );
      out.write("';\">\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
	} 

      out.write("\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form action=\"userSearch.jsp\">\r\n");
      out.write("<center>\r\n");
      out.write("\tfind user:\r\n");
      out.write("\t<input type=\"text\" name=\"q\" value=\"\" size=\"30\" maxlength=\"50\">\r\n");
      out.write("\t<input type=\"submit\" value=\"Find!\">\r\n");
      out.write("</center>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
