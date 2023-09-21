FROM ubuntu:14.10

ENV DEBIAN_FRONTEND=non-interactive

RUN sed -i 's/archive.ubuntu.com/old-releases.ubuntu.com/g' /etc/apt/sources.list

RUN apt-get update
RUN apt-get install -y build-essential
RUN apt-get install -y wget

# Install Apache 2.2.14
WORKDIR /tmp
RUN wget http://archive.apache.org/dist/httpd/httpd-2.2.14.tar.gz && \
    tar -zxf httpd-2.2.14.tar.gz

WORKDIR /tmp/httpd-2.2.14
RUN ./configure && \
    make && \
    make install

# Install MySQL 5.1.41
WORKDIR /tmp
RUN wget https://downloads.mysql.com/archives/get/p/23/file/mysql-5.1.41-linux-x86_64-glibc23.tar.gz && \
    tar -zxf mysql-5.1.41-linux-x86_64-glibc23.tar.gz -C /usr/local && \
    ln -s /usr/local/mysql-5.1.41-linux-x86_64-glibc23 /usr/local/mysql

# Install Apache Tomcat 6.0.24
WORKDIR /tmp
RUN wget https://archive.apache.org/dist/tomcat/tomcat-6/v6.0.24/bin/apache-tomcat-6.0.24.tar.gz && \
    tar -zxf apache-tomcat-6.0.24.tar.gz -C /usr/local && \
    ln -s /usr/local/apache-tomcat-6.0.24 /usr/local/tomcat

# Clean Up
WORKDIR /root
RUN rm -rf /tmp/httpd-2.2.14 /tmp/httpd-2.2.14.tar.gz /tmp/mysql-5.1.41-linux-x86_64-glibc23.tar.gz /tmp/apache-tomcat-6.0.24.tar.gz

# Copy application data
COPY ./vm/etc/ /etc/
COPY ./vm/owaspbwa/ /owaspbwa
RUN ln -s /owaspbwa/owaspbwa-svn/var/www /var/www
RUN ln -s /owaspbwa/owaspbwa-svn/etc/apache2 /etc/apache2
RUN ln -s /owaspbwa/owaspbwa-svn/var/lib/mysql /var/lib/mysql


COPY ./run.sh /run.sh
EXPOSE 80
ENTRYPOINT /run.sh
