<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="servlets.module.challenge.SecurityMisconfigStealTokens, java.sql.*,java.io.*,java.net.*,org.owasp.esapi.ESAPI, org.owasp.esapi.Encoder, dbProcs.*, utils.*" errorPage="" %>

<%
/**
 * Security Misconfiguration Cookie Challenge
 * 
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 * @author Mark Denihan
 */
 String levelName = new String("Security Misconfiguration Cookie Flag Challenge");
 String levelHash = new String("c4285bbc6734a10897d672c1ed3dd9417e0530a4e0186c27699f54637c7fb5d4");
 ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName + " Accessed");
 if (request.getSession() != null)
 {
 	HttpSession ses = request.getSession();
 	//Getting CSRF Token from client
 	Cookie tokenCookie = null;
 	try
 	{
 		tokenCookie = Validate.getToken(request.getCookies());
 	}
 	catch(Exception htmlE)
 	{
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName +".jsp: tokenCookie Error:" + htmlE.toString());
 	}
 	// validateSession ensures a valid session, and valid role credentials
 	// If tokenCookie == null, then the page is not going to continue loading
 	if (Validate.validateSession(ses) && tokenCookie != null)
 	{
 		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName + " has been accessed by " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
 		// Getting Session Variables
		//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
		Encoder encoder = ESAPI.encoder();
		String applicationRoot = getServletContext().getRealPath("");
		String csrfToken = encoder.encodeForHTML(tokenCookie.getValue());
		String userClass = null;
		if(ses.getAttribute("userClass") != null)
		{
			userClass = encoder.encodeForHTML(ses.getAttribute("userClass").toString());
		}
		String userId = encoder.encodeForHTML(ses.getAttribute("userStamp").toString());
		String challengeUrl = request.getRequestURL().toString();
		ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName +".jsp: DEBUG: Challenge URL " + challengeUrl);
		//Changing URL to HTTP
		challengeUrl = challengeUrl.replaceAll("(?i)https", "http");
		//Set User  Cookie
		try
		{
			Cookie userCookie = new Cookie("securityMisconfigLesson", SecurityMisconfigStealTokens.getUserToken(userId, applicationRoot));
	        response.addCookie(userCookie);
		}
		catch(Exception e)
		{
			ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), levelName +".jsp: Error: Could not Get Token: " + e.toString());
		}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Security Shepherd - <%= levelName %></title>
	<link href="../css/lessonCss/theCss.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
	<script type="text/javascript" src="../js/jquery.js"></script>
		<div id="contentDiv">
			<h2 class="title"><%= levelName %></h2>
			<p> 
				To complete this challenge, you must steal the <a>securityMisconfigLesson</a> cookie of another user. The administrators of the sub application have misconfigured the mechanism which enforces cookie security flags. The cookie set in this lesson has not been given the "secure" flag. This means that in any HTTP requests to the lesson will include this cookie. 
				Even though the application (If Shepherd has been correctly configured) redirects to a HTTPs service upon access, the will have been sent across the network in plain text. When players open this lesson, they automatically send a HTTP request thanks to the following &lt;IMG&gt; element. 
				Image elements do not follow the mixed content policy and will send HTTP requests even when loaded inside a HTTPs context. This will not work with an iFrame.
				<br/>
				<br/>
				<img alt="This Image has sent your securityMisconfigLesson across the network in plain text" title="This Image has sent your securityMisconfigLesson across the network in plain text" src="<%= encoder.encodeForHTMLAttribute(challengeUrl) %>">
				<br/>
				<br/>
				If there are no users on your local network, this lesson is <B>very difficult</B> to complete. Please ask your Shepherd Administrator to disable it.
				<br/>
				<br/>
				Once you have stolen another user's <a>securityMisconfigLesson</a> token, click the following button and sub in the other user's token where your own exists.
				<form id="leForm" action="javascript:;">
					<table>
					<tr><td>
						<div id="submitButton"><input type="submit" value="Get Result Key"/></div>
						<p style="display: none;" id="loadingSign">Loading...</p>
					</td></tr>
					</table>
				</form>
				<div id="resultsDiv"></div>
			</p>
		</div>
		<script>
			$("#leForm").submit(function(){
				$("#submitButton").hide("fast");
				$("#loadingSign").show("slow");
				$("#resultsDiv").hide("slow", function(){
					var ajaxCall = $.ajax({
						dataType: "text",
						type: "POST",
						url: "<%= levelHash %>",
						data: {
							csrfToken: "<%= csrfToken %>"
						},
						async: false
					});
					if(ajaxCall.status == 200)
					{
						$("#resultsDiv").html(ajaxCall.responseText);
					}
					else
					{
						$("#resultsDiv").html("<p> An Error Occurred: " + ajaxCall.status + " " + ajaxCall.statusText + "</p>");
					}
					$("#resultsDiv").show("slow", function(){
						$("#loadingSign").hide("fast", function(){
							$("#submitButton").show("slow");
						});
					});
				});
			});
		</script>
		<% if(Analytics.googleAnalyticsOn) { %><%= Analytics.googleAnalyticsScript %><% } %>
</body>
</html>
<%
	}
	else
	{
		response.sendRedirect("../loggedOutSheep.html");
	}
}
else
{
	response.sendRedirect("../loggedOutSheep.html");
}
%>
