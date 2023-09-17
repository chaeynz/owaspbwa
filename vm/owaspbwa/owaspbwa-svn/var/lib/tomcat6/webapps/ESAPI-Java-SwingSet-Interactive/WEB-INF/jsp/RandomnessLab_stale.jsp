<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Random" %>
<%@page import="java.util.Date" %>
<%@include file="header.jsp" %>

<% 
Random ranGen = new Random();
//int randomNum = 0;
int[] randomArr = new int[100];
			
try {
	//String rangeValueString = request.getParameter("rangeVal");
	//int rangeValue = Integer.parseInt(rangeValueString);
	ranGen.setSeed((new Date()).getTime());
	for(int x = 0; x<1000000; x++)
		randomArr[ranGen.nextInt(100)] += 1;
	//request.setAttribute("randomNum", randomNum);
	//request.setAttribute("rangeValue", rangeValue);
	}

catch (Exception e){
	request.setAttribute("randomNum", "Please enter an integer between 0 and 2147483647 in the box above.");	
}
	//String random = request.getAttribute( "randomNum" ).toString();
	//String rangeVal = "";
	//if(request.getAttribute( "rangeValue" ) != null){
		//rangeVal = request.getAttribute( "rangeValue" ).toString();
	//}
	
%>
<h2>Insecure Pseudo-Random-Number Generation</h2>
<p>Click Submit to generate 1,000,000 statistical pseudo-random-numbers between 0 and 100.  The counter below displays how many times each number was generated.</p>  
<form method="POST" action="?function=Randomness&lab">
<!--  Range Value: <input type='text' name='rangeVal' value=''><br /> 
Random Number: <br /><br /> -->
<input type="submit" value="Click to generate a random number">
</form>

<table width="80%" border="1px solid black">
<%
for(int y = 0; y < 100; y++)
	out.println("<tr><td>" + y +"</td><td>" + randomArr[y] + "</td></tr>");
%>
</table>
<%@include file="footer.jsp" %>
