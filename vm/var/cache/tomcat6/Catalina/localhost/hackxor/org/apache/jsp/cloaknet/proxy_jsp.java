package org.apache.jsp.cloaknet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.security.SecureRandom;

public final class proxy_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


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


private String makeSafe(String input){
input = input.replace("'", "''");
input = input.replace("\\", "\\\\");//"fix syntax  highlighting
input = input.toLowerCase().replace("union", "");
input = input.toLowerCase().replace("select", "");
return input;
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
      out.write("<title>Cloak</title>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"cloaknet.css\" />\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("<div id=\"wrapper\">\n");
      out.write("<div id=\"header\">\n");
      out.write("<a href=\"index.jsp\"><img src=\"/logo.png\" alt=\"Thursday\" class=\"logo3\" width=\"468\" height=\"60\" /> </a>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div id=\"quote\">\n");
      out.write("\n");
      out.write("<p class=\"alignmid\">\n");
      out.write("<br><br>\n");
      out.write("The premier cloaking solution for discrete users.\n");
      out.write("</p>\n");
      out.write("</div>\n");
      out.write("<div id=\"mainmid\">\n");
      out.write('\n');
      out.write('\n');
      out.write('\n');


//
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
// user: asd 
//password: asd' union select id,password from users-- 
     try{
     Class.forName("com.mysql.jdbc.Driver").newInstance();
     String url = "jdbc:mysql://localhost:3306/PROXY";
     Connection con=DriverManager.getConnection(url, "webmaster", "disCON1991");
     Statement st=con.createStatement();
     if(user.toLowerCase().matches(".*f.*i.*l.*e.*"))
	    out.print("Attack detected");
     ResultSet rs=st.executeQuery("select id, user from users where user = '"+makeSafe(user)+"' and password = '" + makeSafe(pass) + "'");
    
     if(rs.next()){
       String userid=rs.getString(1);
	user=rs.getString(2);
	session.setAttribute("user", user);
        session.setAttribute("loggedin", "1");
	session.setAttribute("userid", userid);
	response.addHeader("Set-Cookie", "userid=" + userid);
	response.sendRedirect("proxypanel.jsp"); 
	out.println("Logged in as " + session.getAttribute("user")); 
      } 
  else{
 	  out.println("<center><font color='red'>Access denied</font></center>"); 
	  session.setAttribute("loggedin", "0");
	  session.setAttribute("user", "guest");
	}

}catch(Exception e1)
{out.println(e1);}
}}



      out.write("\n");
      out.write("<FORM NAME=\"login\" ID=\"login\" METHOD=\"POST\" action=\"/proxy.jsp\" >\n");
      out.write("<p>\n");
      out.write("    <center> Username: <INPUT id=\"user\" NAME=\"user\" TYPE='text' VALUE=\"\"> </center><p>\n");
      out.write("   <center>    Password: <INPUT id=\"pass\" NAME=\"pass\" TYPE='password' VALUE=\"\" > </center><p>\n");
      out.write("\t\t<input name=\"login\" id=\"login\" type=\"hidden\" value=\"1\">\n");

String token = createToken();
session.setAttribute("token", token);
out.print("<input name='token' id='token' type='hidden' value='" + token + "'>");

      out.write("\n");
      out.write("   <center> <INPUT name=\"asd\" TYPE=\"submit\" VALUE=\"Login\">  </center><br>\n");
      out.write("    </FORM>\n");
      out.write("<br>\n");
      out.write("\n");
      out.write("   <center> <div onclick=\"alert('Try our demo account with username:demo password:demo')\">No account? </div></center>\n");
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
