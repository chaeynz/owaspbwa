<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%
	String type = request.getParameter( "type" );
	if ( type == null ) type = "SafeString";
	String input = request.getParameter( "input" );
	if ( input == null ) input = "type input here";
	
	System.out.println(" >>>>>" );
	byte[] inputBytes=input.getBytes("UTF-8");
	for ( int i = 0; i<inputBytes.length; i++ ) System.out.print (" " + inputBytes[i] );
	System.out.println();
	
	String canonical = "";
	try{
		canonical = ESAPI.encoder().canonicalize(input);
		ESAPI.validator().getValidInput("Swingset Validation Secure Exercise",input,type,200,false);
	} catch( ValidationException e ) {
		input="Validation attack detected";
		request.setAttribute("userMessage", e.getUserMessage() );
		request.setAttribute("logMessage", e.getLogMessage() );
	} catch( IntrusionException ie ) {
		input="double encoding attack detected";
		request.setAttribute("userMessage", ie.getUserMessage() );
		request.setAttribute("logMessage", ie.getLogMessage() );
	}
	
%>

<h2 align="center">Exercise</h2>
<h4>Enter a Type/Regex and Invalid Data</h4>

<form action="main?function=ValidateUserInput&mode=secure" method="POST">
	Type/Regex: <input name="type" value="<%=ESAPI.encoder().encodeForHTMLAttribute(type)%>"><br>
	<textarea style="width:400px; height:150px" name="input"><%=ESAPI.encoder().encodeForHTML(input)%></textarea><br>
	<input type="submit" value="submit">
</form>

<p>Canonical output: <%=ESAPI.encoder().encodeForHTML(canonical) %></p>


<%@include file="footer.jsp" %>
