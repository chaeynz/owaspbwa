#!/bin/bash
echo "Compiling and deploying Yazd in /owaspbwa/owaspbwa-svn/misc-src/Yazd_1.0-src/...." 

#echo "Stoping Tomcat Service"
#/usr/sbin/service tomcat6 stop

cd /owaspbwa/owaspbwa-svn/misc-src/Yazd_1.0-src/build
export JAVA_HOME=/usr/lib/jvm/java-6-openjdk ; ./ant
cd - # return to previous directory

# fix file permissions and restart services
#echo ".: Fixing Tomcat Permissions :."
#chown -R tomcat6:tomcat6 /owaspbwa/owaspbwa-svn/var/lib/tomcat6/webapps
#/usr/sbin/service tomcat6 start

