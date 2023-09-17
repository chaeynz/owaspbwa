package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class removeForum_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/admin/include/pageTitle.jsp");
    _jspx_dependants.add("/admin/forumChooser.jsp");
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
 *	$RCSfile: removeForum.jsp,v $
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
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write(" \r\n");
	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
	boolean doDelete = ParamUtils.getBooleanParameter(request,"doDelete");

      out.write("\r\n");
      out.write(" \r\n");
	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	boolean errors = (noForumSpecified);

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// delete forum if specified
	if( doDelete && !errors ) {
		String message = "";
		try {
			Forum deleteableForum = forumFactory.getForum(forumID);
			forumFactory.deleteForum(deleteableForum);
			message = "Forum deleted successfully!";
		}
		catch( ForumNotFoundException fnfe ) {
			message = "No forum found";
		}
		catch( UnauthorizedException ue ) {
			message = "Not authorized to delete this forum";
		}
		request.setAttribute("message",message);
		response.sendRedirect("forums.jsp");
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
	String[] pageTitleInfo = { "Forums : Remove Forum" };

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
	////////////////////
	// display a list of forums to remove if no group was specified:
	if( noForumSpecified ) {
		String formAction = "remove";

      out.write("\r\n");
      out.write("\t\t");
      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: forumChooser.jsp,v $
 *	$Revision: 1.2 $
 *	$Date: 2000/12/18 02:06:21 $
 */

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// get an iterator of forums and display a list
	
	Iterator forumIterator = forumFactory.forums();
	if( !forumIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\tNo forums!\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\tTry <a href=\"createForum.jsp\">creating one</a>.\r\n");

	} else {

      out.write("\r\n");
      out.write("\r\n");
      out.write("\t");
	if( formAction.equals("edit") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"editForum.jsp\">\r\n");
      out.write("\t");
	} else if( formAction.equals("remove") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"removeForum.jsp\">\r\n");
      out.write("\t");
	} else if( formAction.equals("filters") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"forumFilters.jsp\">\r\n");
      out.write("\t");
	} else if( formAction.equals("content") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"forumContent.jsp\">\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<select name=\"forum\">\r\n");
      out.write("\t");
	while( forumIterator.hasNext() ) {
			Forum tmpForum = (Forum)forumIterator.next();
	
      out.write("\r\n");
      out.write("\t\t\t<option value=\"");
      out.print( tmpForum.getID() );
      out.write('"');
      out.write('>');
      out.print( tmpForum.getName() );
      out.write('\r');
      out.write('\n');
      out.write('	');
	}
	
      out.write("\r\n");
      out.write("\t</select>\r\n");
      out.write("\t\r\n");
      out.write("\t");
	if( formAction.equals("edit") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Edit Properties...\">\r\n");
      out.write("\t");
	} else if( formAction.equals("remove") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Remove This Forum...\">\r\n");
      out.write("\t");
	} else if( formAction.equals("filters") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Work with filters...\">\r\n");
      out.write("\t");
	} else if( formAction.equals("content") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Forum Content...\">\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t</form>\r\n");
      out.write("\t\r\n");
	} 
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
		out.flush();
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// at this point, we know there is a forum to work with:
	Forum forum = null;
	try {
		forum                  = forumFactory.getForum(forumID);
	} catch( ForumNotFoundException fnfe ) {
	} catch( UnauthorizedException ue ) {
	}
	String forumName             = forum.getName();
	int threadCount = forum.getThreadCount();
	int messageCount = forum.getMessageCount();

      out.write("\r\n");
      out.write("\r\n");
      out.write("Remove <b>");
      out.print( forumName );
      out.write("</b>? \r\n");
      out.write("(Threads: ");
      out.print( threadCount );
      out.write(", Messages ");
      out.print( messageCount );
      out.write(",\r\n");
      out.write("<a href=\"forumContent.jsp?forum=");
      out.print(forumID);
      out.write("\">view content</a>\r\n");
      out.write(")\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<ul>\r\n");
      out.write("\tWarning: This will delete <b>all</b> forum messages &amp; threads. Are\r\n");
      out.write("\tyou sure you really want to do this? (It will <b>not</b> delete users or groups\r\n");
      out.write("\twho have permission to use this forum.)\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<form action=\"removeForum.jsp\" name=\"deleteForm\">\r\n");
      out.write("<input type=\"hidden\" name=\"doDelete\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t<input type=\"submit\" value=\" Delete Forum \">\r\n");
      out.write("\t&nbsp;\r\n");
      out.write("\t<input type=\"submit\" name=\"cancel\" value=\" Cancel \" style=\"font-weight:bold;\"\r\n");
      out.write("\t onclick=\"location.href='forums.jsp';return false;\">\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("<!--\r\n");
      out.write("document.deleteForm.cancel.focus();\r\n");
      out.write("//-->\r\n");
      out.write("</script>\r\n");
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
