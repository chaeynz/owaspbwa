<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<%@page import="java.util.*"%>

<div id="navigation">
<a href="main">Home</a> | 
<a href="main?function=AccessControl">Tutorial</a> | 
<a href="main?function=AccessControl&lab">Lab : Forced Browsing</a>| 
<a href="main?function=AccessControl&solution">Solution</a> |
<a href="main?function=ObjectReference&lab">Lab : Direct Object Reference</a> | 
<b><a href="main?function=ObjectReference&solution">Solution</a></b>
</div>
<div id="header"></div>
<p>
<hr>

<%
String href="?function=ObjectReference&solution&showItem=";
String output = "Click a link or change the URL to change this message.";

String dir0 = session.getAttribute("ind0").toString();
String dir1 = session.getAttribute("ind1").toString();
String dir2 = session.getAttribute("ind2").toString();
String dir3 = session.getAttribute("ind3").toString();
String dir4 = session.getAttribute("ind4").toString();
String dir5 = session.getAttribute("ind5").toString();
String dir6 = session.getAttribute("ind6").toString();
%>
<h2>Solution: Insecure Object Reference</h2>
<h4>JSP Location: WebContent\WEB-INF\jsp\ObjectReferenceSolution.jsp</h4>
<p>Below is a list of users which have been put in session attributes in the following Action Class:<br/>
	<b><i>Java Resources:src/org.owasp.esapi.swingset.actions.ObjectReferenceSolution.java</i></b><br/><br/>
   The references are created using org.owasp.esapi.reference.RandomAccessReferenceMap. 
	</p>

<table width="100%" border="1">
<tr><th width="50%">Links with indirect references</th><th>The direct reference</th></tr>
<tr><td><a href="<%=href + session.getAttribute("ind0") %>"><%=session.getAttribute("ind0") %></a></td><td><%=session.getAttribute(dir0) %></td></tr>
<tr><td><a href="<%=href + session.getAttribute("ind1") %>"><%=session.getAttribute("ind1") %></a></td><td><%=session.getAttribute(dir1) %></td></tr>
<tr><td><a href="<%=href + session.getAttribute("ind2") %>"><%=session.getAttribute("ind2") %></a></td><td><%=session.getAttribute(dir2) %></td></tr>
<tr><td><a href="<%=href + session.getAttribute("ind3") %>"><%=session.getAttribute("ind3") %></a></td><td><%=session.getAttribute(dir3) %></td></tr>
<tr><td><a href="<%=href + session.getAttribute("ind4") %>"><%=session.getAttribute("ind4") %></a></td><td><%=session.getAttribute(dir4) %></td></tr>
<tr><td><a href="<%=href + session.getAttribute("ind5") %>"><%=session.getAttribute("ind5") %></a></td><td><%=session.getAttribute(dir5) %></td></tr>
<tr><td><a href="<%=href + session.getAttribute("ind6") %>"><%=session.getAttribute("ind6") %></a></td><td><%=session.getAttribute(dir6) %></td></tr>
</table>
<%

if( request.getAttribute("output") != null)
	output = request.getAttribute("output").toString();

if( request.getParameter("showItem") != null ){
	String show = request.getParameter("showItem");
	if(session.getAttribute(show) != null){
		output = session.getAttribute(show).toString();
	}
	else 
		output = "<p style=\"color: red; display:inline\">Invalid item.</p>  See the value? :)";
}

%>
Message: <%=output %>
<br /><br />
Click <a href="?function=ObjectReference&solution&refresh">here</a> to get new object mapping.

<%@include file="footer.jsp" %>
