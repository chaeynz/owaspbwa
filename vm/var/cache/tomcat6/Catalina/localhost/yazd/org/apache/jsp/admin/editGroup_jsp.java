package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class editGroup_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/admin/include/pageTitle.jsp");
    _jspx_dependants.add("/admin/groupChooser.jsp");
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
 *	$RCSfile: editGroup.jsp,v $
 *	$Revision: 1.3.2.1 $
 *	$Date: 2001/05/22 17:59:54 $
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
 try { 
      out.write("\r\n");
      out.write(" \r\n");
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
      out.write(" \r\n");
	////////////////////
	// get parameters
	
	int groupID = ParamUtils.getIntParameter(request,"group",-1);
	String newGroupName = ParamUtils.getParameter(request,"groupName");
	String newGroupDescription = ParamUtils.getParameter(request,"groupDescription",true);
	boolean doEdit = ParamUtils.getBooleanParameter(request,"doEdit");

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	//
	
	boolean noGroupSpecified = (groupID<0);

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean errorGroupName = (newGroupName == null);
	boolean errorGroupAlreadyExists = false;
	boolean errorNoPermissionToEdit = false;
	boolean errorGroupNotFound = false;
	
	boolean errors = (noGroupSpecified);

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	////////////////////////////////////////////////////////////////
	// if there are no errors at this point, start the process of
	// adding the user
	
	ProfileManager manager = forumFactory.getProfileManager();
	Group thisGroup = null;
	if( !errors ) {
		try {
			thisGroup = manager.getGroup(groupID);
		} catch( GroupNotFoundException gnfe ) {
			errorGroupNotFound = true;
			System.err.println( "groupNotFoundException" );
		}
	}
	errors = (errors || (thisGroup==null));
	if( !errors && doEdit ) {
		// get a profile manager to edit user properties
		try {
			thisGroup.setName( newGroupName );
			if( newGroupDescription != null ) {
				thisGroup.setDescription( newGroupDescription );
			}
		}
		catch( UnauthorizedException ue ) {
			errorNoPermissionToEdit = true;
		}
	}
	String name = null;
	String description = null;
	if( !errors ) {
		name = thisGroup.getName();
		description = thisGroup.getDescription();
	}

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// overall  error check
	errors = (errorGroupName || errorGroupAlreadyExists
		|| errorNoPermissionToEdit);

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// set error messages
	if( errors ) {
		if( errorGroupName ) {
			errorMessage = "Please specify a group name.";
		}
		else if( errorGroupAlreadyExists ) {
			errorMessage = "This group already exists, please choose "
				+ "a different name";
		}
		else if( errorNoPermissionToEdit ) {
			errorMessage = "Sorry, you don't have permission to edit "
				+ "a group";
		}
		else if( errorGroupNotFound ) {
			errorMessage = "Group not found";
		}
		else {
			errorMessage = "A general error occured.";
		}
	}

      out.write("\r\n");
      out.write("\r\n");
	//////////////////////////////////////////////////////////////////////
	// if a group was edited was successfully created, say so and return (to stop the 
	// jsp from executing
	if( !errors ) {
		request.setAttribute("message","Group was edited successfully!");
		response.sendRedirect("groups.jsp");
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
	String[] pageTitleInfo = { "Groups : Edit Group" };

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
	////////////////////
	// display a list of groups to edit if no group was specified:
	if( noGroupSpecified ) {
		String formAction = "edit";

      out.write("\r\n");
      out.write("\t\t");
      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: groupChooser.jsp,v $
 *	$Revision: 1.2 $
 *	$Date: 2000/12/18 02:06:21 $
 */

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// get an iterator of groups and display a list
	
	Iterator groupIterator = manager.groups();
	if( !groupIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\tNo groups to edit!\r\n");

	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<form action=\"");
      out.print( formAction );
      out.write("Group.jsp\">\r\n");
      out.write("<select name=\"group\">\r\n");
	while( groupIterator.hasNext() ) {
		Group group = (Group)groupIterator.next();

      out.write("\r\n");
      out.write("\t\t<option value=\"");
      out.print( group.getID() );
      out.write('"');
      out.write('>');
      out.print( group.getName() );
      out.write('\r');
      out.write('\n');
	}

      out.write("\r\n");
      out.write("</select>\r\n");
	if( formAction.equals("edit") ) { 
      out.write("\r\n");
      out.write("\t<input type=\"submit\" value=\"Edit This Group...\">\r\n");
	} else if( formAction.equals("remove") ) { 
      out.write("\r\n");
      out.write("\t<input type=\"submit\" value=\"Remove This Group...\">\r\n");
	} 
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
		out.flush();
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form action=\"editGroup.jsp\" method=\"post\">\r\n");
      out.write("<input type=\"hidden\" name=\"group\" value=\"");
      out.print( groupID );
      out.write("\">\r\n");
      out.write("<input type=\"hidden\" name=\"doEdit\" value=\"true\">\r\n");
      out.write("\r\n");
      out.write("<b>Change Group Information:</b>\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<table bgcolor=\"#999999\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"95%\" align=\"right\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#999999\" cellspacing=\"1\" cellpadding=\"3\" border=\"0\" width=\"100%\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\">Group Name:</i></font></td>\r\n");
      out.write("\t<td><input type=\"text\" name=\"groupName\" size=\"30\" maxlength=\"100\"\r\n");
      out.write("\t\t value=\"");
      out.print( (name!=null)?name:"" );
      out.write("\">\r\n");
      out.write("\t</td>\t\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td><font size=\"-1\">Group Description:<br>(optional)</i></font></td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\t<textarea name=\"groupDescription\" wrap=\"virtual\" cols=\"40\" rows=\"5\"\r\n");
      out.write(" \t\t>");
      out.print( (description!=null)?description:"" );
      out.write("</textarea>\r\n");
      out.write("\t</td>\t\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("<br clear=\"all\"><br>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("<center>\r\n");
      out.write("\t<input type=\"submit\" value=\"Update Group Info\">\r\n");
      out.write("\t&nbsp;\r\n");
      out.write("\t<input type=\"submit\" value=\"Cancel\" onclick=\"location.href='groups.jsp';return false;\">\r\n");
      out.write("</center>\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
	} catch( Exception e ) {
		System.err.println(e);
		e.printStackTrace();
	}

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
