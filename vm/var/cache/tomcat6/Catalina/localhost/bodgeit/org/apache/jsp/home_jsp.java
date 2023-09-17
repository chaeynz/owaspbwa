package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.math.*;
import java.text.*;
import java.sql.*;

public final class home_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


    private Connection conn = null;

    public void jspInit() {
        try {
            // Get hold of the JDBC driver
            Class.forName("org.hsqldb.jdbcDriver" );
            // Establish a connection to an in memory db
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:SQL", "sa", "");
        } catch (SQLException e) {
            getServletContext().log("Db error: " + e);
        } catch (Exception e) {
            getServletContext().log("System error: " + e);
        }
    }

    public void jspDestroy() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            getServletContext().log("Db error: " + e);
        } catch (Exception e) {
            getServletContext().log("System error: " + e);
        }
    }

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/dbconnection.jspf");
  }

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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write('\r');
      out.write('\n');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/header.jsp", out, false);
      out.write("\n");
      out.write("\n");
      out.write("<h3>Our Best Deals!</h3>\n");

	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.prepareStatement("SELECT COUNT (*) FROM Products");
		rs = stmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		stmt.close();
		out.println("<center><table border=\"1\" class=\"border\" width=\"80%\">");
		out.println("<tr><th>Product</th><th>Type</th><th>Price</th></tr>");
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		for (int i=0; i < 10; i++) {
			stmt = conn.prepareStatement("SELECT * FROM Products, ProductTypes WHERE Products.productid = " + ((int)(Math.random() * count) + 1) + " AND Products.typeid = ProductTypes.typeid");
			rs = stmt.executeQuery();
			if (rs.next()) {
				out.println("<tr>");
				String product = rs.getString("product");
				String type = rs.getString("type");
				BigDecimal price = rs.getBigDecimal("price");
				out.println("<td><a href=\"product.jsp?prodid=" + rs.getInt("productid") + "\">" + 
						product + "</a></td><td>" + type + "</td><td align=\"right\">" + nf.format(price) + "</td>");
				out.println("</tr>");
			}
			stmt.close();
			rs.close();
		}
		out.println("</table></center><br/>");
	} catch (SQLException e) {
		if ("true".equals(request.getParameter("debug"))) {
			out.println("DEBUG System error: " + e + "<br/><br/>");
		} else {
			out.println("System error.");
		}
	}

      out.write('\n');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/footer.jsp", out, false);
      out.write('\n');
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
