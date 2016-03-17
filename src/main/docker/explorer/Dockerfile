FROM tomcat:8-jre8
MAINTAINER Librairy <librairy.framework@gmail.com>

ADD api.war /usr/local/tomcat/webapps/api.war

VOLUME /usr/local/tomcat/logs

ENV LIBRAIRY_HOME /usr/local/tomcat

ENV COLUMN_DB_HOST column-db
ENV COLUMN_DB_PORT 9042

ENV DOCUMENT_DB_HOST document-db
ENV DOCUMENT_DB_PORT 9300

ENV GRAPH_DB_HOST graph-db
ENV GRAPH_DB_PORT 7474

ENV MESSAGE_BROKER_HOST event-bus
ENV MESSAGE_BROKER_PORT 5672

EXPOSE 8080
CMD ["catalina.sh", "run"]