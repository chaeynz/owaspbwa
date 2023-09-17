
<hr>
<%
	String message = (String)request.getAttribute( "message" );
	if ( request.getAttribute("userMessage") != null || request.getAttribute("logMessage") != null) {
%>
	<p>User Message: <font color="red"><%=org.owasp.esapi.ESAPI.encoder().encodeForHTML(request.getAttribute("userMessage").toString()) %></font></p>
	<p>Log Message: <font color="red"><%=org.owasp.esapi.ESAPI.encoder().encodeForHTML(request.getAttribute("logMessage").toString()) %></font></p><hr>
<%
	}
%>
<p><center><a href="http://www.owasp.org/index.php/ESAPI">OWASP Enterprise Security API Project</a> (c) 2008</center></p>
<!--  <span id="copyright">Design by <a href="http://www.sitecreative.net" target="_blank" title="Opens link to SiteCreative.net in a New Window">SiteCreative</a></span> -->
	</div> <!-- end holder div -->
</div> <!-- end container div -->
</body>
</html>