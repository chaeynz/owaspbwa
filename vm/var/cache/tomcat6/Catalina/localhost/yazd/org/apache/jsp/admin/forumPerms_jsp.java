package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.net.URLEncoder;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class forumPerms_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	////////////////////////
	// global page variables
	
	private final int READ = ForumPermissions.READ;
	private final int CREATE_THREAD = ForumPermissions.CREATE_THREAD;
	private final int CREATE_MESSAGE = ForumPermissions.CREATE_MESSAGE;
	private final int MODERATOR = ForumPermissions.MODERATOR;
	private final int[] perms = { READ, CREATE_THREAD, CREATE_MESSAGE,MODERATOR };
	private final String[] permDescriptions = {
		"read content of","post threads in","post messages in","forum moderator"
	};

	///////////////////
	// global methods
	
	private int[] getIntListboxParams( String[] paramVal ) {
		if( paramVal == null ) { 
			return new int[0]; 
		}
		int[] params = new int[paramVal.length];
		for (int i=0;i<paramVal.length;i++)
		{
			try {
				params[i] = Integer.parseInt(paramVal[i]);
			} catch( NumberFormatException nfe ) {}
		}
		return params;
	}

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
 *	$RCSfile: forumPerms.jsp,v $
 *	$Revision: 1.3.2.1 $
 *	$Date: 2001/02/09 20:38:41 $
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
      out.write(" \r\n");
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
	boolean isForumAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isForumAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// get parameters
	
	int forumID = ParamUtils.getIntParameter(request,"forum",-1);
	
	// action parameters
	boolean doAddUserPerm = ParamUtils.getBooleanParameter(request,"doAddUserPerm");
	boolean doAddGroupPerm = ParamUtils.getBooleanParameter(request,"doAddGroupPerm");
	boolean doRemoveUserPerm = ParamUtils.getBooleanParameter(request,"doRemoveUserPerm");
	boolean doRemoveGroupPerm = ParamUtils.getBooleanParameter(request,"doRemoveGroupPerm");
	
	int permType = ParamUtils.getIntParameter(request,"permType",-1);
	int[] usersWithPerm = getIntListboxParams(request.getParameterValues("usersWithPerm"));
	int[] groupsWithPerm = getIntListboxParams(request.getParameterValues("groupsWithPerm"));
	int[] userList = getIntListboxParams(request.getParameterValues("userList"));
	String userAddUsername = request.getParameter("userListField");
	int[] groupList = getIntListboxParams(request.getParameterValues("groupList"));
	int[] userPermTypesList = getIntListboxParams(request.getParameterValues("userPermTypes"));
	int[] groupPermTypesList = getIntListboxParams(request.getParameterValues("groupPermTypes"));
	

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// other page variables
	
	boolean doAction = ( 
		doAddUserPerm || doAddGroupPerm || doRemoveUserPerm || doRemoveGroupPerm
	);

      out.write("\r\n");
      out.write("\r\n");
	///////////////////////
	// error variables
	
	boolean errors = false;

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////////////////
	// global variables
	
	ProfileManager manager = forumFactory.getProfileManager();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// try to load the forum from the passed in forumID
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	}
	catch( ForumNotFoundException fnfe ) {
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("Forum " + forumID + " not found") );
		return;
	}
	catch( UnauthorizedException ue ) {
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("No permission to work with this forum"));
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// this forum's properties
	
	String forumName = forum.getName();
	String forumDescription = forum.getDescription();
	Iterator allUsers = manager.users();
	Iterator allGroups = manager.groups();
	int[] usersWithReadPerm = new int[0];
	int[] usersWithThreadPerm = new int[0];
	int[] usersWithMessagePerm = new int[0];
	int[] usersWithModeratorPerm = new int[0];
	int[] groupsWithReadPerm = new int[0];
	int[] groupsWithThreadPerm = new int[0];
	int[] groupsWithMessagePerm = new int[0];
	int[] groupsWithModeratorPerm = new int[0];
	try {
		usersWithReadPerm = forum.usersWithPermission(READ);
		usersWithThreadPerm = forum.usersWithPermission(CREATE_THREAD);
		usersWithMessagePerm = forum.usersWithPermission(CREATE_MESSAGE);
		usersWithModeratorPerm = forum.usersWithPermission(MODERATOR);
		groupsWithReadPerm = forum.groupsWithPermission(READ);
		groupsWithThreadPerm = forum.groupsWithPermission(CREATE_THREAD);
		groupsWithMessagePerm = forum.groupsWithPermission(CREATE_MESSAGE);
		groupsWithModeratorPerm = forum.groupsWithPermission(MODERATOR);
	}
	catch( UnauthorizedException ue ) {}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	/////////////////////////
	// do an action!
	
	if( doAction ) {
		
		// add a new user permission
		if( doAddUserPerm ) {
			try {
				for( int i=0; i<userList.length; i++ ) {
					User user = manager.getUser(userList[i]);
					for( int j=0; j<userPermTypesList.length; j++ ) {
					 if (userPermTypesList[j] != MODERATOR){
						forum.addUserPermission(user,userPermTypesList[j]);
					 }
					}
				}
			} catch( UserNotFoundException unfe ) {
			} catch( UnauthorizedException ue ) {
			}
			try {
				if( !"-Enter Username-".equals(userAddUsername) 
					&& userAddUsername != null )
				{
					User user = manager.getUser(userAddUsername);
					for( int j=0; j<userPermTypesList.length; j++ ) {
						forum.addUserPermission(user,userPermTypesList[j]);
					}
				}
			} catch( UserNotFoundException unfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
		
		// remove a user permission
		if( doRemoveUserPerm ) {
			try {
				for( int i=0; i<usersWithPerm.length; i++ ) {
					User user = manager.getUser(usersWithPerm[i]);
					forum.removeUserPermission(user,permType);
				}
			} catch( UserNotFoundException unfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
		
		// add a new group permission
		if( doAddGroupPerm ) {
			try {
				for( int i=0; i<groupList.length; i++ ) {
					Group group = manager.getGroup(groupList[i]);
					for( int j=0; j<groupPermTypesList.length; j++ ) {
						forum.addGroupPermission(group,groupPermTypesList[j]);
					}
				}
			} catch( GroupNotFoundException gnfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
		
		// remove a user permission
		if( doRemoveGroupPerm ) {
			try {
				for( int i=0; i<groupsWithPerm.length; i++ ) {
					Group group = manager.getGroup(groupsWithPerm[i]);
					forum.removeGroupPermission(group,permType);
				}
			} catch( GroupNotFoundException gnfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// if we did something, redirect to this page again (since we're doing POSTS
	// on the form)
	
	// uncommented so i can debug parameters!!
	if( doAction ) {
		response.sendRedirect("forumPerms.jsp?forum="+forumID);
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title></title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("\t<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("\t<!--\r\n");
      out.write("\t\tfunction selAllListBox( el, chkbx ) {\r\n");
      out.write("\t\t\tif( chkbx.checked ) {\r\n");
      out.write("\t\t\t\tfor( var i=0; i<el.options.length; i++ ) {\r\n");
      out.write("\t\t\t\t\tel.options[i].selected = true;\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t//-->\r\n");
      out.write("\t</script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/shadowBack.gif\" bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums: Forum Permissions" };

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
      out.write("<b>Permissions for forum:</b>\r\n");
      out.print( forumName );
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\" align=\"center\" border=\"0\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\" border=\"0\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\tPermission Summary\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<table cellpadding=\"3\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t");
      out.write("\r\n");
      out.write("\t\t\tUsers of this forum with a particular permission:\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" align=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveUserPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( READ );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( usersWithReadPerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tuser");
      out.print( (usersWithReadPerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">READ</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"usersWithPerm\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<usersWithReadPerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		User user = manager.getUser(usersWithReadPerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		int userID = user.getID(); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		if( userID == -1 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* Anonymous Users\r\n");
      out.write("\t\t\t\t\t");
		} else if( userID == 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* All Registered Users\r\n");
      out.write("\t\t\t\t\t");
		} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write('"');
      out.write('>');
      out.print( user.getUsername() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		} catch( UserNotFoundException unfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb03\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.usersWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb03\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveUserPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( CREATE_THREAD );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( usersWithThreadPerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tuser");
      out.print( (usersWithThreadPerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">CREATE THREAD</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"usersWithPerm\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<usersWithThreadPerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		User user = manager.getUser(usersWithThreadPerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		int userID = user.getID(); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		if( userID == -1 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* Anonymous Users\r\n");
      out.write("\t\t\t\t\t");
		} else if( userID == 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* All Registered Users\r\n");
      out.write("\t\t\t\t\t");
		} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write('"');
      out.write('>');
      out.print( user.getUsername() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		} catch( UserNotFoundException unfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb04\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.usersWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb04\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveUserPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( CREATE_MESSAGE );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( usersWithMessagePerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tuser");
      out.print( (usersWithMessagePerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">CREATE MESSAGE</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"usersWithPerm\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<usersWithMessagePerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		User user = manager.getUser(usersWithMessagePerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		int userID = user.getID(); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		if( userID == -1 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* Anonymous Users\r\n");
      out.write("\t\t\t\t\t");
		} else if( userID == 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* All Registered Users\r\n");
      out.write("\t\t\t\t\t");
		} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write('"');
      out.write('>');
      out.print( user.getUsername() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		} catch( UserNotFoundException unfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb05\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.usersWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb05\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveUserPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( MODERATOR );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( usersWithModeratorPerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tuser");
      out.print( (usersWithModeratorPerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">MODERATOR</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"usersWithPerm\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<usersWithModeratorPerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		User user = manager.getUser(usersWithModeratorPerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		int userID = user.getID(); 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		if( userID == -1 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* Anonymous Users\r\n");
      out.write("\t\t\t\t\t");
		} else if( userID == 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write("\">* All Registered Users\r\n");
      out.write("\t\t\t\t\t");
		} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( userID );
      out.write('"');
      out.write('>');
      out.print( user.getUsername() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		} catch( UserNotFoundException unfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb05\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.usersWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb05\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t");
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<hr size=\"0\" width=\"75%\">\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t");
      out.write("\r\n");
      out.write("\t\t\tGroups of this forum with a particular permission:\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" align=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveGroupPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( READ );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( groupsWithReadPerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tgroup");
      out.print( (groupsWithReadPerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">READ</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"groupsWithPerm\">\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<groupsWithReadPerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		Group group = manager.getGroup(groupsWithReadPerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( group.getID() );
      out.write('"');
      out.write('>');
      out.print( group.getName() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} catch( GroupNotFoundException gnfe ) {
      out.write(' ');
      out.print( groupsWithReadPerm[i] );
      out.write(' ');
} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb06\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.groupsWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb06\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveGroupPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( CREATE_THREAD );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( groupsWithThreadPerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tgroup");
      out.print( (groupsWithThreadPerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">CREATE THREAD</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"groupsWithPerm\">\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<groupsWithThreadPerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		Group group = manager.getGroup(groupsWithThreadPerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( group.getID() );
      out.write('"');
      out.write('>');
      out.print( group.getName() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} catch( GroupNotFoundException gnfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb07\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.groupsWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb07\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveGroupPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( CREATE_MESSAGE );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( groupsWithMessagePerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tgroup");
      out.print( (groupsWithMessagePerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">CREATE MESSAGE</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"groupsWithPerm\">\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<groupsWithMessagePerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		Group group = manager.getGroup(groupsWithMessagePerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( group.getID() );
      out.write('"');
      out.write('>');
      out.print( group.getName() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} catch( GroupNotFoundException gnfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb08\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.groupsWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb08\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"forumPerms.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doRemoveGroupPerm\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"permType\" value=\"");
      out.print( MODERATOR );
      out.write("\">\r\n");
      out.write("\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t<b>");
      out.print( groupsWithMessagePerm.length );
      out.write("</b>\r\n");
      out.write("\t\t\t\t\tgroup");
      out.print( (groupsWithMessagePerm.length==1)?"":"s" );
      out.write(" \r\n");
      out.write("\t\t\t\t\twith <font color=\"#008000\">MODERATOR</font> permission:\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<select size=\"5\" name=\"groupsWithPerm\">\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<groupsWithModeratorPerm.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		try { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		Group group = manager.getGroup(groupsWithModeratorPerm[i]); 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( group.getID() );
      out.write('"');
      out.write('>');
      out.print( group.getName() );
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
		} catch( GroupNotFoundException gnfe ) {} 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select><br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb08\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.groupsWithPerm,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb08\">Select All</label>) <br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" value=\"Remove\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t");
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form action=\"forumPerms.jsp\" method=\"get\"> \r\n");
      out.write("<input type=\"hidden\" name=\"doAddUserPerm\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\" align=\"center\" border=\"0\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\" border=\"0\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td width=\"99%\">\r\n");
      out.write("\tAdd new <b>user</b> permission\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t(<a href=\"createUser.jsp\">Create a new user</a>)\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\t<table cellpadding=\"3\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" align=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td nowrap>I want</td>\r\n");
      out.write("\t\t\t\t<td width=\"1%\">\r\n");
      out.write("\t\t\t\t\t<select name=\"userList\" size=\"2\" multiple>\r\n");
      out.write("\t\t\t\t\t<option value=\"-1\">* Anonymous Users\r\n");
      out.write("\t\t\t\t\t<option value=\"0\">* All Registered Users\r\n");
      out.write("\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb01\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.userList,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb01\">Select All</label>)\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<input type=\"text\" size=\"20\" name=\"userListField\"\r\n");
      out.write("\t\t\t\t\t value=\"-Enter Username-\"\r\n");
      out.write("\t\t\t\t\t onclick=\"this.select();\">\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t<td>to be able to</td>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<select name=\"userPermTypes\" size=\"");
      out.print( perms.length );
      out.write("\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<perms.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<option value=\"");
      out.print( perms[i] );
      out.write('"');
      out.write('>');
      out.print( permDescriptions[i] );
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb02\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.userPermTypes,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb02\">Select All</label>)\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t<td>this forum</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td colspan=\"5\" align=\"center\">\r\n");
      out.write("\t\t\t\t<input type=\"submit\" name=\"\" value=\"Add User Permission\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form action=\"forumPerms.jsp\" method=\"get\"> \r\n");
      out.write("<input type=\"hidden\" name=\"doAddGroupPerm\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\" align=\"center\" border=\"0\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\" border=\"0\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td width=\"99%\">\r\n");
      out.write("\tAdd new <b>group</b> permission\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t(<a href=\"createGroup.jsp\">Create a new group</a>)\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\t<table cellpadding=\"3\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" align=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td nowrap>I want</td>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<select name=\"groupList\" size=\"4\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	while( allGroups.hasNext() ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t");
		Group group = (Group)allGroups.next(); 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print( group.getID() );
      out.write('"');
      out.write('>');
      out.print( group.getName() );
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb11\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.groupList,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb11\">Select All</label>)\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t<td>to be able to</td>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<select name=\"groupPermTypes\" size=\"");
      out.print( perms.length );
      out.write("\" multiple>\r\n");
      out.write("\t\t\t\t\t");
	for( int i=0; i<perms.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<option value=\"");
      out.print( perms[i] );
      out.write('"');
      out.write('>');
      out.print( permDescriptions[i] );
      out.write("\r\n");
      out.write("\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t(<input type=\"checkbox\" id=\"cb12\"\r\n");
      out.write("\t\t\t\t\t  onclick=\"selAllListBox(this.form.groupPermTypes,this);\">\r\n");
      out.write("\t\t\t\t\t<label for=\"cb12\">Select All</label>)\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t<td>this forum</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td colspan=\"5\" align=\"center\">\r\n");
      out.write("\t\t\t\t<input type=\"submit\" name=\"\" value=\"Add Group Permission\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
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
