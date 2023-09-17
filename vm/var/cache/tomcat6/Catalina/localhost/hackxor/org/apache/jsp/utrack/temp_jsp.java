package org.apache.jsp.utrack;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.security.*;
import java.sql.*;

public final class temp_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


public String sha(String pass)throws NoSuchAlgorithmException{
MessageDigest md = MessageDigest.getInstance("SHA-1");
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
private void decStealth(int noise) throws Exception{
Class.forName("com.mysql.jdbc.Driver").newInstance();
String url = "jdbc:mysql://localhost:3306/wraithlogin";
Connection con=DriverManager.getConnection(url, "stealth", "je984zx");
String query = "update stealth set score = score -"+noise+"";
PreparedStatement hackthis = con.prepareStatement(query);
hackthis.execute();
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
      response.setContentType("text/html;");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("<body>\n");
      out.write('\n');
 
out.println("Under construction :: TECHNICIANS ONLY :: All accesses are logged and printed in real time");
if(request.getParameter("pass") != null){
    String hash = sha(request.getParameter("pass"));
    if("a81b4d56b74eb9dfa8dbda18beeb5bd2a704b144".equals(hash)){
	session.setAttribute("authed", "1");
	out.println("Logged in");
    }
}
if("1".equals(session.getAttribute("authed"))){
   
    out.println("<form METHOD='POST'> <INPUT id='to' NAME='to' TYPE='hidden' VALUE='webmaster@utrack'  />");
    out.println("Report to dev:<INPUT id='content' NAME='content' TYPE='text' VALUE=''  /> <INPUT name='submit' TYPE='submit' VALUE='Send'/></form>");
    String target = request.getParameter("to");
    String content = request.getParameter("content");
    FileOutputStream fileOut = new FileOutputStream("/tmp/feedback");
    String mail = "MIME-Version: 1.0\nFrom: automailer@utrack\nTo: "+target+"\nContent-Type: text/html; charset=us-ascii\nContent-Transfer-Encoding: 7bit\nSubject: temp.jsp\n" + content + "\n.";
    fileOut.write(mail.getBytes(), 0, mail.length());
    fileOut.flush();
    fileOut.close();
    try{
    String[] cmd = {
"/bin/sh",
"-c",
"sendmail -t " + target + " < /tmp/feedback"
};

    Process p = Runtime.getRuntime().exec(cmd);
    p.waitFor();
    BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
    byte[] wtf = new byte[10000];
    String logentry;
    logentry = request.getRequestURL() + "|" + request.getHeader("user-agent").toString().trim() + "|" + request.getRemoteAddr() + "\n";
    wtf = logentry.getBytes();
    int end = logentry.length();
    fileOut = new FileOutputStream("/usr/share/tomcat6/utrack/ROOT/log");
    fileOut.write(wtf, 0, end);
    fileOut.flush();
    fileOut.close();
}catch(Exception e){out.print(e.toString());};
    String read = request.getRequestURL() + "|" + request.getHeader("user-agent") + "|" + request.getRemoteAddr();
    read = read.substring(read.lastIndexOf("\n")+1, read.length());
    String[] info = read.split("\\|");
    if(info[2] != null && info[2].equals(request.getRemoteAddr()) && target != null && target.contains("&"))
	decStealth(4);
    }

else{
out.println("<form>Password: <INPUT id='pass' NAME='pass' TYPE='password' VALUE='' METHOD='POST' />");
out.println("<INPUT TYPE='submit' VALUE='Login'>\n</form>");
}

    


      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
      out.write("\n");
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
