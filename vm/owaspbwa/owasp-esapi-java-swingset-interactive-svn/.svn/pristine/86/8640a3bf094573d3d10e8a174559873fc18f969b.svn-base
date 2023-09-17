OWASP ESAPI Swingset Interactive Installation Instructions

In this download, encryption keys have been included in the esapi.properties file and SSL is already setup for tomcat.
Application was tested with Windows 7, Eclipse Helios (32 bit), JRE 6 (32 bit) and Firefox 4.

    Setup directory locations:
        Unzip the downloaded Swingset Interactive bundle
        Move the .keystore file and the .esapi directory to your Windows User Home directory (located under C:\Users\{your_account_name}).
        Move the apache-tomcat-6.0.24 and SwingSet directories to locations of your choice (e.g. your eclipse workspace).
    Import the SwingSet project into Eclipse
        File -> Import... -> General -> Existing Project into Workspace, then, under "Select root directory:", browse to the location of your SwingSet directory, click o.k., (don't check the "Copy projects into workspace" checkbox) and then click Finish.
    Eclipse Tomcat server configuration:
        Window -> Show View -> Servers
        right-click on an empty space in the Servers view -> New -> Sever
        Under Apache, select a "Tomcat v6.0 Server" -> Next -> Browse to the newly moved apache-tomcat-6.0.24 folder, choose Workbench default JRE under "JRE" -> Next -> Add the "Available" SwingSet project to the "Configured" ressources on the right -> click Finish
        "Clean..." the SwingSet project. (Select the "SwingSet" project in the Project Explorer, then under the "Project" menu, select "Clean...".)
        If you have errors related to javax.servlet.* import statements in your code:
            go to the Java Build Path for the SwingSet Project (right-click on SwingSet Project -> Build Path -> Configure Build Path...), select the "Apache Tomcat v6.0 ..." entry, click on Edit... . You should have an "Apache Tomcat v6.0 runtime entry in the "Edit Library" window. Select it and click Finish. The errors should go away.
        Start the Server. Chek the Console if you want to make sure that everything started up fine. The last line should read "INFO: Server startup in {xxx} ms."
            Some times performing a "clean..." on the server itself is necessary to make it start up correctly using the ESAPI project.
    Browse to https://localhost:8443/SwingSet/main (preferably with a Firefox Browser)
        If you get a warning about an untrusted SSL certificate, accept anyway and proceed to the page.


Setting it all up manually:
If you want to set thing up manually, you will need to

    Configure SSL.
        SSL is required for the login functionality of ESAPI. Details of how to set up SSL for tomcat can be found at Set up SSL for tomcat 6.0
    Replace your encryption keys

        You should replace the ESAPI Encryptor.MasterKey and Encryptor.MasterSalt in ESAPI.properties (located in the .esapi directory which should now be located in your Windows User Home directory) with ones you personally generate. By default, the ESAPI.properties file has neither of these set and therefore many encryption related things will fail until you properly set them. Change them now in a Windows cmd terminal by using (be sure that "java" is in your Windows Environment PATH Variable, c.f. http://www.java.com/en/download/help/path.xml):
            cd "'ESAPI-2.0-rc4' directory contained in the original SwingSet download"
            java -Dorg.owasp.esapi.resources="configuration\.esapi" -classpath ESAPI-2.0-rc4.jar;lib\required\log4j\log4j\1.2.12\log4j-1.2.12.jar org.owasp.esapi.reference.JavaEncryptor
        The final lines of output from this will look something like:
            Copy and paste this into ESAPI.properties
            Encryptor.MasterKey="something here"
            Encryptor.MasterSalt="something here"
        Simply take the two generated entries and paste them into your ESAPI.properties, replacing the empty ones already there. These are the unique key and salt for your ESAPI installation.
