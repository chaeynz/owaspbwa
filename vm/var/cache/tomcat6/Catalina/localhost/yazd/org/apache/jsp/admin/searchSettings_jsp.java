package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class searchSettings_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	/////////////////////
	// global variables
	
	// Date formatter
	private final SimpleDateFormat dateFormatter
		= new SimpleDateFormat( "h:mm:ss aaa z 'on' EEE, MMM d, yyyy" );

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
 *	$RCSfile: searchSettings.jsp,v $
 *	$Revision: 1.3 $
 *	$Date: 2000/12/18 02:06:21 $
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
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isSystemAdmin ) {
		throw new UnauthorizedException("Sorry, you don't have permission to change search settings.");
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////
	// get parameters
	
	boolean doIndex = ParamUtils.getBooleanParameter(request,"doIndex");
	boolean doUpdateIndex = ParamUtils.getBooleanParameter(request,"doUpdateIndex");
	boolean doRebuildIndex = ParamUtils.getBooleanParameter(request,"doRebuildIndex");
	boolean doIntervalUpdate = ParamUtils.getBooleanParameter(request,"doIntervalUpdate");
	boolean makeAutoIndexEnabled = ParamUtils.getBooleanParameter(request,"isAutoIndexEnabled");
	int hourInterval = ParamUtils.getIntParameter(request,"hourInterval",0);
	int minInterval = ParamUtils.getIntParameter(request,"minInterval",0);

      out.write("\r\n");
      out.write("\r\n");
	////////////////
	// load up indexer
	
	SearchIndexer indexer = forumFactory.getSearchIndexer();
	boolean isAutoIndexEnabled = indexer.isAutoIndexEnabled();

      out.write("\r\n");
      out.write("\r\n");
	///////////////
	// rebuild or update index or intervals
	
	if( doUpdateIndex ) {
		indexer.updateIndex();
	}
	if( doRebuildIndex ) {
		indexer.rebuildIndex();
	}
	if( doIntervalUpdate ) {
		indexer.setUpdateInterval( minInterval, hourInterval );
	}
	if( doIndex ) {
		indexer.setAutoIndexEnabled( makeAutoIndexEnabled );
	}
	
	// redirect if necessary
	if( doUpdateIndex || doRebuildIndex || doIntervalUpdate || doIndex ) {
		response.sendRedirect("searchSettings.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	////////////////////////////
	// get properties of the indexer
	
	int hoursUpdateInterval = indexer.getHoursUpdateInterval();
	int minsUpdateInterval = indexer.getMinutesUpdateInterval();
	Date lastIndexedDate = indexer.getLastIndexedDate();

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
	String[] pageTitleInfo = { "Search Settings" };

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
      out.write("The search indexer periodically adds new messages into the searchable \r\n");
      out.write("message database. You can control how often this update takes place by \r\n");
      out.write("adjusting the refresh interval. Messages will not appear in search results \r\n");
      out.write("until they are indexed, so a relatively frequent refresh interval is \r\n");
      out.write("recommended.\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("If you choose, auto-indexing can be turned off.\r\n");
      out.write("However, in that case, messages will not be indexed unless you do so manually.\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form action=\"searchSettings.jsp\">\r\n");
      out.write("<input type=\"hidden\" name=\"doIndex\" value=\"true\">\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"80%\" align=\"center\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Auto Indexing:</td>\r\n");
      out.write("\t<td align=\"center\"");
      out.print( (isAutoIndexEnabled)?" bgcolor=\"#99cc99\"":"" );
      out.write(">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"isAutoIndexEnabled\" value=\"true\" id=\"rb01\"\r\n");
      out.write("\t\t ");
      out.print( (isAutoIndexEnabled)?"checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb01\">");
      out.print( (isAutoIndexEnabled)?"<b>On</b>":"On" );
      out.write("</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td align=\"center\"");
      out.print( (!isAutoIndexEnabled)?" bgcolor=\"#cc6666\"":"" );
      out.write(">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"isAutoIndexEnabled\" value=\"false\" id=\"rb02\"\r\n");
      out.write("\t\t ");
      out.print( (!isAutoIndexEnabled)?"checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb02\">");
      out.print( (!isAutoIndexEnabled)?"<b>Off</b>":"Off" );
      out.write("</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("<p>\r\n");
      out.write("<center>\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Update\">\r\n");
      out.write("</center>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<form action=\"searchSettings.jsp\">\r\n");
      out.write("<input type=\"hidden\" name=\"doIntervalUpdate\" value=\"true\">\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"80%\" align=\"center\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Last indexed:</td>\r\n");
      out.write("\t<td>");
      out.print( dateFormatter.format(lastIndexedDate) );
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td>Refresh Interval:</td>\r\n");
      out.write("\t<td>\r\n");
      out.write("\t\tRefresh every\r\n");
      out.write("\t\t<select size=\"1\" name=\"hourInterval\">\r\n");
      out.write("\t\t");
	for( int i=0; i<24; i++ ) { 
      out.write("\r\n");
      out.write("\t\t");
		String selected = ""; 
      out.write("\r\n");
      out.write("\t\t");
		if( i==hoursUpdateInterval ) { selected = " selected"; } 
      out.write("\r\n");
      out.write("\t\t\t<option value=\"");
      out.print( i );
      out.write('"');
      out.print( selected );
      out.write('>');
      out.print( i );
      out.write("\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t</select>\r\n");
      out.write("\t\thours,\r\n");
      out.write("\t\t<select size=\"1\" name=\"minInterval\">\r\n");
      out.write("\t\t");
	for( int i=0; i<60; ) { 
      out.write("\r\n");
      out.write("\t\t");
		String selected = ""; 
      out.write("\r\n");
      out.write("\t\t");
		if( i==minsUpdateInterval ) { selected = " selected"; } 
      out.write("\r\n");
      out.write("\t\t\t<option value=\"");
      out.print( i );
      out.write('"');
      out.print( selected );
      out.write('>');
      out.print( i );
      out.write("\r\n");
      out.write("\t\t");
	if( i>=10 ) { 
      out.write("\r\n");
      out.write("\t\t");
		i+=5;  
      out.write("\r\n");
      out.write("\t\t");
	} else { i++; } 
      out.write("\r\n");
      out.write("\t\t");
	} 
      out.write("\r\n");
      out.write("\t\t</select>\r\n");
      out.write("\t\tminutes.\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("<p>\r\n");
      out.write("<center>\r\n");
      out.write("\t<input type=\"submit\" value=\"Save Changes\">\r\n");
      out.write("\t&nbsp;\r\n");
      out.write("\t<input type=\"submit\" value=\"Done\"\r\n");
      out.write("\t onclick=\"location.href='main.jsp';return false;\">\r\n");
      out.write("</center>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<b>Force update of index</b>\r\n");
      out.write("<form action=\"searchSettings.jsp\">\r\n");
      out.write("<input type=\"hidden\" name=\"doUpdateIndex\" value=\"true\">\r\n");
      out.write("<ul>\r\n");
      out.write("\tThis will index any messages added since the last indexer was run.<br>\r\n");
      out.write("\t<input type=\"submit\" value=\"Update Index Now\">\r\n");
      out.write("</ul>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<b>Force rebuild of index</b>\r\n");
      out.write("<form action=\"searchSettings.jsp\">\r\n");
      out.write("<input type=\"hidden\" name=\"doRebuildIndex\" value=\"true\">\r\n");
      out.write("<ul>\r\n");
      out.write("\tThis will reindex all messages. (May take a few moments if you have\r\n");
      out.write("\ta lot of messages)<br>\r\n");
      out.write("\t<input type=\"submit\" value=\"Rebuild Index Now\">\r\n");
      out.write("</ul>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
