package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import dbProcs.Getter;
import utils.*;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

public final class scoreboard_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html; charset=iso-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");

	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG: scoreboard.jsp *************************");

/**
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 * @author Mark Denihan
 */
 if (request.getSession() != null)
 {
 	HttpSession ses = request.getSession();
 	//Getting CSRF Token from client
 	Cookie tokenCookie = null;
 	try
 	{
 		tokenCookie = Validate.getToken(request.getCookies());
 	}
 	catch(Exception htmlE)
 	{
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(scoreboard.jsp): tokenCookie Error:" + htmlE.toString());
 	}
 	// validateSession ensures a valid session, and valid role credentials
 	// Also, if tokenCookie != null, then the page is good to continue loading
 	if (Validate.validateSession(ses) && tokenCookie != null)
 	{
 		//Log User Name
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Scoreboard accessed by: " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
 		// Getting Session Variables
 		boolean canSeeScoreboard = ScoreboardStatus.canSeeScoreboard((String)ses.getAttribute("userRole"));
 		//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
 		Encoder encoder = ESAPI.encoder();
 		String csrfToken = encoder.encodeForHTML(tokenCookie.getValue());
		
      out.write("\r\n");
      out.write("\t\t<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("\t\t<head>\r\n");
      out.write("\t\t<title>OWASP Security Shepherd - Scoreboard</title>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<link href=\"css/theCss.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("\t\t<link rel=\"shortcut icon\" href=\"css/images/flavicon.jpg\" type=\"image/jpeg\" />\r\n");
      out.write("\t\t</head>\r\n");
      out.write("\t\t<body>\r\n");
      out.write("\t\t<script type=\"text/javascript\" src=\"js/jquery.js\"></script>\r\n");
      out.write("\t\t<script type=\"text/javascript\" src=\"js/tinysort.js\"></script>\r\n");
      out.write("\t\t<div id=\"wrapper\">\r\n");
      out.write("\t\t<!-- start header -->\r\n");
      out.write("\t\t<div id=\"header\">\r\n");
      out.write("\t\t\t<h1>Scoreboard</h1>\r\n");
      out.write("\t\t\t<p>The OWASP Security Shepherd Project</p>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<!-- end header -->\r\n");
      out.write("\t\t<!-- start page -->\r\n");
      out.write("\t\t<div id=\"page\">\r\n");
      out.write("\t\t\t<!-- start content -->\r\n");
      out.write("\t\t\t\t<div id=\"badData\"></div>\r\n");
      out.write("\t\t\t\t");
 if(canSeeScoreboard) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<ul id=\"leaderboard\" class=\"leaderboard\"></ul>\r\n");
      out.write("\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t<p>Scoreboard is not currently available!</p>\r\n");
      out.write("\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<!-- end content -->\r\n");
      out.write("\t\t\t<!-- start sidebar -->\r\n");
      out.write("\t\t\t<!-- end sidebar -->\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<!-- end page -->\r\n");
      out.write("\t\t");
 if(canSeeScoreboard) { 
      out.write("\r\n");
      out.write("\t\t\t<script>\r\n");
      out.write("\t\t\t\tvar windowIsActive = true;\r\n");
      out.write("\t\r\n");
      out.write("\t\t\t\twindow.onfocus = function windowFocus () { \r\n");
      out.write("\t\t\t\t\twindowIsActive = true; \r\n");
      out.write("\t\t\t\t}; \r\n");
      out.write("\t\r\n");
      out.write("\t\t\t\twindow.onblur = function windowBlur () { \r\n");
      out.write("\t\t\t\t\twindowIsActive = false; \r\n");
      out.write("\t\t\t\t}; \r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\tfunction timeSince(date) {\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t    var seconds = Math.floor((new Date() - date) / 1000);\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t    var interval = Math.floor(seconds / 31536000);\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t    if (interval > 1) {\r\n");
      out.write("\t\t\t\t        return interval + \" years\";\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t    interval = Math.floor(seconds / 2592000);\r\n");
      out.write("\t\t\t\t    if (interval > 1) {\r\n");
      out.write("\t\t\t\t        return interval + \" months\";\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t    interval = Math.floor(seconds / 86400);\r\n");
      out.write("\t\t\t\t    if (interval > 1) {\r\n");
      out.write("\t\t\t\t        return interval + \" days\";\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t    interval = Math.floor(seconds / 3600);\r\n");
      out.write("\t\t\t\t    if (interval > 1) {\r\n");
      out.write("\t\t\t\t        return interval + \" hours\";\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t    interval = Math.floor(seconds / 60);\r\n");
      out.write("\t\t\t\t    if (interval > 1) {\r\n");
      out.write("\t\t\t\t        return interval + \" minutes\";\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t    \r\n");
      out.write("\t\t\t\t    if (Math.floor(seconds) >= 60) {\r\n");
      out.write("\t\t\t\t    \treturn \"1 minute\"\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t    else {\r\n");
      out.write("\t\t\t\t    \treturn \"< 1 minute\";\r\n");
      out.write("\t\t\t\t    }\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\tvar lastUpdated = new Date();\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t//Scoreboard based on http://mightystuff.net/dynamic-leaderboard\r\n");
      out.write("\t\t\t\tfunction poll() {\r\n");
      out.write("\t\t\t\t\tif(!windowIsActive) { //If Window/Tab is currently not in focus, wait to do the magic\r\n");
      out.write("\t\t\t\t\t\tconsole.log ( 'Window not active. Waiting' );\r\n");
      out.write("\t\t\t\t\t\t$(\"#badData\").html('<center>Scoreboard last updated ' + timeSince(lastUpdated) + ' ago</center>');\r\n");
      out.write("\t\t\t\t\t\t$(\"#badData\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\tt=setTimeout(\"poll()\", 500); // try again really soon\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\telse { // Window is Active. Do Magic\r\n");
      out.write("\t\t\t\t\t\tconsole.log ( 'Window Active. Refreshing' );\r\n");
      out.write("\t\t\t\t\t\tvar ajaxCall = $.ajax({\r\n");
      out.write("\t\t\t\t\t\t\ttype: \"POST\",\r\n");
      out.write("\t\t\t\t\t\t\turl: 'scoreboard', // needs to return a JSON array of items having the following properties: id, score, username\r\n");
      out.write("\t\t\t\t\t\t\tdataType: 'json',\r\n");
      out.write("\t\t\t\t\t\t\tdata: {\r\n");
      out.write("\t\t\t\t\t\t\t\tcsrfToken: \"");
      out.print( csrfToken );
      out.write("\"\r\n");
      out.write("\t\t\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\t\t\tasync: false,\r\n");
      out.write("\t\t\t\t\t\t\tsuccess: function(o) {\r\n");
      out.write("\t\t\t\t\t\t\t\t$(\"#badData\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t\t\tfor(i=0;i<o.length;i++) {\r\n");
      out.write("\t\t\t\t\t\t\t\t\tif ($('#userbar-'+ o[i].id).length == 0) {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t// this id doesn't exist, so add it to our list.\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\tvar newUser = '<li class=\"scoreLine\"><div id=\"userbar-'+ o[i].id + '\" class=\"scoreBar\" title=\"' + o[i].userTitle + '\" style=\"width: ' + o[i].scale + '\\u0025;\">' +\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t'<div id=\"userplace-'+ o[i].id + '\" class=\"place\"><h3 style=\"display:none;\" id=\"user-' + o[i].id + '\">' + o[i].order + '</h3>' + getGetOrdinal(o[i].place) + ': </div>' \r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t+ '<div class=\"scoreName\" >'+ o[i].username\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t+ '<div id=\"goldMedals-' + o[i].id + '\" class=\"medalContainer\"><div style=\"' + o[i].goldDisplay + '\"><div class=\"goldMedalAmountBubble\">' + o[i].goldMedalCount + '</div></div></div>'\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t+ '<div id=\"silverMedals-' + o[i].id + '\" class=\"medalContainer\"><div style=\"' + o[i].silverDisplay + '\"><div class=\"silverMedalAmountBubble\">' + o[i].silverMedalCount + '</div></div></div>'\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t+ '<div id=\"bronzeMedals-' + o[i].id + '\" class=\"medalContainer\"><div style=\"' + o[i].bronzeDisplay + '\"><div class=\"bronzeMedalAmountBubble\">' + o[i].bronzeMedalCount + '</div></div></div>'\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t+ '</div><div class=\"scoreNumber\" id=\"userscore-'+ o[i].id + '\">' + o[i].score + '</div></div></li>';\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$(\"#leaderboard\").append(newUser);\r\n");
      out.write("\t\t\t\t\t\t\t\t\t} else {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t// this id does exist\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t//update user elements in the list item.\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#userbar-'+ o[i].id).prop('title', o[i].userTitle);\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#userscore-'+o[i].id).html(o[i].score);\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#userplace-'+o[i].id).html('<h3 style=\"display:none;\" id=\"user-' + o[i].id + '\">' + o[i].order + '</h3>' + getGetOrdinal(o[i].place) + ': ');\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#goldMedals-' + o[i].id).html('<div style=\"' + o[i].goldDisplay + '\"><div class=\"goldMedalAmountBubble\">' + o[i].goldMedalCount + '</div></div></div>');\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#silverMedals-' + o[i].id).html('<div style=\"' + o[i].silverDisplay + '\"><div class=\"silverMedalAmountBubble\">' + o[i].silverMedalCount + '</div></div></div>');\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#bronzeMedals-' + o[i].id).html('<div style=\"' + o[i].bronzeDisplay + '\"><div class=\"bronzeMedalAmountBubble\">' + o[i].bronzeMedalCount + '</div></div></div>');\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$('#userbar-'+ o[i].id).animate({\r\n");
      out.write("\t\t\t\t\t\t\t\t\t        width: o[i].scale+\"%\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\t    }, 1300 );\r\n");
      out.write("\t\t\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t\t\tsort();\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t});\t\r\n");
      out.write("\t\t\t\t\t\tvar fullResponse = new String(ajaxCall.responseText);\r\n");
      out.write("\t\t\t\t\t\tif (fullResponse.startsWith(\"ERROR:\")) {\r\n");
      out.write("\t\t\t\t\t\t\tconsole.log ('Response contained error: ' + fullResponse);\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#badData\").html('<center>' + fullResponse + '</center>');\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#badData\").show(\"slow\");\r\n");
      out.write("\t\t\t\t\t\t\t//Scoreboard will not refresh after this\r\n");
      out.write("\t\t\t\t\t\t\tconsole.log(\"Scoreboard will not refresh following this error\");\r\n");
      out.write("\t\t\t\t\t\t} else {\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#badData\").hide(\"fast\");\r\n");
      out.write("\t\t\t\t\t\t\tlastUpdated = new Date().getTime();\r\n");
      out.write("\t\t\t\t\t\t\t// play it again, sam (7 secs)\r\n");
      out.write("\t\t\t\t\t\t\tt=setTimeout(\"poll()\",7000);\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t//Algorithm from http://tinysort.sjeiti.com/\r\n");
      out.write("\t\t\t\tfunction sort() {\r\n");
      out.write("\t\t\t\t\tvar $Ul = $('ul#leaderboard');\r\n");
      out.write("\t\t\t\t\t$Ul.css({position:'relative',height:$Ul.height(),display:'block'});\r\n");
      out.write("\t\t\t\t\tvar iLnH;\r\n");
      out.write("\t\t\t\t\tvar $Li = $('ul#leaderboard>li');\r\n");
      out.write("\t\t\t\t\t$Li.each(function(i,el){\r\n");
      out.write("\t\t\t\t\t\tvar iY = $(el).position().top;\r\n");
      out.write("\t\t\t\t\t\t$.data(el,'h',iY);\r\n");
      out.write("\t\t\t\t\t\tif (i===1) iLnH = iY;\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\r\n");
      out.write("\t\t\t\t\t$Li.tsort('h3:eq(0)',{order:'asc'}).each(function(i,el){\r\n");
      out.write("\t\t\t\t\t\tvar $El = $(el);\r\n");
      out.write("\t\t\t\t\t\tvar iFr = $.data(el,'h');\r\n");
      out.write("\t\t\t\t\t\tvar iTo = i*iLnH;\r\n");
      out.write("\t\t\t\t\t\t$El.css({position:'absolute',top:iFr}).animate({top:iTo},500);\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\tfunction fixBoard(){\r\n");
      out.write("\t\t\t\t\t$(\"#page\").width($(window).width()*0.8);\r\n");
      out.write("\t\t\t\t\tvar container = $(window);\r\n");
      out.write("\t\t\t\t\tvar content = $('#page');\r\n");
      out.write("\t\t\t\t\tcontent.css(\"left\", (container.width()-content.width())/2);\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\tfixBoard();\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t$(window).resize(function() \r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\tfixBoard();\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\tfunction getGetOrdinal(n) {\r\n");
      out.write("\t\t\t\t   var s=[\"th\",\"st\",\"nd\",\"rd\"],\r\n");
      out.write("\t\t\t\t\t   v=n%100;\r\n");
      out.write("\t\t\t\t   return n+(s[(v-20)%10]||s[v]||s[0]);\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t//Kick off Scoreboard\r\n");
      out.write("\t\t\t\tpoll();\r\n");
      out.write("\t\t\t</script>\r\n");
      out.write("\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t");
 if(Analytics.googleAnalyticsOn) { 
      out.print( Analytics.googleAnalyticsScript );
 } 
      out.write("\r\n");
      out.write("\t\t</body>\r\n");
      out.write("\t</html>\r\n");
      out.write("\t");

 	}
	else
	{
		response.sendRedirect("login.jsp");
	}
}
else
{
	response.sendRedirect("login.jsp");
}

      out.write('\r');
      out.write('\n');
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
