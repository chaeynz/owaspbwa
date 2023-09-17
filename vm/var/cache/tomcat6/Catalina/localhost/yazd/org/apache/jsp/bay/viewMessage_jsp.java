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

public final class viewMessage_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	final int DEFAULT_FORUM_ID = 1; // ServletForum.com Beta Forum ID
	final int IMG_WIDTH = 13;
	final int IMG_HEIGHT = 12;
	final SimpleDateFormat dateFormatter = new SimpleDateFormat( "EEEE, MMM d, yyyy" );
	final SimpleDateFormat timeFormatter = new SimpleDateFormat( "H:mm, z" );

	final String TREE_ROOT =    "<img src='images/t0.gif' align='top' width=8 height=20>";
	final String TREE_EMPTY =   "<img src='images/t0.gif' align='top' width=20>";
	final String TREE_LINE =    "<img src='images/t1.gif' align='top'>";
	final String TREE_CORNER =  "<img src='images/t2.gif' align='top'>";
	final String TREE_FORK =    "<img src='images/t3.gif' align='top'>";

	final String TREE_ARROW =   "<img src='images/t_arrow.gif' align='top'>";
	final String TREE_NEW =   	"<img src='images/t_new.gif' align='top'>";

	// Recursive method to print all the children of a message.
	private void printChildren(TreeWalker walker, ForumMessage message, ForumThread thread, 
								int currentMessageID, int level, int childCount, 
								StringBuffer buf, int messageID, int forumID,
								int lineNr[], String tree, long lastVisited,
								String forumParams) 
	{
		java.util.Date date = message.getModifiedDate();
		long	modifiedDate = date.getTime();
		String subj         = message.getSubject();
		String username 	= "Anonymous";
		if( !message.isAnonymous() ) {
			User user = message.getUser();
			username = user.getName();
			if (username == null)
    			username = user.getUsername();
		}
		int msgID           = message.getID();
		int thrID           = thread.getID();
		boolean onCurrent   = (message.getID() == messageID);
		boolean rootMsg     = (lineNr[0] == 0);
		String bgcolor      = "";
		
		if( subj == null || subj.equals("") ) {
			subj = "[no subject]";
		}
		if( (lineNr[0]++ & 1) != 0) {
			//bgcolor = " bgcolor='#dddddd'";
			bgcolor = " bgcolor='#eeeeee'";
		}
		else {
			bgcolor = " bgcolor='#ffffff'";
		}
		if( onCurrent ) {
			bgcolor = " bgcolor='#dddddd'";
			//bgcolor = " bgcolor='#ffffff'";
		}
		
		// print start of row, with appropriate background color
		buf.append("<tr valign=middle").append( bgcolor ).append(">\n");
		
		// start of subject cell
		if( onCurrent ) {
			buf.append("<td width='98%' class='subjectOn'><font size=2>");
		} else {
			buf.append("<td width='98%' class='subject'><font size=2>");
		}
		
		// padding images
		if (rootMsg) {
		    if (!onCurrent)
    		    buf.append(TREE_ROOT);
		} else {
    		buf.append(tree);
    		if (childCount > 0)
    		    buf.append(TREE_FORK);
    		else
    		    buf.append(TREE_CORNER);
		}
		
		if (onCurrent) {
			buf.append(TREE_ARROW);
		}

		// subject cell
		if (modifiedDate > lastVisited) {
			buf.append(TREE_NEW);
		}
		if (!onCurrent) {
			buf.append("<a href='viewMessage.jsp?message=").append(msgID);
			buf.append("&thread=").append(thrID);
			buf.append("&parent=").append(currentMessageID);
			buf.append("&forum=").append(forumID);
			if (forumParams.length() > 0)
				buf.append(forumParams);
			buf.append("'>");
		} 
		else {
			buf.append("<a name='currentMsg'>");
		}
		buf.append(subj);
		buf.append("</a>");
		buf.append("</font></td>").append("\n");
		
		// username cell
		if( onCurrent ) {
			buf.append("<td width='1%' nowrap align='center' class='usernameOn'><font size=2>&nbsp;");
		} else {
			buf.append("<td width='1%' nowrap align='center' class='username'><font size=2>&nbsp;");
		}
		buf.append(username);
		buf.append("&nbsp;</font></td>");
		
		// date cell
		if( onCurrent ) {
			buf.append("<td width='1%' nowrap class='datetimeOn'><font size=1>");
		} else {
			buf.append("<td width='1%' nowrap class='datetime'><font size=1>");
		}
		buf.append(dateFormatter.format(date)).append(" - ").append(timeFormatter.format(date));
		buf.append("</font></td>").append("\n");
		
		// print end of row
		buf.append("</tr>\n");

		// recursive call
		if (!rootMsg) {
    		if (childCount > 0)
    		    tree += TREE_LINE;
    		else
    		    tree += TREE_EMPTY;
		}
        int numChildren = walker.getChildCount(message); // keep
        if( numChildren > 0 ) {
            for( int i=0; i<numChildren; i++ ) {
                ForumMessage child = walker.getChild(message, i);
                printChildren( walker, child, thread, message.getID(), ++level, numChildren - i -1, buf, messageID, forumID, lineNr, tree, lastVisited, forumParams);
				level--;
            }
        }
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
 *	$RCSfile: viewMessage.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/27 22:39:45 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
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
	// get parameter values
	int 	parentID  	= ParamUtils.getIntParameter(request, "parent", -1);
	int 	threadID  	= ParamUtils.getIntParameter(request, "thread", -1);
	int 	messageID 	= ParamUtils.getIntParameter(request, "message", -1);
	int 	forumID   	= ParamUtils.getIntParameter(request, "forum", -1);
	String  forumParams = ParamUtils.getParameter(request, "forumparams");
	String  forumParamsEncoded = (forumParams == null) ? "" : "&forumparams=" + URLEncoder.encode(forumParams);
	long	lastVisited = SkinUtils.getLastVisited(request, response);
	int 	nextID  	= -1;
	int 	previousID  = -1;

      out.write("\r\n");
      out.write("\r\n");
	// Get a ForumFactory object
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);

	// Get a forum object, redirect on error:
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	}
	catch( UnauthorizedException ue ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("You don't have access to use this forum") );
		return;
	}
	catch( ForumNotFoundException fnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("Can't view a forum that doesn't exist.") );
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	
	// Get the thread, then message
	ForumThread thread = null;
	int threadChildCount = 0;
	try {
		thread = forum.getThread(threadID);
		threadChildCount = thread.getMessageCount()-1;
	}
	catch( ForumThreadNotFoundException tnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("The thread does not exist") );
		return;
	}
	
	ForumMessage message = null;
	try {
		if (messageID < 0) {
			message = thread.getRootMessage();
		}
		else {
			message = thread.getMessage(messageID);
		}
	}
	catch( ForumMessageNotFoundException mnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("The message does not exist") );
		return;
	}

	//Get the TreeWalker	
	TreeWalker walker = thread.treeWalker();
	ForumMessage root = walker.getRoot();
	int numRecursiveChildren = walker.getRecursiveChildCount(message);
		
	ForumMessage parent = null;

	if (parentID != -1) {
		parent = thread.getMessage(parentID);
		
		int numParentsChildren = walker.getChildCount(parent);
	
		//Get the previous and next messages in the thread.
		int currentChildNum = walker.getIndexOfChild(parent,message);
		
		if (currentChildNum >= numParentsChildren-1) {
			nextID = -1;
		}
		else {
			nextID = walker.getChild(parent,currentChildNum + 1).getID();	
		} 
		if (currentChildNum > 0) {
			previousID = walker.getChild(parent,currentChildNum - 1).getID();
		}
	}

      out.write("\r\n");
      out.write("\r\n");
	/////////////////
	// header include
	
	String title = (message == null) ? "Message not found" : message.getSubject();

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
      out.write("<font face=\"verdana\" size=\"2\"><b><a href=\"index.jsp\" class=\"normal\">Home</a>\r\n");
      out.write("&gt;\r\n");
      out.write("<a href=\"viewForum.jsp?forum=");
      out.print( forumID );
      out.print( forumParams == null ? "" : forumParams );
      out.write("\" class=\"normal\">");
      out.print( forum.getName() );
      out.write("</a>\r\n");
      out.write("&gt;\r\n");
      out.print( thread == null ? "" : thread.getName() );
      out.write("\r\n");
      out.write("</font>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=1 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("<tr>\r\n");
      out.write("<td>\r\n");
      out.write("\t<table bgcolor=\"#dddddd\" cellpadding=3 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<td bgcolor=\"#dddddd\" width=\"99%\">\r\n");
      out.write("\t\t");
	
			java.util.Date date = message.getModifiedDate(); 
			String subj         = message.getSubject();
			boolean canEdit     = false;
			boolean canReply    = message.hasPermission(ForumPermissions.CREATE_MESSAGE);

			if( subj == null || subj.equals("") ) {
				subj = "[no subject]";
			}
		
      out.write("\r\n");
      out.write("\t\t<font face=\"Trebuchet MS\">\r\n");
      out.write("\t\t<b>");
      out.print( subj );
      out.write("</b>\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<font face=\"verdana\" size=2>\r\n");
      out.write("\t\t<b>\r\n");
      out.write("\t\t");

			if( !message.isAnonymous() ) { 
				User user = message.getUser();
				String email = user.getEmail();
				String name = user.getName();
				if (name == null)
				    name = user.getUsername();

            	canEdit = canReply && (user.getID() == authToken.getUserID() &&
            	           System.currentTimeMillis() < date.getTime() + 2 * 3600 * 1000);

				if (email != null) {
		
      out.write("\r\n");
      out.write("\t\t\t\t\t<a href=\"mailto:");
      out.print( email );
      out.write("\" class=\"normal\">");
      out.print( name );
      out.write("</a>\r\n");
      out.write("\t\t");
		} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
      out.print( name );
      out.write("\r\n");
      out.write("\t\t");
		}
			} else { 
      out.write("\r\n");
      out.write("\t\t\t [Anonymous]\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t</b>\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td bgcolor=\"#dddddd\" width=\"1%\" nowrap>\r\n");
      out.write("\t\t<font face=\"verdana\" size=1>\r\n");
      out.write("\t\t<b>\r\n");
      out.write("\t\t");
 	java.util.Date d = message.getCreationDate(); 
      out.write("\r\n");
      out.write("\t\t");
      out.print( dateFormatter.format(d) );
      out.write(' ');
      out.write('-');
      out.write(' ');
      out.print( timeFormatter.format(d) );
      out.write("\r\n");
      out.write("\t\t</b>\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<table bgcolor=\"#666666\" cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<td><img src=\"images/blank.gif\" width=1 height=1 border=0></td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<table bgcolor=\"#eeeeee\" cellpadding=3 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t");
	if (canEdit) { 
      out.write("\r\n");
      out.write("    \t\t<a href=\"post.jsp?message=");
      out.print( messageID );
      out.write("&thread=");
      out.print( threadID );
      out.write("&forum=");
      out.print( forumID );
      out.print( forumParamsEncoded );
      out.write("&postType=edit\"><img src=\"images/edit_button.gif\" width=50 height=16 border=\"0\"></a> &nbsp;\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t");
	if (canReply) { 
      out.write("\r\n");
      out.write("\t\t\t<a href=\"post.jsp?message=");
      out.print( messageID );
      out.write("&thread=");
      out.print( threadID );
      out.write("&forum=");
      out.print( forumID );
      out.print( forumParamsEncoded );
      out.write("&postType=reply\"><img src=\"images/reply_button.gif\" width=50 height=16 border=\"0\"></a><br>\r\n");
      out.write("\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t\t&nbsp;\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<table bgcolor=\"#666666\" cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<td><img src=\"images/blank.gif\" width=1 height=1 border=0></td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<table bgcolor=\"#ffffff\" cellpadding=5 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t");
      out.print( message.getBody() );
      out.write('\r');
      out.write('\n');
	if( !message.isAnonymous() ) {
		User user = message.getUser();
		String sig = (String)user.getProperty("sig");

      out.write("\r\n");
      out.write("<pre>\r\n");
      out.print( (sig!=null)?sig:"" );
      out.write("</pre>\r\n");
	} 
      out.write("\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<font face=\"tahoma\" size=\"2\"><b>\r\n");
	if( numRecursiveChildren == 0 ) { 
      out.write("\r\n");
      out.write("\tNo replies to this message.\r\n");
 	} else { 
      out.write("\r\n");
      out.write("\t<a href=\"#currentMsg\">");
      out.print( numRecursiveChildren );
      out.write(" \r\n");
      out.write("\t");
      out.print( (numRecursiveChildren>1)?" replies":" reply" );
      out.write("</a> to this message.\r\n");
	} 
      out.write("\r\n");
      out.write("</b>\r\n");
	if (canReply) { 
      out.write("\r\n");
      out.write("\t[<a href=\"post.jsp?message=");
      out.print( messageID );
      out.write("&thread=");
      out.print( threadID );
      out.write("&forum=");
      out.print( forumID );
      out.print( forumParamsEncoded );
      out.write("&postType=reply\" class=\"normal\">Add your own</a>]\r\n");
	} 
      out.write("\r\n");
      out.write("</font>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#999999\" cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#999999\" cellpadding=0 cellspacing=1 border=0 width=\"100%\">\r\n");
	StringBuffer buf = new StringBuffer();
	int level = 0;
	int[] lineNr = { 0 };
	String tree = "";
	printChildren( walker, root, thread, -1, level, -1,  buf, messageID, forumID, lineNr, tree, lastVisited, forumParamsEncoded);  
	out.println( buf.toString() );

      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<br>\r\n");
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
