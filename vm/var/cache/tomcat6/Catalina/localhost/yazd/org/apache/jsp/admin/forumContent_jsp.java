package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.SimpleDateFormat;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class forumContent_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	//////////////////////////
	// global vars
	
	// date formatter for message dates
	private final SimpleDateFormat dateFormatter
		= new SimpleDateFormat( "EEE, MMM d 'at' hh:mm:ss z" );
	private final static int RANGE = 15;
	private final static int START = 0;

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
 *	$RCSfile: forumContent.jsp,v $
 *	$Revision: 1.4 $
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
	System.err.println("heelo");
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
	boolean isModerator   = permissions.get(ForumPermissions.MODERATOR);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin && !isModerator) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write(" \r\n");
	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
	boolean deleteThread = ParamUtils.getBooleanParameter(request,"deleteThread");
	int threadID = ParamUtils.getIntParameter(request,"thread",-1);
	int start = ParamUtils.getIntParameter(request,"start",START);
	int range = ParamUtils.getIntParameter(request,"range",RANGE);

      out.write("\r\n");
      out.write(" \r\n");
	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	boolean noThreadSpecified = (threadID < 0);

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// delete a thread
	if( deleteThread ) {
			Forum tempForum = forumFactory.getForum(forumID);
			ForumThread tempThread = tempForum.getThread(threadID);
			tempForum.deleteThread(tempThread);
			response.sendRedirect("forumContent.jsp?forum="+forumID);
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
	String[] pageTitleInfo = { "Forums : Manage Forum Content" };

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
	//////////////////////
	// show the name of the forum we're working with (if one was selected)
	Forum currentForum = null;
	if( !noForumSpecified ) {
		try {
			currentForum = forumFactory.getForum(forumID);
	
      out.write("\r\n");
      out.write("\t\t\tYou're currently working with forum: <b>");
      out.print( currentForum.getName() );
      out.write("</b>\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\tTopics: ");
      out.print( currentForum.getThreadCount() );
      out.write(", Messages: ");
      out.print( currentForum.getMessageCount() );
      out.write('\r');
      out.write('\n');
      out.write('	');
	}
		catch( ForumNotFoundException fnfe ) {
	
      out.write("\r\n");
      out.write("\t\t\t<span class=\"errorText\">Forum not found.</span>\r\n");
      out.write("\t");
	}
		catch( UnauthorizedException ue ) {
	
      out.write("\r\n");
      out.write("\t\t\t<span class=\"errorText\">Not authorized to administer this forum.</span>\r\n");
      out.write("\t");
	}
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
	///////////////////////
	// show a pulldown box of forums where this user can manage content:
	Iterator forumIterator = forumFactory.forumsModeration();
	if( isUserAdmin || isSystemAdmin ) {
	    forumIterator = forumFactory.forums();
	}
	if( !forumIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\tNo forums!\r\n");
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form>\r\n");
      out.write("\t<select size=\"1\" name=\"\" onchange=\"location.href='forumContent.jsp?forum='+this.options[this.selectedIndex].value;\">\r\n");
      out.write("\t<option value=\"-1\">Manage content for:\r\n");
      out.write("\t<option value=\"-1\">---------------------\r\n");
	while( forumIterator.hasNext() ) {
		Forum forum = (Forum)forumIterator.next();

      out.write("\r\n");
      out.write("\t\t<option value=\"");
      out.print( forum.getID() );
      out.write('"');
      out.write('>');
      out.print( forum.getName() );
      out.write('\r');
      out.write('\n');
	}

      out.write("\r\n");
      out.write("\t</select>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
	if( noForumSpecified ) {
		out.flush();
		return;
	}

      out.write("\r\n");
      out.write("\t\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	int numThreads = currentForum.getThreadCount(); 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t");
      out.write("\r\n");
      out.write("\t\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t\t\t<td width=\"50%\">\r\n");
      out.write("\t\t\t");
	if( start > 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<a href=\"forumContent.jsp?forum=");
      out.print(forumID);
      out.write("&start=");
      out.print( (start-range) );
      out.write("&range=");
      out.print( range );
      out.write("\" class=\"toolbar\"\r\n");
      out.write("\t\t\t\t>Previous ");
      out.print( range );
      out.write(" threads</a>\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t&nbsp;\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td width=\"50%\" align=\"right\">\r\n");
      out.write("\t\t\t");
	if( numThreads > (start+range) ) { 
      out.write("\r\n");
      out.write("\t\t\t");
		int numRemaining = (numThreads-(start+range)); 
      out.write("\r\n");
      out.write("\t\t\t\t<a href=\"forumContent.jsp?forum=");
      out.print(forumID);
      out.write("&start=");
      out.print( (start+range) );
      out.write("&range=");
      out.print( range );
      out.write("\" class=\"toolbar\"\r\n");
      out.write("\t\t\t\t>Next ");
      out.print( (numRemaining>range)?range:numRemaining );
      out.write(" threads</a>\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#cccccc\" cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#cccccc\" cellpadding=3 cellspacing=1 border=0 width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#dddddd\">\r\n");
      out.write("\t<td class=\"forumListHeader\" width=\"1%\" nowrap bgcolor=\"#cccccc\"><b>delete?</b></td>\r\n");
      out.write("\t<td class=\"forumListHeader\" width=\"95%\">Topic</td>\r\n");
      out.write("\t<td class=\"forumListHeader\" width=\"1%\" nowrap>replies</td>\r\n");
      out.write("\t<td class=\"forumListHeader\" width=\"1%\" nowrap>posted by</td>\r\n");
      out.write("\t<td class=\"forumListHeader\" width=\"1%\" nowrap>date</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
	/////////////////////
	// get an iterator of threads
	Iterator threadIterator = currentForum.threads(start,range);
	
	if( !threadIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<td colspan=\"5\" align=\"center\" class=\"forumListCell\">\r\n");
      out.write("\t\t<br><i>No messages in this forum.</i><br><br>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t\r\n");
	}
	while( threadIterator.hasNext() ) {
			ForumThread currentThread = (ForumThread)threadIterator.next();
			int currentThreadID = currentThread.getID();
			ForumMessage rootMessage = currentThread.getRootMessage();
			boolean rootMsgIsAnonymous = rootMessage.isAnonymous();
			User rootMessageUser = rootMessage.getUser();
			String username = rootMessageUser.getUsername();
			String name = rootMessageUser.getName();
			String email = rootMessageUser.getEmail();

      out.write("\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<form>\r\n");
      out.write("\t\t<td width=\"1%\" class=\"forumListCell\" align=\"center\">\r\n");
      out.write("\t\t\t<input type=\"radio\"\r\n");
      out.write("\t\t\t onclick=\"if(confirm('Are you sure you want to delete this thread and all its messages?')){location.href='forumContent.jsp?forum=");
      out.print(forumID);
      out.write("&deleteThread=true&thread=");
      out.print( currentThreadID );
      out.write("';}\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td class=\"forumListCell\" width=\"96%\">\r\n");
      out.write("\t\t\t<a href=\"threadContent.jsp?forum=");
      out.print(forumID);
      out.write("&thread=");
      out.print(currentThreadID);
      out.write('"');
      out.write('>');
      out.print( currentThread.getName() );
      out.write("</a>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td class=\"forumListCell\" width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t\t");
      out.print( currentThread.getMessageCount()-1 );
      out.write("\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td class=\"forumListCell\" width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t");
	if( rootMsgIsAnonymous ) { 
      out.write("\r\n");
      out.write("\t\t\t<i>Anonymous</i>\r\n");
      out.write("\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t\t<a href=\"mailto:");
      out.print(email);
      out.write("\" title=\"");
      out.print( name );
      out.write('"');
      out.write('>');
      out.print( username );
      out.write("</a>\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td class=\"forumListDateCell\" width=\"1%\" nowrap>\r\n");
      out.write("\t\t\t");
      out.print( dateFormatter.format(rootMessage.getCreationDate()) );
      out.write("\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\r\n");
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
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
