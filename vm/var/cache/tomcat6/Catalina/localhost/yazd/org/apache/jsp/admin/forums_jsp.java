package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class forums_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	///////////////////////////
	// page variables
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
 *	$RCSfile: forums.jsp,v $
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
	
	// make sure the user is authorized to create forums::
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to create forums");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////
	// get parameters
	
	// paging vars:
	int start = ParamUtils.getIntParameter(request,"start",0);
	int range = ParamUtils.getIntParameter(request,"range",DEFAULT_RANGE);

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
	String[] pageTitleInfo = { "Forums" };

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
	///////////////////////////////
	// print out message, if any
	String message = (String)request.getAttribute("message");
	if( message != null ) {

      out.write("\r\n");
      out.write("\t<span class=\"messageText\">\r\n");
      out.write("\t");
      out.print( message );
      out.write("\r\n");
      out.write("\t</span>\r\n");
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
	//////////////////////
	// forum iterator, forum count
	
	Iterator forumIterator = forumFactory.forums();
	int forumCount = forumFactory.getForumCount();

      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
	if( (forumCount>0) ) { 
      out.write("\r\n");
      out.write("<form>\r\n");
      out.write("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t");
	if( (start-range) >= 0 ) { 
      out.write("\r\n");
      out.write("\t\t&laquo; <a href=\"forums.jsp?range=");
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
      out.write("\t\t onchange=\"location.href='forums.jsp?start=");
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
      out.write("\t\tforums per page\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t");
	if( (start+range) < forumCount ) { 
      out.write("\r\n");
      out.write("\t\t<a href=\"forums.jsp?range=");
      out.print(range);
      out.write("&start=");
      out.print((start+range));
      out.write("\">Next ");
      out.print( ((start+range-forumCount)<range)?(forumCount-range):range );
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
	while( start-- > 0 ) {
		forumIterator.next();
	}

      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#999999\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("<td>\r\n");
      out.write("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td class=\"forumCellHeader\" width=\"1%\" nowrap>\r\n");
      out.write("\t\t<b>ID</b>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" width=\"1%\" nowrap>\r\n");
      out.write("\t\t<b>Forum Name</b>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" width=\"93%\"><b>Description</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\" width=\"1%\" nowrap><b>Threads /<br>Messages</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\" width=\"1%\" nowrap><b>Properties</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\" width=\"1%\" nowrap><b>Permissions</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\" width=\"1%\" nowrap><b>Filters</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\" width=\"1%\" nowrap><b>Remove</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\" width=\"1%\" nowrap><b>Content</b></td>\r\n");
      out.write("</tr>\r\n");
	int count = 0;
	if( !forumIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<td colspan=\"9\" align=\"center\" class=\"forumCell\"><br><i>No forums.<br>Try <a href=\"createForum.jsp\">creating one</a>.</i><br><br></td>\r\n");
      out.write("\t\t</tr>\r\n");

	}
	while( forumIterator.hasNext() && (count++)<range ) {
		Forum forum = (Forum)forumIterator.next();
		int forumID = forum.getID();
		String description = forum.getDescription();
	
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<td class=\"forumCell\" align=\"center\"><b>");
      out.print( forum.getID() );
      out.write("</b></td>\r\n");
      out.write("\t\t<td class=\"forumCell\">\r\n");
      out.write("\t\t\t<b><a href=\"forumDetail.jsp?forum=");
      out.print( forum.getID() );
      out.write("\"\r\n");
      out.write("\t\t\t    title=\"More details...\">");
      out.print( forum.getName() );
      out.write("</a></b>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td class=\"forumCell\"><i>");
      out.print( (description!=null&&!description.equals(""))?description:"" );
      out.write("</i></td>\r\n");
      out.write("\t\t<td align=\"center\" class=\"forumCell\">");
      out.print( forum.getThreadCount() );
      out.write(' ');
      out.write('/');
      out.write(' ');
      out.print( forum.getMessageCount() );
      out.write("</td>\r\n");
      out.write("\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"radio\" name=\"edit\"\r\n");
      out.write("\t\t\t onclick=\"location.href='editForum.jsp?forum=");
      out.print( forumID );
      out.write("'\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"radio\" name=\"perms\"\r\n");
      out.write("\t\t\t onclick=\"location.href='forumPerms.jsp?forum=");
      out.print( forumID );
      out.write("'\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"radio\" name=\"filters\"\r\n");
      out.write("\t\t\t onclick=\"location.href='forumFilters.jsp?forum=");
      out.print( forumID );
      out.write("';\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"radio\" name=\"remove\"\r\n");
      out.write("\t\t\t onclick=\"location.href='removeForum.jsp?forum=");
      out.print( forumID );
      out.write("';\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"radio\" name=\"content\"\r\n");
      out.write("\t\t\t onclick=\"location.href='forumContent.jsp?forum=");
      out.print( forumID );
      out.write("';\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
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
      out.write("<form action=\"forumSearch.jsp\">\r\n");
      out.write("<center>\r\n");
      out.write("\tfind forum:\r\n");
      out.write("\t<input type=\"text\" name=\"q\" value=\"\" size=\"30\" maxlength=\"50\">\r\n");
      out.write("\t<input type=\"submit\" value=\"Find!\">\r\n");
      out.write("</center>\r\n");
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
