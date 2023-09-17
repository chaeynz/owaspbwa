package org.apache.jsp.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.forum.util.tree.*;
import com.Yasna.forum.util.admin.*;

public final class sidebar_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

	////////////////////
	// page variables
	private final String bullet = "<font color='#666666'>&#149;</font>";

	////////////////////
	// page methods
	
	private final String printTree( com.Yasna.forum.util.tree.Tree tree ) {
		StringBuffer buf = new StringBuffer();
		for( int i=0; i<tree.size(); i++ ) {
			com.Yasna.forum.util.tree.TreeObject treeObject = tree.getChild(i);
			if( treeObject.getType() == tree.NODE ) {
				com.Yasna.forum.util.tree.TreeNode node 
					= (com.Yasna.forum.util.tree.TreeNode)treeObject;
				buf.append( printNodeHTML(tree,node) );
				if( node.isVisible() ) {
					buf.append( printTree( node.getChildren() ) );
				}
			}
			else {
				com.Yasna.forum.util.tree.TreeLeaf leaf 
					= (com.Yasna.forum.util.tree.TreeLeaf)treeObject;
				buf.append( printLeafHTML(leaf) );
			}
		}
		return buf.toString();
	}
	
	private final String printNodeHTML( com.Yasna.forum.util.tree.Tree tree, 
			com.Yasna.forum.util.tree.TreeNode node ) 
	{
		StringBuffer buf = new StringBuffer();
		buf.append( "<table cellpadding='3' cellspacing='0' border='0' width='100%'><td width='1%'>" );
		buf.append( "<a href='sidebar.jsp?flip=" ).append(node.getId()).append("&tree=").append(tree.getName()).append("'>" );
		if( node.isVisible() ) {
			buf.append( "<img src='images/minus.gif' width='11' height='11' border='0'>" );
		}
		else {
			buf.append( "<img src='images/plus.gif' width='11' height='11' border='0'>" );
		}
		buf.append( "</a>" );
		buf.append( "</td><td width='99%' class='sidebarNode'><font size='-1'>" );
		String link = node.getLink();
		if( link != null ) {
			buf.append( "<a href='").append(link).append("' target='main' class='sidebarLink'>" )
			   .append( node.getName() ).append( "</a>" );
		}
		else {
			buf.append( node.getName() );
		}
		buf.append( "</font></td></table>" );
		return buf.toString();
	}
	
	private final String printLeafHTML( com.Yasna.forum.util.tree.TreeLeaf leaf ) {
		StringBuffer buf = new StringBuffer();
		buf.append( "<table cellpadding='3' cellspacing='0' border='0' width='100%'><td width='1%'>" );
		buf.append( "<img src='images/blank.gif' width='11' height='11' border='0'>" );
		buf.append( "</td><td width='1%'>").append(bullet).append("</td><td width='98%' class='sidebarLeaf'>" );
		buf.append( "<a href='" ).append( leaf.getLink() ).append( "' target='main' class='sidebarLink'>" );
		buf.append( leaf.getName() );
		buf.append( "</a></td></table>" );
		return buf.toString();
	}

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
 *	$RCSfile: sidebar.jsp,v $
 *	$Revision: 1.3 $
 *	$Date: 2000/12/18 02:06:21 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t \r\n");
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
	//////////////////////
	// Get parameters
	
	// "tree" (tells which tree to display
	String treeName = ParamUtils.getParameter(request,"tree");
	
	// "flip" (id of node to expand/contract)
	int flip = ParamUtils.getIntParameter(request,"flip",-1);

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
	///////////////////////////////////////
	// Figure out which tree to display
	
	com.Yasna.forum.util.tree.Tree tree = null;
	
	// if treeName is null, try to show the system tree by default if
	// that user has access to see it:
	if( treeName == null ) {
		if( isSystemAdmin || isGroupAdmin ) {
			tree = adminBean.getTree("systemTree");
		}else if (isModerator){
			tree = adminBean.getTree("forumTree");
		}
	}
	// treeName is not null so use that variable to get the tree
	else {
		if( treeName.equals("system") && 
			(isSystemAdmin || isGroupAdmin) ) 
		{
			tree = adminBean.getTree("systemTree");
		}
		else if( treeName.equals("forum") && ( isSystemAdmin || isForumAdmin || isModerator ) ) {
			tree = adminBean.getTree("forumTree");
		}
	}
	
	// Flip any nodes that need to be flipped:
	if( flip > -1 ) {
		com.Yasna.forum.util.tree.TreeNode node 
			= (com.Yasna.forum.util.tree.TreeNode)tree.getChild(flip);
		node.toggleVisible();
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>sidebar.jsp</title>\r\n");
      out.write("\t<link rel=\"stylesheet\" href=\"style/global.css\">\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body bgcolor=\"#dddddd\" text=\"#000000\" link=\"#0000ff\" vlink=\"#0000ff\" alink=\"#ff0000\">\r\n");
      out.write("\r\n");
      out.write("<img src=\"images/blank.gif\" width=\"50\" height=\"10\" border=\"0\"><br>\r\n");
      out.write("\r\n");
	if( tree != null ) { 
      out.write('\r');
      out.write('\n');
      out.write('	');
      out.print( printTree(tree) );
      out.write('\r');
      out.write('\n');
	} 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<br><br>\r\n");
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
