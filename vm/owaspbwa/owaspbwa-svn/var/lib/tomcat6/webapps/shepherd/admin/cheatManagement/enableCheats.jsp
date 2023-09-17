<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.io.*,java.net.*,org.owasp.esapi.ESAPI, org.owasp.esapi.Encoder, dbProcs.*, utils.*" errorPage="" %>

<%
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG: enableCheats.jsp *************************");

/**
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
 
if (request.getSession() != null)
{
HttpSession ses = request.getSession();
Getter get = new Getter();
//Getting CSRF Token from client
Cookie tokenCookie = null;
try
{
	tokenCookie = Validate.getToken(request.getCookies());
}
catch(Exception htmlE)
{
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(enableCheats.jsp): tokenCookie Error:" + htmlE.toString(), ses.getAttribute("userName"));
}
// validateAdminSession ensures a valid session, and valid administrator credentials
// Also, if tokenCookie != null, then the page is good to continue loading
// Token is now validated when accessing admin pages to stop attackers causing other users to tigger logs of access attempts
Object tokenParmeter = request.getParameter("csrfToken");
if(Validate.validateAdminSession(ses, tokenCookie, tokenParmeter))
{
	//Logging Username
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Accessed by: " + ses.getAttribute("userName").toString());
	// Getting Session Variables
	//This encoder should escape all output to prevent XSS attacks. This should be performed everywhere for safety
	Encoder encoder = ESAPI.encoder();
	String csrfToken = encoder.encodeForHTMLAttribute(tokenCookie.getValue());
	String userName = encoder.encodeForHTML(ses.getAttribute("userName").toString());
	String userRole = encoder.encodeForHTML(ses.getAttribute("userRole").toString());
	String userId = encoder.encodeForHTML(ses.getAttribute("userStamp").toString());
	String ApplicationRoot = getServletContext().getRealPath("");
%>
	<div id="formDiv" class="post">
		<h1 class="title">Enable Cheat Sheets</h1>
		<div class="entry">
			
				<% if (!CheatSheetStatus.getStatusForAll()) {%>
					<form id="theForm" action="javascript:;">
					<p>Are you sure that you want to enable <a>cheat sheets</a> for all users?</p>
					<div id="badData"></div>
					<input type="hidden" id="csrfToken" value="<%= csrfToken %>"/>
					<div id="cheatSheetTable">
						<table align="center">
							<tr><td colspan="2" align="center">
								<input type="submit" id="submitButton" value="Enable Cheat Sheets For Admins"/>
							</td></tr>
							<tr><td colspan="2" align="center">
								<br>Or<br><br>
								<input type="button" id="openAll" value="Enable Cheat Sheets For All"/>
							</td></tr>
						</table>
					</div>
					<br>
					<div id="loadingDiv" style="display:none;" class="menuButton">Loading...</div>
					<div id="resultDiv" style="display:none;" class="informationBox"></div>
					<div id="badData"></div>
					</form>
					<script>					
					$("#theForm").submit(function(){
						//Get Data
						var theCsrfToken = $('#csrfToken').val();
						//The Ajax Operation
						$("#loadingDiv").show("fast");
						$("#badData").hide("fast");
						$("#resultDiv").hide("fast");
						$("#cheatSheetTable").slideUp("fast", function(){
							var ajaxCall = $.ajax({
								type: "POST",
								url: "enableCheats",
								data: {
									enableForAll: "nope",
									csrfToken: theCsrfToken
								},
								async: false
							});
							$("#loadingDiv").hide("fast", function(){
								if(ajaxCall.status == 200)
								{
									//Now output Result Div and Show
									$("#resultDiv").html(ajaxCall.responseText);
									$("#resultDiv").show("fast");
								}
								else
								{
									$("#badData").html("<div id='errorAlert'><p> Sorry but there was an error: " + ajaxCall.status + " " + ajaxCall.statusText + "</p></div>");
									$("#badData").show("slow");
								}
								$("#cheatSheetTable").slideDown("slow");
							});
						});
					});
					
					$("#openAll").click(function(){
						//Get Data
						var theCsrfToken = $('#csrfToken').val();
						$("#loadingDiv").show("fast");
						$("#badData").hide("fast");
						$("#resultDiv").hide("fast");
						$("#cheatSheetTable").slideUp("fast", function(){
							//The Ajax Operation
							var ajaxCall = $.ajax({
								type: "POST",
								url: "enableCheats",
								data: {
									enableForAll: "true",
									csrfToken: theCsrfToken
								},
								async: false
							});
							$("#loadingDiv").hide("fast", function(){
								if(ajaxCall.status == 200)
								{
									//Now output Result Div and Show
									$("#resultDiv").html(ajaxCall.responseText);
									$("#resultDiv").show("fast");
								}
								else
								{
									$("#badData").html("<div id='errorAlert'><p> Sorry but there was an error: " + ajaxCall.status + " " + ajaxCall.statusText + "</p></div>");
									$("#badData").show("slow");
								}
								$("#cheatSheetTable").slideDown("slow");
							});
						});
					});
					</script>
				<% } else { %>
					<p>Cheat sheets are already enabled!</p>
				<% } %>
			
			<% if(Analytics.googleAnalyticsOn) { %><%= Analytics.googleAnalyticsScript %><% } %>
		</div>
	</div>
	<%
}
else
{
response.sendRedirect("../../loggedOutSheep.html");
}
}
else
{
response.sendRedirect("../../loggedOutSheep.html");
}
%>