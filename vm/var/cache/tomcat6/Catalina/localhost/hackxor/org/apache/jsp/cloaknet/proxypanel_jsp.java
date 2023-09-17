package org.apache.jsp.cloaknet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.security.SecureRandom;

public final class proxypanel_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private String makeSafe(String input){
input = input.replace("'", "''");
input = input.replace("\\", "\\\\");//"fix syntax  highlighting
input = input.toLowerCase().replace("union", "");
input = input.toLowerCase().replace("select", "");
return input;
}

private String getUID(Cookie[] cookies){
String userid = "";
	    if(cookies != null){
		for(int i = 0; i < cookies.length; i++){
				if(cookies[i].getName().equals("userid")){
				    userid = cookies[i].getValue();
				    }
				}
	    }
return userid;
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


public String getList(String userid, Connection con) throws SQLException{
	String message = "";
	Statement st=con.createStatement();//badly hide logs
	ResultSet rs=st.executeQuery("select source, target, timestamp from logs where userid = "+makeSafe(userid)+" ");
	while(rs.next()){
	    message = message + "<tr>"; 
	    message = message + "<td> " + rs.getString(1) + "</a></td>";
	    message = message + "<td> " + rs.getString(2) + "</a></td>";
	    message = message + "<td> " + rs.getString(3) + "</a></td>";
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
      out.write("<title>Cloak Control Panel</title>\n");
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
      out.write("<p class=\"floatmid alignmid\">\n");
      out.write("<br><br>\n");
      out.write("The premier cloaking solution for discrete users.\n");
      out.write("</p>\n");
      out.write("</div>\n");
      out.write("<center>\n");
      out.write("<div id=\"mainmid\">\n");
      out.write("<a href=\"/logout.jsp\">Logout</a>\n");



String messageid = request.getParameter("messageid");
if(session.getAttribute("user") != null){
	   out.print("<br><br><br>Welcome " + session.getAttribute("user"));
	  String userid = getUID(request.getCookies());
	    Class.forName("com.mysql.jdbc.Driver").newInstance();
	     String url = "jdbc:mysql://localhost:3306/PROXY";
	    Connection con=DriverManager.getConnection(url, "webmaster", "disCON1991");
	  

		String mailtable = "<table id='hor-minimalist-b' summary='Employee Pay Sheet'>\n<thead>\n<tr>\n<th scope='col'>"
		 +"Source</th>\n<th scope='col'>Target</th>\n<th scope='col'>Date</th>\n</tr>\n</thead>\n<tbody>\n";
		out.print(mailtable);
		out.print(getList(userid, con));
		out.print("</tbody></table>");

}

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
