package org.apache.jsp.wraithmail;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.security.SecureRandom;
import java.lang.System;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private long getFails(String user, Connection con) throws Exception{
String query = "select fails from users where user =?";
PreparedStatement hackthis = con.prepareStatement(query);
user = ascii(user);
hackthis.setString(1, user);
ResultSet rs=hackthis.executeQuery();
String fails = "0";
if(rs.next())
    fails = rs.getString(1);
return Long.parseLong(fails);
}

private void setFails(String user, Connection con, long fails) throws Exception
{
PreparedStatement hackthis = con.prepareStatement("update users set fails=? where user =?");
hackthis.setString(1, "" + fails);
hackthis.setString(2, user);
hackthis.execute();
}

private void regFail(String user, Connection con) throws Exception{
    long fails = getFails(user, con);
    if(fails >= 9)
	setFails(user, con, System.currentTimeMillis());
    else
	setFails(user, con, ++fails);
}

public boolean isLocked(String user, Connection con) throws Exception{
long time = getFails(user, con);
boolean locked;
if(time <= 9)
    locked = false;
else{
    if(time > (System.currentTimeMillis()-1800000))
	locked = true;
    else{
	locked = false;
	setFails(user, con, 0);
	}
    }
return locked;
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
      out.write("<p class=\"alignmid\">\n");
      out.write("<br><br>\n");
      out.write("The premier communications solution for discrete users.\n");
      out.write("</p>\n");
      out.write("</div><div id=\"mainmid\">\n");
      out.write('\n');
      out.write('\n');
      out.write('\n');


//if(request.getParameter("login") != null && request.getParameter("login").equals("1")){
if("1".equals(request.getParameter("login"))){
String user=request.getParameter("user");
String pass=request.getParameter("pass");
String gotToken = request.getParameter("token");
if(!gotToken.equals(session.getAttribute("token")) || session.getAttribute("token") == null){
    out.println("<center><font color='red'>Access denied: Invalid token</font></center>");
  }
else{
Boolean correct = false;

     try{
      
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      String url = "jdbc:mysql://localhost:3306/wraithlogin";
      Connection con=DriverManager.getConnection(url, "wraith", "5Z1aZfs%&zaA!6597");
      user = ascii(user);

      if(isLocked(user, con))
        out.print("Account temporarily locked.");
      else{
      String query = "select `user` from users where user =? AND password = ?";
      PreparedStatement hackthis = con.prepareStatement(query);
     
      hackthis.setString(1, user); 
      hackthis.setString(2, pass); 
      ResultSet rs=hackthis.executeQuery();
    
     if(rs.next()){
	setFails(user, con, 0);
       String username=rs.getString(1);
        session.setAttribute("loggedin", "1");
	session.setAttribute("user", user);
	response.sendRedirect("mail.jsp"); 
	out.println("Logged in as " + session.getAttribute("user")); 
      } 
  else{
	  regFail(user, con);
 	  out.println("<center><font color='red'>Access denied</font></center>"); 
	  session.setAttribute("loggedin", "0");
	  session.setAttribute("user", "guest");
	}}

}catch(Exception e1)
{}
}}



      out.write("<div id=\"login2\">\n");
      out.write("<FORM NAME=\"login\" ID=\"login\" METHOD=POST action=\"/index.jsp\" class=\"login\">\n");
      out.write("    <center>   Username: <INPUT id=\"user\" NAME=\"user\" TYPE='text' VALUE=\"\" /> </center><p>\n");
      out.write("   <center>    Password: <INPUT id=\"pass\" NAME=\"pass\" TYPE='password' VALUE=\"\" /> </center><p>\n");
      out.write("\t\t<input name=\"login\" id=\"login\" type=\"hidden\" value=\"1\" />\n");

String token = createToken();
session.setAttribute("token", token);
out.print("<input name='token' id='token' type='hidden' value='" + token + "'/>");

      out.write("\n");
      out.write("   <center> <INPUT name=\"submit\" TYPE=\"submit\" VALUE=\"Login\"/>  </center><br>\n");
      out.write("    </FORM>\n");
      out.write("    </div>\n");
      out.write("<br>\n");
      out.write("\n");
      out.write("</div>\n");
      out.write("<div id=\"footer\">Copyright 1999-2011 Discrete Industries Ltd and related entities </div>\n");
      out.write("\n");
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
