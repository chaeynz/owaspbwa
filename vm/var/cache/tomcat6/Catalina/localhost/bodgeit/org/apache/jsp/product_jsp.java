package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.math.*;
import java.text.*;
import java.sql.*;

public final class product_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    function decQuantity () {\n");
      out.write("        if (val < 1) {\n");
      out.write("            val = 1;\n");
      out.write("        }\n");
      out.write("        if (q != null) {\n");
      out.write("            var val = --q.value;\n");
      out.write("            q.value = val;\n");
      out.write("        }\n");
      out.write("        var q = document.getElementById('quantity');\n");
      out.write("    }\n");
      out.write("    function incQuantity () {\n");
      out.write("\tvar q = document.getElementById('quantity');\n");
      out.write("\tif (q != null) {\n");
      out.write("\t\tvar val = ++q.value;\n");
      out.write("\t\tif (val > 12) {\n");
      out.write("\t\t\tval = 12;\n");
      out.write("\t\t}\n");
      out.write("\t\tq.value = val;\n");
      out.write("\t}\n");
      out.write("}\n");
      out.write("</script>\n");
      out.write("\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/header.jsp", out, false);
      out.write('\n');
      out.write('\n');

	String productId = request.getParameter("prodid");
	String typeId = request.getParameter("typeid");
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		if (typeId != null) {
			stmt = conn.prepareStatement("SELECT * FROM Products, ProductTypes where typeid=" + Integer.parseInt(typeId) +
					" AND Products.typeid = ProductTypes.typeid");
			rs = stmt.executeQuery();
			out.println("<h3>Products</h3><center><table border=\"1\" width=\"80%\" class=\"border\">");
			out.println("<tr><th>Product</th><th>Type</th><th>Price</th></tr>");
			while (rs.next()) {
				out.println("<tr>");
				String product = rs.getString("product");
				BigDecimal price = rs.getBigDecimal("price");
				out.println("<td><a href=\"product.jsp?prodid=" + rs.getInt("productid") + "\">" + 
						product + "</a><td>" + rs.getString("type")+ 
						"</td></td><td align=\"right\">" + nf.format(price) + "</td>");
				out.println("</tr>");
			}
			out.println("</table></center><br/>");
			
		} else if (productId != null) {
			stmt = conn.prepareStatement("SELECT * FROM Products, ProductTypes where productid=" + Integer.parseInt(productId) +
					" AND Products.typeid = ProductTypes.typeid");
			rs = stmt.executeQuery();
			out.println("<h3>Product</h3><form action=\"basket.jsp\" method=\"post\">");
			out.println("<center><table class=\"border\" width=\"80%\">");
			out.println("<tr><th>Product</th><th>Type</th><th>Price</th><th>Quantity</th><th>Buy</th></tr>");
			if (rs.next()) {
				int id = rs.getInt("productid");
				String product = rs.getString("product");
				BigDecimal price = rs.getBigDecimal("price");
				out.println("<input type=\"hidden\" name=\"productid\" value=\"" + id + "\"/>");
				out.println("<input type=\"hidden\" name=\"price\" value=\"" + price + "\"/>");
				out.println("<tr>");
				out.println("<td>" + product + "</td>");
				out.println("<td><a href=\"product.jsp?typeid=" + rs.getInt("typeid") + "\">" + rs.getString("type") + "</a></td>");
				out.println("<td align=\"right\">" + nf.format(price) + "</td>");
				out.println("<td align=\"center\">&nbsp;<a href=\"#\" onclick=\"decQuantity();\"><img src=\"images/130.png\" alt=\"Decrease quantity in basket\" border=\"0\"></a>&nbsp;<input id=\"quantity\" name=\"quantity\" value=\"1\" maxlength=\"2\" size = \"2\" style=\"text-align: right\" READONLY />&nbsp;<a href=\"#\" onclick=\"incQuantity();\"><img src=\"images/129.png\" alt=\"Increase quantity in basket\" border=\"0\"></a>&nbsp;");
				out.println("<td align=\"center\"><input type=\"submit\" id=\"submit\" value=\"Add to Basket\"/></td>");
				out.println("</tr>");
				out.println("</table></center>");
				out.println("</form>");
				out.println("<h3>Description</h3>");
				out.println(rs.getString("desc"));
				out.println("<br/><br/>");
			} else {
				out.println("</tr>");
				out.println("</table></center>");
				out.println("</form>");
				
				out.println("<br/> Product not found.");
			}
			
		} else {

		}
	} catch (NumberFormatException e) {
		out.println("<br/> Product not found.");

	} catch (SQLException e) {
		if ("true".equals(request.getParameter("debug"))) {
			conn.createStatement().execute("UPDATE Score SET status = 1 WHERE task = 'HIDDEN_DEBUG'");
			out.println("DEBUG System error: " + e + "<br/><br/>");
		} else {
			out.println("System error.");
		}
	} finally {
		if (stmt != null) {
			stmt.close();
		}
		if (rs != null) {
			rs.close();
		}
	}

      out.write('\n');
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
