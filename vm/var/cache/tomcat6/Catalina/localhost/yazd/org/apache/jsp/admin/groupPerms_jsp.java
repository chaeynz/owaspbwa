package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.net.URLEncoder;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class groupPerms_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	////////////////////////
	// global page variables
	
	private final String ADD = " Add ";
	private final String REMOVE = "Remove";

	///////////////////
	// global methods
	
	private String getParameterMode( String paramVal ) {
		if( paramVal == null ) {
			return "";
		}
		if( paramVal.equals(ADD) ) { return "add"; }
		else if( paramVal.equals(REMOVE) ) { return "remove"; }
		else {
			return "";
		}
	}
	
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
 *	$RCSfile: groupPerms.jsp,v $
 *	$Revision: 1.3.2.4 $
 *	$Date: 2001/05/22 17:55:13 $
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
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isGroupAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer groups");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// get parameters
	
	int groupID = ParamUtils.getIntParameter(request,"group",-1);
	boolean doAction = ParamUtils.getBooleanParameter(request,"doAction");
	
	String groupAdminMode = getParameterMode(ParamUtils.getParameter(request,"groupAdminMode"));
	String groupUserMode  = getParameterMode(ParamUtils.getParameter(request,"groupUserMode"));
	
	int[] groupAdminsParam = getIntListboxParams(request.getParameterValues("groupAdmins"));
	//int[] allAdminsParam   = getIntListboxParams(request.getParameterValues("allAdmins"));
	int[] groupUsersParam  = getIntListboxParams(request.getParameterValues("groupUsers"));
	//int[] allUsersParam    = getIntListboxParams(request.getParameterValues("allUsers"));
	String allAdminsUsername = ParamUtils.getParameter(request,"allAdminsUsername");
	String allUsersUsername = ParamUtils.getParameter(request,"allUsersUsername");

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// other page variables
	
	boolean addGroupAdmin = (groupAdminMode.equals("add"));
	boolean removeGroupAdmin = (groupAdminMode.equals("remove"));
	
	boolean addGroupUser = (groupUserMode.equals("add"));
	boolean removeGroupUser = (groupUserMode.equals("remove"));

      out.write("\r\n");
      out.write("\r\n");
	///////////////////////
	// error variables
	
	boolean errorGroupNotFound = false;
	boolean errorNoPermission = false;
	boolean errors = false;

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////////////////
	// global variables
	
	ProfileManager manager = forumFactory.getProfileManager();

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// try to load the group from the passed in group id
	Group group = null;
	try {
		group = manager.getGroup(groupID);
	}
	catch( GroupNotFoundException gnfe ) {
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("Group " + groupID + " not found") );
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// this group properties
	
	String groupName = group.getName();
	String groupDescription = group.getDescription();
	int userCount = group.getMemberCount();
	Iterator adminIterator = group.administrators();
	Iterator allAdmins = manager.users();
	Iterator userIterator = group.members();
	Iterator allUsers = manager.users();

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	/////////////////////////
	// do an action!
	
	if( doAction ) {
		
		// add a group administrator
		if( addGroupAdmin ) {
			try {
				User admin = manager.getUser(allAdminsUsername);
				group.addAdministrator(admin);
			}
			catch( UserNotFoundException unfe ) {}
			catch( UnauthorizedException ue   ) {}
		}
		
		// remove a group administrator
		else if( removeGroupAdmin ) {
			for( int i=0; i<groupAdminsParam.length; i++ ) {
				try {
					User admin = manager.getUser(groupAdminsParam[i]);
					group.removeAdministrator(admin);
				}
				catch( UserNotFoundException unfe ) {}
				catch( UnauthorizedException ue   ) {}
			}
		}
		
		// add a group user
		else if( addGroupUser ) {
			try {
				User user = manager.getUser(allUsersUsername);
				group.addMember(user);
			}
			catch( UserNotFoundException unfe ) {}
			catch( UnauthorizedException ue   ) {}
		}
		
		// remove a group user
		else if( removeGroupUser ) {
			for( int i=0; i<groupUsersParam.length; i++ ) {
				try {
					System.out.println( "remove: " + groupUsersParam[i] );
					User user = manager.getUser(groupUsersParam[i]);
					group.removeMember(user);
				}
				catch( UserNotFoundException unfe ) {}
				catch( UnauthorizedException ue   ) {}
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
		response.sendRedirect("groupPerms.jsp?group="+groupID);
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
	String[] pageTitleInfo = { "Groups : Group Permissions" };

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
      out.write("<b>Permissions for group:</b>\r\n");
      out.print( groupName );
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form action=\"groupPerms.jsp\" method=\"post\"> \r\n");
      out.write("<input type=\"hidden\" name=\"doAction\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"group\" value=\"");
      out.print( groupID );
      out.write("\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\" align=\"center\" border=\"0\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\" align=\"center\" border=\"0\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td width=\"");
      out.print( isSystemAdmin?"99":"100" );
      out.write("%\">\r\n");
      out.write("\tMembers of this group\r\n");
      out.write("\t</td>\r\n");
      out.write("\t");
	if( isSystemAdmin ) { 
      out.write("\r\n");
      out.write("\t\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<a href=\"createUser.jsp?type=user\">Create New User</a>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=\"50%\" align=\"center\">\r\n");
      out.write("\t\t\tMembers of this group:\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<select size=\"5\" name=\"groupUsers\" multiple>\r\n");
      out.write("\t\t\t");
	HashMap groupUserMap = new HashMap(); 
      out.write("\r\n");
      out.write("\t\t\t");
	while( userIterator.hasNext() ) { 
      out.write("\r\n");
      out.write("\t\t\t");
		User user = (User)userIterator.next(); 
      out.write("\r\n");
      out.write("\t\t\t");
		int userID = user.getID(); 
      out.write("\r\n");
      out.write("\t\t\t");
		groupUserMap.put( ""+userID, ""+userID ); 
      out.write("\r\n");
      out.write("\t\t\t\t<option value=\"");
      out.print(user.getID());
      out.write('"');
      out.write('>');
      out.print( user.getUsername() );
      out.write("\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</select>\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t(<input type=\"checkbox\" name=\"\" value=\"\" id=\"cbusr01\"\r\n");
      out.write("\t\t\t  onclick=\"selAllListBox(this.form.groupUsers,this);\">\r\n");
      out.write("\t\t\t<label for=\"cbusr01\">Select All</label>)\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<input type=\"submit\" name=\"groupUserMode\" value=\"");
      out.print( REMOVE );
      out.write("\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td width=\"50%\" align=\"center\">\r\n");
      out.write("\t\t\tEnter a username to add a user to this group:\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<input type=\"text\" name=\"allUsersUsername\" value=\"\">\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<input type=\"submit\" name=\"groupUserMode\" value=\"");
      out.print( ADD );
      out.write("\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t\r\n");
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
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\" align=\"center\" border=\"0\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\" align=\"center\" border=\"0\">\r\n");
      out.write("<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td width=\"");
      out.print( isSystemAdmin?"99":"100" );
      out.write("%\">\r\n");
      out.write("\tAdminstrators for this group\r\n");
      out.write("\t</td>\r\n");
      out.write("\t");
	if( isSystemAdmin ) { 
      out.write("\r\n");
      out.write("\t\t<td width=\"1%\" nowrap>\r\n");
      out.write("\t\t<a href=\"createUser.jsp?type=admin\">Create New Admin</a>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=\"50%\" align=\"center\">\r\n");
      out.write("\t\t\tEnter a username to make a user an admin of this group:\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<select size=\"5\" name=\"groupAdmins\" multiple>\r\n");
      out.write("\t\t\t");
	HashMap groupAdminMap = new HashMap(); 
      out.write("\r\n");
      out.write("\t\t\t");
	while( adminIterator.hasNext() ) { 
      out.write("\r\n");
      out.write("\t\t\t");
		User admin = (User)adminIterator.next(); 
      out.write("\r\n");
      out.write("\t\t\t");
		int adminID = admin.getID(); 
      out.write("\r\n");
      out.write("\t\t\t");
		groupAdminMap.put( ""+adminID, ""+adminID ); 
      out.write("\r\n");
      out.write("\t\t\t\t<option value=\"");
      out.print(admin.getID());
      out.write('"');
      out.write('>');
      out.print( admin.getUsername() );
      out.write("\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</select>\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t(<input type=\"checkbox\" name=\"\" value=\"\" id=\"cbadm01\"\r\n");
      out.write("\t\t\t  onclick=\"selAllListBox(this.form.groupAdmins,this);\">\r\n");
      out.write("\t\t\t<label for=\"cbadm01\">Select All</label>)\r\n");
      out.write("\t\t\t<p>\r\n");
      out.write("\t\t\t<input type=\"submit\" name=\"groupAdminMode\" value=\"");
      out.print( REMOVE );
      out.write("\">\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td width=\"50%\" align=\"center\">\r\n");
      out.write("\t\t\tAll admins not in this group:\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<input type=\"text\" name=\"allAdminsUsername\" value=\"\">\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<input type=\"submit\" name=\"groupAdminMode\" value=\"");
      out.print( ADD );
      out.write("\">\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t<font size=\"-2\">\r\n");
      out.write("\t\t\t<i>\r\n");
      out.write("\t\t\tYou can't promote a user of a group to be an admin of a group.\r\n");
      out.write("\t\t\tFirst remove the user from the group then re-add them as an\r\n");
      out.write("\t\t\tadministrator.\r\n");
      out.write("\t\t\t</i>\r\n");
      out.write("\t\t\t</font>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<center>\r\n");
      out.write("\t<form action=\"groups.jsp\">\r\n");
      out.write("\t<input type=\"submit\" value=\"Done\">\r\n");
      out.write("\t</form>\r\n");
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
