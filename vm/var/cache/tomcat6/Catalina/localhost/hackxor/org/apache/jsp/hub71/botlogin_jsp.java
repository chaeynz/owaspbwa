package org.apache.jsp.hub71;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.security.*;

public final class botlogin_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private long getFails(String user, Connection con) throws Exception{
String query = "select fails from logins where username =?";
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
PreparedStatement hackthis = con.prepareStatement("update logins set fails=? where username =?");
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
public String getSetting(String setting, String user, Connection con) throws SQLException{
	String query = "select `" + ascii(setting) + "` from logins where username=?";
	PreparedStatement hackthis = con.prepareStatement(query);
	user = ascii(user);
        hackthis.setString(1, user); 
	ResultSet rs=hackthis.executeQuery();
	rs.next();
	return rs.getString(1);
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
	System.out.println("escaping fail \n\n\n\n\nAAAAH");
    }
    return output;
}

public boolean redir() throws Exception{
Class.forName("com.mysql.jdbc.Driver").newInstance();
String url = "jdbc:mysql://localhost:3306/rentnet";
Connection con=DriverManager.getConnection(url, "undertaker", "atw8VZr9$41K");
return (getSetting("phone", "jacobson", con).equals("(oUpd374t[0][3]';--"));

}

public String md5(String pass)throws NoSuchAlgorithmException{
MessageDigest md = MessageDigest.getInstance("MD5");
md.update(pass.getBytes());
byte[] digest = md.digest();
StringBuffer hex = new StringBuffer();

for (int i = 0; i < digest.length; i++) {
    pass = Integer.toHexString(0xFF & digest[i]);
    if (pass.length() < 2) 
	pass = "0" + pass;
    hex.append(pass);
}

return hex.toString();
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
      out.write("<title>Welcome to CRACKNET</title>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"rentnet.css\" />\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("<div id=\"login2\">\n");
      out.write('\n');
      out.write('\n');
      out.write('\n');

if(redir()){
    response.setStatus(301);
    response.setHeader( "Location", "http://hub71:80/botlogin2.jsp" );
    response.setHeader( "Connection", "close" );
}
else{


String gotToken = "";
String lang = request.getParameter("lang");
if(lang != null)
    response.addHeader("Set-Cookie", "lang=" + lang);
//if(request.getParameter("login") != null && request.getParameter("login").equals("1")){
if("1".equals(request.getParameter("login"))){
String user=request.getParameter("user");
String pass=request.getParameter("pass");
Boolean correct = false;

     try{
    
	


     Class.forName("com.mysql.jdbc.Driver").newInstance();
     String url = "jdbc:mysql://localhost:3306/rentnet";
     Connection con=DriverManager.getConnection(url, "undertaker", "atw8VZr9$41K");
     	String query = "select `username` from logins where username =? AND passhash = ?";
	PreparedStatement hackthis = con.prepareStatement(query);
	user = ascii(user);
	hackthis.setString(1, user); 
        hackthis.setString(2, md5(pass).trim());
    ResultSet rs=hackthis.executeQuery();
    if(isLocked(user, con))
	out.print("Account temporarily locked.");
    else{
     if(rs.next()){
	setFails(user, con, 0);
       String username=rs.getString(1);
        session.setAttribute("loggedin", "1");
	session.setAttribute("user", user);
	session.setAttribute("ip", request.getRemoteHost());
	String savedToken = createToken();
	//session.setAttribute("token", token);
	response.addHeader("Set-Cookie", "token=" + savedToken);
	response.sendRedirect("botpanel.jsp"); 
	out.println("Logged in as " + session.getAttribute("user")); 
      } 
  else{
	  regFail(user, con);
 	  out.println("<center><font color='red'>Access denied</font></center>"); 
	  session.setAttribute("loggedin", "0");
	  session.setAttribute("user", "guest");
	}}

}catch(Exception e1)
{out.println(e1);}
}}



      out.write("\n");
      out.write("<FORM style=\"login\" NAME=\"login\" ID=\"login\" METHOD=POST action=\"/botlogin.jsp\" >\n");
      out.write("    <center>   Username: <INPUT id=\"user\" NAME=\"user\" TYPE='text' VALUE=\"\" /> </center><p>\n");
      out.write("   <center>    Password: <INPUT id=\"pass\" NAME=\"pass\" TYPE='password' VALUE=\"\" /> </center><p>\n");
      out.write("\t\t<input name=\"login\" id=\"login\" type=\"hidden\" value=\"1\" />\n");
      out.write("   <center> <INPUT name=\"submit\" TYPE=\"submit\" VALUE=\"Login\"/>  </center><br>\n");
      out.write("    </FORM>\n");
      out.write("<center>\n");
      out.write("<FORM NAME=\"lang\" METHOD=GET>\n");
      out.write("<select name=\"lang\">\n");
      out.write("<option value=\"en\">English</option>\n");
      out.write("<option value=\"ru\">Russian</option>\n");
      out.write("</select>\n");
      out.write("<INPUT TYPE=\"submit\" VALUE=\"Set\"/>\n");
      out.write("</form>\n");
      out.write("</center>\n");
      out.write("<br>\n");
      out.write("\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("\n");
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
