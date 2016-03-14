FROM tomcat:8.0
MAINTAINER Carlos Badenes <cbadenes@gmail.com>

#docker run -it --rm -p 8080:8080 -v $(pwd)/sample:/usr/local/tomcat/webapps/sample tomcat:8.0
COPY ../../../target/api.war /usr/local/tomcat/webapps

EXPOSE 8080
CMD ["catalina.sh", "run"]