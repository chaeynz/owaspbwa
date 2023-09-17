package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class groups_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	//////////////////
	// page variables
	private final String YES = "<font color='#006600' size='-1'><b>Yes</b></font>";
	private final String NO  = "<font color='#ff0000' size='-1'><b>No</b></font>";

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
 *	$RCSfile: groups.jsp,v $
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
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isGroupAdmin && !isSystemAdmin ) {
		throw new UnauthorizedException("Sorry, you don't have permission to administer groups");
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title></title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("\t<style type=\"text/css\">\r\n");
      out.write("\t.groupHeader {\r\n");
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
	String[] pageTitleInfo = { "Groups" };

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
	Iterator groupIterator = manager.groups();

      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
	if( isSystemAdmin ) { 
      out.write("\r\n");
      out.write("\t<p>\r\n");
      out.write("\t(<a href=\"createGroup.jsp\">Create New Group</a>)\r\n");
      out.write("\t<p>\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form>\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"1%\" nowrap>ID</td>\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"40%\" nowrap>Group Name</td>\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"55%\">Description</td>\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"1%\" nowrap>Members</td>\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"1%\" nowrap>Edit</td>\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"1%\" nowrap>Permissions</td>\r\n");
      out.write("\t<td class=\"groupHeader\" width=\"1%\" nowrap>Remove</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
	//////////////////
	// no groups:
	if( !groupIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t\t<td colspan=\"7\" align=\"center\">\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<i>No groups, try <a href=\"createGroup.jsp\">adding one</a>.</i>\r\n");
      out.write("\t\t\t<br><br>\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////////////////////
	// iterate through users, show info
	
	while( groupIterator.hasNext() ) {
		Group group = (Group)groupIterator.next();
		int groupID = group.getID();
		String groupName = group.getName();
		String groupDescription = group.getDescription();
		int numMembers = group.getMemberCount();

      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td align=\"center\">");
      out.print( groupID );
      out.write("</td>\r\n");
      out.write("\t<td><b>");
      out.print( groupName );
      out.write("</b></td>\r\n");
      out.write("\t<td><i>");
      out.print( (groupDescription!=null&&!groupDescription.equals(""))?groupDescription:"&nbsp;" );
      out.write("</i></td>\r\n");
      out.write("\t<td align=\"center\">");
      out.print( numMembers );
      out.write("</td>\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"edit\"\r\n");
      out.write("\t\t onclick=\"location.href='editGroup.jsp?group=");
      out.print(groupID);
      out.write("';\">\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"perms\"\r\n");
      out.write("\t\t onclick=\"location.href='groupPerms.jsp?group=");
      out.print(groupID);
      out.write("';\">\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"remove\"\r\n");
      out.write("\t\t onclick=\"location.href='removeGroup.jsp?group=");
      out.print(groupID);
      out.write("';\">\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
	}

      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
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
