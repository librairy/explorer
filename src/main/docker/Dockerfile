FROM tomcat:8-jre8
MAINTAINER Librairy <librairy.framework@gmail.com>

ADD api.war /usr/local/tomcat/webapps/api.war
RUN rm -rf /usr/local/tomcat/webapps/ROOT/
ADD drinventor/ /usr/local/tomcat/webapps/ROOT/
RUN ls -la /usr/local/tomcat/webapps/ROOT/*

ENV LIBRAIRY_HOME /librairy

RUN apt-get update && apt-get --fix-missing -y install build-essential python-dev libcurl4-nss-dev libsasl2-dev libsasl2-modules maven libapr1-dev libsvn-dev

ADD waitFor.sh /
RUN chmod +x /waitFor.sh

VOLUME /librairy
VOLUME /usr/local/tomcat

EXPOSE 8080
CMD ["catalina.sh", "run"]