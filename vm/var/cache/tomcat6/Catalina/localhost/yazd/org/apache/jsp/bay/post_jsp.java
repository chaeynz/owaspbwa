package org.apache.jsp.bay;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.text.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.util.*;

public final class post_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	final static String DEFAULT_REDIRECT_PAGE = "viewMessage.jsp";
	final static String STR_TYPE_NEW 	= "new";
	final static String STR_TYPE_REPLY 	= "reply";
	final static String STR_TYPE_EDIT 	= "edit";
	final static String STR_ACTION_DOPOST = "doPost";
	final static int 	TYPE_NEW 	= 1;
	final static int 	TYPE_REPLY 	= 2;
	final static int 	TYPE_EDIT 	= 4;
	final static int 	ACTION_DOPOST = 1;
	SimpleDateFormat dateFormatter = new SimpleDateFormat( "EEEE, MMM d, yyyy" );
	SimpleDateFormat timeFormatter = new SimpleDateFormat( "h:mm a, z" );

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
 *	$RCSfile: post.jsp,v $
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
	// Get parameter values
	int 	forumID 		= ParamUtils.getIntParameter(request, "forum", -1);		// forumID: Forum to post to
	int 	threadID 		= ParamUtils.getIntParameter(request, "thread", -1);	// parentID: Parent message ID
	int 	parentID 		= ParamUtils.getIntParameter(request, "message", -1);	// threadID: Thread to post to
	String 	redirectPage 	= ParamUtils.getParameter(request, "redirectPage");		// redirectPage: Where to go when this post is successful
	String 	postTypeStr 	= ParamUtils.getParameter(request, "postType");			// postType: either "new" or "reply" or "edit"
	String 	postActionStr	= ParamUtils.getParameter(request, "postAction");		// postAction: either blank or "doPost"
	String 	subject 		= ParamUtils.getParameter(request, "subject");			// subject: subject of the message
	String 	body	 		= ParamUtils.getParameter(request, "body");				// body: body of the message
	int		postType 		= 0;
	int		postAction 		= 0;

	if (redirectPage == null || redirectPage.length() == 0) {
		redirectPage = DEFAULT_REDIRECT_PAGE;
	}

	// New is default postType
	if (postTypeStr == null) {
		postType = TYPE_NEW;
		postTypeStr = STR_TYPE_NEW;
	} else if (STR_TYPE_NEW.equals(postTypeStr)) {
		postType = TYPE_NEW;
	} else if (STR_TYPE_REPLY.equals(postTypeStr)) {
		postType = TYPE_REPLY;
	} else if (STR_TYPE_EDIT.equals(postTypeStr)) {
		postType = TYPE_EDIT;
	}

	if (STR_ACTION_DOPOST.equals(postActionStr)) {
		postAction = ACTION_DOPOST;
	}

      out.write("\r\n");
      out.write("\r\n");
	// Get a ForumFactory object, check if it is null. If it is null, redirect to the error page.
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	// Get a forum object, redirect on error:
	Forum forum = forumFactory.getForum(forumID);
	
	// Get a profile manager and create the user object. If the userID of the authorization
	// token can't be found, redirect to the error page
	ProfileManager manager = forumFactory.getProfileManager();
	User thisUser = manager.getUser( authToken.getUserID() );

      out.write("\r\n");
      out.write("\r\n");
	// If this is a reply, setup the parent message and the thread objects
	ForumThread thread = null;
	ForumMessage parentMessage = null;
	if (postType == TYPE_REPLY || postType == TYPE_EDIT) {
		thread = forum.getThread( threadID );			// throws ForumThreadNotFoundException
		parentMessage = thread.getMessage( parentID );	// throws ForumMessageNotFoundException
	}

      out.write("\r\n");
      out.write("\r\n");
	// Create the message only if we're posting or replying to a message:
	if (postAction == ACTION_DOPOST) {
		ForumMessage newMessage = null;
		int messageID = -1;
        if (subject == null || body == null) {
			throw new Exception( "Please enter the " + (subject == null ? "subject " : "")  + (body == null ? "message" : ""));
        }
		//try {
			if (postType == TYPE_EDIT) {
                if (parentMessage.getUser().getID() != authToken.getUserID() ||
            	    (System.currentTimeMillis() > parentMessage.getCreationDate().getTime() + 3 * 3600 * 1000)) {
					throw new Exception( "The message can only be changed within 2-3 hours after creation" );
                }
    			parentMessage.setSubject( subject );
    			parentMessage.setBody( body );
    			parentMessage.setProperty( "IP", request.getRemoteAddr() );
    			messageID = parentID;
			} else {
    			newMessage = forum.createMessage( thisUser );
    			newMessage.setSubject( subject );
    			newMessage.setBody( body );
    			newMessage.setProperty( "IP", request.getRemoteAddr() );
    			messageID = newMessage.getID();
    			if (postType == TYPE_REPLY) {
    				thread.addMessage( parentMessage, newMessage );
    			}
    			else if (postType == TYPE_NEW) {
    				// make new thread with the new message as the root.
    				thread = forum.createThread(newMessage);
    				forum.addThread(thread);
    				threadID = thread.getID();
    			}
			}
		//}
		//catch( UnauthorizedException ue) {
		//	System.err.println( "servletforum/post.jsp: " + ue );
		//	response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("Not authorized to post messages here.") );
		//	return;
		//}
		
		// at this point, the message was created and added successfully,
		// so redirect to the redirect page:
		response.sendRedirect( redirectPage + "?forum=" + forumID + "&thread=" + threadID + "&parent=" + parentID + "&message=" + messageID);
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	/////////////////
	// header include
	
	String title = "Yazd: Post Message";

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
      out.write("<font face=\"verdana\" size=\"2\"><b><a href=\"index.jsp\" class=\"normal\">Home</a>\r\n");
      out.write("&gt;\r\n");
      out.write("<a href=\"viewForum.jsp?forum=");
      out.print( forumID );
      out.write("\" class=\"normal\">");
      out.print( forum.getName() );
      out.write("</a>\r\n");
      out.write("&gt;</b>\r\n");
	if (postType == TYPE_REPLY) { 
      out.write("\r\n");
      out.write("\tReply to: <b>");
      out.print( parentMessage.getSubject() );
      out.write("</b>\r\n");
	} else if (postType == TYPE_NEW) { 
      out.write("\r\n");
      out.write("\tPost a new message\r\n");
	} 
      out.write("\r\n");
      out.write("</font>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
	if (postType == TYPE_REPLY || postType == TYPE_EDIT) { 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<table bgcolor=\"#666666\" cellpadding=1 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<table bgcolor=\"#dddddd\" cellpadding=3 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t\t<td bgcolor=\"#dddddd\" width=\"99%\">\r\n");
      out.write("\t\t\t<font face=\"Trebuchet MS\">\r\n");
      out.write("\t\t\t<b>");
      out.print( parentMessage.getSubject() );
      out.write("</b>\r\n");
      out.write("\t\t\t</font>\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<font face=\"verdana\" size=2>\r\n");
      out.write("\t\t\t<b>\r\n");
      out.write("\t\t\t");
	if (!parentMessage.isAnonymous()) { 
					User parentUser = parentMessage.getUser();
			
      out.write("\r\n");
      out.write("\t\t\t\t\t<a href=\"mailto:");
      out.print( parentUser.getEmail() );
      out.write("\" class=\"normal\">");
      out.print( parentUser.getName() != null ? parentUser.getName() : parentUser.getUsername() );
      out.write("</a>\r\n");
      out.write("\t\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t[Anonymous]\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</b>\r\n");
      out.write("\t\t\t</font>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td bgcolor=\"#dddddd\" width=\"1%\" nowrap>\r\n");
      out.write("\t\t\t<font face=\"verdana\" size=1>\r\n");
      out.write("\t\t\t<b>\r\n");
      out.write("\t\t\t");
 	java.util.Date d = parentMessage.getCreationDate(); 
      out.write("\r\n");
      out.write("\t\t\t");
      out.print( dateFormatter.format(d) );
      out.write(' ');
      out.write('-');
      out.write(' ');
      out.print( timeFormatter.format(d) );
      out.write("\r\n");
      out.write("\t\t\t</b>\r\n");
      out.write("\t\t\t</font>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t<table bgcolor=\"#666666\" cellpadding=0 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t\t<td><img src=\"images/blank.gif\" width=1 height=1 border=0></td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t<table bgcolor=\"#ffffff\" cellpadding=5 cellspacing=0 border=0 width=\"100%\">\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t");
      out.print( parentMessage.getBody() );
      out.write("\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\r\n");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form action=\"post.jsp\" method=\"post\" name=\"postForm\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"message\" value=\"");
      out.print( parentID );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"thread\" value=\"");
      out.print( threadID );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"postType\" value=\"");
      out.print( postTypeStr );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"postAction\" value=\"");
      out.print( STR_ACTION_DOPOST );
      out.write("\">\r\n");
      out.write("\r\n");
      out.write("\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t<font face=\"verdana\" size=\"2\"><b>\r\n");
      out.write("\t\t\t");
 if (postType == TYPE_EDIT) { 
      out.write("\r\n");
      out.write("\t\t\t\tEdit your message:\r\n");
      out.write("\t\t\t");
 } else if (postType == TYPE_REPLY) { 
      out.write("\r\n");
      out.write("\t\t\t\tReply to this message:\r\n");
      out.write("\t\t\t");
 } else if (postType == TYPE_NEW) { 
      out.write("\r\n");
      out.write("\t\t\t\tPost a new message:\r\n");
      out.write("\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t</b></font>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\t<p>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\t<table>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td><font face=\"verdana\" size=\"2\"><b>Subject</b></font></td>\r\n");
      out.write("\t\t\t\t\t");
	String parentSubject = "";
						if (parentMessage != null) {
							parentSubject = parentMessage.getSubject();
                            if (postType == TYPE_REPLY) {
    							if (parentSubject == null) {
    							    	parentSubject = "Re: your post";
    							} else if (!parentSubject.startsWith( "Re:" )) {
    								parentSubject = "Re: " + parentSubject;
    							}
                            }
						}
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<td><input type=\"text\" name=\"subject\" value=\"");
      out.print( parentSubject );
      out.write("\" size=\"50\" maxlength=\"100\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t");
	if (postType == TYPE_REPLY) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td><br></td>\r\n");
      out.write("\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t");
 
						/*** <font face="verdana,arial,helvetica" size="1">
						[ <a href="" onmouseover="window.status='Click to quote the original message.';return true;" onmouseout="window.status='';return true;" onclick="document.postForm.message.value=quoteOrig(document.postForm.message);return false;" style="text-decoration:none;">Quote original</a> ]
						</font> ***/
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<br></td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td valign=\"top\"><font face=\"verdana\" size=\"2\"><b>\r\n");
      out.write("\t\t\t\t\t");
	if (postType == TYPE_REPLY) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\tReply\r\n");
      out.write("\t\t\t\t\t");
	} else if (postType == TYPE_NEW) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\tQuestion\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</b></font></td>\r\n");
      out.write("\t\t\t\t\t");
	String parentBody = "";
						if (parentMessage != null && postType == TYPE_EDIT) {
						    parentBody = parentMessage.getBody();
/***/		// QQQ unfilter waiting for interface change...
			parentBody = StringUtils.replace(parentBody, "<BR>", "\n");
			parentBody = StringUtils.replace(parentBody, "<b>", "[b]");
			parentBody = StringUtils.replace(parentBody, "</b>", "[/b]");
			parentBody = StringUtils.replace(parentBody, "<i>", "[i]");
			parentBody = StringUtils.replace(parentBody, "</i>", "[/i]");
/***/
						}
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<td><textarea name=\"body\" cols=\"50\" rows=\"10\" wrap=\"virtual\">");
      out.print( parentBody );
      out.write("</textarea></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td><br></td>\r\n");
      out.write("\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t\t<input type=\"submit\" value=\"Post Message\">\r\n");
      out.write("\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
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
