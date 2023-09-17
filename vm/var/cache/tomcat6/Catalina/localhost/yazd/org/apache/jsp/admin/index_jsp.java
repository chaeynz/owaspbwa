package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.tree.*;
import com.Yasna.forum.util.admin.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

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
 *	$RCSfile: index.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/18 02:06:21 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\r\n");
      com.Yasna.forum.util.admin.AdminBean adminBean = null;
      synchronized (session) {
        adminBean = (com.Yasna.forum.util.admin.AdminBean) _jspx_page_context.getAttribute("adminBean", PageContext.SESSION_SCOPE);
        if (adminBean == null){
          adminBean = new com.Yasna.forum.util.admin.AdminBean();
          _jspx_page_context.setAttribute("adminBean", adminBean, PageContext.SESSION_SCOPE);
        }
      }
      out.write("\r\n");
      out.write("\t\t\r\n");
	//////////////////////////////
	// Yazd installation check
	
	// Check for the existence of the property "setup" in the 
	// yazd.properties file. This is managed by the PropertyManager class.
	// This property tells us if the admin tool is being run for the first time.
	
	boolean setupError = false;
	String installed = null;
	try {
		installed = PropertyManager.getProperty("setup");
		if( installed == null || !installed.equals("true") ) {
			// the "installed" property doesn't exist or isn't set
			response.sendRedirect("setup/setup.jsp");
			return;
		}
		//else if( !installed.equals("true") ) {
		//	setupError = true;
		//}
	}
	catch( Exception e ) {
		// signal an error. the file yazd.properties might not exist.
		setupError = true;
	}
	
	// print out a setup error:
	if( setupError ) { 
      out.write("\r\n");
      out.write("\t\t<html>\r\n");
      out.write("\t\t<head>\r\n");
      out.write("\t\t<title>Yazd Administration - Beta</title>\r\n");
      out.write("\t\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("\t\t</head>\r\n");
      out.write("\t\t<body>\r\n");
      out.write("\t\tSetup Error! Make sure your <b>yazd.properties</b> file is in\r\n");
      out.write("\t\tyour app server's classpath.\r\n");
      out.write("\t\t</body>\r\n");
      out.write("\t\t</html>\r\n");
		// for some reason, we have to call flush.. some app servers won't
		// display the above html w/o flushing the stream
		out.flush();
		return;
	}

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
      out.write("\r\n");
	////////////////////////
	// get parameters
	boolean logout = ParamUtils.getBooleanParameter(request,"logout");

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////////
	// logout if requested:
	if( logout ) {
		try {
			session.invalidate();
			adminBean.resetAuthToken();
		}
		catch( IllegalStateException ignored ) { // if session is already invalid
		}
		finally {
			response.sendRedirect( "index.jsp" );
			return;
		}
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	///////////////////////////////////////
	// Get the permissions for this user:
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isForumAdmin  = ((Boolean)session.getValue("yazdAdmin.forumAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	boolean isModerator  = ((Boolean)session.getValue("yazdAdmin.Moderator")).booleanValue();

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////////////////////////////////////////////////
	// set the menu trees in the bean based on the user's permissions
	
	// system tree
	if( isSystemAdmin || isGroupAdmin ) {
		com.Yasna.forum.util.tree.Tree systemTree 
			= new com.Yasna.forum.util.tree.Tree("system");
		int nodeID = 0;
		TreeNode node = null;
		
		if( isSystemAdmin ) {
			node = new TreeNode( nodeID++, "System Settings" );
			node.addChild( new TreeLeaf("Cache", "cache.jsp") );
			node.addChild( new TreeLeaf("Database Info", "dbInfo.jsp") );
			node.addChild( new TreeLeaf("Search Settings", "searchSettings.jsp") );
			node.addChild( new TreeLeaf("Property Manager", "propManager.jsp") );
			node.setVisible(true);
			systemTree.addChild(node);
		}
		
		if( isSystemAdmin ) {
			node = new TreeNode( nodeID++, "Users" );
			node.addChild( new TreeLeaf("User Summary", "users.jsp") );
			//node.addChild( new TreeLeaf("System Admins", "systemAdmins.jsp") );
			node.addChild( new TreeLeaf("Passwords", "password.jsp") );
			node.addChild( new TreeLeaf("Create User", "createUser.jsp") );
			//node.addChild( new TreeLeaf("Edit User",   "editUser.jsp") );
			node.addChild( new TreeLeaf("Remove User", "removeUser.jsp") );
			node.setVisible(true);
			systemTree.addChild(node);
		}
		
		if( isSystemAdmin || isGroupAdmin ) {
			node = new TreeNode( nodeID++, "Groups" );
			node.addChild( new TreeLeaf("Group Summary", "groups.jsp") );
			if( isSystemAdmin ) {
				node.addChild( new TreeLeaf("Create Group", "createGroup.jsp") );
			}
			node.addChild( new TreeLeaf("Edit Group",   "editGroup.jsp") );
			node.addChild( new TreeLeaf("Remove Group", "removeGroup.jsp") );
			node.setVisible(true);
			systemTree.addChild(node);
		}
		
		if( systemTree.size() > 0 ) {
			adminBean.addTree( "systemTree", systemTree );
		}
	}
	
	// forum tree
	if( isSystemAdmin || isModerator ) {
		com.Yasna.forum.util.tree.Tree forumTree 
			= new com.Yasna.forum.util.tree.Tree("forum");
		int nodeID = 0;
		TreeNode node = null;
		
		node = new TreeNode( nodeID++, "Forums" );
		if (isSystemAdmin){
		node.addChild( new TreeLeaf("Summary",     "forums.jsp") );
		node.addChild( new TreeLeaf("Create",      "createForum.jsp") );
		node.addChild( new TreeLeaf("Edit Properties", "editForum.jsp") );
		node.addChild( new TreeLeaf("Remove",      "removeForum.jsp") );
		node.addChild( new TreeLeaf("Filters",     "forumFilters.jsp") );
		}
		if (isSystemAdmin || isModerator){
		node.addChild( new TreeLeaf("Content",     "forumContent.jsp") );
		}
		node.setVisible(true);
		forumTree.addChild(node);
		
		adminBean.addTree( "forumTree", forumTree );
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>Yazd Administration</title>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<frameset rows=\"85,*\" bordercolor=\"#0099cc\" border=\"0\" frameborder=\"0\" framespacing=\"0\" style=\"background-color:#0099cc\">\r\n");
      out.write("\t<frame src=\"header.jsp\" name=\"header\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" noresize>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<frameset cols=\"175,*\" bordercolor=\"#0099cc\" border=\"0\" frameborder=\"0\" style=\"background-color:#0099cc\">\r\n");
      out.write("\t\t<frame src=\"sidebar.jsp\" name=\"sidebar\" scrolling=\"auto\" marginheight=\"0\" marginwidth=\"0\" noresize>\t   \r\n");
      out.write("\t\t<frameset rows=\"15,*\" bordercolor=\"#0099cc\" border=\"0\" frameborder=\"0\" style=\"background-color:#0099cc\">\r\n");
      out.write("\t\t\t<frame src=\"shadow.html\" name=\"shadow\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" noresize>\t   \r\n");
      out.write("\t\t\t<frame src=\"main.jsp\" name=\"main\" scrolling=\"auto\" noresize>\r\n");
      out.write("\t\t</frameset>\r\n");
      out.write("\t\t\r\n");
      out.write("\t</frameset>\r\n");
      out.write("\t\r\n");
      out.write("</frameset>\r\n");
      out.write("\r\n");
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
