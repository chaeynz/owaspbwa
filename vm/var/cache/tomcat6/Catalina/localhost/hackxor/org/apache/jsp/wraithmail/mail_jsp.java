package org.apache.jsp.wraithmail;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.security.SecureRandom;

public final class mail_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private String getStealth() throws Exception{
Class.forName("com.mysql.jdbc.Driver").newInstance();
String url = "jdbc:mysql://localhost:3306/wraithlogin";
Connection con=DriverManager.getConnection(url, "stealth", "je984zx");
String query = "select * from stealth";
PreparedStatement hackthis = con.prepareStatement(query);
ResultSet rs=hackthis.executeQuery();
rs.next();
return rs.getString(1);
}

public String ascii(String input){
    String output = "";
    for(int i = 0; i<input.length(); i++){
	char c = input.charAt(i);
	if((c > 47 && c < 58) || (c > 64 && c < 91) || (c > 96 && c < 122))
	    output = output + c;
    }
    if(!(output.matches("[a-zAA-Z0-9]*"))){
    }
    return output;
}

public String createToken(){
      SecureRandom random = new SecureRandom();
      byte bytes[] = new byte[32];
      random.nextBytes(bytes);
      String secret = "";
      for(int i = 0; i<32; i++){
	secret = secret + bytes[i];
    }

return secret.replace("-", "");//craps on randomness but whatever
}

public String getMessage(String messageid, String user, Connection con) throws SQLException{
	String query = "select `toad`, `fromad`,`subject`,`body`  from mail where mid =? AND user = ?";
	PreparedStatement hackthis = con.prepareStatement(query);
	user = ascii(user);
	hackthis.setInt(1, Integer.parseInt(messageid)); 
        hackthis.setString(2, user); 
	ResultSet rs=hackthis.executeQuery();
	String message = "";
	while(rs.next()){
	    message = message + "<p>To " + rs.getString(1) + "<br>";
	    message = message +"From " + rs.getString(2) + "<br>";
	    message = message +"Subject " + rs.getString(3) + "<br>";
	    message = message +"Date " + rs.getString(4) + "<br><br>";
	    message = message +"" + rs.getString(6) + "<br>";
	}
	return message;
}

public String getList(String user, Connection con) throws SQLException{
	String query = "select `toad`, `fromad`,`subject`,`mid`  from mail where toad = ?";
	PreparedStatement hackthis = con.prepareStatement(query);
	user = ascii(user) + "@wraithmail.net";
	hackthis.setString( 1, user); 
	String message = "";
	ResultSet rs=hackthis.executeQuery();
	while(rs.next()){
	    String wtf = "<a id='a' href='http://wraithbox:80/htmlisland.jsp?messageid=" + rs.getString(4) + "'>";
	    message = message + "<tr>"; 
	    message = message + "<td> " +wtf+  rs.getString(1) + "</a></td>";
	    message = message + "<td> " +wtf+ rs.getString(2) + "</a></td>";
	    message = message + "<td> " +wtf+ rs.getString(3) + "</a></td>";
	    message = message + "</tr>\n";
	}
	return message;
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

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"DTD/xhtml1-transitional.dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("\n");
      out.write("\n");
      out.write("<title>Welcome to WRAITHMAIL</title>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"wraithmail.css\" />\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("<div id=\"wrapper\">\n");
      out.write("<div id=\"header\">\n");
      out.write("<a href=\"index.jsp\"><img src=\"wraith.png\" alt=\"Thursday\" class=\"logo3\" width=\"468\" height=\"60\" /> </a>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div id=\"quote\">\n");
      out.write("\n");
      out.write("<p class=\"floatmid alignmid\">\n");
      out.write("<br><br>\n");
      out.write("The premier communications solution for discrete users.\n");
      out.write("</p>\n");
      out.write("</div>\n");
      out.write("<div id=\"linkbar\">\n");
      out.write("<ul class=\"linklist\">\n");
      out.write("<li><a href=\"/send.jsp\">Compose</a><br></li>\n");
      out.write("<li><a href=\"/mail.jsp\">Inbox</a><br></li>\n");
      out.write("<li><a href=\"http://wraithbox:80/history.jsp?id=");
out.print(session.getAttribute("user"));
      out.write("\">Login history</a></li>\n");
      out.write("</ul>\n");
      out.write("</div>\n");
      out.write("</div>\n");
      out.write("<div id=\"mainmid\">\n");
      out.write("<center>\n");



String messageid = request.getParameter("messageid");
if(session.getAttribute("user") != null){
	  String user = session.getAttribute("user").toString();
	  if(user.equals("algo")) out.print("Stealth rating: "+getStealth());
	   Class.forName("com.mysql.jdbc.Driver").newInstance();
	     String url = "jdbc:mysql://localhost:3306/wraithlogin";
	     Connection con=DriverManager.getConnection(url, "wraith", "5Z1aZfs%&zaA!6597");
	  
	if(messageid == null){
		String mailtable = "<table id='hor-minimalist-b' >\n<thead>\n<tr>\n<th scope='col'>"
		 +"To</th>\n<th scope='col'>From</th>\n<th scope='col'>Subject</th>\n</tr>\n</thead>\n<tbody>\n";
		out.print(mailtable);
		out.print(getList(user, con));
		out.print("</tbody></table>");
	      } //no timing attacks for YOU, scum!
	else{
	  out.println("<br><br><br><br></center>" + getMessage(messageid, user, con));
	}
}
else
    response.sendRedirect("index.jsp"); 

      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</center>\n");
      out.write("</div>\n");
      out.write("<div id=\"footer\">Copyright 1999-2011 Discrete Industries Ltd and related entities </div>\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
