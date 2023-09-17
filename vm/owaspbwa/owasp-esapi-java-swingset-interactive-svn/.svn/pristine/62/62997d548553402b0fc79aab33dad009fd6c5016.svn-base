<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 10)%>
<div id="header"></div>
<p>
<hr>

<h2 align="center">Tutorial</h2>

<p>A SQL injection attack consists of insertion or "injection" of a SQL query via the input 
data from the client to the application. A successful SQL injection exploit can read sensitive 
data from the database, modify database data (Insert/Update/Delete), execute administration 
operations on the database (such as shutdown the DBMS), recover the content of a given file 
present on the DBMS file system and in some cases issue commands to the operating system.</p>

<p>The following query accepts a user input to select the details of a specific product. If the 
user enters a product name like '; DELETE FROM products; -- two statements will be executed.</p>

<p class="newsItem">
<code>
"SELECT * FROM products WHERE name = '" + request.getParameter("product") + "'"<br />
<br />
// product = '; DELETE FROM products; --<br />
1. SELECT * FROM products WHERE name = '';<br />
2. DELETE FROM products; --'
</code>
</p>

<p>The single quotation mark closes the name parameter string and the semicolon terminates the end 
of the SELECT command which can be followed by a new command. The two hyphen at the end of the command 
comments out the rest of the line. So this whole statement will return all products with no name and than 
deletes all entries of the table. In general each other command could be injected instead of the DELETE 
depending on the rights the current user has on the database.</p>

<p>The best way to prevent SQL Injections is to use parameterized statements. Therefore JDBC 
offers PreparedStatements where the parameters are separated from the statement by 
using question marks as a placeholder for them. To set the values you just have to call the specific 
set method like setString() or setLong().</p>

<p class="newsItem">
<code>
PreparedStatement stmt = con.prepareStatement("SELECT * FROM products WHERE name = ?");<br />
stmt.setString(1, request.getParameter("product"));
</code>
</p>

<p>If you do not have the possibility to use prepared statements ESAPI offers a method to encode 
a string for the use within a SQL statement but even OWASP considers this method as a weaker alternative 
which should only be used if it is not possible to use prepared statements.</p>

<p class="newsItem">
<code>
Codec mySqlCodec = new MySQLCodec(MySQLCodec.Mode.ANSI);<br />
String product = ESAPI.encoder().encodeForSQL(mySqlCodec, request.getParameter("product"));<br />
String query = "SELECT * FROM products WHERE name = '" + product + "'";
</code>
</p>

<ul>
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/Encoder.html#encodeForSQL(org.owasp.esapi.codecs.Codec, java.lang.String)">java.lang.String encodeForSQL(Codec codec, java.lang.String input)</a></b> Encode input for use in a SQL query, according to the selected codec (appropriate codecs include the MySQLCodec and OracleCodec).</li>
</ul>

<%@include file="footer.jsp" %>
