FROM ubuntu:14.10

ENV DEBIAN_FRONTEND=non-interactive

RUN apt-get update && \
    apt-get install -y build-essential wget libapr1-dev libaprutili-dev libpcre3-dev

WORKDIR /tmp

# Install Apache 2.2.14
RUN wget http://archive.apache.org/dist/httpd/httpd-2.2.14.tar.gz && \
    tar -zxf httpd-2.2.14.tar.gz

WORKDIR /tmp/httpd-2.2.14
RUN ./configure && \
    make && \
    make install

# Install MySQL 5.1.41
RUN wget https://downloads.mysql.com/archives/get/p/23/file/mysql-5.1.41-linux-x86_64-glibc23.tar.gz && \
    tar -zxf mysql-5.1.41-linux-x86_64-glibc23.tar.gz -C /usr/local && \
    ln -s /usr/local/mysql-5.1.41-linux-x86_64-glibc23 /usr/local/mysql

# Install Apache Tomcat 6.0.24
RUN wget https://archive.apache.org/dist/tomcat/tomcat-6/v6.0.24/bin/apache-tomcat-6.0.24.tar.gz && \
    tar -zxf apache-tomcat-6.0.24.tar.gz -C /usr/local && \
    ln -s /usr/local/apache-tomcat-6.0.24 /usr/local/tomcat

# Clean Up
WORKDIR /root
RUN rm -rf /tmp/httpd-2.2.14 /tmp/httpd-2.2.14.tar.gz /tmp/mysql-5.1.41-linux-x86_64-glibc23.tar.gz /tmp/apache-tomcat-6.0.24.tar.gz

# Copy application data
COPY ./vm/etc/mysql/ /etc/mysql/
COPY ./vm/owaspbwa/ /owaspbwa
RUN ln -s /owaspbwa/owaspbwa-svn/var/www /var/www
RUN ln -s /owaspbwa/owaspbwa-svn/etc/apache2 /etc/apache2


COPY ./run.sh /run.sh
EXPOSE 80
ENTRYPOINT /run.sh
