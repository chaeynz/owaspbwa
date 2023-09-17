<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<%  String random = ""; String rangeVal = "";
if(request.getAttribute( "randomNum" ) != null){ random = request.getAttribute( "randomNum" ).toString();
}
if(request.getAttribute( "rangeValue" ) != null){ rangeVal = request.getAttribute( "rangeValue" ).toString();
}
%>
<h2>Exercise: Secure Pseudo-Random-Number Generation</h2>
<p>Enter a range value in the textbox and click the button to generate a random number using a more secure pseudo-random-number generator. The random number will be in the range of (0, rangeValue). </p>
 <form method="POST" action="?function=Randomness&solution"> Range Value: 
<input type='text' name='rangeVal' value='<%=rangeVal %>'><br /> Random Number: <%=random %><br /><br /> 
<input type="submit" value="Click to generate a random number"> </form>
<%@include file="footer.jsp" %>  
