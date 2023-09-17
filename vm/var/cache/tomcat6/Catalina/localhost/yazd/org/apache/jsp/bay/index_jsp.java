package org.apache.jsp.bay;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.util.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/bay/header.jsp");
    _jspx_dependants.add("/bay/footer.jsp");
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
 *	$RCSfile: index.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/27 22:39:45 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	// get parameters
	boolean logout = 		ParamUtils.getBooleanParameter(request, "logout");
	int 	forumID = 		ParamUtils.getIntParameter(request, "forum", -1);
	long	lastVisited = 	SkinUtils.getLastVisited(request, response);

      out.write("\r\n");
      out.write("\r\n");
	// logout if requested:
	if (logout) {
		SkinUtils.removeUserAuthorization(request, response);
		response.sendRedirect("index.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////////
	// Authorization check
	
	// check for the existence of an authorization token
	Authorization authToken = SkinUtils.getUserAuthorization(request,response);
	
	// if the token was null, they're not authorized. Since this skin will
	// allow guests to view forums, we'll set a "guest" authentication
	// token
	if( authToken == null ) {
		authToken = AuthorizationFactory.getAnonymousAuthorization();
	}

      out.write("\r\n");
      out.write("\r\n");
	// get a forum factory and forum iterator

	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	Iterator forumIterator = forumFactory.forums();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////
	// header include
	
	String title = "Yazd: Bay skin";

      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: header.jsp,v $
 *	$Revision: 1.5.2.1 $
 *	$Date: 2001/01/11 06:22:04 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	// prepend "header" to variables used here to prevent clashes with including pages...
	String		siteTitle = "CoolServlets";
	String		pageTitle = (title == null || title.length() == 0) ? siteTitle : title + " - " + siteTitle;
	String 		headerUserName = null;
	boolean		headerCanPost = false;
	if (forumID > -1) {
		try {
			headerCanPost = forumFactory.getForum(forumID).hasPermission(ForumPermissions.CREATE_THREAD);
		} catch (Exception ignoreFnF) {}
	}
	if (authToken != null && forumFactory != null) {
		try {
			ProfileManager headerProfileManager = forumFactory.getProfileManager();
			User headerUser = (headerProfileManager == null) ? null : headerProfileManager.getUser(authToken.getUserID());
			if (headerUser != null && !headerUser.isAnonymous()) {
				headerUserName = headerUser.getName();
				if (headerUserName == null || headerUserName.trim().length() == 0) {
					headerUserName = headerUser.getUsername();
				}
			}
		} catch (Exception ignore) {}
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>");
      out.print( title );
      out.write("</title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("</head>\r\n");
      out.write("<body bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0033cc\" vlink=\"#663366\" alink=\"#ff3300\">\r\n");
      out.write("\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr>\r\n");
      out.write("<td width=\"1%\">\r\n");
      out.write("\t<a href=\"index.jsp\"><img src=\"images/Yazdheader.gif\" alt=\"Yazd\" border=\"0\"></a>\r\n");
      out.write("</td>\r\n");
      out.write("<td width=\"1%\">\r\n");
      out.write("\t&nbsp;\r\n");
      out.write("</td>\r\n");
      out.write("\r\n");
	if (headerUserName != null) { 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t<a href=\"account.jsp?mode=2\"><b>");
      out.print( headerUserName );
      out.write("</b></a>\r\n");
      out.write("\t</td>\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
	if (forumID > 0) { 
      out.write("\r\n");
      out.write("\t<td width=\"90%\">&nbsp;</td>\r\n");
	} else { 
      out.write("\r\n");
      out.write("\t<td width=\"94%\">&nbsp;</td>\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
	if (headerUserName != null) { 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t<a href=\"index.jsp?logout=true\" class=\"normal\">\r\n");
      out.write("\t\t\tLogout\r\n");
      out.write("\t\t</a>\r\n");
      out.write("\t</td>\r\n");
	} else { 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t<a href=\"account.jsp\" class=\"normal\">\r\n");
      out.write("\t\t\tLogin\r\n");
      out.write("\t\t</a>\r\n");
      out.write("\t</td>\r\n");
	} 
      out.write('\r');
      out.write('\n');
	if (forumID > 0) { 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t&nbsp;\r\n");
      out.write("\t\t<img src=\"images/bluedot.gif\" width=1 height=25 border=0>\r\n");
      out.write("\t\t&nbsp;\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<a href=\"viewForum.jsp?forum=");
      out.print( forumID );
      out.write("\"\r\n");
      out.write("\t\t\t><img src=\"images/read_tb.gif\" width=71 height=30 alt=\"Read messages\" border=\"0\"\r\n");
      out.write("\t\t></a>\r\n");
      out.write("\t</td>\r\n");
	if (headerCanPost) { 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<img src=\"images/vertLine.gif\" width=1 height=30 border=0 hspace=4>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<a href=\"post.jsp?forum=");
      out.print( forumID );
      out.write("\"\r\n");
      out.write("\t\t\t><img src=\"images/post_tb.gif\" width=62 height=30 alt=\"Post a message\" border=\"0\"\r\n");
      out.write("\t\t></a>\r\n");
      out.write("\t</td>\r\n");
	} 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<img src=\"images/vertLine.gif\" width=1 height=30 border=0 hspace=4>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<a href=\"search.jsp?forum=");
      out.print( forumID );
      out.write("\"\r\n");
      out.write("\t\t\t><img src=\"images/search_tb_left.gif\" height=30 alt=\"Search\" border=\"0\"\r\n");
      out.write("\t\t\t><img src=\"images/search_tb_right.gif\" height=30 border=\"0\"\r\n");
      out.write("\t\t></a>\r\n");
      out.write("\t</td>\r\n");
	} else { 
      out.write('\r');
      out.write('\n');
      out.write('	');
 if( forumID > -1 ) { 
      out.write("\r\n");
      out.write("\t<td width=\"1%\" nowrap align=\"center\">\r\n");
      out.write("\t\t&nbsp;\r\n");
      out.write("\t\t<img src=\"images/bluedot.gif\" width=1 height=25 border=0>\r\n");
      out.write("\t\t&nbsp;\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<a href=\"search.jsp?forum=-1\"\r\n");
      out.write("\t\t\t><img src=\"images/search_tb_left.gif\" height=30 alt=\"Search\" border=\"0\"\r\n");
      out.write("\t\t\t><img src=\"images/search_tb_right.gif\" height=30 border=\"0\"\r\n");
      out.write("\t\t></a>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t");
 } 
      out.write('\r');
      out.write('\n');
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<hr size=\"0\">\r\n");
      out.write("<br>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr>\r\n");
      out.write("<td width=\"1%\"><img src=\"images/blank.gif\" width=30 height=1 border=0></td>\r\n");
      out.write("<td width=\"98%\" valign=\"top\">\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<font face=\"verdana\">\r\n");
      out.write("\t<b>Yazd example skin</b>\r\n");
      out.write("\t</font>\r\n");
      out.write("\t<p>\r\n");
      out.write("\t\r\n");
	// check to see if there are no forums
	if( !forumIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\t<ul>\r\n");
      out.write("\t\t\tNo forums in the system, or you don't have permission to view forums.\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\r\n");
	} else { 
      out.write("\r\n");
      out.write("\t\t<table border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\r\n");
 		while( forumIterator.hasNext() ) {
				Forum 	forum 		= (Forum)forumIterator.next();
				int 	id		 	= forum.getID();
				String 	name 		= forum.getName();
				String 	description	= forum.getDescription();
				int 	numThreads 	= forum.getThreadCount();
				int 	numMessages = forum.getMessageCount();
				long 	lastModified= forum.getModifiedDate().getTime();

      out.write("\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t\t\t");
	if (lastModified > lastVisited) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<img src=\"images/new.gif\">\r\n");
      out.write("\t\t\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t&#149;\r\n");
      out.write("\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td width=\"99%\">\r\n");
      out.write("\t\t\t\t<font face=\"verdana\" size=2>\r\n");
      out.write("\t\t\t\t<b><a href=\"viewForum.jsp?forum=");
      out.print( id );
      out.write("\" class=\"normal\">\r\n");
      out.write("\t\t\t\t\t");
      out.print( name );
      out.write("\r\n");
      out.write("\t\t\t\t</a></b>\r\n");
      out.write("\t\t\t\t<i>(");
      out.print( description );
      out.write(")</i>\r\n");
      out.write("\t\t\t\t</font>\r\n");
      out.write("\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t<font face=\"verdana\" size=1>\r\n");
      out.write("\t\t\t\t(");
      out.print( numThreads );
      out.write(' ');
      out.write(' ');
      out.print( (numThreads==1)  ? " thread"  : " threads" );
      out.write(",\r\n");
      out.write("\t\t\t\t ");
      out.print( numMessages );
      out.write(' ');
      out.print( (numMessages==1) ? " message" : " messages" );
      out.write(")\r\n");
      out.write("\t\t\t\t</font>\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
		} 
      out.write("\r\n");
      out.write("\t\t</table>\r\n");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("</td>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<td valign=\"top\" width=\"1%\">\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellpadding=\"1\" cellspacing=\"0\" border=\"0\" width=\"220\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "recentMessages.jsp", out, true);
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("</td>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: footer.jsp,v $
 *	$Revision: 1.3 $
 *	$Date: 2000/12/18 02:05:55 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("<hr size=\"0\">\r\n");
      out.write("\r\n");
      out.write("<center>\r\n");
      out.write("\t<font face=\"verdana\" size=\"-1\">\r\n");
      out.write("\t<a href=\"http://www.Yasna.com/yazd/\" class=\"normal\"\r\n");
      out.write("\t>www.Yasna.com/yazd</a>\r\n");
      out.write("\t</font>\r\n");
      out.write("</center>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write('\r');
      out.write('\n');
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
