package org.apache.jsp.bay;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.text.*;
import com.Yasna.forum.*;
import com.Yasna.forum.util.*;
import com.Yasna.util.*;

public final class recentMessages_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


	// List threads of the last 31 days
	final static long MAX_AGE 	= 1000L * 60 * 60 * 24 * 31;
	// At most, 8 threads in the list
	final static int MAX_MSGS	= 8;
	// At most, 3 messages per forum
	final static int MAX_MSGS_PER_FORUM = 3;

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
 *	$RCSfile: recentMessages.jsp,v $
 *	$Revision: 1.4 $
 *	$Date: 2000/12/27 22:39:45 $
 */

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
	////////////////////////
	// Authorization check
	
	// check for the existence of an authorization token
	Authorization authToken = SkinUtils.getUserAuthorization(request,response);
	
	// if the token was null, they're not authorized. Since this skin will
	// allow guests to view forums, we'll set a "guest" authentication
	// token
	if( authToken == null ) {
		authToken = AuthorizationFactory.getAnonymousAuthorization();
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

	// Message age cutoff timestamp
	long 	boundary = System.currentTimeMillis() - MAX_AGE;
	// Remember to declare the table tags
	boolean	needHeader = true;
	
	// titles of the most recent threads
	String[] titles = new String[MAX_MSGS];
	String title;
	// age, forumId, threadID, msgID of most recent threads
	long[][] msgs = new long[MAX_MSGS][4];
	long[]	msg;
	int 	n;
	int 	nr;
	long 	age;
	
	// Loop through all the forums
	Iterator allForums = ForumFactory.getInstance(authToken).forums();
	while (allForums.hasNext()) {
		// per forum, loop through the first x recent threads
		Forum f = (Forum)allForums.next();
		Iterator fMsgs = f.threads();
		for (nr = 0; nr < MAX_MSGS_PER_FORUM && fMsgs.hasNext() ; ) {
			// if this thread is newer than what we have until now
			ForumThread t = (ForumThread)fMsgs.next();
			age = t.getModifiedDate().getTime();
			if (age > boundary && age > msgs[0][0]) {
				// gets its vitals and sort it into the list of recent msgs
				Iterator ms = t.messages(t.getMessageCount()-1, 1);
				ForumMessage m = (ForumMessage)ms.next();
				title = m.getSubject() + "</a> &nbsp; (" + t.getMessageCount() + ")";
				titles[0] = title;
				msg = msgs[0];
				msg[0] = age; msg[1] = f.getID(); msg[2] = t.getID(); msg[3] = m.getID();
				for (n = 1; n < MAX_MSGS && age > msgs[n][0]; n++) {
					titles[n-1] = titles[n];
					titles[n] = title;
					msgs[n-1] = msgs[n];
					msgs[n] = msg;
				}
				nr++;
			}
		}
	}
	
	// if we found any msgs, output them into a table
	for (n = MAX_MSGS -1; n >= 0 ; n--) {
		if (msgs[n][0] > 0) {
			if (needHeader) {
				needHeader = false;
				
      out.write("\r\n");
      out.write("\t\t\t\t<table bgcolor=\"#eeeeee\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n");
      out.write("\t\t\t\t<tr bgcolor=\"#cccccc\">\r\n");
      out.write("\t\t\t\t<td align=\"center\" colspan=\"2\">\r\n");
      out.write("\t\t\t\t\t<b>Recent Discussions</b>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t");

			}		
			
      out.write("\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td width=\"1%\" valign=\"top\" align=\"center\"><font size=\"-1\">&#149;</font></td>\r\n");
      out.write("\t\t\t\t<td width=\"99%\">\r\n");
      out.write("\t\t\t\t<font size=\"-1\">\r\n");
      out.write("\t\t\t\t<a href=\"viewMessage.jsp?message=");
      out.print( msgs[n][3] );
      out.write("&thread=");
      out.print( msgs[n][2] );
      out.write("&forum=");
      out.print( msgs[n][1] );
      out.write("&parent=-1\">");
      out.print( titles[n] );
      out.write("\r\n");
      out.write("\t\t\t\t</font>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t");

		}
	}

	if (!needHeader) {
		
      out.write(" </table> ");

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
