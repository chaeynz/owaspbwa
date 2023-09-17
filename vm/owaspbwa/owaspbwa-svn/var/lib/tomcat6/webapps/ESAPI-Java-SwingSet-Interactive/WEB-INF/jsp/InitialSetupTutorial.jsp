<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<div id="navigation">
<a href="main">Home</a> | 
<b><a href="main?function=InitialSetup">Initial Setup Tutorial</a></b>  
</div>
<div id="header"></div>
<p>
<hr>

<h2 align="center">Initial Setup Tutorial</h2>
<h4>1. Download ESAPI</h4>
<p>This version of Swingset has been tested with ESAPI Release candidate 4 which can be downloaded from - <br/><br/>
	<a href="http://code.google.com/p/owasp-esapi-java/downloads/detail?name=ESAPI-2.0-rc4.zip&can=2&q=">http://code.google.com/p/owasp-esapi-java/downloads/detail?name=ESAPI-2.0-rc4.zip&can=2&q=</a>
	<br/><br/>
	</p>
<h4>2. Copy ESAPI and Log4J jar files to WEB-INF/lib directory</h4>
<p>
	From the extracted zip file that you downloaded copy 
	<ul>
		<li>ESAPI-2.0-rc4\ESAPI-2.0-rc4.jar</li>
		<li>ESAPI-2.0-rc4\lib\required\log4j\log4j\1.2.12\log4j-1.2.12.jar</li>
	</ul>
	
	To
	<ul>
		<li><i>SwingSet\WebContent\WEB-INF\lib</i></li>
	</ul>
</p>

<h4>3. Configure 'org.owasp.esapi.resources' directory</h4>
<p>There are a number of esapi properties files that are generally located in a '.esapi' folder.
	This folder is configured by setting 'org.owasp.esapi.resources' as a Java VM Argument.
	In the extracted zip file that you downloaded, a sample .esapi folder is contained in <i>ESAPI-2.0-rc5\configuration\.esapi</i>
</p>
<p>To do this in Tomcat - 
	<ul>
		<li>Open the Server Overview by double clicking on the instance of your server in the 'Server' view.</li>
		<li>Click on 'Open Launch Configuration'</li>
		<li>Click on the 'Arguments' tab.</li>
		<li>In the 'VM Arguments' list, enter the following <br/> <i><b>-Dorg.owasp.esapi.resources="INSERT_PATH__HERE\.esapi"</b></i></li>	
		<li>Click 'Apply' then 'Ok'</li>
	</ul> 
</p>

<h4>4. Replace encryption key</h4>
<p>You MUST replace the ESAPI Encryptor.MasterKey and Encryptor.MasterSalt in ESAPI.properties (located in the .esapi directory configured in the previous step) with ones you personally generate. By default, the ESAPI.properties file has neither of these set and therefore any many encryption related things will fail until you properly set them. Change them now by using:<br/><br/> 
cd "directory containing ESAPI jar"<br/>
java -Dorg.owasp.esapi.resources="configuration\.esapi" -classpath ESAPI-2.0-rc4.jar;lib\required\log4j\log4j\1.2.12\log4j-1.2.12.jar org.owasp.esapi.reference.JavaEncryptor
<!-- For Release Candidate 5
java -Dorg.owasp.esapi.resources="configuration\.esapi" -classpath ESAPI-2.0-rc5.jar;libs\log4j-1.2.12.jar;libs\servlet-api-2.4.jar;libs\commons-fileupload-1.2.jar org.owasp.esapi.reference.crypto.JavaEncryptor<br/>
 -->
</p>
<p>
The final lines of output from this will look something like:<br/>
Copy and paste this into ESAPI.properties<br/><br/>

Encryptor.MasterKey="something here"<br/>
Encryptor.MasterSalt="something here"<br/>
</p>
<p>
Simply take the two generated entries and paste them into your ESAPI.properties, replacing the empty ones already there. These are the unique key and salt for your ESAPI installation.
</p>

<h4></h4>

<h4>5. Configure SSL</h4>
<p>SSL is required for the login functionality of ESAPI. Details of how to set up SSL for tomcat can be found at <br/><br/>
	<a href="http://tomcat.apache.org/tomcat-6.0-doc/ssl-howto.html#Configuration">Set up SSL for tomcat 6.0</a>
<br/><br/>
Make sure your .keystore file is in your User Home directory. 
</p>

<h4>6. How Application is Structured?</h4>
<p>Give brief overview of how application is structured to help initial users.</p>

<%@include file="footer.jsp" %>
