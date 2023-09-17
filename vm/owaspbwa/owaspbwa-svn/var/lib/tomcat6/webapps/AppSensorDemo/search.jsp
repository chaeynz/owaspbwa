<%@include file="header.jsp" %>
<%@page import="org.owasp.appsensor.demoapp.Utility"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.owasp.appsensor.demoapp.*"%>
<%@page import="java.util.Iterator;"%>

<b>Search Page</b>


<%
//searchQuery should only be present during post requests
if (!(request.getMethod().equalsIgnoreCase("Post")) && request.getParameterMap().containsKey("searchQuery")){		
	User user = org.owasp.appsensor.demoapp.UserManager.getLoggedInUserObject(request);		
	//@AppSensor Attack Detection - RE3
	//Attacker is sending a GET to a page designed to receive POST	
	new AppSensorIntrusionException(request.getServletPath(), "RE3", user,"User Message RE3"
			,"Attacker is sending a non-Post request ("+request.getMethod()+") with parameter searchQuery");						
}



String searchQuery="";
searchQuery=Utility.safeGetParam("search", request);


//AppSensor Checks on Search Value
ArrayList<String> attackPatterns=new ArrayList<String>();
attackPatterns.add("\"><script>");
attackPatterns.add("script.*document.cookie");
attackPatterns.add("<script>");
attackPatterns.add("<IMG.*SRC.*=.*script");
attackPatterns.add("<iframe>.*</iframe>");

Iterator<String> i=attackPatterns.iterator();
if (null!=searchQuery){
	while(i.hasNext()){
		if (null!=Utility.regexFind(searchQuery, i.next())){
			User user = org.owasp.appsensor.demoapp.UserManager.getLoggedInUserObject(request);	
			//@AppSensor Attack Detection - IE1
			//Attacker is sending a XSS attempt	
			new AppSensorIntrusionException(request.getServletPath(), "IE1", user,"User Message IE1"
					,"Attacker is sending a likely XSS attempt ("+searchQuery+") within parameter searchQuery");
			searchQuery="xxxx";
		}
	}
}
%>
 
<form name="input" action="search.jsp" method="POST">
<table>

<tr>
<td>Search:</td>
<td><input type="text" name="search" value=<%=Utility.safeOut(searchQuery)%>></td>
</tr>
<tr>
<td><input type="submit" value="Submit"></td>
</tr>
</table>
</form>

<%


if (searchQuery !=null && searchQuery.length()>0){
%>
Your results for : <%=Utility.safeOut(searchQuery)%><br>
<a href="#">Result 1</a><br>
<a href="#">Result 2</a><br>
<a href="#">Result 3</a><br>	

<% } %>
 
<%@include file="footer.jsp" %>
