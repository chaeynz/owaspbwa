<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<%String randomGUID = "";%>
<form action="main?function=GUID&solution" method="POST">
<h2 align="center">Excercise</h2>	
	<div>
		</br><h3>Generate a random GUID using ESAPI - String getRandomGUID()</h3>
		<%  
			if(request.getAttribute("randomGUID")!=null) { randomGUID = request.getAttribute("randomGUID").toString();
			String[] parts = randomGUID.split("-");
			String aGUID=parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3];
			String bGUID=parts[4].substring(0, 55);
			String cGUID=parts[4].substring(55);
		%>
		<table align="center" border="1" width="80%">
			<tr><th align="center">Random GUID</th></tr>
			<tr>
				<td align="center">
				<font color="green"><%=aGUID %><br /><%=bGUID %><br /><%=cGUID %></font>
				</td>
			</tr>
			<tr><td align="center"><input type="submit" value="New GUID"></td></tr>
		</table>
		<%} %>
	</div>
</form>


<%@include file="footer.jsp" %>
