package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class forumDetail_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	///////////////////////
	// page variables
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d 'at' hh:mm:ss a");

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
 *	$RCSfile: forumDetail.jsp,v $
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
	String[] pageTitleInfo = { "Forums : Forum Details" };

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
	/////////////////////
	// at this point, we know there is a forum to work with:
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	} catch( ForumNotFoundException fnfe ) {
	} catch( UnauthorizedException ue ) {
	}
	
	Date creationDate = forum.getCreationDate();
	String description = forum.getDescription();
	ForumMessageFilter[] installedFilters = null;
	try {
		installedFilters = forum.getForumMessageFilters();
	}
	catch( UnauthorizedException ue ) {}
	int messageCount = forum.getMessageCount();
	Date modifiedDate = forum.getModifiedDate();
	String forumName = forum.getName();
	ForumPermissions forumPermissions = forum.getPermissions(authToken);
	Enumeration propertyNames = forum.propertyNames();
	int threadCount = forum.getThreadCount();
	

      out.write("\r\n");
      out.write("\r\n");
      out.write("<font size=\"+1\">\r\n");
      out.write("Forum: ");
      out.print( forumName );
      out.write("\r\n");
      out.write("</font>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#999999\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"0\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#999999\" cellspacing=\"1\" cellpadding=\"3\" width=\"100%\" border=\"0\">\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td nowrap>Description</td>\r\n");
      out.write("\t<td><i>");
      out.print( (description!=null&&!description.equals(""))?description:"&nbsp;" );
      out.write("</i></td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td nowrap>Number of Threads</td>\r\n");
      out.write("\t<td>");
      out.print( threadCount );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td nowrap>Number of Messages</td>\r\n");
      out.write("\t<td>");
      out.print( messageCount );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td nowrap>Creation Date</td>\r\n");
      out.write("\t<td>");
      out.print( dateFormat.format(creationDate) );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td nowrap>Last Modified Date</td>\r\n");
      out.write("\t<td>");
      out.print( dateFormat.format(modifiedDate) );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<b>Extended Properties:</b><p>\r\n");
	if( !propertyNames.hasMoreElements() ) { 
      out.write("\r\n");
      out.write("\t<ul><i>No extended properties.</i></ul>\r\n");
	} else { 
      out.write("\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellspacing=\"0\" cellpadding=\"0\" width=\"95%\" align=\"right\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellspacing=\"1\" cellpadding=\"3\" width=\"100%\">\r\n");
      out.write("\t");
	while( propertyNames.hasMoreElements() ) { 
      out.write('\r');
      out.write('\n');
      out.write('	');
		String propertyName = (String)propertyNames.nextElement(); 
      out.write('\r');
      out.write('\n');
      out.write('	');
		String propertyValue = forum.getProperty(propertyName); 
      out.write("\r\n");
      out.write("\t\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<td>");
      out.print( propertyName );
      out.write("</td>\r\n");
      out.write("\t\t<td>");
      out.print( propertyValue );
      out.write("</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<br clear=\"all\"><br>\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<b>Installed Filters:</b> (<a href=\"forumFilters.jsp?forum=");
      out.print(forumID);
      out.write("\">filters</a>)<p>\r\n");
	if( installedFilters == null || installedFilters.length == 0 ) { 
      out.write("\r\n");
      out.write("\t<ul><i>No filters installed. (<a href=\"forumFilters.jsp?forum=");
      out.print(forumID);
      out.write("\">add some</a>)</i></ul>\r\n");
	} else { 
      out.write("\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellspacing=\"0\" cellpadding=\"0\" width=\"95%\" align=\"right\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellspacing=\"1\" cellpadding=\"3\" width=\"100%\">\r\n");
      out.write("\t");
	for( int i=0; i<installedFilters.length; i++ ) { 
      out.write('\r');
      out.write('\n');
      out.write('	');
		ForumMessageFilter filter = installedFilters[i]; 
      out.write("\r\n");
      out.write("\t\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<td>");
      out.print( filter.getName() );
      out.write("</td>\r\n");
      out.write("\t\t<td>");
      out.print( filter.getDescription() );
      out.write("</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<br clear=\"all\"><br>\r\n");
	} 
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
