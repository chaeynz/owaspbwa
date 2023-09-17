package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.*;
import com.Yasna.forum.*;
import com.Yasna.util.*;
import com.Yasna.forum.database.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.admin.*;

public final class cache_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	
	Runtime runtime = Runtime.getRuntime();

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
 *	$RCSfile: cache.jsp,v $
 *	$Revision: 1.3.2.1 $
 *	$Date: 2001/05/10 02:06:34 $
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
	////////////////////
	// Security check
	
	// make sure the user is authorized to create forums::
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isSystemAdmin ) {
		request.setAttribute("message","No permission to create forums");
		response.sendRedirect("error.jsp");
		return;
	}

      out.write("\r\n");
      out.write("\r\n");
	/////////////////
	// get parameters
	
	int cacheID = ParamUtils.getIntParameter(request,"cacheID",-1);
	boolean doClear = ParamUtils.getBooleanParameter(request,"doClear");
	boolean doEdit = ParamUtils.getBooleanParameter(request,"doEdit");
	boolean doSave = ParamUtils.getBooleanParameter(request,"doSave");
	int maxSize = ParamUtils.getIntParameter(request,"cacheMaxSize",-1);
	boolean cacheEnabled = ParamUtils.getBooleanParameter(request,"cacheEnabled");
	boolean doCache = ParamUtils.getBooleanParameter(request,"doCache");
	
	DbForumFactory dbForumFactory = null;
	try {
		dbForumFactory = (DbForumFactory)((ForumFactoryProxy)forumFactory).getUnderlyingForumFactory(); 
	} catch (Exception e) { }
	DbCacheManager cacheManager = dbForumFactory.getCacheManager();
	
	// clear the requested cache
	if( doClear ) {
		if( cacheID != -1 ) {
			cacheManager.clear(cacheID);
		}
	}
	
	if( doSave ) {
		if( cacheID != -1 ) {
			Cache cache = cacheManager.getCache(cacheID);
			cache.setMaxSize(maxSize*1024);
		}
	}
	
	// turn the cache on or off
	if( doCache ) {
		cacheManager.setCacheEnabled(cacheEnabled);
	}
	cacheEnabled = cacheManager.isCacheEnabled();

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title></title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("\t<script language=\"JavaScript\" type=\"text/javascript\">\r\n");
      out.write("\tfunction convert(src,dest) {\r\n");
      out.write("\t\tif( dest.value == \"\" ) { return; }\r\n");
      out.write("\t\tvar unit = src.options[src.selectedIndex].value;\r\n");
      out.write("\t\tif( unit == \"K\" ) {\r\n");
      out.write("\t\t\tdest.value = dest.value * 1024;\r\n");
      out.write("\t\t} else if( unit == \"MB\" ) {\r\n");
      out.write("\t\t\tdest.value = dest.value / 1024;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t}\r\n");
      out.write("\t</script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/shadowBack.gif\" bgcolor=\"#ffffff\" text=\"#000000\" link=\"#0000ff\" vlink=\"#800080\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Cache" };

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
      out.write("<b>Cache Summary</b>\r\n");
      out.write("<ul>\r\n");
      out.write("\t");
  DecimalFormat formatter = new DecimalFormat("#.00");
	
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<table bgcolor=\"#999999\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t<tr bgcolor=\"#eeeeee\">\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\"><b>Cache Type</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\"><b>Size</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\"><b>Objects</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\"><b>Effectiveness</b></td>\r\n");
      out.write("\t<td class=\"forumCellHeader\" align=\"center\">&nbsp;</td>\r\n");
      out.write("    </tr>\r\n");
      out.write("\t");

		Cache cache;
		double memUsed, totalMem, freeMem, hitPercent;
		long hits, misses;
	
      out.write("\r\n");
      out.write("\t\t\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t");
 cache = cacheManager.getCache(DbCacheManager.MESSAGE_CACHE); 
      out.write("\r\n");
      out.write("\t\t<td>Message</td>\r\n");
      out.write("\t\t");
 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		
      out.write(" \r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(totalMem) );
      out.write(" MB,\r\n");
      out.write("\t\t\t");
      out.print( formatter.format(freeMem));
      out.write("% free\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=right>&nbsp;");
      out.print( cache.getNumElements() );
      out.write("&nbsp;</td>\r\n");
      out.write("\t\t");
 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		
      out.write("\r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(hitPercent));
      out.write('%');
      out.write(' ');
      out.write('(');
      out.print( hits );
      out.write(" hits, ");
      out.print( misses );
      out.write(" misses)</td> \r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doEdit\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.MESSAGE_CACHE );
      out.write("\">\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.MESSAGE_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td><input type=\"submit\" value=\"Clear Cache\"></td> \r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	if( doEdit && cacheID==DbCacheManager.MESSAGE_CACHE ) { 
      out.write("\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doSave\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.MESSAGE_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td colspan=\"4\">\r\n");
      out.write("\t\t\tSet message size:\r\n");
      out.write("\t\t\t<input type=\"text\" value=\"");
      out.print( cache.getMaxSize()/1024 );
      out.write("\" size=\"6\"\r\n");
      out.write("\t\t\t name=\"cacheMaxSize\">K \r\n");
      out.write("\t\t\t <br>\r\n");
      out.write("\t\t\t 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Save\"></td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.THREAD_CACHE );
      out.write("\">\r\n");
      out.write("\t\t");
 cache = cacheManager.getCache(DbCacheManager.THREAD_CACHE); 
      out.write("\r\n");
      out.write("\t\t<td>Thread</td>\r\n");
      out.write("\t\t");
 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		
      out.write(" \r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(totalMem) );
      out.write(" MB,\r\n");
      out.write("\t\t\t");
      out.print( formatter.format(freeMem));
      out.write("% free\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=right>&nbsp;");
      out.print( cache.getNumElements() );
      out.write("&nbsp;</td>\r\n");
      out.write("\t\t");
 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		
      out.write("\r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(hitPercent));
      out.write('%');
      out.write(' ');
      out.write('(');
      out.print( hits );
      out.write(" hits, ");
      out.print( misses );
      out.write(" misses)</td> \r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doEdit\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.THREAD_CACHE );
      out.write("\">\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.THREAD_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td><input type=\"submit\" value=\"Clear Cache\"></td> \r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	if( doEdit && cacheID==DbCacheManager.THREAD_CACHE ) { 
      out.write("\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doSave\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.THREAD_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td colspan=\"4\">\r\n");
      out.write("\t\t\tSet message size:\r\n");
      out.write("\t\t\t<input type=\"text\" value=\"");
      out.print( cache.getMaxSize()/1024 );
      out.write("\" size=\"6\"\r\n");
      out.write("\t\t\t name=\"cacheMaxSize\">K \r\n");
      out.write("\t\t\t <br>\r\n");
      out.write("\t\t\t 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Save\"></td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.FORUM_CACHE );
      out.write("\">\r\n");
      out.write("\t\t");
 cache = cacheManager.getCache(DbCacheManager.FORUM_CACHE); 
      out.write("\r\n");
      out.write("\t\t<td>Forum</td>\r\n");
      out.write("\t\t");
 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		
      out.write(" \r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(totalMem) );
      out.write(" MB,\r\n");
      out.write("\t\t\t");
      out.print( formatter.format(freeMem));
      out.write("% free\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=right>&nbsp;");
      out.print( cache.getNumElements() );
      out.write("&nbsp;</td>\r\n");
      out.write("\t\t");
 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		
      out.write("\r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(hitPercent));
      out.write('%');
      out.write(' ');
      out.write('(');
      out.print( hits );
      out.write(" hits, ");
      out.print( misses );
      out.write(" misses)</td> \r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doEdit\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.FORUM_CACHE );
      out.write("\">\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.FORUM_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td><input type=\"submit\" value=\"Clear Cache\"></td> \r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	if( doEdit && cacheID==DbCacheManager.FORUM_CACHE ) { 
      out.write("\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doSave\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.FORUM_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td colspan=\"4\">\r\n");
      out.write("\t\t\tSet message size:\r\n");
      out.write("\t\t\t<input type=\"text\" value=\"");
      out.print( cache.getMaxSize()/1024 );
      out.write("\" size=\"6\"\r\n");
      out.write("\t\t\t name=\"cacheMaxSize\">K \r\n");
      out.write("\t\t\t <br>\r\n");
      out.write("\t\t\t 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Save\"></td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.USER_CACHE );
      out.write("\">\r\n");
      out.write("\t\t");
 cache = cacheManager.getCache(DbCacheManager.USER_CACHE); 
      out.write("\r\n");
      out.write("\t\t<td>User</td>\r\n");
      out.write("\t\t");
 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		
      out.write(" \r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(totalMem) );
      out.write(" MB,\r\n");
      out.write("\t\t\t");
      out.print( formatter.format(freeMem));
      out.write("% free\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=right>&nbsp;");
      out.print( cache.getNumElements() );
      out.write("&nbsp;</td>\r\n");
      out.write("\t\t");
 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		
      out.write("\r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(hitPercent));
      out.write('%');
      out.write(' ');
      out.write('(');
      out.print( hits );
      out.write(" hits, ");
      out.print( misses );
      out.write(" misses)</td> \r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doEdit\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.USER_CACHE );
      out.write("\">\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.USER_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td><input type=\"submit\" value=\"Clear Cache\"></td> \r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	if( doEdit && cacheID==DbCacheManager.USER_CACHE ) { 
      out.write("\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doSave\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.USER_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td colspan=\"4\">\r\n");
      out.write("\t\t\tSet message size:\r\n");
      out.write("\t\t\t<input type=\"text\" value=\"");
      out.print( cache.getMaxSize()/1024 );
      out.write("\" size=\"6\"\r\n");
      out.write("\t\t\t name=\"cacheMaxSize\">K \r\n");
      out.write("\t\t\t <br>\r\n");
      out.write("\t\t\t 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Save\"></td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.GROUP_CACHE );
      out.write("\">\r\n");
      out.write("\t\t");
 cache = cacheManager.getCache(DbCacheManager.GROUP_CACHE); 
      out.write("\r\n");
      out.write("\t\t<td>Group</td>\r\n");
      out.write("\t\t");
 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		
      out.write(" \r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(totalMem) );
      out.write(" MB,\r\n");
      out.write("\t\t\t");
      out.print( formatter.format(freeMem));
      out.write("% free\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=right>&nbsp;");
      out.print( cache.getNumElements() );
      out.write("&nbsp;</td>\r\n");
      out.write("\t\t");
 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		
      out.write("\r\n");
      out.write("\t\t<td>");
      out.print( formatter.format(hitPercent));
      out.write('%');
      out.write(' ');
      out.write('(');
      out.print( hits );
      out.write(" hits, ");
      out.print( misses );
      out.write(" misses)</td> \r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doEdit\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.GROUP_CACHE );
      out.write("\">\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doClear\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.GROUP_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td><input type=\"submit\" value=\"Clear Cache\"></td> \r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	if( doEdit && cacheID==DbCacheManager.GROUP_CACHE ) { 
      out.write("\r\n");
      out.write("\t<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t\t<form action=\"cache.jsp\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"doSave\" value=\"true\">\r\n");
      out.write("\t\t<input type=\"hidden\" name=\"cacheID\" value=\"");
      out.print( DbCacheManager.GROUP_CACHE );
      out.write("\">\r\n");
      out.write("\t\t<td colspan=\"4\">\r\n");
      out.write("\t\t\tSet message size:\r\n");
      out.write("\t\t\t<input type=\"text\" value=\"");
      out.print( cache.getMaxSize()/1024 );
      out.write("\" size=\"6\"\r\n");
      out.write("\t\t\t name=\"cacheMaxSize\">K \r\n");
      out.write("\t\t\t <br>\r\n");
      out.write("\t\t\t 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Save\"></td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t");
	} 
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("<b>Java VM Memory</b>\r\n");
      out.write("<ul>\r\n");
      out.write(" \t");
   
		double freeMemory = (double)runtime.freeMemory()/(1024*1024);
		double totalMemory = (double)runtime.totalMemory()/(1024*1024);
		double usedMemory = totalMemory - freeMemory;
		double percentFree = ((double)freeMemory/(double)totalMemory)*100.0;
	
      out.write("\r\n");
      out.write("\t<table border=0>\r\n");
      out.write("\t<tr><td>Used Memory:</td><td>");
      out.print( formatter.format(usedMemory) );
      out.write(" MB</td></tr>\r\n");
      out.write("\t<tr><td>Total Memory:</td><td>");
      out.print( formatter.format(totalMemory) );
      out.write(" MB</td></tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<br>\r\n");
      out.write("\t<!--\r\n");
      out.write("\t<table bgcolor=\"#000000\" cellpadding=\"1\" cellspacing=\"0\" border=\"0\" width=\"300\">\r\n");
      out.write("\t<td>\r\n");
      out.write("\t<table bgcolor=\"#ffffff\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t<td width=\"");
      out.print( percentFree );
      out.write("%\" background=\"images/cache.gif\"><img src=\"images/blank.gif\" width=\"");
      out.print( percentFree );
      out.write("\" height=\"20\" border=\"0\"></td>\r\n");
      out.write("\t<td width=\"");
      out.print( 100-percentFree );
      out.write("%\"></td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t<b>");
      out.print( formatter.format(percentFree) );
      out.write("% free</b>\r\n");
      out.write("\t<p>\r\n");
      out.write("\t-->\r\n");
      out.write("\t");
	int free = 100-(int)Math.round(percentFree); 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t");
	int tableWidth = 200; 
      out.write('\r');
      out.write('\n');
      out.write('	');
	int numBlocks = 50; 
      out.write("\r\n");
      out.write("<table border=0><td>\t\r\n");
      out.write("<table bgcolor=\"#000000\" cellpadding=\"1\" cellspacing=\"0\" border=\"0\" width=\"");
      out.print( tableWidth );
      out.write("\" align=left>\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#000000\" cellpadding=\"1\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
 for( int i=0; i<numBlocks; i++ ) { 
      out.write('\r');
      out.write('\n');
		if( (i*(100/numBlocks)) < free ) { 
      out.write("\r\n");
      out.write("\t<td bgcolor=\"#00ff00\" width=\"");
      out.print( (100/numBlocks) );
      out.write("%\"><img src=\"blank.gif\" width=\"1\" height=\"15\" border=\"0\"></td>\r\n");
		} else { 
      out.write("\r\n");
      out.write("\t<td bgcolor=\"#006600\" width=\"");
      out.print( (100/numBlocks) );
      out.write("%\"><img src=\"blank.gif\" width=\"1\" height=\"15\" border=\"0\"></td>\r\n");
		} 
      out.write('\r');
      out.write('\n');
	} 
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table></td><td> &nbsp;<b>");
      out.print( formatter.format(percentFree) );
      out.write("% free</b> </td></table>\r\n");
      out.write("\t\t\r\n");
      out.write("</ul>\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\r\n");
      out.write("<b>Cache Status</b>\r\n");
      out.write("<ul>\r\n");
      out.write("\r\n");
      out.write("<form action=\"cache.jsp\">\r\n");
      out.write("<input type=\"hidden\" name=\"doCache\" value=\"true\">\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"300\">\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#666666\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\" width=\"100%\">\r\n");
      out.write("<tr bgcolor=\"#ffffff\">\r\n");
      out.write("\t<td align=\"center\"");
      out.print( (cacheEnabled)?" bgcolor=\"#99cc99\"":"" );
      out.write(">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"cacheEnabled\" value=\"true\" id=\"rb01\"\r\n");
      out.write("\t\t ");
      out.print( (cacheEnabled)?"checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb01\">");
      out.print( (cacheEnabled)?"<b>On</b>":"On" );
      out.write("</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td align=\"center\"");
      out.print( (!cacheEnabled)?" bgcolor=\"#cc6666\"":"" );
      out.write(">\r\n");
      out.write("\t\t<input type=\"radio\" name=\"cacheEnabled\" value=\"false\" id=\"rb02\"\r\n");
      out.write("\t\t ");
      out.print( (!cacheEnabled)?"checked":"" );
      out.write(">\r\n");
      out.write("\t\t<label for=\"rb02\">");
      out.print( (!cacheEnabled)?"<b>Off</b>":"Off" );
      out.write("</label>\r\n");
      out.write("\t</td>\r\n");
      out.write("\t<td align=\"center\">\r\n");
      out.write("\t\t<input type=\"submit\" value=\"Update\">\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</table>\r\n");
      out.write("</ul>\r\n");
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
