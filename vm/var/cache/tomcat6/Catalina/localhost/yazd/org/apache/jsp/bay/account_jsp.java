package org.apache.jsp.bay;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.util.*;

public final class account_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	final int CREATE = 1;
	final int MANAGE = 2;
	final int PASSWORD = 3;
	final int LOGIN = 4;
	final int VIEW = 5;

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
 *	$RCSfile: account.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/27 22:39:45 $
 */

      out.write("\r\n");
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
	// Get parameters
	int     mode		= ParamUtils.getIntParameter(request, "mode", -1);
	int     userID 		= ParamUtils.getIntParameter(request, "user", -1);
	int     forumID		= ParamUtils.getIntParameter(request, "forum", -1);
	boolean doCreate 	= ParamUtils.getBooleanParameter(request, "doCreate");
	String  username 	= ParamUtils.getParameter(request, "username");	    // required to create account
	String  password 	= ParamUtils.getParameter(request, "password");		// required to create account
	String  password2 	= ParamUtils.getParameter(request, "password2");		// required to create account
	String  email    	= ParamUtils.getParameter(request, "email");		// required to create account
	String  URL			= ParamUtils.getParameter(request, "URL");
	String  name		= ParamUtils.getParameter(request, "name");
	String  sig			= ParamUtils.getParameter(request, "signature");
	boolean emailVisible= ParamUtils.getCheckboxParameter(request, "emailVisible");
	boolean nameVisible	= ParamUtils.getCheckboxParameter(request, "nameVisible");
	boolean autoLogin   = ParamUtils.getCheckboxParameter(request, "autoLogin");
	String  message		= ParamUtils.getParameter(request, "message");
	
	if (message == null)
	    message = "";
	
	boolean emailOK				= ( email != null && email.length() != 0 );
	boolean usernameOK			= ( username != null && username.length() != 0 );
	boolean passwordOK			= ( password != null && password.length() != 0 );
	passwordOK 					= ((mode == LOGIN && passwordOK) ||
								   (mode == CREATE && (passwordOK && password.equals(password2))) ||
								   (mode == MANAGE && ((password == null && password2 == null) || (passwordOK && password.equals(password2)))) );
	
	boolean requiredParamsOK	= ( emailOK && usernameOK && passwordOK );
	boolean createSuccess 		= false;
	String	redirectPage		= "account.jsp?mode=" + mode; 
	User 	user = null;

      out.write("\r\n");
      out.write("\r\n");
	// Create a ForumFactory object
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ProfileManager manager = forumFactory.getProfileManager();

      out.write("\r\n");
      out.write("\r\n");
	// Login in
	if (mode == LOGIN) {
		// check to make sure the username and password are valid (ie, not null or blank)
		if( !usernameOK || !passwordOK ) {
			message = "Login failed. Please make sure your username and password are correct.";
			response.sendRedirect( "account.jsp?message=" + URLEncoder.encode(message) );
			return;
		}
		else {
			try {
				// get the user's authorization token
				authToken = SkinUtils.setUserAuthorization(request, response, username, password, autoLogin);

				// redirect to the main page
				response.sendRedirect( "index.jsp" );
				return;
			}
			catch( UnauthorizedException ue ) {
				message = "Login failed. Please make sure your username and password are correct.";
				response.sendRedirect( "account.jsp?message=" + URLEncoder.encode(message) );
				return;
			}
		}
	}
	
	// Create a new user, or change your account
	// check to make sure username, password and email are valid (ie, not null or blank)
	else if (doCreate && requiredParamsOK && (mode == CREATE || mode == MANAGE)) {
		try {
			if (mode == CREATE) {
				user = manager.createUser(username, password, email);	// throws a UserAlreadyExistsException
				message = "Account created successfully!";
			} else {
				user = manager.getUser(authToken.getUserID());
				message = "Account updated successfully!";
			}
			
			if (name != null && !name.equals(user.getName())) {
				user.setName(name);
			}
			if (password != null && mode == MANAGE) {
				user.setPassword(password);
			}
			if (email != null && !email.equals(user.getEmail())) {
				user.setEmail(email);
			}
			if (nameVisible != user.isNameVisible()) {
				user.setNameVisible(nameVisible);
			}
			if (emailVisible != user.isEmailVisible()) {
				user.setEmailVisible(emailVisible);
			}
			// IP, URL and Signature are extended properties:
			if (!request.getRemoteAddr().equals(user.getProperty("IP"))) {
				user.setProperty("IP", request.getRemoteAddr());
			}
			if (URL != null && !URL.equals(user.getProperty("URL"))) {
				user.setProperty("URL", URL);
			}
			if (sig != null && !sig.equals(user.getProperty("sig"))) {
				user.setProperty("sig", sig);
			}

			if (mode == CREATE) {
				authToken = SkinUtils.setUserAuthorization(request, response, username, password, autoLogin);
			}

			response.sendRedirect( redirectPage + "&message=" + URLEncoder.encode(message) );
			return;
		}
		catch( UserAlreadyExistsException uaee ) { 
			message = "Sorry, that username is taken.";
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
		catch( UserNotFoundException unfe ) {
			message = "Oops, you do not seem to exist.";
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
		catch( UnauthorizedException ue ) {
			java.io.StringWriter sw = new java.io.StringWriter();
			ue.printStackTrace(new PrintWriter(sw,true));
			message = "You are not authorized." + sw.toString();
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
	}

	// View or Update a users attributes
	// check to make sure username, password and email are valid (ie, not null or blank)
	else if (!doCreate && (mode == VIEW || mode == MANAGE)) {
		try {
			if (mode == VIEW) {
// userID?				user = manager.getUser(authToken.getUserID());
				message = "Account created successfully!";
			} else {
				user = manager.getUser(authToken.getUserID());
			}

			username = user.getUsername();
			name = user.getName();
			if (name == null) {
				name = (mode == VIEW) ? "<i>Not visible</i>" : "";
			}
			email = user.getEmail();
			if (email == null) {
				email = (mode == VIEW) ? "<i>Not visible</i>" : "";
			}
			nameVisible = user.isNameVisible();
			emailVisible = user.isEmailVisible();
			URL = user.getProperty("URL");
			sig = user.getProperty("sig");
		}
		catch( UserNotFoundException unfe ) {
			message = "Oops, you do not seem to exist.";
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
	}
	else if (!doCreate && (mode == CREATE)) {
		nameVisible = true;
		emailVisible = true;
	}

      out.write("\r\n");
      out.write("\r\n");
	// header include
	String title = "Manage Your Account";

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
      out.write("<font face=\"verdana\" size=\"-1\"><b><a href=\"index.jsp\" class=\"normal\">Home</a>\r\n");
      out.write("&gt;\r\n");
	if( mode > 0 ) { 
      out.write("\r\n");
      out.write("\t<a href=\"account.jsp\" class=\"normal\">User Account</a>\r\n");
      out.write("\t&gt;\r\n");
      out.write("\t");
	if( mode == CREATE ) { 
      out.write("\r\n");
      out.write("\t\tCreate an account\r\n");
      out.write("\t");
	} else if( mode == MANAGE ) { 
      out.write("\r\n");
      out.write("\t\tManage your account\r\n");
      out.write("\t");
	} else if( mode == PASSWORD ) { 
      out.write("\r\n");
      out.write("\t\tPassword help\r\n");
      out.write("\t");
	} else if( mode == LOGIN ) { 
      out.write("\r\n");
      out.write("\t\tUser Account Login\r\n");
      out.write("\t");
	} else { 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t");
	} 
      out.write('\r');
      out.write('\n');
	} else { 
      out.write("\r\n");
      out.write("\tUser Account Login\r\n");
	} 
      out.write("\r\n");
      out.write("</b></font>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("<font color=\"#ff0000\">");
      out.print( message );
      out.write("</font>\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
	if(mode == CREATE || mode == MANAGE) { 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<p>\r\n");
      out.write("\r\n");
      out.write("\t");
	// if we're trying to create a user and there's an error, print a message:
		if( !createSuccess && doCreate ) { 
      out.write("\r\n");
      out.write("\t\tUser account creation failed. Please correct the marked fields.\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<ul>\r\n");
      out.write("\t\t<table cellpadding=3 cellspacing=0 border=0>\r\n");
      out.write("\t\t<form action=\"account.jsp\" method=\"post\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"mode\" value=\"");
      out.print( mode );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doCreate\" value=\"true\">\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">\r\n");
      out.write("\t\t\t\t<b>Username:</b>\r\n");
      out.write("\t\t\t\t");
	if( !usernameOK && doCreate ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<font size=4 color=\"#ff0000\" face=\"arial,helvetica\"><b>*</b></font>\r\n");
      out.write("\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>\r\n");
      out.write("\t\t\t\t");
 if (mode == CREATE) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<input type=\"text\" name=\"username\" value=\"");
      out.print( username == null ? "" : username );
      out.write("\" size=20 maxlength=30>\r\n");
      out.write("\t\t\t\t\t<i>(required)</i>\r\n");
      out.write("\t\t\t\t");
 } else {
      out.write("\r\n");
      out.write("\t\t\t\t\t<input type=\"hidden\" name=\"username\" value=\"");
      out.print( username == null ? "" : username );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t");
      out.print( username == null ? "[error]" : username );
      out.write("\r\n");
      out.write("\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">\r\n");
      out.write("\t\t\t\t<b>Password:</b>\r\n");
      out.write("\t\t\t\t");
	if( !passwordOK && doCreate ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<font size=4 color=\"#ff0000\" face=\"arial,helvetica\"><b>*</b></font>\r\n");
      out.write("\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td><input type=\"password\" name=\"password\" value=\"\">\r\n");
      out.write("\t\t\t\t");
 if (mode == CREATE) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<i>(required)</i>\r\n");
      out.write("\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">\r\n");
      out.write("\t\t\t\t<b>Confirm:</b>\r\n");
      out.write("\t\t\t\t");
	if( !passwordOK && doCreate ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<font size=4 color=\"#ff0000\" face=\"arial,helvetica\"><b>*</b></font>\r\n");
      out.write("\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td><input type=\"password\" name=\"password2\" value=\"\">\r\n");
      out.write("\t\t\t\t");
 if (mode == CREATE) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<i>(required)</i>\r\n");
      out.write("\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">\r\n");
      out.write("\t\t\t\t<b>Email address:</b>\r\n");
      out.write("\t\t\t\t");
	if( !emailOK && doCreate ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<font size=4 color=\"#ff0000\" face=\"arial,helvetica\"><b>*</b></font>\r\n");
      out.write("\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td><input type=\"text\" name=\"email\" value=\"");
      out.print( email == null ? "" : email );
      out.write("\" size=30 maxlength=30>\r\n");
      out.write("\t\t\t\t<i>(required)</i>\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">Name:</td>\r\n");
      out.write("\t\t\t<td><input type=\"text\" name=\"name\" value=\"");
      out.print( name == null ? "" : name );
      out.write("\" size=40 maxlength=50></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">URL:</td>\r\n");
      out.write("\t\t\t<td><input type=\"text\" name=\"URL\" value=\"");
      out.print( URL == null ? "" : URL );
      out.write("\" size=40 maxlength=255></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">Signature:<br><font size=1>(255 characters only)</font></td>\r\n");
      out.write("\t\t\t<td><textarea cols=40 rows=4 name=\"signature\" wrap=\"virtual\">");
      out.print( sig == null ? "" : sig );
      out.write("</textarea></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">Show my name:</td>\r\n");
      out.write("\t\t\t<td><input type=\"checkbox\" name=\"nameVisible\" ");
      out.print( nameVisible ? "checked" : "" );
      out.write("></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td align=\"right\">Show my email:</td>\r\n");
      out.write("\t\t\t<td><input type=\"checkbox\" name=\"emailVisible\" ");
      out.print( emailVisible ? "checked" : "" );
      out.write("></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t");
 if (mode == CREATE) { 
      out.write("\r\n");
      out.write("\t    \t<tr>\r\n");
      out.write("\t\t\t\t<td align=\"right\"><font face=\"verdana\" size=\"-1\">Auto login:</font></td>\r\n");
      out.write("\t\t\t\t<td><input type=\"checkbox\" name=\"autoLogin\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td><br></td>\r\n");
      out.write("\t\t\t<td><input type=\"submit\" value=\"");
      out.print( mode == CREATE ? "Create account" : "Update account" );
      out.write("\"></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</ul>\r\n");
      out.write("\t\r\n");
	} else if( mode == PASSWORD ) { 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<font face=\"verdana\" size=\"-1\">\r\n");
      out.write("\t<b>Send your password</b>\r\n");
      out.write("\t<ul>\r\n");
      out.write("\t\t<li> Not yet implemented.\r\n");
      out.write("\t</ul>\r\n");
      out.write("\t</font>\r\n");
      out.write("\t\t\r\n");
      out.write('\r');
      out.write('\n');
	} else { 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t");
	user = manager.getUser( authToken.getUserID() );
		boolean anonUser = user.isAnonymous();
	
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<font face=\"verdana\" size=2>\r\n");
      out.write("\t\r\n");
      out.write("\t");
	if (anonUser) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"account.jsp\" method=\"post\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"mode\" value=\"");
      out.print( LOGIN );
      out.write("\">\r\n");
      out.write("\t\t<ul>\r\n");
      out.write("\t\t\t<table cellpadding=3 cellspacing=0 border=0>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td align=\"right\"><font face=\"verdana\" size=\"-1\"><i>username:</i></font></td>\r\n");
      out.write("\t\t\t\t<td><input type=\"text\" name=\"username\" size=\"20\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td align=\"right\"><font face=\"verdana\" size=\"-1\"><i>password:</i></font></td>\r\n");
      out.write("\t\t\t\t<td><input type=\"password\" name=\"password\" size=\"20\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("        \t<tr>\r\n");
      out.write("\t\t\t\t<td align=\"right\"><font face=\"verdana\" size=\"-1\"><i>auto login:</i></font></td>\r\n");
      out.write("\t\t\t\t<td><input type=\"checkbox\" name=\"autoLogin\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td align=\"right\" colspan=\"2\"><input type=\"submit\" value=\"Login\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\tDon't have an account? <a href=\"account.jsp?mode=");
      out.print( CREATE );
      out.write("\" class=\"normal\">Create one</a>\r\n");
      out.write("\t\t<p>\r\n");
      out.write("\t\tForget your <a href=\"account.jsp?mode=");
      out.print( PASSWORD );
      out.write("\" class=\"normal\" \r\n");
      out.write("\t\t             onclick=\"alert('Not implemented yet');return false;\">password?</a>\r\n");
      out.write("\t");
	} else { 
      out.write("\r\n");
      out.write("\t\tYou are logged in <b>");
      out.print( user.getUsername() );
      out.write("</b>.\r\n");
      out.write("\t\t<br><br>\r\n");
      out.write("\t\t[<a href=\"index.jsp?logout=true\" class=\"normal\">logout</a>]\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<br>\r\n");
      out.write("\t</font>\r\n");
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
