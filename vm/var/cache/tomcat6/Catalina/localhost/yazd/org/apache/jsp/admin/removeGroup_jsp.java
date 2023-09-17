package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class removeGroup_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 *	$RCSfile: removeGroup.jsp,v $
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
	int groupID   = ParamUtils.getIntParameter(request,"group",-1);
	boolean doDelete = ParamUtils.getBooleanParameter(request,"doDelete");

      out.write("\r\n");
      out.write(" \r\n");
	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noGroupSpecified = (groupID < 0);
	boolean errors = (noGroupSpecified);

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();

      out.write('\r');
      out.write('\n');
	/////////////////////
	// delete group if specified
	if( doDelete && !errors ) {
		String message = "";
		try {
			Group group = manager.getGroup(groupID);
			manager.deleteGroup(group);
			message = "Group deleted successfully!";
		}
		catch( GroupNotFoundException fnfe ) {
			System.err.println( fnfe );
			message = "No group found";
		}
		catch( UnauthorizedException ue ) {
			System.err.println( ue );
			message = "Not authorized to delete this group";
		}
		request.setAttribute("message",message);
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
	String[] pageTitleInfo = { "Groups : Remove Group" };

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
		String formAction = "remove";

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
	/////////////////////
	// at this point, we know there is a group to work with:
    Group group = null;
	try {
		group = manager.getGroup(groupID);
	} catch( GroupNotFoundException gnfe ) {
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("Remove <b>");
      out.print( group.getName() );
      out.write("</b>? \r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<ul>\r\n");
      out.write("\tWarning: This will permanently delete the group. Are\r\n");
      out.write("\tyou sure you really want to do this? (It will <b>not</b> delete the\r\n");
      out.write("\tusers associated with this group, but will remove group-permissions for these users\r\n");
      out.write("    granted by membership of this group.).\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<form action=\"removeGroup.jsp\" name=\"deleteForm\">\r\n");
      out.write("<input type=\"hidden\" name=\"doDelete\" value=\"true\">\r\n");
      out.write("<input type=\"hidden\" name=\"group\" value=\"");
      out.print( groupID );
      out.write("\">\r\n");
      out.write("\t<input type=\"submit\" value=\" Delete group \">\r\n");
      out.write("\t&nbsp;\r\n");
      out.write("\t<input type=\"submit\" name=\"cancel\" value=\" Cancel \" style=\"font-weight:bold;\"\r\n");
      out.write("\t onclick=\"location.href='groups.jsp';return false;\">\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("<!--\r\n");
      out.write("document.deleteForm.cancel.focus();\r\n");
      out.write("//-->\r\n");
      out.write("</script>\r\n");
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
