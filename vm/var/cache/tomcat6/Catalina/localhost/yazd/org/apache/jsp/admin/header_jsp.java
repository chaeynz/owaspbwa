package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;

public final class header_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 *	$RCSfile: header.jsp,v $
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
	////////////////////////////////////////
	// figure out what type of user we are:
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isForumAdmin  = ((Boolean)session.getValue("yazdAdmin.forumAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	boolean isModerator  = ((Boolean)session.getValue("yazdAdmin.Moderator")).booleanValue();

      out.write("\r\n");
      out.write("\r\n");
	////////////////
	// get parameters
	
	String tab = ParamUtils.getParameter(request,"tab");
	if( tab == null ) {
		tab = "global";
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>header.jsp</title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body background=\"images/backleft.gif\" marginwidth=0 marginheight=0 leftmargin=0 topmargin=0 bgcolor=\"#ffffff\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" height=\"100%\">\r\n");
      out.write("<tr height=\"100%\">\r\n");
      out.write("\t<td height=\"100%\" width=\"1%\" nowrap valign=\"bottom\"\r\n");
      out.write("\t><img src=\"images/title.gif\" hspace=\"5\" width=\"123\" height=\"50\" alt=\"Yazd Administration Tool\" border=\"0\"\r\n");
      out.write("\t><br>");

	if ( isSystemAdmin){ 
      out.write("<a href=\"sidebar.jsp?tree=system\" onclick=\"location.href='header.jsp?tab=global';\" target=\"sidebar\"\r\n");
      out.write("\t><img src=\"images/tabs_global_");
      out.print( (tab.equals("global"))?"on":"off" );
      out.write(".gif\" width=\"138\" height=\"35\" alt=\"\" border=\"0\"\r\n");
      out.write("\t></a>");

	}
	if (isSystemAdmin || isModerator){ 
      out.write("<a href=\"sidebar.jsp?tree=forum\" onclick=\"location.href='header.jsp?tab=forums';\" target=\"sidebar\"\r\n");
      out.write("\t><img src=\"images/tabs_forum_");
      out.print( (tab.equals("forums"))?"on":"off" );
      out.write(".gif\" width=\"138\" height=\"35\" alt=\"\" border=\"0\"\r\n");
      out.write("\t></a>");
}
      out.write("</td><td height=\"100%\" width=\"98%\" valign=\"bottom\"\r\n");
      out.write("\t><img src=\"images/tabs_padding.gif\" width=\"100%\" height=\"85\" alt=\"\" border=\"0\"></td\r\n");
      out.write("\t><td height=\"100%\" width=\"1%\" valign=\"bottom\"\r\n");
      out.write("\t><a href=\"index.jsp?logout=true\"\r\n");
      out.write("\t><img src=\"images/logout.gif\" width=\"64\" height=\"85\" alt=\"\" border=\"0\" target=\"_top\"></a></td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
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
