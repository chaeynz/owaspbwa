package org.apache.jsp.bay;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.*;
import java.net.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.util.*;

public final class viewForum_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	// Global variables
	//final SimpleDateFormat formatter = new SimpleDateFormat( "EE, MMM d" );
	final SimpleDateFormat formatter = new SimpleDateFormat( "EE, MMM d yyyy 'at' H:mm" );
	final SimpleDateFormat timeFormatter = new SimpleDateFormat( "h:mm a" );
	final SimpleDateFormat dayFormatter = new SimpleDateFormat( "EEEE" );
	final long ONE_DAY = 1000L * 60 * 60 * 24;
	final long ONE_WEEK = ONE_DAY * 7;
	final int DEFAULT_MSG_RANGE = 15;
	
	// Method to print one line on the thread table
	private String displayMessage(int forumID, ForumThread thread, ForumMessage message, String forumParams, long now, long lastVisited) {
		StringBuffer buf = new StringBuffer();
		if( message != null ) {
			Date messageDate = thread.getModifiedDate();
			long messageDateMS = messageDate.getTime(); 
			String subject = message.getSubject();
			int messageID = message.getID();
			String username = null;

			if( !message.isAnonymous() ) {
				User user = message.getUser();
				username = user.getName();
				if (username == null)
				    username = user.getUsername();
			}
			if (username == null)
			    username = "<i>Anonymous</i>";
			int threadID = thread.getID();
			int numReplies = thread.getMessageCount()-1;
			
			if( subject == null || subject.equals("") ) {
				subject = "[no subject]";
			}
			String dateText = "";
			
			if (messageDateMS >= (now - 2 * ONE_DAY)) {
				dateText = SkinUtils.dateToText(messageDate);
			}
			else {
				dateText = formatter.format(messageDate);
			}

			// new flag
			buf.append("<td width='1%'>");
			buf.append(messageDateMS > lastVisited ? "<img src=\"images/new.gif\">" : "&nbsp;");
			buf.append("</td>");

			// subject cell
			buf.append("<td width='96%'><font face='verdana' size='2'>");
			buf.append("<a href='viewMessage.jsp?message=").append(messageID).append("&thread=");
			buf.append(threadID).append("&forum=").append(forumID);
			if (forumParams.length() > 0)
				buf.append("&forumparams=").append(forumParams);
			buf.append("'><b>").append(subject);
			buf.append("</b></a>");
			//buf.append("&nbsp;[").append(numReplies).append("]");
			buf.append("</font></td>").append("\n");

			// replies cell
			buf.append("<td width='1%' align='center'>");
			buf.append("<font size='-1' color='#666666' face='verdana' size='2'>");
			buf.append("[");
			buf.append("<a href='viewMessage.jsp?message=").append(messageID).append("&thread=");
			buf.append(threadID).append("&forum=").append(forumID);
			if (forumParams.length() > 0)
				buf.append("&forumparams=").append(forumParams);			
			buf.append("'><b>").append(numReplies);
			buf.append("</b></a>");
			buf.append("]");
			buf.append("</font></td>");
			
			// username cell
			buf.append("<td width='1%' nowrap align='center'>");
			buf.append("<font size='-1' color='#666666' face='verdana' size='2'>");
			buf.append(username);
			buf.append("</font></td>");
			
			// date cell
			if( messageDateMS >= (now-ONE_WEEK) ) {
				buf.append("<td width='1%' nowrap class='dateTimeListToday' align='center'>");
			} else { 
				buf.append("<td width='1%' nowrap class='dateTimeList' align='center'>");
			}
			buf.append("<font face='verdana' size='2'>");
			buf.append( dateText );
			buf.append("</font></font>");
			buf.append("</td>").append("\n");

		}
		return buf.toString();
    }

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
 *	$RCSfile: viewForum.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/27 22:39:45 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
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
	// get parameters
	int 	forumID = 	ParamUtils.getIntParameter(request, "forum", -1);
	int 	range = 	ParamUtils.getIntParameter(request, "range", DEFAULT_MSG_RANGE);
	int 	start = 	ParamUtils.getIntParameter(request, "start", 0);
	long	lastVisited = SkinUtils.getLastVisited(request, response);
	String	startParam =  (start == 0) ? "" : "&start=" + start;
	String	rangeParam =  (range == DEFAULT_MSG_RANGE) ? "" : "&range=" + range;

      out.write("\r\n");
      out.write("\r\n");
	ForumThread thread = null;
	Forum forum = null;
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	try {
		forum = forumFactory.getForum(forumID); // throws ForumNotFoundException
	}
	catch( UnauthorizedException ue ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("You don't have access to view this forum.") );
		return;
	}
	catch( ForumNotFoundException fnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("Can't view a forum that doesn't exist.") );
		return;
	}

	String 	 forumName = forum.getName();
	Iterator forumIterator = forumFactory.forums();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////
	// header include
	
	String title = forumName;

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
      out.write("\r\n");
      out.write("<form>\r\n");
      out.write("<font face=\"verdana\" size=\"2\"><b><a href=\"index.jsp\" class=\"normal\">Home</a>\r\n");
      out.write("&gt;</b>\r\n");
      out.write("<font size=2><b>\r\n");
      out.write("<select name=\"forumName\" size=1 class=\"breadcrumbBox\" onchange=\"location=this.options[this.selectedIndex].value;\">\r\n");
      out.write("<option value=\"\" selected>");
      out.print( forumName );
      out.write("\r\n");
      out.write("<option value=\"viewForum.jsp?forum=");
      out.print( forumID );
      out.write("\">-----------------\r\n");
	while (forumIterator.hasNext()) {
		Forum tmpForum = (Forum)forumIterator.next();
		String name = tmpForum.getName();
		String location = "viewForum.jsp?forum=" + tmpForum.getID();

      out.write("\r\n");
      out.write("\t\t<option value=\"");
      out.print( location );
      out.write("\">&gt; ");
      out.print( name );
      out.write('\r');
      out.write('\n');
	}

      out.write("\r\n");
      out.write("</select>\r\n");
      out.write("</b></font>\r\n");
      out.write("</font>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("\r\n");
	Iterator it = forum.threads(start,range);
	if( !it.hasNext() ) {

      out.write("\r\n");
      out.write("\t\t<ul>\r\n");
      out.write("\t\t<font face=\"verdana\" size=2><b>\r\n");
      out.write("\t\tThere are no messages in this forum.\r\n");
      out.write("\t\t</b><br>\r\n");
      out.write("\t\t<a href=\"post.jsp?forum=");
      out.print( forumID );
      out.write("\" class=\"normal\"><i>Add your own message.</i></a>\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t\t\r\n");
	} else { 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellpadding=3 cellspacing=1 border=0 width=\"100%\">\r\n");
      out.write("\t<tr bgcolor=\"#dddddd\">\r\n");
      out.write("\t\t<td width=\"1%\"> &nbsp; </td>\r\n");
      out.write("\t\t<td align=\"center\" width=\"96%\"><font size=\"1\" face=\"tahoma,arial,helvetica\">subject</font></td>\r\n");
      out.write("\t\t<td align=\"center\" width=\"1%\" nowrap><font size=\"1\" face=\"tahoma,arial,helvetica\">replies</font></td>\r\n");
      out.write("\t\t<td align=\"center\" width=\"1%\" nowrap><font size=\"1\" face=\"tahoma,arial,helvetica\">posted by</font></td>\r\n");
      out.write("\t\t<td align=\"center\" width=\"1%\" nowrap><img src=\"images/arrow-up.gif\" width=8 height=7 border=0 hspace=6><font size=\"1\" face=\"tahoma,arial,helvetica\">date</font></td>\r\n");
      out.write("\t</tr>\r\n");
		long now = (new java.util.Date()).getTime();
		ForumMessage root =  null;
		int rowColor = 0;
		String bgcolor = "";
		String forumParams = URLEncoder.encode(startParam + rangeParam);
		int numThreadInForum = forum.getThreadCount();
		
		while(it.hasNext()) {
			rowColor++;
			thread = (ForumThread)it.next();
			root = thread.getRootMessage();
			if( rowColor%2 == 0 ) {
				bgcolor = "#ffffff";
			} else {
				bgcolor = "#eeeeee";
			}

      out.write("\r\n");
      out.write("\t\t\t<tr bgcolor=\"");
      out.print( bgcolor );
      out.write("\">\r\n");
      out.write("\t\t\t");
      out.print( displayMessage(forumID, thread, root, forumParams, now, lastVisited) );
      out.write("\r\n");
      out.write("\t\t\t</tr>\r\n");
		} 
      out.write("\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t\r\n");
      out.write("<table cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t\t<font size=\"-2\" face=\"verdana,arial\">\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t");
	if( (start-range) >= 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<a href=\"viewForum.jsp?forum=");
      out.print( forumID );
      out.print( rangeParam );
      out.write("&start=");
      out.print( (start-range) );
      out.write("\"><b>&lt;&lt;</b> previous ");
      out.print( range );
      out.write(" messages</a>\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</font>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td width=\"98%\" nowrap>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t\t<font size=\"-2\" face=\"verdana,arial\">\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t");
	if( (range+start) < numThreadInForum ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<a href=\"viewForum.jsp?forum=");
      out.print( forumID );
      out.print( rangeParam );
      out.write("&start=");
      out.print( (start+range) );
      out.write("\">next ");
      out.print( (((numThreadInForum-range)<=range)?(numThreadInForum-range):range) );
      out.write(" messages <b>&gt;&gt;</b></a>\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</font>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\r\n");
	} 
      out.write("\r\n");
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
