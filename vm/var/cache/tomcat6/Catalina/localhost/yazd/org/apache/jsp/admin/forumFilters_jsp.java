package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class forumFilters_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	// Global variables/methods
	private final int ADD_FILTER = 1;
	private final int REMOVE_FILTER = 2;
	private final int EDIT_FILTER = 3;
	private final int SAVE_MODIFIED_FILTER = 4;
	private final int SAVE_PROPERTIES = 5;
	private final String[] classNames = { "com.Yasna.forum.filter.FilterHtml",
			"com.Yasna.forum.filter.FilterCodeHighlight",
			"com.Yasna.forum.filter.FilterFontStyle",
			"com.Yasna.forum.filter.FilterNewline",
			"com.Yasna.forum.filter.FilterSmileyFace",
			"com.Yasna.forum.filter.FilterURLConverter",
			"com.Yasna.forum.filter.FilterProfanity",
			"com.Yasna.forum.filter.FilterHackerSpeak"};
   	private boolean containsString( ForumMessageFilter[] installedFilters, String filterName ) {
		for( int i=0; i<installedFilters.length; i++ ) {
			if( installedFilters[i].getName().equals(filterName) ) {
				return true;
			}
		}
		return false;
	}
	private String escapeHTML( String line ) {
		StringBuffer buf = new StringBuffer();
		for( int i=0; i<line.length(); i++ ) {
			char ch = line.charAt(i);
			if( ch == '"' ) {
				buf.append( "&quot;" ); //"
			} else if( ch == '&' ) {
				buf.append( "&amp;" );
			} else {
				buf.append( ch );
			}
		}
		return buf.toString();
	}
	private String unescapeHTML( String line ) {
		//line = replace( line, "&gt;", ">" );
		//line = replace( line, "&lt;", "<" );
		line = replace( line, "&quot;", "\"" ); //"
		line = replace( line, "&amp;", "&" );
		return line;
	}
	private String replace( String line, String oldString, String newString ) {
		int i=0;
		if ( ( i=line.indexOf( oldString, i ) ) >= 0 ) {
			int oLength = oldString.length();
			int nLength = newString.length();
			StringBuffer buf = new StringBuffer();
			buf.append(line.substring(0,i)).append(newString);
			i += oLength;
			int j = i;
			while( ( i=line.indexOf( oldString, i ) ) > 0 ) {
				buf.append(line.substring(j,i)).append(newString);
				i += oLength;
				j = i;
			}
			buf.append(line.substring(j));
			return buf.toString();
		}
		return line;
	}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/admin/include/pageTitle.jsp");
    _jspx_dependants.add("/admin/forumChooser.jsp");
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
 *	$RCSfile: forumFilters.jsp,v $
 *	$Revision: 1.4.2.1 $
 *	$Date: 2001/05/10 02:06:19 $
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
	////////////////////
	// Security check
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write(" \r\n");
	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
	int mode      = ParamUtils.getIntParameter(request,"mode",-1);
	int filterID  = ParamUtils.getIntParameter(request,"filter",-1);
	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	
	boolean filterIDOK = (filterID != -1 );
	boolean doMode = (mode != -1);
    ////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();

    // Create the factory and forum objects
	Forum forum = null;
	if (!noForumSpecified)
	{
	    try {
		    forum = forumFactory.getForum(forumID);
	    }
	    catch( UnauthorizedException ue ) { System.err.println( "... in admin/admin-filter.jsp: " + ue ); }
	    catch( ForumNotFoundException fnfe ) { System.err.println( "... in admin/admin-filter.jsp: " + fnfe ); }
	    catch( Exception e ) {
		    System.err.println( "... in admin/admin-filter.jsp: " + e );
		    e.printStackTrace( System.err );
	    }
	}
	boolean forumOK = (forum != null);
	// Get an array of all filters in the system
	ForumMessageFilter[] allFilters = null;
	if (!noForumSpecified)
	{
	    try {
		    allFilters = new ForumMessageFilter[classNames.length];
		    for( int i=0; i<classNames.length; i++ ) {
			    Class c = Class.forName(classNames[i]); // load the name of the filter
			    allFilters[i] = (ForumMessageFilter)(c.newInstance());
		    }
	    }
	    catch( ClassNotFoundException cnfe ) { System.err.println( "... in admin/admin-filter.jsp: " + cnfe ); }
	    catch( IllegalAccessException iae ) { System.err.println( "... in admin/admin-filter.jsp: " + iae ); }
	    catch( InstantiationException ie ) { System.err.println( "... in admin/admin-filter.jsp: " + ie ); }
	}
	    // Get an array of installed filters
	    ForumMessageFilter[] installedFilters = null;
	    String[] installedFilterNames = null;
	    if( forumOK ) {
		    try {
			    installedFilters = forum.getForumMessageFilters();
		    }
		    catch( UnauthorizedException ue ) { 
			    System.err.println( "... in admin/admin-filter.jsp: " + ue );
		    }
		    if( installedFilters != null ) {
			    // build up list of class names of the forums (so we can not display the ones that are
			    // installed in the "add new webfilter box"
			    installedFilterNames = new String[installedFilters.length];
			    for( int i=0; i<installedFilterNames.length; i++ ) {
				    installedFilterNames[i] = installedFilters[i].getClass().getName();
			    }
		    }
	    }
	    // Based on the mode, do an action:
	    if( doMode ) {
		    // Add a filter
		    if( mode == ADD_FILTER ) {		
			    if( filterIDOK ) {	
		            try {
		                Class c = Class.forName(classNames[filterID]); // load the name of the filter
					    ForumMessageFilter newFilter = (ForumMessageFilter)(c.newInstance());
					    forum.addForumMessageFilter( newFilter );
		            }
		            catch( ClassNotFoundException cnfe ) { System.err.println( "... in admin/admin-filter.jsp: " + cnfe ); }
				    catch( IllegalAccessException iae ) { System.err.println( "... in admin/admin-filter.jsp: " + iae ); }
				    catch( InstantiationException ie ) { System.err.println( "... in admin/admin-filter.jsp: " + ie ); }
				    catch( UnauthorizedException ue ) { System.err.println( "... in admin/admin-filter.jsp: " + ue ); }
			    }
			    // redirect to same page 
			    response.sendRedirect( "forumFilters.jsp?forum=" + forumID );
				return;
		    }
		    else if( mode == REMOVE_FILTER ) {
			    if( filterIDOK ) {
				    try {
					    forum.removeForumMessageFilter(filterID);
				    } catch( UnauthorizedException ue ) { System.err.println( "... in admin/admin-filter.jsp: " + ue ); }
			    }
			    // redirect to same page
			    response.sendRedirect( "forumFilters.jsp?forum=" + forumID );
				return;
		    }
		    else if( mode == SAVE_PROPERTIES ) {
			    if( filterIDOK ) {
				    Enumeration props = installedFilters[filterID].filterPropertyNames();
				    while( props.hasMoreElements() ) {
					    String propName = (String)props.nextElement();
					    String propValue = request.getParameter(propName);
					    if( propValue != null ) {
						    try {
							    installedFilters[filterID].setFilterProperty(propName,propValue);
						    } catch( IllegalArgumentException iae ) { System.err.println( "... in admin/admin-filter.jsp: " + iae ); }
					    }
				    }
			    }
		    }
		    else {
			    // do nothing
		    }
	    }

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title></title>\r\n");

    if (!noForumSpecified)
    {

      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\tvar descriptions = new Array(\r\n");
      out.write("\t\t");
	for( int i=0; i<allFilters.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\"");
      out.print( unescapeHTML(allFilters[i].getDescription()) );
      out.write("\"\r\n");
      out.write("\t\t\t");
	if( (allFilters.length-i) > 1 ) { 
      out.write("\r\n");
      out.write("\t\t\t,\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t");
	} 
      out.write(" );\r\n");
      out.write("\tfunction doDesc( theForm ) {\r\n");
      out.write("\t\tvar selected = theForm.filter.options[theForm.filter.selectedIndex].value;\r\n");
      out.write("\t\ttheForm.filterDesc.value = descriptions[selected];\r\n");
      out.write("\t}\r\n");
      out.write("\t</script>\r\n");

    }

      out.write("\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/shadowBack.gif\" bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums : Forum Filters" };

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
	if( noForumSpecified ) {
		String formAction = "filters";

      out.write("\r\n");
      out.write("\t\t");
      out.write('\r');
      out.write('\n');

/**
 *	$RCSfile: forumChooser.jsp,v $
 *	$Revision: 1.2 $
 *	$Date: 2000/12/18 02:06:21 $
 */

      out.write("\r\n");
      out.write("\r\n");
	/////////////////////
	// get an iterator of forums and display a list
	
	Iterator forumIterator = forumFactory.forums();
	if( !forumIterator.hasNext() ) {

      out.write("\r\n");
      out.write("\t\tNo forums!\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\tTry <a href=\"createForum.jsp\">creating one</a>.\r\n");

	} else {

      out.write("\r\n");
      out.write("\r\n");
      out.write("\t");
	if( formAction.equals("edit") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"editForum.jsp\">\r\n");
      out.write("\t");
	} else if( formAction.equals("remove") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"removeForum.jsp\">\r\n");
      out.write("\t");
	} else if( formAction.equals("filters") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"forumFilters.jsp\">\r\n");
      out.write("\t");
	} else if( formAction.equals("content") ) { 
      out.write("\r\n");
      out.write("\t\t<form action=\"forumContent.jsp\">\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<select name=\"forum\">\r\n");
      out.write("\t");
	while( forumIterator.hasNext() ) {
			Forum tmpForum = (Forum)forumIterator.next();
	
      out.write("\r\n");
      out.write("\t\t\t<option value=\"");
      out.print( tmpForum.getID() );
      out.write('"');
      out.write('>');
      out.print( tmpForum.getName() );
      out.write('\r');
      out.write('\n');
      out.write('	');
	}
	
      out.write("\r\n");
      out.write("\t</select>\r\n");
      out.write("\t\r\n");
      out.write("\t");
	if( formAction.equals("edit") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Edit Properties...\">\r\n");
      out.write("\t");
	} else if( formAction.equals("remove") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Remove This Forum...\">\r\n");
      out.write("\t");
	} else if( formAction.equals("filters") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Work with filters...\">\r\n");
      out.write("\t");
	} else if( formAction.equals("content") ) { 
      out.write("\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Forum Content...\">\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t</form>\r\n");
      out.write("\t\r\n");
	} 
      out.write('\r');
      out.write('\n');
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
	//Forum forum                  = forumFactory.getForum(forumID);
	String forumName             = forum.getName();

      out.write("\r\n");
      out.write("\r\n");
      out.write("Filters for: <b>");
      out.print( forumName );
      out.write("</b>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<table cellpadding=5 cellspacing=0 border=1 width=\"100%\">\r\n");
      out.write("\t<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t\t<td align=\"center\">Current WebFilters installed for forum <b>");
      out.print( forum.getName() );
      out.write("</b></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t");
	if( installedFilters==null || installedFilters.length == 0 ) { 
      out.write("\r\n");
      out.write("\t\t\t<i>No filters installed. (You can install one below)</i>\r\n");
      out.write("\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t\t<table cellpadding=3 cellspacing=0 border=1 width=\"100%\">\r\n");
      out.write("\t\t\t<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t\t\t\t<td width=\"1%\" nowrap>Name</td>\r\n");
      out.write("\t\t\t\t<td width=\"1%\" nowrap>Version</td>\r\n");
      out.write("\t\t\t\t<td width=\"97%\">Description</td>\r\n");
      out.write("\t\t\t\t<td width=\"1%\" nowrap align=\"center\">Properties</td>\r\n");
      out.write("\t\t\t\t<td width=\"1%\" nowrap align=\"center\">Remove</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t");
	for( int i=0; i<installedFilters.length; i++ ) { 
      out.write("\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td>");
      out.print( installedFilters[i].getName() );
      out.write("</td>\r\n");
      out.write("\t\t\t\t\t<td align=\"center\">");
      out.print( installedFilters[i].getMajorVersion() );
      out.write('.');
      out.print( installedFilters[i].getMinorVersion() );
      out.write("</td>\r\n");
      out.write("\t\t\t\t\t<td>");
      out.print( installedFilters[i].getDescription() );
      out.write("</td>\r\n");
      out.write("\t\t\t\t\t<form name=\"editFilterForum\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"hidden\" name=\"filter\" value=\"");
      out.print( i );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"hidden\" name=\"mode\" value=\"");
      out.print( EDIT_FILTER );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t<td align=\"center\"><input type=\"submit\" value=\"Edit..\"></td>\r\n");
      out.write("\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\t<form name=\"removeFilterForm\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"hidden\" name=\"filter\" value=\"");
      out.print( i );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t<input type=\"hidden\" name=\"mode\" value=\"");
      out.print( REMOVE_FILTER );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t<td align=\"center\"><input type=\"radio\" value=\"\" name=\"remove");
      out.print( i );
      out.write("\" onclick=\"if(confirm('Are you sure you want to remove this filter?')){this.form.submit();}\"></td>\r\n");
      out.write("\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t");
	if( mode==EDIT_FILTER && filterID==i ) { 
      out.write("\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<!-- <td><br></td> -->\r\n");
      out.write("\t\t\t\t\t\t<td colspan=5>\r\n");
      out.write("\t\t\t\t\t\t\t");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t<table cellpadding=2 cellspacing=0 border=1 width=\"100%\">\r\n");
      out.write("\t\t\t\t\t\t\t<form action=\"forumFilters.jsp\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print( forumID );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"hidden\" name=\"mode\" value=\"");
      out.print( SAVE_PROPERTIES );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"hidden\" name=\"filter\" value=\"");
      out.print( i );
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t");
	Enumeration props = installedFilters[i].filterPropertyNames();
								if( props == null ) {
							
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td colspan=4><i>No properties to set</i></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\t\t");
	} else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td width=\"1%\" nowrap>Name</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td width=\"1%\" nowrap>Value</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td width=\"97%\">Description</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td width=\"1%\" rowspan=99 align=\"center\"><input type=\"submit\" value=\"Save\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\t\t");
		while( props.hasMoreElements() ) {
										String propName = (String)props.nextElement();
							
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td>");
      out.print( propName );
      out.write("</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td><input type=\"text\" name=\"");
      out.print( propName );
      out.write("\" size=\"25\" value=\"");
      out.print( escapeHTML(installedFilters[i].getFilterProperty(propName)) );
      out.write("\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t<td>");
      out.print( installedFilters[i].getFilterPropertyDescription(propName) );
      out.write("</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\t\t\t");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
      out.write("\r\n");
      out.write("\t\t");
			} 
      out.write("\r\n");
      out.write("\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<br>\r\n");
      out.write("\t\r\n");
      out.write("\t");
      out.write("\r\n");
      out.write("\t<table cellpadding=5 cellspacing=0 border=1 width=\"50%\">\r\n");
      out.write("\t<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t\t<td align=\"center\">Install New WebFilter</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t<table cellpadding=3 cellspacing=0 border=1 width=\"100%\">\r\n");
      out.write("\t\t<form action=\"forumFilters.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"forum\" value=\"");
      out.print(forumID);
      out.write("\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"mode\" value=\"");
      out.print(ADD_FILTER);
      out.write("\">\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"1%\" nowrap align=\"center\" valign=\"top\">\r\n");
      out.write("\t\t\tFilters:<br>\r\n");
      out.write("\t\t\t<select name=\"filter\" size=\"7\" onchange=\"doDesc(this.form);\">\r\n");
      out.write("\t\t\t");
	for( int i=0; i<allFilters.length; i++ ) {
					// if the filter is not currently installed, show it as 
					// being available for install.
					if( !containsString( installedFilters, allFilters[i].getName() ) ) {
			
      out.write("\r\n");
      out.write("\t\t\t\t\t<option value=\"");
      out.print( i );
      out.write('"');
      out.write('>');
      out.print( allFilters[i].getName() );
      out.write("\r\n");
      out.write("\t\t\t");
		} 
      out.write("\r\n");
      out.write("\t\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t</select>\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td width=\"98%\"><br></td>\r\n");
      out.write("\t\t\t<td width=\"1%\" nowrap align=\"center\" valign=\"top\">\r\n");
      out.write("\t\t\t\tDescription:<br>\r\n");
      out.write("\t\t\t\t<textarea rows=\"6\" cols=\"20\" name=\"filterDesc\" wrap=\"virtual\"></textarea>\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td colspan=\"3\" align=\"center\"><input type=\"submit\" value=\"Install...\"></td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t");
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
